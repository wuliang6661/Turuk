package com.haoyigou.hyg.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.LiveBean;
import com.haoyigou.hyg.entity.LiveLunBean;
import com.haoyigou.hyg.entity.NewLiveLunBean;
import com.haoyigou.hyg.ui.LoginActivity;
import com.haoyigou.hyg.ui.RoundImageView;
import com.haoyigou.hyg.ui.ShowingRoom;
import com.haoyigou.hyg.ui.SmartFooter;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.TopAndButtomListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xbanner.XBanner;

/**
 * Created by Witness on 2019-12-10
 * Describe: 直播
 */
public class ShowOnlineFragment extends BaseActivity {


    @BindView(R.id.listview)
    TopAndButtomListView listview;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshRoot;
    @BindView(R.id.title_text)
    TextView titleText;

    private CommonAdapter<LiveBean> adapter;
    private List<LiveBean> data = new ArrayList<>();

    private LiveLunBean liveLunBean;
    private List<NewLiveLunBean> liveLunbo = new ArrayList<>();


    private XBanner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_online_layout);
        ButterKnife.bind(this);
        initView();
        startProgressDialog("", this);
        getLiveList();
        getLunbo();
        goBack();
    }

    protected void initView() {
        setTitleText("优选直播");
        refreshRoot.setRefreshHeader(new SmartHeader(ShowOnlineFragment.this));
        refreshRoot.setRefreshFooter(new SmartFooter(ShowOnlineFragment.this));
        refreshRoot.setFooterHeight(60);
        refreshRoot.setHeaderHeight(60);
        refreshRoot.setEnableLoadMore(false);
        View view = LayoutInflater.from(this).inflate(R.layout.show_header_layout, null);
        banner = view.findViewById(R.id.banner);
        ViewGroup.LayoutParams param = banner.getLayoutParams();
        param.height = (int) (DisplayUtils.getScreenWidth(this) * 0.55);
        banner.setLayoutParams(param);
        listview.addHeaderView(view);
        refreshRoot.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getLiveList();
                getLunbo();
                refreshLayout.finishRefresh(2000);
                refreshRoot.finishRefresh(2000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 设置商品数据
     */
    private int mPosition = -1;
    private boolean isReminded = false;//是否提醒了

    private void setShowAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            return;
        }
        adapter = new CommonAdapter<LiveBean>(this,
                R.layout.onshow_item_layout, data) {
            @Override
            protected void convert(ViewHolder viewHolder, final LiveBean item, final int position) {
                RoundImageView roundImage = viewHolder.getConvertView().findViewById(R.id.roundImage);
                ViewGroup.LayoutParams params = roundImage.getLayoutParams();
                params.width = (int) (GlobalApplication.screen_width * 0.68);
                params.height = (int) (GlobalApplication.screen_width * 0.55);
                roundImage.setLayoutParams(params);

                RelativeLayout rlRemind = viewHolder.getConvertView().findViewById(R.id.rlRemind);
                TextView txtRemind = viewHolder.getConvertView().findViewById(R.id.txtRemind);

                if (item.getStatus() == 2) {//回放
                    viewHolder.getView(R.id.txtState).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.txtState, "回放");
                } else if (item.getStatus() == 1) {//直播
                    viewHolder.getView(R.id.txtState).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.txtState, "直播中");
                } else if (item.getStatus() == 0) {//预告
                    viewHolder.getView(R.id.txtState).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.txtState, "预告");
                } else {
                    viewHolder.getView(R.id.txtState).setVisibility(View.VISIBLE);
                    viewHolder.setText(R.id.txtState, "直播中");
                }
                Glide.with(ShowOnlineFragment.this).
                        load(item.getListPic()).asBitmap().
                        into((ImageView) viewHolder.getConvertView().findViewById(R.id.roundImage));
                if (item.getStatus() == 2 || item.getStatus() == 1) {//直播或回放
                    viewHolder.setText(R.id.seeNum, String.format("%s 观看", item.getPv()));
                    viewHolder.getView(R.id.rlRemind).setVisibility(View.GONE);
                } else {
                    viewHolder.setText(R.id.seeNum, String.format("%s", item.getStartTime()));
                    viewHolder.getView(R.id.rlRemind).setVisibility(View.VISIBLE);
                }
                Glide.with(ShowOnlineFragment.this).
                        load(item.getAnchor().getHeader()).asBitmap().
                        into((ImageView) viewHolder.getConvertView().findViewById(R.id.headImage));
                viewHolder.setText(R.id.name, String.format("%s", item.getAnchor().getName()));
                viewHolder.setText(R.id.place, String.format("%s", item.getAnchor().getCity()));
                if (item.getProductList() != null && item.getProductList().size() > 0) {
                    viewHolder.getView(R.id.productOne).setVisibility(View.VISIBLE);
                    viewHolder.getView(R.id.productOnePrice).setVisibility(View.VISIBLE);

                    Glide.with(ShowOnlineFragment.this).
                            load(item.getProductList().get(0).getLogo()).asBitmap().
                            into((ImageView) viewHolder.getConvertView().findViewById(R.id.productOne));
                    viewHolder.setText(R.id.productOnePrice, String.format("￥%s", item.getProductList().get(0).getPrice()));

                    if (item.getProductList().size() >= 2) {
                        viewHolder.getView(R.id.productTwo).setVisibility(View.VISIBLE);
                        viewHolder.getView(R.id.productTwoPrice).setVisibility(View.VISIBLE);
                        Glide.with(ShowOnlineFragment.this).
                                load(item.getProductList().get(1).getLogo()).asBitmap().
                                into((ImageView) viewHolder.getConvertView().findViewById(R.id.productTwo));
                        viewHolder.setText(R.id.productTwoPrice, String.format("￥%s", item.getProductList().get(1).getPrice()));
                    } else {
                        viewHolder.getView(R.id.productTwo).setVisibility(View.GONE);
                        viewHolder.getView(R.id.productTwoPrice).setVisibility(View.GONE);
                    }
                } else {
                    viewHolder.getView(R.id.productOne).setVisibility(View.GONE);
                    viewHolder.getView(R.id.productOnePrice).setVisibility(View.GONE);
                    viewHolder.getView(R.id.productTwo).setVisibility(View.GONE);
                    viewHolder.getView(R.id.productTwoPrice).setVisibility(View.GONE);
                }

                if (Integer.valueOf(item.getNotice()) > 0) {//已提醒
                    rlRemind.setBackground(getResources().getDrawable(R.drawable.reminded_bg));
                    txtRemind.setTextColor(Color.parseColor("#E72E2D"));
                    txtRemind.setText("取消提醒");
                    isReminded = true;
                } else {
                    rlRemind.setBackground(getResources().getDrawable(R.drawable.remind_bg));
                    txtRemind.setTextColor(Color.parseColor("#FFFFFF"));
                    txtRemind.setText("提醒我");
                    isReminded = false;
                }
                if (mPosition == position) {//直播提醒
                    if (isReminded) {
                        rlRemind.setBackground(getResources().getDrawable(R.drawable.remind_bg));
                        txtRemind.setTextColor(Color.parseColor("#FFFFFF"));
                        txtRemind.setText("提醒我");
                        isReminded = false;
                    } else {
                        rlRemind.setBackground(getResources().getDrawable(R.drawable.reminded_bg));
                        txtRemind.setTextColor(Color.parseColor("#E72E2D"));
                        txtRemind.setText("取消提醒");
                        isReminded = true;
                    }
                }

                viewHolder.getView(R.id.rlRemind).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (StateMessage.IS_LOGIN) {
                            mPosition = position;
                            notifyDataSetChanged();
                            if (canClick) {
                                if (Integer.valueOf(item.getNotice()) > 0) {//已提醒
                                    notice(String.valueOf(item.getId()), item.getNotice());//取消提醒
                                } else {
                                    notice(String.valueOf(item.getId()), "");//提醒
                                }
                                canClick = false;
                            }

                        } else {
                            startActivity(new Intent(ShowOnlineFragment.this, LoginActivity.class));
                        }
                    }
                });
                viewHolder.getView(R.id.layoutMain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getStatus() == 2 || item.getStatus() == 1) {//直播或回放可以点
                            Intent intent = new Intent(ShowOnlineFragment.this, ShowingRoom.class);
                            intent.putExtra("status",item.getStatus());
                            intent.putExtra("id", String.valueOf(item.getId()));
                            startActivity(intent);
                        } else {//预告
                            ToastUtils.showToast(ShowOnlineFragment.this, "直播将在" + item.getStartTime() + "开始");
                        }
                    }
                });
            }
        };
        listview.setAdapter(adapter);
    }

    /**
     * 初始化XBanner
     */
    private void initBanner(XBanner banner) {
        //设置广告图片点击事件
        banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {
                if (liveLunbo.get(position).getUrl() != null) {//图片
                    String url = liveLunbo.get(position).getUrl();
                    if (url == null || url.length() < 10) return;
                    Intent intent = new Intent(ShowOnlineFragment.this, HomeWebViewAct.class);
                    intent.putExtra("url", liveLunbo.get(position).getUrl());
                    intent.putExtra("all", true);
                    intent.putExtra("isTitle", true);
                    startActivity(intent);
                } else {//直播
                    if (liveLunbo.get(position).getStatus() == 1 || liveLunbo.get(position).getStatus() == 2) {//直播或回放
                        Intent intent = new Intent(ShowOnlineFragment.this, ShowingRoom.class);
                        intent.putExtra("id", String.valueOf(liveLunbo.get(position).getId()));
                        startActivity(intent);
                    } else {//预告
                        ToastUtils.showToast(ShowOnlineFragment.this, "直播将在" + liveLunbo.get(position).getStartTime() + "开始");
                    }
                }
            }
        });
        //加载广告图片
        banner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                ImageView view1 = view.getRootView().findViewById(R.id.roundImage);
                RelativeLayout rlView = view.getRootView().findViewById(R.id.rlMyView);

                RelativeLayout rlNum = view.getRootView().findViewById(R.id.rlNum);
                TextView seeNum = view.getRootView().findViewById(R.id.seeNum);
                TextView txtLive = view.getRootView().findViewById(R.id.txtLive);
                LinearLayout layoutPerson = view.getRootView().findViewById(R.id.layoutPerson);
                RoundImageView headImage = view.getRootView().findViewById(R.id.headImage);
                TextView name = view.getRootView().findViewById(R.id.name);
                TextView place = view.getRootView().findViewById(R.id.place);
                ImageView imageFoucs = view.getRootView().findViewById(R.id.imageFoucs);

                ViewGroup.LayoutParams params = rlView.getLayoutParams();
                if (params != null) {
                    params.height = (int) (DisplayUtils.getScreenWidth(ShowOnlineFragment.this) * 0.56);
                    params.width = (int) (DisplayUtils.getScreenWidth(ShowOnlineFragment.this) * 0.56);
                    rlView.setLayoutParams(params);
                }
                if (liveLunbo.get(position).getUrl() != null) {//图片
                    Glide.with(ShowOnlineFragment.this).load(liveLunbo.get(position).getPic()).asBitmap().into(view1);
                    rlNum.setVisibility(View.GONE);
                    txtLive.setVisibility(View.GONE);
                    layoutPerson.setVisibility(View.GONE);
                    imageFoucs.setVisibility(View.GONE);
                } else {//直播
                    Glide.with(ShowOnlineFragment.this).load(liveLunbo.get(position).getPic()).asBitmap().into(view1);
                    rlNum.setVisibility(View.VISIBLE);
                    txtLive.setVisibility(View.VISIBLE);
                    layoutPerson.setVisibility(View.VISIBLE);
                    Glide.with(ShowOnlineFragment.this).load(liveLunbo.get(position).getAnchor().getHeader()).asBitmap().into(headImage);
                    name.setText(String.format("%s", liveLunbo.get(position).getAnchor().getName()));
                    place.setText(String.format("%s", liveLunbo.get(position).getAnchor().getCity()));
                    imageFoucs.setVisibility(View.GONE);

                    if (liveLunbo.get(position).getStatus() == 2) {//回放
                        txtLive.setText("回放");
                        seeNum.setText(String.format("%s 观看", liveLunbo.get(position).getPv()));
                    } else if (liveLunbo.get(position).getStatus() == 1) {//直播
                        seeNum.setText(String.format("%s 观看", liveLunbo.get(position).getPv()));
                        txtLive.setText("直播中");
                    } else if (liveLunbo.get(position).getStatus() == 0) {//预告
                        txtLive.setText("预告");
                        seeNum.setText(String.format("%s", liveLunbo.get(position).getStartTime()));
                    } else {
                        txtLive.setText("直播中");
                        seeNum.setText(String.format("%s 观看", liveLunbo.get(position).getPv()));
                    }
                }
            }
        });
    }

    /**
     * 轮播图
     */
    private void getLunbo() {
        Map<String, Object> params = new HashMap<>();
        params.put("showplat", "2");
        HttpClient.post(HttpClient.ONLINE_LUNBO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (refreshRoot != null) {
                    refreshRoot.finishRefresh();
                }
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    liveLunBean = JSONArray.parseObject(result.getData(), LiveLunBean.class);
                    liveLunbo.clear();
                    liveLunbo.addAll(liveLunBean.getLive());
                    liveLunbo.addAll(liveLunBean.getPicture());
                    if (liveLunbo.size() == 0) {
                        banner.setVisibility(View.GONE);
                    } else {
                        banner.setVisibility(View.VISIBLE);
                    }
                    initBanner(banner);
                    banner.setAutoPlayAble(liveLunbo.size() > 1);
                    banner.setIsClipChildrenMode(true);
                    banner.setData(R.layout.banner_image_layout, liveLunbo, null);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e("", e.getMessage());
            }
        }, ShowOnlineFragment.this);
    }

    /**
     * 直播列表
     */
    private void getLiveList() {
        Map<String, Object> params = new HashMap<>();
        params.put("showplat", "2");
        HttpClient.post(HttpClient.ONLINE_LIVE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (refreshRoot != null) {
                    refreshRoot.finishRefresh();
                }
                stopProgressDialog();
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    List<LiveBean> mShop = JSONArray.parseArray(result.getData(), LiveBean.class);
                    data.clear();
                    data.addAll(mShop);
                    setShowAdapter();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e("", e.getMessage());
            }
        }, ShowOnlineFragment.this);
    }

    /**
     * 提醒
     */
    private boolean canClick = true;

    private void notice(String id, String noticeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("noticeId", noticeId);
        HttpClient.post(HttpClient.NOTICE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                canClick = true;
                if (result.surcess()) {
                    ToastUtils.showToast(ShowOnlineFragment.this, result.getMessage());
                    getLiveList();
                } else {
                    ToastUtils.showToast(ShowOnlineFragment.this, result.getMessage());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                canClick = true;
                Log.e("", e.getMessage());
            }
        }, ShowOnlineFragment.this);
    }

    @OnClick(R.id.title_text)
    public void onViewClicked() {
    }
}
