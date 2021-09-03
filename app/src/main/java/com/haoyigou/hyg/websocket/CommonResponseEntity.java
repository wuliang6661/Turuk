package com.haoyigou.hyg.websocket;

/**
 * 后台接口返回的数据格式
 * Created by witness on 2018/6/27.
 */

public class CommonResponseEntity {

    /**
     * role : 1
     * header : 10
     * phoneOrOpenid : 10
     * nickname : 10
     * messageType : 1
     * content : 放寒假
     * prizeId : 1
     */

    private String role;
    private String header;
    private String phoneOrOpenid;
    private String nickname;
    private String messageType;
    private String content;
    private String prizeId;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPhoneOrOpenid() {
        return phoneOrOpenid;
    }

    public void setPhoneOrOpenid(String phoneOrOpenid) {
        this.phoneOrOpenid = phoneOrOpenid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }
}
