package com.haoyigou.hyg.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.TVLiveEntry;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DateTimeUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;

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
 * 直播列表
 */

public class TVLiveListActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    @BindView(R.id.yesterday)
    TextView yesterday;
    @BindView(R.id.totay)
    TextView totay;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.live_list)
    ListView liveList;


    int type = 1;// 默认今日直播

    List<TVLiveEntry> live;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_list);
        ButterKnife.bind(this);
        goBack();
        setTitleText("直播表");

        yesterday.setOnClickListener(this);
        totay.setOnClickListener(this);
        liveList.setOnItemClickListener(this);

        ViewGroup.LayoutParams params = line.getLayoutParams();
        params.width = GlobalApplication.screen_width / 2;
        line.setLayoutParams(params);
        yesterday.setText("昨日直播\n" + DateTimeUtils.getLastDate());
        totay.setText("今日直播\n" + DateTimeUtils.gainCurrentDate("MM月dd日"));
        getData(2);
    }

    @Override
    public void onClick(View v) {
        TranslateAnimation animation;
        switch (v.getId()) {
            case R.id.yesterday:
                if (type == 0) {
                    return;
                }
                animation = new TranslateAnimation(0, -GlobalApplication.screen_width / 2, 0, 0);
                animation.setDuration(300);
                animation.setFillAfter(true);//设置为true，动画转化结束后被应用
                line.startAnimation(animation);//开始动画
                yesterday.setTextColor(getResources().getColor(R.color.title_bg));
                totay.setTextColor(Color.parseColor("#666666"));
                type = 0;
                getData(1);
                break;
            case R.id.totay:
                if (type == 1) {
                    return;
                }
                animation = new TranslateAnimation(-GlobalApplication.screen_width / 2, 0, 0, 0);
                animation.setDuration(300);
                animation.setFillAfter(true);//设置为true，动画转化结束后被应用
                line.startAnimation(animation);//开始动画
                totay.setTextColor(getResources().getColor(R.color.title_bg));
                yesterday.setTextColor(Color.parseColor("#666666"));
                type = 1;
                getData(2);
                break;

        }
    }

    /**
     * 获取直播列表数据
     */
    private void getData(int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
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
     * 设置直播列表适配器
     */
    private void setLiveAdapter() {
        CommonAdapter<TVLiveEntry> adapter = new CommonAdapter<TVLiveEntry>(this, R.layout.item_live, live) {
            @Override
            protected void convert(ViewHolder viewHolder, final TVLiveEntry item, int position) {
                Date date = new Date();
                TextView liveType = viewHolder.getView(R.id.live_type);
                viewHolder.setImageUrl(R.id.live_img, item.getLogopic());
                viewHolder.setText(R.id.live_name, item.getProductname());
                TextView oldPrice = viewHolder.getView(R.id.live_price);
                TextView liveTishi = viewHolder.getView(R.id.live_tishi);
                TextView price = viewHolder.getView(R.id.live_allprice);
                oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                if ("0".equals(item.getStore())) {
                    viewHolder.getView(R.id.store_layout).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.store_layout).setVisibility(View.GONE);
                }
                if (Double.parseDouble(item.getDisprice()) == 0) {
                    viewHolder.getView(R.id.price_stutus).setVisibility(View.GONE);
                    price.setText("【电话订单】");
                } else {
                    viewHolder.getView(R.id.price_stutus).setVisibility(View.VISIBLE);
                    price.setText(String.valueOf(Double.parseDouble(item.getDisprice())));
                }
                if (Double.parseDouble(item.getOldprice()) == 0) {
                    oldPrice.setText("市场价：¥");
                } else {
                    oldPrice.setText("市场价：¥" + Double.parseDouble(item.getOldprice()));
                }
                viewHolder.setText(R.id.live_time_txt, DateTimeUtils.formatTime(item.getBegintime(), DateTimeUtils.DF_HH_MM));
                if (date.getTime() > item.getEndtime()) {  //已结束
                    liveType.setVisibility(View.GONE);
                    viewHolder.setTextColor(R.id.live_time_txt, Color.parseColor("#888888"));
                    viewHolder.setImageResource(R.id.live_time_img, R.drawable.time1);
                    liveTishi.setVisibility(View.GONE);
                } else if (date.getTime() < item.getEndtime()
                        && date.getTime() > item.getBegintime()) {   //正在播出
                    liveType.setVisibility(View.VISIBLE);
                    viewHolder.setTextColorRes(R.id.live_time_txt, R.color.title_bg);
                    viewHolder.setImageResource(R.id.live_time_img, R.drawable.time2);
                    liveTishi.setVisibility(View.GONE);
                } else if (date.getTime() < item.getBegintime()) {   //未播出
                    liveType.setVisibility(View.GONE);
                    viewHolder.setTextColorRes(R.id.live_time_txt, R.color.title_bg);
                    viewHolder.setImageResource(R.id.live_time_img, R.drawable.time2);
                    if ("0".equals(item.getNoticestatus())) {
                        liveTishi.setTextColor(Color.parseColor("#e72e2d"));
                        liveTishi.setBackgroundResource(R.drawable.button_red_border);
                        liveTishi.setText("直播提醒");
                        liveTishi.setEnabled(true);
                    } else {
                        liveTishi.setTextColor(Color.parseColor("#717171"));
                        liveTishi.setBackgroundResource(R.drawable.button_gray_brolder);
                        liveTishi.setText("已提醒");
                        liveTishi.setEnabled(false);
                    }
                    liveTishi.setVisibility(View.VISIBLE);
                }
                liveTishi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLiveTishi(item, v);
                    }
                });
            }
        };
        liveList.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if ("0".equals(live.get(position).getProductid())) {
//            showToast("电视直播专属商品，请拨打电话进行下单购买！");
            showToast("特殊商品，请拨打4001188188咨询购买！");
            return;
        }
        Intent intent = new Intent(this, HomeWebViewAct.class);
        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + live.get(position).getProductid();
        intent.putExtra("url", url);
        startActivity(intent);
    }


    /**
     * 设置直播提醒
     */
    private void setLiveTishi(TVLiveEntry item, final View view) {
        Map<String, Object> params = new HashMap<>();
        params.put("distributorId", disId);
        params.put("id", item.getId());
        HttpClient.post(HttpClient.LIVE_TISHI, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                        TextView view1 = (TextView) view;
                        view1.setText("已提醒");
                        view1.setTextColor(Color.parseColor("#717171"));
                        view1.setBackgroundResource(R.drawable.button_gray_brolder);
                        view1.setEnabled(false);
                        showToast("设置提醒成功！");
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, TVLiveListActivity.this);
    }


    /**
     * 弹出直播提醒提示弹窗
     */
    private void showHintDialog() {
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.dialog_live, null);//这里必须是final的
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
            }
        });
        dialog.setView(view);
        dialog.show();
    }
}
