package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/1/12.
 * <p>
 * 分类详细商品列表的bean
 */

public class ShopClassifyEntry implements Serializable {

    private String id;    //商品ID
    private String imgurl;   //商品图片
    private String name;     //商品名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
