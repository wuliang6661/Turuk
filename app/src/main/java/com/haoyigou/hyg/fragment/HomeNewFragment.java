package com.haoyigou.hyg.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.entity.MadrushBO;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.MessageBoxActivty;
import com.haoyigou.hyg.ui.PromotionsPopActivity;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.adapter.BaseAdapterHelper;
import com.haoyigou.hyg.adapter.BerserListAdapterBase;
import com.haoyigou.hyg.adapter.QuickAdapter;
import com.haoyigou.hyg.adapter.RecyclerViewAdapter;
import com.haoyigou.hyg.adapter.RollHeaderView;
import com.haoyigou.hyg.adapter.ShopListAdapter;
import com.haoyigou.hyg.adapter.WeekRecylerAdapter;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.BrandsEntry;
import com.haoyigou.hyg.entity.HomeBeanList;
import com.haoyigou.hyg.entity.LunBoPicEntry;
import com.haoyigou.hyg.entity.MonthEntry;
import com.haoyigou.hyg.entity.PromotionsPopEntity;
import com.haoyigou.hyg.entity.ShopEntry;
import com.haoyigou.hyg.entity.VideoEntry;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.TimeCountUtils;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;
import com.haoyigou.hyg.view.widget.PullDownElasticImp;
import com.haoyigou.hyg.view.widget.PullDownScrollView;
import com.haoyigou.hyg.view.widget.SharePopupWindow;
import com.haoyigou.hyg.view.widget.TopAndButtomListView;
import com.haoyigou.hyg.view.zxing.ActivityCapture;
import com.haoyigou.hyg.base.BaseFragment;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2016/12/6.
 * <p>
 * 尝试改良界面，禁止scollview，嵌套listview
 */

