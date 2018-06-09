package org.sample.dubbo.cap.repository;

import org.sample.dubbo.cap.dao.CapitalAccountDao;
import org.sample.dubbo.cap.entity.CapitalAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class CapitalAccountRepository {

    @Autowired
    CapitalAccountDao capitalAccountDao;

    public CapitalAccount findByUserId(long userId) {

        return capitalAccountDao.findByUserId(userId);
    }

    public void save(CapitalAccount capitalAccount) throws Exception {
        int effectCount = capitalAccountDao.update(capitalAccount);
        if (effectCount < 1) {
            throw new RuntimeException("更新失败");
        }
    }
}
