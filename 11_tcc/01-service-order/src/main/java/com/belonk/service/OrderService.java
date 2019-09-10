package com.belonk.service;

import com.belonk.client.ServicePointFeignClient;
import com.belonk.client.ServiceStockFeignClient;
import com.belonk.dao.OrderDao;
import com.belonk.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by sun on 2019/9/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
public class OrderService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(OrderService.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Resource
    private ServicePointFeignClient servicePointFeignClient;
    @Resource
    private ServiceStockFeignClient serviceStockFeignClient;
    @Resource
    private OrderDao orderDao;

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

    public Order create(Long productId, Integer buyNumber) {
        String orderNo = String.valueOf(System.currentTimeMillis());
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setProductId(productId);
        order.setBuyNumber(buyNumber);
        order.setStatus(Order.Status.CREATED);
        return orderDao.save(order);
    }

    public Order findByOrderNo(String orderNo) {
        return orderDao.findByOrderNo(orderNo);
    }

    // 不使用TCC时，一旦出错事务无法处理。

    /**
     * 订单支付完成
     * <p>
     * 先更改订单状态为已支付，然后扣减库存，再给购买用户加积分
     *
     * @deprecated 没有考虑分布式事务
     */
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public void paySuccessWithoutTcc(String orderNo) {
        log.info("update order status");
        // 查询订单
        Order order = this.findByOrderNo(orderNo);
        // 更改订单状态
        order.setStatus(Order.Status.PAYED);
        orderDao.save(order);

        log.info("Reducing stock.");
        // 扣减库存
        int buyNumber = 2;
        serviceStockFeignClient.reduce(buyNumber);

        log.info("Adding point.");
        // 添加积分
        servicePointFeignClient.add(buyNumber * 100);
    }
}