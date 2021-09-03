package com.haoyigou.hyg.entity;

/**
 * Created by Witness on 2020-04-20
 * Describe:
 */
public class CommentBean {
    /**
     * header : null
     * id : 1
     * isTip : 0
     * name : 17376551017
     * text : 6666
     * tips : 0
     */

    private String header;
    private String id;
    private String isTip;
    private String name;
    private String text;
    private String tips;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsTip() {
        return isTip;
    }

    public void setIsTip(String isTip) {
        this.isTip = isTip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
