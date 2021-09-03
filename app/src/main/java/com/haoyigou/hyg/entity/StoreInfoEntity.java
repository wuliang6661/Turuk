package com.haoyigou.hyg.entity;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class StoreInfoEntity implements Serializable {
    private String shopname;//商铺名称
    private String shopdesc;//商铺描述
    private String headerpic;//头像地址
    private String unreadnum;//未处理任务
    private String distributorId;//合伙人Id
    private boolean dissel;//是否是合伙人
    private String address;
    private String  id;
    private String pictureroot;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

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

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopdesc() {
        return shopdesc;
    }

    public void setShopdesc(String shopdesc) {
        this.shopdesc = shopdesc;
    }

    public String getHeaderpic() {
        return headerpic;
    }

    public void setHeaderpic(String headerpic) {
        this.headerpic = headerpic;
    }

    public String getUnreadnum() {
        return unreadnum;
    }

    public void setUnreadnum(String unreadnum) {
        this.unreadnum = unreadnum;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public boolean isDissel() {
        return dissel;
    }

    public void setDissel(boolean dissel) {
        this.dissel = dissel;
    }
}
