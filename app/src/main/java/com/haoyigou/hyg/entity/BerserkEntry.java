package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/11/23.
 * <p>
 * 正在疯抢的数据bean
 */

public class BerserkEntry implements Serializable {

    private double disprice;
    private String id;
    private String name;
    private String piclogo;
    private double price;
    private String overplus;
    private String jumpAdress;

    public String getJumpAdress() {
        return jumpAdress;
    }

    public void setJumpAdress(String jumpAdress) {
        this.jumpAdress = jumpAdress;
    }

    public String getOverplus() {
        return overplus;
    }

    public void setOverplus(String overplus) {
        this.overplus = overplus;
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

    public String getPiclogo() {
        return piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
