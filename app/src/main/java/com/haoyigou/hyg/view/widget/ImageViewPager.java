package com.haoyigou.hyg.view.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by kristain on 16/1/17.
 */
public class ImageViewPager extends ViewPager {

    PointF downPoint = new PointF();
    public static boolean ifIntercept = true;
    OnSingleTouchListener onSingleTouchListener;

    public ImageViewPager(Context context) {
        super(context);
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = event.getX();
                downPoint.y = event.getY();
                // if (this.getChildCount() > 1) { // 有内容，多于1个时
                // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                // }
                break;
            case MotionEvent.ACTION_MOVE:
                // if (this.getChildCount() > 1) {
                getParent().requestDisallowInterceptTouchEvent(true);
                // }
                break;
            case MotionEvent.ACTION_UP:
                // 在up时判断是否按下和松手的坐标为一个点
                if (PointF.length(event.getX() - downPoint.x, event.getY()
                        - downPoint.y) < (float) 5.0) {
                    onSingleTouchListener.onSingleTouch(getCurrentItem());
                }
                break;
        }
        return super.onTouchEvent(event);
    }
//
//	public boolean onInterceptTouchEvent(MotionEvent event) {
//		Log.d("gaolei", "onInterceptTouchEvent-------------");
//		// 当拦截触摸事件到达此位置的时候，返回true，
//		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
//		return ifIntercept;
//	}

    // 创建点击事件接口

    public interface OnSingleTouchListener {
        void onSingleTouch(int position);
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}
