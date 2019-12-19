package org.sample.dubbo.order.service;

import com.tcc.transaction.api.cap.CapitalAccountServiceApi;
import com.tcc.transaction.api.redpacket.RedPacketAccountServiceApi;
import org.apache.dubbo.config.annotation.Reference;
import org.sample.dubbo.order.OrderApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("accountService")
public class AccountServiceImpl {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderApplication.class);

    //@Reference
    RedPacketAccountServiceApi redPacketAccountService;

    //@Reference
    CapitalAccountServiceApi capitalAccountService;


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
