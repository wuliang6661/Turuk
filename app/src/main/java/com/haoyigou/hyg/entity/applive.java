package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/26.
 */

public class applive implements Serializable {


    /**
     * message : 获取地址成功
     * status : 1
     * data : {"url":"https://ruiduo.happydoit.com/liveShow/showIndex"}
     * code :
     */

    private String message;
    private int status;
    private DataBo data;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBo getData() {
        return data;
    }

    public void setData(DataBo data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataBo {
        /**
         * url : https://ruiduo.happydoit.com/liveShow/showIndex
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}