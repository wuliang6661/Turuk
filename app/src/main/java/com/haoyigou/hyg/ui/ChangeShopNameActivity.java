package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.SavePersonalInfomationEntity;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.okhttp.Request;

import java.io.IOException;

public class ChangeShopNameActivity extends Activity {
    private EditText editText;
    private TextView tv_cancle;
    private TextView tv_finish;
    private ImageView im_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set_shop);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.color_statu_gray);//通知栏所需颜色
        }
        initview();
        initevevnt();
        // loadData();
    }

    private void initevevnt() {
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeShopNameActivity.this, "您取消了保存店铺名字", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                Intent intent = new Intent(ChangeShopNameActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    private void loadData() {
        String shopname = editText.getText().toString();
        SavePersonalInfomationEntity param = new SavePersonalInfomationEntity();
        param.setSaveType("2");
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        param.setName(shopname);
        HttpClient.changeshopname(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getShopName", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                // if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                String name = object.getString("nowshopname");
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
//                Log.e("Shopname", name);
                Intent intent = new Intent(ChangeShopNameActivity.this, SettingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(ChangeShopNameActivity.this, "您成功了保存店铺名字", Toast.LENGTH_SHORT).show();
                //} else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                //}
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, ChangeShopNameActivity.this);
    }

    private void initview() {
        editText = (EditText) findViewById(R.id.et_changeshopname);
        tv_cancle = (TextView) findViewById(R.id.tv_shop_name_cancle);
        tv_finish = (TextView) findViewById(R.id.tv_change_shop_name_finish);
        im_delete = (ImageView) findViewById(R.id.delete_shop_name);
    }
}
