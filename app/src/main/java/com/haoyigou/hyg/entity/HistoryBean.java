package com.haoyigou.hyg.entity;

/**
 * Created by Witness on 2020-03-09
 * Describe:
 */
public class HistoryBean {


    private String text;
    private String name;

    public HistoryBean(){}


    public HistoryBean(String text,String name){
        this.text = text;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        String mName;
        if (name.startsWith("1") && name.length() == 11) {
            mName = name.substring(0, 3) + "****" + name.substring(7);//
        } else {
            mName = name;//
            if (mName.length() > 8) {
                mName = name.substring(0, 8) + "...";
            }
        }
        return mName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
