package org.sample.dubbo.order.entity;

import org.sample.dubbo.order.common.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {

    private static final long serialVersionUID = -5908730245224893590L;
    private Long id;

    private Long buyerUserId;

    private Long sellerUserId;

    private BigDecimal productAmount;
    private BigDecimal redPacketAmount;

    private BigDecimal capitalAmount;

    private Integer status;

    private String orderNo;

    private Long version = 1L;

    public Order() {

    }

    public Order(Long buyerUserId, Long sellerUserId) {
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.orderNo = UUID.randomUUID().toString();
    }

    public void pay(BigDecimal redPacketPayAmount, BigDecimal capitalPayAmount) {
        this.redPacketAmount = redPacketPayAmount;
        this.capitalAmount = capitalPayAmount;
        this.status = OrderStatus.START;
    }


    public void confirm() {
        this.status = OrderStatus.CONFIRM;
    }

    public void cancelPayment() {
        this.status = OrderStatus.CONCEL;
    }

    public void updateVersion() {
        version++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(long buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public long getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(long sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public BigDecimal getRedPacketAmount() {
        return redPacketAmount;
    }

    public void setRedPacketAmount(BigDecimal redPacketAmount) {
        this.redPacketAmount = redPacketAmount;
    }

    public BigDecimal getCapitalAmount() {
        return capitalAmount;
    }

    public void setCapitalAmount(BigDecimal capitalAmount) {
        this.capitalAmount = capitalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", buyerUserId=" + buyerUserId +
                ", sellerUserId=" + sellerUserId +
                ", productAmount=" + productAmount +
                ", redPacketAmount=" + redPacketAmount +
                ", capitalAmount=" + capitalAmount +
                ", status='" + status + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", version=" + version +
                '}';
    }
}
