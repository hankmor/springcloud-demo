package com.belonk.api;

import com.belonk.entity.Stock;
import com.belonk.service.StockService;
import com.belonk.service.impl.CrudStockService;
import org.bytesoft.compensable.Compensable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by sun on 2019/9/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/stock")
@Compensable(interfaceClass = StockService.class, confirmableKey = "stockConfirmService", cancellableKey = "stockCancelService")
public class StockApi implements StockService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(StockApi.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Resource
    private CrudStockService stockService;

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
     * Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    // @PostMapping("/reduce/prepare")
    // public Map<String, Object> prepareReduce(Long productId, Integer stockNumber) {
    //     Stock stock = stockService.prepareReduce(productId, stockNumber);
    //     Map<String, Object> resultMap = new HashMap<>();
    //     resultMap.put("rtnCode", 0);
    //     resultMap.put("rtnMsg", "success");
    //     resultMap.put("rtnData", stock);
    //     return resultMap;
    // }
    //
    // @PostMapping("/reduce/confirm")
    // public Map<String, Object> confirmReduce(Long productId) {
    //     Stock stock = stockService.confirmReduce(productId);
    //     Map<String, Object> resultMap = new HashMap<>();
    //     resultMap.put("rtnCode", 0);
    //     resultMap.put("rtnMsg", "success");
    //     resultMap.put("rtnData", stock);
    //     return resultMap;
    // }
    //
    // @PostMapping("/reduce/cancel")
    // public Map<String, Object> cancelReduce(Long productId) {
    //     Stock stock = stockService.cancelReduce(productId);
    //     Map<String, Object> resultMap = new HashMap<>();
    //     resultMap.put("rtnCode", 0);
    //     resultMap.put("rtnMsg", "success");
    //     resultMap.put("rtnData", stock);
    //     return resultMap;
    // }

    @PostMapping("/reduce")
    @Transactional
    @Override
    public Stock reduce(Long productId, Integer stockNumber) {
        Stock stock = stockService.prepareReduce(productId, stockNumber);
        return stock;
    }
}