package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
public class FlowBO implements Serializable {

    /**
     * message : 获取接口数据成功
     * result : [{"disprice":0.01,"id":19,"isShowIcon":1,"ordertype":"01","price":10,"pricetemp":0.01,"recharge":10,"rechargeName":"10元"},{"disprice":19.6,"id":20,"isShowIcon":0,"ordertype":"01","price":20,"pricetemp":19.6,"recharge":20,"rechargeName":"10元"},{"disprice":30,"id":8,"isShowIcon":0,"ordertype":"01","price":30,"pricetemp":30,"recharge":30,"rechargeName":"30元"},{"disprice":0.01,"id":21,"isShowIcon":1,"ordertype":"01","price":49.8,"pricetemp":49.8,"recharge":50,"rechargeName":"50元"},{"disprice":99,"id":3,"isShowIcon":0,"ordertype":"01","price":99,"pricetemp":99,"recharge":100,"rechargeName":"100元"},{"disprice":99.7,"id":44,"isShowIcon":0,"ordertype":"01","price":99.9,"pricetemp":99.9,"recharge":101,"rechargeName":"101元"},{"disprice":198,"id":10,"isShowIcon":0,"ordertype":"01","price":198,"pricetemp":198,"recharge":200,"rechargeName":"200元"},{"disprice":10,"id":23,"isShowIcon":0,"ordertype":"02","price":10,"pricetemp":10,"recharge":100,"rechargeName":"100M","skulist":[{"disprice":10,"id":23,"memo":"全国流量，当月可用","region":0},{"disprice":14.7,"id":48,"memo":"省内流量，当月可用","region":1}]},{"disprice":3,"id":39,"isShowIcon":0,"ordertype":"02","price":3,"pricetemp":3,"recharge":10,"rechargeName":"10M","skulist":[{"disprice":3,"id":39,"memo":"全国流量，当月可用","region":0}]},{"disprice":0.01,"id":38,"isShowIcon":1,"ordertype":"02","price":10,"pricetemp":10,"recharge":30,"rechargeName":"30M","skulist":[{"disprice":0.01,"id":38,"memo":"全国流量，当月可用","region":0}]}]
     * status : 1
     */




            private double disprice;
            private int id;
            private String memo;
            private int region;

            public double getDisprice() {
                return disprice;
            }

            public void setDisprice(double disprice) {
                this.disprice = disprice;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMemo() {
                return memo;
            }

            public void setMemo(String memo) {
                this.memo = memo;
            }

            public int getRegion() {
                return region;
            }

            public void setRegion(int region) {
                this.region = region;
            }
        }


