package com.haoyigou.hyg.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.NewsAdapter;
import com.haoyigou.hyg.adapter.NewsSmallAdapter;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.ClassifyDataBO;
import com.haoyigou.hyg.entity.ClassifyFragmentBO;
import com.haoyigou.hyg.entity.TagBean;
import com.haoyigou.hyg.ui.SmartFooter;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.GlideCacheUtil;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.HeaderGridView;
import com.haoyigou.hyg.view.widget.SharePopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by witness on 2018/9/10.
 *  第一种类型的分类界面
 */

public class ClassifyOneFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{

    @BindView(R.id.grid_view)
    HeaderGridView gridView;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshRoot;
    @BindView(R.id.topButton)
    ImageView topButton;
    @BindView(R.id.banner)
    ConvenientBanner banner;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.refreshProdect)
    SmartRefreshLayout refreshProdect;

    List<TagBean> mTitles = new ArrayList<>();
    List<String> mTitlesId = new ArrayList<>();

    private List<ClassifyDataBO> classifyDataBO = new ArrayList<>();

    private long catagoryid;//轮播图界面的id
    private long tabid;//长图界面的id
    private boolean isPrepared = false, isVisible = false;//是否加载完成，是否可见
    private ClassifyFragmentBO classifyList ;//标签和轮播图
    private int currPageNo = 1;
    private boolean isLong = false;
    private int normalPosition = 0;//当前选中的小标签

    private String sharetitle;   //分享标题
    private String imgurl;     //分享图片
    private String tabdesc;     //分享内容
    private String shareurl;   //分享链接
    private boolean isAddMore = false;//是否是下拉加载下一个标签的情况

    View view;
    private boolean isTop = true;//listview是否在顶部
    private boolean isChangeLable = true;//是否切换标签
    private int mverticalOffset;

    private boolean isFirstLoad = false;


    public static ClassifyOneFragment newInstance(String tabid, String catagoryid, String sharetitle) {
        ClassifyOneFragment informationFragment = new ClassifyOneFragment();
        Bundle b = new Bundle();
        b.putString("catagoryid",catagoryid);
        b.putString("tabid",tabid);
        b.putString("shareTitle",sharetitle);
        informationFragment.setArguments(b);
        return informationFragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlideCacheUtil.getInstance().clearImageMemoryCache(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            catagoryid = Long.parseLong(args.getString("catagoryid"));
            tabid = Long.parseLong(args.getString("tabid"));
            if (!args.getString("shareTitle").equals("")){
                sharetitle = args.getString("shareTitle");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.classify_one_layout, null,false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView.setOnItemClickListener(this);
        isPrepared = true;//表示布局加载好了
        refreshRoot.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                GlideCacheUtil.getInstance().clearImageMemoryCache(getActivity());
                if (normalPosition > 0) {
                    normalPosition = 0;
                    tabLayout.getTabAt(0).select();
                }else {
                    if (mTitles != null && mTitles.size() > 0) {
                        if (catagoryid != 0) {
                            getData(false, false, String.valueOf(catagoryid), String.valueOf(mTitles.get(normalPosition).getTagId()));
                        } else {
                            getData(false, false, String.valueOf(tabid), String.valueOf(mTitles.get(normalPosition).getTagId()));
                        }
                    }
                }
            }
        });
        refreshRoot.setRefreshHeader(new SmartHeader(getActivity()));
        refreshRoot.setHeaderHeight(60);
        refreshRoot.setEnableLoadMore(false);
        refreshProdect.setEnableRefresh(false);
        refreshProdect.setEnableLoadMore(true);
        refreshProdect.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (normalPosition < mTitles.size()) {
//                            footView.setVisibility(View.VISIBLE);
                    if (catagoryid != 0) {
                        getData(true, true, String.valueOf(catagoryid), String.valueOf(mTitles.get(normalPosition).getTagId()));
                    } else {
                        getData(true, true, String.valueOf(tabid), String.valueOf(mTitles.get(normalPosition).getTagId()));
                    }
                }else {
                    ToastUtils.showToast(getActivity(),"没有更多了");
                    refreshProdect.finishLoadMore();
                }
            }
        });
        refreshProdect.setRefreshFooter(new SmartFooter(getActivity()));
        refreshProdect.setFooterHeight(60);
        initView();


        isFirstLoad = true;//视图创建完成，将变量置为true
        if (getUserVisibleHint()) {//如果Fragment可见进行数据加载
            initData();
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
            initData();
            isFirstLoad = false;
        }

    }

    private void initView() {
        if (share != null) {
            share.setOnClickListener(this);
        }
        if (catagoryid != 0) {
            ViewGroup.LayoutParams params1 = banner.getLayoutParams();
            params1.height = (int) (DisplayUtil.getMobileHeight(getActivity()) / 4);
            banner.setLayoutParams(params1);
            isLong = false;
            banner.setCanLoop(true);
        } else {
            ViewGroup.LayoutParams params1 = banner.getLayoutParams();
            params1.height = (int) (DisplayUtil.getMobileWidth(getActivity()) / 0.9);
            banner.setLayoutParams(params1);
            ViewGroup.LayoutParams params2 = appBarLayout.getLayoutParams();
            params2.height = (int) (DisplayUtil.getMobileWidth(getActivity()) / 0.9) + (int) (DisplayUtil.getMobileWidth(getActivity())/9.8);
            appBarLayout.setLayoutParams(params2);
            isLong = true;
            banner.setCanLoop(false);
        }
    }

    private void initData(){
        getLable();
    }

    private void setTablayout() {
        if (classifyList.getSowing() != null && classifyList.getSowing().size()>0) {
            setBanner();
        }else {
            ViewGroup.LayoutParams params1 = banner.getLayoutParams();
            params1.height = 0;
            banner.setLayoutParams(params1);
        }
        mTitles.clear();
        mTitlesId.clear();
        tabLayout.removeAllTabs();
        for (int i=0;i<classifyList.getSecond().size();i++){
//            mTitles.add(classifyList.getSecond().get(i).getName());
            mTitles.add(new TagBean(classifyList.getSecond().get(i).getId(),classifyList.getSecond().get(i).getName()));
            mTitlesId.add(String.valueOf(classifyList.getSecond().get(i).getId()));
        }
        for (int i = 0; i < mTitles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(i).getTagName()));
        }
        tabLayout.getTabAt(0).select();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                GlideCacheUtil.getInstance().clearImageMemoryCache(getActivity());
                normalPosition = tab.getPosition();
                if (catagoryid != 0) {
                    getData(false,isAddMore,String.valueOf(catagoryid), String.valueOf(mTitles.get(normalPosition).getTagId()));
                }else{
                    getData(false,isAddMore,String.valueOf(tabid),String.valueOf(mTitles.get(normalPosition).getTagId()));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ViewCompat.setNestedScrollingEnabled(gridView,true);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
//                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                        if (normalPosition < mTitles.size()) {
////                            footView.setVisibility(View.VISIBLE);
//                            if (catagoryid != 0) {
//                                getData(true, true, String.valueOf(catagoryid), String.valueOf(mTitles.get(normalPosition).getTagId()));
//                            } else {
//                                getData(true, true, String.valueOf(tabid), String.valueOf(mTitles.get(normalPosition).getTagId()));
//                            }
//                     }else {
//                            ToastUtils.showToast(getActivity(),"没有更多了");
//                        }
//                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {   //当前显示第几行 最上面为头部布局，不需显示
                    isTop = true;
                    if (mverticalOffset >= 0) {
                        refreshRoot.setEnabled(true);
                    }else {
                        refreshRoot.setEnabled(false);
                    }
                } else {
                    refreshRoot.setEnabled(false);
                    isTop = false;
                }
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(mTitles == null){
                    return;
                }
                mverticalOffset = verticalOffset;
                if(verticalOffset >= 0 && isTop){
                    refreshRoot.setEnabled(true);
                }else{
                    refreshRoot.setEnabled(false);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share:
                showWindow();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public static class LocalImageHolderView implements Holder<ClassifyFragmentBO.SowingBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, ClassifyFragmentBO.SowingBean data) {
            Glide.with(context).load(data.getPictureroot())
                    .error(R.drawable.default_image)
                    .into(imageView);
        }
    }

    /**
     * 设置轮播图
     */
    private void setBanner() {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        banner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        },classifyList.getSowing())
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.lunbo_press, R.drawable.lunbo_normal})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        //设置翻页的效果，不需要翻页效果可用不设
//                .setPageTransformer(new AccordionTransformer());   // 集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
        banner.setManualPageable(true);//设置不能手动影响
