package org.sample.dubbo.cap.resource;


import com.tcc.transaction.api.cap.CapitalAccountServiceApi;
import org.apache.dubbo.config.annotation.Service;
import org.sample.dubbo.cap.repository.CapitalAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Service
public class CapitalAccountServiceImpl implements CapitalAccountServiceApi {


    @Autowired
    CapitalAccountRepository capitalAccountRepository;

    @Override
    public BigDecimal getCapitalAccountByUserId(long userId) {
        return capitalAccountRepository.findByUserId(userId).getBalanceAmount();
    }
}
