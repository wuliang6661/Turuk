package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/26.
 */
public class AppPayEntity implements Serializable{
    private String strdata;
    private int paytype;

    public String getStrdata() {
        return strdata;
    }

    public void setStrdata(String strdata) {
        this.strdata = strdata;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }
}
