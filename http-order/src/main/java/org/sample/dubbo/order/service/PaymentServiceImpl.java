package org.sample.dubbo.order.service;

import org.sample.dubbo.cap.api.CapitalTradeOrderService;
import org.sample.dubbo.cap.api.dto.CapitalTradeOrderDto;
import org.sample.dubbo.order.OrderApplication;
import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.repository.OrderRepository;
import org.sample.dubbo.redpacket.api.RedPacketTradeOrderService;
import org.sample.dubbo.redpacket.api.dto.RedPacketTradeOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;

@Service
public class PaymentServiceImpl {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    @Qualifier("captialTradeOrderService")
    CapitalTradeOrderService capitalTradeOrderService;
    @Autowired
    @Qualifier("redPacketTradeOrderService")
    RedPacketTradeOrderService redPacketTradeOrderService;
    @Autowired
    private OrderRepository orderRepository;


    //TODO 需要TCC分布式事务处理，保证订单，钱包，红包数据的一致性
    public void makePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) throws RuntimeException{


        //check if the order status is DRAFT, if no, means that another call makePayment for the same order happened, ignore this call makePayment.
        if (order.getStatus().equals("DRAFT")) {
            //更改订单的状态为：PAYING
            order.pay(redPacketPayAmount, capitalPayAmount);
            try {
                //更新订单的红包支付金额和余额直接金额
                orderRepository.updateOrder(order);
            } catch (OptimisticLockingFailureException e) {
                //ignore the concurrently update order exception, ensure idempotency.
            }
        }

        //RPC接口，创建钱包使用记录，并扣除钱包该订单使用金额
        String capResult = capitalTradeOrderService.record(buildCapitalTradeOrderDto(order));
        //RPC接口，创建红包使用记录，并扣除红包该订单使用金额
        String redResult = redPacketTradeOrderService.record(buildRedPacketTradeOrderDto(order));

        LOGGER.info("capital执行结果: ===> {}" + capResult);
        LOGGER.info("redResult执行结果:===> {}" + redResult);

    }

    //创建与余额rpc调用的参数
    private CapitalTradeOrderDto buildCapitalTradeOrderDto(Order order) {

        CapitalTradeOrderDto tradeOrderDto = new CapitalTradeOrderDto();
        tradeOrderDto.setAmount(order.getCapitalPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));

        return tradeOrderDto;
    }

    //创建与红包rpc调用的参数
    private RedPacketTradeOrderDto buildRedPacketTradeOrderDto(Order order) {
        RedPacketTradeOrderDto tradeOrderDto = new RedPacketTradeOrderDto();
        tradeOrderDto.setAmount(order.getRedPacketPayAmount());
        tradeOrderDto.setMerchantOrderNo(order.getMerchantOrderNo());
        tradeOrderDto.setSelfUserId(order.getPayerUserId());
        tradeOrderDto.setOppositeUserId(order.getPayeeUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getMerchantOrderNo()));

        return tradeOrderDto;
    }

}
