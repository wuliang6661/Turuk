package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/4.
 */
public class PopularityBo implements Serializable {
    private String message;
    private int status;
    private int code;
    private List<RenQiBO> data;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<RenQiBO> getData() {
        return data;
    }

    public void setData(List<RenQiBO> data) {
        this.data = data;
    }


}
