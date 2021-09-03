package com.haoyigou.hyg.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.MessageEvent;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.ShopEntry;
import com.haoyigou.hyg.entity.TVLiveEntry;
import com.haoyigou.hyg.ui.RoundImageView;
import com.haoyigou.hyg.ui.TVLiveListActivity;
import com.haoyigou.hyg.ui.TVLiveVideoAct;
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
import com.haoyigou.hyg.view.widget.ListViewForScrollView;
import com.yunfan.player.widget.YfCloudPlayer;
import com.yunfan.player.widget.YfPlayerKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/8/28.
 * <p>
 * app直播跳转的tv直播页面
 */

public class LiveAPPFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        YfCloudPlayer.OnErrorListener, YfCloudPlayer.OnPreparedListener,
        YfCloudPlayer.OnInfoListener, YfCloudPlayer.OnCompletionListener,
        YfCloudPlayer.OnBufferingUpdateListener,
        YfCloudPlayer.OnCaptureResultListener, YfCloudPlayer.OnGenerateGifListener {

    @BindView(R.id.shop_list)
    ListView shopList;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.applive_button)
    LinearLayout applivebutton;
    @BindView(R.id.rlRed)
    RelativeLayout rlRed;
    @BindView(R.id.layout_tab)
    LinearLayout layout_tab;
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
//    TextView liveTitle;
    RecyclerView liveRecycle;
    TextView hotTitle;
    List<TVLiveEntry> live;
    List<ShopEntry> shop;
    RelativeLayout rlNowShow;
    TextView txtTitle;
    TextView nowPrice;
    TextView oldPrice;
    ImageView ivBuy;
    View viewTop;
    RelativeLayout rlOpenList;
    String liveUrl;
    String title;
    RelativeLayout rlRemindMe;//取消提醒
    RelativeLayout rlReminded;//提醒我
    private List<TVLiveEntry> liveRemindYes = new ArrayList<>();//已设置提醒列表
    RoundImageView ivRemindPic;
    TextView remindTitle;
    TextView txtRemindPrice;
    TextView remindOldPrice;
    ImageView remindBuy;
    TextView remindTime;
    TextView remindSize;

    private ListViewForScrollView nowPlayProduct;//正在播放列表
    private List<TVLiveEntry> nowLiveData = new ArrayList<>();//正在播放商品列表

    /** 未播出的商品**/
    TextView secondTime;//未设置提醒的商品时间
    RelativeLayout rlSecondRemindMe;//取消提醒
    RelativeLayout rlSecondReminded;//提醒我
    RoundImageView ivSecondPic;//默认商品的图片
    TextView titleSecond;//默认商品的名称
    TextView txtSecondPrice;//现价
    TextView listSecondOldPrice;//原价
    ImageView buySecond;//去买按钮
    ListViewForScrollView live_recycleSecond;//展开的列表
    RelativeLayout rlOpenListSecond;//展开按钮
    TextView openListSecond;//更多的商品数量
    private List<TVLiveEntry> liveRemindNo = new ArrayList<>();//未设置提醒列表
    private RelativeLayout rlBuySecond;
    /** 已播出的商品**/
    TextView thirdTime;//已播出的商品时间
    RoundImageView ivThirdPic;//默认的商品图片
    TextView titleThird;//默认的商品名称
    TextView txtThirdPrice;//现价
    TextView listThirdOldPrice;//原价
    ImageView buyThird;//去买
    ListViewForScrollView live_recycleThird;//展开的列表
    RelativeLayout rlOpenListThird;//展开按钮
    TextView openListThird;////更多的商品数量
    private List<TVLiveEntry> liveRemindShowed = new ArrayList<>();//已播出列表
    private RelativeLayout rlBuyThird;

    boolean isVoice = true;   //默认音量是开启的
    int livePosition = 0;

    private boolean isVisible = false;//当前fragment是否可见
    private String url;//当前播放商品
    private String urlOver;//已播放第一个
    private String urlNot;//未播放第一个
    private String Productid;//当前直播商品id
    private boolean firstItem = false;//第一个列表
    private boolean secondItem = false;//第二个列表
    private boolean thirdItem = false;//第三个列表
    Date date = new Date();

    private String firstNotRemindId="";//未播出的第一项
    private String firstProdectId = "";//未播出的第一项
    private String secondProdectId = "";//未播出的第二项

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_live_app_tv, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        rlRed.setVisibility(View.GONE);
        layout_tab.setVisibility(View.GONE);
        invition();
        setListener();
        getVideoUrl();
        getData();
        getShopData();
        return view;
    }

    /**
     * 初始化界面
     */
    private void invition() {
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.act_live_tv_header, null);
        liveRecycle = (RecyclerView) headerView.findViewById(R.id.live_recycle);
        nowPlayProduct = headerView.findViewById(R.id.nowPlayProduct);
        hotTitle = (TextView) headerView.findViewById(R.id.hot_title);
        ivRemindPic = headerView.findViewById(R.id.ivRemindPic);
        remindTitle = headerView.findViewById(R.id.remindTitle);
        txtRemindPrice = headerView.findViewById(R.id.txtRemindPrice);
        remindOldPrice = headerView.findViewById(R.id.remindOldPrice);
        remindBuy = headerView.findViewById(R.id.remindBuy);
        remindSize = headerView.findViewById(R.id.remindSize);


        /** 已设置提醒的商品**/
        rlNowShow = headerView.findViewById(R.id.rlNowShow);
        txtTitle = headerView.findViewById(R.id.txtTitle);
        nowPrice = headerView.findViewById(R.id.nowPrice);
        oldPrice = headerView.findViewById(R.id.oldPrice);
        ivBuy = headerView.findViewById(R.id.ivBuy);
        viewTop = headerView.findViewById(R.id.viewTop);
        rlOpenList = headerView.findViewById(R.id.rlOpenList);
        rlRemindMe = headerView.findViewById(R.id.rlRemindMe);
        rlReminded = headerView.findViewById(R.id.rlReminded);
        rlBuySecond = headerView.findViewById(R.id.rlBuySecond);
        rlOpenList.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        liveRecycle.setLayoutManager(manager);
        liveRecycle.setNestedScrollingEnabled(false);
        /** 未设置提醒的商品**/
        secondTime = headerView.findViewById(R.id.secondTime);
        rlSecondRemindMe = headerView.findViewById(R.id.rlSecondRemindMe);
        rlSecondReminded = headerView.findViewById(R.id.rlSecondReminded);
        ivSecondPic = headerView.findViewById(R.id.ivSecondPic);
        titleSecond = headerView.findViewById(R.id.titleSecond);
        txtSecondPrice = headerView.findViewById(R.id.txtSecondPrice);
        listSecondOldPrice = headerView.findViewById(R.id.listSecondOldPrice);
        live_recycleSecond = headerView.findViewById(R.id.live_recycleSecond);
        rlOpenListSecond = headerView.findViewById(R.id.rlOpenListSecond);
        openListSecond = headerView.findViewById(R.id.openListSecond);
        buySecond= headerView.findViewById(R.id.buySecond);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity());
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
//        live_recycleSecond.setLayoutManager(manager2);
//        live_recycleSecond.setNestedScrollingEnabled(false);
        /** 已播出的商品**/
        thirdTime = headerView.findViewById(R.id.thirdTime);
        ivThirdPic = headerView.findViewById(R.id.ivThirdPic);
        titleThird = headerView.findViewById(R.id.titleThird);
        txtThirdPrice = headerView.findViewById(R.id.txtThirdPrice);
        listThirdOldPrice = headerView.findViewById(R.id.listThirdOldPrice);
        buyThird = headerView.findViewById(R.id.buyThird);
        live_recycleThird= headerView.findViewById(R.id.live_recycleThird);
        rlOpenListThird = headerView.findViewById(R.id.rlOpenListThird);
        openListThird = headerView.findViewById(R.id.openListThird);
        LinearLayoutManager manager3 = new LinearLayoutManager(getActivity());
        manager3.setOrientation(LinearLayoutManager.VERTICAL);
        rlBuyThird = headerView.findViewById(R.id.rlBuyThird);
