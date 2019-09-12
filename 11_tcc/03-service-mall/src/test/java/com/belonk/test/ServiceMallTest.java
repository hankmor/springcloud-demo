package com.belonk.test;

import com.belonk.entity.Product;
import com.belonk.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sun on 2019/9/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceMallTest {
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
    private WebApplicationContext wac;
    MockMvc mockMvc;

    @Resource
    private ProductService productService;

    private Random random = new Random();

    private static Long iPhoneId = 1L;
    private static Long iPadId = 2L;

    private static Long[] productIds = {iPadId, iPhoneId};
    private static Long userId = 1L;

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

    @Before
    public void test() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysExpect(status().isOk())
                .build();
    }


    /**
     * 准备测试数据
     */
    @Test
    public void testCreateProduct() {
        String name = "iPhone";
        double price = 5000d;
        Product product = productService.create(name, price);
        System.out.println("iPhone id : " + product.getId());

        name = "iPad";
        price = 3000d;
        product = productService.create(name, price);
        System.out.println("iPad id : " + product.getId());
    }

    @Test
    public void testBuySomething() throws Exception {
        Long productId = productIds[random.nextInt(productIds.length)];
        int buyNumber = random.nextInt(10) + 1;

        // 下单购买
        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/product/buy")
                        .param("userId", String.valueOf(userId))
                        .param("productId", String.valueOf(productId))
                        .param("buyNumber", String.valueOf(buyNumber))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().isOk());
        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(result, Map.class);
        Map orderMap = (Map) map.get("rtnData");
        String orderNo = (String) orderMap.get("orderNo");
        Assert.assertTrue(orderNo != null && !"".equals(orderNo));

        // 订单支付成功
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/product/pay")
                        .param("orderNo", orderNo)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().isOk());
    }

    @Test
    public void testPay() throws Exception {
        String orderNo = "1568277244427";
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/product/pay")
                        .param("orderNo", orderNo)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().isOk());
    }
}