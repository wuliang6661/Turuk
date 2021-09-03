package com.haoyigou.hyg.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.ui.LoginActivity;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.adapter.ClassifyAdapter;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.ClassifyShopEntry;
import com.haoyigou.hyg.entity.ShopClassifyEntry;
import com.haoyigou.hyg.ui.VoucherCenterAct;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.base.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分类fragment
 */
public class FenLeiFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.select_edit)
    TextView selectEdit;
    @BindView(R.id.classifytitle)
    ListView classifytitle;
    @BindView(R.id.shop_details)
    GridView shopDetails;

    List<ClassifyShopEntry> shopLists;    //商品列表
    ClassifyAdapter adapter;//分类适配器
    CommonAdapter<ShopClassifyEntry> shopAdapter;
    private static int position = 0;     //记录上次点击的分类，默认第一项

    private String disId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_classify, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        back.setVisibility(View.GONE);
//        titleText.setText("全部商品");
        disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        if (GlobalApplication.user != null) {
            String search = GlobalApplication.user.getSearchdesc();
            if (search != null) {
                selectEdit.setText(search);
            }
        }
        setListener();
        getClassifyMessage();
    }

    /**
     * 设置监听
     */
    private void setListener() {
        classifytitle.setOnItemClickListener(this);
        shopDetails.setOnItemClickListener(this);
        selectEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
//                intent.putExtra("url", HttpClient.SEARCH + "?distributorId=" + disId);
//                startActivity(intent);
                goToActivity(SelectorActivity.class, false);
            }
        });
    }


    /***
     * 请求分类数据
     */
    private void getClassifyMessage() {
        Map<String, Object> params = new HashMap<>();
        HttpClient.get(HttpClient.CLASSIFY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--class", content);
                JSONObject object = JSON.parseObject(content);
                if (object.getString("status").equals(HttpClient.RET_SUCCESS_CODE)) {
                    String result = object.getString("result");
                    shopLists = JSONArray.parseArray(result, ClassifyShopEntry.class);
                    if (shopLists != null && shopLists.size() != 0) {
                        setClassifyAdapter();
                        setShopGridAdapter(shopLists.get(0));   //默认显示第一个分类
                    }
                }
            }
        }, getActivity(), false);
    }


    /***
     * 设置界面显示
     */
    private void setClassifyAdapter() {
        adapter = new ClassifyAdapter(getActivity(), shopLists);
        classifytitle.setAdapter(adapter);
    }


    /**
     * 设置商品显示，默认显示第一个
     */
    private void setShopGridAdapter(ClassifyShopEntry entry) {
        shopAdapter = new CommonAdapter<ShopClassifyEntry>(getActivity(),
                R.layout.item_class_shop, entry.getSecond()) {
            @Override
            protected void convert(ViewHolder viewHolder, ShopClassifyEntry item, int position) {
                if (!StringUtils.isEmpty(item.getImgurl())) {
                    viewHolder.setImageUrl(R.id.shop_img, item.getImgurl());
                } else {
                    viewHolder.setImageResource(R.id.shop_img, R.drawable.default_image);
                }
                viewHolder.setText(R.id.shop_name, item.getName());
            }
        };
        shopDetails.setAdapter(shopAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.classifytitle:
                if (i != position) {
                    position = i;
                    adapter.setPosition(i);
                    setShopGridAdapter(shopLists.get(i));
                }
                break;
            case R.id.shop_details:
                if (position == 0) {
                    if (StateMessage.IS_LOGIN) {
//                        Intent intentVoucher = new Intent(getActivity(), VoucherCenterAct.class);
//                        intentVoucher.putExtra("type", i);
//                        startActivity(intentVoucher);
                        Intent intent = new Intent(getActivity(), SelectorActivity.class);
                        intent.putExtra("Pfflid", shopLists.get(position).getId());
                        intent.putExtra("pfflid", shopLists.get(position).getSecond().get(i).getId());
                        startActivity(intent);
                        return;
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("addFinish", true);
                        startActivity(intent);
                        getActivity().finish();
                        return;
                    }
                }
                Intent intent = new Intent(getActivity(), SelectorActivity.class);
                intent.putExtra("Pfflid", shopLists.get(position).getId());
                intent.putExtra("pfflid", shopLists.get(position).getSecond().get(i).getId());
                startActivity(intent);
                break;
        }
    }
}
