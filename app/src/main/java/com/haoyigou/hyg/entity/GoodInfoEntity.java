package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/9/9.
 */
public class GoodInfoEntity implements Serializable {

    private String distributorId;//合伙人ID
    private int cuurPageNo;//加载第几页数据
    private int pageSize;//每页数据条数
    private String piclogo2;//图片地址
    private String name;//商品名称
    private String price;//原价
    private String disprice;//折扣价
    private String id;
    private String oldprice;//市场价
    private String hasCoupons;
    private int tabType;

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }

    public String getHasCoupons() {
        return hasCoupons;
    }

    public void setHasCoupons(String hasCoupons) {
        this.hasCoupons = hasCoupons;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPiclogo2() {
        return piclogo2;
    }

    public void setPiclogo2(String piclogo2) {
        this.piclogo2 = piclogo2;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCuurPageNo() {
        return cuurPageNo;
    }

    public void setCuurPageNo(int cuurPageNo) {
        this.cuurPageNo = cuurPageNo;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisprice() {
        return disprice;
    }

    public void setDisprice(String disprice) {
        this.disprice = disprice;
    }
}
