package com.tcc.transaction.api.redpacket;

import java.math.BigDecimal;

public interface RedPacketAccountServiceApi {

    BigDecimal getRedPacketAccountByUserId(long userId);
}
