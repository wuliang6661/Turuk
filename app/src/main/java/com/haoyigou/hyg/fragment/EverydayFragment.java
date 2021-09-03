package com.haoyigou.hyg.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.DailyBean;
import com.haoyigou.hyg.ui.EverydayDetail;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.HeaderGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2020-03-23
 * Describe: 每日一品
 */
public class EverydayFragment extends BaseFragment {


    @BindView(R.id.grid_view)
    HeaderGridView gridView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.noActivityImage)
    ImageView noActivityImage;


    /**
     * 头布局
     */
    private RelativeLayout rlTop;
    private TextView month;//月
    private TextView day;//日
    private ImageView productImg;//商品图片
    private TextView productName;//商品名字
    private TextView hygPrice;//好易购价
    private TextView selectPrice;//优选价格
    private TextView sellNum;//月售件数
    private LinearLayout headBtn;
    private RelativeLayout listTitle;


    CommonAdapter<DailyBean> adapter;
    private List<DailyBean> data = new ArrayList<>();
    private List<DailyBean> removeData = new ArrayList<>();
    private DailyBean topBean;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_home2, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void initView() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.everyday_head_layout, null);
        rlTop = headerView.findViewById(R.id.rlTop);
        month = headerView.findViewById(R.id.month);
        day = headerView.findViewById(R.id.day);
        productImg = headerView.findViewById(R.id.productImg);
        productName = headerView.findViewById(R.id.productName);
        hygPrice = headerView.findViewById(R.id.hygPrice);
        selectPrice = headerView.findViewById(R.id.selectPrice);
        sellNum = headerView.findViewById(R.id.sellNum);
        headBtn = headerView.findViewById(R.id.headBtn);
        listTitle = headerView.findViewById(R.id.listTitle);
        ViewGroup.LayoutParams params = rlTop.getLayoutParams();
        params.height = (int) (DisplayUtils.getScreenWidth(getActivity()) * 0.4);
        rlTop.setLayoutParams(params);
        gridView.addHeaderView(headerView);
        refreshLayout.setRefreshHeader(new SmartHeader(getActivity()));
        refreshLayout.setHeaderHeight(60);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            removeData.clear();
            getData();
            refreshLayout.finishRefresh(2000);
        });
        headBtn.setOnClickListener(v -> {
            if (topBean != null) {
                Intent intent = new Intent(getActivity(), EverydayDetail.class);
                intent.putExtra("data", topBean);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void setAdapter() {
        adapter = new CommonAdapter<DailyBean>(getActivity(), R.layout.everyday_item_layout, removeData) {
            @Override
            protected void convert(ViewHolder viewHolder, DailyBean item, int position) {
                TextView oldPrice = viewHolder.getConvertView().findViewById(R.id.oldPrice);
                oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                Glide.with(getActivity()).load(item.getPicture()).asBitmap().into((ImageView) viewHolder.getView(R.id.image));
                viewHolder.setText(R.id.name, item.getTitle());
                viewHolder.setText(R.id.sell, item.getMemo());
                viewHolder.setText(R.id.nowPrice, item.getDisprice());
                oldPrice.setText(String.format("￥ %s", item.getPrice()));
                viewHolder.getView(R.id.btnDetail).setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), EverydayDetail.class);
                    intent.putExtra("data", item);
                    startActivity(intent);
                });
            }
        };
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    /**
     * 获取每日一品数据
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.DAILY_NEW, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                refreshLayout.finishRefresh();
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    data = JSONArray.parseArray(result.getData(), DailyBean.class);
                    if (data.size() > 0) {
                        gridView.setVisibility(View.VISIBLE);
                        noActivityImage.setVisibility(View.GONE);
                        topBean = data.get(0);
                        month.setText(String.format("%s月", data.get(0).getMonth()));
                        day.setText(String.format("%s", data.get(0).getDate()));
                        Glide.with(getActivity()).load(data.get(0).getPicture()).asBitmap().into(productImg);
                        productName.setText(data.get(0).getTitle());
                        sellNum.setText(data.get(0).getMemo());
                        hygPrice.setText(String.format("￥%s", data.get(0).getPrice()));
                        selectPrice.setText(data.get(0).getDisprice());
                        if (data.size() > 1) {
                            data.remove(0);
                            removeData = data;
                        }
                        if (removeData.size() > 0) {
                            listTitle.setVisibility(View.VISIBLE);
                        } else {
                            listTitle.setVisibility(View.GONE);
                        }
                        setAdapter();
                    }else {
                        gridView.setVisibility(View.GONE);
                        noActivityImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    gridView.setVisibility(View.GONE);
                    noActivityImage.setVisibility(View.VISIBLE);
                    showToast(result.getMessage());
                }
            }
        }, getActivity());
    }


}
