package org.sample.dubbo.redpacket.api;


import api.TccTransactionContext;
import org.sample.dubbo.redpacket.api.dto.RedPacketTradeOrderDto;


public interface RedPacketTradeOrderService {

     String record(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context) throws RuntimeException;
}
