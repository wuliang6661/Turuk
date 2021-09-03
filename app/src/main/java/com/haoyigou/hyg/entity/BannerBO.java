package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/10/18.
 * <p>
 * 轮播图bean
 */

public class BannerBO implements Serializable {

    /**
     * address : http://ruiduo.happydoit.com:8080/mycoupon/getdis?discode=1252261035&source=1
     * id : 41
     * pictureroot : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201610/14SiYJ.jpg
     */

    private String address;
    private int id;
    private String pictureroot;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPictureroot() {
        return pictureroot;
    }

    public void setPictureroot(String pictureroot) {
        this.pictureroot = pictureroot;
    }

}