public class HomeNewFragment extends BaseFragment implements PullDownScrollView.RefreshListener,
        View.OnClickListener, AdapterView.OnItemClickListener {

    private static int TIMEDOWNIMG = 5;    //默认轮播图切换时间为5秒
    private static int page = 1;          //从第一页商品开始获取
    private static final int pageNum = 20;    // 默认获取商品数为20个
    private static int FENGQIANGTYPE = 1;

    @BindView(R.id.scan_img)
    ImageView scanImg;
    @BindView(R.id.search_text)
    TextView searchText;
    @BindView(R.id.search_layout)
    RelativeLayout searchLayout;
    @BindView(R.id.message_img)
    ImageView messageImg;
    @BindView(R.id.share_img)
    ImageView shareImg;
    @BindView(R.id.classify_list_view)
    RecyclerView classifyListView;
    @BindView(R.id.shop_list)
    TopAndButtomListView shopListview;
    @BindView(R.id.refresh_root)
    PullDownScrollView refreshRoot;
    @BindView(R.id.home_fragment)
    RelativeLayout homeFragment;
    @BindView(R.id.topButton)
    ImageView topButton;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.num_sun)
    TextView numSun;
    @BindView(R.id.shop_num_layout)
    LinearLayout shopNumLayout;

    private TextView fengqiangText;
    private TextView nextNotice;
    private ListViewForScrollView huodongList;
    private ImageView more;
    private RollHeaderView gdViewpager;
    private ListViewForScrollView chaozhiList;
    private RecyclerView weekNewsList;
    private TextView notFengqiang;
    private ListViewForScrollView fengqiangList;
    private View viewLine, viewLine2;
    private LinearLayout videoFragment;   //视频显示的fragment
    private TextView videoTitle;            //视频标题

    private SharePopupWindow sharePopupWindow;//分享的弹窗

    HomeBeanList dataBean;  //首页的数据集合
    RecyclerViewAdapter classifyAdapter;   //分类的适配器
    BerserListAdapterBase berserkAdapter;          //正在疯抢适配器
    List<MadrushBO> berserkList;    //正在疯抢的数据集合
    List<ShopEntry> shopList;              //商品数据集合
    List<LunBoPicEntry> lunboList;        //轮播图列表
    List<MonthEntry> huodong;               //活动集合
    List<BrandsEntry> chaozhi;              //超值热卖集合

    boolean isPageSun = true;   //用来表示下一页没有加载 ,可以加载下一页
    private static String totalCount;   //商品的总数量显示
    private boolean isScooll = false;   //表示列表是否正在滑动,默认为否

    private String disId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_new_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        invitionHeader();
        invition();
        refreshRoot.setVisibility(View.GONE);
        setListener();
        startProgressDialog("", getActivity());
        getDataMessgae();
        getRushedMessage(1);
        getShopMessageData();
        isHasPromotion();
    }


    /***
     * 初始化listView的头部布局控件
     */
    private void invitionHeader() {
        View headlerView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_header_view, null);
        fengqiangText = (TextView) headlerView.findViewById(R.id.fengqiang_text);
        nextNotice = (TextView) headlerView.findViewById(R.id.next_notice);
        huodongList = (ListViewForScrollView) headlerView.findViewById(R.id.huodong_list);
        more = (ImageView) headlerView.findViewById(R.id.more);
        gdViewpager = (RollHeaderView) headlerView.findViewById(R.id.gd_viewpager);
        chaozhiList = (ListViewForScrollView) headlerView.findViewById(R.id.chaozhi_list);
        weekNewsList = (RecyclerView) headlerView.findViewById(R.id.week_news_list);
        notFengqiang = (TextView) headlerView.findViewById(R.id.not_fengqiang);
        fengqiangList = (ListViewForScrollView) headlerView.findViewById(R.id.fengqiang_list);
        viewLine = headlerView.findViewById(R.id.view_line);
        viewLine2 = headlerView.findViewById(R.id.view_line2);
        videoFragment = (LinearLayout) headlerView.findViewById(R.id.video_layout);
        videoTitle = (TextView) headlerView.findViewById(R.id.video_title);
        ViewGroup.LayoutParams params = videoFragment.getLayoutParams();
        params.width = GlobalApplication.screen_width;
        params.height = (int) (GlobalApplication.screen_width * 0.56);
        videoFragment.setLayoutParams(params);
        shopListview.addHeaderView(headlerView);
        gdViewpager.setApplicationContext(getActivity().getApplicationContext());
    }


    /**
     * 设置点击事件监听
     */

    private void setListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   //申请拍照权限扫一扫功能
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        sharePopupWindow = new SharePopupWindow(getActivity());
        topButton.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        scanImg.setOnClickListener(this);
        shareImg.setOnClickListener(this);
        fengqiangText.setOnClickListener(this);
        nextNotice.setOnClickListener(this);
        shopListview.setOnItemClickListener(this);
        huodongList.setOnItemClickListener(this);
        messageImg.setOnClickListener(this);
        more.setOnClickListener(this);
        chaozhiList.setOnItemClickListener(this);
        refreshRoot.setRefreshListener(this);  //设置下拉刷新监听
        refreshRoot.setPullDownElastic(new PullDownElasticImp(getActivity()));
        shopListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        if (isPageSun) {
                            isPageSun = false;
                            page++;
                            getShopMessageData();
                        }
                    }
                    if (shopNumLayout.getVisibility() == View.VISIBLE) {
                        topButton.setVisibility(View.VISIBLE);
                        shopNumLayout.setVisibility(View.GONE);
                    }
                    isScooll = false;
                } else {
                    isScooll = true;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i == 0) {   //当前显示第几行 最上面为头部布局，不需显示
                    topButton.setVisibility(View.GONE);
                    shopNumLayout.setVisibility(View.GONE);
                } else {
                    if (!isScooll) return;   //如果滑动状态是停止状态，就不执行下面的
                    if (totalCount != null) {
                        numSun.setText(totalCount);
                        num.setText(i + "");
                        shopNumLayout.setVisibility(View.VISIBLE);
                        topButton.setVisibility(View.GONE);
                    }
                }
            }
        });
        gdViewpager.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
            @Override
            public void HeaderViewClick(int position) {
                String url = lunboList.get(position).getAddress();
                if (url == null || url.length() < 10) return;
                Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                intent.putExtra("url", url);
                intent.putExtra("all", true);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (StateMessage.haveMes.equals("0")) {   //无未读消息
            messageImg.setImageResource(R.drawable.first_message);
        } else {
            messageImg.setImageResource(R.drawable.first_messag_dis);
        }
        if (GlobalApplication.user != null) {
            String search = GlobalApplication.user.getSearchdesc();
            if (search != null) {
                searchText.setText(search);
            }
        }
    }

    /**
     * 出始化控件初始属性
     */
    private void invition() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置为横向布局
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //设置布局管理器
        classifyListView.setLayoutManager(layoutManager);
        //设置增加或删除条目的动画
        classifyListView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        //设置为横向布局
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager1.setAutoMeasureEnabled(true);
        weekNewsList.setLayoutManager(layoutManager1);
        weekNewsList.setItemAnimator(new DefaultItemAnimator());
