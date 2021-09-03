package com.haoyigou.hyg.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.MessageBean;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.utils.DownLoadImageUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2016/12/19.
 * <p>
 * 消息盒子页面
 */

public class MessageBoxActivty extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    LinearLayout backTo;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.huodong_num)
    TextView huodongNum;
    @BindView(R.id.huodong_message)
    TextView huodongMessage;
    @BindView(R.id.huodong_time)
    TextView huodongTime;
    @BindView(R.id.dingdan_num)
    TextView dingdanNum;
    @BindView(R.id.dingdan_message)
    TextView dingdanMessage;
    @BindView(R.id.dingdan_time)
    TextView dingdanTime;
    @BindView(R.id.tongzhi_num)
    TextView tongzhiNum;
    @BindView(R.id.tongzhi_message)
    TextView tongzhiMessage;
    @BindView(R.id.tongzhi_time)
    TextView tongzhiTime;
    @BindView(R.id.zichan_num)
    TextView zichanNum;
    @BindView(R.id.zichan_message)
    TextView zichanMessage;
    @BindView(R.id.zichan_time)
    TextView zichanTime;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.huodong_layout)
    RelativeLayout huodongLayout;
    @BindView(R.id.dingdan_layout)
    RelativeLayout dingdanLayout;
    @BindView(R.id.tongzhi_layout)
    RelativeLayout tongzhiLayout;
    @BindView(R.id.zichan_layout)
    RelativeLayout zichanLayout;
    @BindView(R.id.kefu_layout)
    RelativeLayout kefu_layout;
    @BindView(R.id.kefu_num)
    TextView kefu_num;//客服数量
    @BindView(R.id.kefu_title)
    TextView kefu_title;//客服title
    @BindView(R.id.kefu_message)
    TextView kefu_message;
    @BindView(R.id.kefu_time)
    TextView kefu_time;

    private String disId;
    private MessageBean message;
    private boolean isPush = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_box_layout);
        ButterKnife.bind(this);

        isPush = getIntent().getBooleanExtra("push", false);
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isPush) {
//                    goToActivity(MainActivity.class, true);
//                } else {
                    finish();
//                }
            }
        });
        kefu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomerService("","undefined");
            }
        });
        titleText.setText("我的消息");
        disId = SharedPreferencesUtils.getInstance().getString("distributorId", "");
        setListener();
    }


    private void openCustomerService(String logo, String str){
        if (ContextCompat.checkSelfPermission(MessageBoxActivty.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( MessageBoxActivty.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        final HashMap<String, String> clientInfo = new HashMap<>();
        final String shopMessage;
        if (str.equals("undefined")) {
            Intent intent2 = new MQIntentBuilder(MessageBoxActivty.this)
                    .build();
            startActivity(intent2);
            return;
        }
//        Log.e("log--", "呼叫客服" + str);
        if (str.indexOf("&*") != -1) {
            String[] users = str.split("\\**");
            clientInfo.put("name", users[1]);
            clientInfo.put("tel", users[2]);
            clientInfo.put("comment", users[3]);
            shopMessage = users[0];
        } else {
            shopMessage = str;
            if (StringUtils.isEmpty(logo)) {
                Intent intent2 = new MQIntentBuilder(MessageBoxActivty.this)
                        .setPreSendTextMessage(shopMessage)
                        .setClientInfo(clientInfo).build();
                startActivity(intent2);
                return;
            }
        }
        new DownLoadImageUtils().startDownLoading(logo, new DownLoadImageUtils.onDownLodingSource() {
            @Override
            public void onSource(String filePath) {
                Intent intent2 = new MQIntentBuilder(MessageBoxActivty.this)
                        .setPreSendTextMessage(shopMessage)
                        .setPreSendImageMessage(new File(filePath))
                        .setClientInfo(clientInfo).build();
                startActivity(intent2);
            }
        });
    }
    /**
     * 设置监听
     */
    private void setListener() {
        huodongLayout.setOnClickListener(this);
        dingdanLayout.setOnClickListener(this);
        tongzhiLayout.setOnClickListener(this);
        zichanLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /***
     * 获取各自消息数量
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("distributorId", disId);
        HttpClient.get(HttpClient.MESSAGEBOX, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--mesbox", content);
                if (content == null || content.equals("")) return;
                JSONObject object = JSON.parseObject(content);
                if (object.getString("status").equals(HttpClient.RET_SUCCESS_CODE)) {
                    String result = object.getString("result");
                    message = JSONObject.parseObject(result, MessageBean.class);
                    setShowMessage();
                }
            }
        }, this, false);
    }

    /**
     * 为界面设置值
     */
    private void setShowMessage() {
        int Tpnum = message.getTpnum();
        if (Tpnum <= 0) {
            huodongNum.setVisibility(View.GONE);
        } else {
            huodongNum.setVisibility(View.VISIBLE);
            huodongNum.setText(Tpnum + "");
        }
        huodongMessage.setText(message.getTptitle());
        huodongTime.setText(getTimeCound(message.getTpdatestr()));
        int Osnum = message.getOsnum();
        if (Osnum <= 0) {
            dingdanNum.setVisibility(View.GONE);
        } else {
            dingdanNum.setVisibility(View.VISIBLE);
            dingdanNum.setText(Osnum + "");
        }
        dingdanMessage.setText(message.getOstitle());
        dingdanTime.setText(getTimeCound(message.getOsdatestr()));
        int Infornum = message.getInformnum();
        if (Infornum <= 0) {
            tongzhiNum.setVisibility(View.GONE);
        } else {
            tongzhiNum.setVisibility(View.VISIBLE);
            tongzhiNum.setText(Infornum + "");
        }
        tongzhiMessage.setText(message.getInformtitle());
        tongzhiTime.setText(getTimeCound(message.getInformdatestr()));
        int Assetnum = message.getAssetnum();
        if (Assetnum <= 0) {
            zichanNum.setVisibility(View.GONE);
        } else {
            zichanNum.setVisibility(View.VISIBLE);
            zichanNum.setText(Assetnum + "");
        }
        zichanMessage.setText(message.getAssettitle());
        zichanTime.setText(getTimeCound(message.getAssetdatestr()));
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MessageBoxDetailsAct.class);
        switch (view.getId()) {
            case R.id.huodong_layout:   //精选活动
                intent.putExtra("Type", "1");
//                huodongNum.setVisibility(View.GONE);
                break;
            case R.id.dingdan_layout:  //订单服务
                intent.putExtra("Type", "2");
//                dingdanNum.setVisibility(View.GONE);
                break;
            case R.id.tongzhi_layout:   //消息通知
                intent.putExtra("Type", "3");
//                tongzhiNum.setVisibility(View.GONE);
                break;
            case R.id.zichan_layout:   //我的资产
                intent.putExtra("Type", "4");
//                zichanNum.setVisibility(View.GONE);
                break;
        }
        startActivity(intent);
    }


    /**
     * 将时间转成HH:mm
     */
    private String getTimeCound(String time) {
        if (StringUtils.isEmpty(time)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().getDay() == date.getDay()) {   //是同一天
            return new SimpleDateFormat("HH:mm").format(date);
        } else {   //不是同一天
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
    }

}