package org.sample.dubbo.order.service.test;

import org.sample.dubbo.order.dao.LockTestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * @author wangxi
 * @date 2019-12-04 22:34
 */
@Service
public class TransactionService {


    @Autowired
    private LockTestDao lockTestDao;

    @Transactional(isolation=Isolation.SERIALIZABLE)
    public void updateMoney(){

        lockTestDao.updateOrderMoney(143);
        lockTestDao.selectOrderMoney(143);
    }

    @Transactional
    public void selectMoney(){

        lockTestDao.selectOrderMoney(143);
        lockTestDao.updateOrderMoney(143);
    }
}
