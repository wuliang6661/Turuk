package com.haoyigou.hyg.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.ShopCarAdapter;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.ExchangeCondition;
import com.haoyigou.hyg.entity.ShopCarBean;
import com.haoyigou.hyg.ui.ExchangeActivity;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.WebviewActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.ProgressUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.textview.DrawableCenterTextView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物车fragment
 */

public class ShoppingCartFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.shopcar_list)
    ListView shopcarList;
    @BindView(R.id.all_radio)
    CheckBox allRadio;
    @BindView(R.id.summary_button)
    Button summaryButton;
    @BindView(R.id.all_price)
    TextView allPrice;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.all_text)
    TextView allText;
    @BindView(R.id.all_layout)
    LinearLayout allLayout;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.not_layout)
    LinearLayout notLayout;
    @BindView(R.id.tvActivityOne)
    DrawableCenterTextView tvActivityOne;
    @BindView(R.id.tvActivityTwo)
    DrawableCenterTextView tvActivityTwo;
    @BindView(R.id.tvActivityThree)
    DrawableCenterTextView tvActivityThree;
    @BindView(R.id.ll_activity)
    LinearLayout ll_activity;
    @BindView(R.id.rlFreeFee)
    RelativeLayout rlFreeFee;
    @BindView(R.id.txtDescribe)
    TextView txtDescribe;
    @BindView(R.id.rlGoTo)
    RelativeLayout rlGoTo;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshRoot;
    @BindView(R.id.header)
    RelativeLayout header;
    @BindView(R.id.factoryOneSelect)
    RelativeLayout factoryOneSelect;
    @BindView(R.id.factoryOneHave)
    TextView factoryOneHave;
    @BindView(R.id.factoryOneNotSelect)
    RelativeLayout factoryOneNotSelect;
    @BindView(R.id.factoryTwoSelect)
    RelativeLayout factoryTwoSelect;
    @BindView(R.id.factoryTwoHave)
    TextView factoryTwoHave;
    @BindView(R.id.factoryTwoNotSelect)
    RelativeLayout factoryTwoNotSelect;
    @BindView(R.id.viewFactoryTwo)
    View viewFactoryTwo;
    @BindView(R.id.rlExchange)
    RelativeLayout rlExchange;//换购栏
    @BindView(R.id.exchangeDescribe)
    TextView exchangeDescribe;//换购描述
    @BindView(R.id.rlGoToExchange)
    RelativeLayout rlGoToExchange;//去换购按钮

    private List<ShopCarBean> shopList;    //商品列表
    private Map<String, ShopCarBean> checkList;    //选中的商品列表
    private Map<String, Integer> shopnums;   //各商品的实际数量
    private ShopCarAdapter adapter;   //适配器

    private boolean isManual = false;   //全选是否是手动代码触发

    private boolean isEdit = false;     //是否编辑模式
    private Integer maxnum;   //最大购买量
    private List<String> activity;//活动
    private String cdurl;//凑单路径
    private double minMoney;//包邮最低价格

    private int nowFactorySelect = 0;//仓库1： 0 ； 仓库2： 1  ,仓库1默认选中

    private String rightPoint = ""; //仓库2是否有数据

    private View footView;
    private RecyclerView exchangeProductList;//加价购商品列表
    private CommonAdapter<ShopCarBean> exchangeProductAdapter;
    private ArrayList<ShopCarBean> exchangeProductData = new ArrayList<>();//加价购商品数据
    private ExchangeCondition exchangeCondition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_shopping_cart, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setFlgsbg(R.color.white);
//        setStatusBar();
        allRadio.setOnCheckedChangeListener(this);
        summaryButton.setOnClickListener(this);
        edit.setOnClickListener(this);
        rlGoTo.setOnClickListener(this);
        checkList = new HashMap<>();
        allLayout.setOnClickListener(this);
