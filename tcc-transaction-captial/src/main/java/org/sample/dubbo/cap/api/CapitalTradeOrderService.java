package org.sample.dubbo.cap.api;


import org.sample.dubbo.cap.api.dto.CapitalTradeOrderDto;

public interface CapitalTradeOrderService {
    String record(CapitalTradeOrderDto tradeOrderDto) throws RuntimeException;
}
