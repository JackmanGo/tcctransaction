package org.sample.dubbo.order.web.controller;

import org.sample.dubbo.order.OrderApplication;
import org.sample.dubbo.order.common.OrderStatus;
import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.entity.Product;
import org.sample.dubbo.order.repository.OrderRepository;
import org.sample.dubbo.order.repository.ProductRepository;
import org.sample.dubbo.order.service.AccountServiceImpl;
import org.sample.dubbo.order.service.PlaceOrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderApplication.class);

    @Autowired
    PlaceOrderServiceImpl placeOrderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    OrderRepository orderRepository;


    /**
     * 获取 商店 的 所有产品
     * @return
     */
    @GetMapping(value = "/products")
    public Map getProductsInShop() {


        List<Product> products = productRepository.findAllProduct();

        LOGGER.info("products=>{}", products);

        Map mv = new HashMap(3);

        mv.put("products", products);
        mv.put("userId", 2000);

        return mv;
    }

    /**
     * 执行下单操作，查询用户的余额，红包余额
     * @param productId 下单对商品
     * @return
     */
    @GetMapping(value = "/product/{productId}")
    public Map productDetail(@PathVariable Long productId) {

        LOGGER.info("userId ==>{},  productId ===> {}", 2000, productId);


        Map mv = new HashMap<>(5);

        //查询资金余额 rpc接口
        mv.put("capitalAmount", accountService.getCapitalAccountByUserId(2000));
        //查询好包余额 rpc接口
        mv.put("redPacketAmount", accountService.getRedPacketAccountByUserId(2000));
        //查询商品详情
        mv.put("product", productRepository.findById(productId));

        mv.put("userId", 2000);

        return mv;
    }

    /**
     * 购买
     * @param redPacketPayAmount 使用红包的金额
     * @param productId 商品id
     * @return
     */
    @GetMapping(value = "/buy/{productId}")
    public RedirectView placeOrder(@PathVariable Long productId, @RequestParam(required = false, defaultValue = "0") Double redPacketPayAmount) {

        LOGGER.info("redPacketPayAmount ==>{}, payerUserId ===> {}, productId ===> {}",
                redPacketPayAmount,2000, productId);

        if (BigDecimal.valueOf(redPacketPayAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("invalid red packet amount :" + redPacketPayAmount);
        }

        //创建订单获取订单号，并依次调用RPC来扣除红包和钱包使用的金额
        String orderNo = placeOrderService.placeOrder(2000,
                productId, BigDecimal.valueOf(redPacketPayAmount));

        LOGGER.info("orderNo ==>{}", orderNo);
        return new RedirectView("/payresult/" + orderNo);
    }

    /**
     *
     * @param merchantOrderNo 订单号
     * @return
     */
    @GetMapping(value = "/payresult/{merchantOrderNo}")
    public Map getPayResult(@PathVariable String merchantOrderNo) {

        Map mv = new HashMap(3);

        String payResultTip = null;
        Order foundOrder = orderRepository.findByMerchantOrderNo(merchantOrderNo);

        //查询订单号的订单状态
        if (OrderStatus.CONFIRM.equals(foundOrder.getStatus())) {
            payResultTip = "支付成功";
        }
        else if (OrderStatus.CONCEL.equals(foundOrder.getStatus())) {
            payResultTip = "支付失败";
        }
        else {
            payResultTip = "Unknown";
        }

        mv.put("payResult", payResultTip);

        mv.put("capitalAmount", accountService.getCapitalAccountByUserId(foundOrder.getBuyerUserId()));
        mv.put("redPacketAmount", accountService.getRedPacketAccountByUserId(foundOrder.getBuyerUserId()));

        return mv;
    }


}
