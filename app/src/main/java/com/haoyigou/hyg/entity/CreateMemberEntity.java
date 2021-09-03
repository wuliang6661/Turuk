package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/1.
 */
public class CreateMemberEntity implements Serializable {
    private String munbertype;//固定为1005
    private String cusname;//名字
    private String cusmunber;//手机号码

    public String getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(String inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    private String inviteNumber;   //邀请码
    private String phoneCode;//验证码

    public String getMunbertype() {
        return munbertype;
    }

    public void setMunbertype(String munbertype) {
        this.munbertype = munbertype;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getCusmunber() {
        return cusmunber;
    }

    public void setCusmunber(String cusmunber) {
        this.cusmunber = cusmunber;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }


}