//        refreshRoot.setNestedScrollingEnabled(false);
//        refreshRoot.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
//                getShopCarData(nowFactorySelect);
//            }
//        });
//        refreshRoot.setRefreshHeader(new SmartHeader(getActivity()));
//        refreshRoot.setHeaderHeight(60);
        header.setBackgroundDrawable(getResources().getDrawable(R.drawable.change_color));

        ViewGroup.LayoutParams params = factoryOneSelect.getLayoutParams();
        params.width = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.2);
        params.height = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.08);
        factoryOneSelect.setLayoutParams(params);

        ViewGroup.LayoutParams params2 = factoryOneNotSelect.getLayoutParams();
        params2.width = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.2);
        params2.height = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.07);
        factoryOneNotSelect.setLayoutParams(params2);

        ViewGroup.LayoutParams params3 = factoryTwoSelect.getLayoutParams();
        params3.width = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.2);
        params3.height = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.08);
        factoryTwoSelect.setLayoutParams(params3);

        ViewGroup.LayoutParams params4 = factoryTwoNotSelect.getLayoutParams();
        params4.width = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.2);
        params4.height = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.07);
        factoryTwoNotSelect.setLayoutParams(params4);

        //加价购商品
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.exchange_foot_layout, null);
        exchangeProductList = footView.findViewById(R.id.exchangeProductList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        exchangeProductList.setLayoutManager(manager);
        shopcarList.addFooterView(footView);

    }

    /**
     * 加价购商品
     */
    private void setExchangeProductAdapter() {
        if (exchangeProductAdapter != null){
            exchangeProductAdapter.notifyDataSetChanged();
            return;
        }
        exchangeProductAdapter = new CommonAdapter<ShopCarBean>(getActivity(),R.layout.item_shopcar_layout,exchangeProductData) {
            @Override
            protected void convert(ViewHolder holder, ShopCarBean shopCarBean, int position) {
                ImageView item_shop_img= holder.getConvertView().findViewById(R.id.item_shop_img);
                holder.getView(R.id.radio_layout).setVisibility(View.INVISIBLE);
                holder.setText(R.id.shop_num,"1");
                holder.getView(R.id.shop_cut).setVisibility(View.INVISIBLE);
                holder.getView(R.id.shop_add).setVisibility(View.INVISIBLE);
                Glide.with(getActivity()).load(shopCarBean.getPiclogo()).into(item_shop_img);
                holder.setText(R.id.item_shop_name,shopCarBean.getProductname());
                if (StringUtils.isEmpty(shopCarBean.getColorname()) || shopCarBean.getColorname().equals("共同")) {
                    holder.getView(R.id.item_shop_content).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.item_shop_content).setVisibility(View.VISIBLE);
                    holder.setText(R.id.item_shop_content,shopCarBean.getColorname());
                }
                if (StringUtils.isEmpty(shopCarBean.getSizename())|| shopCarBean.getSizename().equals("共同")) {
                    holder.getView(R.id.item_shop_content01).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.item_shop_content01).setVisibility(View.VISIBLE);
                    holder.setText(R.id.item_shop_content01,shopCarBean.getSizename());
                }
                holder.setText(R.id.shop_price,"¥" + shopCarBean.getAddprice());

                holder.getView(R.id.btnDelete).setVisibility(View.GONE);
            }
        };
        exchangeProductList.setAdapter(exchangeProductAdapter);
    }

    /**
     * 判断是否显示返回键
     */
    private void isFinish() {
        if (StateMessage.isShopCarFinnish) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("all", true);
                    bundle.putString("url", StateMessage.url);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            back.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏返回按键
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShopCarBean bean) {   //是否进入首页
        isFinish();
    }

    @Override
    public void onRefresh() {
        getShopCarData(nowFactorySelect);
    }

    @Override
    public void onResume() {
        super.onResume();
        getShopCarData(nowFactorySelect);
        isFinish();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getShopCarData(nowFactorySelect);
        }
    }

    /***
     * 获取购物车数据并显示
     */
    private void getShopCarData(int store) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentLocation", "113");
        params.put("store",store);
        HttpClient.post(HttpClient.SHOPCARDATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (refreshRoot != null) {
                    refreshRoot.finishRefresh();
                }
//                Log.e("shopcar", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    minMoney = object.getDouble("minmoney");//包邮的最低价格
                    rightPoint = object.getString("redPoint");//仓库2是否有数据
                    if (rightPoint != null && rightPoint.equals("1")){
                        viewFactoryTwo.setVisibility(View.VISIBLE);
                    }else {
                        viewFactoryTwo.setVisibility(View.GONE);
                    }
                    cdurl = object.getString("cdurl");
                    String arrays = object.getString("items");
                    shopList = JSONArray.parseArray(arrays, ShopCarBean.class);
                    for (int i = 0; i < shopList.size(); i++) {
                        if (!shopList.get(i).isFreesend()) {//包邮
                            if (minMoney > 0) {
                                rlFreeFee.setVisibility(View.VISIBLE);
                                txtDescribe.setText(String.format("满%s元可免运费", minMoney));
                                rlGoTo.setVisibility(View.VISIBLE);
                            } else {
                                rlFreeFee.setVisibility(View.GONE);
                                txtDescribe.setText(String.format("%s", "已满足免邮条件"));
                                rlGoTo.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }
                    if (shopList.size() == 0) {
                        notLayout.setVisibility(View.VISIBLE);
                        shopcarList.setVisibility(View.GONE);
                        rlFreeFee.setVisibility(View.GONE);
                    } else {
                        rlFreeFee.setVisibility(View.VISIBLE);
                        notLayout.setVisibility(View.GONE);
                        shopcarList.setVisibility(View.VISIBLE);
                    }
                    exchangeProductData.clear();
                    for (int i=0;i<shopList.size();i++){
                        if (shopList.get(i).getIsadd() != null &&
                                shopList.get(i).getIsadd().equals("1")){
                            if (checkList.size()>0) {
                                exchangeProductData.add(shopList.get(i));
                            }else {
                                StateMessage.canExchange = false;
                                footView.setVisibility(View.GONE);
                                rlExchange.setVisibility(View.GONE);
                            }
                            shopList.remove(i);
                            i--;
                        }
                    }
                    setAdapter();
                    if (StateMessage.canExchange) {
                        if (exchangeProductData.size() > 0) {
                            footView.setVisibility(View.VISIBLE);
                            setExchangeProductAdapter();//设置加价购商品
                        } else {
                            footView.setVisibility(View.GONE);
                        }
                    }else {
                        footView.setVisibility(View.GONE);
                    }

                    JSONArray jsonArray = object.getJSONArray("titles");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        ll_activity.setVisibility(View.VISIBLE);
                        if (jsonArray.get(0) != null && !jsonArray.get(0).equals("")) {
                            tvActivityOne.setText(jsonArray.get(0).toString());
                        } else {
                            tvActivityOne.setVisibility(View.INVISIBLE);
                        }

                        if (jsonArray.get(1) != null && !jsonArray.get(1).equals("")) {
                            tvActivityTwo.setText(jsonArray.get(1).toString());
                        } else {
                            tvActivityTwo.setVisibility(View.INVISIBLE);
                        }

                        if (jsonArray.get(2) != null && !jsonArray.get(2).equals("")) {
                            tvActivityThree.setText(jsonArray.get(2).toString());
                        } else {
                            tvActivityThree.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        ll_activity.setVisibility(View.GONE);
                    }
                    balancePrice();
                }
            }
        }, getActivity());
    }

    /**
     * 设置购物车适配器
     */
    private void setAdapter() {
        if (adapter != null) {
            adapter.setShopList(shopList);
            return;
        }
        adapter = new ShopCarAdapter(getActivity(), shopList);
        adapter.setOnClickItem(new ShopCarAdapter.onClickItemsAll() {

            @Override
            public void onChangeNumClick(Map<String, Integer> newShop) {
                shopnums = newShop;
                checkCondition();
                balancePrice();
            }

            @Override
            public void onCheckClick(Map<String, ShopCarBean> select, Map<String, Integer> nums) {
                checkList = select;
                shopnums = nums;
                checkCondition();
                balancePrice();

            }

            @Override
            public void onDeleteClick(List<ShopCarBean> allShop, Map<String, ShopCarBean> newShop, Map<String, Integer> newNums) {
                shopList = allShop;
                checkList = newShop;
                shopnums = newNums;
                if (shopList.size() == 0) {
                    allRadio.setChecked(false);
                }
                checkCondition();
                balancePrice();
            }
        });
        shopcarList.setAdapter(adapter);
    }

    /**
     * 判断是否满足加价购的条件
     */
    private void checkCondition(){
        //加价购
        if (checkList.size() == 0) {
            footView.setVisibility(View.GONE);
            delAdd();
            return;
        }
        String[] items = new String[checkList.size()];
        int i = 0;
        ShopCarBean bean;
        for (String key : checkList.keySet()) {
            bean = checkList.get(key);
            items[i] = bean.getId();
            if (bean.getMaxnum() == 0) {
                maxnum = bean.getStore();
            } else {
                maxnum = bean.getMaxnum() <= bean.getStore() ? bean.getMaxnum() : bean.getStore();
            }
            if (!isEdit) {
                if (maxnum < shopnums.get(bean.getId())) {
                    showToast(bean.getProductname() + "库存剩余" + maxnum + "件，购买超出数量！");
                    return;
                }
            }
            i++;
        }
        getExchangeCondition(items);
    }



    /**
     * 数据出现改变时，此处进行结算
     * <p>
     * newShop:改变之后的集合
     */
    private void balancePrice() {
        isManual = true;
        if (shopList.size() != 0) {
            notLayout.setVisibility(View.GONE);
            shopcarList.setVisibility(View.VISIBLE);
            int quantity = 0;
            for (int i = 0; i < shopList.size(); i++) {
                if (shopList.get(i).getQuantity() == 0) {
                    quantity++;
                }
            }
            if (shopList.size() - quantity == checkList.size()) {   //全选了
                allRadio.setChecked(true);
            } else {
                allRadio.setChecked(false);
            }
        } else {
            notLayout.setVisibility(View.VISIBLE);
            shopcarList.setVisibility(View.GONE);
            rlExchange.setVisibility(View.GONE);
            footView.setVisibility(View.GONE);
        }
        isManual = false;
        if (checkList.size() == 0) {
            allPrice.setText("¥ 0.00");
            footView.setVisibility(View.GONE);
            rlExchange.setVisibility(View.GONE);
        }
        double Price = 0;
        if (StateMessage.canExchange && exchangeProductData.size()>0){//加价购商品
            for (String key : checkList.keySet()) {
                if (shopnums.get(checkList.get(key).getId()) != null) {
                    Price += checkList.get(key).getRightprice() * shopnums.get(checkList.get(key).getId());
                }
            }
            for (int i=0;i<exchangeProductData.size();i++){
                Price += Double.parseDouble(exchangeProductData.get(i).getAddprice());
            }
            allPrice.setText("¥ " + String.format("%.2f", Price));
        }else {//没有加价购，只有普通商品
            for (String key : checkList.keySet()) {
                if (shopnums.get(checkList.get(key).getId()) != null) {
                    Price += checkList.get(key).getRightprice() * shopnums.get(checkList.get(key).getId());
                    allPrice.setText("¥ " + String.format("%.2f", Price));
                }
            }
        }
        for (String key : checkList.keySet()) {
            if (checkList.get(key).isFreesend()) {
                rlFreeFee.setVisibility(View.VISIBLE);
                txtDescribe.setText(String.format("%s", "已满足免邮条件"));
                rlGoTo.setVisibility(View.GONE);
                return;
            }
        }
        if (Price > minMoney) {//总价大于包邮最低价
            rlFreeFee.setVisibility(View.VISIBLE);
            txtDescribe.setText(String.format("%s", "已满足免邮条件"));
            rlGoTo.setVisibility(View.GONE);
        } else {
            rlFreeFee.setVisibility(View.VISIBLE);
            rlGoTo.setVisibility(View.VISIBLE);
            if (minMoney - Price > 0) {
                txtDescribe.setText(String.format("再购%s元可免运费", minMoney - Price));
            } else {
                rlFreeFee.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (adapter != null && !isManual) {
            adapter.setAllChecked(b);
        }
        compoundButton.setChecked(b);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:   //编辑按钮
                if (!isEdit) {
                    edit.setText("完成");
                    allText.setVisibility(View.GONE);
                    allPrice.setVisibility(View.GONE);
                    summaryButton.setText("删除");
                    allRadio.setChecked(false);
                    isEdit = true;
                } else {
                    edit.setText("编辑");
                    allText.setVisibility(View.VISIBLE);
                    allPrice.setVisibility(View.VISIBLE);
                    summaryButton.setText("去结算");
                    allRadio.setChecked(false);
                    isEdit = false;
                }
                if (adapter != null) {
                    adapter.setAllChecked(false);   //手动取消所有选项
                }
                break;
            case R.id.summary_button:   //结算和删除按钮
                if (checkList.size() == 0) {
                    ToastUtils.showToast(getActivity(),"请先选择商品");
                    return;
                }
                String[] items = new String[checkList.size()];
                int i = 0;
                ShopCarBean bean;
                for (String key : checkList.keySet()) {
                    bean = checkList.get(key);
                    items[i] = bean.getId();
                    if (bean.getMaxnum() == 0) {
                        maxnum = bean.getStore();
                    } else {
                        maxnum = bean.getMaxnum() <= bean.getStore() ? bean.getMaxnum() : bean.getStore();
                    }
                    if (!isEdit) {
                        if (maxnum < shopnums.get(bean.getId())) {
                            showToast(bean.getProductname() + "库存剩余" + maxnum + "件，购买超出数量！");
                            return;
                        }
                    }
                    i++;
                }
                if (isEdit && adapter != null) {   //删除
                    adapter.deleteItems(items);
                } else if (!isEdit && adapter != null) {   //提交
                    submitAllShop(items);
                }
                break;
            case R.id.all_layout:  //扩大checkbox的点击热区
                if (allRadio.isChecked()) {
                    allRadio.setChecked(false);
                } else {
                    allRadio.setChecked(true);
                }
                break;
            case R.id.rlGoTo://去凑单
                Intent intent = new Intent(getActivity(), WebviewActivity.class);
                intent.putExtra("url", cdurl);
                intent.putExtra("all", true);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 提交购买
     */
    private void submitAllShop(String[] items) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder itemIds = new StringBuilder();
        StringBuilder itemNums = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i == items.length - 1) {   //最后一个
                itemIds.append(items[i]);
                itemNums.append(shopnums.get(items[i]));
            } else {
                itemIds.append(items[i] + ",");
                itemNums.append(shopnums.get(items[i]) + ",");
            }
        }
        if (exchangeProductData.size()>0){//有加价购商品
            for (int i=0;i<exchangeProductData.size();i++){
                if (itemIds.length() > 0) {
                    itemIds.append(",");
                }
                if (itemNums.length() > 0) {
                    itemNums.append(",");
                }
                itemIds.append(exchangeProductData.get(i).getId());
                itemNums.append("1");
            }
        }
        params.put("itemids", itemIds.toString());
        params.put("quantities", itemNums.toString());
        HttpClient.post(HttpClient.SUBMITSHOP, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--submit", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    allRadio.setChecked(false);
                    allPrice.setText("¥ 0.00");
                    String data = object.getString("data");
                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                    intent.putExtra("url", HttpClient.SUBMITURL + "&itemdata=" + data);
                    startActivity(intent);
                }
            }
        }, getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.factoryOneSelect, R.id.factoryOneNotSelect, R.id.factoryTwoSelect, R.id.factoryTwoNotSelect,R.id.rlExchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.factoryOneSelect:
                factoryOneSelect.setVisibility(View.VISIBLE);
                factoryOneNotSelect.setVisibility(View.GONE);
                factoryTwoNotSelect.setVisibility(View.VISIBLE);
                factoryTwoSelect.setVisibility(View.GONE);
                nowFactorySelect = 0;
                startProgressDialog("",getActivity());
                getShopCarData(nowFactorySelect);
                break;
            case R.id.factoryOneNotSelect:
                factoryOneSelect.setVisibility(View.VISIBLE);
                factoryOneNotSelect.setVisibility(View.GONE);
                factoryTwoNotSelect.setVisibility(View.VISIBLE);
                factoryTwoSelect.setVisibility(View.GONE);
                nowFactorySelect = 0;
                startProgressDialog("",getActivity());
                getShopCarData(nowFactorySelect);
                break;
            case R.id.factoryTwoSelect:
                factoryOneSelect.setVisibility(View.GONE);
                factoryOneNotSelect.setVisibility(View.VISIBLE);
                factoryTwoNotSelect.setVisibility(View.GONE);
                factoryTwoSelect.setVisibility(View.VISIBLE);
                nowFactorySelect = 1;
                startProgressDialog("",getActivity());
                getShopCarData(nowFactorySelect);
                break;
            case R.id.factoryTwoNotSelect:
                factoryOneSelect.setVisibility(View.GONE);
                factoryOneNotSelect.setVisibility(View.VISIBLE);
                factoryTwoNotSelect.setVisibility(View.GONE);
                factoryTwoSelect.setVisibility(View.VISIBLE);
                nowFactorySelect = 1;
                startProgressDialog("",getActivity());
                getShopCarData(nowFactorySelect);
                break;
            case R.id.rlExchange://去换购
                if (exchangeCondition != null && exchangeCondition.getType() != null){//去凑单
                    Intent intent3 = new Intent(getActivity(), LabelActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("productTabId", "" + exchangeCondition.getIds());
                    intent3.putExtras(bundle);
                    startActivity(intent3);
                }else {
                    Intent intent = new Intent(getActivity(), ExchangeActivity.class);
                    //加价购
                    if (checkList.size() == 0) {
                        return;
                    }
                    ArrayList<String> idList = new ArrayList<>();
                    ShopCarBean bean;
                    for (String key : checkList.keySet()) {
                        bean = checkList.get(key);
                        idList.add(bean.getProductid());
                    }
                    intent.putStringArrayListExtra("productIds", idList);
                    if (StateMessage.canExchange && exchangeProductData.size() > 0) {
                        ArrayList<String> addProduct = new ArrayList<>();
                        for (int i = 0; i < exchangeProductData.size(); i++) {
                            addProduct.add(exchangeProductData.get(i).getProductid());
                        }
                        intent.putStringArrayListExtra("addProduct", addProduct);
                    }
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 加价购条件判断
     */
    private void getExchangeCondition(String[] items) {
        Map<String, Object> params = new HashMap<>();
        String[] items2 = new String[checkList.size()];
        int j=0;
        ShopCarBean bean;
        for (String key : checkList.keySet()) {
            bean = checkList.get(key);
            if (shopnums.get(bean.getId()) != null) {
                items2[j] = bean.getProductid();
            }
            j++;
        }
        StringBuilder itemIds = new StringBuilder();
        StringBuilder itemNums = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i == items.length - 1) {
                itemNums.append(shopnums.get(items[i]));
            } else {
                itemNums.append(shopnums.get(items[i]) + ",");
            }
        }
        for (int i = 0; i < items2.length; i++) {
            if (i == items2.length - 1) {
                itemIds.append(items2[i]);
            } else {
                itemIds.append(items2[i] + ",");
            }
        }
        params.put("productids", itemIds.toString());
        params.put("productNums", itemNums.toString());
        params.put("disrange", GlobalApplication.user.getLevel_type());
        HttpClient.post(HttpClient.EXCHANGE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    JSONObject jsonObject = JSON.parseObject(object.getString("data"));
                    exchangeCondition = JSON.parseObject(object.getString("data"),ExchangeCondition.class);
                    if (jsonObject.getString("status").equals("1")){//满足条件
                        rlExchange.setVisibility(View.VISIBLE);
                        exchangeDescribe.setText(jsonObject.getString("descript"));
                        rlGoToExchange.setVisibility(View.VISIBLE);
                        StateMessage.canExchange = true;
                    }else {
                        rlExchange.setVisibility(View.VISIBLE);
                        exchangeDescribe.setText(jsonObject.getString("descript"));
                        rlGoToExchange.setVisibility(View.VISIBLE);
                        footView.setVisibility(View.GONE);
                        StateMessage.canExchange = false;
                        delAdd();
                        balancePrice();
                    }
                }else {
                    rlExchange.setVisibility(View.GONE);
                    footView.setVisibility(View.GONE);
                    rlGoToExchange.setVisibility(View.GONE);
                    StateMessage.canExchange = false;
                    delAdd();
                    balancePrice();
                }
            }
        }, getActivity());
    }


    /**
     * 删除加价购
     */
    private void delAdd(){
        if (exchangeProductData.size()>0){
            String[] items = new String[exchangeProductData.size()];
            for (int i=0;i<exchangeProductData.size();i++){
                items[i] = exchangeProductData.get(i).getId();
            }
            exchangeProductData.clear();
            deleteItems(items);
        }
    }

    /**
     * 删除商品
     */
    public void deleteItems(final String[] items) {
        Map<String, Object> params = new HashMap<>();
        String itemIds = "";
        for (int i = 0; i < items.length; i++) {
            if (i == items.length - 1) {   //最后一个
                itemIds += items[i];
            } else {
                itemIds += items[i] + ",";
            }
        }
        params.put("Itemids", itemIds);
        HttpClient.post(HttpClient.DELCARTS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Log.e("log--del", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    exchangeProductData.clear();
                }
            }
        }, getActivity());
    }

}