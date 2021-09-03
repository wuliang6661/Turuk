package com.haoyigou.hyg.entity;

import java.io.Serializable;

/**
 * Created by wuliang on 2017/1/5.
 * <p>
 * 首页视频的信息bean
 */

public class VideoEntry implements Serializable {

    private String id;

    private String img;   //视频图片

    private String url;    //视频地址

    private String title;  //视频标题

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String videoImg) {
        this.img = videoImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String videoUrl) {
        this.url = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String videoTitle) {
        this.title = videoTitle;
    }
}
