package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/2/15.
 * <p>
 * 购物车中商品bean
 */

public class ShopCarBean implements Serializable {

    private String productid;    //商品Id
    private String productname;   //商品名称
    private String piclogo;      //商品logo
    private Integer maxnum;       //商品限购数量
    private String colorname;     //商品颜色
    private String sizename;     //商品大小
    private Integer store;        // 库存量
    private String id;           //item的Id
    private Integer quantity;    //收藏数量
    private Double leftprice;
    private Double rightprice;    //单价
    private String ischecked;    //是否是已选中状态
    private boolean freesend;   //是否包邮
    private String isadd;       //是否是加价购商品  1  是  0  不是
    private String addprice;

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPiclogo() {
        return piclogo;
    }

    public void setPiclogo(String piclogo) {
        this.piclogo = piclogo;
    }

    public Integer getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(Integer maxnum) {
        this.maxnum = maxnum;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    public String getSizename() {
        return sizename;
    }

    public void setSizename(String sizename) {
        this.sizename = sizename;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getLeftprice() {
        return leftprice;
    }

    public void setLeftprice(Double leftprice) {
        this.leftprice = leftprice;
    }

    public Double getRightprice() {
        return rightprice;
    }

    public void setRightprice(Double rightprice) {
        this.rightprice = rightprice;
    }

    public String getIschecked() {
        return ischecked;
    }

    public void setIschecked(String ischecked) {
        this.ischecked = ischecked;
    }

    public void setFreesend(boolean freesend) {
        this.freesend = freesend;
    }

    public boolean isFreesend() {
        return freesend;
    }

    public String getIsadd() {
        return isadd;
    }

    public void setIsadd(String isadd) {
        this.isadd = isadd;
    }

    public String getAddprice() {
        return addprice;
    }

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }
}
