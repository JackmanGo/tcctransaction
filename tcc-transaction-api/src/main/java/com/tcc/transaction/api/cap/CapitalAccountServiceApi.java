package com.tcc.transaction.api.cap;

import java.math.BigDecimal;

public interface CapitalAccountServiceApi {

    BigDecimal getCapitalAccountByUserId(long userId);
}
