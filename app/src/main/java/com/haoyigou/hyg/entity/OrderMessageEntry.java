package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2017/9/5.
 * <p>
 * 订单详情bean
 */

public class OrderMessageEntry implements Serializable {


    /**
     * address : {"address":"六合路309","name":"李华佳","telphone":"13524563670"}
     * carriage : 0
     * cashaccount : null
     * details : [{"attrname":"共同","attrsize":"共同","disprice":298,"name":"2皇家SkinMaster3D水漾BB套組","num":1,"piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201610/633cnCv.jpg","price":298}]
     * disprice : 298
     * id : 1046
     * lastfee : 298
     * ordernum : 2016103116522485591
     * payway : 1
     * price : 298
     * status : 6
     * time : 2016-10-31 16:52:24
     * zftime : 2016-10-31 16:52:39
     */

    private AddressBo address;
    private String carriage;
    private String cashaccount;
    private String disprice;
    private String id;
    private String lastfee;
    private String ordernum;
    private String payway;
    private String price;
    private String status;
    private String time;
    private String zftime;
    private String discontent;
    private String discount;
    private List<DetailsBo> details;
    private String lastzftime;
    private String advanceid;//为空是普通订单，不为空是预售订单
    private String advanceStatus;// 0 未付定金  1 定金已付，尾款未付
    private String endOrdernum;//尾款订单号
    private String vender;
    private String skuId;
    private String payStatus;

    public String getDiscontent() {
        return discontent;
    }

    public void setDiscontent(String discontent) {
        this.discontent = discontent;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public AddressBo getAddress() {
        return address;
    }

    public void setAddress(AddressBo address) {
        this.address = address;
    }

    public String getCarriage() {
        return carriage;
    }

    public void setCarriage(String carriage) {
        this.carriage = carriage;
    }

    public String getCashaccount() {
        return cashaccount;
    }

    public void setCashaccount(String cashaccount) {
        this.cashaccount = cashaccount;
    }

    public String getDisprice() {
        return disprice;
    }

    public void setDisprice(String disprice) {
        this.disprice = disprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastfee() {
        return lastfee;
    }

    public void setLastfee(String lastfee) {
        this.lastfee = lastfee;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getZftime() {
        return zftime;
    }

    public void setZftime(String zftime) {
        this.zftime = zftime;
    }

    public List<DetailsBo> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBo> details) {
        this.details = details;
    }
    public void setLastzftime(String lastzftime){
        this.lastzftime = lastzftime;
    }

    public String getLastzftime(){
        return  lastzftime;
    }

    public void setAdvanceid(String advanceid) {
        this.advanceid = advanceid;
    }

    public String getAdvanceid() {
        return advanceid;
    }

    public void setAdvanceStatus(String advanceStatus) {
        this.advanceStatus = advanceStatus;
    }

    public String getAdvanceStatus() {
        return advanceStatus;
    }

    public void setEndOrdernum(String endOrdernum) {
        this.endOrdernum = endOrdernum;
    }

    public String getEndOrdernum() {
        return endOrdernum;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public static class AddressBo {
        /**
         * address : 六合路309
         * name : 李华佳
         * telphone : 13524563670
         */

        private String address;
        private String name;
        private String telphone;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

    }

    public static class DetailsBo {
        /**
         * attrname : 共同
         * attrsize : 共同
         * disprice : 298
         * name : 2皇家SkinMaster3D水漾BB套組
         * num : 1
         * piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/upload/201610/633cnCv.jpg
         * price : 298
         */

        private String attrname;
        private String attrsize;
        private String disprice;
        private String name;
        private String num;
        private String piclogo;
        private String price;
        private String id;
        private String jumpAdress;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAttrname() {
            return attrname;
        }

        public void setAttrname(String attrname) {
            this.attrname = attrname;
        }

        public String getAttrsize() {
            return attrsize;
        }

        public void setAttrsize(String attrsize) {
            this.attrsize = attrsize;
        }

        public String getDisprice() {
            return disprice;
        }

        public void setDisprice(String disprice) {
            this.disprice = disprice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPiclogo() {
            return piclogo;
        }

        public void setPiclogo(String piclogo) {
            this.piclogo = piclogo;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getJumpAdress() {
            return jumpAdress;
        }

        public void setJumpAdress(String jumpAdress) {
            this.jumpAdress = jumpAdress;
        }
    }
}
