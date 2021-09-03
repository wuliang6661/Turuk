package com.haoyigou.hyg.view.widget;

import android.view.View;
import android.view.animation.Animation;

/**
 * @author xwangly@163.com
 * @date 2013-7-10
 * 下拉控件接口
 */
public interface IPullDownElastic {
    int RELEASE_To_REFRESH = 0;
    int PULL_To_REFRESH = 1;
    int REFRESHING = 2;
    int DONE = 3;

    View getElasticLayout();

    int getElasticHeight();

    void showArrow(int visibility);

    void startAnimation(Animation animation);

    void clearAnimation();

    void showProgressBar(int visibility);

    void setTips(String tips);

    void showLastUpdate(int visibility);

    void setLastUpdateText(String text);

    /**
     * 可以不用实现此方法，PullDownScrollView会处理ElasticLayout布局中的状态
     * 如果需要特殊处理，可以实现此方法进行处理
     *
     * @param state  @see RELEASE_To_REFRESH
     * @param isBack 是否是松开回退
     */
    void changeElasticState(int state, boolean isBack);

}