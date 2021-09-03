package com.haoyigou.hyg.entity;

/**
 * Created by witness on 2018/9/10.
 * 个人中心优惠券
 */

public class CouponBO {

    /**
     * discount : 100元
     * unit : 元
     * limitprice : 满200元可用
     * url : https://ruiduo.happydoit.com/coupons/coupondetail?couponid=10124
     * id : 10124
     */

    private String discount;
    private String unit;
    private String limitprice;
    private String url;
    private int id;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLimitprice() {
        return limitprice;
    }

    public void setLimitprice(String limitprice) {
        this.limitprice = limitprice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
