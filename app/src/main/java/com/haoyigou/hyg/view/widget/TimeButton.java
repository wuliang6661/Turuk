package com.haoyigou.hyg.view.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.haoyigou.hyg.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/8/11.
 */
public class TimeButton extends Button {
    private long length = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = "秒后重新获取";
    private String textbefore = "重新获取验证码";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private Timer t;
    private TimerTask tt;
    private long time;
    private Context mContext;
//    Map<String, Long> map = new HashMap<String, Long>();

    public TimeButton(Context context) {
        super(context);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText(textbefore);
                TimeButton.this.setBackgroundResource(R.drawable.verification_button);
                clearTimer();
            }
        }
    };

    private void initTimer() {
        time = length;
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                //Log.e("yung", time / 1000 + "");
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        //  Toast.makeText(mContext, "计时结束", Toast.LENGTH_SHORT).show();
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }


    public void startTimer() {
        TimeButton.this.setBackgroundResource(R.drawable.verification_button_dis);
        initTimer();
        this.setText(time / 1000 + textafter);
        this.setEnabled(false);
        t.schedule(tt, 0, 1000);
    }


//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        if (l instanceof TimeButton) {
//            super.setOnClickListener(l);
//        } else
//            this.mOnclickListener = l;
//    }

    /**
     * 设置计时时候显示的文本
     */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /**
     * 设置点击之前的文本
     */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置到计时长度
     *
     * @param lenght 时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.length = lenght;
        return this;
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
//        if (MainActivity.map == null)
//            MainActivity.map = new HashMap<String, Long>();
//        MainActivity.map.put(TIME, time);
//        MainActivity.map.put(CTIME, System.currentTimeMillis());
        clearTimer();
    }

    public void onCreate(Bundle bundle) {
//        Log.e("yung", MainActivity.map + "");
//        if (MainActivity.map == null)
//            return;
//        if (MainActivity.map.size() <= 0)// 这里表示没有上次未完成的计时
//            return;
//        long time = System.currentTimeMillis() - MainActivity.map.get(CTIME)
//                - MainActivity.map.get(TIME);
//        MainActivity.map.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
        }
    }


}