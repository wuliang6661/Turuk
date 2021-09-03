package com.haoyigou.hyg.entity;

/**
 * Created by wuliang on 2017/4/6.
 * <p>
 * 充值记录的javabean
 */

public class VoucherRecordEntry {


    private String disprice;
    private String zftimeStr;     //充值时间
    private String account;    //充值手机号码
    private String status;     //1/2/-2 已支付-1 充值失败 3  已到账-3 已退款
    private String ordertype;   //01 充值话费 02 充值流量
    private String recharge;    //冲多少 若为话费充值，则单位是元；若为流量充值，则单位是M；
    private String rechargeName;

    public String getRechargeName() {
        return rechargeName;
    }

    public void setRechargeName(String rechargeName) {
        this.rechargeName = rechargeName;
    }

    public String getDisprice() {
        return disprice;
    }

    public void setDisprice(String disprice) {
        this.disprice = disprice;
    }

    public String getZftimeStr() {
        return zftimeStr;
    }

    public void setZftimeStr(String zftimeStr) {
        this.zftimeStr = zftimeStr;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getRecharge() {
        return recharge;
    }

    public void setRecharge(String recharge) {
        this.recharge = recharge;
    }
}
