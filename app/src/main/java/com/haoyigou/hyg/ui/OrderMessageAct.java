package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.OrderMessageEntry;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.AppManager;
import com.haoyigou.hyg.utils.DateUtil;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.TimeCountUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;
import com.haoyigou.hyg.view.widget.PayPupWindow;
import com.haoyigou.hyg.wxapi.WxPayTask;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

import static meijia.com.srdlibrary.myutil.StatusBarUtils.setStatusBarColor;

/**
 * Created by wuliang on 2017/9/4.
 * <p>
 * 订单详情页面
 */

public class OrderMessageAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.shop_person)
    TextView shopPerson;
    @BindView(R.id.shop_address)
    TextView shopAddress;
    @BindView(R.id.shop_list)
    ListViewForScrollView shopList;
    @BindView(R.id.order_num)
    TextView orderNum;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.order_pay_num)
    TextView orderPayNum;
    @BindView(R.id.order_num_layout)
    LinearLayout orderNumLayout;
    @BindView(R.id.order_copy)
    TextView orderCopy;
    @BindView(R.id.order_pay_type)
    TextView orderPayType;
    @BindView(R.id.shop_all_price)
    TextView shopAllPrice;
    @BindView(R.id.shop_yunfei)
    TextView shopYunfei;
    @BindView(R.id.shop_pay_price)
    TextView shopPayPrice;
    @BindView(R.id.pay1_price)
    RelativeLayout pay1Price;
    @BindView(R.id.youhui1)
    TextView youhui1;
    @BindView(R.id.integral_price)
    TextView integralPrice;
    @BindView(R.id.youhui2)
    TextView youhui2;
    @BindView(R.id.balance_price)
    TextView balancePrice;
    @BindView(R.id.order_pay_price)
    TextView orderPayPrice;
    @BindView(R.id.youhui_layout)
    LinearLayout youhuiLayout;
    @BindView(R.id.pay_button)
    TextView payButton;
    @BindView(R.id.see_flow)
    Button seeFlow;
    @BindView(R.id.confirm_receipt)
    Button confirmReceipt;
    @BindView(R.id.button_layout)
    LinearLayout buttonLayout;
    @BindView(R.id.pay_button_layout)
    LinearLayout payButtonLayout;
    @BindView(R.id.scollview)
    ScrollView scollview;
    @BindView(R.id.youhui_list)
    ListViewForScrollView youhuiList;
    @BindView(R.id.rlTitleBg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivClock)
    ImageView ivClock;
    @BindView(R.id.txtClock)
    TextView txtClock;
    @BindView(R.id.txtLastTime)
    TextView txtLastTime;//倒计时时间
    @BindView(R.id.txtT)
    TextView txtT;

    private String orderNumStr;
    private OrderMessageEntry order;

    private boolean confirmRe = true;   //默认为确认收货

    private static OrderMessageAct activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//去掉状态栏灰色遮罩层并且设置字体颜色为暗色
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } catch (Exception e) {}
        }

        setContentView(R.layout.act_order_message);
        ButterKnife.bind(this);
        goBack();
        setTitleText("订单详情");
        setmStatusBar();
