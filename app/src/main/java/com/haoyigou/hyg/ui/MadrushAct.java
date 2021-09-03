package com.haoyigou.hyg.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.BerserListAdapterBase;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.BerserkListEntry;
import com.haoyigou.hyg.entity.MadrushBO;
import com.haoyigou.hyg.utils.TimeCountUtils;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/3.
 */
public class MadrushAct extends BaseActivity implements View.OnClickListener {
    private static int FENGQIANGTYPE = 1;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_image01)
    ImageView rightImage01;
    @BindView(R.id.right_image)
    ImageView rightImage;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.fengqiang_text)
    TextView fengqiangText;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.next_notice)
    TextView nextNotice;
    @BindView(R.id.view_line2)
    View viewLine2;
    @BindView(R.id.not_fengqiang)
    TextView notFengqiang;
    @BindView(R.id.fengqiang_list)
    ListViewForScrollView fengqiangList;
//    @InjectView(R.id.listview)
//    ListView listview;

    private List<MadrushBO> berserkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.madrushact_act);
        ButterKnife.bind(this);
        setTitleText("正在疯抢");
        goBack();
        loadFengQiang(1);
        nextNotice.setOnClickListener(this);
        fengqiangText.setOnClickListener(this);
    }


    private void loadFengQiang(final int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", type + "");
        if (!MApplication.madrushParentLocation.equals("")){
            params.put("parentLocation",MApplication.madrushParentLocation);
        }
        HttpClient.post(HttpClient.MADRUSH, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    berserkList = JSONArray.parseArray(result.getData(), MadrushBO.class);
                    setBannerdAdapter(type);
                } else {
                    showToast(result.getMessage());
                }
            }
        }, this);
    }

    /**
     * 设置疯抢列表适配器
     */
    private void setBannerdAdapter(int i) {
        if (FENGQIANGTYPE == 1) {
            notFengqiang.setText("本次活动已结束！");
        } else {
            notFengqiang.setText("活动暂未开始，敬请期待！");
        }
        if (berserkList == null || berserkList.size() == 0) {
            notFengqiang.setVisibility(View.VISIBLE);
            fengqiangList.setVisibility(View.GONE);
            return;
        }
        BerserListAdapterBase berserkAdapter = new BerserListAdapterBase(this, berserkList);
        berserkAdapter.setIndex(i);
        fengqiangList.setAdapter(berserkAdapter);
        TimeCountUtils utils = TimeCountUtils.getInstance();
        utils.setOnTimeConuntListener(new TimeCountUtils.onTimeConunt() {
            @Override
            public void onProgress() {
                if (this != null)
                    refreshChild();
            }
        });
        notFengqiang.setVisibility(View.GONE);
        fengqiangList.setVisibility(View.VISIBLE);
    }

    /**
     * 定时器计算并给每一行更新数据
     */
    private void refreshChild() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < berserkList.size(); i++) {
                    if (berserkList.get(i).getProduct().size()>0) {
                        Long[] time = setTime(berserkList.get(i).getTime());
                        if (time == null) {
                            berserkList.remove(i);
                            setBannerdAdapter(1);
                        } else {
                            View view = fengqiangList.getChildAt(i);
                            if (view == null) return;
                            TextView hour = (TextView) view.findViewById(R.id.hour_text);
                            TextView minnute = (TextView) view.findViewById(R.id.minute_text);
                            TextView second = (TextView) view.findViewById(R.id.second_text);
                            LinearLayout llTime = (LinearLayout) view.findViewById(R.id.llTime);
                            llTime.setVisibility(View.VISIBLE);
                            hour.setText(time[0] + "");
                            minnute.setText(time[1] + "");
                            second.setText(time[2] + "");
                        }
                    }else {
                        View view = fengqiangList.getChildAt(i);
                        if (view == null) return;
                        LinearLayout llTime = (LinearLayout) view.findViewById(R.id.llTime);
                        llTime.setVisibility(View.GONE);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fengqiang_text:   //正在疯抢
                if (FENGQIANGTYPE == 1) return;
                FENGQIANGTYPE = 1;
                loadFengQiang(1);
                fengqiangText.setTextColor(Color.parseColor("#0E4394"));
                nextNotice.setTextColor(Color.parseColor("#666666"));
                viewLine.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.GONE);
                break;
            case R.id.next_notice:    //下期预告
                if (FENGQIANGTYPE == 2) return;
                FENGQIANGTYPE = 2;
                loadFengQiang(2);
                fengqiangText.setTextColor(Color.parseColor("#666666"));
                nextNotice.setTextColor(Color.parseColor("#0E4394"));
                viewLine.setVisibility(View.GONE);
                viewLine2.setVisibility(View.VISIBLE);
                break;
        }

    }

}