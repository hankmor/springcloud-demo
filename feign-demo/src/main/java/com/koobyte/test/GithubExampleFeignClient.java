package com.koobyte.test;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GithubExampleFeignClient {

    public interface GitHub {
        class Repository {
            String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        class Contributor {
            String login;

            public String getLogin() {
                return login;
            }

            public void setLogin(String login) {
                this.login = login;
            }
        }

        class Issue {
            Issue() {
            }

            String title;
            String body;
            List<String> assignees;
            int milestone;
            List<String> labels;
        }

        @RequestLine("GET /users/{username}/repos?sort=full_name")
        List<Repository> repos(@Param("username") String owner);

        @RequestLine("GET /repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);

        @RequestLine("POST /repos/{owner}/{repo}/issues")
        void createIssue(Issue issue, @Param("owner") String owner, @Param("repo") String repo);

        /**
         * Lists all contributors for all repos owned by a user.
         */
        default List<String> contributors(String owner) {
            return repos(owner).stream()
                    .flatMap(repo -> repo == null ? Stream.empty() : contributors(owner, repo.name).stream())
                    .map(c -> c.login)
                    .distinct()
                    .collect(Collectors.toList());
        }

        static GitHub connect() {
            // 解析器：解析请求结果为对象
            final Decoder decoder = new JacksonDecoder();
            // 编码器：编码对象为http body
            final Encoder encoder = new JacksonEncoder();
            // 错误解码器
            final GitHubErrorDecoder errorDecoder = new GitHubErrorDecoder(decoder);
            return Feign.builder()
                    .encoder(encoder)
                    .decoder(decoder)
                    .errorDecoder(errorDecoder)
                    .logger(new Logger.ErrorLogger())
                    .logLevel(Logger.Level.BASIC)
                    // 请求拦截器，添加请求Header信息
//                    .requestInterceptor(template -> {
//                        template.header(
//                                // not available when building PRs...
//                                // https://docs.travis-ci.com/user/environment-variables/#defining-encrypted-variables-in-travisyml
//                                "Authorization",
//                                "token 383f1c1b474d8f05a21e7964976ab0d403fee071");
//                    })
                    // 请求选项设置，如超时时间等
                    .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                    .target(GitHub.class, "https://api.github.com");
        }
    }

    /**
     * 异常类
     */
    static class GitHubClientError extends RuntimeException {
        private String message; // parsed from json

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static void main(String... args) {
        final GitHub github = GitHub.connect();

        System.out.println("Let's fetch and print a list of the repositories for koobyte.");
        List<GitHub.Repository> repos = github.repos("koobyte");
        for (GitHub.Repository repo : repos) {
            System.out.println(repo.name);
        }

        System.out.println("Let's fetch and print a list of the contributors to this org.");
        final List<String> contributors = github.contributors("koobyte");
        for (final String contributor : contributors) {
            System.out.println(contributor);
        }

        System.out.println("Now, let's cause an error.");
        try {
            github.contributors("koobyte", "some-unknown-project");
        } catch (final GitHubClientError e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Now, try to create an issue - which will also cause an error.");
        try {
            final GitHub.Issue issue = new GitHub.Issue();
            issue.title = "The title";
            issue.body = "Some Text";
            github.createIssue(issue, "koobyte", "SomeRepo");
        } catch (final GitHubClientError e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 错误解析器
     */
    static class GitHubErrorDecoder implements ErrorDecoder {
        final Decoder decoder;
        final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

        GitHubErrorDecoder(Decoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public Exception decode(String methodKey, Response response) {
            try {
                // must replace status by 200 other GSONDecoder returns null
                response = response.toBuilder().status(200).build();
                return (Exception) decoder.decode(response, GitHubClientError.class);
            } catch (final IOException fallbackToDefault) {
                return defaultDecoder.decode(methodKey, response);
            }
        }
    }
}