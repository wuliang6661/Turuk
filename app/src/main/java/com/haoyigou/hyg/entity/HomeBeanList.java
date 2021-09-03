package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2016/11/23.
 * <p>
 * 首页数据集合bean
 */

public class HomeBeanList implements Serializable {

    private List<ClassifyEntry> classify;   //分类集合

    private List<LunBoPicEntry> lunbopics;    //轮播图集合

    private List<MonthEntry> tabs;   //每月活动集合

    private List<MonthNewsEntry> newpros;   //每月上新集合

    private List<BrandsEntry> brands;    //超值热卖集合

    private List<VideoEntry> video;     //视频集合

    public List<ClassifyEntry> getClassify() {
        return classify;
    }

    public void setClassify(List<ClassifyEntry> classify) {
        this.classify = classify;
    }

    public List<LunBoPicEntry> getLunbopics() {
        return lunbopics;
    }

    public void setLunbopics(List<LunBoPicEntry> lunbopics) {
        this.lunbopics = lunbopics;
    }

    public List<MonthEntry> getTabs() {
        return tabs;
    }

    public void setTabs(List<MonthEntry> tabs) {
        this.tabs = tabs;
    }

    public List<MonthNewsEntry> getNewpros() {
        return newpros;
    }

    public void setNewpros(List<MonthNewsEntry> newpros) {
        this.newpros = newpros;
    }

    public List<BrandsEntry> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandsEntry> brands) {
        this.brands = brands;
    }

    public List<VideoEntry> getVideo() {
        return video;
    }

    public void setVideo(List<VideoEntry> video) {
        this.video = video;
    }
}
