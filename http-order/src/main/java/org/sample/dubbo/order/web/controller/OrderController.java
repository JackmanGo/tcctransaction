package org.sample.dubbo.order.web.controller;

import org.sample.dubbo.order.OrderApplication;
import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.entity.Product;
import org.sample.dubbo.order.repository.OrderRepository;
import org.sample.dubbo.order.repository.ProductRepository;
import org.sample.dubbo.order.repository.ShopRepository;
import org.sample.dubbo.order.service.AccountServiceImpl;
import org.sample.dubbo.order.service.PlaceOrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;

@Controller
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

    @Autowired
    ShopRepository repository;

    /**
     * 主页
     * @return
     */
    @GetMapping("/")
    public String index() {

        //测试Spring事务传播机制
        //repository.createFirst();
        return "index";
    }

    /**
     * 获取 商店 的 所有产品
     * @param userId 查询的用户id
     * @param shopId 店铺id
     * @return
     */
    @GetMapping(value = "/user/{userId}/shop/{shopId}")
    public ModelAndView getProductsInShop(@PathVariable Long userId,
                                          @PathVariable Long shopId) {

        LOGGER.info("userId ==>{}, shopId ===> {}", userId, shopId);

        List<Product> products = productRepository.findByShopId(shopId);

        LOGGER.info("products=>{}", products);

        ModelAndView mv = new ModelAndView("/shop");

        mv.addObject("products", products);
        mv.addObject("userId", userId);
        mv.addObject("shopId", shopId);

        return mv;
    }

    /**
     * 执行下单操作，查询用户的余额，红包余额
     * @param userId 下单用户id
     * @param shopId 下单对店铺
     * @param productId 下单对商品
     * @return
     */
    @GetMapping(value = "/user/{userId}/shop/{shopId}/product/{productId}/confirm")
    public ModelAndView productDetail(@PathVariable Long userId,
                                      @PathVariable Long shopId,
                                      @PathVariable Long productId) {

        LOGGER.info("userId ==>{}, shopId ===> {}, productId ===> {}", userId, shopId, productId);


        ModelAndView mv = new ModelAndView("product_detail");

        //查询资金余额 rpc接口
        mv.addObject("capitalAmount", accountService.getCapitalAccountByUserId(userId));
        //查询好包余额 rpc接口
        mv.addObject("redPacketAmount", accountService.getRedPacketAccountByUserId(userId));
        //查询商品详情
        mv.addObject("product", productRepository.findById(productId));

        mv.addObject("userId", userId);
        mv.addObject("shopId", shopId);

        return mv;
    }

    /**
     * 购买
     * @param redPacketPayAmount 使用红包的金额
     * @param shopId 店铺id
     * @param payerUserId 购买用户
     * @param productId 商品id
     * @return
     */
    @PostMapping(value = "/placeorder")
    public RedirectView placeOrder(@RequestParam Double redPacketPayAmount,
                                   @RequestParam Long shopId,
                                   @RequestParam Long payerUserId,
                                   @RequestParam Long productId) {

        LOGGER.info("redPacketPayAmount ==>{}, shopId ===> {}, payerUserId ===> {}, productId ===> {}",
                redPacketPayAmount, shopId, payerUserId, productId);

        if (BigDecimal.valueOf(redPacketPayAmount).compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidParameterException("invalid red packet amount :" + redPacketPayAmount);

        //创建订单获取订单号，并依次调用RPC来扣除红包和钱包使用的金额
        String merchantOrderNo = placeOrderService.placeOrder(payerUserId, shopId,
                productId, BigDecimal.valueOf(redPacketPayAmount));

        return new RedirectView("/payresult/" + merchantOrderNo);
    }

    /**
     *
     * @param merchantOrderNo 订单号
     * @return
     */
    @GetMapping(value = "/payresult/{merchantOrderNo}")
    public ModelAndView getPayResult(@PathVariable String merchantOrderNo) {

        ModelAndView mv = new ModelAndView("pay_success");

        String payResultTip = null;
        Order foundOrder = orderRepository.findByMerchantOrderNo(merchantOrderNo);

        //查询订单号的订单状态
        if ("CONFIRMED".equals(foundOrder.getStatus()))
            payResultTip = "支付成功";
        else if ("PAY_FAILED".equals(foundOrder.getStatus()))
            payResultTip = "支付失败";
        else
            payResultTip = "Unknown";

        mv.addObject("payResult", payResultTip);

        mv.addObject("capitalAmount", accountService.getCapitalAccountByUserId(foundOrder.getPayerUserId()));
        mv.addObject("redPacketAmount", accountService.getRedPacketAccountByUserId(foundOrder.getPayerUserId()));

        return mv;
    }


}
