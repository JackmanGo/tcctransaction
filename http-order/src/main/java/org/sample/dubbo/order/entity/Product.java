package org.sample.dubbo.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable{
    private long productId;

    private long ownerUserId;

    private String productName;

    private BigDecimal price;

    public Product() {
    }

    public Product(long productId, long ownerUserId, String productName, BigDecimal price) {
        this.productId = productId;
        this.ownerUserId = ownerUserId;
        this.productName = productName;
        this.price = price;
    }

    public long getProductId() {
        return productId;
    }

    public long getOwnerUserId() {
        return ownerUserId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
