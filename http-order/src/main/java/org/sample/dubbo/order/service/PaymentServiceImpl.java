package org.sample.dubbo.order.service;

import api.TccTransaction;
import api.TccTransactionContext;
import org.sample.dubbo.cap.api.CapitalTradeOrderService;
import org.sample.dubbo.cap.api.dto.CapitalTradeOrderDto;
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

    @TccTransaction(confirmMethod="makePaymentConfirm", cancelMethod = "makePaymentCancel")
    public void makePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount, TccTransactionContext context) throws RuntimeException{

        LOGGER.info("makePayment context"+context);

        //检查order是否为DRAFT，如果不是，支付请求已被处理
        if ("DRAFT".equals(order.getStatus())) {
            //更改订单的状态为：PAYING
            order.pay(redPacketPayAmount, capitalPayAmount);
            try {
                //更新订单的红包支付金额和余额直接金额：预扣款买家账户金额操作（冻结资金）
                orderRepository.updateOrder(order);
            } catch (OptimisticLockingFailureException e) {
                //忽略更新状态失败
            }
        }

        //RPC接口，创建钱包使用记录，并扣除钱包该订单使用金额
        String capResult = capitalTradeOrderService.record(buildCapitalTradeOrderDto(order), null);
        int i = 10/0;
        //RPC接口，创建红包使用记录，并扣除红包该订单使用金额
        String redResult = redPacketTradeOrderService.record(buildRedPacketTradeOrderDto(order), null);

        LOGGER.info("capital执行结果: ===> {}" + capResult);
        LOGGER.info("redResult执行结果:===> {}" + redResult);

    }

    public void makePaymentConfirm(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount,  TccTransactionContext context) throws RuntimeException {

        LOGGER.info("order confirm make payment called. MerchantOrderNo===>{}", order.getMerchantOrderNo());

        Order foundOrder = orderRepository.findByMerchantOrderNo(order.getMerchantOrderNo());

        //check if the trade order status is PAYING, if no, means another call confirmMakePayment happened, return directly, ensure idempotency.
        if (foundOrder != null && foundOrder.getStatus().equals("PAYING")) {
            order.confirm();
            orderRepository.updateOrder(order);
        }
    }

    public void makePaymentCancel(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount,  TccTransactionContext context) throws RuntimeException {

        LOGGER.info("order cancel make payment called. MerchantOrderNo===>{}", order.getMerchantOrderNo());

        Order foundOrder = orderRepository.findByMerchantOrderNo(order.getMerchantOrderNo());

        //check if the trade order status is PAYING, if no, means another call confirmMakePayment happened, return directly, ensure idempotency.
        if (foundOrder != null && foundOrder.getStatus().equals("PAYING")) {
            order.cancelPayment();
            orderRepository.updateOrder(order);
        }

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
