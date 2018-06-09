package org.sample.dubbo.order.dao;

import org.sample.dubbo.order.entity.OrderLine;
import org.springframework.stereotype.Repository;

public interface OrderLineDao {
    void insert(OrderLine orderLine);
}
