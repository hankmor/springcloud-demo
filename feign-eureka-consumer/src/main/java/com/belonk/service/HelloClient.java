package com.belonk.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by sun on 2021/7/22.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
// name：客户端名字，必须定义
// url：指定具体的请求地址
@FeignClient(name = "hello-service", url = "http://localhost:8082")
public interface HelloClient {
	//~ Constants/Initializer


	//~ Interfaces

	// Get请求接口，name参数必须要能够获取到，可以添加@RequestParam注解，或者使用@PathVariable注解
	@GetMapping("/hello")
	String hello(@RequestParam String name);
}
