package com.belonk.web;

import com.belonk.service.HelloClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by sun on 2021/7/22.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
@RestController
@RequestMapping
public class HelloConsumer {
	//~ Static fields/constants/initializer


	//~ Instance fields

	@Resource
	private HelloClient helloClient;

	//~ Constructors


	//~ Methods

	@GetMapping("/hello")
	public String hello(String name) {
		return helloClient.hello(name);
	}
}