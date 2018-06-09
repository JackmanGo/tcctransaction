package org.sample.dubbo.order.repository;
import org.sample.dubbo.order.dao.ProductDao;
import org.sample.dubbo.order.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductRepository {

    @Autowired
    ProductDao productDao;

    public Product findById(long productId){
        return productDao.findById(productId);
    }

    public List<Product> findByShopId(long shopId){
        return productDao.findByShopId(shopId);
    }
}
