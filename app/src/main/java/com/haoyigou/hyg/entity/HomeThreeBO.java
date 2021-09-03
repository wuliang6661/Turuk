package com.haoyigou.hyg.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuliang on 2017/10/20.
 * <p>
 * 首页第三部分bean
 */

public class HomeThreeBO implements Serializable {
    private List<RenQiBO> upper_recommend;

    public List<RenQiBO> getUpper_recommend() {
        return upper_recommend;
    }

    public void setUpper_recommend(List<RenQiBO> upper_recommend) {
        this.upper_recommend = upper_recommend;
    }

    private List<MonthNewsEntry> newpros;    //新品首发

    private List<RenQiBO> recommend;    //人气推荐

    private List<ZhuanTiBO> cullings;      //专题精选

    private List<BenyueBO> tabs;      //本月活动

    public List<MonthNewsEntry> getNewpros() {
        return newpros;
    }

    public void setNewpros(List<MonthNewsEntry> newpros) {
        this.newpros = newpros;
    }

    public List<RenQiBO> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<RenQiBO> recommend) {
        this.recommend = recommend;
    }

    public List<ZhuanTiBO> getCullings() {
        return cullings;
    }

    public void setCullings(List<ZhuanTiBO> cullings) {
        this.cullings = cullings;
    }

    public List<BenyueBO> getTabs() {
        return tabs;
    }

    public void setTabs(List<BenyueBO> tabs) {
        this.tabs = tabs;
    }
}
