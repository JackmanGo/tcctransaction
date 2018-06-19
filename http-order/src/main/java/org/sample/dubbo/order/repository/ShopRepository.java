package org.sample.dubbo.order.repository;

import org.junit.Test;
import org.sample.dubbo.order.dao.ShopDao;
import org.sample.dubbo.order.entity.Shop;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShopRepository {

    @Autowired
    ShopDao shopDao;

    public Shop findById(long id) {

        return shopDao.findById(id);
    }


    /**
     *
     * 测试Spring的事务传播机制
     */
    @Autowired
    ShopRepository shopRepository;


    @Transactional
    public int createFirst(){
       Shop shop1 = new Shop(11, 11);
       shopDao.create(shop1);
       Shop shop2 = new Shop(22, 22);
       shopDao.create(shop2);
       try {
           this.createSecond();
           //shopRepository.createSecond();
       }catch (Exception e){
          System.out.println(e.getMessage());
       }
       return 0;
    }


    @Transactional
    public int createSecond(){
        Shop shop3 = new Shop(33, 33);
        shopDao.create(shop3);
        int i = 1/0;
        Shop shop4 = new Shop(44, 44);
        shopDao.create(shop4);
        return 0;
    }
}
