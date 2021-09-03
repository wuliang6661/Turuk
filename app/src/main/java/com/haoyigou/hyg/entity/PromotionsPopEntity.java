package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/30.
 */
public class PromotionsPopEntity implements Serializable {

    private String distributorId;
    private String source;
    private String businessid;

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }
}
