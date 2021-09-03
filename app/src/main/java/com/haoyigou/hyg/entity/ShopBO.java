package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/10/19.
 * <p>
 * 首页热卖单品bean
 */

public class ShopBO implements Serializable {

    /**
     * content : 皮皮虾走了特惠
     * decimalCount : 0
     * discontent :
     * disprice : 1698
     * hasCoupons : false
     * id : 130
     * jumpAdress : https://ruiduo.happydoit.com/pix?distributorId=1&source=1&productid=130
     * name : 团购测试商品
     * pic5 :
     * piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201711/184GShf.jpg
     * price : 2199
     * store : 999999
     * tabType : 0
     * unitCount : 0
     */

    private String content;
    private int decimalCount;
    private String discontent;
    private double disprice;
    private boolean hasCoupons;
    private int id;
    private String jumpAdress;
    private String name;
    private String pic5;
    private String piclogo;
    private double price;
    private int store;
    private int tabType;
    private int unitCount;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDecimalCount() {
        return decimalCount;
    }

    public void setDecimalCount(int decimalCount) {
        this.decimalCount = decimalCount;
    }

    public String getDiscontent() {
        return discontent;
    }

    public void setDiscontent(String discontent) {
        this.discontent = discontent;
    }

    public double getDisprice() {
        return disprice;
    }

    public void setDisprice(double disprice) {
        this.disprice = disprice;
    }

    public boolean isHasCoupons() {
        return hasCoupons;
    }

    public void setHasCoupons(boolean hasCoupons) {
        this.hasCoupons = hasCoupons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJumpAdress() {
        return jumpAdress;
    }

    public void setJumpAdress(String jumpAdress) {
        this.jumpAdress = jumpAdress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic5() {
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
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

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }
}
