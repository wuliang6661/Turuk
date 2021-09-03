package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/8/31.
 * <p>
 * tv直播数据
 */

public class TVLiveEntry implements Serializable {


    /**
     * begintime : 1476411120000
     * disprice : 0.0
     * endtime : 1476414720000
     * id : 44
     * logopic : http://old.bz55.com/uploads/allimg/150505/139-150505095443.jpg
     * nfmpgmscheduleid : 29750
     * oldprice : 0.0
     * price : 0.0
     * productcode : 2
     * productid : 0
     * productname : 測試1測試1測試1測試1
     * url : null
     */

    private long begintime;
    private String disprice;
    private long endtime;
    private String id;
    private String logopic;
    private String nfmpgmscheduleid;
    private String oldprice;
    private String price;
    private String productcode;
    private String productid;
    private String productname;
    private String url;
    private String noticestatus;
    private String store;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getNoticestatus() {
        return noticestatus;
    }

    public void setNoticestatus(String noticestatus) {
        this.noticestatus = noticestatus;
    }

    public long getBegintime() {
        return begintime;
    }

    public void setBegintime(long begintime) {
        this.begintime = begintime;
    }

    public String getDisprice() {
        return disprice;
    }

    public void setDisprice(String disprice) {
        this.disprice = disprice;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogopic() {
        return logopic;
    }

    public void setLogopic(String logopic) {
        this.logopic = logopic;
    }

    public String getNfmpgmscheduleid() {
        return nfmpgmscheduleid;
    }

    public void setNfmpgmscheduleid(String nfmpgmscheduleid) {
        this.nfmpgmscheduleid = nfmpgmscheduleid;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
