package org.sample.dubbo.cap.common;

/**
 * 订单状态枚举
 */
public enum StatusEnum {

    //草稿
    DRAFT(0, "DRAFT"),
    //支付中
    PAYING(1, "PAYING"),
    //提交
    CONFIRM(2, "CONFIRM"),
    //回滚
    CANCEL(3, "CANCEL");

    private int status;
    private String name;


    StatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
