package org.sample.dubbo.order.dao;


import org.sample.dubbo.order.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductDao {

    Product findById(long productId);

    List<Product> findByShopId(long shopId);
}
