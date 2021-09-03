package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/11/23.
 * 每月上新bean
 */

public class MonthNewsEntry implements Serializable {

    private double disprice;

    private String id;

    private String name;

    private String oldprice;

    private String piclogo;

    private String price;

    private String jumpAdress;

    public String getJumpAdress() {
        return jumpAdress;
    }

    public void setJumpAdress(String jumpAdress) {
        this.jumpAdress = jumpAdress;
    }

    public double getDisprice() {
        return disprice;
    }

    public void setDisprice(double disprice) {
        this.disprice = disprice;
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

    public String getPiclogo() {
        return piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
