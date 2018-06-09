package org.sample.dubbo.cap.dao;


import org.sample.dubbo.cap.entity.CapitalAccount;

public interface CapitalAccountDao {

    CapitalAccount findByUserId(long userId);

    int update(CapitalAccount capitalAccount);
}
