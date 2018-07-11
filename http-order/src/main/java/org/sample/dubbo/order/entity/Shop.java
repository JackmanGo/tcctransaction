package org.sample.dubbo.order.entity;

public class Shop {

    private long id;

    private long ownerUserId;

    public Shop(){

    }

    public Shop(long id, long ownerUserId){

        this.id = id;
        this.ownerUserId = ownerUserId;
    }

    public long getOwnerUserId() {
        return ownerUserId;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", ownerUserId=" + ownerUserId +
                '}';
    }
}
