package com.haoyigou.hyg.view;

/**
 * Created by Witness on 2019-12-12
 * Describe:  直播点赞动画
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.haoyigou.hyg.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HeartView extends View implements View.OnClickListener {
    private int mWidth;

    private int mHeight;

    private int key = 0;

    private List<String> keys = new ArrayList<>();//key值集合

    private HashMap<String, BezierEvaluator> hashMap = new HashMap<>();

    private int addSize;

    public HeartView(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hashMap.size() > 0)
            for (String i : keys) {
                canvas.drawBitmap(hashMap.get(i).bitmap, hashMap.get(i).x, hashMap.get(i).y, hashMap.get(i).paint);
            }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec) / 2;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public void onClick(View v) {
//        keys.add(String.valueOf(key));
//        hashMap.put(String.valueOf(key), new BezierEvaluator(String.valueOf(key)));
//        key++;
    }

    public void addHeart(int size) {
        addSize = size;
        if (size > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    keys.add(String.valueOf(key));
                    hashMap.put(String.valueOf(key), new BezierEvaluator(String.valueOf(key)));
                    key++;
                    addSize--;
                    addHeart(addSize);
                }
            }, 100);
        } else {
            return;
        }
    }

    class BezierEvaluator implements TypeEvaluator<Point>, ValueAnimator.AnimatorUpdateListener {

        private Point randomPoint; //随机控制点

        private int x;//每次更新的曲线的x坐标

        private int y;//每次更新的曲线的y坐标

        public int i;//随机选择的图片样式

        public String key;//用来删除动画结束的心

        private Bitmap bitmap;//要画心的bitmap

        public Paint paint;//画笔

        public BezierEvaluator(String key) {
            this.key = key;
            paint = new Paint();
            paint.setAntiAlias(true);

            i = new Random().nextInt(5);
            getImg(i);
            //获得随机的bitmap
            if (mWidth == 0){
                mWidth = 4;
            }
            if (mHeight == 0){
                mHeight = 2;
            }
            randomPoint = new Point(new Random().nextInt( mWidth / 2) + mWidth / 4, new Random().nextInt(mHeight / 2));
            //获得随机控制点
            Point startP = new Point(mWidth - 150, mHeight - 50);
            //设置起点位置，向上偏移50
            Point endP = new Point(new Random().nextInt(mWidth ) + mWidth , 0);
            //设置终点位置，让终点在上图的随机位置

            ValueAnimator anim = ValueAnimator.ofObject(this, startP, endP);
            //设置动画
            anim.addUpdateListener(this);
            anim.setDuration(6000);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    hashMap.remove(BezierEvaluator.this.key);
                    //在hashmap中把当前删除
                    keys.remove(BezierEvaluator.this.key);
                    //删除当前key值
                    bitmap.recycle();
                    //回收bitmap
                    bitmap = null;
                    //滞空bitmap
                }
            });
            anim.setInterpolator(new LinearInterpolator());
            //为动画加入插值器
            anim.start();
        }

        private void getImg(int i) {
            switch (i) {
                case 0:
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.gift1).copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 1:
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.gift2).copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 2:
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.gift3).copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 3:
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.gift4).copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 4:
                    bitmap = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.gift5).copy(Bitmap.Config.ARGB_8888, true);
                    break;
            }
        }

        @Override
        public Point evaluate(float t, Point startValue, Point endValue) {
            int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * randomPoint.x + t * t * endValue.x);
            int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * randomPoint.y + t * t * endValue.y);
            //根据二阶贝塞尔曲线公式获得在屏幕运动中的x，y坐标
            return new Point(x, y);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //根据动画的更新来获得坐标来重绘onDraw
            Point point = (Point) animation.getAnimatedValue();
            this.x = point.x;
            this.y = point.y;
            int alpha = point.y / 4;
//            paint.setAlpha(alpha);
            //为画笔设置透明度
            invalidate();
        }
    }
}
