package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/20.
 */
public class APPWeChatLoginBindMobileEntity implements Serializable {
    private String phone;
    private String messcode;
    private String appopenid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMesscode() {
        return messcode;
    }

    public void setMesscode(String messcode) {
        this.messcode = messcode;
    }

    public String getAppopenid() {
        return appopenid;
    }

    public void setAppopenid(String appopenid) {
        this.appopenid = appopenid;
    }
}
