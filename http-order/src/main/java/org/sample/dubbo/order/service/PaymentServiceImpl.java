package org.sample.dubbo.order.service;

import api.TccTransaction;
import api.TccTransactionContext;
import com.tcc.transaction.api.cap.dto.CapitalTradeOrderDto;
import com.tcc.transaction.api.redpacket.dto.RedPacketTradeOrderDto;
import org.sample.dubbo.order.common.OrderStatus;
import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.proxy.OrderRpcServiceProxy;
import org.sample.dubbo.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    private OrderRpcServiceProxy rpcServiceProxy;
    @Autowired
    private OrderRepository orderRepository;

    @TccTransaction(confirmMethod = "makePaymentConfirm", cancelMethod = "makePaymentCancel")
    @Transactional(rollbackFor = Throwable.class)
    public void makePayment(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount, TccTransactionContext context) throws RuntimeException {

        LOGGER.info("makePayment context" + context);

        //更新订单的红包支付金额和余额直接金额：预扣款买家账户金额操作（冻结资金）
        order.pay(redPacketPayAmount, capitalPayAmount);
        orderRepository.updateOrder(order);


        //RPC接口，创建钱包使用记录，并扣除钱包该订单使用金额
        String capResult = rpcServiceProxy.capitalRecord(buildCapitalTradeOrderDto(order), context);

        //RPC接口，创建红包使用记录，并扣除红包该订单使用金额
        if(order.getRedPacketAmount().compareTo(BigDecimal.ZERO)>0) {
            String redResult = rpcServiceProxy.redPacketRecord(buildRedPacketTradeOrderDto(order), context);
            LOGGER.info("redResult执行结果:===> {}" + redResult);
        }
        LOGGER.info("capital执行结果: ===> {}" + capResult);
    }

    /**
     * tcc事务确认
     * @param order
     * @param redPacketPayAmount
     * @param capitalPayAmount
     * @param context
     * @throws RuntimeException
     */
    public void makePaymentConfirm(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount, TccTransactionContext context) throws RuntimeException {

        LOGGER.info("order confirm make payment called. orderNo===>{}", order.getOrderNo());

        Order foundOrder = orderRepository.findByMerchantOrderNo(order.getOrderNo());

        //check if the trade order status is PAYING, if no, means another call confirmMakePayment happened, return directly, ensure idempotency.
        if (foundOrder != null && foundOrder.getStatus().equals(OrderStatus.START)) {
            order.confirm();
            orderRepository.updateOrder(order);
        }
    }

    /**
     * tcc事务回滚
     * @param order
     * @param redPacketPayAmount
     * @param capitalPayAmount
     * @param context
     * @throws RuntimeException
     */
    public void makePaymentCancel(Order order, BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount, TccTransactionContext context) throws RuntimeException {

        LOGGER.info("order cancel make payment called. MerchantOrderNo===>{}", order.getOrderNo());

        Order foundOrder = orderRepository.findByMerchantOrderNo(order.getOrderNo());

        //check if the trade order status is PAYING, if no, means another call confirmMakePayment happened, return directly, ensure idempotency.
        if (foundOrder != null && foundOrder.getStatus().equals(OrderStatus.START)) {
            order.cancelPayment();
            orderRepository.updateOrder(order);
        }

    }


    //创建与余额rpc调用的参数
    private CapitalTradeOrderDto buildCapitalTradeOrderDto(Order order) {

        CapitalTradeOrderDto tradeOrderDto = new CapitalTradeOrderDto();
        tradeOrderDto.setAmount(order.getCapitalAmount());
        tradeOrderDto.setMerchantOrderNo(order.getOrderNo());
        tradeOrderDto.setSelfUserId(order.getBuyerUserId());
        tradeOrderDto.setOppositeUserId(order.getSellerUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getOrderNo()));

        return tradeOrderDto;
    }

    //创建与红包rpc调用的参数
    private RedPacketTradeOrderDto buildRedPacketTradeOrderDto(Order order) {
        RedPacketTradeOrderDto tradeOrderDto = new RedPacketTradeOrderDto();
        tradeOrderDto.setAmount(order.getRedPacketAmount());
        tradeOrderDto.setMerchantOrderNo(order.getOrderNo());
        tradeOrderDto.setSelfUserId(order.getBuyerUserId());
        tradeOrderDto.setOppositeUserId(order.getSellerUserId());
        tradeOrderDto.setOrderTitle(String.format("order no:%s", order.getOrderNo()));

        return tradeOrderDto;
    }

}
