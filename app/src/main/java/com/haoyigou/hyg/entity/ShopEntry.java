package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/11/24.
 * <p>
 * 首页商品bean
 */

public class ShopEntry implements Serializable {

    private String disprice;   //折扣价
    private boolean hasCoupons;  //显示优惠券图案
    private String id;
    private String name;
    private String oldprice;
    private String newprice;
    private String piclogo;
    private String piclogo2;
    private String pic4;
    private String pic5;
    private String price;
    private String tabType;
    private String store;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getPic5() {
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }

    public String getPic4() {
        return pic4;
    }

    public void setPic4(String pic4) {
        this.pic4 = pic4;
    }

    public String getDisprice() {
        return disprice;
    }

    public void setDisprice(String disprice) {
        this.disprice = disprice;
    }

    public boolean getHasCoupons() {
        return hasCoupons;
    }

    public void setHasCoupons(boolean hasCoupons) {
        this.hasCoupons = hasCoupons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getPiclogo2() {
        return piclogo2;
    }

    public void setPiclogo2(String piclogo2) {
        this.piclogo2 = piclogo2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public boolean isHasCoupons() {
        return hasCoupons;
    }

    public String getNewprice() {
        return newprice;
    }

    public void setNewprice(String newprice) {
        this.newprice = newprice;
    }

    public String getPiclogo() {
        return piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }
}
