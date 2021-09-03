package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/11/2.
 * <p>
 * 微信支付的bean
 */

public class WXPayBO implements Serializable {


    /**
     * appid : wx080d4ccaa7f3f9a0
     * noncestr : 51da85a3c3dfa1f360b48852b64218b2
     * packages : Sign=WXPay
     * partnerid : 1390172202
     * prepayid : wx20171102184344f289bcbcdf0447598500
     * sign : C9A9D68FCCB2EBF3B36BA21BAAA3553C
     * timestamp : 1509619424
     */

    private String appid;
    private String noncestr;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }


    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
