package com.haoyigou.hyg.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.ui.VideoRotatorAct;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.entity.VideoEntry;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.utils.NetworkUtils;
import com.haoyigou.hyg.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/1/3.
 * <p>
 * 实现视频播放的fragment
 */

public class VideoFragment extends BaseFragment implements
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, View.OnClickListener {


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
    @BindView(R.id.video_img)
    ImageView videoImg;
    @BindView(R.id.video_img_start)
    ImageView videoImgStart;
    @BindView(R.id.video_img_layout)
    RelativeLayout videoImgLayout;
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
    private boolean isFull = false;  //是否全屏
    Rect rect;

    VideoEntry entry;   //视频信息
    private boolean isKill = true;   //视频是否被Kill掉,默认是需要重新开始的

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);
    }

    /**
     * 接收视频信息并赋值
     *
     * @param entry
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoEntry entry) {   //将视频信息传递进来
        if (StringUtils.isEmpty(entry.getUrl())) {   //告诉我停止视频
            stopVideo(false);
            return;
        }
        this.entry = entry;
        Glide.with(getActivity()).load(entry.getImg()).into(videoImg);
        videoImgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isNetworkConnected(getActivity())) {
                    showToast("网络无连接");
                    return;
                }
                if (NetworkUtils.isWifiConnected(getActivity())) {   //是否连接wifi
                    if (isKill) {
                        playVideo();
                    } else {
                        reStartVideo();
                    }
                    videoImgLayout.setVisibility(View.GONE);
                    videoStart.setImageResource(R.drawable.video_stop);
                } else {
                    showHintDialog();
                }
            }
        });
    }


    // SurfaceView的callBack
    private class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // surfaceView被创建
            // 设置播放资源
            // 判断播放位置
            if (VideoContains.playPosition >= 0 && isFull) {
                videoImgLayout.setVisibility(View.GONE);
                isFull = false;
                reStartVideo();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // surfaceView销毁,同时销毁mediaPlayer
//            if (null != mediaPlayer) {
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }
        }
    }


    /**
     * 播放视频
     */
    public void playVideo() {
        progressBar.setVisibility(View.VISIBLE);
        // 初始化MediaPlayer
        mediaPlayer = VideoContains.getMedia();
        // 重置mediaPaly,建议在初始滑mediaplay立即调用。
        mediaPlayer.reset();
        // 设置声音效果
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置播放完成监听
        mediaPlayer.setOnCompletionListener(VideoFragment.this);
        // 设置媒体加载完成以后回调函数。
        mediaPlayer.setOnPreparedListener(VideoFragment.this);
        // 错误监听回调函数
        mediaPlayer.setOnErrorListener(VideoFragment.this);
        // 设置缓存变化监听
        mediaPlayer.setOnBufferingUpdateListener(VideoFragment.this);
        Uri uri = Uri.parse(entry.getUrl());
        try {
            // mediaPlayer.reset();
            // mediaPlayer.setDataSource(pathString);
            mediaPlayer.setDataSource(getActivity(), uri);
            // mediaPlayer.setDataSource(SurfaceViewTestActivity.this, uri);
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "加载视频错误！", Toast.LENGTH_LONG).show();
        }
    }


    /***
     * 重新启动视频
     */
    private void reStartVideo() {
        // 设置播放完成监听
        mediaPlayer.setOnCompletionListener(VideoFragment.this);
        // 设置媒体加载完成以后回调函数。
        mediaPlayer.setOnPreparedListener(VideoFragment.this);
        // 错误监听回调函数
        mediaPlayer.setOnErrorListener(VideoFragment.this);
        // 设置缓存变化监听
        mediaPlayer.setOnBufferingUpdateListener(VideoFragment.this);
        // 判断是否有保存的播放位置,防止屏幕旋转时，界面被重新构建，播放位置丢失。
        if (VideoContains.playPosition >= 0) {
            mediaPlayer.seekTo(VideoContains.playPosition);
            VideoContains.playPosition = -1;
            // surfaceHolder.unlockCanvasAndPost(Constants.getCanvas());
        }
        seekBarAutoFlag = true;
        // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
        seekbar.setMax(mediaPlayer.getDuration());
        // 设置播放时间
        videoTimeLong = mediaPlayer.getDuration();
        videoTimeString = getShowTime(videoTimeLong);
        videoAlltime.setText(videoTimeString);
        // 播放视频
        videoStart.setImageResource(R.drawable.video_stop);
        mediaPlayer.start();
        // 设置显示到屏幕
        mediaPlayer.setDisplay(surfaceHolder);
        // 开启线程 刷新进度条
        new Thread(runnable).start();
        // 设置surfaceView保持在屏幕上
        mediaPlayer.setScreenOnWhilePlaying(true);
        surfaceHolder.setKeepScreenOn(true);
    }


    /**
     * 视频加载完毕监听
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // 当视频加载完毕以后，隐藏加载进度条
        progressBar.setVisibility(View.GONE);
        // 判断是否有保存的播放位置,防止屏幕旋转时，界面被重新构建，播放位置丢失。
        if (VideoContains.playPosition >= 0) {
            mediaPlayer.seekTo(VideoContains.playPosition);
            VideoContains.playPosition = -1;
            // surfaceHolder.unlockCanvasAndPost(Constants.getCanvas());
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
        mediaPlayer.start();
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
//        seekbar.setProgress(Integer.parseInt(String.valueOf(videoTimeLong)));
        stopVideo(true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
//                Toast.makeText(getActivity(), "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT)
//                        .show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(getActivity(), "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                Toast.makeText(getActivity(), "MEDIA_ERROR_IO", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                Toast.makeText(getActivity(), "MEDIA_ERROR_MALFORMED", Toast.LENGTH_SHORT)
                        .show();
                break;
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(getActivity(),
                        "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                Toast.makeText(getActivity(), "MEDIA_ERROR_TIMED_OUT", Toast.LENGTH_SHORT)
                        .show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                Toast.makeText(getActivity(), "MEDIA_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT)
                        .show();
                break;
        }
        return false;
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
                    if (null != VideoFragment.this.mediaPlayer
                            && VideoFragment.this.mediaPlayer
                            .isPlaying()) {
                        seekbar.setProgress(mediaPlayer.getCurrentPosition());
                        handler.sendEmptyMessage(0x11);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 用来监听视频是否显示在屏幕上
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int[] location = new int[2];
            surfaceView.getLocationInWindow(location);
            if (surfaceView.getLocalVisibleRect(rect)) {
                // 控件在屏幕可见区域
            } else {
                // 控件已不在屏幕可见区域（已滑出屏幕）
                stopVideo(false);
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
            isFull = true;
            videoStart.setImageResource(R.drawable.video_stop);
            mediaPlayer.start();
            Intent intent = new Intent(getActivity(), VideoRotatorAct.class);
            getActivity().startActivity(intent);
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
            if (!isFull) {
                stopVideo(false);
            } else {
                if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                    VideoContains.playPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    seekBarAutoFlag = false;
                }
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
        if (null != mediaPlayer && isFull) {
            // 保存播放位置
            VideoContains.playPosition = mediaPlayer.getCurrentPosition();
        }
    }

    /**
     * 屏幕销毁时调用
     */
    @Override
    public void onDestroy() {
        Log.e("log--", "onDestroy()被调用!");
        super.onDestroy();
        // 由于MediaPlay非常占用资源，所以建议屏幕当前activity销毁时，则直接销毁
        try {
            if (null != VideoFragment.this.mediaPlayer) {
                // 提前标志为false,防止在视频停止时，线程仍在运行。
                seekBarAutoFlag = false;
                // 如果正在播放，则停止。
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                VideoContains.playPosition = -1;
                // 释放mediaPlayer
                VideoFragment.this.mediaPlayer.release();
                VideoFragment.this.mediaPlayer = null;
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

    /**
     * 停止视频
     */
    private void stopVideo(boolean isKill) {
        if (mediaPlayer == null) return;
        this.isKill = isKill;
        videoStart.setImageResource(R.drawable.video_start);
        // 提前标志为false,防止在视频停止时，线程仍在运行。
        seekBarAutoFlag = false;
        // 如果正在播放，则停止。
        if (isKill) {
            seekbar.setProgress(0);
            VideoContains.closeMedia();
        } else {
            if (mediaPlayer != null) {
                VideoContains.playPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        }
        menuLayout.setVisibility(View.GONE);
        videoImgLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 弹出网络环境提示弹窗
     */
    private void showHintDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());//提示框
        final View view = factory.inflate(R.layout.hint_dialog_layout, null);//这里必须是final的
        TextView cancle = (TextView) view.findViewById(R.id.off_commit);
        TextView commit = (TextView) view.findViewById(R.id.commit);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //事件
                dialog.dismiss();
                if (isKill) {
                    playVideo();
                } else {
                    reStartVideo();
                }
                videoImgLayout.setVisibility(View.GONE);
                videoStart.setImageResource(R.drawable.video_stop);
            }
        });
        dialog.setView(view);
        dialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
