package org.sample.dubbo.cap.apiImpl;


import org.sample.dubbo.cap.api.CapitalAccountService;
import org.sample.dubbo.cap.repository.CapitalAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("capitalAccountService")
public class CapitalAccountServiceImpl implements CapitalAccountService {


    @Autowired
    CapitalAccountRepository capitalAccountRepository;

    @Override
    public BigDecimal getCapitalAccountByUserId(long userId) {
        return capitalAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}
