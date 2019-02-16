package com.belonk.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

/**
 * Created by sun on 2018/8/17.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
public class HelloHystrixService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * 断路器定义，当失败时会调用<code>fallbackMethod</code>提供的方法。
     * <p>
     * Spring Cloud会自动根据<code>@HystrixCommand</code>生成代理，使其连接到断路器上，
     * 断路器能够自己判断什么时候打开或者回路，或者失败之后做什么。
     *
     * @return string
     */
    @HystrixCommand(fallbackMethod = "helloFailed")
    public String hello() {
        throw new RuntimeException("some exception ...");
//        return "Hello hystrix";
    }

    /**
     * 回调方法，抛出异常时该方法将会被执行。
     *
     * @return 结果信息
     */
    public String helloFailed() {
        return "Hello request failed.";
    }

    //~ 主方法和回调方法签名必须一致，否则抛出FallbackDefinitionException异常

    @HystrixCommand(fallbackMethod = "helloFailed1")
    public String hello1(String name) {
        throw new RuntimeException("some exception1 ...");
//        return "Hello, " + name;
    }

    /*
     * 签名与主方法不一致
     * 抛出异常：FallbackDefinitionException: fallback method wasn't found: helloFailed1([class java.lang.String])
     */
    public String helloFailed1() {
        return "Hello1 request failed.";
    }

    //~ 获取回调的异常信息

    @HystrixCommand(fallbackMethod = "helloFailed2")
    public String hello2(String name) {
        throw new RuntimeException("some exception2");
//        return "Hello, " + name;
    }

    @HystrixCommand(fallbackMethod = "helloFailed3")
    public String helloFailed2(String name, Throwable e) {
        assert "some exception2".equals(e.getMessage());
        throw new RuntimeException("some exception3");
    }

    // @HystrixCommand
    public String helloFailed3(String name, Throwable e) {
        return "Hello2 request failed, exception : " + e.getMessage();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}