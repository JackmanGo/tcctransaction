package org.sample.dubbo.redpacket.api;

import java.math.BigDecimal;

public interface RedPacketAccountService {

    BigDecimal getRedPacketAccountByUserId(long userId);
}
