package org.sample.dubbo.order.service;
import org.sample.dubbo.cap.api.CapitalAccountService;
import org.sample.dubbo.order.OrderApplication;
import org.sample.dubbo.redpacket.api.RedPacketAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("accountService")
public class AccountServiceImpl {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderApplication.class);

    @Autowired
    RedPacketAccountService redPacketAccountService;

    @Autowired
    CapitalAccountService capitalAccountService;


    /**
     * 调用红包服务，获取红包金额余额
     * @param userId
     * @return
     */
    public BigDecimal getRedPacketAccountByUserId(long userId){

        LOGGER.info("调用红包服务 ===>{}", userId);
        return redPacketAccountService.getRedPacketAccountByUserId(userId);
    }

    /**
     * 调用钱包服务，获取钱包余额
     * @param userId
     * @return
     */
    public BigDecimal getCapitalAccountByUserId(long userId){

        LOGGER.info("调用钱包服务 ===>{}", userId);
        return capitalAccountService.getCapitalAccountByUserId(userId);
    }

}
