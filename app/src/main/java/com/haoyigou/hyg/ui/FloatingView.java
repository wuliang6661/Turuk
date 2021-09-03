package com.haoyigou.hyg.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.MessageEvent;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.NetworkUtils;
import com.haoyigou.hyg.view.HeartView;
import com.haoyigou.hyg.view.LockableScrollView;
import com.haoyigou.hyg.view.circlephoto.RoundImageView;
import com.haoyigou.hyg.view.viewpager.ViewPagerSlide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yunfan.player.widget.YfCloudPlayer;
import com.yunfan.player.widget.YfPlayerKit;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.haoyigou.hyg.utils.LogUtils.showToast;

/**
 * Created by Witness on 2019-12-12
 * Describe: 直播View,可拖动
 */
public class FloatingView extends BaseFloatingView implements View.OnClickListener,YfCloudPlayer.OnErrorListener, YfCloudPlayer.OnPreparedListener,
        YfCloudPlayer.OnInfoListener, YfCloudPlayer.OnCompletionListener,
        YfCloudPlayer.OnBufferingUpdateListener,
        YfCloudPlayer.OnCaptureResultListener, YfCloudPlayer.OnGenerateGifListener {

    private Context mContext;
    private View root;

    private boolean isFocus = false;//是否关注了主播
    private boolean isLike = false;//是否喜欢

    /** 主界面,直播 */
    private YfPlayerKit surfaceView;
    private ViewPagerSlide viewPager;
    /**
     * viewPager相关
     */
    private List<View> viewListData = new ArrayList<>();//view数组
    private PagerAdapter pagerAdapter;
    private View liveView;
    private View emptyView;

    /**
     * 直播界面
     */
    private RoundImageView headImage;
    private TextView name;
    private TextView seeNum;
    private TextView focusTxt;
    private RelativeLayout focusBtn;
    private RelativeLayout closeRoom;
    private ImageView shopBtn;
    private EditText inputEdit;
    private RelativeLayout rlSend;
    private Button sendBtn;
    private ImageView likeBtn;
    private ImageView shareBtn;
    private RelativeLayout bottomLayout;
    private RecyclerView talkList;
    private SmartRefreshLayout refreshRoot;
    private HeartView heart;
    private LockableScrollView myScroll;

    /**
     * 弹幕列表
     */
    private CommonAdapter<String> adapter;
    private List<String> data = new ArrayList<>();
    private LinearLayoutManager manager;
    /**
     * 商品弹窗列表
     */
    private CommonAdapter<String> productAdapter;
    private List<String> productData = new ArrayList<>();

    private String liveUrl;


    private int width;//宽
    private int height;//高

    public FloatingView(Context context) {
        super(context);
        this.mContext = context;
//        EventBus.getDefault().register(context);
        width = DisplayUtil.getMobileWidth(context);
        height = DisplayUtils.getScreenHeight(context);
        initView(context);
        getVideoUrl();
        data.add("拍下商品链接1，立减10元");
        data.add("6666666666，主播快撒优惠券！坐等优惠券呀优惠券~");
        data.add("拍下商品链接1");
        setAdapter();
        timer.start();
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static int dip2px(Context context, float dipValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }

    public boolean show(){
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = width;
        lp.height = height;
        lp.windowAnimations = android.R.style.Animation_Translucent;
//        lp.width = dip2px(mContext, 160);
//        lp.height = dip2px(mContext, 90);
        if (root.getParent() == null)
            addView(root, lp);

//        /** 设置缩放动画 */
//        final ScaleAnimation animation = new ScaleAnimation(1f, 0.3f, 1f, 0.3f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);// 从相对于自身0.5倍的位置开始缩放，也就是从控件的位置缩放
//        animation.setDuration(2000);//设置动画持续时间
//
//        /** 常用方法 */
//        //animation.setRepeatCount(int repeatCount);//设置重复次数
//        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        //animation.setStartOffset(long startOffset);//执行前的等待时间
//
//        /** 开始动画 */
//        animation.startNow();

        super.showView(this);
        return true;
    }

    public void dismiss() {
        super.hideView();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        dismiss();
        return true;
    }

    private void initView(Context context){
        this.mContext = context;
        root = View.inflate(context, R.layout.show_room_layout,null);

        surfaceView = root.findViewById(R.id.surfaceView);
        viewPager = root.findViewById(R.id.viewPager);

        LayoutInflater inflater = LayoutInflater.from(context);
        liveView = inflater.inflate(R.layout.live_view, null);
        emptyView = inflater.inflate(R.layout.live_empty, null);
        initLiveView(liveView);//初始化搜索页面
        viewListData.add(emptyView);
        viewListData.add(liveView);
        setViewPagerAdapter();
        viewPager.setCurrentItem(1);

    }

    /**
     * 获取视频链接
     */
    private void getVideoUrl() {
        HttpClient.post(HttpClient.LIVE_URL, new HashMap<String, Object>(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                        liveUrl = object.getString("url");
//                        Log.d("直播地址", "onSuccess: " + liveUrl);
                        inviVideo();
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, mContext);
    }

    /** 初始化播放操作界面 **/
    private void initLiveView(View view) {
        headImage = view.findViewById(R.id.headImage);
        name = view.findViewById(R.id.name);
        seeNum = view.findViewById(R.id.seeNum);
        focusTxt = view.findViewById(R.id.focusTxt);
        focusBtn = view.findViewById(R.id.focusBtn);
        closeRoom = view.findViewById(R.id.closeRoom);
        shopBtn = view.findViewById(R.id.shopBtn);
        inputEdit = view.findViewById(R.id.inputEdit);
        rlSend = view.findViewById(R.id.rlSend);
        sendBtn = view.findViewById(R.id.sendBtn);
        likeBtn = view.findViewById(R.id.likeBtn);
        shareBtn = view.findViewById(R.id.shareBtn);
        bottomLayout = view.findViewById(R.id.bottomLayout);
        talkList = view.findViewById(R.id.talkList);
        refreshRoot = view.findViewById(R.id.refresh_root);
        heart = view.findViewById(R.id.heart);

        manager = new LinearLayoutManager(mContext);
        manager.setStackFromEnd(true);
        talkList.setLayoutManager(manager);
        myScroll.setScrollingEnabled(false);

        focusBtn.setOnClickListener(this);
        closeRoom.setOnClickListener(this);
        shopBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
    }

    /** ViewPager 适配器 */
    private void setViewPagerAdapter() {
        pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewListData.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewListData.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewListData.get(position));


                return viewListData.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.focusBtn:
                if (isFocus) {
                    focusBtn.setBackground(getResources().getDrawable(R.drawable.foucs_bg));
                    focusTxt.setText("关注");
                    focusTxt.setTextColor(Color.parseColor("#ffffff"));
                    focusTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.room_foucs), null, null, null);
                    isFocus = false;
                } else {
                    focusBtn.setBackground(getResources().getDrawable(R.drawable.foucs_not_bg));
                    focusTxt.setText("已关注");
                    focusTxt.setTextColor(Color.parseColor("#E60012"));
                    focusTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                    isFocus = true;
                }
                break;
            case R.id.closeRoom:
//                finish();
//                scaleWindow();
                if (!Settings.canDrawOverlays(mContext)) {
                    EventBus.getDefault().post(new MessageEvent("permission", "yes"));//发给ShowingRoom
                } else {

                }
                break;
            case R.id.shopBtn:
                for (int i = 0; i < 10; i++) {
                    productData.add("");
                }
                showProductDialog();
                break;
            case R.id.sendBtn:
                break;
            case R.id.likeBtn:
                heart.addHeart(10);
                if (isLike) {
                    likeBtn.setImageDrawable(getResources().getDrawable(R.mipmap.zan_no));
                    isLike = false;
                } else {
                    likeBtn.setImageDrawable(getResources().getDrawable(R.mipmap.zan_yes));
                    isLike = true;
                }
                break;
            case R.id.shareBtn:
                break;
        }
    }


    /**
     * ****************************************  初始化直播控件  ***************************************************************
     */
    private void inviVideo() {
//        YfP2PHelper.initNetLib();
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            showToast("网络无连接");
            return;
        }
        if (NetworkUtils.isWifiConnected(mContext)) {   //是否连接wifi
            surfaceView.setHardwareDecoder(true);//设置解码为硬解
            surfaceView.enableBufferState(false);//可开启/关闭缓冲状态
            surfaceView.setSpeed(1);//改变播放速度
            surfaceView.setSurfaceCallBack(mSHCallback);
            surfaceView.setVideoLayout(YfPlayerKit.VIDEO_LAYOUT_MATCH_PARENT);
//        mVideoView.setBufferSize(4 * 1024 * 1024);
            openVideo(liveUrl);
        } else {
            showHintDialog(true);
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
                    surfaceView.setHardwareDecoder(true);//设置解码为硬解
                    surfaceView.enableBufferState(false);//可开启/关闭缓冲状态
                    surfaceView.setSpeed(1);//改变播放速度
                    surfaceView.setSurfaceCallBack(mSHCallback);
                    surfaceView.setVideoLayout(YfPlayerKit.VIDEO_LAYOUT_MATCH_PARENT);
//                    surfaceView.setBufferSize(4 * 1024 * 1024);
                    openVideo(liveUrl);
                } else {
                    openVideo(liveUrl);//简单粗暴地重连
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
            if (surfaceView != null) {
                surfaceView.setVolume(0, 0);
                surfaceView.pause();
            }
            showHintDialog(false);
        } else if (str.equals("wifi")) {
            openVideo(liveUrl);//简单粗暴地重连
        }
    }

    /**
     * ******************************** 设置直播播放 ********************************
     */
    public void openVideo(String path) {
        if (!TextUtils.isEmpty(path) && surfaceView != null) {
            surfaceView.setOnPreparedListener(this);
            surfaceView.setOnErrorListener(this);
            surfaceView.setOnBufferingUpdateListener(this);
            surfaceView.setOnInfoListener(this);
            surfaceView.setonCaptureResultListener(this);
            surfaceView.setOnGenerateGifListener(this);
            startPlayBack(path);
        }
    }

    private void startPlayBack(String path) {
        if (surfaceView == null)
            return;
        surfaceView.setVideoPath(path);
        surfaceView.start();
    }

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceCreated = true;
            if (surfaceView != null) {
                if (needToReopenVideo) {
                    Log.e("wuliang", "重新打开视频！");
                    needToReopenVideo = false;
                    openVideo(liveUrl);
                }
                Log.e("wuliang", "音量恢复！");

                surfaceView.setVolume(1, 1);
                surfaceView.resume();

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
        Log.d("wuliang", "onError from YfPlayerKitDemo:" + what + "__" + extra);
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
    public void onPrepared(YfCloudPlayer mp) {
    }


    private void writeLog(String content) {
//        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
//        LogUtils.d("wuliang", content);
    }

    /**
     * 设置聊天列表
     */
    private void setAdapter() {
        adapter = new CommonAdapter<String>(mContext, R.layout.talk_item_layout, data) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.content, s);
            }
        };
        talkList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    CountDownTimer timer = new CountDownTimer(1000000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //每隔countDownInterval秒会回调一次onTick()方法
            data.add("拍下商品链接1，立减10元");
            adapter.notifyDataSetChanged();
            manager.scrollToPositionWithOffset(adapter.getItemCount() - 1, Integer.MIN_VALUE);
        }

        @Override
        public void onFinish() {

        }
    };

    /**
     * 商品弹窗
     */
    private void showProductDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.show_product_dialog, null);
        TextView productNum = v.findViewById(R.id.productNum);//商品数量
        RelativeLayout rlClose = v.findViewById(R.id.rlClose);
        SmartRefreshLayout refresh_root = v.findViewById(R.id.refresh_root);
        RecyclerView productList = v.findViewById(R.id.productList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        productList.setLayoutManager(manager);
        productAdapter = new CommonAdapter<String>(mContext, R.layout.product_dialog_item_layout, productData) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        productList.setAdapter(productAdapter);
        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.BOTTOM);
        noticeDialog.getWindow().setWindowAnimations(R.style.anim_menu_bottombar);
        noticeDialog.show();
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (DisplayUtils.getScreenWidth(mContext) * 1.08);
        noticeDialog.getWindow().setAttributes(layoutParams);
    }



}
