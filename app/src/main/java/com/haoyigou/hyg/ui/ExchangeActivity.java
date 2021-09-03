package com.haoyigou.hyg.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.imageview.ShapeableImageView;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.ExchangeBean;
import com.haoyigou.hyg.entity.SKU;
import com.haoyigou.hyg.entity.SkuBean;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.dragflowlayout.DragFlowLayout;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 5/26/21
 * Describe:换购商品
 */
public class ExchangeActivity extends BaseActivity {


    @BindView(R.id.cartBtn)
    ImageView cartBtn;
    @BindView(R.id.productList)
    ListView productList;

    private CommonAdapter<ExchangeBean> adapter;
    private List<ExchangeBean> data = new ArrayList<>();

    private SkuBean skuBean;
    private List<SKU> skuList = new ArrayList<>();

    private List<String> productIds = new ArrayList<>();
    private String skuOneId = "";//第一个sku的code
    private String selectSkuId = "";//最终的skuId
    private List<String> addProduct = new ArrayList<>();//已经选中过的商品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_layout);
        ButterKnife.bind(this);
        StateMessage.canExchange = false;
        setTitleText("加价购");
        goBack();
        productIds = getIntent().getStringArrayListExtra("productIds");
        addProduct = getIntent().getStringArrayListExtra("addProduct");
        initView();
        startProgressDialog("",this);
        getList();
    }

    private void initView() {
        cartBtn.setVisibility(View.VISIBLE);
        cartBtn.setOnClickListener(v -> finish());
    }


    private void setAdapter() {
        if (adapter != null){
            adapter.notifyDataSetChanged();
            return;
        }
        adapter = new CommonAdapter<ExchangeBean>(this,R.layout.exchange_item_layout,data) {
            @Override
            protected void convert(ViewHolder holder, ExchangeBean item, int position) {
                ShapeableImageView image = holder.getConvertView().findViewById(R.id.image);
                ImageButton addBtn = holder.getConvertView().findViewById(R.id.addBtn);
                Glide.with(ExchangeActivity.this).load(item.getProduct().getPiclogo()).into(image);
                holder.setText(R.id.name,item.getProduct().getName());
                holder.setText(R.id.oldPrice,String.format("好易购价  ￥%s",item.getOriginPrice()));
                holder.setText(R.id.nowPrice,String.format("换购价  ￥%s",item.getPrice()));
                if (addProduct != null
                        && addProduct.size()>0 && addProduct.contains(item.getProduct().getId())){
                    Glide.with(ExchangeActivity.this).load(R.mipmap.swoosh).into(addBtn);
                }else {
                    Glide.with(ExchangeActivity.this).load(R.mipmap.add_exchange).into(addBtn);
                }
                holder.getView(R.id.addBtn).setOnClickListener(v -> {
                    if (addProduct != null
                            && addProduct.size()>0 && addProduct.contains(item.getProduct().getId())){
                        ToastUtils.showToast(ExchangeActivity.this,"您已经添加过该商品了");
                    }else {
                        startProgressDialog("", ExchangeActivity.this);
                        getDetail(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getPiclogo(), item.getPrice());
                    }
                });
            }
        };
        productList.setAdapter(adapter);
    }

    /**
     * sku
     */
    private int selectOnePosition = -1;
    private int selectTwoPosition = -1;
    private List<SKU.ColorBean> skuOne = new ArrayList<>();
    private List<SKU.SizeBean> skuTwo = new ArrayList<>();
    private com.zhy.adapter.recyclerview.CommonAdapter<SKU.ColorBean> adapter1;
    private com.zhy.adapter.recyclerview.CommonAdapter<SKU.SizeBean> adapter2;
    private void showSKU(String id,String name ,String pic,String mprice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.sku_layout, null);
        RecyclerView flowLayoutOne = v.findViewById(R.id.flowLayoutOne);
        RecyclerView flowLayoutTwo = v.findViewById(R.id.flowLayoutTwo);
        TextView price = v.findViewById(R.id.price);
        TextView store = v.findViewById(R.id.store);
        Button submitBtn = v.findViewById(R.id.submitBtn);
        ImageButton close = v.findViewById(R.id.close);
        ImageView productImg = v.findViewById(R.id.productImg);
        TextView productName = v.findViewById(R.id.name);
        price.setText(String.format("￥%s",mprice));
        productName.setText(name);
        Glide.with(ExchangeActivity.this).load(pic).into(productImg);

        if (skuBean.getSkutype().equals("1")){//只有一种sku
            flowLayoutOne.setVisibility(View.VISIBLE);
            flowLayoutTwo.setVisibility(View.GONE);
        }else {//两种都有
            flowLayoutOne.setVisibility(View.VISIBLE);
            flowLayoutTwo.setVisibility(View.VISIBLE);
        }
        skuOneId = "";
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flowLayoutOne.setLayoutManager(layoutManager);
        FlexboxLayoutManager layoutManager2 = new FlexboxLayoutManager(this);
        layoutManager2.setFlexWrap(FlexWrap.WRAP);
        layoutManager2.setFlexDirection(FlexDirection.ROW);
        layoutManager2.setAlignItems(AlignItems.STRETCH);
        layoutManager2.setJustifyContent(JustifyContent.FLEX_START);
        flowLayoutTwo.setLayoutManager(layoutManager2);
        adapter1 = new com.zhy.adapter.recyclerview.CommonAdapter<SKU.ColorBean>(this,R.layout.item_seletor_text,skuOne) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, SKU.ColorBean s, int position) {
                holder.setText(R.id.select_text,s.getName());
                TextView select_text = holder.getConvertView().findViewById(R.id.select_text);
                if (selectOnePosition == position){
                    select_text.setBackground(getResources().getDrawable(R.drawable.selector_edit_yes));
                    select_text.setTextColor(Color.parseColor("#FFFFFF"));
                }else {
                    select_text.setBackground(getResources().getDrawable(R.drawable.selector_edit));
                    select_text.setTextColor(Color.parseColor("#7d7d7d"));
                }
                holder.getView(R.id.select_text).setOnClickListener(v1 -> {
                    selectOnePosition = position;
                    notifyDataSetChanged();
                    skuTwo.clear();
                    skuOneId = s.getCode();
                    for (int i=0;i<skuList.size();i++){
                        if (s.getCode().equals(skuList.get(i).getColor().getCode())){
                            if (skuList.get(i).getSize() != null
                                    && skuList.get(i).getSize().getCode() != null){
                                skuTwo.add(skuList.get(i).getSize());
                            }
                        }
                    }
                    adapter2.notifyDataSetChanged();
                });
            }
        };
        flowLayoutOne.setAdapter(adapter1);

        adapter2 = new com.zhy.adapter.recyclerview.CommonAdapter<SKU.SizeBean>(this,R.layout.item_seletor_text,skuTwo) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, SKU.SizeBean s, int position) {
                holder.setText(R.id.select_text,s.getName());
                TextView select_text = holder.getConvertView().findViewById(R.id.select_text);
                if (selectTwoPosition == position){
                    select_text.setBackground(getResources().getDrawable(R.drawable.selector_edit_yes));
                    select_text.setTextColor(Color.parseColor("#FFFFFF"));
                }else {
                    select_text.setBackground(getResources().getDrawable(R.drawable.selector_edit));
                    select_text.setTextColor(Color.parseColor("#7d7d7d"));
                }
                holder.getView(R.id.select_text).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectTwoPosition = position;
                        notifyDataSetChanged();
                        for (int i=0;i<skuList.size();i++){//两个id都满足，则为当前选中的sku
                            if (skuList.get(i).getColor().getCode().equals(skuOneId)
                                    && skuList.get(i).getSize().getCode().equals(s.getCode())){
                                selectSkuId = skuList.get(i).getId();
                                store.setText(String.format("库存:%s",skuList.get(i).getStore()));
                                break;
                            }
                        }
                    }
                });
            }
        };
        flowLayoutTwo.setAdapter(adapter2);

        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.BOTTOM);
        noticeDialog.getWindow().setWindowAnimations(R.style.anim_menu_bottombar);
        noticeDialog.show();

        submitBtn.setOnClickListener(v12 -> {
            for (int i=0;i<skuList.size();i++){//两个id都满足，则为当前选中的sku
                if (skuList.get(i).getId().equals(selectSkuId)){
                    if (skuList.get(i).getStore().equals("0")){
                        ToastUtils.showToast(this,"库存不足");
                        selectSkuId = "";
                        return;
                    }
                }
            }
            addCart(selectSkuId,id);
            noticeDialog.dismiss();
        });

        close.setOnClickListener(v13 -> noticeDialog.dismiss());

        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        noticeDialog.getWindow().setAttributes(layoutParams);
    }


    /**
     * 加价商品列表
     */
    private void getList() {
        Map<String, Object> params = new HashMap<>();

        HttpClient.post(HttpClient.EXCHANGE_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    data.clear();
                    data.addAll(JSON.parseArray(object.getString("data"),ExchangeBean.class));
                    if (productIds.size()>0){
                        for (int i=0;i<productIds.size();i++){
                            for (int j=0;j<data.size();j++){
                                if (productIds.get(i).equals(data.get(j).getProduct().getId())){
                                    data.remove(j);
                                }
                            }
                        }
                    }
                    setAdapter();
                }
            }
        }, this);
    }


    /**
     * 加价商品详情
     */
    private void getDetail(String id,String name,String pic,String price) {
        Map<String, Object> params = new HashMap<>();
        params.put("productid",id);
        HttpClient.post(HttpClient.EXCHANGE_DETAIL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                skuBean = JSON.parseObject(content,SkuBean.class);
                if (skuBean !=null && skuBean.getSkutype() != null && !skuBean.getSkutype().equals("0")) {//显示SKU
                    skuList.clear();
                    skuOne.clear();
                    skuTwo.clear();
                    if (skuBean.getSkutype().equals("1")){
                        skuOne.addAll(JSON.parseArray(skuBean.getColor(),SKU.ColorBean.class));
                    }else {
                        skuOne.addAll(JSON.parseArray(skuBean.getColor(),SKU.ColorBean.class));
                        skuTwo.addAll(JSON.parseArray(skuBean.getSize(),SKU.SizeBean.class));
                        skuList.addAll(JSON.parseArray(skuBean.getAttrsjson(),SKU.class));
                    }
                    showSKU(id,name,pic,price);
                }else {//直接加入购物车
                    addCart(skuBean.getAttrid(),id);
                }

            }
        }, this);
    }


    /**
     * 加入购物车
     */
    private void addCart(String attrid,String productid) {
        Map<String, Object> params = new HashMap<>();
        params.put("pparam",attrid+"_"+productid+"_"+"1"+"_"+SharedPreferencesUtils.getInstance().getString("distributorId", null)+"_"+"1");
        params.put("type",1);
        HttpClient.post(HttpClient.ADD_CART, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                JSONObject jsonObject = JSON.parseObject(content);
                if (jsonObject.getString("status").equals("1")){
                    ToastUtils.showToast(ExchangeActivity.this,"加入成功");
                    StateMessage.canExchange = true;
                    finish();
                }else {
                    StateMessage.canExchange = false;
                    ToastUtils.showToast(ExchangeActivity.this,"加入失败，请稍后重新尝试");
                }
            }
        }, this);
    }
}
