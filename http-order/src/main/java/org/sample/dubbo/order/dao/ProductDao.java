package org.sample.dubbo.order.dao;


import org.sample.dubbo.order.entity.Product;

import java.util.List;

public interface ProductDao {

    Product findById(long productId);

    List<Product> findProducts();
}
