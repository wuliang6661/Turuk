package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2016/11/23.
 * 登陆的用户信息bean
 */

public class UserBean implements Serializable {

    private String sex;
    private String status;
    private String nickname;
    private String shopdesc;
    private String parentid;
    private String distributorId;
    private String shopname;
    private String disself;
    private String headerpic;
    private String sharetitle;
    private String shareimage;
    private String searchdesc;
    private String sharetext;
    private String income;
    private String unreadNum;
    private String bind;       //是否显示会员绑定
    private String invitation;   //是否显示邀请码
    private String gameopen;   //是否显示游戏
    private String mission;     //是否显示新手任务
    private String hassigin;     //是否签到
    private String newhave;     //是否享受新人专享
    private String level_type;//会员等级
    private String level_name;//等级名称

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getLevel_type() {
        return level_type;
    }

    public void setLevel_type(String level_type) {
        this.level_type = level_type;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getGameopen() {
        return gameopen;
    }

    public void setGameopen(String gameopen) {
        this.gameopen = gameopen;
    }

    public String getHassigin() {
        return hassigin;
    }

    public void setHassigin(String hassigin) {
        this.hassigin = hassigin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getShopdesc() {
        return shopdesc;
    }

    public void setShopdesc(String shopdesc) {
        this.shopdesc = shopdesc;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getDisself() {
        return disself;
    }

    public void setDisself(String disself) {
        this.disself = disself;
    }

    public String getHeaderpic() {
        return headerpic;
    }

    public void setHeaderpic(String headerpic) {
        this.headerpic = headerpic;
    }

    public String getSharetitle() {
        return sharetitle;
    }

    public void setSharetitle(String sharetitle) {
        this.sharetitle = sharetitle;
    }

    public String getShareimage() {
        return shareimage;
    }

    public void setShareimage(String shareimage) {
        this.shareimage = shareimage;
    }

    public String getSearchdesc() {
        return searchdesc;
    }

    public void setSearchdesc(String searchdesc) {
        this.searchdesc = searchdesc;
    }

    public String getSharetext() {
        return sharetext;
    }

    public void setSharetext(String sharetext) {
        this.sharetext = sharetext;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(String unreadNum) {
        this.unreadNum = unreadNum;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public String getNewhave() {
        return newhave;
    }

    public void setNewhave(String newhave) {
        this.newhave = newhave;
    }
}
