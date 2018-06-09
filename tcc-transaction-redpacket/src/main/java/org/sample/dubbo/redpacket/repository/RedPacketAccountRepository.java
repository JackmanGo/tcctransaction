package org.sample.dubbo.redpacket.repository;


import org.sample.dubbo.redpacket.dao.RedPacketAccountDao;
import org.sample.dubbo.redpacket.entity.RedPacketAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class RedPacketAccountRepository {

    @Autowired
    RedPacketAccountDao redPacketAccountDao;

    public RedPacketAccount findByUserId(long userId) {

        return redPacketAccountDao.findByUserId(userId);
    }

    public void save(RedPacketAccount redPacketAccount) {
        int effectCount = redPacketAccountDao.update(redPacketAccount);
        if (effectCount < 1) {
            throw new RuntimeException();
        }
    }
}
