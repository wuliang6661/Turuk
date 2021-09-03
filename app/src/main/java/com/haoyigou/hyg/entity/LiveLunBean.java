package com.haoyigou.hyg.entity;

import java.util.List;

/**
 * Created by Witness on 2019-12-25
 * Describe:
 */
public class LiveLunBean {
    private List<NewLiveLunBean> picture;
    private List<NewLiveLunBean> live;

    public List<NewLiveLunBean> getPicture() {
        return picture;
    }

    public void setPicture(List<NewLiveLunBean> picture) {
        this.picture = picture;
    }

    public List<NewLiveLunBean> getLive() {
        return live;
    }

    public void setLive(List<NewLiveLunBean> live) {
        this.live = live;
    }
}
