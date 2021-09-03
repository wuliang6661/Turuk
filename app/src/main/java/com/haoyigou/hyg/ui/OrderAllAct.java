package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.Constants;
import com.haoyigou.hyg.entity.OrderEntry;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.NetworkUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.utils.Util;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.PayPupWindow;
import com.haoyigou.hyg.view.widget.PullDownElasticImp;
import com.haoyigou.hyg.view.widget.PullDownScrollView;
import com.haoyigou.hyg.view.widget.TopAndButtomListView;
import com.haoyigou.hyg.wxapi.WxPayTask;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/9/1.
 * <p>
 * 订单页面
 */

public class OrderAllAct extends BaseActivity implements View.OnClickListener,
        PullDownScrollView.RefreshListener, AdapterView.OnItemClickListener {

    @BindView(R.id.all_order)
    TextView allOrder;
    @BindView(R.id.payment_order)
    TextView paymentOrder;
    @BindView(R.id.drop_order)
    TextView dropOrder;
    @BindView(R.id.shipped_order)
    TextView shippedOrder;
    @BindView(R.id.ani_line)
    View aniLine;
    @BindView(R.id.listview)
    TopAndButtomListView listview;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshRoot;
    @BindView(R.id.fenlei_layout)
    RelativeLayout fenleiLayout;
    @BindView(R.id.not_layout)
    LinearLayout notLayout;

    List<OrderEntry> orderList;

    private int type = 0;   //0为全部订单 1 代付款  2 代发货 3 已发货 4 退款/售后
    private int page = 1;  // 默认第一页
    boolean isPageSun = true;   //用来表示下一页没有加载 ,可以加载下一页


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_all_order);
        ButterKnife.bind(this);
        goBack();
        setTitleText("我的订单");

        orderList = new ArrayList<>();
        distance = (GlobalApplication.screen_width / 4 - Util.dp2px(OrderAllAct.this, 63)) / 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                Util.dp2px(OrderAllAct.this, 63), Util.dp2px(OrderAllAct.this, 2));
        params.setMargins((int) distance, 0, 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        aniLine.setLayoutParams(params);
        setListener();
        type = getIntent().getIntExtra("type", 0);
        if (type == 4) {
            fenleiLayout.setVisibility(View.GONE);
        } else {
            setAnniLine(type);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderList = new ArrayList<>();
        listview.setVisibility(View.GONE);
        adapter = null;
        page = 1;
        getOrderData(type == 0 ? 4 : type - 1);
        startProgressDialog("", this);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        allOrder.setOnClickListener(this);
        paymentOrder.setOnClickListener(this);
        dropOrder.setOnClickListener(this);
        shippedOrder.setOnClickListener(this);
        listview.setOnItemClickListener(this);
        refreshRoot.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                page = 1;
                orderList.clear();
                getOrderData(type == 0 ? 4 : type - 1);
            }
        });
        refreshRoot.setRefreshHeader(new SmartHeader(this));
        refreshRoot.setHeaderHeight(60);
        refreshRoot.setEnableLoadMore(true);
        refreshRoot.setEnableRefresh(true);
        refreshRoot.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isPageSun) {
                    isPageSun = false;
                    page++;
                    getOrderData(type == 0 ? 4 : type - 1);
                    startProgressDialog("", OrderAllAct.this);
                }
            }
        });
        refreshRoot.setRefreshFooter(new SmartFooter(this));
        refreshRoot.setFooterHeight(60);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
