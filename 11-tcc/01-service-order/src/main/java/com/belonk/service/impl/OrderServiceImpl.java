package com.belonk.service.impl;

import com.belonk.dao.OrderDao;
import com.belonk.entity.Order;
import com.belonk.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Random;

/**
 * Created by sun on 2019/9/11.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Resource
    private OrderDao orderDao;
    private static Random random = new Random();

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

    @Override
    @Transactional
    public Order create(Long userId, Long productId, Integer buyNumber) {
        String orderNo = String.valueOf(System.currentTimeMillis());
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setProductId(productId);
        order.setUserId(userId);
        order.setBuyNumber(buyNumber);
        order.setStatus(Order.Status.CREATED);
        order = orderDao.save(order);
        int r = random.nextInt(10);
        // 非compensable事务，抛出异常回滚
        if (r / 2 == 0) {
            throw new RuntimeException("Order create failed.");
        }
        return order;
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        return orderDao.findByOrderNo(orderNo);
    }

    // 不使用TCC时，一旦出错事务无法处理。

    // /**
    //  * 订单支付完成
    //  * <p>
    //  * 先更改订单状态为已支付，然后扣减库存，再给购买用户加积分
    //  */
    // @Transactional(rollbackFor = Exception.class)
    // public void paySuccessWithoutTcc(String orderNo) {
    //     log.info("update order status");
    //     // 查询订单
    //     Order order = this.findByOrderNo(orderNo);
    //     // 更改订单状态
    //     order.setStatus(Order.Status.PAYED);
    //     orderDao.save(order);
    //
    //     log.info("Reducing stock.");
    //     // 扣减库存
    //     int buyNumber = 2;
    //     省略扣减库存代码
    //
    //     log.info("Adding point.");
    //     // 添加积分
    //    省略添加积分代码
    // }
}