package org.sample.dubbo.order.dao;

import org.sample.dubbo.order.entity.Order;
import org.springframework.stereotype.Repository;

public interface OrderDao {

    int insert(Order order);

    int update(Order order);

    Order findByMerchantOrderNo(String merchantOrderNo);
}
