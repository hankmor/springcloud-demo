package com.belonk.service.impl;

import com.belonk.dao.OrderDao;
import com.belonk.entity.Order;
import com.belonk.service.OrderTccService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by sun on 2019/9/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Service
public class OrderCancelService implements OrderTccService {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(OrderCancelService.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Resource
    private OrderDao orderDao;
    private Random random = new Random();

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

    /**
     * 1） 全局事务决定回滚时，分支事务中可补偿型service的cancel逻辑不一定会被执行，原因是：参与该分支事务的Try方法可能抛出异常导致其
     * 本地事务回滚，因此该服务的Try操作是没有生效的；
     * <br/>
     * 2） 全局事务决定回滚时，主事务中可补偿型service的cancel逻辑并不一定会被执行；原因是：主事务控制着全局事务的最终完成方向，当其最
     * 终决定回滚全局事务时，有机会通过将自己本地Try阶段的事务直接rollback来完成撤销try阶段操作，而不必通过cancel逻辑来实现。
     * <br/>
     * 3） cancel阶段仅负责本service的cancel逻辑，而不应该再执行远程调用。如果try阶段调用过远程服务，则事务上下文已传播至远程节点，全
     * 局事务回滚时，将由其所在节点的事务管理器负责执行cancel逻辑。
     * <br/>
     *
     * @param orderNo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order paySuccess(String orderNo) {
        System.err.println("========> 执行了OrderCancelService的paySuccess");
        Order order = orderDao.findByOrderNo(orderNo);
        // 还原状态
        order.setStatus(Order.Status.CREATED);
        int r = random.nextInt(10);
        if (r / 3 == 0) {
            throw new RuntimeException("Business process failed.");
        }
        orderDao.save(order);
        return order;
    }
}