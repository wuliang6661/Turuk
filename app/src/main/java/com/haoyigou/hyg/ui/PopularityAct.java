package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.HomeRenQiAdapter;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.HomeThreeBO;
import com.haoyigou.hyg.entity.PopularityBo;
import com.haoyigou.hyg.entity.RenQiBO;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/3.
 */
public class PopularityAct  extends BaseActivity {

    @BindView(R.id.list_renqi)
    ListViewForScrollView listRenqi;
    private String idparam;
    private List<RenQiBO> threeBO;
//    @InjectView(R.id.listview)
//    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popularity_act);
        ButterKnife.bind(this);
        setTitleText(getIntent().getStringExtra("tatle"));
        goBack();
        getData();
    }

    /**
     * 获取充值记录数据
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageId", getIntent().getStringExtra("idparam"));
        if (!MApplication.popularityParentLocation.equals("")){
            params.put("parentLocation",MApplication.popularityParentLocation);
        }
        HttpClient.post(HttpClient.JUNIOR_RECOMMEND, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                PopularityBo result = JSONObject.parseObject(content, PopularityBo.class);
                if (result.getStatus()==1) {
                    threeBO = result.getData();
                    //人气推荐
                    setUIData2();
                }}

        }, this);
    }
    private void setUIData2() {
        if (threeBO == null || threeBO.size() == 0) {
            listRenqi.setVisibility(View.GONE);
        } else {
            HomeRenQiAdapter adapter3 = new HomeRenQiAdapter(this, disId, threeBO);
            listRenqi.setAdapter(adapter3);

        }
    }
    }

