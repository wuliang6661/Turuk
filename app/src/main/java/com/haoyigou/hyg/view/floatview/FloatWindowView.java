package com.haoyigou.hyg.view.floatview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.utils.MessageEvent;
import com.haoyigou.hyg.utils.NetworkUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.viewpager.ViewPagerSlide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yunfan.player.widget.YfCloudPlayer;
import com.yunfan.player.widget.YfPlayerKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer.GiraffePlayer;
import tcking.github.com.giraffeplayer.IjkVideoView;

import static com.haoyigou.hyg.utils.LogUtils.showToast;

/**
 * FloatWindowView:悬浮窗控件V1-利用windowManger控制窗口
 *
 * @author Nonolive-杜乾 Created on 2017/12/12 - 17:16.
 * E-mail:dusan.du@nonolive.com
 */

public class FloatWindowView extends FrameLayout implements IFloatView {

    private static final String TAG = FloatWindowView.class.getSimpleName();
    private float xInView;
    private float yInView;
    private float xInScreen;
    private float yInScreen;
    private float xDownInScreen;
    private float yDownInScreen;
    private Context mContext;

    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mWindowParams = null;
    private FloatViewParams params = null;
    private FloatViewListener listener;
    private int statusBarHeight = 0;
    private int screenWidth;
    private int screenHeight;
    private int mMinWidth;//初始宽度
    private int mMaxWidth;//视频最大宽度
    private float mRatio = 1.77f;//窗口高/宽比
    private int videoViewMargin;
    private boolean isSdkGt23 = false;//sdk版本是否>=23



    /** 主界面,直播 */
    private RelativeLayout rlRoom;
    private ImageView iv_zoom_btn;
    private ImageView iv_close_window;
    private IjkVideoView videoView;
    private String liveUrl;
    private int currentPos = 0;
    private Activity activity;


    public FloatWindowView(Context context) {
        super(context);
        init();
    }

