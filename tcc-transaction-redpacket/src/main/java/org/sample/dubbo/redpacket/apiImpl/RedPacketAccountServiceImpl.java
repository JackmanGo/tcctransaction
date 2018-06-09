package org.sample.dubbo.redpacket.apiImpl;

import org.sample.dubbo.redpacket.api.RedPacketAccountService;
import org.sample.dubbo.redpacket.repository.RedPacketAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("redPacketAccountService")
public class RedPacketAccountServiceImpl implements RedPacketAccountService {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketAccountServiceImpl.class);

    @Autowired
    RedPacketAccountRepository redPacketAccountRepository;

    @Override
    public BigDecimal getRedPacketAccountByUserId(long userId) {

        LOGGER.info("获取红包余额服务,===> {}", userId);
        return redPacketAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}
