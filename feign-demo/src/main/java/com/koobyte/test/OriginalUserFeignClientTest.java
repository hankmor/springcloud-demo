package com.koobyte.test;

import com.koobyte.domain.User;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * Created by sun on 2021/7/22.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class OriginalUserFeignClientTest {
    //~ Static fields/constants/initializer


    //~ Instance fields


    //~ Constructors


    //~ Methods

    public static void main(String[] args) {

    }

    // 原生Feign客户端
    interface UserFeignClient {
        @RequestLine("GET /user/{id}")
        User queryUser(@Param("id") Long id);

        @RequestLine("GET /user")
        List<User> queryAllUser();

        @RequestLine("POST /user")
        @Headers("Content-Type: application/json")
        Long add(User user);

        @RequestLine("PUT /user")
        void update(User user);

        @RequestLine("DELETE /user/{id}")
        Long delete(@Param("id") Long id);
    }
}