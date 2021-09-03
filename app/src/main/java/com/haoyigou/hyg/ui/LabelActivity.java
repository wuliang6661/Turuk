package com.haoyigou.hyg.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.CouponAdapter;
import com.haoyigou.hyg.adapter.LabelAdapter;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.DiscountEntity;
import com.haoyigou.hyg.entity.ShopEntry;
import com.haoyigou.hyg.entity.TabBean;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.LogUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.widget.SharePopupWindow;
import com.haoyigou.hyg.view.widget.TopAndButtomListView;
import com.haoyigou.hyg.base.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/2/13.
 * <p>
 * 商品标签页
 */

public class LabelActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.shop_list)
    TopAndButtomListView shopList;

    ImageView imageIntroduce;
    TextView textTitle;
    RecyclerView couponLayout;
    View topView;
    View buttomView;

    private List<ShopEntry> list;//标签页集合
    private List<ShopEntry> lists;
    private List<DiscountEntity> disList;   //优惠卷集合
    private TabBean bean;

    private String productTabId;   //每周上新 -1，新人专享 -2，其他标签传标签id
    /**
     * 以下两参数从首页弹窗进入才传的参数
     */
    private String tabTitle;
    private String fromTabAc;  //首页进入则为-1，否则不传

    private String sharetitle;   //分享标题
    private String imgurl;     //分享图片
    private String tabdesc;     //分享内容
    private String shareurl;   //分享链接

    /**
     * 以下两参数从消息列表跳入才传的参数
     */
    private String frommes = "1";
    private String pushMesId;
    private boolean isScooll = false;   //表示列表是否正在滑动,默认为否
    private LabelAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_label_layout);
        ButterKnife.bind(this);
        goBack();
        setTitleText("");
        setRightImage(R.drawable.first_share, this);
        invitionHelder();
        getIntentData();
        setListener();
        startProgressDialog("",this);
        getData(false);
    }

    /**
     * 初始化顶部布局
     */
    private void invitionHelder() {
        View view = LayoutInflater.from(this).inflate(R.layout.act_label_herdle, null);
        imageIntroduce = (ImageView) view.findViewById(R.id.image_introduce);
        textTitle = (TextView) view.findViewById(R.id.text_title);
        couponLayout = (RecyclerView) view.findViewById(R.id.coupon_layout);
        topView = view.findViewById(R.id.top_view);
        buttomView = view.findViewById(R.id.buttom_view);
        shopList.addHeaderView(view);
    }


    /**
     * 接收传入的参数值
     */
    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        productTabId = bundle.getString("productTabId", "");
        tabTitle = bundle.getString("tabTitle", "");
        fromTabAc = bundle.getString("fromTabAc", "");
        pushMesId = bundle.getString("pushMesId", "");
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        couponLayout.setLayoutManager(manager);
    }

    /**
     * 获取商品数据
     */
    int page = 1;
    private void getData(final boolean isPageAdd) {
        if (isPageAdd) {
            page++;
        } else {
            page = 1;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("distributorId", disId);
        if (!StringUtils.isEmpty(fromTabAc)) {    //是首页则传1
            params.put("fromTabAc", "1");
        }
        if (!StringUtils.isEmpty(tabTitle)) {
            params.put("tabTitle", tabTitle);
        }
        if (!StringUtils.isEmpty(pushMesId)) {     //消息列表进入
            params.put("pushMesId", pushMesId);
            params.put("frommes", frommes);
        }
        params.put("productTabId", productTabId);
        params.put("currPageNo", page);
        if (!MApplication.labelParentLocation.equals("")){
            params.put("parentLocation", MApplication.labelParentLocation);
        }
        HttpClient.post(HttpClient.LABELDATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
//                LogUtils.e("log--", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {   //请求成功
                    bean = JSONObject.parseObject(object.getString("tab"), TabBean.class);
                    imgurl = object.getString("imgurl");
                    String array = object.getString("products");
                    String comArray = object.getString("discounts");  // 优惠卷集合
                    list = JSONArray.parseArray(array, ShopEntry.class);

                    disList = JSONArray.parseArray(comArray, DiscountEntity.class);
                    if (list == null || list.size() == 0) {
//                        showToast("已经到底了！");
//                        page--;
                        return;
                    }
                    if (isPageAdd) {
                        lists.addAll(list);
                    } else {
                        lists = list;
                    }
                    if(page==1){
                        setAdapter(page);
                        setCouponAdapter();
                        setTabMessage(bean);
                    }else {
                        setAdapter(page);
                    }


                }
            }
        }, this);
    }


    /**
     * 设置监听
     */
    private void setListener() {
        shopList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        getData(true);
                    }

                } else {

                }
            }

            @Override
            public void onScroll(AbsListView view, int i, int visibleItemCount, int totalItemCount) {

            }
        });
    }
    /**
     * 设置标签图片和内容
     */
    private void setTabMessage(TabBean bean) {
        if (!StringUtils.isEmpty(bean.getTabpic())) {
            imageIntroduce.setVisibility(View.VISIBLE);
            Glide.with(this).load(bean.getTabpic()).into(imageIntroduce);
            ViewGroup.LayoutParams params = imageIntroduce.getLayoutParams();
            params.height = (int) (DisplayUtil.getMobileWidth(this) / 2.58);
            imageIntroduce.setLayoutParams(params);
        } else {
            imageIntroduce.setVisibility(View.GONE);
        }

        if(bean.getTabdesc().equals("")){
            textTitle.setVisibility(View.GONE);
        }else {
            textTitle.setVisibility(View.VISIBLE);
            textTitle.setText(bean.getTabdesc());
        }

        setTitleText(bean.getSupertitle());
    }


    /**
     * 设置商品的适配器
     */
    private void setAdapter(final int a) {
        if(a==1){
             adapter1 = new LabelAdapter(LabelActivity.this, productTabId, lists);
            shopList.setAdapter(adapter1);
        }else {
            adapter1.notifyDataSetChanged();
        }

    }

    /**
     * 设置优惠卷适配器
     */
    private void setCouponAdapter() {
        if (disList != null && disList.size() != 0) {
            topView.setVisibility(View.VISIBLE);
            buttomView.setVisibility(View.VISIBLE);
        }
        CouponAdapter adapter = new CouponAdapter(LabelActivity.this, disList);
        couponLayout.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_image:
                showWindow();
                break;
        }
    }

    /***
     * 显示分享弹窗
     */
    private void showWindow() {
        SharePopupWindow share = new SharePopupWindow(this);
        sharetitle = bean == null || StringUtils.isEmpty(bean.getTabtitle()) ? "好易购热卖" : bean.getTabtitle();
        tabdesc = bean == null || StringUtils.isEmpty(bean.getTabdesc()) ? "好易购" : bean.getTabdesc();
        shareurl = HttpClient.HTTP_DOMAIN + "/distributor/productTab/" + productTabId + "/" + disId + ".html?visfrom=5";
        share.setShareMessage(sharetitle, imgurl, tabdesc, shareurl);
        share.showAtLocation(findViewById(R.id.label_layout), Gravity.BOTTOM, 0, 0);
    }
}