package com.haoyigou.hyg.entity;

/**
 * Created by Witness on 2020-04-20
 * Describe:
 */
public class AboutProductBean {
    /**
     * id : 201
     * name : openapi专用勿改10019002
     * piclogo : https://yanxuan-item.nosdn.127.net/7af7e5f36f0d8958b2105d407666698e.png
     */

    private int id;
    private String name;
    private String piclogo;

    private String price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
