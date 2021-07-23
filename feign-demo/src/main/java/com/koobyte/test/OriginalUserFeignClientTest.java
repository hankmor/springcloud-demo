package com.koobyte.test;

import com.koobyte.domain.User;
import com.koobyte.domain.UserQuery;
import com.koobyte.service.CommonErrorDecoder;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

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
		OriginalUserFeignClient userFeignClient = OriginalUserFeignClient.connect();
		// 查询一个用户
		System.out.println("查询用户, id: " + 1L);
		User user = userFeignClient.queryUser(1L);
		System.out.println("查询结果：" + user);
		// 查询所有用户
		System.out.println("查询所有用户：");
		List<User> users = userFeignClient.queryAllUser();
		System.out.println("查询结果：");
		users.forEach(System.out::println);
		// 添加用户
		System.out.println("添加用户: ");
		Long id = userFeignClient.add(new User("嘻嘻", 20));
		System.out.println("添加后的用户id：" + id);
		user = userFeignClient.queryUser(id);
		System.out.println("添加了嘻嘻：" + user);
		// 修改用户
		System.out.println("修改用户: ");
		userFeignClient.update(new User(id, "哈哈", 20));
		user = userFeignClient.queryUser(id);
		System.out.println("嘻嘻被修改成了：" + user);
		// 删除用户
		id = userFeignClient.delete(id);
		System.out.println("哈哈被删除了：" + id);

		// 使用表单的方式来添加用户
		System.out.println("添加用户: ");
		id = userFeignClient.add("大山", 40);
		System.out.println("添加后的用户id: " + id);
		System.out.println("修改用户: ");
		userFeignClient.update(id, "大山", 38);
		System.out.println("大山的年龄被修改为38");

		// 条件查询
		System.out.println("条件查询: ");
		UserQuery query = new UserQuery();
		query.setMinAge(38);
		users = userFeignClient.query(query);
		System.out.println("查询结果");
		users.forEach(System.out::println);

		// Json条件查询
		System.out.println("条件查询: ");
		query = new UserQuery();
		query.setMaxAge(38);
		users = userFeignClient.queryByJson(query);
		System.out.println("查询结果");
		users.forEach(System.out::println);
	}

	// 原生Feign客户端
	interface OriginalUserFeignClient {
		@RequestLine("GET /user/{id}")
		User queryUser(@Param("id") Long id);

		@RequestLine("GET /user")
		List<User> queryAllUser();

		/*
		以下两个query方法用来测试传递查询对象进行GET查询请求。
		JDK的HttpURLConnection将带body的GET请求转为POST，所以报错：Request method 'POST' not supported
		1、如果是表单查询参数，可以使用feign提供的@QueryMap注解将查询对象转为表单提交格式的查询参数
		2、如果要支持带body格式的请求，那么默认的JDK HttpURLConnection不能支持，需要转换为如Apache HttpClient这类支持GET请求串Body的http库
		*/

		@RequestLine("GET /user/query")
		@Headers("Content-Type: application/x-www-form-urlencoded")
			// List<User> query(UserQuery query); // 错误：Request method 'POST' not supported
		List<User> query(@QueryMap UserQuery query);

		@RequestLine("GET /user/query/json")
		@Headers("Content-Type: application/json")
		List<User> queryByJson(UserQuery query); //  错误：Request method 'POST' not supported, 改为ApacheHttpClient后ok

		// json方式请求
		@RequestLine("POST /user")
		@Headers("Content-Type: application/json")
		Long add(User user);

		// 使用@Body定义模板，可以与@Param绑定
		@RequestLine("POST /user")
		@Headers("Content-Type: application/json")
		@Body("%7B\"name\": \"{name}\", \"age\": \"{age}\"%7D")
		Long add(@Param("name") String name, @Param("age") Integer age);

		// 表单方式请求
		@RequestLine("PUT /user")
		@Headers("Content-Type: application/x-www-form-urlencoded")
		void update(User user);

		// 使用@Body定义模板
		@RequestLine("PUT /user")
		@Headers("Content-Type: application/x-www-form-urlencoded")
		@Body("id={id}&name={name}&age={age}")
		void update(@Param("id") long id, @Param("name") String name, @Param("age") Integer age);

		@RequestLine("DELETE /user/{id}")
		Long delete(@Param("id") Long id);

		static OriginalUserFeignClient connect() {
			// json编码器
			Encoder jacksonEncoder = new JacksonEncoder();
			// 表单编码器
			// 支持json也支持表单提交参数，需要使用表单编码器包装json编码器
			// 底层通过contentType来判断是否能够处理，不能处理则交给代理的json编码器来解析
			Encoder encoder = new FormEncoder(jacksonEncoder);
			Decoder decoder = new JacksonDecoder();
			ErrorDecoder errorDecoder = new CommonErrorDecoder(decoder);
			return Feign.builder()
					.encoder(encoder)
					.decoder(decoder)
					.errorDecoder(errorDecoder)
					// 使用slf4j日志
					.logger(new Slf4jLogger())
					.logLevel(Logger.Level.BASIC)
					.client(new ApacheHttpClient()) // 将默认的HTTP客户端从HttpURLConnection改为ApacheHttpClient，以支持带body的get请求
					.target(OriginalUserFeignClient.class, "http://localhost:8080");
		}
	}
}