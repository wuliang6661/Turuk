package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by witness on 2018/9/13.
 * 分类商品详情
 */

public class ClassifyDataBO implements Serializable {


    /**
     * id : 166
     * piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201808/02kXGnds.jpg
     * name : 三星盖乐视J3 Pro【测试商品第二个推送品号】
     * price : 999
     * disprice : 0.01
     * store : 39970
     * discontent : 下单专用测试
     * unitCount : 0
     * decimalCount : 0
     * url : https://ruiduo.happydoit.com/pix?distributorId=1060&source=1&productid=166
     */

    private int id;
    private String piclogo;
    private String name;
    private int price;
    private double disprice;
    private int store;
    private String discontent;
    private int unitCount;
    private int decimalCount;
    private String url;
    private String content;
    private String pic5;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPiclogo() {
        return piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDisprice() {
        return disprice;
    }

    public void setDisprice(double disprice) {
        this.disprice = disprice;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public String getDiscontent() {
        return discontent;
    }

    public void setDiscontent(String discontent) {
        this.discontent = discontent;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public int getDecimalCount() {
        return decimalCount;
    }

    public void setDecimalCount(int decimalCount) {
        this.decimalCount = decimalCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public String getPic5() {
        return pic5;
    }

    public void setPic5(String pic5) {
        this.pic5 = pic5;
    }

}
