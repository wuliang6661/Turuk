package com.haoyigou.hyg.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haoyigou.hyg.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kristain on 16/1/17.
 */
public class LoadingProgressDialog extends Dialog {


    private Context context = null;
    private static LoadingProgressDialog progressDialog = null;

    public LoadingProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public static LoadingProgressDialog createDialog(Context context) {
        progressDialog = new LoadingProgressDialog(context, R.style.loadingProgressDialog);
        progressDialog.setContentView(R.layout.loading_toast);
        progressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return progressDialog;
    }

    public static LoadingProgressDialog getDialog() {
        return progressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (progressDialog == null) {
            return;
        }

        // runDot();
        runRefreshAnimal();
    }

    /**
     * [Summary]
     * setTitile 标题
     *
     * @param strTitle
     * @return
     */
    public LoadingProgressDialog setTitile(String strTitle) {
        return progressDialog;
    }

    /**
     * [Summary]
     * setMessage 提示内容
     *
     * @param strMessage
     * @return
     */
    public LoadingProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) progressDialog.findViewById(R.id.loadingmsg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return progressDialog;
    }

    // 加载中的加载点的进度
    private void runDot() {
        final TextView loadingmsg_dot = (TextView) progressDialog
                .findViewById(R.id.loadingmsg_dot);
        final Handler handler = new Handler() {
            private int count = 0;

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        loadingmsg_dot.setText(getDot(count++));
                        break;
                }
                super.handleMessage(msg);
            }

            private String getDot(int i) {
                int mod = i % 3;
                StringBuffer s = new StringBuffer();
                if (mod == 0) {
                    s.append(".  ");
                } else if (mod == 1) {
                    s.append(".. ");
                } else {
                    s.append("...");
                }
                return s.toString();
            }
        };
        TimerTask task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, 0, 500); // 延时0ms后执行，500ms执行一次
    }

    // 加载图片
    private void runRefreshAnimal() {
//        ProgressBar imageView = (ProgressBar) progressDialog.findViewById(R.id.loading_icon);
//        RotateAnimation refreshAnimation = new RotateAnimation(0, 180,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        refreshAnimation.setRepeatCount(-1);
//        refreshAnimation.setRepeatMode(Animation.RESTART);
//        refreshAnimation.setDuration(1000);
//        refreshAnimation.setInterpolator(new LinearInterpolator());
//        imageView.startAnimation(refreshAnimation);
//        refreshAnimation.startNow();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
