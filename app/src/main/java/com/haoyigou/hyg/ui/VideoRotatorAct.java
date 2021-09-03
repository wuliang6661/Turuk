package com.haoyigou.hyg.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.fragment.VideoContains;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/1/4.
 * <p>
 * 横屏显示的视频播放
 */

public class VideoRotatorAct extends Activity implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, View.OnClickListener {

    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.video_start)
    ImageView videoStart;
    @BindView(R.id.video_nowtime)
    TextView videoNowtime;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.video_alltime)
    TextView videoAlltime;
    @BindView(R.id.video_full)
    ImageView videoFull;
    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;

    /**
     * surfaceView播放控制
     */
    private SurfaceHolder surfaceHolder;
    /**
     * 播放视频
     */
    private MediaPlayer mediaPlayer;
    /**
     * seekBar是否自动拖动
     */
    private boolean seekBarAutoFlag = false;
    /**
     * 播放总时间
     */
    private String videoTimeString;
    private long videoTimeLong;
    /**
     * 屏幕的宽度和高度
     */
    private int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_rotator);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.GONE);
        screenWidth = GlobalApplication.screen_width;
        screenHeight = GlobalApplication.screen_height;
        initvion();
    }


    /**
     * 初始化控件
     */
    private void initvion() {
        // 初始化控件
        // 设置surfaceHolder
        surfaceHolder = surfaceView.getHolder();
        // 设置Holder类型,该类型表示surfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置surface回调
        surfaceHolder.addCallback(new SurfaceCallback());
    }


    // SurfaceView的callBack
    private class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // surfaceView被创建
            // 设置播放资源
            playVideo();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    /**
     * 播放视频
     */
    public void playVideo() {
        // 初始化MediaPlayer
        mediaPlayer = VideoContains.getMedia();
        mediaPlayer.setOnCompletionListener(this);
        if (VideoContains.playPosition >= 0) {
            mediaPlayer.seekTo(VideoContains.playPosition);
        }
        seekBarAutoFlag = true;
        // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
        seekbar.setMax(mediaPlayer.getDuration());
        // 设置播放时间
        videoTimeLong = mediaPlayer.getDuration();
        videoTimeString = getShowTime(videoTimeLong);
        videoAlltime.setText(videoTimeString);
        // 设置拖动监听事件
        seekbar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        // 设置按钮监听事件
        // 暂停和播放
        videoStart.setOnClickListener(this);
        // 视频大小
        videoFull.setOnClickListener(this);
        surfaceView.setOnClickListener(this);
        // 播放视频
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            videoStart.setImageResource(R.drawable.video_stop);
        }
        // 设置显示到屏幕
        mediaPlayer.setDisplay(surfaceHolder);
        // 开启线程 刷新进度条
        new Thread(runnable).start();
        // 设置surfaceView保持在屏幕上
        mediaPlayer.setScreenOnWhilePlaying(true);
        surfaceHolder.setKeepScreenOn(true);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        // 设置seeKbar跳转到最后位置
        seekbar.setProgress(Integer.parseInt(String.valueOf(videoTimeLong)));
        // 设置播放标记为false
        seekBarAutoFlag = false;
        finish();
    }


    /**
     * 滑动条变化线程
     */
    private Runnable runnable = new Runnable() {

        public void run() {
            // TODO Auto-generated method stub
            // 增加对异常的捕获，防止在判断mediaPlayer.isPlaying的时候，报IllegalStateException异常
            try {
                while (seekBarAutoFlag) {
                    /*
                     * mediaPlayer不为空且处于正在播放状态时，使进度条滚动。
					 * 通过指定类名的方式判断mediaPlayer防止状态发生不一致
					 */
                    if (null != VideoRotatorAct.this.mediaPlayer
                            && VideoRotatorAct.this.mediaPlayer
                            .isPlaying()) {
                        seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * seekBar拖动监听类
     *
     * @author shenxiaolei
     */
    @SuppressWarnings("unused")
    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub
            if (progress >= 0) {
                // 如果是用户手动拖动控件，则设置视频跳转。
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                // 设置当前播放时间
                videoNowtime.setText(getShowTime(progress));
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public void onClick(View view) {
        // 播放、暂停按钮
        if (view == videoStart) {
            if (null != mediaPlayer) {
                // 正在播放
                if (mediaPlayer.isPlaying()) {
                    VideoContains.playPosition = mediaPlayer.getCurrentPosition();
                    // seekBarAutoFlag = false;
                    mediaPlayer.pause();
                    videoStart.setImageResource(R.drawable.video_start);
                } else {
                    if (VideoContains.playPosition >= 0) {
                        // seekBarAutoFlag = true;
                        mediaPlayer.seekTo(VideoContains.playPosition);
                        mediaPlayer.start();
                        videoStart.setImageResource(R.drawable.video_stop);
                        VideoContains.playPosition = -1;
                    }
                }
            }
        }
        if (view == videoFull) {
            // 调用改变大小的方法
//            changeVideoSize();
            finish();
        }
        if (view == surfaceView) {
            if (menuLayout.getVisibility() == View.VISIBLE) {
                menuLayout.setVisibility(View.GONE);
            } else {
                menuLayout.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 从暂停中恢复
     */
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 判断播放位置
//        if (VideoContains.playPosition >= 0) {
//            if (null != mediaPlayer) {
//                seekBarAutoFlag = true;
//                mediaPlayer.seekTo(VideoContains.playPosition);
//                mediaPlayer.start();
//            }
//        }
    }

    /**
     * 页面处于暂停状态
     */
    @Override
    public void onPause() {
        super.onPause();
        try {
            if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                VideoContains.playPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                seekBarAutoFlag = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 发生屏幕旋转时调用
     */
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        if (null != mediaPlayer) {
            // 保存播放位置
            VideoContains.playPosition = mediaPlayer.getCurrentPosition();
        }
    }

    /**
     * 屏幕销毁时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 由于MediaPlay非常占用资源，所以建议屏幕当前activity销毁时，则直接销毁
        try {
            if (null != mediaPlayer) {
                // 提前标志为false,防止在视频停止时，线程仍在运行。
                seekBarAutoFlag = false;
                VideoContains.playPosition = mediaPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 转换播放时间
     *
     * @param milliseconds 传入毫秒值
     * @return 返回 hh:mm:ss或mm:ss格式的数据
     */
    @SuppressLint("SimpleDateFormat")
    public String getShowTime(long milliseconds) {
        // 获取日历函数
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat dateFormat = null;
        // 判断是否大于60分钟，如果大于就显示小时。设置日期格式
        if (milliseconds / 60000 > 60) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("mm:ss");
        }
        return dateFormat.format(calendar.getTime());
    }

}
