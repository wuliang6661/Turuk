package com.haoyigou.hyg.fragment;

import android.graphics.Canvas;
import android.media.MediaPlayer;

/**
 * Created by wuliang on 2017/1/3.
 * <p>
 * 视频播放用到的配置类
 */

public class VideoContains {

    /**
     * 记录播放位置
     */
    public static int playPosition = -1;

    private static Canvas canvas;

    /**
     * 记录播放控制器
     *
     * @return
     */
    private static MediaPlayer player;

    public static Canvas getCanvas() {
        return canvas;
    }

    public static void setCanvas(Canvas canvas) {
        VideoContains.canvas = canvas;
    }

    public static MediaPlayer getMedia() {
        if (player == null) {
            player = new MediaPlayer();
        }
        return player;
    }

    /**
     * 释放播放器
     */
    public static void closeMedia() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            VideoContains.playPosition = -1;
            // 释放mediaPlayer
            player.release();
            player = null;
        }
    }

}
