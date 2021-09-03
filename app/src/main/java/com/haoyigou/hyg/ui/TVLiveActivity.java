package com.haoyigou.hyg.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.ShopEntry;
import com.haoyigou.hyg.entity.TVLiveEntry;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DateTimeUtils;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.MyDialog;
import com.haoyigou.hyg.utils.NetworkUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.TelPhoneUtils;
import com.haoyigou.hyg.view.lgrecycleadapter.LGRecycleViewAdapter;
import com.haoyigou.hyg.view.lgrecycleadapter.LGViewHolder;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.yunfan.player.widget.YfCloudPlayer;
import com.yunfan.player.widget.YfPlayerKit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/8/28.
 * <p>
 * tv直播页面
 */

public class TVLiveActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener,
        YfCloudPlayer.OnErrorListener, YfCloudPlayer.OnPreparedListener,
        YfCloudPlayer.OnInfoListener, YfCloudPlayer.OnCompletionListener,
        YfCloudPlayer.OnBufferingUpdateListener,
        YfCloudPlayer.OnCaptureResultListener, YfCloudPlayer.OnGenerateGifListener {

    @BindView(R.id.shop_list)
    ListView shopList;
    @BindView(R.id.title)
    TextView mtitle;
    @BindView(R.id.live_video_layout)
    RelativeLayout liveVideoLayout;
    @BindView(R.id.surface_view)
    YfPlayerKit mVideoView;
    @BindView(R.id.buttom_layout)
    RelativeLayout buttomLayout;
    @BindView(R.id.voice_button)
    ImageView voiceButton;
    @BindView(R.id.live_all)
    ImageView liveAll;
    /**
     * 头部布局
     */
    View headerView;
    TextView liveTitle;
    RecyclerView liveRecycle;
    TextView hotTitle;

    List<TVLiveEntry> live;
    List<ShopEntry> shop;


    String liveUrl;
    String title;

    boolean isVoice = true;   //默认音量是开启的

    int livePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_tv);
        ButterKnife.bind(this);

        goBack();
        setTitleText("好易购直播");
        invition();
        setListener();
        getVideoUrl();
        getData();
        getShopData();
    }

    /**
     * 初始化界面
     */
    private void invition() {
        headerView = LayoutInflater.from(this).inflate(R.layout.act_live_tv_header, null);


        liveRecycle = (RecyclerView) headerView.findViewById(R.id.live_recycle);
        hotTitle = (TextView) headerView.findViewById(R.id.hot_title);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        liveRecycle.setLayoutManager(manager);
        liveRecycle.setNestedScrollingEnabled(false);


        shopList.addHeaderView(headerView);
        ViewGroup.LayoutParams params = liveVideoLayout.getLayoutParams();
        params.height = (int) (GlobalApplication.screen_width / 1.78);
        liveVideoLayout.setLayoutParams(params);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        liveVideoLayout.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        liveAll.setOnClickListener(this);
        shopList.setOnItemClickListener(this);
        timer.start();
        shopList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (Math.abs(headerView.getTop()) >= hotTitle.getY()) {
                    mtitle.setVisibility(View.VISIBLE);
                } else {
                    mtitle.setVisibility(View.GONE);
                }
            }
        });
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
                        title = object.getString("title");
                        if (StringUtils.isEmpty(title)) {
                            liveTitle.setVisibility(View.GONE);
                        } else {
                            liveTitle.setVisibility(View.VISIBLE);
                            liveTitle.setText(title);
                        }
                        inviVideo();
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }


    /**
     * 获取直播内容
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "0");
        HttpClient.post(HttpClient.LIVE_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                        String array = object.getString("result");
                        live = JSONArray.parseArray(array, TVLiveEntry.class);
                        setLiveAdapter();
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    /**
     * 设置直播列表数据
     */
    private void setLiveAdapter() {
        LGRecycleViewAdapter<TVLiveEntry> adapter = new LGRecycleViewAdapter<TVLiveEntry>(live) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_live_list;
            }

            @Override
            public void convert(LGViewHolder holder, TVLiveEntry tvLiveEntry, int position) {
                Date date = new Date();
                ImageView liveImg = (ImageView) holder.getView(R.id.live_img);
                TextView liveName = (TextView) holder.getView(R.id.live_name);
                TextView liveType = (TextView) holder.getView(R.id.live_type);
                Glide.with(TVLiveActivity.this).load(tvLiveEntry.getLogopic()).into(liveImg);
                liveName.setText(tvLiveEntry.getProductname());
                if (date.getTime() > tvLiveEntry.getEndtime()) {  //已结束
                    liveType.setBackgroundColor(Color.parseColor("#888888"));
                    liveType.setText("已播出");
                    liveType.setTextColor(getResources().getColor(R.color.white));
                } else if (date.getTime() < tvLiveEntry.getEndtime()
                        && date.getTime() > tvLiveEntry.getBegintime()) {   //正在播出
                    liveType.setBackgroundColor(getResources().getColor(R.color.title_bg));
                    liveType.setText("直播中");
                    liveType.setTextColor(getResources().getColor(R.color.white));
                } else if (date.getTime() < tvLiveEntry.getBegintime()) {   //未播出
                    liveType.setBackgroundColor(Color.parseColor("#ffffff"));
                    liveType.setTextColor(getResources().getColor(R.color.title_bg));
                    liveType.setText(DateTimeUtils.formatTime(tvLiveEntry.getBegintime(), DateTimeUtils.DF_HH_MM));
                }
            }
        };
        adapter.setOnItemClickListener(R.id.item_layout, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if ("0".equals(live.get(position).getProductid())) {
                    showLoginDialog();
                    return;
                }
                Intent intent = new Intent(TVLiveActivity.this, HomeWebViewAct.class);
                String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
                String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + live.get(position).getProductid();
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        liveRecycle.setAdapter(adapter);
        moveToPosition();
    }

    private void showLoginDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//        tvTitle.setText("提示");
        TextPaint tp = tvTitle.getPaint();
        tp.setFakeBoldText(true);
        final TextView btnYes = (TextView) view.findViewById(R.id.f_quchecbutton_btn_queding);
        final TextView btlNo = (TextView) view.findViewById(R.id.f_quchecbutton_btn_quxiao);
        final MyDialog builder = new MyDialog(TVLiveActivity.this,0,0,view,R.style.DialogTheme);

        builder.setCancelable(false);
        builder.show();
        //设置对话框显示的View
        //点击确定是的监听
        //拨打电话
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                call("4001188188");
                builder.cancel();
            }
        });


        btlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * 滑动到指定项
     */
    private void moveToPosition() {
        for (int i = 0; i < live.size(); i++) {
            if (new Date().getTime() < live.get(i).getEndtime()
                    && new Date().getTime() > live.get(i).getBegintime()) {   //正在播出
                livePosition = i;
                break;
            }
        }
        liveRecycle.scrollToPosition(livePosition);
    }


    /**
     * 获取商品列表
     */
    private void getShopData() {
        HttpClient.post(HttpClient.LIVE_SHOP, new HashMap<String, Object>(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    String array = object.getString("result");
                    shop = JSONArray.parseArray(array, ShopEntry.class);
                    setShopAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, this);
    }


    /**
     * 设置商品适配器
     */
    private void setShopAdapter() {
        CommonAdapter<ShopEntry> adapter = new CommonAdapter<ShopEntry>(this, R.layout.item_shop_layout, shop) {
            @Override
            protected void convert(ViewHolder viewHolder, ShopEntry item, int position) {
                viewHolder.getView(R.id.add_pic).setVisibility(View.GONE);
                viewHolder.getView(R.id.youhui).setVisibility(View.GONE);
                ImageView shopImg = viewHolder.getView(R.id.shop_img);
                RelativeLayout shopLayout = viewHolder.getView(R.id.shop_img_layout);
                ViewGroup.LayoutParams params = shopLayout.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(TVLiveActivity.this) / 1.83);
                shopLayout.setLayoutParams(params);
                Glide.with(TVLiveActivity.this).load(item.getPiclogo2()).into(shopImg);
                viewHolder.setText(R.id.shop_name, item.getName());
                TextView oldPrice = viewHolder.getView(R.id.old_price);
                oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                TextView price = viewHolder.getView(R.id.price);
                if (item.getPrice().equals(item.getDisprice())) {
                    if (item.getOldprice().equals(item.getDisprice())) {   //只显示折扣价
                        oldPrice.setVisibility(View.GONE);   //不显示但占位置
                        price.setText(String.valueOf("¥ " + Double.parseDouble(item.getDisprice())));
                    } else {
                        oldPrice.setVisibility(View.GONE);
                        price.setText(String.valueOf("¥ " + Double.parseDouble(item.getDisprice())));
                        oldPrice.setText(String.valueOf("市场价：¥ " + Double.parseDouble(item.getOldprice())));
                    }
                } else {
                    oldPrice.setVisibility(View.GONE);
                    price.setText(String.valueOf("¥ " + Double.parseDouble(item.getDisprice())));
                    oldPrice.setText(String.valueOf("原价：¥ " + Double.parseDouble(item.getPrice())));
                }
                if ("0".equals(item.getStore())) {
                    viewHolder.getView(R.id.store_layout).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.store_layout).setVisibility(View.GONE);
                }
            }
        };
        shopList.setAdapter(adapter);
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
                Intent intent = new Intent(this, TVLiveVideoAct.class);
                intent.putExtra("url", liveUrl);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        if ("0".equals(shop.get(position - 1).getId())) {
//            showToast("电视直播专属商品，请拨打电话进行下单购买！");
            showToast("特殊商品，是否拨打电话购买？");
            return;
        }
        Intent intent = new Intent(this, HomeWebViewAct.class);
        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + shop.get(position - 1).getId();
        intent.putExtra("url", url);
        startActivity(intent);
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
//        YfP2PHelper.clearNetSdk();
    }

    //////////////////////////////////////////////直播模块////////////////////////////////////////


    /**
     * 初始化直播控件
     */
    private void inviVideo() {
//        YfP2PHelper.initNetLib();
        if (!NetworkUtils.isNetworkConnected(this)) {
            showToast("网络无连接");
            return;
        }
        if (NetworkUtils.isWifiConnected(this)) {   //是否连接wifi
            mVideoView.setHardwareDecoder(true);//设置解码为硬解
            mVideoView.enableBufferState(false);//可开启/关闭缓冲状态
            mVideoView.setSpeed(1);//改变播放速度
            mVideoView.setSurfaceCallBack(mSHCallback);
            mVideoView.setVideoLayout(YfPlayerKit.VIDEO_LAYOUT_16_9_FIT_PARENT);
//        mVideoView.setBufferSize(4 * 1024 * 1024);
            openVideo(liveUrl);
        } else {
            showHintDialog();
        }
    }


    /**
     * 弹出网络环境提示弹窗
     */
    private void showHintDialog() {
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.hint_dialog_layout, null);//这里必须是final的
        TextView cancle = (TextView) view.findViewById(R.id.off_commit);
        TextView commit = (TextView) view.findViewById(R.id.commit);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
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
                mVideoView.setHardwareDecoder(true);//设置解码为硬解
                mVideoView.enableBufferState(false);//可开启/关闭缓冲状态
                mVideoView.setSpeed(1);//改变播放速度
                mVideoView.setSurfaceCallBack(mSHCallback);
                mVideoView.setVideoLayout(YfPlayerKit.VIDEO_LAYOUT_16_9_FIT_PARENT);