//        if (scrollView.isScrolledToTop()) topButton.setVisibility(View.GONE);  //判断topButton要不要显示
    }


    /***
     * 获取首页分类、轮播图、每周上新、本月活动、超值热卖数据
     */
    public void getDataMessgae() {
        Map<String, Object> map = new HashMap<>();
        map.put("distributorId", disId);
        map.put("showplat", "2");
        HttpClient.post(HttpClient.HOMEDATA, map, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--lunbo", content);
                if (content == null || content.equals("")) return;
                JSONObject object = JSON.parseObject(content);
                if (object.getString("status").equals(HttpClient.RET_SUCCESS_CODE)) {
                    TIMEDOWNIMG = object.getIntValue("lunbotime");
                    String data = object.getString("result");
                    if (data != null && !data.equals("")) {
                        dataBean = JSONObject.parseObject(data, HomeBeanList.class);
                        StateMessage.haveMes = object.getString("havemes");
                        if (StateMessage.haveMes.equals("0")) {   //无未读消息
                            messageImg.setImageResource(R.drawable.first_message);
                        } else {
                            messageImg.setImageResource(R.drawable.first_messag_dis);
                        }
                        List<VideoEntry> lists = JSONArray.parseArray(object.getString("video"), VideoEntry.class);
                        setDataAdapter(lists);
                    }
                } else {
                    stopProgressDialog();
                }
            }
        }, getActivity());
    }

    /***
     * 设置首页分类、轮播图、每周上新、本月活动、超值热卖适配器
     */
    private void setDataAdapter(List<VideoEntry> lists) {
        //设置视频信息
        if (lists != null && lists.size() != 0) {
            videoTitle.setVisibility(View.VISIBLE);
            videoTitle.setText(lists.get(0).getTitle());
            EventBus.getDefault().post(lists.get(0));
        } else {
            videoFragment.setVisibility(View.GONE);
            videoTitle.setVisibility(View.GONE);
        }
        //分类
        classifyAdapter = new RecyclerViewAdapter(getActivity(), dataBean.getClassify());
        classifyListView.setAdapter(classifyAdapter);
        //轮播图
        lunboList = dataBean.getLunbopics();
        gdViewpager.setCarouselTime(TIMEDOWNIMG);
        gdViewpager.setImgUrlData(lunboList);
        //本月活动
        huodong = dataBean.getTabs();
        QuickAdapter<MonthEntry> adapter = new QuickAdapter<MonthEntry>(getActivity(), R.layout.item_month_layout, huodong) {
            @Override
            protected void convert(BaseAdapterHelper helper, MonthEntry item) {
                ImageView image = helper.getView(R.id.huodong_img);
                ViewGroup.LayoutParams params = image.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(getActivity()) / 1.83);
                image.setLayoutParams(params);
                helper.setImageUrl(R.id.huodong_img, item.getPic());
            }
        };
        huodongList.setAdapter(adapter);
        //每周上新
        WeekRecylerAdapter adapter1 = new WeekRecylerAdapter(getActivity(), dataBean.getNewpros());
        weekNewsList.setAdapter(adapter1);
        //超值热卖
        chaozhi = dataBean.getBrands();
        QuickAdapter<BrandsEntry> adapter3 = new QuickAdapter<BrandsEntry>(getActivity(), R.layout.item_hot_banner, chaozhi) {
            @Override
            protected void convert(BaseAdapterHelper helper, BrandsEntry item) {
                ImageView image = helper.getView(R.id.banner_img);
                ViewGroup.LayoutParams params = image.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(context) / 1.83);
                image.setLayoutParams(params);
                helper.setImageUrl(R.id.banner_img, item.getPic2());
                helper.setText(R.id.banner_name, item.getTitle());
                helper.setText(R.id.banner_message, item.getShortDesc());
                helper.setText(R.id.banner_price, "¥ " + item.getMinPrice() + " 起");
            }
        };
        chaozhiList.setAdapter(adapter3);
        refreshRoot.setVisibility(View.VISIBLE);
        stopProgressDialog();
    }

    /**
     * 获取正在疯抢数据
     * <p>
     * 参数i为1，则获取正在疯抢数据，为2则获取下期预告数据
     */
    private void getRushedMessage(final int i) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", i + "");
        params.put("showplat", "2");
        HttpClient.post(HttpClient.HOMEFENGQIANG, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--fengqiang", content);
                if (content == null || content.equals("")) return;
                JSONObject object = JSON.parseObject(content);
                if (object.getString("status").equals(HttpClient.RET_SUCCESS_CODE)) {
                    String data = object.getString("result");
                    if (berserkList != null)
                        berserkList.clear();
                    berserkList = JSONArray.parseArray(data, MadrushBO.class);
                }
                setBannerdAdapter(i);
            }
        }, getActivity());
    }

    /**
     * 设置疯抢列表适配器
     */
    private void setBannerdAdapter(int i) {
        if (FENGQIANGTYPE == 1) {
            notFengqiang.setText("本次活动已结束！");
        } else {
            notFengqiang.setText("活动暂未开始，敬请期待！");
        }
        if (berserkList == null || berserkList.size() == 0) {
            notFengqiang.setVisibility(View.VISIBLE);
            fengqiangList.setVisibility(View.GONE);
            return;
        }
        berserkAdapter = new BerserListAdapterBase(getActivity(), berserkList);
        berserkAdapter.setIndex(i);
        fengqiangList.setAdapter(berserkAdapter);
        TimeCountUtils utils = TimeCountUtils.getInstance();
        utils.setOnTimeConuntListener(new TimeCountUtils.onTimeConunt() {
            @Override
            public void onProgress() {
                if (getActivity() != null)
                    refreshChild();
            }
        });
//        Log.e("wuliang", "设置适配器进入");
        notFengqiang.setVisibility(View.GONE);
        fengqiangList.setVisibility(View.VISIBLE);
    }

    /**
     * 定时器计算并给每一行更新数据
     */
    private void refreshChild() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < berserkList.size(); i++) {
                    Long[] time = setTime(berserkList.get(i).getTime());
                    if (time == null) {
                        berserkList.remove(i);
                        setBannerdAdapter(1);
                    } else {
                        View view = fengqiangList.getChildAt(i);
                        if (view == null) return;
                        TextView hour = (TextView) view.findViewById(R.id.hour_text);
                        TextView minnute = (TextView) view.findViewById(R.id.minute_text);
                        TextView second = (TextView) view.findViewById(R.id.second_text);
                        hour.setText(time[0] + "");
                        minnute.setText(time[1] + "");
                        second.setText(time[2] + "");
                    }
                }
            }
        });
    }

    public Long[] setTime(long timeMinitute) {
        long dis = timeMinitute - new Date().getTime();
        if (dis != 0) {
            long diftime = dis / 1000;
            long hour = 0, minute, second;
            if (diftime > 60) {
                if (diftime > 3600) {
                    hour = diftime / 3600;
                    minute = diftime % 3600 / 60;
                    second = diftime % 3600 % 60 % 60;
                } else {
                    second = diftime % 60;
                    minute = diftime / 60;
                }
            } else {
                second = diftime;
                minute = 0;
                hour = 0;
            }
            if (hour == 0 && minute == 0 && second <= 0) {
                return null;
            }
            return new Long[]{hour, minute, second};
        }
        return null;
    }

    /***
     * 获取底下页面商品数据
     */
    private void getShopMessageData() {
        Map<String, Object> params = new HashMap<>();
        params.put("showplat", "2");
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        params.put("currPageNo", page + "");
        params.put("pageSize", pageNum + "");
        HttpClient.post(HttpClient.GETGOODINFO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--shop", content);
                isPageSun = true;
                if (content == null || content.equals("")) {
                    return;
                }
                JSONObject jsonObject = JSONObject.parseObject(content);
                String data = jsonObject.getString("result");
                totalCount = jsonObject.getString("totalCount");
                List<ShopEntry> shop = JSONArray.parseArray(data, ShopEntry.class);
                if (shop == null || shop.size() == 0) {
                    Toast.makeText(getActivity(), "已经到底啦！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (shopList != null) {
                    shopList.addAll(shop);
                } else {
                    shopList = shop;
                }
                setShopAdapter(shop);
            }
        }, getActivity());
    }

    ShopListAdapter shopadapter;

    /***
     * 设置商品适配器
     */
    private void setShopAdapter(List<ShopEntry> list) {
        if (shopadapter != null) {
            shopadapter.notifyDataSetChanged();
            return;
        }
        shopadapter = new ShopListAdapter(getActivity(), shopList);
        shopListview.setAdapter(shopadapter);
    }


    /***
     * 判断是否有优惠券
     */
    private void isHasPromotion() {
        PromotionsPopEntity param = new PromotionsPopEntity();
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
//        Log.e("PPP", param.toString());
        HttpClient.promptionspop(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, String body) {
                if (statusCode != 200 || StringUtils.isEmpty(body)) {
                    return;
                }
//                Log.i("getPrictureTAG", "loadPromotionsPop:" + body);
                JSONObject object = JSON.parseObject(body);
                int hasGetDiscount = object.getInteger("hasGetDiscount");
                int hastab = object.getInteger("hastab");
                if (hasGetDiscount == 1 || hastab == 1) {
                    Intent intent = new Intent(getActivity(), PromotionsPopActivity.class);
                    intent.putExtra("data", body);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
//                Log.i("getPrictureTAG", "失败返回数据:" + request.toString());
            }
        }, getActivity());
    }

    /***
     * 下拉刷新的回调时间
     */
    @Override
    public void onRefresh(PullDownScrollView view) {
        new Handler().postDelayed(new Runnable() {   //刷新一秒
            @Override
            public void run() {
                page = 1;
                refreshRoot.finishRefresh("刷新");
                shopList.clear();
                lunboList.clear();
                getDataMessgae();
                berserkAdapter = null;
                getRushedMessage(1);
                FENGQIANGTYPE = 1;
                getShopMessageData();
                fengqiangText.setTextColor(Color.parseColor("#e60012"));
                nextNotice.setTextColor(Color.parseColor("#666666"));
                fengqiangText.setTextSize(18);
                nextNotice.setTextSize(15);
                viewLine.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
        String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        switch (view.getId()) {
            case R.id.scan_img:   //扫一扫
                Intent intent1 = new Intent(getActivity(), ActivityCapture.class);
                startActivity(intent1);
                break;
            case R.id.search_layout:   //搜索框点击
//                intent.putExtra("url", HttpClient.SEARCH + "?distributorId=" + disid);
//                startActivity(intent);
                goToActivity(SelectorActivity.class, false);
                break;
            case R.id.message_img:   //消息盒子
                messageImg.setImageResource(R.drawable.first_message);
                StateMessage.haveMes = "0";
                Intent intent2 = new Intent(getActivity(), MessageBoxActivty.class);
                startActivity(intent2);
                break;
            case R.id.share_img:    //分享
                sharePopupWindow.setCopyUrl(HttpClient.SHAREURL + disid + ".html");
                sharePopupWindow.showAtLocation(getActivity().findViewById(R.id.home_fragment), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                startProgressDialog("", getActivity());
                break;
            case R.id.fengqiang_text:   //正在疯抢
                if (FENGQIANGTYPE == 1) return;
                FENGQIANGTYPE = 1;
                getRushedMessage(1);
                fengqiangText.setTextColor(Color.parseColor("#e60012"));
                nextNotice.setTextColor(Color.parseColor("#666666"));
                fengqiangText.setTextSize(18);
                nextNotice.setTextSize(15);
                viewLine.setVisibility(View.VISIBLE);
                viewLine2.setVisibility(View.GONE);
                break;
            case R.id.next_notice:    //下期预告
                if (FENGQIANGTYPE == 2) return;
                FENGQIANGTYPE = 2;
                getRushedMessage(2);
                fengqiangText.setTextColor(Color.parseColor("#666666"));
                nextNotice.setTextColor(Color.parseColor("#e60012"));
                fengqiangText.setTextSize(15);
                nextNotice.setTextSize(18);
                viewLine.setVisibility(View.GONE);
                viewLine2.setVisibility(View.VISIBLE);
                break;
            case R.id.topButton:
                shopListview.setSelection(0);
                shopListview.smoothScrollToPosition(0);
                topButton.setVisibility(View.GONE);
                break;
            case R.id.more:
//                intent.putExtra("url", HttpClient.HOMEMESSAGE + "?distributorId=" + disid + "&productTabId=-1");
//                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("productTabId", "-1");
                goToActivity(LabelActivity.class, bundle, false);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        switch (adapterView.getId()) {
            case R.id.shop_list:
//                intent = new Intent(getActivity(), WebviewActivity.class);
                String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + shopList.get(i - 1).getId();
//                intent.putExtra("url", HttpClient.HTTP_DOMAIN + url);
//                Log.e("log--aaa", url);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.huodong_list:
                if (StringUtils.isEmpty(huodong.get(i).getUrl())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("productTabId", huodong.get(i).getId());
                    goToActivity(LabelActivity.class, bundle, false);
//                    intent.putExtra("url", HttpClient.HOMEMESSAGE + "?distributorId=" + disId + "&productTabId=" + huodong.get(i).getId());
                } else {
                    intent.putExtra("all", true);
                    intent.putExtra("url", huodong.get(i).getUrl());
                    startActivity(intent);
                }
                break;
            case R.id.chaozhi_list:
//                intent.putExtra("url", HttpClient.HOMEMESSAGE + "?distributorId=" + disId + "&productTabId=" + chaozhi.get(i).getId());
                Bundle bundle = new Bundle();
                bundle.putString("productTabId", chaozhi.get(i).getId());
                goToActivity(LabelActivity.class, bundle, false);
                break;
        }
    }

    @Override
    public void onPause() {   //当页面暂停之后关闭进度条
        super.onPause();
//        stopProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        page = 1;
        if (shopList != null)
            shopList.clear();
        if (lunboList != null)
            lunboList.clear();
        TimeCountUtils utils = TimeCountUtils.getInstance();
        utils.stopTimeCount();
    }

}
