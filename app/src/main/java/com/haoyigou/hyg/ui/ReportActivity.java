package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Witness on 2020/8/4
 * Describe: 举报
 */
public class ReportActivity extends BaseActivity {

    @BindView(R.id.imageOne)
    ImageView imageOne;
    @BindView(R.id.btnOne)
    RelativeLayout btnOne;
    @BindView(R.id.imageTwo)
    ImageView imageTwo;
    @BindView(R.id.btnTwo)
    RelativeLayout btnTwo;
    @BindView(R.id.imageThree)
    ImageView imageThree;
    @BindView(R.id.btnThree)
    RelativeLayout btnThree;
    @BindView(R.id.imageFour)
    ImageView imageFour;
    @BindView(R.id.btnFour)
    RelativeLayout btnFour;
    @BindView(R.id.submitReport)
    Button submitReport;

    private String productId = "";
    private String reportTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);
        ButterKnife.bind(this);
        goBack();
        setTitleText("商品举报");
        if (getIntent().getStringExtra("id") != null) {
            productId = getIntent().getStringExtra("id");
        }
    }

    /**
     * 举报
     */
    private void report(String productId, String text) {
        startProgressDialog("", this);
        final Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("text", text);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", ""));
        HttpClient.post(HttpClient.REPORT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
//                JSONObject object = JSON.parseObject(content);
                showToast("举报成功");
                finish();
//                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
//                    showToast("举报成功");
//                    finish();
//                } else {
//                    showToast(object.getString("message"));
//                }
            }
        }, this);
    }

    @OnClick({R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour,R.id.submitReport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnOne:
                imageOne.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_select));
                imageTwo.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageThree.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageFour.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                reportTxt = "出售禁售品";
                break;
            case R.id.btnTwo:
                imageOne.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageTwo.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_select));
                imageThree.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageFour.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                reportTxt = "假冒品牌/盗版";
                break;
            case R.id.btnThree:
                imageOne.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageTwo.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageThree.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_select));
                imageFour.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                reportTxt = "疑似欺诈";
                break;
            case R.id.btnFour:
                imageOne.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageTwo.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageThree.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_normal));
                imageFour.setImageDrawable(getResources().getDrawable(R.drawable.image_oval_select));
                reportTxt = "泄露隐私";
                break;
            case R.id.submitReport:
                if (StateMessage.IS_LOGIN) {  //判断是否登陆
                    if (reportTxt.equals("")){
                        ToastUtils.showToast(this,"请选择举报原因");
                        return;
                    }
                    report(productId, reportTxt);
                }else {
                    startActivity(new Intent(ReportActivity.this,LoginActivity.class));
                }
                break;
        }
    }
}
