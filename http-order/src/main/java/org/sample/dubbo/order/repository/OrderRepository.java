package org.sample.dubbo.order.repository;

import org.sample.dubbo.order.dao.OrderDao;
import org.sample.dubbo.order.dao.OrderLineDao;
import org.sample.dubbo.order.entity.Order;
import org.sample.dubbo.order.entity.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderRepository {

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderLineDao orderLineDao;

    @Autowired
    ProductRepository productRepository;

    public void createOrder(Order order) {
        orderDao.insert(order);

        for (OrderLine orderLine : order.getOrderLines()) {
            orderLineDao.insert(orderLine);
        }
    }

    public void updateOrder(Order order) {
        order.updateVersion();
        int effectCount = orderDao.update(order);

        if (effectCount < 1) {
            //throw new Exception("update order failed");
            return;
        }
    }

    public Order findByMerchantOrderNo(String merchantOrderNo) {
        return orderDao.findByMerchantOrderNo(merchantOrderNo);
    }


    public Order createOrder(Long payerUserId, Long payeeUserId, Long productId) {
        Order order = buildOrder(payerUserId, payeeUserId, productId);

        this.createOrder(order);

        return order;
    }


    private Order buildOrder(Long payerUserId, Long payeeUserId, Long productId) {

        Order order = new Order(payerUserId, payeeUserId);

        order.addOrderLine(new OrderLine(productId, 1, productRepository.findById(productId).getPrice()));

        return order;
    }
}
