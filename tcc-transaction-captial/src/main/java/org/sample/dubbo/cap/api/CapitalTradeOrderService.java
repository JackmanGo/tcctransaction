package org.sample.dubbo.cap.api;


import api.TccTransactionContext;
import org.sample.dubbo.cap.api.dto.CapitalTradeOrderDto;

public interface CapitalTradeOrderService {
    String record(CapitalTradeOrderDto tradeOrderDto, TccTransactionContext context) throws RuntimeException;
}
