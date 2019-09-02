package org.sample.dubbo.order.repository;

import org.sample.dubbo.order.dao.OrderDao;
import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.entity.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderRepository {

    @Autowired
    OrderDao orderDao;

    public void updateOrder(Order order) {
        order.updateVersion();
        int effectCount = orderDao.update(order);

        if (effectCount < 1) {
            throw new RuntimeException("更新失败");
        }
    }

    public Order findByMerchantOrderNo(String merchantOrderNo) {
        return orderDao.findByOrderNo(merchantOrderNo);
    }


    public Order createOrder(Long buyerUserId, Long sellerUserId, BigDecimal productAmount) {

        Order order = new Order(buyerUserId, sellerUserId);

        order.setProductAmount(productAmount);

        orderDao.insert(order);
        return order;
    }
}
