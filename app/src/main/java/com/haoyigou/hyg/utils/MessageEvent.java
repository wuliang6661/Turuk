package com.haoyigou.hyg.utils;

/**
 * Created by witness on 2018/4/9 0009.
 */

public class MessageEvent {

    private String messageType;
    private Object passValue;

    public MessageEvent(String messageType, Object passValue) {
        this.messageType = messageType;
        this.passValue = passValue;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMseeageType(String messageType) {
        this.messageType = messageType;
    }

    public Object getPassValue() {
        return passValue;
    }

    public void setPassValue(Object passValue) {
        this.passValue = passValue;
    }
}