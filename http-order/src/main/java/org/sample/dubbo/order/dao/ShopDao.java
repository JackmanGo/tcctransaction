package org.sample.dubbo.order.dao;


import org.sample.dubbo.order.entity.Shop;
import org.springframework.stereotype.Repository;

public interface ShopDao {
    Shop findById(long id);
}
