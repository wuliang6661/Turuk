package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/12.
 */

/**
 * 订单链接的实体类
 */
public class OrderLinkEntity implements Serializable {
    private String distributorId;
    private String ordertype;

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }
}
