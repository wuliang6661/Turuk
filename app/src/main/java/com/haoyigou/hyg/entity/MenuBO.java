package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/10/19.
 * <p>
 * 首页五菜单Bean
 */

public class MenuBO implements Serializable {


    /**
     * channelName : 1
     * id : 15
     * img : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201710/694RPnL.png
     * tabId : 1
     * title : 充值中心
     * url :
     */

    private String channelName;
    private int id;
    private String img;
    private int tabId;
    private String title;
    private String url;
    private int jumptype;
    private String idparam;


    public String getIdparam() {
        return idparam;
    }

    public void setIdparam(String idparam) {
        this.idparam = idparam;
    }

    public int getJumptype() {
        return jumptype;
    }

    public void setJumptype(int jumptype) {
        this.jumptype = jumptype;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getTabId() {
        return tabId;
    }

    public void setTabId(int tabId) {
        this.tabId = tabId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