//        banner.setCanLoop(true);
//        banner.setScrollDuration(2000);
        try {
            Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + AccordionTransformer.class.getSimpleName());
            ABaseTransformer transforemer = (ABaseTransformer) cls.newInstance();
            banner.getViewPager().setPageTransformer(true, transforemer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        banner.startTurning(5000);
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = classifyList.getSowing().get(position).getAddress();
                if (url == null || url.length() < 10) return;
                Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                intent.putExtra("url", url);
                intent.putExtra("all", true);
                intent.putExtra("isTitle", true);
                startActivity(intent);
            }
        });
    }

    private void getLable() {
        Map<String, Object> params = new HashMap<>();
        if (catagoryid !=0) {
            params.put("catagoryid", catagoryid);
        }
        if (tabid != 0) {
            params.put("tabid", tabid);
        }
        HttpClient.post(HttpClient.CLASSIFY_LABLE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                refreshRoot.finishRefresh();
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    classifyList = com.alibaba.fastjson.JSONObject.parseObject(result.getData(), ClassifyFragmentBO.class);
                    setTablayout();
                    if (catagoryid != 0) {
                        getData(false,false,String.valueOf(catagoryid), String.valueOf(mTitles.get(normalPosition).getTagId()));
                    }else{
                        getData(false,false,String.valueOf(tabid),String.valueOf(mTitles.get(normalPosition).getTagId()));
                    }
                } else {
                    showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                refreshRoot.finishRefresh();
            }
        }, getActivity());
    }

    NewsAdapter newsAdapter;
    NewsSmallAdapter newsSmallAdapter;

    private void getData(final boolean isPageAdd, final boolean loadmore, final String tabid, final String mcatagoryid) {
        if (isPageAdd) {
            currPageNo++;
        } else {
            currPageNo = 1;
        }
        Map<String, Object> params = new HashMap<>();
        if (isLong) {
            params.put("tabid", tabid);
            params.put("catagoryid",mcatagoryid);
        }else {
            params.put("catagoryid",tabid);
            params.put("secondid",mcatagoryid);
        }
        params.put("currPageNo",currPageNo);
        params.put("parentLocation",MApplication.classifyParentLocation);

        HttpClient.post(HttpClient.CLASSIFY_DATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                refreshRoot.finishRefresh();
                refreshProdect.finishLoadMore();
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    if (isLong) {
                        if (isPageAdd) {
                            if (com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class) == null ||
                                    com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class).size() == 0) {
                                currPageNo = 1;
                                normalPosition ++;
                                isAddMore = true;
                                if (normalPosition < mTitles.size()) {
                                    tabLayout.getTabAt(normalPosition).select();
                                }else {
                                    isAddMore = false;
                                    ToastUtils.showToast(getActivity(),"没有更多了");
                                    refreshProdect.finishLoadMore();
                                }
                                isChangeLable = false;
                            }else {
                                isAddMore = false;
                                isChangeLable = false;
                                classifyDataBO.addAll(com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class));
                                setLongAdapter(classifyDataBO);
                            }
                        } else {
                            classifyDataBO.clear();
                            isAddMore = false;
                            isChangeLable = true;
                            classifyDataBO.addAll(com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class));
                            setLongAdapter(classifyDataBO);
                        }
                    }else {
                        if (isPageAdd || loadmore) {
                            if (com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class) == null ||
                                    com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class).size() == 0) {
                                currPageNo = 1;
                                normalPosition ++;
                                isAddMore = true;
                                if (normalPosition < mTitles.size()) {
                                    tabLayout.getTabAt(normalPosition).select();

                                }else {
                                    isAddMore = false;
                                    ToastUtils.showToast(getActivity(),"没有更多了");
                                    refreshProdect.finishLoadMore();
                                }
                                isChangeLable = false;
                            }else {
                                isChangeLable = false;
                                isAddMore = false;
                                classifyDataBO.addAll(com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class));
                                setShortAdapter(classifyDataBO);
                            }
                        } else {
                            classifyDataBO.clear();
                            isAddMore = false;
                            isChangeLable = true;
                            classifyDataBO.addAll(com.alibaba.fastjson.JSONArray.parseArray(result.getData(), ClassifyDataBO.class));
                            setShortAdapter(classifyDataBO);
                        }
                    }
                } else {
                    showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                refreshProdect.finishLoadMore();
            }
        }, getActivity());
    }

    /***
     * 显示分享弹窗
     */
    private void showWindow() {
        SharePopupWindow share = new SharePopupWindow(getActivity());
        shareurl = HttpClient.HTTP_DOMAIN + "/distributor/productTab/" + tabid+ "/" + disId + ".html?visfrom=5";
        if (classifyList.getSowing().get(0).getPictureroot() != null) {
            imgurl = classifyList.getSowing().get(0).getPictureroot();
        }
        share.setShareMessage("好易购", imgurl, sharetitle, shareurl);
        share.showAtLocation( view , Gravity.BOTTOM, 0, 0);
    }


    /**
     * 长图界面适配器
     */
    CommonAdapter<ClassifyDataBO> longAdapter;
    private void setLongAdapter(final List<ClassifyDataBO> classifyDataBO){
        gridView.setNumColumns(1);
        if (longAdapter != null) {
            longAdapter.notifyDataSetChanged();
            if (isChangeLable){
                gridView.smoothScrollToPosition(0);
            }
            return;
        }
        longAdapter = new CommonAdapter<ClassifyDataBO>(getActivity(),R.layout.item_classify,classifyDataBO) {
            @Override
            protected void convert(ViewHolder holder, ClassifyDataBO item, final int position) {
                holder.setText(R.id.tvTitle,classifyDataBO.get(position).getName());
                TextView tvCount = holder.getConvertView().findViewById(R.id.tvCount);
                if (classifyDataBO.get(position).getDiscontent().equals("")){
                    holder.setText(R.id.tvCount,classifyDataBO.get(position).getContent());
                    Drawable drawableLeft = getResources().getDrawable(R.mipmap.hui);
                    tvCount.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null, null, null);
                    holder.getView(R.id.rlCount).setVisibility(View.GONE);
                    holder.getView(R.id.custom_pic).setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(classifyDataBO.get(position).getPic5())
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into((ImageView) holder.getView(R.id.custom_pic));
                }else {
                    holder.setText(R.id.tvCount,classifyDataBO.get(position).getDiscontent());
                    Drawable drawableLeft = getResources().getDrawable(
                            R.mipmap.count);
                    tvCount.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null, null, null);
                    holder.getView(R.id.rlCount).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv1,String.valueOf(classifyDataBO.get(position).getUnitCount()));
                    holder.setText(R.id.tv2,String.valueOf(classifyDataBO.get(position).getDecimalCount()));
                    holder.getView(R.id.custom_pic).setVisibility(View.GONE);
                }
                holder.setText(R.id.tvOldPrice,String.format("¥%s",classifyDataBO.get(position).getPrice()));
                holder.setText(R.id.tvnewPrice,String.format("¥%s",classifyDataBO.get(position).getDisprice()));

                final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>((ImageView) holder.getView(R.id.ivPic));
                ImageView target = imageViewWeakReference.get();
                if (target != null) {
                    Glide.with(getActivity()).load(classifyDataBO.get(position).getPiclogo()).asBitmap().error(R.drawable.default_image)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override((int)(DisplayUtil.getMobileWidth(getActivity()) * 0.26),(int)(DisplayUtil.getMobileWidth(getActivity()) * 0.26))
                            .into(target);
                }
                holder.getView(R.id.rlAddShopCar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", classifyDataBO.get(position).getUrl() + "&parentLocation="+ MApplication.classifyParentLocation);
                        intent.putExtra("all", true);
                        startActivity(intent);
                    }
                });
                holder.getView(R.id.llBack).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", classifyDataBO.get(position).getUrl()+ "&parentLocation=" + MApplication.classifyParentLocation);
                        intent.putExtra("all", true);
                        startActivity(intent);
                    }
                });
                if (classifyDataBO.get(position).getStore() == 0) {
                    holder.getView(R.id.store_layout).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.store_layout).setVisibility(View.GONE);
                }
            }
        };
        gridView.setAdapter(longAdapter);
    }

    /**
     * 短图界面适配器
     */
    CommonAdapter<ClassifyDataBO> shortAdapter;
    private void setShortAdapter(final List<ClassifyDataBO> classifyDataBO){
        gridView.setNumColumns(2);
        if (shortAdapter != null) {
            shortAdapter.notifyDataSetChanged();
            if (isChangeLable ){
                gridView.smoothScrollToPosition(0);
            }
            return;
        }
        shortAdapter = new CommonAdapter<ClassifyDataBO>(getActivity(),R.layout.item_home_hot_shop,classifyDataBO) {
            @Override
            protected void convert(ViewHolder holder, ClassifyDataBO item, final int position) {
                final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>((ImageView) holder.getView(R.id.shop_img));
                ImageView target = imageViewWeakReference.get();
                if (StringUtils.isEmpty(classifyDataBO.get(position).getPiclogo())) {
                    if (target != null) {
                        Glide.with(getActivity()).load(R.drawable.default_image)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .override((int)(DisplayUtil.getMobileWidth(getActivity()) * 0.46),(int)(DisplayUtil.getMobileWidth(getActivity()) * 0.46))
                                .into(target);
                    }
                } else {
                    Glide.with(getActivity()).load(classifyDataBO.get(position).getPiclogo())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override((int)(DisplayUtil.getMobileWidth(getActivity()) * 0.46),(int)(DisplayUtil.getMobileWidth(getActivity()) * 0.46))
                            .into(target);
                }
                holder.setText(R.id.shop_title,classifyDataBO.get(position).getName());
                holder.setText(R.id.price,String.format("¥%s",classifyDataBO.get(position).getDisprice()));
                holder.getView(R.id.shop_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", classifyDataBO.get(position).getUrl()+ "&parentLocation=" + MApplication.classifyParentLocation);
                        intent.putExtra("all", true);
                        startActivity(intent);
                    }
                });
                if (classifyDataBO.get(position).getStore() == 0) {
                    holder.getView(R.id.store_layout).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.store_layout).setVisibility(View.GONE);
                }

                if (classifyDataBO.get(position).getPic5() != null) {
                    if (!classifyDataBO.get(position).getPic5().equals("")) {
                        holder.getView(R.id.custom_pic).setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(classifyDataBO.get(position).getPic5())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into((ImageView) holder.getView(R.id.custom_pic));
                    } else {
                        holder.getView(R.id.custom_pic).setVisibility(View.GONE);
                    }
                }
            }
        };
        gridView.setAdapter(shortAdapter);
    }
}
