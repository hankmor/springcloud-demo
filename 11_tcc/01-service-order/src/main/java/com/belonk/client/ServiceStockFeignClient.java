package com.belonk.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by sun on 2019/9/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@FeignClient(name = "SERVICE-STOCK")
public interface ServiceStockFeignClient {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constants/Initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interfaces
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    // @PostMapping("/stock/reduce/prepare")
    // Map<String, Object> prepareReduce(@RequestParam("productId") Long productId, @RequestParam("stockNumber") Integer stockNumber);
    //
    // @PostMapping("/stock/reduce/confirm")
    // Map<String, Object> confirmReduce(@RequestParam("productId") Long productId);
    //
    // @PostMapping("/stock/reduce/cancel")
    // Map<String, Object> cancelReduce(@RequestParam("productId") Long productId);

    @PostMapping("/stock/reduce")
    Map<String, Object> reduce(@RequestParam("productId") Long productId, @RequestParam("stockNumber") Integer stockNumber);
}
