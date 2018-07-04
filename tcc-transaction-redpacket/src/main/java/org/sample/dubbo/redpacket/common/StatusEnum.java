package org.sample.dubbo.redpacket.common;

/**
 * 订单状态枚举
 */
public enum StatusEnum {

    DRAFT(0, "草稿"),
    CONFIRM(1, "提交"),
    CANCEL(2, "回滚");

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
