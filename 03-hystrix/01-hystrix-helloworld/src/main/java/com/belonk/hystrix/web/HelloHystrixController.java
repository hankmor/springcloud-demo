package com.belonk.hystrix.web;

import com.belonk.hystrix.service.ErrorPropagationService;
import com.belonk.hystrix.service.FallbackDemoService;
import com.belonk.hystrix.service.HelloHystrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sun on 2018/8/17.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@RestController
public class HelloHystrixController {
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

    @Autowired
    private HelloHystrixService hystrixService;
    @Autowired
    private FallbackDemoService fallbackDemoService;
    @Autowired
    private ErrorPropagationService errorPropagationService;

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

    @GetMapping("/hello")
    public String hello() {
        return hystrixService.hello();
    }

    @GetMapping("/hello1")
    public String hello1(String name) {
        return hystrixService.hello1(name);
    }

    @GetMapping("/hello2")
    public String hello2(String name) {
        return hystrixService.hello2(name);
    }

    //~ 默认fallback测试

    @GetMapping("/test1")
    public String test1() {
        return fallbackDemoService.test1();
    }

    @GetMapping("/test2")
    public String test2() {
        return fallbackDemoService.test2("test2");
    }

    @GetMapping("/test3")
    public String test3() {
        return fallbackDemoService.test3(100L);
    }

    //~ 异常传递测试

    @GetMapping("/ep0")
    public String errorPropagation0() {
        return errorPropagationService.errorPropagation0();
    }

    @GetMapping("/ep0_1")
    public String errorPropagation0_1() {
        return errorPropagationService.errorPropagation0_1();
    }

    @GetMapping("/ep1")
    public String errorPropagation1() {
        // 抛出异常信息，回调方法没有被执行
        return errorPropagationService.errorPropagation1();
    }

    @GetMapping("/ep2")
    public String errorPropagation2() {
        return errorPropagationService.errorPropagation2();
    }

    @GetMapping("/ep3")
    public String errorPropagation3() {
        try {
            return errorPropagationService.errorPropagation3();
        } catch (Exception e) {
            // TODO 官方文档javanic说这里应该得到BusinessException，而实际上是OtherException？？
            System.out.println("class : " + e.getClass());
        }
        return null;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}