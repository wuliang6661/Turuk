package com.haoyigou.hyg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.utils.DisplayUtils;

import gorden.refresh.JRefreshHeader;
import gorden.refresh.JRefreshLayout;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;


/**
 * 经典下拉刷新
 * Created by Gorden on 2017/6/17.
 */

public class ClassicalHeader extends FrameLayout implements JRefreshHeader {
    private static final String TAG = "ClassicalHeader";

    private ImageView arrawImg;
//    private TextView textTitle;
    private Context mcontext;

    private RotateAnimation rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    public ClassicalHeader(@NonNull Context context) {
        this(context,null);
        mcontext = context;
    }

    public ClassicalHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        mcontext = context;
    }

    public ClassicalHeader(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext = context;

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.CENTER_VERTICAL);
        addView(root,LayoutParams.WRAP_CONTENT,(int)(DisplayUtils.getScreenWidth(mcontext) * 0.16));
        ((LayoutParams)root.getLayoutParams()).gravity = Gravity.CENTER;

        arrawImg = new ImageView(context);
        Glide.with(context)
                .load(R.drawable.topload)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into(arrawImg);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(DisplayUtils.getScreenWidth(mcontext) * 0.18),
                (int)(DisplayUtils.getScreenWidth(mcontext) * 0.09));
        arrawImg.setLayoutParams(params);
        arrawImg.setScaleType(ImageView.ScaleType.CENTER);
        root.addView(arrawImg);
    }

    @Override
    public long succeedRetention() {
        return 200;
    }

    @Override
    public long failingRetention() {
        return 0;
    }

    @Override
    public int refreshHeight() {
        return getHeight();
    }

    @Override
    public int maxOffsetHeight() {
        return 4*getHeight();
    }


    boolean isReset = true;

    @Override
    public void onReset( JRefreshLayout refreshLayout) {
        Log.e(TAG,"----------------> onReset");
        Glide.with(mcontext).load(R.drawable.topload).asGif().into(arrawImg);
//        textTitle.setText("下拉刷新...");
        isReset = true;
        arrawImg.setVisibility(VISIBLE);
    }

    @Override
    public void onPrepare( JRefreshLayout refreshLayout) {
        Log.e(TAG,"----------------> onPrepare");
        Glide.with(mcontext).load(R.drawable.topload).asGif().into(arrawImg);
//        textTitle.setText("下拉刷新...");
    }

    @Override
    public void onRefresh( JRefreshLayout refreshLayout) {
        Log.e(TAG,"----------------> onRefresh");
        Glide.with(mcontext).load(R.drawable.topload).asGif().into(arrawImg);
//        arrawImg.startAnimation(rotateAnimation);
//        textTitle.setText("加载中...");
        isReset = false;
    }

    @Override
    public void onComplete(JRefreshLayout refreshLayout, boolean isSuccess) {
        Log.e(TAG,"----------------> onComplete");
//        arrawImg.clearAnimation();
//        arrawImg.setVisibility(GONE);
//        if (isSuccess){
//            textTitle.setText("刷新完成...");
//        }else{
//            textTitle.setText("刷新失败...");
//        }
    }

    boolean attain = false;

    @Override
    public void onScroll(JRefreshLayout refreshLayout, int distance, float percent, boolean refreshing) {
        Log.e(TAG,"----------------> onScroll  "+percent);

//        if (!refreshing&&isReset){
//            if(percent>=1&&!attain){
//                attain = true;
////                textTitle.setText("释放刷新...");
//                arrawImg.animate().rotation(-180).start();
//            }else if (percent<1&&attain){
//                attain = false;
//                arrawImg.animate().rotation(0).start();
////                textTitle.setText("下拉刷新...");
//            }
//        }
    }



}
