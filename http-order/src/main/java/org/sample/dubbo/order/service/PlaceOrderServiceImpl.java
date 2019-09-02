package org.sample.dubbo.order.service;

import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.entity.Product;
import org.sample.dubbo.order.repository.OrderRepository;
import org.sample.dubbo.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PlaceOrderServiceImpl {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentServiceImpl paymentService;

    /**
     *
     * @param payerUserId
     * @param productId
     * @param redPacketPayAmount
     * @return 订单号
     */
    public String placeOrder(long payerUserId, Long productId, BigDecimal redPacketPayAmount) throws RuntimeException{
        Product product = productRepository.findById(productId);

        //生成订单，此时订单的状态为默认 DRAFT
        Order order = orderRepository.createOrder(payerUserId, product.getOwnerUserId(), product.getPrice());

        //执行扣款。rpc即tcc事务在此时执行
        paymentService.makePayment(order, redPacketPayAmount, order.getProductAmount().subtract(redPacketPayAmount),null);

        return order.getOrderNo();
    }
}
