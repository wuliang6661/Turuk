package com.haoyigou.hyg.entity;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/28.
 */
public class APPLoginEntity implements Serializable {
    private int apptype;
    private String code;
    private String tel;
    private String mobileCode;
    private String token;

    public int getApptype() {
        return apptype;
    }

    public void setApptype(int apptype) {
        this.apptype = apptype;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public APPLoginEntity copy()
    {
        return JSON.parseObject(JSON.toJSONString(this), APPLoginEntity.class);
    }


}
