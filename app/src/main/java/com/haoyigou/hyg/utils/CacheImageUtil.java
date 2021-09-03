package com.haoyigou.hyg.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.haoyigou.hyg.view.inter.LruCacheUtils;

/**
 * Created by wuliang on 2016/12/6.
 * <p>
 * 提供Glide中加载大量图片的方法
 */

public class CacheImageUtil {

    /***
     * 此方法提供缓存大量图片所需(注意：缓存的适配器不能设置Tag的值)
     *
     * @param viewId
     * @param imageUrl
     * @return
     */
    public static void setImageUrl(ImageView viewId, String imageUrl, int position) {
        //判断缓存中是否已经缓存过该图片，有则直接拿Bitmap，没有则直接调用Glide加载并缓存Bitmap
        Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(imageUrl);
        if (bitmap != null) {
            viewId.setImageBitmap(bitmap);
        } else {
            displayImageTarget(viewId, imageUrl, getTarget(viewId,
                    imageUrl, position));
        }
    }


    /**
     * 加载图片 Target
     *
     * @param imageView
     * @param target
     * @param url
     */
    public static void displayImageTarget(final ImageView imageView, final String
            url, BitmapImageViewTarget target) {
        Glide.get(imageView.getContext())
                .with(imageView.getContext())
                .load(url)
                .asBitmap()//强制转换Bitmap
                .thumbnail(0.4f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(target);
    }


    /**
     * 获取BitmapImageViewTarget
     */
    private static BitmapImageViewTarget getTarget(ImageView imageView, final String url,
                                            final int position) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                //缓存Bitmap，以便于在没有用到时，自动回收
                LruCacheUtils.getInstance().addBitmapToMemoryCache(url,
                        resource);
            }
        };
    }

}
