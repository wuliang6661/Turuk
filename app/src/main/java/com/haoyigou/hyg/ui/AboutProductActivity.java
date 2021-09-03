package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.AboutProductBean;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Witness on 2020-04-20
 * Describe: 关联商品
 */
public class AboutProductActivity extends BaseActivity {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.rlTitleBg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.grid_view)
    ListView listView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private List<AboutProductBean> data = new ArrayList<>();
    private CommonAdapter<AboutProductBean> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_product_layout);
        ButterKnife.bind(this);
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        startProgressDialog("",AboutProductActivity.this);
        getProduct();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }


    private void setAdapter(){
        adapter = new CommonAdapter<AboutProductBean>(this,R.layout.about_product_item_layout,data) {
            @Override
            protected void convert(ViewHolder holder, AboutProductBean item, int position) {
                Glide.with(AboutProductActivity.this).load(item.getPiclogo()).asBitmap().into((ImageView)holder.getView(R.id.productImg));

                holder.setText(R.id.name,item.getName());
                holder.setText(R.id.price,String.format("￥%s",item.getPrice()));

                holder.getView(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("id",String.valueOf(item.getId()));
                        intent.putExtra("name",item.getName());
                        setResult(666,intent);
                        finish();
                    }
                });
            }
        };
        listView.setAdapter(adapter);
    }



    private void getProduct() {
        Map<String, Object> params = new HashMap<>();
        params.put("type",getIntent().getStringExtra("sendType"));
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.SHOWPRODUCT_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (refresh != null) {
                    refresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    data.clear();
                    data.addAll(JSON.parseArray(object.getString("data"),AboutProductBean.class));
                    setAdapter();
                }
            }
        }, AboutProductActivity.this);
    }

}
