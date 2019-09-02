package org.sample.dubbo.order.proxy;

import api.TccTransaction;
import api.TccTransactionContext;
import com.tcc.transaction.api.cap.CapitalTradeOrderServiceApi;
import com.tcc.transaction.api.cap.dto.CapitalTradeOrderDto;
import com.tcc.transaction.api.redpacket.RedPacketTradeOrderServiceApi;
import com.tcc.transaction.api.redpacket.dto.RedPacketTradeOrderDto;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * rpc调用红包，余额也需要在tcc事务中进行，故此需要在代理中进行开启
 *
 * 为什么一定需要该代理？
 * 假如没有代理存在，在tcc根事务中直接调用rpc的方法，在rpc服务提供着会直接开启分支事务
 *
 * tcc事务在完成TRYING状态后，仅有根事务会根据AOP中配置的事务参与者依次调用confirm或cancel
 * 而分支事务并不会调用（因为分支事务的成功并不代表整个事务的成功）。
 *
 * 而在RPC调用过程中，如果根事务中直接通过rpc调用，并在rpc服务提供着中开启分支事务。这时，该分支事务的
 * confirm和cancel等事务参与者信息是无法被根事务所获取的从而写入根事务的事务上下文（因为rpc的调用无法被当前环境的AOP所拦截）
 *
 * 所以为导致的情况就是当事务完成后，根事务的tcc事务会根据事务上下文相应的调用confirm或cancel。
 * 而分支事务则无法调用confirm或cancel。
 *
 * 因为需要把分支事务的参与者信息也写入根事务的事务上下文中。根事务会根据当前事务情况调用confirm或cancel。
 * 提供AOP的兼容即分支事务的AOP对当前拦截到的请求的事务上下文状态做判断，可在分支事务AOP中完成对分支事务的confirm或cancel的调用
 *
 */
@Component
public class OrderRpcServiceProxy {

    @Reference
    private CapitalTradeOrderServiceApi capitalTradeOrderService;
    @Reference
    private RedPacketTradeOrderServiceApi redPacketTradeOrderService;


    /**
     *  RPC接口，创建钱包使用记录，并扣除钱包该订单使用金额
     */
    @TccTransaction(confirmMethod = "capitalRecord", cancelMethod = "capitalRecord")
    public String capitalRecord(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context){

        String capResult = capitalTradeOrderService.record(tradeOrderDto, context);
        return capResult;
    }

    /**
     *  RPC接口，创建红包使用记录，并扣除红包该订单使用金额
     */
    @TccTransaction(confirmMethod = "redPacketRecord", cancelMethod = "redPacketRecord")
    public String redPacketRecord(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context){

        String redResult = redPacketTradeOrderService.record(tradeOrderDto, context);
        return redResult;
    }

}