//                        if (isPageSun) {
//                            isPageSun = false;
//                            page++;
//                            getOrderData(type == 0 ? 4 : type - 1);
//                            startProgressDialog("", OrderAllAct.this);
//                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        orderList = new ArrayList<>();
        page = 1;
        startProgressDialog("", this);
        switch (v.getId()) {
            case R.id.all_order:
                setAnniLine(0);
                type = 0;
                getOrderData(4);
                break;
            case R.id.payment_order:
                setAnniLine(1);
                type = 1;
                getOrderData(0);
                break;
            case R.id.drop_order:
                setAnniLine(2);
                type = 2;
                getOrderData(1);
                break;
            case R.id.shipped_order:
                setAnniLine(3);
                type = 3;
                getOrderData(2);
                break;
        }
    }


    /**
     * 获取订单数据
     */
    private void getOrderData(int type) {
        if (!NetworkUtils.isNetworkConnected(this)) {
            showToast("网络无连接");
            notLayout.setVisibility(View.VISIBLE);
            refreshRoot.setVisibility(View.GONE);
            stopProgressDialog();
            return;
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("ordertype", type);
        params.put("currPageNo", page);
        HttpClient.post(HttpClient.ORDER_DATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                refreshRoot.finishLoadMore();
                refreshRoot.finishRefresh();
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    listview.setVisibility(View.VISIBLE);
                    isPageSun = true;
                    String array = object.getString("result");
                    List<OrderEntry> list = JSONArray.parseArray(array, OrderEntry.class);
                    if (page == 1 && list.size() == 0) {
                        notLayout.setVisibility(View.VISIBLE);
                        refreshRoot.setVisibility(View.GONE);
                    } else {
                        notLayout.setVisibility(View.GONE);
                        refreshRoot.setVisibility(View.VISIBLE);
                    }
                    if (list == null || list.size() == 0) {
                        page = page == 1 ? 1 : --page;
                    }
                    Message msg = new Message();
                    msg.obj = list;
                    msg.what = 0x11;
                    handler.sendMessage(msg);
                } else {
                    showToast(object.getString("message"));
                }
            }
        }, this);
    }

    CommonAdapter<OrderEntry> adapter;

    /**
     * 设置界面适配
     */
    private void setListAdapter() {
        adapter = new CommonAdapter<OrderEntry>(this, R.layout.item_order_all, orderList) {
            @Override
            protected void convert(ViewHolder viewHolder, OrderEntry item, int position) {
                viewHolder.setText(R.id.order_num, "订单编号：" + item.getOrdernum());
                viewHolder.setImageUrl(R.id.order_img, item.getDetails().get(0).getPiclogo());
                viewHolder.setText(R.id.order_name, item.getDetails().get(0).getName());
                viewHolder.setText(R.id.order_moket_num, "×" + item.getDetails().get(0).getNum());
                viewHolder.setText(R.id.order_num_txt, "共" + item.getProductnum() + "件商品 合计：");
                viewHolder.setText(R.id.order_price, "¥" + item.getDetails().get(0).getDisprice());
                viewHolder.setText(R.id.order_allprice, item.getDisprice());
                viewHolder.setText(R.id.order_carriage, "（运费：¥" + item.getCarriage() + "）");
                String name = item.getDetails().get(0).getAttrname();
                if (item.getDetails().get(0).getAttrname().equals("共同")){
                    name= "";
                }
                viewHolder.setText(R.id.order_style,name);
                TextView orderOldprice = viewHolder.getView(R.id.order_oldprice);
                orderOldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                orderOldprice.setText("¥" + item.getDetails().get(0).getPrice());
                viewHolder.getView(R.id.order_button1).setVisibility(View.VISIBLE);
                TextView button1 = viewHolder.getView(R.id.order_button1);
                TextView button2 = viewHolder.getView(R.id.order_button2);
                TextView button3 = viewHolder.getView(R.id.order_button3);
                if (item.getAdvanceid() != null){//advanceid为空是普通订单，不为空是预售订单
                    button1.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                }else {
                    button1.setVisibility(View.VISIBLE);
                    if (item.getEnjoyDiscount() != null && item.getEnjoyDiscount().equals("0")){
                        if ("0".equals(item.getStatus())){//待付款不能领红包
                            button3.setVisibility(View.GONE);
                        }else {
                            button3.setVisibility(View.VISIBLE);
                        }
                    }else {
                        button3.setVisibility(View.GONE);
                    }
                }
                if ("3".equals(item.getStatus())) {
                    viewHolder.setText(R.id.order_stutus, "已完成");
                    button1.setText("查看物流");
                    button2.setText("再次购买");
                } else if ("-1".equals(item.getStatus())) {
                    viewHolder.setText(R.id.order_stutus, "已取消");
                    button1.setText("删除订单");
                    button2.setText("再次购买");
                } else if ("1".equals(item.getStatus())) {
                    viewHolder.setText(R.id.order_stutus, "待发货");
                    button1.setText("提醒发货");
                    button2.setText("再次购买");
                } else if ("0".equals(item.getStatus())) {
                    viewHolder.setText(R.id.order_stutus, "待付款");
                    if (item.getAdvanceid() != null){//advanceid为空是普通订单，不为空是预售订单
                        button1.setVisibility(View.GONE);
                        if (item.getAdvanceStatus().equals("0")){//值为0的时候是定金未付，值为1是定金已付尾款未付
                            button2.setText("支付定金");
                        }
                        if (item.getAdvanceStatus().equals("1")){
                            button2.setText("支付尾款");
                        }
                    }else {
                        button1.setText("取消订单");
                        button2.setText("去支付");
                    }
                } else if ("2".equals(item.getStatus())) {
                    viewHolder.setText(R.id.order_stutus, "已发货");
                    button1.setText("查看物流");
                    button2.setText("确认收货");
                } else if ("4".equals(item.getStatus())) {
                    viewHolder.setText(R.id.order_stutus, "已退款");
                    button1.setText("删除订单");
                    button2.setText("确认收货");
                } else if ("-88".equals(item.getStatus())){
                    viewHolder.setText(R.id.order_stutus, "待取消");
                    button1.setVisibility(View.GONE);
                    button2.setText("再次购买");
                }else {//6,7待退款
                    viewHolder.setText(R.id.order_stutus, "待退款");
                    button1.setVisibility(View.GONE);
                    button2.setText("再次购买");
                }
                Hodler hodler = new Hodler();
                hodler.entry = item;
                hodler.position = position;
                button1.setTag(hodler);
                button2.setTag(hodler);
                button3.setTag(hodler);
                button1.setOnClickListener(listener);
                button2.setOnClickListener(listener);
                button3.setOnClickListener(listener);
            }
        };
        listview.setAdapter(adapter);
    }


    class Hodler {
        OrderEntry entry;
        int position;
    }

    PayPupWindow pupWindow;

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Hodler hodler = (Hodler) v.getTag();
            switch (v.getId()) {
                case R.id.order_button1:
                    switch (hodler.entry.getStatus()) {
                        case "2":     //查看物流
                        case "3":
                            Intent intent = new Intent(OrderAllAct.this, PersonWebViewAct.class);
                            intent.putExtra("url", HttpClient.ORDER_RED_LOG + "?jumptype=1&ordernum=" +
                                    hodler.entry.getDetails().get(0).getId() +
                                    "&distributorId=" + disId);
                            intent.putExtra("isTitle", true);
                            startActivity(intent);
                            break;
                        case "-1":    //删除订单
                        case "4":
                            removeOrder(hodler.entry.getOrdernum(), hodler.position);
                            break;
                        case "1":    //提醒发货
                            remindOrder(hodler.entry.getOrdernum());
                            break;
                        case "0":     //取消订单
                            cancleOrder(hodler.entry.getOrdernum(), hodler.position);
                            break;
                    }
                    break;
                case R.id.order_button2:
                    switch (hodler.entry.getStatus()) {
                        case "3":     //再次购买
                        case "-1":
                        case "1":
                            againPayShop(hodler.entry.getOrdernum());
                            break;
                        case "2":     //确认收货
                        case "4":
                            confirmReceipt(hodler.entry.getOrdernum(), hodler.position);
                            break;
                        case "0":   //去支付
                            if (hodler.entry.getAdvanceid() != null) {//advanceid为空是普通订单，不为空是预售订单
                                if (hodler.entry.getAdvanceStatus().equals("0")) {//值为0的时候是定金未付，值为1是定金已付尾款未付
                                    Intent intent = new Intent(OrderAllAct.this, PersonWebViewAct.class);
                                    intent.putExtra("url", HttpClient.PAY_ORDER_MONEY + "?ordernum=" + hodler.entry.getOrdernum()
                                            + "&distributorId=" + disId + "&source=1");
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(OrderAllAct.this, PersonWebViewAct.class);
                                    intent.putExtra("url", HttpClient.PAY_PRESELL + "?ordernum=" + hodler.entry.getOrdernum()
                                            + "&distributorId=" + disId + "&source=1");
                                    startActivity(intent);
                                }
                            }else {
                                pupWindow = new PayPupWindow(OrderAllAct.this);
                                pupWindow.setOnPayButton(new PayPupWindow.onPayButton() {
                                    @Override
                                    public void onClick(int type) {
                                        pupWindow.dismiss();
                                        if (type == 1) {   //微信支付
                                            payWX(hodler.entry.getOrdernum());
                                        } else {    //支付宝支付
                                            payAliba(hodler.entry.getOrdernum());
                                        }
                                    }
                                });
                                pupWindow.showAtLocation(listview, Gravity.BOTTOM, 0, 0);
                            }
                            break;
                        default:    //再次购买
                            againPayShop(hodler.entry.getOrdernum());
                            break;
                    }
                    break;
                case R.id.order_button3:
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNum", hodler.entry.getOrdernum());
                    Intent intent = new Intent(OrderAllAct.this, WebActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 删除订单
     */
    private void removeOrder(String orderNum, final int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordernum", orderNum);
        params.put("userdelete", "1");
        HttpClient.post(HttpClient.ORDER_DELETE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    showToast("订单已删除！");
                    orderList.remove(position);
                    adapter.notifyDataSetChanged();
                    if (orderList.size() == 0) {
                        notLayout.setVisibility(View.VISIBLE);
                        refreshRoot.setVisibility(View.GONE);
                    }
                } else {
                    showToast(object.getString("message"));
                }
            }
        }, this);
    }

    /**
     * 取消订单
     */
    private void cancleOrder(String orderNum, final int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordernum", orderNum);
        HttpClient.post(HttpClient.ORDER_CANCLE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    showToast("订单已取消！");
                    orderList.remove(position);
                    adapter.notifyDataSetChanged();
                    if (orderList.size() == 0) {
                        notLayout.setVisibility(View.VISIBLE);
                        refreshRoot.setVisibility(View.GONE);
                    }
                } else {
                    showToast(object.getString("message"));
                }
            }
        }, this);
    }

    /**
     * 提醒发货
     */
    private void remindOrder(String orderNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordernum", orderNum);
        HttpClient.post(HttpClient.ORDER_REMIND, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                showToast(object.getString("message"));
            }
        }, this);
    }

    /**
     * 确认收货
     */
    private void confirmReceipt(String orderNum, final int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordernum", orderNum);
        HttpClient.post(HttpClient.ORDER_CONFIRM, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    showToast(object.getString("message"));
                    orderList.remove(position);
                    adapter.notifyDataSetChanged();
                    if (orderList.size() == 0) {
                        notLayout.setVisibility(View.VISIBLE);
                        refreshRoot.setVisibility(View.GONE);
                    }
                } else {
                    showToast(object.getString("message"));
                }
            }
        }, this);

    }


    /**
     * 再次购买
     */
    private void againPayShop(String orderNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordernum", orderNum);
        HttpClient.post(HttpClient.ORDER_AGAGIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    EventBus.getDefault().post(new Boolean(false));
                    finish();
                } else {
                    showToast(object.getString("message"));
                }
            }
        }, this);
    }

    /**
     * 微信支付
     */
    private void payWX(String orderNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ordernum", orderNum);
        map.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", null));
        WxPayTask task = new WxPayTask(this, 1);
        task.startProgressDialog("", this);
        task.execute(map);
    }


    /**
     * 支付宝支付
     */
    private void payAliba(final String orderNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ordernum", orderNum);
        HttpClient.post(HttpClient.ORDER_PAYALI, map, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String data = object.getString("data");
                    new AlipayThread(data, orderNum).start();
                } else {
                    showToast(object.getString("message"));
                }
            }
        }, this);
    }


    class AlipayThread extends Thread {

        String itemdata;
        String orderNum;

        AlipayThread(String itemdata, String orderNum) {
            this.itemdata = itemdata;
            this.orderNum = orderNum;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            PayTask alipay = new PayTask(OrderAllAct.this);
            String result = alipay.pay(itemdata, true);
//            Log.i("log--", result);
            Message msg = new Message();
            msg.what = 0x22;
            msg.obj = orderNum;
            handler.sendMessage(msg);
            super.run();
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    List<OrderEntry> list = (List<OrderEntry>) msg.obj;
                    orderList.addAll(list);
                    if (adapter != null) {
                        adapter.setmDatas(orderList);
                    } else {
                        setListAdapter();
                    }
                    break;
                case 0x22:   //支付宝支付完成
                    String orderNum = (String) msg.obj;
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNum", orderNum);
                    goToActivity(OrderMessageAct.class, bundle, false);
                    break;
            }
        }
    };


    private float distance;  //记录上次的位置

    /**
     * 动画横移
     */
    private void setAnniLine(int seat) {
        TranslateAnimation animation = new TranslateAnimation(distance,
                GlobalApplication.screen_width / 4 * seat, 0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        aniLine.startAnimation(animation);//开始动画
        distance = GlobalApplication.screen_width / 4 * seat;
        setTextColor(seat);
    }


    /**
     * 设置点击效果
     */
    private void setTextColor(int seat) {
        TextView[] orders = new TextView[]{allOrder, paymentOrder, dropOrder, shippedOrder};
        for (int i = 0; i < orders.length; i++) {
            if (seat == i) {
                orders[i].setTextColor(getResources().getColor(R.color.mainBlue));
            } else {
                orders[i].setTextColor(getResources().getColor(R.color.D));
            }
        }
    }

    @Override
    public void onRefresh(PullDownScrollView view) {
        new Handler().postDelayed(new Runnable() {   //刷新一秒
            @Override
            public void run() {
//                page = 1;
//                orderList.clear();
//                getOrderData(type == 0 ? 4 : type - 1);
//                refreshRoot.finishRefresh("刷新");
            }
        }, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (orderList.get(position).getAdvanceid() != null) {//advanceid为空是普通订单，不为空是预售订单
            if (orderList.get(position).getAdvanceStatus().equals("1")) {//值为0的时候是定金未付，值为1是定金已付尾款未付
                Intent intent = new Intent(OrderAllAct.this, PersonWebViewAct.class);
                intent.putExtra("url", HttpClient.PAY_PRESELL + "?ordernum=" + orderList.get(position).getOrdernum()
                        + "&distributorId=" + disId + "&source=1");
                startActivity(intent);
            }else {
                Bundle bundle = new Bundle();
                bundle.putString("orderNum", orderList.get(position).getOrdernum());
                goToActivity(OrderMessageAct.class, bundle, false);
            }
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("orderNum", orderList.get(position).getOrdernum());
            goToActivity(OrderMessageAct.class, bundle, false);
        }
    }
}
