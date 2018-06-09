package org.sample.dubbo.redpacket.dao;

import org.sample.dubbo.redpacket.entity.RedPacketAccount;

public interface RedPacketAccountDao {

    RedPacketAccount findByUserId(long userId);

    int update(RedPacketAccount redPacketAccount);
}