//        live_recycleThird.setLayoutManager(manager3);
//        live_recycleThird.setNestedScrollingEnabled(false);

        shopList.addHeaderView(headerView);
        ViewGroup.LayoutParams params = liveVideoLayout.getLayoutParams();
        params.height = (int) (GlobalApplication.screen_width / 1.78);
        liveVideoLayout.setLayoutParams(params);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)viewTop.getLayoutParams();
        lp.height= (int) (GlobalApplication.screen_width / 1.78);
        viewTop.setLayoutParams(lp);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        applivebutton.setOnClickListener(this);
        liveVideoLayout.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        liveAll.setOnClickListener(this);
        shopList.setOnItemClickListener(this);
        ivBuy.setOnClickListener(this);
        rlRemindMe.setOnClickListener(this);
        rlReminded.setOnClickListener(this);
        rlSecondRemindMe.setOnClickListener(this);
        rlSecondReminded.setOnClickListener(this);
        rlOpenListSecond.setOnClickListener(this);
        buySecond.setOnClickListener(this);
        rlOpenListThird.setOnClickListener(this);
        buyThird.setOnClickListener(this);
        rlNowShow.setOnClickListener(this);
        rlBuySecond.setOnClickListener(this);
        rlBuyThird.setOnClickListener(this);

        timer.start();
        shopList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (Math.abs(headerView.getTop()) >= (hotTitle.getY() + rlNowShow.getY())) {
                    llTitle.setVisibility(View.GONE);
                } else {
                    llTitle.setVisibility(View.GONE);
                }
                int lastVisibleCount = firstVisibleItem + visibleItemCount;
                int offset = 0;
                if (firstVisibleItem == 0) {
                    View firstItem = shopList.getChildAt(0);
                    if (firstItem != null) {
                        offset = 0 - firstItem.getTop();
                    }
                } else {
                    offset = (int) (GlobalApplication.screen_width / 1.78);
                }
                float percent = (offset * 1f) / ((int) (GlobalApplication.screen_width / 1.78) * 1f) ;
                if (percent >= 0 && percent <= 1) {
                    if (percent == 0 && offset == 0){
                        ViewGroup.LayoutParams params = liveVideoLayout.getLayoutParams();
                        params.height = (int) (GlobalApplication.screen_width / 1.78);
                        params.width = GlobalApplication.screen_width;
                        liveVideoLayout.setLayoutParams(params);
                    }
                }else {
                    //当视频播放器宽度高度缩小到屏幕宽度四分之一后不再缩小
                    if ((int) ((GlobalApplication.screen_width / 1.78) / percent) > (int) (GlobalApplication.screen_width * 0.25)
                            && (int) (GlobalApplication.screen_width / percent) > (int) (GlobalApplication.screen_width * 0.25) ) {
                        ViewGroup.LayoutParams params = liveVideoLayout.getLayoutParams();
                        params.height = (int) ((GlobalApplication.screen_width / 1.78) / percent);
                        params.width = (int) (GlobalApplication.screen_width / percent);
                        liveVideoLayout.setLayoutParams(params);
                    }
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
                        Log.d("直播地址", "onSuccess: "+liveUrl);
                        title = object.getString("title");
//                        if (StringUtils.isEmpty(title)) {
//                            liveTitle.setVisibility(View.GONE);
//                        } else {
//                            liveTitle.setVisibility(View.VISIBLE);
//                            liveTitle.setText(title);
//                        }
                        inviVideo();
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity());
    }


    /**
     * 获取直播内容
     */

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "0");
        params.put("parentLocation", MApplication.tvParentLocation);
        HttpClient.post(HttpClient.LIVE_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                        String array = object.getString("result");
                        live = JSONArray.parseArray(array, TVLiveEntry.class);
                        for (int i=0;i<live.size();i++){
                            if (date.getTime() < live.get(i).getEndtime()
                                    && date.getTime() > live.get(i).getBegintime()) {   //正在播出
                                nowLiveData.add(live.get(i));
                                setLiveAdapter();
                            }
                            if (date.getTime() > live.get(i).getEndtime()) {  //已结束
                                liveRemindShowed.add(live.get(i));
                            }
                            if (date.getTime() < live.get(i).getBegintime()){//未播出
                                liveRemindNo.add(live.get(i));
                            }
                        }
                        if (liveRemindNo != null && liveRemindNo.size()>0){//未提醒第一项
                            Glide.with(getActivity()).load(liveRemindNo.get(0).getLogopic()).into(ivSecondPic);
                            titleSecond.setText(String.valueOf(liveRemindNo.get(0).getProductname()));
                            if ("0".equals(liveRemindNo.get(0).getProductid())) {
                                txtSecondPrice.setText(String.format("%s","【电话订购】"));
                                listSecondOldPrice.setVisibility(View.GONE);
                            }else {
                                listSecondOldPrice.setVisibility(View.VISIBLE);
                                txtSecondPrice.setText(String.format("¥%s",liveRemindNo.get(0).getDisprice()));
                                listSecondOldPrice.setText(String.format("¥%s",liveRemindNo.get(0).getOldprice()));
                                listSecondOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                            }
//                            txtSecondPrice.setText(String.format("¥%s",liveRemindNo.get(0).getDisprice()));
//                            listSecondOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                            listSecondOldPrice.setText(String.format("¥%s",liveRemindNo.get(0).getOldprice()));
                            secondTime.setText(String.format("%s-%s",DateTimeUtils.formatTime(liveRemindNo.get(0).getBegintime(), DateTimeUtils.DF_HH_MM),
                                    DateTimeUtils.formatTime(liveRemindNo.get(0).getEndtime(), DateTimeUtils.DF_HH_MM)));
                            firstNotRemindId = liveRemindNo.get(0).getId();
                            firstProdectId = liveRemindNo.get(0).getProductid();
                            urlNot = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + liveRemindNo.get(0).getProductid()+
                                    "&parentLocation="+MApplication.tvParentLocation;
                            liveRemindNo.remove(0);
                        }
                        openListSecond.setText(String.format("更多商品(%s)",liveRemindNo.size()));
                        if (liveRemindShowed != null && liveRemindShowed.size()>0){//已播出第一项
                            Glide.with(getActivity()).load(liveRemindShowed.get(0).getLogopic()).into(ivThirdPic);
                            titleThird.setText(String.valueOf(liveRemindShowed.get(0).getProductname()));
                            if ("0".equals(liveRemindShowed.get(0).getProductid())) {
                                txtThirdPrice.setText(String.format("%s","【电话订购】"));
                                listThirdOldPrice.setVisibility(View.GONE);
                            }else {
                                listThirdOldPrice.setVisibility(View.VISIBLE);
                                txtThirdPrice.setText(String.format("¥%s",liveRemindShowed.get(0).getDisprice()));
                                listThirdOldPrice.setText(String.format("¥%s",liveRemindShowed.get(0).getOldprice()));
                                listThirdOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                            }

//                            txtThirdPrice.setText(String.format("¥%s",liveRemindShowed.get(0).getDisprice()));
//                            listThirdOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                            listThirdOldPrice.setText(String.format("¥%s",liveRemindShowed.get(0).getOldprice()));
                            thirdTime.setText(String.format("%s-%s",DateTimeUtils.formatTime(liveRemindShowed.get(0).getBegintime(), DateTimeUtils.DF_HH_MM),
                                    DateTimeUtils.formatTime(liveRemindShowed.get(0).getEndtime(), DateTimeUtils.DF_HH_MM)));
                            secondProdectId = liveRemindShowed.get(0).getProductid();
                            urlOver = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + liveRemindShowed.get(0).getProductid()+
                                    "&parentLocation="+MApplication.tvParentLocation;
                            liveRemindShowed.remove(0);
                        }
                        openListThird.setText(String.format("更多商品(%s)",liveRemindShowed.size()));
//                        setLiveAdapter();
                        setShowedAdapter();//已播出
                        if (liveRemindShowed.size()>0) {
                            live_recycleSecond.setVisibility(View.VISIBLE);
                            setNotRemindAdapter();//未播出
                        }else {
                            live_recycleSecond.setVisibility(View.GONE);
                        }
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity());
    }

    /**
     * 设置正在播放列表数据
     */
    private void setLiveAdapter() {
        com.zhy.adapter.abslistview.CommonAdapter<TVLiveEntry> adapter = new com.zhy.adapter.abslistview.CommonAdapter<TVLiveEntry>(
                getActivity(),R.layout.live_item_layout,nowLiveData) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, final TVLiveEntry item, int position) {
                holder.setText(R.id.txtTitle,item.getProductname());
                TextView oldPrice = holder.getConvertView().findViewById(R.id.oldPrice);
                if ("0".equals(item.getProductid())) {
                    holder.setText(R.id.nowPrice,String.format("%s","【电话订购】"));
                    holder.getView(R.id.oldPrice).setVisibility(View.GONE);
                }else {
                    holder.getView(R.id.oldPrice).setVisibility(View.VISIBLE);
                    holder.setText(R.id.nowPrice,String.format("¥%s",item.getDisprice()));
                    holder.setText(R.id.oldPrice,String.format("¥%s",item.getOldprice()));
                    oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                }
                holder.getView(R.id.ivBuy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
                        url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + item.getProductid()+
                                "&parentLocation="+MApplication.tvParentLocation;
                        Productid = item.getProductid();
                        if (Productid == null || "0".equals(Productid)) {
                            showLoginDialog();
                            return;
                        }
                        Intent intent2 = new Intent(getActivity(), HomeWebViewAct.class);
                        intent2.putExtra("url", url);
                        startActivity(intent2);
                    }
                });
            }

        };
        nowPlayProduct.setAdapter(adapter);
        moveToPosition();
    }


    /**
     * 设置未播出列表数据
     */
    private void setNotRemindAdapter() {
        com.zhy.adapter.abslistview.CommonAdapter<TVLiveEntry> adapter = new com.zhy.adapter.abslistview.CommonAdapter<TVLiveEntry>(getActivity(),
                R.layout.new_live_item,liveRemindNo) {
            @Override
            protected void convert(final com.zhy.adapter.abslistview.ViewHolder holder, final TVLiveEntry item, int position) {
                ImageView liveImg = (ImageView) holder.getView(R.id.ivPic);
                TextView listOldPrice = (TextView) holder.getView(R.id.listOldPrice);
                Glide.with(getActivity()).load(item.getLogopic()).into(liveImg);
                holder.setText(R.id.time,String.format("%s-%s",DateTimeUtils.formatTime(item.getBegintime(), DateTimeUtils.DF_HH_MM),
                        DateTimeUtils.formatTime(item.getEndtime(), DateTimeUtils.DF_HH_MM)));
                holder.setText(R.id.txtState,String.format("%s","敬请期待"));
                holder.setText(R.id.title,String.format("%s",item.getProductname()));
                if ("0".equals(item.getProductid())) {
                    holder.setText(R.id.txtPrice,String.format("%s","【电话订购】"));
                    listOldPrice.setVisibility(View.GONE);
                }else {
                    listOldPrice.setVisibility(View.VISIBLE);
                    holder.setText(R.id.txtPrice,String.format("¥%s",item.getDisprice()));
                    listOldPrice.setText(String.format("¥%s",item.getOldprice()));
                    listOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                }
                holder.getView(R.id.rlReminded).setVisibility(View.VISIBLE);
                if (SharedPreferencesUtils.getInstance().getString("id","").equals(item.getId())){
                    rlRemindMe.setVisibility(View.VISIBLE);
                    rlReminded.setVisibility(View.GONE);
                    rlRemindMe.setEnabled(false);
                }else {
                    rlRemindMe.setVisibility(View.GONE);
                    rlReminded.setVisibility(View.VISIBLE);
                    rlReminded.setEnabled(true);
                }
                holder.getView(R.id.rlReminded).setOnClickListener(new View.OnClickListener() {//提醒我
                    @Override
                    public void onClick(View v) {
                        setLiveTishi(item.getId());
                        if (StateMessage.IS_LOGIN) {
                            holder.getView(R.id.rlReminded).setVisibility(View.GONE);
                            holder.getView(R.id.rlRemindMe).setVisibility(View.VISIBLE);
                        }else {
                            holder.getView(R.id.rlReminded).setVisibility(View.VISIBLE);
                            holder.getView(R.id.rlRemindMe).setVisibility(View.GONE);
                        }

                    }
                });
                holder.getView(R.id.rlRemindMe).setOnClickListener(new View.OnClickListener() {//取消提醒
                    @Override
                    public void onClick(View v) {
//                        holder.getView(R.id.rlReminded).setVisibility(View.VISIBLE);
//                        holder.getView(R.id.rlRemindMe).setVisibility(View.GONE);
                    }
                });
                holder.getView(R.id.buy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("0".equals(item.getProductid())) {
                            showLoginDialog();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
                        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + item.getProductid()+
                                "&parentLocation="+MApplication.tvParentLocation;
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
                holder.getView(R.id.rlItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("0".equals(item.getProductid())) {
                            showLoginDialog();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
                        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + item.getProductid()+
                                "&parentLocation="+MApplication.tvParentLocation;
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
            }
        };
        live_recycleSecond.setAdapter(adapter);
        moveToPosition();
    }


    /**
     * 设置已播出的列表
     */
    private void setShowedAdapter() {
        com.zhy.adapter.abslistview.CommonAdapter<TVLiveEntry> adapter = new com.zhy.adapter.abslistview.CommonAdapter<TVLiveEntry>(getActivity(),
                R.layout.new_live_item,liveRemindShowed) {
            @Override
            protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, final TVLiveEntry item, int position) {
                ImageView liveImg = (ImageView) holder.getView(R.id.ivPic);
                TextView listOldPrice = (TextView) holder.getView(R.id.listOldPrice);
                Glide.with(getActivity()).load(item.getLogopic()).into(liveImg);
                holder.setText(R.id.time,String.format("%s-%s",DateTimeUtils.formatTime(item.getBegintime(), DateTimeUtils.DF_HH_MM),
                        DateTimeUtils.formatTime(item.getEndtime(), DateTimeUtils.DF_HH_MM)));
                holder.setText(R.id.txtState,String.format("%s","已播出"));
                holder.setText(R.id.title,String.format("%s",item.getProductname()));
                if ("0".equals(item.getProductid())) {
                    holder.setText(R.id.txtPrice,String.format("%s","【电话订购】"));
                    listOldPrice.setVisibility(View.GONE);
                }else {
                    listOldPrice.setVisibility(View.VISIBLE);
                    holder.setText(R.id.txtPrice,String.format("¥%s",item.getDisprice()));
                    listOldPrice.setText(String.format("¥%s",item.getOldprice()));
                    listOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                }
                holder.getView(R.id.rlReminded).setVisibility(View.GONE);

                holder.getView(R.id.buy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("0".equals(item.getProductid())) {
                            showLoginDialog();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
                        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + item.getProductid()+
                                "&parentLocation="+MApplication.tvParentLocation;
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
                holder.getView(R.id.rlItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("0".equals(item.getProductid())) {
                            showLoginDialog();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
                        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + item.getProductid()+
                                "&parentLocation="+MApplication.tvParentLocation;
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
            }
        };
        live_recycleThird.setAdapter(adapter);
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
        final MyDialog builder = new MyDialog(getActivity(),0,0,view,R.style.DialogTheme);

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
     * 设置直播提醒
     */
    private void setLiveTishi(final String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("distributorId", disId);
        params.put("id", id);
        params.put("parentLocation",MApplication.tvParentLocation);
        HttpClient.post(HttpClient.LIVE_TISHI, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                        showToast("设置提醒成功！");
                        SharedPreferencesUtils.getInstance().putString("id",id);
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity());
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
        Map<String, Object> params = new HashMap<>();
        params.put("parentLocation", MApplication.tvParentLocation);
        HttpClient.post(HttpClient.LIVE_SHOP, params, new AsyncHttpResponseHandler() {
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
        }, getActivity());
    }


    /**
     * 设置商品适配器
     */
    private void setShopAdapter() {
        CommonAdapter<ShopEntry> adapter = new CommonAdapter<ShopEntry>(getActivity(), R.layout.item_shop_layout, shop) {
            @Override
            protected void convert(ViewHolder viewHolder, ShopEntry item, int position) {
                Date date = new Date();
                viewHolder.getView(R.id.add_pic).setVisibility(View.GONE);
                viewHolder.getView(R.id.youhui).setVisibility(View.GONE);
                ImageView shopImg = viewHolder.getView(R.id.shop_img);
                RelativeLayout shopLayout = viewHolder.getView(R.id.shop_img_layout);
                ViewGroup.LayoutParams params = shopLayout.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(getActivity()) / 1.83);
                shopLayout.setLayoutParams(params);
                Glide.with(getActivity()).load(item.getPiclogo2()).into(shopImg);
                viewHolder.setText(R.id.shop_name, item.getName());
                TextView oldPrice = viewHolder.getView(R.id.old_price);
                oldPrice.setVisibility(View.GONE);
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
                Intent intent = new Intent(getActivity(), TVLiveVideoAct.class);
                intent.putExtra("url", liveUrl);
                startActivityForResult(intent, 1);
                break;
//            case R.id.applive_button:
//                finish();
//                break;
            case R.id.ivBuy://点击进入已设置提醒的第一个商品
            case R.id.rlNowShow:
                if (Productid == null || "0".equals(Productid)) {
                    showLoginDialog();
                    return;
                }
                Intent intent2 = new Intent(getActivity(), HomeWebViewAct.class);
                intent2.putExtra("url", url);
                startActivity(intent2);
                break;
            case R.id.rlOpenList:
                if (!firstItem){
                    liveRecycle.setVisibility(View.VISIBLE);
                    firstItem = true;
                }else {
                    liveRecycle.setVisibility(View.GONE);
                    firstItem = false;
                }
                break;
            case R.id.rlRemindMe://已设置提醒的取消提醒

                break;
            case R.id.rlSecondReminded://未设置提醒的设置提醒
                setLiveTishi(firstNotRemindId);
                if (StateMessage.IS_LOGIN) {
                    rlSecondRemindMe.setVisibility(View.VISIBLE);
                    rlSecondReminded.setVisibility(View.GONE);
                }else {
                    rlSecondRemindMe.setVisibility(View.GONE);
                    rlSecondReminded.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rlOpenListSecond://展开已播出列表
                if (!secondItem){
                    live_recycleSecond.setVisibility(View.VISIBLE);
                    openListSecond.setText("收起");
                    Drawable drawable1 = getResources().getDrawable(R.mipmap.tv_up);
                    drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
                    openListSecond.setCompoundDrawables(null,null,drawable1,null);
                    openListSecond.setCompoundDrawablePadding(7);
                    secondItem = true;
                }else {
                    live_recycleSecond.setVisibility(View.GONE);
                    openListSecond.setText(String.format("更多商品(%s)",liveRemindNo.size()));
                    Drawable drawable1 = getResources().getDrawable(R.mipmap.tv_down);
                    drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
                    openListSecond.setCompoundDrawables(null,null,drawable1,null);
                    openListSecond.setCompoundDrawablePadding(7);
                    secondItem = false;
                }
                break;
            case R.id.rlBuySecond:
            case R.id.buySecond://点击进入未播出的第一个商品
                if (firstProdectId == null || "0".equals(firstProdectId)) {
                    showLoginDialog();
                    return;
                }
                Intent intent4 = new Intent(getActivity(), HomeWebViewAct.class);
                intent4.putExtra("url", urlNot);
                startActivity(intent4);
                break;
            case R.id.rlOpenListThird://展开已播出列表
                if (!thirdItem){
                    live_recycleThird.setVisibility(View.VISIBLE);
                    openListThird.setText("收起");
                    Drawable drawable1 = getResources().getDrawable(R.mipmap.tv_up);
                    drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
                    openListThird.setCompoundDrawables(null,null,drawable1,null);
                    openListThird.setCompoundDrawablePadding(7);
                    thirdItem = true;
                }else {
                    live_recycleThird.setVisibility(View.GONE);
                    openListThird.setText(String.format("更多商品(%s)",liveRemindShowed.size()));
                    Drawable drawable1 = getResources().getDrawable(R.mipmap.tv_down);
                    drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
                    openListThird.setCompoundDrawables(null,null,drawable1,null);
                    openListThird.setCompoundDrawablePadding(7);
                    thirdItem = false;
                }
                break;
            case R.id.rlBuyThird:
            case R.id.buyThird://点击进入已播出的第一个商品
                if (secondProdectId == null || "0".equals(secondProdectId)) {
                    showLoginDialog();
                    return;
                }
                Intent intent3 = new Intent(getActivity(), HomeWebViewAct.class);
                intent3.putExtra("url", urlOver);
                startActivity(intent3);
                break;
                default:
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
        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + shop.get(position - 1).getId()
                +"&parentLocation="+MApplication.tvParentLocation;
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
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
//        YfP2PHelper.clearNetSdk();
        EventBus.getDefault().unregister(this);
    }

    //////////////////////////////////////////////直播模块////////////////////////////////////////


    /**
     * 初始化直播控件
     */
    private void inviVideo() {
//        YfP2PHelper.initNetLib();
        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            showToast("网络无连接");
            return;
        }
        if (NetworkUtils.isWifiConnected(getActivity())) {   //是否连接wifi
            mVideoView.setHardwareDecoder(true);//设置解码为硬解
            mVideoView.enableBufferState(false);//可开启/关闭缓冲状态
            mVideoView.setSpeed(1);//改变播放速度
            mVideoView.setSurfaceCallBack(mSHCallback);
            mVideoView.setVideoLayout(YfPlayerKit.VIDEO_LAYOUT_16_9_FIT_PARENT);
//        mVideoView.setBufferSize(4 * 1024 * 1024);
            openVideo(liveUrl);
        } else {
            showHintDialog(true);
        }
    }


    /**
     * 弹出网络环境提示弹窗
     */
    private void showHintDialog(final boolean isFirst) {
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
                if (isFirst) {
                    mVideoView.setHardwareDecoder(true);//设置解码为硬解
                    mVideoView.enableBufferState(false);//可开启/关闭缓冲状态
                    mVideoView.setSpeed(1);//改变播放速度
                    mVideoView.setSurfaceCallBack(mSHCallback);
                    mVideoView.setVideoLayout(YfPlayerKit.VIDEO_LAYOUT_16_9_FIT_PARENT);
//        mVideoView.setBufferSize(4 * 1024 * 1024);
                    openVideo(liveUrl);
                }else {
                    openVideo(liveUrl);//简单粗暴地重连
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /** 监测网络变化 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals("4G")) {
            if (mVideoView != null) {
                mVideoView.setVolume(0, 0);
                mVideoView.pause();
            }
            showHintDialog(false);
        }else if (str.equals("wifi")){
            openVideo(liveUrl);//简单粗暴地重连
        }
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
//        Log.d("wuliang", "最后写入的地址：" + path);
        mVideoView.setVideoPath(path);
        mVideoView.start();
        if (isVisible) {
            mVideoView.setVolume(1, 1);
            mVideoView.resume();
        }else {
            mVideoView.resume();
            mVideoView.setVolume(0, 0);
        }
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
                if (isVisible) {
                    mVideoView.setVolume(1, 1);
                    mVideoView.resume();
                }else {
                    mVideoView.resume();
                    mVideoView.setVolume(0, 0);
                }
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


    @Override
    public void onStop() {
        super.onStop();
        if (mVideoView != null)
//            if (mBackPressed) {
//                Log.e("wuliang", "视频数据重置！");
//                mVideoView.release(true);
//                mVideoView = null;
//            } else {
                Log.e("wuliang", "视频onStop！");
                mVideoView.setVolume(0, 0);//可以使用暂停的方式，也可以使用设置音量为0的方式
//                Log.d(TAG,"mVideoView.pause()");
//                mVideoView.pause();
//            }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null){
//            mVideoView.pause();
            mVideoView.setVolume(0, 0);//可以使用暂停的方式，也可以使用设置音量为0的方式
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideoView.release(true);
        mVideoView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mVideoView != null) {
//            if (!isVisible) {
//                mVideoView.setVolume(1, 1);
//                voiceButton.setImageResource(R.drawable.voice);
//            }else {
//                mVideoView.setVolume(0, 0);//可以使用暂停的方式，也可以使用设置音量为0的方式
//            }
//        }
//        if(!isVisible){
//            if (mVideoView != null){
//                mVideoView.setVolume(0, 0);//可以使用暂停的方式，也可以使用设置音量为0的方式
//            }
//        } else {
//            if (mVideoView != null){
//                mVideoView.setVolume(1, 1);
//                voiceButton.setImageResource(R.drawable.voice);
//            }
//        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisible = hidden;
        if(hidden){
            if (mVideoView != null){
                mVideoView.setVolume(0, 0);//可以使用暂停的方式，也可以使用设置音量为0的方式
            }
        } else {
            if (mVideoView != null){
                mVideoView.setVolume(1, 1);
                voiceButton.setImageResource(R.drawable.voice);
            }
        }
    }


    public void setVoiceNone(boolean see){
        if (see){
            if (mVideoView != null){
                mVideoView.setVolume(1, 1);
                voiceButton.setImageResource(R.drawable.voice);
            }
            isVisible = true;
        }else {
            if (mVideoView != null){
                mVideoView.setVolume(0, 0);//可以使用暂停的方式，也可以使用设置音量为0的方式
            }
            isVisible = false;
        }
    }

    /**
     * 打开声音
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent){
        if (messageEvent.getMessageType().equals("openVoice")){
//            mVideoView.setVolume(1, 1);
//            isVisible = true;
        }else if (messageEvent.getMessageType().equals("closeVoice")){
//            mVideoView.setVolume(0, 0);
//            isVisible = false;
        }
    }
}

