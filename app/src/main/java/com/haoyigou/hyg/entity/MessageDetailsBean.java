package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/12/30.
 * <p>
 * 消息列表里的消息实例对象
 */

public class MessageDetailsBean implements Serializable {


    private String id;//消息推送的Id
    private String assetPicType;
    private String assetType;
    private String informType;
    private String memo;
    private String orderNum;
    private String orderType;
    private String pic;
    private String productid;
    private String pushDateStr;
    private String pushtype;
    private String tabid;
    private String title;
    private String haveread;  //是否已阅读标志
    private String havereadtrue;   //是否已阅读
    private String havecollect;   //是否已收藏标志
    private String activityType;

    public String getHavereadtrue() {
        return havereadtrue;
    }

    public void setHavereadtrue(String havereadtrue) {
        this.havereadtrue = havereadtrue;
    }

    public String getTaburl() {
        return taburl;
    }

    public void setTaburl(String taburl) {
        this.taburl = taburl;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    private String taburl;

    public String getAssetPicType() {
        return assetPicType;
    }

    public void setAssetPicType(String assetPicType) {
        this.assetPicType = assetPicType;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getInformType() {
        return informType;
    }

    public void setInformType(String informType) {
        this.informType = informType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getPushDateStr() {
        return pushDateStr;
    }

    public void setPushDateStr(String pushDateStr) {
        this.pushDateStr = pushDateStr;
    }

    public String getTabid() {
        return tabid;
    }

    public void setTabid(String tabid) {
        this.tabid = tabid;
    }

    public String getPushtype() {
        return pushtype;
    }

    public void setPushtype(String pushtype) {
        this.pushtype = pushtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHaveread() {
        return haveread;
    }

    public void setHaveread(String haveread) {
        this.haveread = haveread;
    }

    public String getHavecollect() {
        return havecollect;
    }

    public void setHavecollect(String havecollect) {
        this.havecollect = havecollect;
    }
}
