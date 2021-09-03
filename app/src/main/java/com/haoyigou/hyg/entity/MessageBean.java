package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/12/30.
 * <p>
 * 消息盒子的首页Bean
 */

public class MessageBean implements Serializable {

    private Integer tpnum = 0;//精选活动（包括活动和商品）的未读推送消息数目
    private String tptitle;//精选活动（包括活动和商品）的未读推送消息最新标题
    private String tpdatestr;//精选活动（包括活动和商品）的未读推送消息最新时间
    private Integer osnum = 0;//订单服务的未读推送消息数目
    private String ostitle;//订单服务的未读推送消息最新标题
    private String osdatestr;//订单服务的未读推送消息最新时间
    private Integer informnum = 0;//消息通知的未读推送消息数目
    private String informtitle;//消息通知的未读推送消息最新标题
    private String informdatestr;//消息通知的未读推送消息最新时间
    private Integer assetnum = 0;//资产的未读推送消息数目
    private String assettitle;//资产的未读推送消息最新标题
    private String assetdatestr;//资产的未读推送消息最新时间

    public Integer getTpnum() {
        return tpnum;
    }

    public void setTpnum(Integer tpnum) {
        this.tpnum = tpnum;
    }

    public String getTptitle() {
        return tptitle;
    }

    public void setTptitle(String tptitle) {
        this.tptitle = tptitle;
    }

    public Integer getOsnum() {
        return osnum;
    }

    public void setOsnum(Integer osnum) {
        this.osnum = osnum;
    }

    public String getTpdatestr() {
        return tpdatestr;
    }

    public void setTpdatestr(String tpdatestr) {
        this.tpdatestr = tpdatestr;
    }

    public String getOstitle() {
        return ostitle;
    }

    public void setOstitle(String ostitle) {
        this.ostitle = ostitle;
    }

    public String getOsdatestr() {
        return osdatestr;
    }

    public void setOsdatestr(String osdatestr) {
        this.osdatestr = osdatestr;
    }

    public Integer getInformnum() {
        return informnum;
    }

    public void setInformnum(Integer informnum) {
        this.informnum = informnum;
    }

    public String getInformtitle() {
        return informtitle;
    }

    public void setInformtitle(String informtitle) {
        this.informtitle = informtitle;
    }

    public String getInformdatestr() {
        return informdatestr;
    }

    public void setInformdatestr(String informdatestr) {
        this.informdatestr = informdatestr;
    }

    public Integer getAssetnum() {
        return assetnum;
    }

    public void setAssetnum(Integer assetnum) {
        this.assetnum = assetnum;
    }

    public String getAssettitle() {
        return assettitle;
    }

    public void setAssettitle(String assettitle) {
        this.assettitle = assettitle;
    }

    public String getAssetdatestr() {
        return assetdatestr;
    }

    public void setAssetdatestr(String assetdatestr) {
        this.assetdatestr = assetdatestr;
    }
}