//        setFlgsbg(android.R.color.white);//通知栏所需颜色
        rlTitleBg.setBackgroundColor(getResources().getColor(R.color.white));
        title_text.setTextColor(getResources().getColor(R.color.black));
        ivBack.setBackground(getResources().getDrawable(R.mipmap.fanhui1));
        activity = this;
        orderNumStr = getIntent().getExtras().getString("orderNum", "");
        getOrderData();
        setListener();
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setmStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置事件逻辑
     */
    private void setListener() {
        orderCopy.setOnClickListener(this);
        payButton.setOnClickListener(this);
        confirmReceipt.setOnClickListener(this);
        seeFlow.setOnClickListener(this);
    }

    public static OrderMessageAct getInstance() {
        return activity;
    }


    /**
     * 获取订单详情信息
     */
    private void getOrderData() {
        Map<String, Object> params = new HashMap<>();
        params.put("ordernum", orderNumStr);
        HttpClient.post(HttpClient.ORDER_MESSAGE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String str = object.getString("result");
                    order = JSONObject.parseObject(str, OrderMessageEntry.class);
                    setUIData();
                    setShopAdapter();
                } else {
                    showToast(object.getString("message"));
                }
            }
        }, this);
    }

    /**
     * 设置界面数据
     */
    private void setUIData() {
        TimeCountUtils utils = TimeCountUtils.getInstance();
        utils.setOnTimeConuntListener(new TimeCountUtils.onTimeConunt() {
            @Override
            public void onProgress() {
                refreshChild();
            }
        });
        shopPerson.setText("收货人：" + order.getAddress().getName() + "  " + order.getAddress().getTelphone());
        shopAddress.setText("收货地址：" + order.getAddress().getAddress());
        orderNum.setText("订单编号：" + order.getOrdernum());
        orderTime.setText("下单时间：" + order.getTime());
        orderPayNum.setText("付款时间：" + order.getZftime());
        orderPayType.setText("支付方式：" + ("1".equals(order.getPayway()) ? "在线支付" : "货到付款"));
        shopAllPrice.setText("¥ " + order.getPrice());
        shopYunfei.setText("¥ " + order.getCarriage());
        shopPayPrice.setText("¥ " + (Double.parseDouble(order.getDisprice()) + Double.parseDouble(order.getCarriage())));
        integralPrice.setText("¥ " + (Double.parseDouble(order.getLastfee()) - Double.parseDouble(order.getDisprice())));
        if (StringUtils.isEmpty(order.getDiscontent())) {
            youhuiList.setVisibility(View.GONE);
        } else {
            String[] list = order.getDiscontent().split("@");
            String[] youhui = order.getDiscount().split("@");
            setYouhuiAdapter(list, youhui);
        }
        if (StringUtils.isEmpty(order.getCashaccount())) {
            balancePrice.setText("¥ 0.00");
        } else {
            balancePrice.setText("¥ " + order.getCashaccount());
        }
        orderPayPrice.setText(String.format("¥ %s",Double.parseDouble(order.getDisprice()) + Double.parseDouble(order.getCarriage())));
//        orderPayPrice.setText("¥ " + (Double.parseDouble(order.getDisprice()) + Double.parseDouble(order.getCarriage())));
        if ("0".equals(order.getStatus())) {  //未付款
            youhuiLayout.setVisibility(View.GONE);
            if (order.getAdvanceid() != null){//advanceid为空是普通订单，不为空是预售订单
                if (order.getAdvanceStatus().equals("0")){//值为0的时候是定金未付，值为1是定金已付尾款未付
                    payButton.setText("去付定金");
                }
                if (order.getAdvanceStatus().equals("1")){
                    payButton.setText("去付尾款");
                }
            }else {
                payButton.setText("去付款");
            }
            buttonLayout.setVisibility(View.GONE);
            orderPayNum.setVisibility(View.GONE);
            ivClock.setVisibility(View.VISIBLE);
            txtLastTime.setVisibility(View.VISIBLE);
            txtT.setVisibility(View.VISIBLE);
            txtClock.setText("待付款");
        } else if ("1".equals(order.getStatus())) { //待发货
            payButton.setText("提醒发货");
            pay1Price.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
            ivClock.setVisibility(View.INVISIBLE);
            txtClock.setText("待发货");
            txtLastTime.setVisibility(View.GONE);
            txtT.setVisibility(View.GONE);
        } else if ("6".equals(order.getStatus())) {   //待退款
            youhuiLayout.setVisibility(View.GONE);
            payButton.setText("再次购买");
            buttonLayout.setVisibility(View.GONE);
            orderPayNum.setVisibility(View.GONE);
            ivClock.setVisibility(View.INVISIBLE);
            txtClock.setText("待退款");
            txtLastTime.setVisibility(View.GONE);
            txtT.setVisibility(View.GONE);
        } else if ("4".equals(order.getStatus())){//已退款
            youhuiLayout.setVisibility(View.GONE);
            payButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
            orderPayNum.setVisibility(View.GONE);
            ivClock.setVisibility(View.INVISIBLE);
            txtClock.setText("已退款");
            txtLastTime.setVisibility(View.GONE);
            txtT.setVisibility(View.GONE);
        }else {
            payButtonLayout.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);
            confirmReceipt.setText("再次购买");
            payButton.setVisibility(View.GONE);
            pay1Price.setVisibility(View.VISIBLE);
            ivClock.setVisibility(View.INVISIBLE);
            txtClock.setText("");
            txtLastTime.setVisibility(View.GONE);
            txtT.setVisibility(View.GONE);
            confirmRe = false;
        }
    }

    /**
     * 定时器计算并给每一行更新数据
     */
    private void refreshChild() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (order != null) {
                    Long[] time = new Long[0];
                    if (order.getLastzftime() != null) {
                        time = setTime(DateUtil.getStringToDate(order.getLastzftime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    if (time != null) {
                        if (time[0] != 0){
                            txtLastTime.setText(String.format("%s时%s分%s秒",time[0],time[1],time[2]));
                        }else {
                            if (time[1] == 0 && time[2] == 1){
                                OrderMessageAct.this.finish();
                            }else {
                                txtLastTime.setText(String.format("%s分%s秒", time[1],time[2]));
                            }
                        }
//                        txtLastTime.setVisibility(View.VISIBLE);
//                        txtT.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public Long[] setTime(long timeMinitute) {
        long dis = timeMinitute - new Date().getTime();
        if (dis != 0) {
            long diftime = dis / 1000;
            long hour = 0, minute, second;
            if (diftime > 60) {
                if (diftime > 3600) {
                    hour = diftime / 3600;
                    minute = diftime % 3600 / 60;
                    second = diftime % 3600 % 60 % 60;
                } else {
                    second = diftime % 60;
                    minute = diftime / 60;
                }
            } else {
                second = diftime;
                minute = 0;
                hour = 0;
            }
            if (hour == 0 && minute == 0 && second <= 0) {
                return null;
            }
            return new Long[]{hour, minute, second};
        }
        return null;
    }

    /**
     * 设置优惠列表显示
     */
    private void setYouhuiAdapter(String[] list, final String[] youhuiPrice) {
//        TimeCountUtils utils = TimeCountUtils.getInstance();
//        utils.setOnTimeConuntListener(new TimeCountUtils.onTimeConunt() {
//            @Override
//            public void onProgress() {
//                if (OrderMessageAct.this != null)
//                    refreshChild();
//            }
//        });
        List<String> youhui = Arrays.asList(list);
        CommonAdapter<String> adapter = new CommonAdapter<String>(this, R.layout.item_youhui, youhui) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.youhui1, item);
                viewHolder.setText(R.id.integral_price, youhuiPrice[position]);
            }
        };
        youhuiList.setAdapter(adapter);
    }


    /**
     * 设置商品列表适配器
     */
    private void setShopAdapter() {
        CommonAdapter<OrderMessageEntry.DetailsBo> adapter =
                new CommonAdapter<OrderMessageEntry.DetailsBo>(this, R.layout.item_order_shop, order.getDetails()) {
                    @Override
                    protected void convert(ViewHolder viewHolder, final OrderMessageEntry.DetailsBo item, int position) {
                        final TextView apply_sales = viewHolder.getConvertView().findViewById(R.id.apply_sales);
                        viewHolder.setImageUrl(R.id.shop_img, item.getPiclogo());
                        viewHolder.setText(R.id.order_name, item.getName());
                        viewHolder.setText(R.id.order_moket_num, "×" + item.getNum());
                        String name = item.getAttrname();
                        String size = item.getAttrsize();
                        if (item.getAttrname().equals("共同")){
                            name = "";
                        }
                        if (item.getAttrsize().equals("共同")){
                            size = "";
                        }
                        viewHolder.setText(R.id.order_style, name+size);
                        final TextView orderOldprice = viewHolder.getView(R.id.order_oldprice);
                        orderOldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        orderOldprice.setText("¥" + item.getPrice());
                        viewHolder.setText(R.id.order_price, "¥" + item.getDisprice());

                        if ((order.getStatus().equals("1") || order.getStatus() .equals("2")) && order.getAdvanceid() != null
                                && (order.getVender() != null && !order.getVender().equals("2"))){
                            viewHolder.getView(R.id.apply_sales).setVisibility(View.VISIBLE);
                            viewHolder.setText(R.id.apply_sales,"申请退款");
                        }else if (order.getStatus().equals("3") && order.getVender() != null && order.getVender().equals("2")){
                            viewHolder.getView(R.id.apply_sales).setVisibility(View.VISIBLE);
                            viewHolder.setText(R.id.apply_sales,"申请退款");
                        }else if (order.getStatus().equals("1") && order.getAdvanceid() == null && order.getVender()!= null &&
                                order.getVender().equals("2")){
                            viewHolder.getView(R.id.apply_sales).setVisibility(View.VISIBLE);
                            viewHolder.setText(R.id.apply_sales,"申请退款");
                        }else if (order.getSkuId() != null && order.getPayStatus() != null && order.getPayStatus().equals("7")){
                            viewHolder.getView(R.id.apply_sales).setVisibility(View.VISIBLE);
                            viewHolder.setText(R.id.apply_sales,"寄回商品");
                        }else if (order.getSkuId() != null && order.getPayStatus() != null && order.getPayStatus().equals("8")){
                            viewHolder.getView(R.id.apply_sales).setVisibility(View.VISIBLE);
                            viewHolder.setText(R.id.apply_sales,"已寄出");
                        }else {
                            if (order.getStatus().equals("3")){
                                viewHolder.getView(R.id.apply_sales).setVisibility(View.GONE);
                            }else {
                                viewHolder.getView(R.id.apply_sales).setVisibility(View.VISIBLE);
                            }
                        }


                        viewHolder.getView(R.id.apply_sales).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (apply_sales.getText().equals("寄回商品") || apply_sales.getText().equals("已寄出")){
                                    Intent intent = new Intent(OrderMessageAct.this, HomeWebViewAct.class);
                                    intent.putExtra("url", "/order/refundLogisticsIndex?detailId=" + item.getId());
                                    intent.putExtra("all", false);
                                    intent.putExtra("isTitle", true);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(OrderMessageAct.this, ApplicationDrawbackActivity.class);
                                    intent.putExtra("isweb", false);
                                    intent.putExtra("orderNum", order.getOrdernum());
                                    startActivity(intent);
                                }
                            }
                        });
                        viewHolder.getView(R.id.rlBack).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(OrderMessageAct.this, HomeWebViewAct.class);
                                if (item.getJumpAdress().contains("?")) {
                                    intent.putExtra("url", item.getJumpAdress()+"&parentLocation=114");
                                }else {
                                    intent.putExtra("url", item.getJumpAdress()+"?parentLocation=114");
                                }
                                intent.putExtra("all", true);
                                intent.putExtra("isTitle", true);
                                startActivity(intent);
                            }
                        });
                    }
                };
        shopList.setAdapter(adapter);
    }

    PayPupWindow pupWindow;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_copy:   //复制按钮
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(order.getOrdernum());
                showToast("复制成功！");
                break;
            case R.id.pay_button:
                if ("0".equals(order.getStatus())) {  //未付款去付款
                    if (order.getAdvanceid() != null) {//advanceid为空是普通订单，不为空是预售订单
                        if (order.getAdvanceStatus().equals("0")){//值为0的时候是定金未付，值为1是定金已付尾款未付
//                            pupWindow = new PayPupWindow(OrderMessageAct.this);
//                            pupWindow.setOnPayButton(new PayPupWindow.onPayButton() {
//                                @Override
//                                public void onClick(int type) {
//                                    pupWindow.dismiss();
//                                    if (type == 1) {   //微信支付
//                                        payWX(order.getOrdernum());
//                                    } else {    //支付宝支付
//                                        payAliba(order.getOrdernum());
//                                    }
//                                }
//                            });
//                            pupWindow.showAtLocation(scollview, Gravity.BOTTOM, 0, 0);

                            Intent intent = new Intent(OrderMessageAct.this, PersonWebViewAct.class);
                            intent.putExtra("url", HttpClient.PAY_ORDER_MONEY + "?ordernum=" + order.getOrdernum()
                                    + "&distributorId=" + disId + "&source=1");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(OrderMessageAct.this, PersonWebViewAct.class);
                            intent.putExtra("url", HttpClient.PAY_PRESELL + "?ordernum=" + order.getOrdernum()
                                    + "&distributorId=" + disId + "&source=1");
                            startActivity(intent);
                        }
                    }else {
                        pupWindow = new PayPupWindow(OrderMessageAct.this);
                        pupWindow.setOnPayButton(new PayPupWindow.onPayButton() {
                            @Override
                            public void onClick(int type) {
                                pupWindow.dismiss();
                                if (type == 1) {   //微信支付
                                    payWX(order.getOrdernum());
                                } else {    //支付宝支付
                                    payAliba(order.getOrdernum());
                                }
                            }
                        });
                        pupWindow.showAtLocation(scollview, Gravity.BOTTOM, 0, 0);
                    }
                } else if ("1".equals(order.getStatus())) {    //提醒发货
                    remindOrder(order.getOrdernum());
                } else {     //再次购买
                    againPayShop(order.getOrdernum());
                }
                break;
            case R.id.confirm_receipt:
                if (confirmRe) {    //确认收货
                    confirmReceipt(order.getOrdernum());
                } else {      //再次购买
                    EventBus.getDefault().post(new Boolean(false));
                    AppManager.getAppManager().goBackMain();
                }
                break;
            case R.id.see_flow:    //查看物流
                Intent intent = new Intent(OrderMessageAct.this, PersonWebViewAct.class);
                intent.putExtra("url", HttpClient.ORDER_RED_LOG + "?jumptype=1&ordernum=" +
                        order.getDetails().get(0).getId() +
                        "&distributorId=" + disId);
                intent.putExtra("isTitle", true);
                startActivity(intent);
                break;
        }
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
    private void confirmReceipt(String orderNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordernum", orderNum);
        HttpClient.post(HttpClient.ORDER_CONFIRM, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    showToast(object.getString("message"));
                    confirmReceipt.setText("再次购买");
                    confirmRe = false;
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
                    AppManager.getAppManager().goBackMain();
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
            PayTask alipay = new PayTask(OrderMessageAct.this);
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
                case 0x22:   //支付宝支付完成
                    getOrderData();
                    break;
            }
        }
    };
}
