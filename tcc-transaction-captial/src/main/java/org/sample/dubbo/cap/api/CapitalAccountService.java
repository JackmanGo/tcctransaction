package org.sample.dubbo.cap.api;

import java.math.BigDecimal;

public interface CapitalAccountService {

    BigDecimal getCapitalAccountByUserId(long userId);
}
