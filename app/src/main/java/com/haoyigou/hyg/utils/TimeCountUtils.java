package com.haoyigou.hyg.utils;

import android.util.Log;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wuliang on 2016/11/24.
 * <p>
 * 正在疯抢的倒计时类
 */

public class TimeCountUtils {

    private onTimeConunt listener;

    private static volatile TimeCountUtils time;
    private Timer timer;

    private TimeCountUtils() {
        startTimeCount();
    }

    public static TimeCountUtils getInstance() {
        if (time == null) {
            synchronized (Object.class) {
                if (time == null) {
                    time = new TimeCountUtils();
                }
            }
        }
        return time;
    }


    private void startTimeCount() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (listener != null)
                    listener.onProgress();
//                Log.e("log--", "11111");
            }
        }, 1000, 1000);
    }

    public void stopTimeCount() {
        if (timer != null) {
            timer.cancel();
        }
        time = null;
    }

    public void setOnTimeConuntListener(onTimeConunt count) {
        listener = count;
    }


    public interface onTimeConunt {
        void onProgress();
    }
}
