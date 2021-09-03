package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2017/1/12.
 * <p>
 * 分类页面的数据bean
 */

public class ClassifyShopEntry implements Serializable {


    private String name;    //分类名称
    private String id;        //分类id
    private List<ShopClassifyEntry> second;   //商品列表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShopClassifyEntry> getSecond() {
        return second;
    }

    public void setSecond(List<ShopClassifyEntry> second) {
        this.second = second;
    }
}
