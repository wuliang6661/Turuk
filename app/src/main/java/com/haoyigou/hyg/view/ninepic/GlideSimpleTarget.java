package com.haoyigou.hyg.view.ninepic;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import ch.ielse.view.imagewatcher.ImageWatcher;

/**
 * @author KCrason
 * @date 2018/5/3
 */
public class GlideSimpleTarget extends SimpleTarget<Bitmap> {

    private ImageWatcher.LoadCallback mLoadCallback;

    public GlideSimpleTarget(ImageWatcher.LoadCallback loadCallback) {
        this.mLoadCallback = loadCallback;
    }

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        if (mLoadCallback != null) {
            mLoadCallback.onLoadStarted(placeholder);
        }
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        if (mLoadCallback != null) {
            mLoadCallback.onResourceReady(resource);
        }
    }




}