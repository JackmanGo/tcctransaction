package org.sample.dubbo.order.service;

import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.entity.Shop;
import org.sample.dubbo.order.repository.OrderRepository;
import org.sample.dubbo.order.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PlaceOrderServiceImpl {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentServiceImpl paymentService;

    /**
     *
     * @param payerUserId
     * @param shopId
     * @param productId
     * @param redPacketPayAmount
     * @return 订单号
     */
    public String placeOrder(long payerUserId, long shopId, Long productId, BigDecimal redPacketPayAmount) {
        Shop shop = shopRepository.findById(shopId);

        //生成订单，此时订单的状态为默认 DRAFT
        Order order = orderRepository.createOrder(payerUserId, shop.getOwnerUserId(), productId);

        Boolean result = false;

        //执行扣款
        paymentService.makePayment(order, redPacketPayAmount, order.getTotalAmount().subtract(redPacketPayAmount));

        return order.getMerchantOrderNo();
    }
}
