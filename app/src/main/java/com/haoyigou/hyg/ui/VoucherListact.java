package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.BaseQuickAdapter;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.ShopBO;
import com.haoyigou.hyg.entity.TVLiveEntry;
import com.haoyigou.hyg.entity.VoucherListactBo;

import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DateTimeUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/18.
 */

public class VoucherListact extends BaseActivity implements AdapterView.OnItemClickListener{
    private ListView liveList;
    private ArrayList<VoucherListactBo> shopBOs= new ArrayList<>();
    private int id;
    public final static int RESULT_CODE=1;
    public final static int RESULT_CODE_NO=2;
    private String sty;
    private TextView no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_voucher);
        liveList = (ListView)findViewById(R.id.list);
        String sID=getIntent().getStringExtra("VoucherListactid");
        sty=getIntent().getStringExtra("sty");
        id=Integer.parseInt(sID);
        liveList.setOnItemClickListener(this);
        setTitleText("使用优惠券");
        goBack();
        getdata();
        no = (TextView)findViewById(R.id.no);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nodata = new Intent();
                nodata.putExtra("no", "no");
                setResult(RESULT_CODE_NO, nodata);// 设置resultCode，onActivityResult()中能获取到
                finish();
            }
        });

    }

    private void getdata() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        HttpClient.post(HttpClient.CHARGECOUPON, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (object.getString("status").equals(HttpClient.RET_SUCCESS_CODE)) {
                    String data = object.getString("result");
//                    Log.e("log--aaaa", "成功"+content);
                    List<VoucherListactBo> mvoucherListactBo = JSONArray.parseArray(data, VoucherListactBo.class);
                    shopBOs.clear();
                    shopBOs.addAll(mvoucherListactBo);
                    setLiveAdapter();
                }


            }
        },this);
    }
    private void setLiveAdapter() {
        CommonAdapter<VoucherListactBo> adapter = new CommonAdapter<VoucherListactBo>(this, R.layout.item_voucher, shopBOs) {
            @Override
            protected void convert(ViewHolder viewHolder, VoucherListactBo item, int position) {

                if(shopBOs.get(position).getCanuse().equals("0")){
                    viewHolder.getView(R.id.youhuijuanbg).setBackgroundResource(R.drawable.bukeyong);
                }
                if(shopBOs.get(position).getCanuse().equals("1")){
                    viewHolder.getView(R.id.youhuijuanbg).setBackgroundResource(R.drawable.youhuijiuan);
                }

                viewHolder.setText(R.id.kaishi, item.getBtime());
                viewHolder.setText(R.id.jieshu, item.getEtime());//discount
                viewHolder.setText(R.id.jine, String.valueOf(item.getDiscount()));
                viewHolder.setText(R.id.men, item.getTitle());
                viewHolder.setText(R.id.tatle, "满"+String.valueOf(item.getLimitprice())+"元使用");
            }

        };
        liveList.setAdapter(adapter);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(shopBOs.get(position).getCanuse().equals("1")){
            Intent data = new Intent();
            data.putExtra("style", sty);
            data.putExtra("youhui", "1");
            data.putExtra("text",shopBOs.get(position).getTitle());
            data.putExtra("jine", shopBOs.get(position).getDiscount().toString());
            data.putExtra("shopBOsresult", String.valueOf(shopBOs.get(position).getId()));
//            Log.e("DDDFFF", shopBOs.get(position).getDiscount().toString());
            setResult(RESULT_CODE, data);// 设置resultCode，onActivityResult()中能获取到
            finish();
        }
        if(shopBOs.get(position).getCanuse().equals("0")){

        }


    }

}