//        mVideoView.setBufferSize(4 * 1024 * 1024);
                openVideo(liveUrl);
            }
        });
        dialog.setView(view);
        dialog.show();
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
//            if (path.contains("http://") || path.contains("rtmp://")) {
//                path = YfP2PHelper.createDirectNetTast(path);
//                startPlayBack(path);
//            } else {
            startPlayBack(path);
//            }
        }
    }


    private void startPlayBack(String path) {
        if (mVideoView == null)
            return;
        Log.d("wuliang", "最后写入的地址：" + path);
        mVideoView.setVideoPath(path);
        mVideoView.start();
    }


    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceCreated = true;
            if (mVideoView != null) {
                if (needToReopenVideo) {
                    Log.e("wuliang", "重新打开视频！");
                    needToReopenVideo = false;
                    openVideo(liveUrl);
                }
                Log.e("wuliang", "音量恢复！");
                mVideoView.setVolume(1, 1);
                mVideoView.resume();
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
    public void onBackPressed() {
        Log.e("wuliang", "页面结束！");
        mBackPressed = true;
//        YfP2PHelper.stopDirectNetBuffer();
        finish();
    }

    @Override
    public void onPrepared(YfCloudPlayer mp) {
    }


    private void writeLog(String content) {
//        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        LogUtils.d("wuliang", content);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView != null)
            if (mBackPressed) {
                Log.e("wuliang", "视频数据重置！");
                mVideoView.release(true);
                mVideoView = null;
            } else {
                Log.e("wuliang", "视频onStop！");
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
            voiceButton.setImageResource(R.drawable.voice);
        }
    }

}
