package com.haoyigou.hyg.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.utils.LogUtils;
import com.yunfan.player.widget.YfCloudPlayer;
import com.yunfan.player.widget.YfPlayerKit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/10/9.
 * <p>
 * 全屏显示的直播
 */

public class TVLiveVideoAct extends BaseActivity implements YfCloudPlayer.OnErrorListener, YfCloudPlayer.OnPreparedListener,
        YfCloudPlayer.OnInfoListener, YfCloudPlayer.OnCompletionListener,
        YfCloudPlayer.OnBufferingUpdateListener,
        YfCloudPlayer.OnCaptureResultListener, YfCloudPlayer.OnGenerateGifListener, View.OnClickListener {


    @BindView(R.id.surface_view)
    YfPlayerKit mVideoView;
    @BindView(R.id.buttom_layout)
    RelativeLayout buttomLayout;
    @BindView(R.id.voice_button)
    ImageView voiceButton;
    @BindView(R.id.live_all)
    ImageView liveAll;
    @BindView(R.id.live_video_layout)
    RelativeLayout liveVideoLayout;

    String liveUrl;

    boolean isVoice = true;   //默认音量是开启的


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setIsSunce(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_tv_video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        setListener();
        liveUrl = getIntent().getStringExtra("url");
        inviVideo();
    }


    /**
     * 设置监听
     */
    private void setListener() {
        liveVideoLayout.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        liveAll.setOnClickListener(this);
        timer.start();
    }


    /**
     * 初始化直播控件
     */
    private void inviVideo() {
        mVideoView.setHardwareDecoder(true);//设置解码为硬解
        mVideoView.enableBufferState(false);//可开启/关闭缓冲状态
        mVideoView.setSpeed(1);//改变播放速度
        mVideoView.setSurfaceCallBack(mSHCallback);
        mVideoView.setVideoLayout(YfPlayerKit.VIDEO_LAYOUT_MATCH_PARENT);
        openVideo(liveUrl);
    }


    /**
     * 设置直播播放
     */
    public void openVideo(String path) {
        if (!TextUtils.isEmpty(path) && mVideoView != null) {
            mVideoView.setOnPreparedListener(this);
            mVideoView.setOnErrorListener(this);
            mVideoView.setOnBufferingUpdateListener(this);
            mVideoView.setOnInfoListener(this);
            mVideoView.setonCaptureResultListener(this);
            mVideoView.setOnGenerateGifListener(this);
            startPlayBack(path);
        }
    }


    private void startPlayBack(String path) {
        if (mVideoView == null)
            return;
//        Log.d("wuliang", "最后写入的地址：" + path);
        mVideoView.setVideoPath(path);
        mVideoView.start();
    }


    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceCreated = true;
            if (mVideoView != null) {
                if (needToReopenVideo) {
                    needToReopenVideo = false;
                    openVideo(liveUrl);
                }
                mVideoView.setVolume(1, 1);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            surfaceCreated = false;
        }
    };
    boolean surfaceCreated = false;
    boolean needToReopenVideo = false;


    @Override
    public void onBufferingUpdate(YfCloudPlayer yfCloudPlayer, int i) {

    }

    @Override
    public void onCaptureResult(String s) {

    }

    @Override
    public void onCompletion(YfCloudPlayer yfCloudPlayer) {

    }

    @Override
    public boolean onError(YfCloudPlayer mp, int what, int extra) {
//        Log.d("wuliang", "onError from YfPlayerKitDemo:" + what + "__" + extra);
        switch (what) {
            case YfCloudPlayer.ERROR_CODE_UNKNOWN:
                writeLog("onError_unknown:" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;
            case YfCloudPlayer.ERROR_CODE_SERVER_DIED:
                writeLog("onError_服务器已挂:" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;
            case YfCloudPlayer.ERROR_CODE_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                writeLog("onError_播放异常:" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;
            case YfCloudPlayer.ERROR_CODE_IO:
                writeLog("onError_IO异常:" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;
            case YfCloudPlayer.ERROR_CODE_MALFORMED:
                writeLog("onError_格式异常:" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;
            case YfCloudPlayer.ERROR_CODE_UNSUPPORTED:
                writeLog("onError_支持有误:" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;
            case YfCloudPlayer.ERROR_CODE_TIMED_OUT:
                writeLog("onError_超时:" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;

            default:
                writeLog("onError_不懂。。。:" + what + "__" + extra + ":" + (SystemClock.elapsedRealtime()));
                break;
        }
        if (surfaceCreated)//只有当surface存在的时候才能打开视频
            openVideo(liveUrl);//简单粗暴地重连
        else
            needToReopenVideo = true;//设置标志位，在surface初始化后打开视频
        return false;
    }

    @Override
    public void onGenerateGifSuccess(String s) {

    }

    @Override
    public void onGenerateGifFail(String s) {

    }

    @Override
    public boolean onInfo(YfCloudPlayer yfCloudPlayer, int i, int i1) {
        return false;
    }

    boolean mBackPressed;

    @Override
    public void onBackPressed() {
        mBackPressed = true;
        finish();
    }

    @Override
    public void onPrepared(YfCloudPlayer mp) {
    }


    private void writeLog(String content) {
//        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
//        LogUtils.d("wuliang", content);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView != null)
            if (mBackPressed) {
                mVideoView.release(true);
                mVideoView = null;
            } else {
                mVideoView.setVolume(0, 0);//可以使用暂停的方式，也可以使用设置音量为0的方式
//                Log.d(TAG,"mVideoView.pause()");
//                mVideoView.pause();
            }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.setVolume(1, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_video_layout:
                if (buttomLayout.getVisibility() == View.VISIBLE) {
                    buttomLayout.setVisibility(View.GONE);
                    timer.cancel();
                } else {
                    buttomLayout.setVisibility(View.VISIBLE);
                    timer.start();
                }
                break;
            case R.id.voice_button:
                if (isVoice) {
                    mVideoView.setVolume(0, 0);
                    voiceButton.setImageResource(R.drawable.voice_close);
                    isVoice = false;
                } else {
                    mVideoView.setVolume(1, 1);
                    voiceButton.setImageResource(R.drawable.voice);
                    isVoice = true;
                }
                break;
            case R.id.live_all:
                setResult(1);
                finish();
                if (MApplication.isHome){
                    MApplication.isFromAll = true;
                    MApplication.isHome = false;
                }

                break;
        }
    }


    /**
     * 倒计时3秒隐藏状态条
     */
    CountDownTimer timer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            buttomLayout.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
