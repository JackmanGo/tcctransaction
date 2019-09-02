package org.sample.dubbo.order.dao;

import org.sample.dubbo.order.entity.Order;

public interface OrderDao {

    int insert(Order order);

    int update(Order order);

    Order findByOrderNo(String orderNo);
}
