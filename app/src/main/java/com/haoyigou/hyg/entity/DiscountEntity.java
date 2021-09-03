package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/2/24.
 * <p>
 * 优惠卷的bean
 */

public class DiscountEntity implements Serializable {

//            "discode": "",
//            "discount": 25,
//            "distype": 4,
//            "haveGet": 0,
//            "limitprice": 249

    private String discode;    //优惠券编码
    private String discount;    //优惠券减去的金额
    private String distype;     //优惠券类型 3 商品优惠券  4 代金券
    /**
     * 该优惠券有没有被用户领取
     * 1 已领取，优惠券样式需要盖章或变灰，
     * 此时该优惠券不能领取，即为点击无效
     * 0 没有领取，优惠券显示可领取的样式
     */
    private String haveGet;
    /**
     * 订单价格限制
     * distype=3时这个字段为空，使用条件显示为“仅限单品使用”；
     * distype=4时这个字段不为空，使用条件显示为“满n
     * 元可用”，n为limitprice
     */
    private String limitprice;

    public String getDiscode() {
        return discode;
    }

    public void setDiscode(String discode) {
        this.discode = discode;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDistype() {
        return distype;
    }

    public void setDistype(String distype) {
        this.distype = distype;
    }

    public String getHaveGet() {
        return haveGet;
    }

    public void setHaveGet(String haveGet) {
        this.haveGet = haveGet;
    }

    public String getLimitprice() {
        return limitprice;
    }

    public void setLimitprice(String limitprice) {
        this.limitprice = limitprice;
    }
}
