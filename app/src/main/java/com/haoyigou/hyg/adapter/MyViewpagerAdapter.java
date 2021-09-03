package com.haoyigou.hyg.adapter;

/**
 * Created by Administrator on 2018/9/16.
 */

import android.os.Parcelable;
import android.view.View;

import androidx.viewpager.widget.PagerAdapter;

public class MyViewpagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return false;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        return null;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
    };

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public void restoreState(android.os.Parcelable state, ClassLoader loader) {

    };

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}