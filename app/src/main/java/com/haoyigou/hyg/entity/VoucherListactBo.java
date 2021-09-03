package com.haoyigou.hyg.entity;

/**
 * Created by Administrator on 2018/1/19.
 */
public class VoucherListactBo {

    private  long id;
    private String btime;
    private String etime;
    private Double discount;
    private String title;
    private Double limitprice;
    private String canuse;
    private int  type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLimitprice() {
        return limitprice;
    }

    public void setLimitprice(Double limitprice) {
        this.limitprice = limitprice;
    }

    public String getCanuse() {
        return canuse;
    }

    public void setCanuse(String canuse) {
        this.canuse = canuse;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
