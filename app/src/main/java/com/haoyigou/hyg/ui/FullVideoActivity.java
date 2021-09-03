package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/9/25.
 * <p>
 * 用于全屏的video
 */

public class FullVideoActivity extends BaseActivity {

//    @BindView(R.id.video)
//    StandardGSYVideoPlayer detailPlayer;

    String url;
    long startTime;

    private boolean isPlay;

//    private OrientationUtils orientationUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setIsSunce(false);
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_full_video);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        startTime = getIntent().getIntExtra("startTime", 0);
        setVideo();
    }


    /**
     * 初始化视频播放器
     */
    private void setVideo() {
//        //外部辅助的旋转，帮助全屏
//        orientationUtils = new OrientationUtils(this, detailPlayer);
//        //初始化不打开外部的旋转
//        orientationUtils.setEnable(false);
//        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
//        gsyVideoOption.setIsTouchWiget(true)
//                .setRotateViewAuto(false)
//                .setLockLand(true)
//                .setShowFullAnimation(true)
//                .setNeedLockFull(true)
//                .setSeekRatio(1)
//                .setUrl(url)
//                .setSeekOnStart(startTime)
//                .setCacheWithPlay(false)
//                .setVideoTitle("介绍")
//                .setStandardVideoAllCallBack(new SampleListener() {
//                    @Override
//                    public void onPrepared(String url, Object... objects) {
//                        super.onPrepared(url, objects);
//                        isPlay = true;
//                    }
//                })
//                .build(detailPlayer);
//        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("startTime", detailPlayer.getCurrentPositionWhenPlaying());
//                setResult(1, intent);
//                finish();
//            }
//        });
//        detailPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("startTime", detailPlayer.getCurrentPositionWhenPlaying());
//                setResult(1, intent);
//                finish();
//            }
//        });
//        detailPlayer.getTitleTextView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("startTime", detailPlayer.getCurrentPositionWhenPlaying());
//                setResult(1, intent);
//                finish();
//            }
//        });
//        detailPlayer.startPlayLogic();
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                Intent intent = new Intent();
//                intent.putExtra("startTime", detailPlayer.getCurrentPositionWhenPlaying());
//                setResult(1, intent);
//                finish();
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (orientationUtils != null) {
//            orientationUtils.backToProtVideo();
//        }
//        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
//            return;
//        }
//        super.onBackPressed();
//    }
//
//
//    @Override
//    protected void onPause() {
//        getCurPlay().onVideoPause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        getCurPlay().onVideoResume();
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (isPlay) {
//            getCurPlay().release();
//        }
//        //GSYPreViewManager.instance().releaseMediaPlayer();
//        if (orientationUtils != null)
//            orientationUtils.releaseListener();
//        super.onDestroy();
//    }
//
//
//    private GSYVideoPlayer getCurPlay() {
//        if (detailPlayer.getFullWindowPlayer() != null) {
//            return detailPlayer.getFullWindowPlayer();
//        }
//        return detailPlayer;
//    }


}
