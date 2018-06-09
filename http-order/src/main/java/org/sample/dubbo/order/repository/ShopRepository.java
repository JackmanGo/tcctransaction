package org.sample.dubbo.order.repository;

import org.sample.dubbo.order.dao.ShopDao;
import org.sample.dubbo.order.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class ShopRepository {

    @Autowired
    ShopDao shopDao;

    public Shop findById(long id) {

        return shopDao.findById(id);
    }
}
