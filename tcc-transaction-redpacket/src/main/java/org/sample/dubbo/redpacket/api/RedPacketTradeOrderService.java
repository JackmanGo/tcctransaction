package org.sample.dubbo.redpacket.api;


import org.sample.dubbo.redpacket.api.dto.RedPacketTradeOrderDto;


public interface RedPacketTradeOrderService {

     String record(RedPacketTradeOrderDto tradeOrderDto) throws RuntimeException;
}
