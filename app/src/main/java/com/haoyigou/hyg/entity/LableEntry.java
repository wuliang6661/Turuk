package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/10/13.
 * <p>
 * 个人中心的小红点标记
 */

public class LableEntry implements Serializable {

    private String income;    //我的收益
    private String read;     //阅读
    private String discount;    //优惠券
    private String recharge;   //充值中心
    private String livetv;    //TV直播


    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRecharge() {
        return recharge;
    }

    public void setRecharge(String recharge) {
        this.recharge = recharge;
    }

    public String getLivetv() {
        return livetv;
    }

    public void setLivetv(String livetv) {
        this.livetv = livetv;
    }
}
