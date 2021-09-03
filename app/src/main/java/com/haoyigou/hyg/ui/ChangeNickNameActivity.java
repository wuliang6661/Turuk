package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.SavePersonalInfomationEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * 修改昵称
 */
public class ChangeNickNameActivity extends BaseActivity {
    private EditText editText;
    private ImageView tv_cancle;
    private TextView tv_finish;
    private ImageView im_delete;
    private RelativeLayout relative_changenicke;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set_name);
        initview();
        initevent();
    }

    private void initevent() {
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.length() == 0) {
                    Toast.makeText(ChangeNickNameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (editText.length() > 10) {
                    Toast.makeText(ChangeNickNameActivity.this, "昵称长度不能大于10位", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadData();

                //  Log.e("TAGGNAME",name);


            }
        });
        im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
//        relative_changenicke.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private void loadData() {
        String nickname = editText.getText().toString();
        SavePersonalInfomationEntity param = new SavePersonalInfomationEntity();
        param.setSaveType("3");
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        param.setNickname(nickname);
        HttpClient.changnickname(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getNickName", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    name = object.getString("nowshopname");
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
//                     bundle.putString("name",name);
//                      Log.e("Shopname",name);
                    Intent intent = new Intent(ChangeNickNameActivity.this, SettingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Toast.makeText(ChangeNickNameActivity.this, "您成功了保存昵称", Toast.LENGTH_SHORT).show();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, ChangeNickNameActivity.this);

    }

    private void initview() {
        relative_changenicke = (RelativeLayout) findViewById(R.id.relative_changenicke);
        editText = (EditText) findViewById(R.id.changnickname);
        tv_cancle = (ImageView) findViewById(R.id.tv_nick_name_cancle);
        tv_finish = (TextView) findViewById(R.id.tv_change_nick_name_finish);
        im_delete = (ImageView) findViewById(R.id.delete_nick_name);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeNickNameActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaduserinfo();
    }

    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        HttpClient.appsettinguserinfo(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getMobileCodeuser", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if ("1".equals(object.getString("status"))) {
                    String nicjname = object.getString("nickname");
                    editText.setText(nicjname);
                } else if ("0".equals(object.getString("status"))) {

                }

            }

            public void onFailure(Request request, IOException e) {
            }
        }, ChangeNickNameActivity.this);
    }
}
