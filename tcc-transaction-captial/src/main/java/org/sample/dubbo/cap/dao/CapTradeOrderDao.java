package org.sample.dubbo.cap.dao;


import org.sample.dubbo.cap.entity.CapTradeOrder;


public interface CapTradeOrderDao {

    int insert(CapTradeOrder capTradeOrder);

    int update(CapTradeOrder capTradeOrder);

    CapTradeOrder findByMerchantOrderNo(String merchantOrderNo);
}
