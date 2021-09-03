package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/11/23.
 * <p>
 * 首页轮播图的bean
 */

public class LunBoPicEntry implements Serializable {

    private String address;

    private String id;

    private String pictureroot;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPictureroot() {
        return pictureroot;
    }

    public void setPictureroot(String pictureroot) {
        this.pictureroot = pictureroot;
    }
}
