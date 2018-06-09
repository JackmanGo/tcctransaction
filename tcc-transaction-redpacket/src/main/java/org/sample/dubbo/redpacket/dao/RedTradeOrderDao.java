package org.sample.dubbo.redpacket.dao;


import org.sample.dubbo.redpacket.entity.RedTradeOrder;

public interface RedTradeOrderDao {

    void insert(RedTradeOrder tradeOrder);

    int update(RedTradeOrder tradeOrder);

    RedTradeOrder findByMerchantOrderNo(String merchantOrderNo);
}
