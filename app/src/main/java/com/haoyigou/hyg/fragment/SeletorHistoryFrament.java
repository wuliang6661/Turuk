package com.haoyigou.hyg.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.view.dragflowlayout.DragFlowLayout;
import com.haoyigou.hyg.base.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/3/16.
 * <p>
 * 显示搜索页面历史记录的Fragment
 */

public class SeletorHistoryFrament extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.history_grid)
    DragFlowLayout historyGrid;
    @BindView(R.id.red_grid)
    DragFlowLayout redGrid;


    List<Object> history;
    List<Object> hotList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainview = inflater.inflate(R.layout.fra_seletor_history, null);
        ButterKnife.bind(this, mainview);
        return mainview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
        delete.setOnClickListener(this);
    }

    /***
     * 获取搜索数据
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("distributorId", disId);
        params.put("showplat", "2");
        if (!MApplication.selectorParentLocation.equals("")){
            params.put("parentLocation",MApplication.selectorParentLocation);
        }
        HttpClient.post(HttpClient.SELETORHISTORY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--his", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    JSONArray historyList = object.getJSONArray("historyList");
                    JSONArray hot = object.getJSONArray("hotList");
                    if (history != null) history.clear();
                    if (hotList != null) hotList.clear();
                    history = historyList.subList(0, historyList.size());
                    hotList = hot.subList(0, hot.size());
                    setAdapter();
                }
            }
        }, getActivity());
    }


    /**
     * 为流式布局FlowLayout设置数据
     */
    private void setAdapter() {
        historyGrid.removeAllViews();
        redGrid.removeAllViews();
        for (int i = 0; i < history.size(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_seletor_text, null);
            TextView textView = (TextView) view.findViewById(R.id.select_text);
            textView.setText(history.get(i).toString());
            historyGrid.addView(view);
        }
        historyGrid.setOnItemClickCallback(new DragFlowLayout.Callback() {
            @Override
            public void onItemClick(int position) {
                if (click != null) click.Callback(history.get(position).toString());
            }
        });
        for (int i = 0; i < hotList.size(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_seletor_text, null);
            TextView textView = (TextView) view.findViewById(R.id.select_text);
            textView.setText(hotList.get(i).toString());
            redGrid.addView(view);
        }
        redGrid.setOnItemClickCallback(new DragFlowLayout.Callback() {
            @Override
            public void onItemClick(int position) {
                if (click != null) click.Callback(hotList.get(position).toString());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> params = new HashMap<>();
        params.put("distributorId", disId);
        if (!MApplication.selectorParentLocation.equals("")){
            params.put("parentLocation",MApplication.selectorParentLocation);
        }
        HttpClient.post(HttpClient.DELETESELETOR, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    getData();
                }
            }
        }, getActivity());
    }

    public interface OnItemClick {

        void Callback(String name);
    }

    OnItemClick click;

    public void setOnClick(OnItemClick click) {
        this.click = click;
    }

}
