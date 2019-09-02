package com.tcc.transaction.api.redpacket;


import api.TccTransactionContext;
import com.tcc.transaction.api.redpacket.dto.RedPacketTradeOrderDto;


public interface RedPacketTradeOrderServiceApi {

     String record(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context) throws RuntimeException;
     void confirmRecord(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context);
     void cancelRecord(RedPacketTradeOrderDto tradeOrderDto, TccTransactionContext context);
}
