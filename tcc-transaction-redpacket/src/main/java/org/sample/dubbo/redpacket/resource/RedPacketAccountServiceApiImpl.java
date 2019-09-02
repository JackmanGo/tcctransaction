package org.sample.dubbo.redpacket.resource;

import com.tcc.transaction.api.redpacket.RedPacketAccountServiceApi;
import org.apache.dubbo.config.annotation.Service;
import org.sample.dubbo.redpacket.repository.RedPacketAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Service
public class RedPacketAccountServiceApiImpl implements RedPacketAccountServiceApi {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketAccountServiceApiImpl.class);

    @Autowired
    RedPacketAccountRepository redPacketAccountRepository;

    @Override
    public BigDecimal getRedPacketAccountByUserId(long userId) {

        LOGGER.info("获取红包余额服务,===> {}", userId);
        return redPacketAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}
