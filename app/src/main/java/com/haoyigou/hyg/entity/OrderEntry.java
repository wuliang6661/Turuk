package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2017/9/1.
 * <p>
 * 订单bean
 */

public class OrderEntry implements Serializable {


    /**
     * carriage : 0.0
     * details : [{"name":"11112.升特五彩缤纷有氧运动走步weii","piclogo":"http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201701/945znYl.png"}]
     * disprice : 498.0
     * id : 3114
     * ordernum : 2017080810553091546
     * productnum : 1
     * status : -88
     * time : 2017-08-08 10:55:30
     * zftime : null
     */

    private String carriage;
    private String disprice;
    private String id;
    private String ordernum;
    private String productnum;
    private String status;
    private String time;
    private String zftime;
    private String enjoyDiscount;
    private List<DetailsBo> details;
    private String advanceid;//为空是普通订单，不为空是预售订单
    private String advanceStatus;// 0 未付定金  1 定金已付，尾款未付
    private String endOrdernum;//尾款订单号


    public String getCarriage() {
        return carriage;
    }

    public void setCarriage(String carriage) {
        this.carriage = carriage;
    }

    public String getEnjoyDiscount() {
        return enjoyDiscount;
    }

    public void setEnjoyDiscount(String enjoyDiscount) {
        this.enjoyDiscount = enjoyDiscount;
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

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getProductnum() {
        return productnum;
    }

    public void setProductnum(String productnum) {
        this.productnum = productnum;
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

    public String getAdvanceid() {
        return advanceid;
    }

    public void setAdvanceid(String advanceid) {
        this.advanceid = advanceid;
    }

    public String getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(String advanceStatus) {
        this.advanceStatus = advanceStatus;
    }

    public String getEndOrdernum() {
        return endOrdernum;
    }

    public void setEndOrdernum(String endOrdernum) {
        this.endOrdernum = endOrdernum;
    }

    public static class DetailsBo {
        /**
         * name : 11112.升特五彩缤纷有氧运动走步weii
         * piclogo : http://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images_test/upload/201701/945znYl.png
         */

        private String attrname;
        private String disprice;
        private String name;
        private String num;
        private String piclogo;
        private String price;
        private String id;


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

        public String getDisprice() {
            return disprice;
        }

        public void setDisprice(String disprice) {
            this.disprice = disprice;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPiclogo() {
            return piclogo;
        }

        public void setPiclogo(String piclogo) {
            this.piclogo = piclogo;
        }

    }
}