    public FloatWindowView(int currentPos,Activity activity,Context mContext, FloatViewParams floatViewParams, WindowManager.LayoutParams wmParams, String liveUrl) {
        super(mContext);
        this.params = floatViewParams;
        this.mWindowParams = wmParams;
        this.mContext = mContext;
        this.liveUrl = liveUrl;
        this.activity = activity;
        this.currentPos = currentPos;
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        try {
            initView();
            initData();
            if (videoView != null){
                videoView.setVideoPath(liveUrl);
                if (currentPos > 0) {
                    videoView.seekTo(currentPos);
                }
                videoView.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        initMView(mContext);
    }

    private void initData() {
        mContext = getContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        statusBarHeight = params.statusBarHeight;
        screenWidth = params.screenWidth;
        screenHeight = params.screenHeight - statusBarHeight;//要去掉状态栏高度
        videoViewMargin = params.viewMargin;
        mMaxWidth = params.mMaxWidth;
        mMinWidth = params.mMinWidth;
        mRatio = params.mRatio;
        //起点
        startX = params.x;
        startY = params.y;
        //isSdkGt23 = Build.VERSION.SDK_INT >= 23;
        // >=23的部分手机缩放会卡顿，系统弹窗更新位置迟缓不够平滑
    }

    private void initMView(Context context){
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View floatView = inflater.inflate(R.layout.show_room_layout2, null);
        rlRoom = floatView.findViewById(R.id.rlRoom);
        iv_zoom_btn = floatView.findViewById(R.id.iv_zoom_btn);
        iv_close_window = floatView.findViewById(R.id.iv_close_window);
        videoView = floatView.findViewById(R.id.video_view);
        iv_close_window.setVisibility(VISIBLE);
        iv_zoom_btn.setOnTouchListener(onZoomBtnTouchListener);
        rlRoom.setOnTouchListener(onMovingTouchListener);

        iv_close_window.setOnClickListener(new OnClickListener() {//关闭浮窗
            @Override
            public void onClick(View view) {
                if (null != listener) {
                    listener.onClose();//关闭
                    if (videoView != null){
                        videoView.pause();
                    }
                }
                EventBus.getDefault().unregister(this);
            }
        });

        final int lastViewWidth = params.contentWidth;
        final int lastViewHeight = (int) (lastViewWidth * mRatio);
        updateViewLayoutParams(lastViewWidth, lastViewHeight);
        addView(floatView);
        rlRoom.post(new Runnable() {
            @Override
            public void run() {
                updateWindowWidthAndHeight(lastViewWidth, lastViewHeight);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageType().equals("closeSound")) {
            if (null != listener) {
                listener.onClose();//关闭
                if (videoView != null){
                    videoView.pause();
                }
            }
            EventBus.getDefault().unregister(this);
        }
    }


    /**
     * ******************************* 弹出网络环境提示弹窗 *******************************
     */
    private void showHintDialog(final boolean isFirst) {
        LayoutInflater factory = LayoutInflater.from(mContext);//提示框
        final View view = factory.inflate(R.layout.hint_dialog_layout, null);//这里必须是final的
        TextView cancle = (TextView) view.findViewById(R.id.off_commit);
        TextView commit = (TextView) view.findViewById(R.id.commit);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
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
                if (isFirst) {
                    if (videoView != null && !liveUrl.equals("")){
                        videoView.setVideoPath(liveUrl);
                        videoView.start();
                    }
                } else {
                    if (videoView != null){
                        videoView.resume();
                    }
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /**
     * ******************************* 监测网络变化*********************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals("4G")) {
            if (videoView != null){
                videoView.pause();
            }
            showHintDialog(false);
        } else if (str.equals("wifi")) {
            if (videoView != null){
                videoView.resume();
            }
        }
    }

    private void updateViewLayoutParams(int width, int height) {
        if (rlRoom != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlRoom.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = width;
            rlRoom.setLayoutParams(layoutParams);
        }
    }

    private final OnTouchListener onZoomBtnTouchListener = new OnTouchListener() {
        float lastX = 0;
        float lastY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isDragged = true;
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    changedX = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
//                    showZoomView();
                    handleMoveEvent(event);
                    break;
                case MotionEvent.ACTION_UP:
                    if (listener != null) {
                        listener.onDragged();
                    }
                    displayZoomViewDelay();
                    if (!isSdkGt23) {
                        rejuestWindow();//缩放完成，要调整悬浮窗到视频大小。由于wm更新布局不及时，会有闪烁的问题
                    }
                    isDragged = false;
                    changedX = 0;
                    break;
                default:
                    break;
            }
            return true;
        }

        private void handleMoveEvent(MotionEvent event) {
            isDragged = true;
            float moveX = event.getRawX();
            float moveY = event.getRawY();
            float dx = moveX - lastX;
            float dy = moveY - lastY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance >= 2) {//控制刷新频率
                //已经是最大或者最小不缩放
                int contentWidth = rlRoom.getWidth();
                if (moveY > lastY && moveX > lastX) {
                    if (contentWidth == mMinWidth) {//最小了，不能再小了
                        return;
                    }
                    distance = -distance;//缩小
                } else {
                    if (contentWidth == mMaxWidth) {
                        return;
                    }
                }
                //double angle = Math.atan2(Math.abs(dy), Math.abs(dx)) * 180 / Math.PI;
                int changedWidth = (int) (distance * Math.cos(45));//粗略计算
                if (!isSdkGt23) {
                    //调节内部view大小，先放大窗体到最大，方便调节大小
                    if (mWindowParams.width != mMaxWidth) {
                        updateWindowSize(mMaxWidth);
                    }
                    updateContentViewSize(changedWidth);
                } else {
                    updateFloatWindowSize(changedWidth);//大于6.0则直接改变window大小
                }
            }
            lastX = moveX;
            lastY = moveY;
        }
    };

    /**
     * 更新FloatWindow的大小
     *
     * @param width 传入变化的宽度
     */
    private void updateFloatWindowSize(int width) {
        int currentWidth = mWindowParams.width;
        int newWidth = currentWidth + width;
        newWidth = checkWidth(newWidth);
        int height = (int) (newWidth * mRatio);
        setFloatViewXYPostion(width);
        //调整window的大小
        updateWindowWidthAndHeight(newWidth, height);
        //调整视频view的大小
        updateViewLayoutParams(newWidth, height);
    }

    /**
     * 设置悬浮窗坐标位置
     *
     * @param changedWidth view宽度的变化
     */
    private void setFloatViewXYPostion(int changedWidth) {
        changedX += changedWidth / 2;
        int x = startX - changedX;
        int y = (int) (startY - changedX * mRatio);
        int width = mWindowParams.width;
        if (width >= mMinWidth && width <= mMaxWidth) {
            mWindowParams.x = x;
            mWindowParams.y = y;
        }
    }

    private int changedX = 0;//x轴方向的变化量
    private int dx = 0;//缩放宽度变化量
    private int dy = 0;//缩放高度变化量
    private int startX = 0;//缩放前的x坐标
    private int startY = 0;//缩放前的y坐标

    /**
     * 改变窗体大小前，获取一下x，y变化大小
     */
    private void rejuestWindow() {
        dx = rlRoom.getLeft();
        dy = rlRoom.getTop();
        //修正窗体xy坐标
        fixWindowXYPostion();
        updateWindowSize(rlRoom.getWidth());
        if (dx > 0 && dy > 0) {
            removeCallbacks(updateWindowPostionRunnable);
            //回到缩放后的位置，用post，并且0 delay效果好一些
            long duration = 0;
            postDelayed(updateWindowPostionRunnable, duration);
        }
    }

    private final Runnable updateWindowPostionRunnable = new Runnable() {
        @Override
        public void run() {
            updateWindowXYPosition(mWindowParams.x + dx, mWindowParams.y + dy);
        }
    };

    public int getContentViewWidth() {
        return rlRoom != null ? rlRoom.getWidth() : mMinWidth;
    }

    /**
     * 更新内部view的大小
     *
     * @param width 传入变化的宽度
     */
    private void updateContentViewSize(int width) {
        int currentWidth = rlRoom.getWidth();
        int newWidth = currentWidth + width;
        newWidth = checkWidth(newWidth);
        int height = (int) (newWidth * mRatio);
        //调整视频view的大小
        updateViewLayoutParams(newWidth, height);
    }

    /**
     * 更新WM的宽高大小
     */
    private void updateWindowSize(int width) {
        width = checkWidth(width);
        int height = (int) (width * mRatio);
        updateWindowWidthAndHeight(width, height);
    }

    /**
     * 更新WM的宽高大小
     */
    private synchronized void updateWindowWidthAndHeight(int width, int height) {
        if (mWindowManager != null) {
            mWindowParams.width = width;
            mWindowParams.height = height;
            mWindowManager.updateViewLayout(this, mWindowParams);
        }
    }

    /**
     * 修正大小，限制最大和最小值
     *
     * @param width
     * @return
     */
    private int checkWidth(int width) {
        if (width > mMaxWidth) {
            width = mMaxWidth;
        }
        if (width < mMinWidth) {
            width = mMinWidth;
        }
        return width;
    }

    /**
     * 调整悬浮窗坐标位置
     */
    private void fixWindowXYPostion() {
        int width = rlRoom.getWidth();
        if (mWindowParams.x + width >= screenWidth) {
            mWindowParams.x = screenWidth - width - 1;// 不让贴近右边和底部
        }
        if (mWindowParams.x <= 0) {
            mWindowParams.x = 0;
        }
        int height = rlRoom.getHeight();
        if (mWindowParams.y + height >= screenHeight) {
            mWindowParams.y = screenHeight - height - 1;
        }
        if (mWindowParams.y <= statusBarHeight) {
            mWindowParams.y = statusBarHeight;
        }
    }

    private boolean isMoving = false;
    private final OnTouchListener onMovingTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return onTouchEvent2(event);
        }
    };

    private long firstClickTime;//第一次点击
    private int countClick = 0;
    private final Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            //Logger.d("dq-fw canClick=" + canClick);
            if (countClick == 1 && canClick) {
                if (listener != null) {
                    listener.onClick();
                }
            }
            countClick = 0;
        }
    };
    private boolean canClick = true;//是否可以点击
    private final Runnable canClickRunnable = new Runnable() {
        @Override
        public void run() {
            canClick = true;
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper());
    private int scaleCount = 1;//统计双击缩放的次数

    //@Override
    public boolean onTouchEvent2(MotionEvent event) {
        if (isDragged) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = false;
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY();
                xInScreen = xDownInScreen;
                yInScreen = yDownInScreen;
                break;
            case MotionEvent.ACTION_MOVE:
//                showZoomView();
                // 手指移动的时候更新小悬浮窗的位置
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                if (!isMoving) {
                    isMoving = !isClickedEvent();
                } else {
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isClickedEvent()) {
                    countClick++;
                    if (countClick == 1) {
                        firstClickTime = System.currentTimeMillis();
                        handler.removeCallbacks(clickRunnable);
                        handler.postDelayed(clickRunnable, 300);
                    } else if (countClick == 2) {
                        long secondClickTime = System.currentTimeMillis();
                        if (secondClickTime - firstClickTime < 300) {//双击
                            if (listener != null) {
                                listener.onDoubleClick();
                            }
                            scaleCount++;
                            handleScaleEvent();
                            countClick = 0;
                            //2秒后才允许再次点击
                            canClick = false;
                            handler.removeCallbacks(canClickRunnable);
                            handler.postDelayed(canClickRunnable, 1000);
                        }
                    }
                } else {
                    if (null != listener) {
                        listener.onMoved();
                    }
                    countClick = 0;
                }
                updateEditStatus();
                isMoving = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void handleScaleEvent() {
        int scaleLevel = scaleCount % 3;//缩放级别
        int width = getFloatWindowWidth(true, screenWidth, scaleLevel);
        int height = (int) (width * mRatio);
        updateWindowWidthAndHeight(width, height);
        updateViewLayoutParams(width, height);
        Log.d("dq-fw", "handleScaleEvent width=" + width + ",height=" + height);
    }

    public int getFloatWindowWidth(boolean isPortrait, int screenWidth, int scaleLevel) {
        int width = 0;
        if (scaleLevel == 0) {
            width = (int) (isPortrait ? screenWidth * 0.30f : screenWidth * 0.45f);
        } else if (scaleLevel == 1) {
            width = (int) (isPortrait ? screenWidth * 0.40f : screenWidth * 0.65f);
        } else if (scaleLevel == 2) {
            width = (int) (isPortrait ? screenWidth * 0.50f : screenWidth * 0.92f);
        }
        return width;
    }

    private boolean isClickedEvent() {
        int scaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        // 是点击事件
        return Math.abs(xDownInScreen - xInScreen) <= scaledTouchSlop
                && Math.abs(yDownInScreen - yInScreen) <= scaledTouchSlop;
    }

    /**
     * 更新悬浮窗位置
     */
    private void updateViewPosition() {
        int x = (int) (xInScreen - xInView);
        int y = (int) (yInScreen - yInView);
        //防止超出通知栏
        if (y < statusBarHeight) {
            y = statusBarHeight;
        }
        //更新起点
        startX = x;
        startY = y;
        updateWindowXYPosition(x, y);
    }

    /**
     * 更新窗体坐标位置
     *
     * @param x
     * @param y
     */
    private synchronized void updateWindowXYPosition(int x, int y) {
        if (mWindowManager != null) {
            mWindowParams.x = x;
            mWindowParams.y = y;
            mWindowManager.updateViewLayout(this, mWindowParams);
        }
    }

    private void updateEditStatus() {
        handleZoomStatus();
        displayZoomViewDelay();
    }

    private boolean isDragged = false;//是否正在拖拽中
    private boolean isEdit = false;//是否进入编辑状态

    /**
     * 处理缩放按钮的状态
     */
    private void handleZoomStatus() {
        //左，上贴边时，隐藏dragView
        boolean isLeft = mWindowParams.x <= 0;
        boolean isTop = mWindowParams.y <= statusBarHeight;
        if (isLeft || isTop) {
            displayZoomView();
            // 贴边时设置视频margin
//            if (isLeft && isTop) {
//                updateVideoMargin(0, 0, videoViewMargin, videoViewMargin);
//            } else if (isLeft) {
//                updateVideoMargin(0, videoViewMargin, videoViewMargin, 0);
//            } else if (isTop) {
//                updateVideoMargin(videoViewMargin, 0, 0, videoViewMargin);
//            }
        } else {
//            showZoomView();
        }
    }

    private void showZoomView() {
        if (!isEdit) {//&& isSdkGt23 只有6.0及以上才显示缩放按钮
            updateVideoMargin(videoViewMargin, videoViewMargin, 0, 0);
            iv_zoom_btn.setVisibility(VISIBLE);
            rlRoom.setBackgroundColor(getResources().getColor(R.color.float_window_bg_border_edit));
            isEdit = true;
        }
    }

    /**
     * 调整视频view的边距
     */
    private void updateVideoMargin(int left, int top, int right, int bottom) {
        if (rlRoom != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlRoom.getLayoutParams();
            layoutParams.setMargins(left, top, right, bottom);
            rlRoom.setLayoutParams(layoutParams);
        }
    }

    private void displayZoomView() {
        isEdit = false;
        iv_zoom_btn.setVisibility(GONE);
        rlRoom.setBackgroundColor(getResources().getColor(R.color.float_window_bg_border_normal));
    }

    private void displayZoomViewDelay() {
        removeCallbacks(dispalyZoomBtnRunnable);
        postDelayed(dispalyZoomBtnRunnable, 2000);
    }

    private final Runnable dispalyZoomBtnRunnable = new Runnable() {
        @Override
        public void run() {
            displayZoomView();
        }
    };

    @Override
    public FloatViewParams getParams() {
        params.contentWidth = getContentViewWidth();
        params.x = mWindowParams.x;
        params.y = mWindowParams.y;
        params.width = mWindowParams.width;
        params.height = mWindowParams.height;
        return params;
    }

    @Override
    public void setFloatViewListener(FloatViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void closeSound() {
        if (null != listener) {
            listener.onClose();//关闭
            if (videoView != null){
                videoView.pause();
            }
        }
    }

    public void setWindowType(int float_window_type) {
        if (float_window_type == FloatWindowManager.FW_TYPE_APP_DIALOG) {
//            tv_info.setText(getResources().getString(R.string.title_float_window_dialog));
        } else if (float_window_type == FloatWindowManager.FW_TYPE_ALERT_WINDOW) {
//            tv_info.setText(getResources().getString(R.string.title_alert_window));
        }
    }

}
