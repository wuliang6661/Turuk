package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
public class LunBoBO implements Serializable {
    private int lunbotime;
    private List<BannerBO> lunbopics;

    public int getLunbotime() {
        return lunbotime;
    }

    public void setLunbotime(int lunbotime) {
        this.lunbotime = lunbotime;
    }

    public List<BannerBO> getLunbopics() {
        return lunbopics;
    }

    public void setLunbopics(List<BannerBO> lunbopics) {
        this.lunbopics = lunbopics;
    }
}
