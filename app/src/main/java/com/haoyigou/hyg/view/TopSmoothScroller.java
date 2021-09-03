package com.haoyigou.hyg.view;

import android.content.Context;

import androidx.recyclerview.widget.LinearSmoothScroller;

/**
 * Created by Witness on 2019-12-24
 * Describe:
 */
public class TopSmoothScroller extends LinearSmoothScroller {
    public TopSmoothScroller(Context context) {
        super(context);
    }
    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }
    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }
}
