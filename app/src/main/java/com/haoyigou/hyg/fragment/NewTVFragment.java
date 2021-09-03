package com.haoyigou.hyg.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.ShopBO;
import com.haoyigou.hyg.entity.TVLeftData;
import com.haoyigou.hyg.entity.TVShopBO;
import com.haoyigou.hyg.ui.SmartFooter;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.zhy.autolayout.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2019/3/26
 * Describe: TV商品
 */
public class NewTVFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.leftList)
    ListView leftList;
    @BindView(R.id.rightList)
    ListView rightList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private CommonAdapter<TVLeftData> leftAdapter;
    private CommonAdapter<TVShopBO> rightAdapter;

    private List<TVLeftData> leftData = new ArrayList<>();
    private List<TVShopBO> rightData = new ArrayList<>();

    private int page = 1;
    private int mPosition = 0;
    private int loadPosition = 0;//左侧分类加载的位置

    private boolean isFirstLoad = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_tv_fragment, null,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rightList.setOnItemClickListener(this);
        initListener();
        isFirstLoad = true;//视图创建完成，将变量置为true

        if (getUserVisibleHint()) {//如果Fragment可见进行数据加载
            getRightLable();
            isFirstLoad = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = false;//视图销毁将变量置为false
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstLoad && isVisibleToUser) {//视图变为可见并且是第一次加载
            getRightLable();
            isFirstLoad = false;
        }

    }

    private void initListener(){
        leftList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //判断滚动是否停止
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //判断界面上显示的最后一项item的position，是否等于item的总个数减1（item的position从0开始），如果是，说明滑动到了底部。
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        /**
                         * 如果程序运行到这里，说明滑动到了底部，下面显示加载效果，通过线程加载数据
                         */
//                        page ++;
//                        if (loadPosition < rightData.size()) {
//                            getLeftData(String.valueOf(rightData.get(loadPosition).getCatagoryid()), page,false);
//                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                intent.putExtra("all", true);
                intent.putExtra("url", leftData.get(position).getJumpAdress() + "&parentLocation=101");
                startActivity(intent);
            }
        });

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page ++;
                if (loadPosition < rightData.size()) {
                    getLeftData(String.valueOf(rightData.get(loadPosition).getCatagoryid()), page,false);
                }
            }
        });
        refreshLayout.setRefreshFooter(new SmartFooter(getActivity()));
        refreshLayout.setFooterHeight(60);
    }

    /**
     * 获取右面列表数据
     */
    private void getRightLable() {
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.RIGHT_LABLE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    List<TVShopBO> mShop = JSONArray.parseArray(result.getData(), TVShopBO.class);
                    rightData.addAll(mShop);
                    getLeftData(String.valueOf(rightData.get(0).getCatagoryid()),page,true);
                    if (rightAdapter != null){
                        rightAdapter.notifyDataSetChanged();
                    }else {
                        setRightAdapter();
                    }
                } else {
                    showToast(result.getMessage());
                }
            }
        }, getActivity());
    }
    /**
     * 获取左面列表数据
     */
    private void getLeftData(String catagoryid, final int pageNo, final boolean isPressed) {
        Map<String, Object> params = new HashMap<>();
        params.put("catagoryid", catagoryid);
        params.put("pageNo", pageNo);
        params.put("parentLocation","117");
        HttpClient.post(HttpClient.LEFT_DATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                refreshLayout.finishLoadMore();
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    List<TVLeftData> mShop = JSONArray.parseArray(result.getData(), TVLeftData.class);
                    if (pageNo == 1 && isPressed){
                        leftData.clear();
                        leftData.addAll(mShop);
                    }else {
                        if (pageNo == 1) {
                            leftData.addAll(mShop);
                        } else {
                            if (mShop == null || mShop.size() == 0) {
                                page = 1;
                                loadPosition++;
                                getLeftData(String.valueOf(rightData.get(loadPosition).getCatagoryid()), page,false);
                                mPosition = loadPosition;
                            }else {
                                leftData.addAll(mShop);
                            }
                        }
                    }
                    if (leftAdapter != null){
                        leftAdapter.notifyDataSetChanged();
                        if (rightAdapter != null){
                            rightAdapter.notifyDataSetChanged();
                        }
                    }else {
                        setLeftAdapter();
                    }
                } else {
                    showToast(result.getMessage());
                }
            }
        }, getActivity());
    }


    private void setRightAdapter(){
        rightAdapter = new CommonAdapter<TVShopBO>(getActivity(),R.layout.new_tv_right_item,rightData) {
            @Override
            protected void convert(ViewHolder viewHolder, TVShopBO item, int position) {
                viewHolder.setText(R.id.txtTitle,item.getName());
                TextView txtTitle = (TextView) viewHolder.getConvertView().findViewById(R.id.txtTitle);
                if (mPosition == position){
                    viewHolder.getView(R.id.rlBack).setBackgroundColor(Color.parseColor("#FFC9191D"));
                    txtTitle.setTextColor(Color.parseColor("#FFFFFF"));
                }else {
                    viewHolder.getView(R.id.rlBack).setBackgroundColor(Color.parseColor("#ffffff"));
                    txtTitle.setTextColor(Color.parseColor("#101010"));
                }
            }
        };
        rightList.setAdapter(rightAdapter);
    }

    private void setLeftAdapter(){
        leftAdapter = new CommonAdapter<TVLeftData>(getActivity(),R.layout.new_tv_left_item,leftData) {
            @Override
            protected void convert(ViewHolder viewHolder, TVLeftData item, int position) {
                Glide.with(getActivity()).load(item.getPiclogo()).into((ImageView)viewHolder.getView(R.id.ivTitle));
                viewHolder.setText(R.id.txtName,item.getName());
                viewHolder.setText(R.id.txtPrice,String.format("¥%s",item.getDisprice()));
                viewHolder.setText(R.id.txtNum,String.format("%s人买过",item.getSalenum()));
                if (item.getPic4() != null && !item.getPic4().equals("")){
                    viewHolder.getView(R.id.ivHot).setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(item.getPic4()).into((ImageView)viewHolder.getView(R.id.ivHot));
                }else {
                    viewHolder.getView(R.id.ivHot).setVisibility(View.GONE);
                }
            }
        };
        leftList.setAdapter(leftAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position;
        if (rightAdapter != null){
            rightAdapter.notifyDataSetChanged();
        }
        page = 1;
        loadPosition = position;
        getLeftData(String.valueOf(rightData.get(position).getCatagoryid()),page,true);
        leftList.smoothScrollToPosition(0);
    }
}
