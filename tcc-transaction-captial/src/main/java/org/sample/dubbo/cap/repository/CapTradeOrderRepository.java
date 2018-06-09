package org.sample.dubbo.cap.repository;


import org.sample.dubbo.cap.dao.CapTradeOrderDao;
import org.sample.dubbo.cap.entity.CapTradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CapTradeOrderRepository {

    @Autowired
    CapTradeOrderDao capTradeOrderDao;

    public void insert(CapTradeOrder capTradeOrder) {
        capTradeOrderDao.insert(capTradeOrder);
    }

    public void update(CapTradeOrder capTradeOrder) {
        capTradeOrder.updateVersion();
        int effectCount = capTradeOrderDao.update(capTradeOrder);
        if (effectCount < 1) {
            throw new RuntimeException("update trade order failed");
        }
    }

    public CapTradeOrder findByMerchantOrderNo(String merchantOrderNo) {
        return capTradeOrderDao.findByMerchantOrderNo(merchantOrderNo);
    }

}
