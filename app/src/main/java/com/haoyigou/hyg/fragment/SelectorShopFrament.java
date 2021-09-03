package com.haoyigou.hyg.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.LabelAdapter;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.ShopEntry;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.ui.SmartFooter;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/3/16.
 * <p>
 * 搜索页面的商品列表fragment
 */

public class SelectorShopFrament extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.zonghe)
    TextView zonghe;
    @BindView(R.id.xiaoliang)
    TextView xiaoliang;
    @BindView(R.id.jiage_image)
    ImageView jiageImage;
    @BindView(R.id.jiage)
    LinearLayout jiage;
    @BindView(R.id.shop_list)
    ListView shopList;
    @BindView(R.id.jiage_text)
    TextView jiageText;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<ShopEntry> list;

    private String sortType = "1";    //1 综合 2 销量倒序  3 价格升序  4 价格降序
    private String Pfflid;
    private String pflid;
    private String name = "";
    private int currPageNo = 1;
    private int currPageNoName = 1;
    LabelAdapter adapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fra_seletor_shop, null);
        ButterKnife.bind(this, mainView);
        return mainView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        zonghe.setOnClickListener(this);
        xiaoliang.setOnClickListener(this);
        jiage.setOnClickListener(this);
        shopList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //判断滚动是否停止
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                //判断界面上显示的最后一项item的position，是否等于item的总个数减1（item的position从0开始），如果是，说明滑动到了底部。
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                        /**
//                        * 如果程序运行到这里，说明滑动到了底部，下面显示加载效果，通过线程加载数据
//                        */
//                        currPageNo++;
//                        currPageNoName ++;
//                        if (!name.equals("")){
//                            if (sortType.equals("2")){
//                                setSortType(sortType);
//                            }else if(sortType.equals("3")){
//                                setSortType(sortType);
//                            }else if(sortType.equals("4")){
//                                setSortType(sortType);
//                            }else {
//                                setSelectorName(name,currPageNoName,context);
//                            }
//                        }else if (!Pfflid.equals("")){
//                            setClassFiy(Pfflid,pflid,context);
//                        }else{
//                            setSortType(sortType);
//                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currPageNo++;
                currPageNoName ++;
                if (!name.equals("")){
                    if (sortType.equals("2")){
                        setSortType(sortType);
                    }else if(sortType.equals("3")){
                        setSortType(sortType);
                    }else if(sortType.equals("4")){
                        setSortType(sortType);
                    }else {
                        setSelectorName(name,currPageNoName,context);
                    }
                }else if (!Pfflid.equals("")){
                    setClassFiy(Pfflid,pflid,context);
                }else{
                    setSortType(sortType);
                }
            }
        });
        refreshLayout.setRefreshFooter(new SmartFooter(getActivity()));
        refreshLayout.setFooterHeight(60);
    }

    /**
     * 根据关键词搜索商品
     *
     * @param name
     */
    public void setSelectorName(String name,int currPageNo,Context context) {
        this.name = name;
        this.Pfflid = "";
        this.pflid = "";
        this.context = context;
        currPageNoName = currPageNo;
        Map<String, Object> params = new HashMap<>();
        params.put("currPageNo",currPageNo);
        params.put("distributorId", disId);
        params.put("name", name);
        params.put("showplat", "2");
        if (!MApplication.selectorParentLocation.equals("")){
            params.put("parentLocation",MApplication.selectorParentLocation);
        }
        postHttpMessage(params,currPageNo);
    }

    /**
     * 根据分类搜索商品
     *
     * @param Pfflid
     * @param pflid  二级分类id。在首页的分类处点击进入搜索页面，此参数不用传递；
     *               在分类页面点击具体二级分类，此参数需要传递
     */
    public void setClassFiy(String Pfflid, String pflid,Context context) {
        this.Pfflid = Pfflid;
        this.pflid = pflid;
        this.context = context;
        Map<String, Object> params = new HashMap<>();
        params.put("currPageNo",currPageNo);
        params.put("distributorId", disId);
        params.put("Pfflid", Pfflid);
        if (!StringUtils.isEmpty(pflid)) params.put("pflid", pflid);
        params.put("name", name);
        params.put("showplat", "2");
        if (!MApplication.selectorParentLocation.equals("")){
            params.put("parentLocation",MApplication.selectorParentLocation);
        }
        postHttpMessage(params,currPageNo);
    }


    /**
     * 根据筛选类别筛选商品
     *
     * @param sortType
     */
    public void setSortType(String sortType) {
        this.sortType = sortType;
        Map<String, Object> params = new HashMap<>();
        params.put("currPageNo",currPageNo);
        params.put("distributorId", disId);
        params.put("name", name);
        params.put("sortType", sortType);
        params.put("showplat", "2");
        if (!MApplication.selectorParentLocation.equals("")){
            params.put("parentLocation",MApplication.selectorParentLocation);
        }
        if (!StringUtils.isEmpty(Pfflid)) params.put("pfflid", Pfflid);
        if (!StringUtils.isEmpty(pflid)) params.put("pflid", pflid);
        postHttpMessage(params,currPageNo);
    }


    /**
     * 根据不同参数请求并设置数据
     */
    private void postHttpMessage(Map<String, Object> params, final int currPageno) {
        if (currPageno == 1) {
            startProgressDialog("加载中", context);
        }
        HttpClient.post(HttpClient.SELETORNAME, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                refreshLayout.finishLoadMore();
                stopProgressDialog();
//                Log.e("log--select", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String array = object.getString("products");
                    if (currPageno == 1) {
                        list = JSONArray.parseArray(array, ShopEntry.class);
                        setAdapter();
                    }else {
                        if (JSONArray.parseArray(array, ShopEntry.class).size() == 0){
                            showToast("没有更多了");
//                            currPageno --;
                        }else {
                            list.addAll(JSONArray.parseArray(array, ShopEntry.class));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                stopProgressDialog();
                refreshLayout.finishLoadMore();
            }
        }, getActivity());
    }

    /**
     * 为商品列表设置适配器
     */
    private void setAdapter() {
        adapter = new LabelAdapter(getActivity(), "10010", list);
        shopList.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zonghe:
                if (!"1".equals(sortType)) {
                    currPageNo = 1;
                    setSortType("1");
                    zonghe.setTextColor(Color.parseColor("#e72e2d"));
                    xiaoliang.setTextColor(Color.parseColor("#3b3b3b"));
                    jiageText.setTextColor(Color.parseColor("#3b3b3b"));
                    jiageImage.setBackgroundResource(R.drawable.selector_price01);
                }
                break;
            case R.id.xiaoliang:
                if (!"2".equals(sortType)) {
                    currPageNo = 1;
                    setSortType("2");
                    zonghe.setTextColor(Color.parseColor("#3b3b3b"));
                    xiaoliang.setTextColor(Color.parseColor("#e72e2d"));
                    jiageText.setTextColor(Color.parseColor("#3b3b3b"));
                    jiageImage.setBackgroundResource(R.drawable.selector_price01);
                }
                break;
            case R.id.jiage:
                if (!"3".equals(sortType)) {
                    currPageNo = 1;
                    setSortType("3");
                    zonghe.setTextColor(Color.parseColor("#3b3b3b"));
                    xiaoliang.setTextColor(Color.parseColor("#3b3b3b"));
                    jiageText.setTextColor(Color.parseColor("#e72e2d"));
                    jiageImage.setBackgroundResource(R.drawable.selector_price02);
                } else {
                    currPageNo = 1;
                    setSortType("4");
                    zonghe.setTextColor(Color.parseColor("#3b3b3b"));
                    xiaoliang.setTextColor(Color.parseColor("#3b3b3b"));
                    jiageText.setTextColor(Color.parseColor("#e72e2d"));
                    jiageImage.setBackgroundResource(R.drawable.selector_price03);
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
