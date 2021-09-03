package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/4.
 */
public class SelectionBo implements Serializable {
    private String message;
    private int status;
    private int code;
    private List<ZhuanTiBO> data;

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

    public List<ZhuanTiBO> getData() {
        return data;
    }

    public void setData(List<ZhuanTiBO> data) {
        this.data = data;
    }


}