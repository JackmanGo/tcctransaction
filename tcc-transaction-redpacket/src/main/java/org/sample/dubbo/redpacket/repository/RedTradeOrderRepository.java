package org.sample.dubbo.redpacket.repository;

import org.sample.dubbo.redpacket.dao.RedTradeOrderDao;
import org.sample.dubbo.redpacket.entity.RedTradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;


@Repository
public class RedTradeOrderRepository {

    @Autowired
    RedTradeOrderDao tradeOrderDao;

    public void insert(RedTradeOrder redTradeOrder) {
        tradeOrderDao.insert(redTradeOrder);
    }

    public void update(RedTradeOrder redTradeOrder) {

        redTradeOrder.updateVersion();
        int effectCount = tradeOrderDao.update(redTradeOrder);
        if (effectCount < 1) {
            throw new OptimisticLockingFailureException("update trade order failed");
        }
    }

    public RedTradeOrder findByMerchantOrderNo(String merchantOrderNo) {
        return tradeOrderDao.findByMerchantOrderNo(merchantOrderNo);
    }

}
