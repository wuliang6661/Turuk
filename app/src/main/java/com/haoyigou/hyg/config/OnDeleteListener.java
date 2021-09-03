package com.haoyigou.hyg.config;

import android.view.View;

import com.haoyigou.hyg.adapter.GridImageAdapter;

/**
 * Created by Witness on 2020-04-20
 * Describe:
 */
public interface OnDeleteListener {

    /**
     * item点击事件
     *  @param v
     * @param position
     */
    void onDeleteClick(GridImageAdapter.ViewHolder v, int position);
}
