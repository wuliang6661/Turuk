package com.haoyigou.hyg.entity;

/**
 * Created by wuliang on 2017/3/27.
 * 所有返回的json数据的公有格式
 */

public class BaseResult {

//    1）	status: 表成功和失败状态。1表成功，0表失败。
//    2）	message: 错误信息，当有错误发生时，此errorMessage包含有错误信息
//    3）	code: 错误编码，当有错误发生时，此errorCode包含有错误编码
//    4）	data：返回数据

    private String status;

    private String message;

    private String code;

    private String data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean surcess() {
        return "1".equals(status);
    }

}
