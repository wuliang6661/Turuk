package com.haoyigou.hyg.view.widget;

import com.haoyigou.hyg.view.pulltorefresh.ObservableScrollView;

/**
 * Created by zhusiliang on 15/12/8.
 */
public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
