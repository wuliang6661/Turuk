package com.haoyigou.hyg.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.MessageDetailsBean;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.TopAndButtomListView;
import com.haoyigou.hyg.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by wuliang on 2016/12/30.
 * <p>
 * 消息盒子点击进去详情页面
 */

public class MessageBoxDetailsAct extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.back)
    LinearLayout backTo;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.message_list)
    TopAndButtomListView messageList;

    private int pageNo = 1;   //获取的页数
    private static final int pageNum = 20;   //获取的数据个数

    private String disId;
    private String Type;    //进入的页面类型  1：精选活动 2：订单服务 3：消息通知 4：我的资产

    List<MessageDetailsBean> detailList;

    CommonAdapter<MessageDetailsBean> adapter;   //适配器

    boolean isPageSun = true;   //用来表示下一页没有加载 ,可以加载下一页
    boolean isReadAll = false;   //表示是否是点击全部阅读

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_box_details_layout);
        ButterKnife.bind(this);

        disId = SharedPreferencesUtils.getInstance().getString("distributorId", "");
        Type = getIntent().getStringExtra("Type");
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setRightText("全部阅读", listener);
        detailList = new ArrayList<>();
        messageList.setOnItemClickListener(this);
        setTopMore();
        switch (Type) {
            case "1":
                titleText.setText("精选活动");
                break;
            case "2":
                titleText.setText("订单服务");
                break;
            case "3":
                titleText.setText("消息通知");
                break;
            case "4":
                titleText.setText("我的资产");
                break;
        }
        getData();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Map<String, Object> params = new HashMap<>();
            params.put("pushtype", Type);
            params.put("distributorId", disId);
            HttpClient.post(HttpClient.ALLREAD, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    JSONObject object = JSON.parseObject(content);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                        detailList.clear();
                        pageNo = 1;
                        isReadAll = true;
                        getData();
                    } else {
                        showToast(object.getString("message"));
                    }
                }
            }, MessageBoxDetailsAct.this);
        }
    };


    /**
     * 上拉加载更多
     */
    private void setTopMore() {
        messageList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        if (isPageSun) {
                            isPageSun = false;
                            pageNo++;
                            getData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取数据
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("distributorId", disId);
        params.put("pageSize", pageNum);
        params.put("pageNo", pageNo);
        params.put("type", Type);
        params.put("mesversion", "1");
        HttpClient.get(HttpClient.MESSAGEDETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--mesDetails", content);
                if (content == null || content.equals("")) return;
                JSONObject object = JSON.parseObject(content);
                if (!HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    return;
                }
                String result = object.getString("result");
                int readnum = object.getInteger("readnum");
                if (StateMessage.badgeNum > readnum && readnum > 0) {
                    StateMessage.badgeNum -= readnum;
                    ShortcutBadger.applyCount(MessageBoxDetailsAct.this, StateMessage.badgeNum);
                }
                List shop = JSONArray.parseArray(result, MessageDetailsBean.class);
                if (shop == null || shop.size() == 0) {
                    pageNo--;
                }
                if (!isPageSun) {
                    detailList.addAll(shop);
//                    adapter.notifyDataSetChanged();
                } else {
                    detailList = shop;
//                    adapter.notifyDataSetChanged();
                }
                isPageSun = true;
                switch (Type) {
                    case "1":
                        setHDAdapter();
                        break;
                    case "2":
                        setOrderAdapter();
                        break;
                    case "3":
                        setTongZhiAdapter();
                        break;
                    case "4":
                        setZiChanAdapter();
                        break;
                }
            }
        }, this, false);
    }


    /**
     * 设置精选活动适配器
     */
    private void setHDAdapter() {
        if (adapter != null && !isReadAll) {
            adapter.setmDatas(detailList);
//            adapter.notifyDataSetChanged();
            return;
        }
        isReadAll = false;
        adapter = new CommonAdapter<MessageDetailsBean>(this, R.layout.item_huodong_layout, detailList) {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected void convert(final ViewHolder helper, final MessageDetailsBean item, int position) {
                helper.setText(R.id.huodong_time, item.getPushDateStr());
                helper.setText(R.id.huodong_title, item.getTitle());
                helper.setText(R.id.huodong_message, item.getMemo());
                helper.setImageUrl(R.id.huodong_img, item.getPic());
                ImageView image = helper.getView(R.id.huodong_img);
                int width = GlobalApplication.screen_width - 156;
                ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
                layoutParams.height = (int) (width / 1.88);
                layoutParams.width = width;
                image.setLayoutParams(layoutParams);
                if (item.getHavereadtrue().equals("0")) {   //未阅读
                    helper.getView(R.id.new_message).setVisibility(View.VISIBLE);
                } else {    //已阅读
                    helper.getView(R.id.new_message).setVisibility(View.GONE);
                }
                if (item.getPushtype().equals("4") && item.getAssetType().equals("3")) {  //无法跳转，不显示阅读
                    helper.getView(R.id.read).setVisibility(View.GONE);
                }
                /*TextView view = helper.getView(R.id.collect_text);
                if (item.getHavecollect().equals("0")) {   //未收藏
                    view.setVisibility(View.VISIBLE);
                    helper.getView(R.id.have_collect).setVisibility(View.GONE);
                    view.setTag(item);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            MessageDetailsBean bean = (MessageDetailsBean) view.getTag();
                            Map<String, Object> params = new HashMap<>();
                            params.put("pushMesId", bean.getId());
                            HttpClient.post(HttpClient.COLLECT, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(String content) {
                                    super.onSuccess(content);
                                    Log.e("log--", content);
                                    if (StringUtils.isEmpty(content)) return;
                                    JSONObject object = JSON.parseObject(content);
                                    showToast(object.getString("message"));
                                    if (object.getString("status").equals(HttpClient.RET_SUCCESS_CODE)) {   //收藏成功
                                        item.setHavecollect("1");
                                        view.setVisibility(View.GONE);
                                        helper.getView(R.id.have_collect).setVisibility(View.VISIBLE);
                                    }
                                }
                            }, MessageBoxDetailsAct.this);
                        }
                    });
                } else {   //已收藏
                    view.setVisibility(View.GONE);
                    helper.getView(R.id.have_collect).setVisibility(View.VISIBLE);
                }*/
            }
        };
        messageList.setAdapter(adapter);
    }


    /**
     * 设置订单服务适配器
     */
    private void setOrderAdapter() {
        if (adapter != null && !isReadAll) {
            adapter.setmDatas(detailList);
            return;
        }
        isReadAll = false;
        adapter = new CommonAdapter<MessageDetailsBean>(this, R.layout.item_dingdan_layout, detailList) {
            @Override
            protected void convert(ViewHolder helper, MessageDetailsBean item, int position) {
                helper.setText(R.id.dingdan_time, item.getPushDateStr());
                helper.setText(R.id.dingdan_title, item.getTitle());
                helper.setText(R.id.dingdan_message, item.getMemo());
                if (StringUtils.isEmpty(item.getPic())) {
                    helper.setImageResource(R.id.dingdan_img, R.drawable.default_image);
                } else {
                    helper.setImageUrl(R.id.dingdan_img, item.getPic());
                }
                if (item.getHavereadtrue().equals("0")) {   //未阅读
                    helper.getView(R.id.new_message).setVisibility(View.VISIBLE);
                } else {    //已阅读
                    helper.getView(R.id.new_message).setVisibility(View.GONE);
                }
                if (item.getPushtype().equals("4") && item.getAssetType().equals("3")) {  //无法跳转，不显示阅读
                    helper.getView(R.id.read_layout).setVisibility(View.GONE);
                }
            }
        };
        messageList.setAdapter(adapter);
    }

    /**
     * 设置消息通知适配器
     */
    private void setTongZhiAdapter() {
        if (adapter != null && !isReadAll) {
            adapter.setmDatas(detailList);
            return;
        }
        isReadAll = false;
        adapter = new CommonAdapter<MessageDetailsBean>(this, R.layout.item_tongzhi_layout, detailList) {
            @Override
            protected void convert(ViewHolder helper, MessageDetailsBean item, int position) {
                helper.setText(R.id.tongzhi_time, item.getPushDateStr());
                helper.setText(R.id.tongzhi_title, item.getTitle());
                helper.setText(R.id.tongzhi_message, item.getMemo());
                if (item.getHavereadtrue().equals("0")) {   //未阅读
                    helper.getView(R.id.new_message).setVisibility(View.VISIBLE);
                } else {    //已阅读
                    helper.getView(R.id.new_message).setVisibility(View.GONE);
                }
                if (item.getPushtype().equals("4") && item.getAssetType().equals("3")) {  //无法跳转，不显示阅读
                    helper.getView(R.id.read_layout).setVisibility(View.GONE);
                }
            }
        };
        messageList.setAdapter(adapter);
    }

    /**
     * 设置我的资产适配器
     */
    private void setZiChanAdapter() {
        if (adapter != null && !isReadAll) {
            adapter.setmDatas(detailList);
            return;
        }
        isReadAll = false;
        adapter = new CommonAdapter<MessageDetailsBean>(this, R.layout.item_dingdan_layout, detailList) {
            @Override
            protected void convert(ViewHolder helper, MessageDetailsBean item, int position) {
                helper.setText(R.id.dingdan_time, item.getPushDateStr());
                helper.setText(R.id.dingdan_title, item.getTitle());
                helper.setText(R.id.dingdan_message, item.getMemo());
                if (item.getHavereadtrue().equals("0")) {   //未阅读
                    helper.getView(R.id.new_message).setVisibility(View.VISIBLE);
                } else {    //已阅读
                    helper.getView(R.id.new_message).setVisibility(View.GONE);
                }
                if (item.getPushtype().equals("4") && item.getAssetType().equals("3")) {  //无法跳转，不显示阅读
                    helper.getView(R.id.read_layout).setVisibility(View.GONE);
                }
                switch (item.getAssetPicType()) {
                    case "1":
                    case "2":
                        helper.setImageResource(R.id.dingdan_img, R.drawable.youhui);
                        break;
                    case "3":
                    case "4":
                        helper.setImageResource(R.id.dingdan_img, R.drawable.shouyi);
                        break;
                }
            }
        };
        messageList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView newMessage = (TextView) view.findViewById(R.id.new_message);
        newMessage.setVisibility(View.GONE);
        MessageDetailsBean bean = detailList.get(i);
        bean.setHaveread("1");
        bean.setHavereadtrue("1");
        Intent intent = new Intent(MessageBoxDetailsAct.this, HomeWebViewAct.class);
        String url = "";
        switch (bean.getPushtype()) {
            case "1":
                if ("1".equals(bean.getActivityType())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("productTabId", bean.getTabid());
                    bundle.putString("pushMesId", bean.getId());
                    goToActivity(LabelActivity.class, bundle, false);
                    return;
                } else if ("2".equals(bean.getActivityType())) {
                    url = bean.getTaburl();
                    intent.putExtra("all", true);
                } else {
                    url = HttpClient.GOODWEBVIEW + "?productid=" + bean.getProductid() +
                            "&distributorId=" + disId + "&frommes=1&accessType=1&source=1&pushMesId=" + bean.getId();
                }
                break;
            case "2":
                switch (bean.getOrderType()) {
                    case "1":
                        url = HttpClient.ORDERDETAILS + "?ordernum=" + bean.getOrderNum() +
                                "&distributorId=" + disId + "&frommes=1&source=1&pushMesId=" + bean.getId();
                        break;
                    case "2":
                        url = HttpClient.MYGAINS + "?distributorId=" + disId + "&pushMesId=" + bean.getId();
                        break;
                }
                break;
            case "3":
                switch (bean.getInformType()) {
                    case "1":
                        url = HttpClient.RECOMMEND + "?distributorId=" + disId + "&pushMesId=" + bean.getId();
                        break;
                    case "2":
                        url = HttpClient.MYTEAM + "?distributorId=" + disId + "&pushMesId=" + bean.getId();
                        break;
                    case "3":
                        url = HttpClient.READPUSH + "?distributorId=" + disId + "&pushMesId=" + bean.getId();
                        break;
                    case "4":
                        url = HttpClient.SHOPMESSAGE + "?distributorId=" + disId + "&productid=" + bean.getProductid()
                                + "&pushMesId=" + bean.getId() + "&showplat=2";
                        break;
                    case "6":
                        url = HttpClient.SIGN + "?distributorId=" + disId + "&pushMesId=" + bean.getId();
                        break;
                    case "5":
                        Intent mIntent = new Intent(this, TVLiveActivity.class);
                        startActivity(mIntent);
                        return;
                }
                break;
            case "4":
                switch (bean.getAssetType()) {
                    case "1":
                        url = HttpClient.YOUHUIPOP + "?distributorId=" + disId + "&pushMesId=" + bean.getId();
                        break;
                    case "2":
                        url = HttpClient.MYGAINS + "?distributorId=" + disId + "&pushMesId=" + bean.getId();
                        break;
                    case "3":
                        return;
                }
                break;
        }
        if (bean.getHaveread().equals("0") && StateMessage.badgeNum > 0) {   //如果是新消息，角标数减1，放到最下面，避免不能点击的消息-1
            StateMessage.badgeNum--;
//            Log.e("badgenum", StateMessage.badgeNum + "");
            ShortcutBadger.applyCount(MessageBoxDetailsAct.this, StateMessage.badgeNum);
        }
        intent.putExtra("url", url);
        startActivity(intent);
    }
}