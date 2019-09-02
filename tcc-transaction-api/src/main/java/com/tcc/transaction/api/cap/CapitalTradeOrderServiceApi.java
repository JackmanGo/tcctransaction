package com.tcc.transaction.api.cap;


import api.TccTransactionContext;
import com.tcc.transaction.api.cap.dto.CapitalTradeOrderDto;

public interface CapitalTradeOrderServiceApi {

    String record(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context) throws RuntimeException;
    void confirmRecord(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context);
    void cancelRecord(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context) throws Exception;

}
