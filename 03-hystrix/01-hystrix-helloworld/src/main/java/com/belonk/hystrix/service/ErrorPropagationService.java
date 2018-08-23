package com.belonk.hystrix.service;

import com.belonk.hystrix.exception.BadRequestException;
import com.belonk.hystrix.exception.BusinessException;
import com.belonk.hystrix.exception.OtherException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import org.springframework.stereotype.Service;

/**
 * Created by sun on 2018/8/20.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
//@DefaultProperties(defaultFallback = "fallback")
public class ErrorPropagationService {
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

    //~ 异常传递

    // 执行回调
    @HystrixCommand(fallbackMethod = "fallback")
    public String errorPropagation0() {
        throw new BusinessException("errorPropagation0 exception");
    }

    // 抛出的是HystrixBadRequestException类型的异常，不会触发回调
    @HystrixCommand(fallbackMethod = "fallback")
    public String errorPropagation0_1() {
        throw new BadRequestException("errorPropagation0-1 exception");
    }

    // ignoreExceptions: 被忽略的异常，即：抛出这些异常，并不触发回调方法，而是直接向外层抛出
    @HystrixCommand(ignoreExceptions = {BusinessException.class}, fallbackMethod = "fallback")
    public String errorPropagation1() {
        throw new BusinessException("errorPropagation1 exception");
    }

    // raiseHystrixExceptions：没有被忽略的异常，将会被包装为HystrixRuntimeException
    // TODO HystrixRuntimeException有什么用？？？测试没有任何作用？？？
    @HystrixCommand(ignoreExceptions = {BadRequestException.class},
            raiseHystrixExceptions = {HystrixException.RUNTIME_EXCEPTION},
            fallbackMethod = "errorPropagationFallback")
    public String errorPropagation2() {
        // 未被忽略的BusinessException，被包装为HystrixRuntimeException
        throw new BusinessException("errorPropagation2 exception");
    }

    @HystrixCommand(fallbackMethod = "fallbackWithOtherException")
    public String errorPropagation3() throws BusinessException {
        throw new BusinessException("errorPropagation3 exception");
    }

    // 全局回调
    private String fallback(Throwable e) {
        System.out.println("exception info : " + e);
        return "Server is busy, exception : " + e.getMessage();
    }

    // 单独回调方法
    String errorPropagationFallback(Throwable e) {
        System.out.println("cause : " + e.getCause());
        return ("fallback method exception");
    }

    @HystrixCommand
    public String fallbackWithOtherException() throws OtherException {
        throw new OtherException("other exception");
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}