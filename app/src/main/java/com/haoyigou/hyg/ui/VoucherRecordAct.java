package com.haoyigou.hyg.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.VoucherRecordEntry;
import com.haoyigou.hyg.utils.DateTimeUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.base.BaseActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/4/6.
 * <p>
 * 充值记录页面
 */

public class VoucherRecordAct extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_record_act);
        ButterKnife.bind(this);
        setTitleText("充值记录");
        goBack();
        getData();
        if (!StateMessage.payOrderNum.equals("") && !StateMessage.activityTitle.equals("")){
            showPrizaDialog();
        }
    }

    /**
     * 获取充值记录数据
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.VOUCHERRECORDS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--充值记录", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String array = object.getString("result");
                    List<VoucherRecordEntry> list = JSONArray.parseArray(array, VoucherRecordEntry.class);
                    setAdapter(list);
                }
            }
        }, this);
    }

    /**
     * 设置列表数据
     */
    private void setAdapter(List<VoucherRecordEntry> list) {
        CommonAdapter<VoucherRecordEntry> adapter =
                new CommonAdapter<VoucherRecordEntry>(this, R.layout.item_voucher_record, list) {
                    @Override
                    protected void convert(ViewHolder viewHolder, VoucherRecordEntry item, int position) {
                        viewHolder.setText(R.id.price, "-" + item.getDisprice());
                        Date data = DateTimeUtils.parseDate(item.getZftimeStr());
                        viewHolder.setText(R.id.date, DateTimeUtils.formatDateTime(data, DateTimeUtils.DF_YYYY_MM_DD));
                        viewHolder.setText(R.id.time, DateTimeUtils.formatDateTime(data, DateTimeUtils.DF_HH_MM_SS));
                        viewHolder.setText(R.id.message, "充值" + item.getRechargeName() + "-" + item.getAccount());
                        switch (item.getStatus()) {
                            case "1":
                            case "2":
                            case "-2":
                                viewHolder.setText(R.id.price_sturts, "已支付");
                                break;
                            case "-1":
                                viewHolder.setText(R.id.price_sturts, "充值失败");
                                break;
                            case "3":
                                viewHolder.setText(R.id.price_sturts, "已到账");
                                break;
                            case "-3":
                                viewHolder.setText(R.id.price_sturts, "已退款");
                                break;
                        }
                    }
                };
        listview.setAdapter(adapter);
    }

    /**
     * 领取充值红包
     */
    private void showPrizaDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.prize_dialog, null);
        ImageView getPrize = v.findViewById(R.id.getPrize);
        ImageView close = v.findViewById(R.id.close);

        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.CENTER);
        noticeDialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
                //清空活动和订单号
                StateMessage.payOrderNum = "";
                StateMessage.activityTitle = "";
            }
        });
        getPrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
                getPrize(StateMessage.payOrderNum);
            }
        });
        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        noticeDialog.getWindow().setAttributes(layoutParams);
    }

    /**
     * 领取充值红包
     */
    private void getPrize(String orderNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNum",orderNum);
        HttpClient.post(HttpClient.GET_PAY_PAKCAGE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    ToastUtils.showToast(VoucherRecordAct.this,object.getString("message"));
                    //清空活动和订单号
                    StateMessage.payOrderNum = "";
                    StateMessage.activityTitle = "";
                }else {
                    ToastUtils.showToast(VoucherRecordAct.this,object.getString("message"));
                }
            }
        }, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空活动和订单号
        StateMessage.payOrderNum = "";
        StateMessage.activityTitle = "";
    }
}
