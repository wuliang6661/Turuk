package com.haoyigou.hyg.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.MadrushListAdapter;
import com.haoyigou.hyg.adapter.NewHomeAdapter;
import com.haoyigou.hyg.adapter.NewHomeThreeAdapter;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.GenerateTestUserSig;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.BannerBO;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.GroupUrlBean;
import com.haoyigou.hyg.entity.HomeOneBO;
import com.haoyigou.hyg.entity.HomeThreeBO;
import com.haoyigou.hyg.entity.HuiProductBean;
import com.haoyigou.hyg.entity.MadrushListBean;
import com.haoyigou.hyg.entity.Marquee;
import com.haoyigou.hyg.entity.MenuBO;
import com.haoyigou.hyg.entity.NewHomeBO;
import com.haoyigou.hyg.entity.NewProdectBO;
import com.haoyigou.hyg.entity.PromotionsPopEntity;
import com.haoyigou.hyg.entity.ShopBO;
import com.haoyigou.hyg.entity.VideoBean;
import com.haoyigou.hyg.ui.FullVideoActivity;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.LoginActivity;
import com.haoyigou.hyg.ui.MadrushAct;
import com.haoyigou.hyg.ui.NewTVActivity;
import com.haoyigou.hyg.ui.NewTVShowActivity;
import com.haoyigou.hyg.ui.PopularityAct;
import com.haoyigou.hyg.ui.PromotionsPopActivity;
import com.haoyigou.hyg.ui.RoundImageView;
import com.haoyigou.hyg.ui.RoundImageView3;
import com.haoyigou.hyg.ui.SelectionAct;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.ui.TVLiveVideoAct;
import com.haoyigou.hyg.ui.VoucherCenterAct;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.userWebviewActivity;
import com.haoyigou.hyg.utils.CustomUpdateParser;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.MyDialog;
import com.haoyigou.hyg.utils.NetworkUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.TimeCountUtils;
import com.haoyigou.hyg.utils.TimeUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.utils.Util;
import com.haoyigou.hyg.view.FixedRecyclerView;
import com.haoyigou.hyg.view.MarqueeView;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.lgrecycleadapter.LGRecycleViewAdapter;
import com.haoyigou.hyg.view.lgrecycleadapter.LGViewHolder;
import com.haoyigou.hyg.view.widget.HeaderGridView;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;
import com.haoyigou.hyg.view.zxing.ActivityCapture;
import com.haoyigou.hyg.wxapi.WXUtils;
import com.luck.picture.lib.tools.SPUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.paradoxie.autoscrolltextview.VerticalTextview;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shehuan.niv.NiceImageView;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.stx.xhb.androidx.XBanner;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xuexiang.xupdate.XUpdate;
import com.yunfan.player.widget.YfCloudPlayer;
import com.yunfan.player.widget.YfPlayerKit;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import cn.jzvd.BuyCallBack;
import cn.jzvd.CallBackUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static com.haoyigou.hyg.base.BaseFragmentActivity.mApplication;

/**
 * Created by witness on 2017/10/16.
 * <p>
 * 新版首页
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener , BuyCallBack{


    @BindView(R.id.grid_view)
    ListView gridView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.topButton)
    ImageView topButton;
    @BindView(R.id.fab)
    ImageView fab;
//    @InjectView(R.id.refresh_root)
//    VpSwipeRefreshLayout refresh_root;
//    @InjectView(R.id.mainListView)
//    ListViewForRefresh mainListView;

    /**
     *  原HomeHeaderTwoFragment,整合到一个页面
     */
    private RecyclerView recycle;
    private ListViewForScrollView listBenyue;
    private ListViewForScrollView listRenqi;
    private TextView seeMore;
    private TextView xinpin;
    private LinearLayout llNewProduct;
    private TextView benyue;
    private TextView renqi;
    private LinearLayout llRenqi;
    private TextView zhuanti;
    private LinearLayout llZhuanTi;
    private View viewBehaveZhuanTi;
    private LinearLayout mianLayout;
    private ListViewForScrollView listZhuanti;
    private TextView remai;
    private LinearLayout llHotSell;
    private View viewBlowHot;
    private ListViewForScrollView listremai;
    private LinearLayout mainBg;

    /**
     *  限时购
     */
    private TextView hour;
    private TextView minute;
    private TextView second;
    private RelativeLayout rlMore;
    private RecyclerView madrush_recycle;
    private LinearLayout llTime;
    HomeThreeBO threeBO;
    NewHomeBO newHomeBO;


    /**
     * 头部布局
     */
    XBanner banner;
    private ImageView bannerBackImg;
    ImageView videoImgTitle;
    private RelativeLayout iconLayout;
//    LinearLayout videoLayout;
//    StandardGSYVideoPlayer detailPlayer;

    private boolean isPlay;
//    private OrientationUtils orientationUtils;

    HomeOneBO oneBO;
    List<ShopBO> shopBOs = new ArrayList<>();
    private boolean isScooll = false;   //表示列表是否正在滑动,默认为否
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    private RecyclerView oneuirecycle;
    private ImageView iconBackImg;
    private LinearLayout videotitlebg;
    private MadrushListBean berserkList;//限时购商品list
    private List<MadrushListBean.GrabDataBean.ProductBean> berserkListTime = new ArrayList<>();//限时购时间
    private MadrushListAdapter madrushListAdapter;
    private LinearLayout buyLayout;//有人购买tv商品的布局
    private com.haoyigou.hyg.view.circlephoto.RoundImageView buyHeader;//购买人头像
    private MarqueeView buyPerson;//购买人信息
    View view;

    /** 新改版首页头部布局数据　**/
    private LinearLayout new_mian_layout;
    private LinearLayout llAfterMonthSell;//第一部分布局
    private ImageView ivAfterMonthSell;//第一部分图片
    private ListViewForScrollView listAfterMonthSell;//第一部分列表
    private ImageView ivHotSell;//第二部分图片
    private LinearLayout llNewThree;//第三部分(同原热卖推荐)
    private ListViewForScrollView list_new_three;//第三部分列表(同原热卖推荐)
    private LinearLayout hotBg;
    private ImageView ivHotFour;//第四部分图片
    private LinearLayout llFour;
    private ListViewForScrollView list_bao;//第四部分列表(爆款专区)
    private List<NewHomeBO.HotTabBean.ProductsBean> newHomeFourData = new ArrayList<>();
    private List<NewProdectBO> prodectData = new ArrayList<>();
    private String fabUrl = "";//悬浮按钮跳转链接

    /** 拼团布局 */
    private RelativeLayout rlTvMore;
    private RelativeLayout rlTop;
    private LinearLayout groupLayout;
    private LinearLayout groupBackLayout;
    private ImageView groupProductOne;//第一个拼团商品
    private TextView oneGroupProductPrice;//价格
    private LinearLayout twoGroupBtn;
    private ImageView groupProductTwoImg;//第二个拼团商品
    private TextView groupProductTwoDownPrice;//第二个拼团商品省下的价格
    private TextView groupProductTwoPrice;//第二个商品单买价格
    private TextView twoGroupProductGPrice;//第二个商品限时拼团价格
    private TextView twoGroupProductSell;//第二个商品已拼件数
    private ImageView groupProductThreeImg;//第三个拼团商品
    private LinearLayout threeGroupBtn;
    private TextView groupProductThreeDownPrice;//第三个拼团商品省下的价格
    private TextView groupProductThreePrice;//第三个商品单买价格
    private TextView threeGroupProductGPrice;//第三个商品限时拼团价格
    private TextView threeGroupProductSell;//第三个商品已拼件数
    private RelativeLayout allGroupProduct;//查看全部


    //第一个布局,图片 + 三个横向图片布局
    private LinearLayout firstLayout;
    private ImageView firstLayoutImg;
    private RecyclerView firstRecyclerView;

    /** 每日上新list */
    private RecyclerView dayNewRecycle;
    private CommonAdapter<MadrushListBean.DayNewProductListBean> dayNewAdapter;

    /**
     * 视频部分
     */
    private RelativeLayout llVideo;//视频头布局
    private RelativeLayout videoBackLayout;

    private HotProductAdapter hotProductAdapter;

    /** TV直播 */
    private TextView seeAllTvShow;//查看更多
    private LinearLayout tvShowLayout;
    private LinearLayout tvShowBackLayout;
    private RoundImageView3 firstTvProductImg;//第一个TV商品
    private TextView firstTvProductTime;//第一个TV商品播放时间
    private TextView firstTvProductName;//第一个TV商品名字
    private TextView firstTvProductPrice;//第一个TV商品价格
    private RoundImageView3 secondTvProductImg;//第二个TV商品
    private TextView secondTvProductTime;//第二个TV商品播放时间
    private TextView secondTvProductName;//第二个TV商品名字
    private TextView secondTvProductPrice;//第二个TV价格
    private RoundImageView3 thirdTvProductImg;//第三个TV商品
    private TextView thirdTvProductTime;//第三个TV商品播放时间
    private TextView thirdTvProductName;//第三个TV商品名字
    private TextView thirdTvProductPrice;//第三个TV价格
    private RelativeLayout imgAndTime1;
    private RelativeLayout imgAndTime2;
    private RelativeLayout imgAndTime3;



    private RelativeLayout rlMoreShow;//查看更多

    List<Marquee> marqueesList = new ArrayList<>();//跑马灯消息


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_home, null,false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        invitionSwipeRefresh(refreshRoot);
        CallBackUtils.setCallBack(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                loadHomeOne();
                loadNewHome();//新改版首页三部分
                loadFengQiang(1);//限时购
                getHuiProduct();//1元爆枪款
                EventBus.getDefault().post(oneBO);
//                getNewShop();//新改版首页单品
                getVideoShop();
                Jzvd.releaseAllVideos();
            }
        });

        refreshLayout.setRefreshHeader(new SmartHeader(getActivity()));
        refreshLayout.setHeaderHeight(60);
        addHeaderLayout();
        setListener();
        loadHomeOne();
//        getNewShop();
        getVideoShop();
        isHasPromotion();
        //原HomeHeaderTwoFragment,整合到一个页面
        initvion();
        loadNewHome();//新版第一部分数据
        setIM("@TGS#aUD6HBJGZ");//设置IM
        if (SPUtils.getInstance("code").getString("codeNo") == null ||
                !SPUtils.getInstance("code").getString("codeNo").equals(Util.getAppVersionName(getActivity()))){
//            infoDialog();
        }
    }

    /**
     * 1元爆抢款
     */
    private void getHuiProduct() {
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.HUIPRODUCT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    HuiProductBean huiProductBean = JSON.parseObject(result.getData(),HuiProductBean.class);
                    if (huiProductBean != null){
                        firstLayout.setVisibility(View.VISIBLE);
                        setHuiProductUI(huiProductBean);
                    }else {
                        firstLayout.setVisibility(View.GONE);
                    }
                } else {
                    showToast(result.getMessage());
                }
            }
        }, getActivity());
    }

    /**
     * 设置1元爆抢布局
     */
    private void setHuiProductUI(HuiProductBean huiProductBean){
        if (huiProductBean.getList().size() == 0){
            firstRecyclerView.setVisibility(View.GONE);
        }else {
            firstRecyclerView.setVisibility(View.VISIBLE);
        }
        if (huiProductBean.getPageSetting().getDoubelElevenSetting() != null){
            if (huiProductBean.getPageSetting().getDoubelElevenSetting().getColor() != null &&
                    !huiProductBean.getPageSetting().getDoubelElevenSetting().getColor().equals("")){
                if (huiProductBean.getPageSetting().getDoubelElevenSetting().getColor().startsWith("#")) {
                    firstLayout.setBackgroundColor(Color.parseColor(huiProductBean.getPageSetting().getDoubelElevenSetting().getColor()));
                }else {
                    firstLayout.setBackgroundColor(Color.parseColor("#"+huiProductBean.getPageSetting().getDoubelElevenSetting().getColor()));
                }
            }
        }
        if (huiProductBean.getBackImg() != null && !huiProductBean.getBackImg().equals("")){
            firstLayoutImg.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(huiProductBean.getBackImg()).asBitmap().into(firstLayoutImg);
        }else {
            firstLayoutImg.setVisibility(View.GONE);
        }
        firstLayoutImg.setOnClickListener(v -> {//图片跳转
            if (huiProductBean.getBackImgLink() != null
                    && huiProductBean.getBackImgLink().length()>8){
                Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                if (huiProductBean.getBackImgLink().contains("http")) {
                    intent.putExtra("all",true);
                }else {
                    intent.putExtra("all",false);
                }
                intent.putExtra("url", huiProductBean.getBackImgLink());
                startActivity(intent);
            }
        });
        setHuiAdapter(huiProductBean.getList());

    }

    /**
     * 设置1元爆枪列表
     * @param data
     */
    private void setHuiAdapter(List<HuiProductBean.ListBean> data) {
        CommonAdapter<HuiProductBean.ListBean>  adapter =
                new CommonAdapter<HuiProductBean.ListBean>(getActivity(),R.layout.single_img_layout,data) {
            @Override
            protected void convert(ViewHolder holder, HuiProductBean.ListBean data, int position) {
                ImageView image = holder.getConvertView().findViewById(R.id.image);
                ViewGroup.LayoutParams params = image.getLayoutParams();
                params.width = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.29);
                params.height = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.35);
                image.setLayoutParams(params);
                Glide.with(getActivity()).load(data.getItemImg()).into(image);
                image.setOnClickListener(v -> {
                    if (data.getItemImgLink() != null && data.getItemImgLink().length()>8){
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        if (data.getItemImgLink().contains("http")) {
                            intent.putExtra("all",true);
                        }else {
                            intent.putExtra("all",false);
                        }
                        intent.putExtra("url", data.getItemImgLink());
                        startActivity(intent);
                    }
                });

            }
        };
        firstRecyclerView.setAdapter(adapter);
    }

    /**
     * 限时购  每日上新  拼团  TV商品
     */
    private void loadFengQiang(final int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", type + "");
        HttpClient.post(HttpClient.HOME_TWO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    berserkList = JSONArray.parseObject(result.getData(), MadrushListBean.class);
                    if (berserkListTime != null){
                        berserkListTime.clear();
                    }
                    for (int i=0;i<berserkList.getGrabData().size();i++) {
                        berserkListTime.addAll(berserkList.getGrabData().get(i).getProduct());
                    }
                    if (berserkList.getGrabData().size() == 0){
//                        llTime.setVisibility(View.GONE);
                    }else {
                        llTime.setVisibility(View.VISIBLE);
                        setBannerdAdapter(type);
                    }
                    setTuanUI();
                } else {
                    showToast(result.getMessage());
                }
            }
        }, getActivity());
    }

    /**
     * 设置首页拼团和TV直播那一小块的布局
     */
    private void setTuanUI(){
        //每日上新
        if (berserkList.getDayNewProductList() != null && berserkList.getDayNewProductList().size()>0){
            setDayNewAdapter(berserkList.getDayNewProductList());
        }
        //拼团
        if (berserkList.getGroupBuy() != null && berserkList.getGroupBuy().size()>0){
            groupBackLayout.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(berserkList.getGroupBuy().get(0).getPic()).asBitmap().into(groupProductOne);
            oneGroupProductPrice.setText(berserkList.getGroupBuy().get(0).getDisprice());

            if (berserkList.getPageSetting().getGroupBuySetting() != null){
                if (berserkList.getPageSetting().getGroupBuySetting().getColor() != null &&
                     !berserkList.getPageSetting().getGroupBuySetting().getColor().equals("")){
                    if (berserkList.getPageSetting().getGroupBuySetting().getColor().startsWith("#")){
                        groupBackLayout.setBackgroundColor(Color.parseColor(berserkList.getPageSetting().getGroupBuySetting().getColor()));
                    }else {
                        groupBackLayout.setBackgroundColor(Color.parseColor("#"+berserkList.getPageSetting().getGroupBuySetting().getColor()));
                    }
                }
            }
            if (berserkList.getGroupBuy().size() > 1) {
                Glide.with(getActivity()).load(berserkList.getGroupBuy().get(1).getPic()).asBitmap().into(groupProductTwoImg);
                groupProductTwoDownPrice.setText(berserkList.getGroupBuy().get(1).getDiffer());
                groupProductTwoPrice.setText(String.format("￥%s", berserkList.getGroupBuy().get(1).getPrice()));
                twoGroupProductGPrice.setText(berserkList.getGroupBuy().get(1).getDisprice());
                twoGroupProductSell.setText(String.format("已拼%s件",berserkList.getGroupBuy().get(1).getCoaststore()));
            }

            if (berserkList.getGroupBuy().size() > 2) {
                Glide.with(getActivity()).load(berserkList.getGroupBuy().get(2).getPic()).asBitmap().into(groupProductThreeImg);
                groupProductThreeDownPrice.setText(berserkList.getGroupBuy().get(2).getDiffer());
                groupProductThreePrice.setText(String.format("￥%s", berserkList.getGroupBuy().get(2).getPrice()));
                threeGroupProductGPrice.setText(berserkList.getGroupBuy().get(2).getDisprice());
                threeGroupProductSell.setText(String.format("已拼%s件",berserkList.getGroupBuy().get(2).getCoaststore()));
            }
            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
            groupProductOne.setOnClickListener(v -> {//第一个拼团商品
                if (berserkList.getGroupBuy().get(0).getJumpUrl().contains("http")) {
                    intent.putExtra("all",true);
                }else {
                    intent.putExtra("all",false);
                }
                intent.putExtra("url", berserkList.getGroupBuy().get(0).getJumpUrl());
                startActivity(intent);
            });

            twoGroupBtn.setOnClickListener(v -> {//第二个拼团商品
                if (berserkList.getGroupBuy().size() < 2){
                    return;
                }
                if (berserkList.getGroupBuy().get(1).getJumpUrl().contains("http")) {
                    intent.putExtra("all",true);
                }else {
                    intent.putExtra("all",false);
                }
                intent.putExtra("url", berserkList.getGroupBuy().get(1).getJumpUrl());
                startActivity(intent);
            });
            threeGroupBtn.setOnClickListener(v -> {//第三个拼团商品
                if (berserkList.getGroupBuy().size() < 3){
                    return;
                }
                if (berserkList.getGroupBuy().get(2).getJumpUrl().contains("http")) {
                    intent.putExtra("all",true);
                }else {
                    intent.putExtra("all",false);
                }
                intent.putExtra("url", berserkList.getGroupBuy().get(2).getJumpUrl());
                startActivity(intent);
            });
        }else {
            groupBackLayout.setVisibility(View.GONE);
        }
        //TV直播
        if (berserkList.getTvData() != null && berserkList.getTvData().size()>0){
            tvShowBackLayout.setVisibility(View.VISIBLE);
            if (berserkList.getPageSetting() != null){//设置背景色
                if (berserkList.getPageSetting().getTvSetting() != null
                        && berserkList.getPageSetting().getTvSetting().getColor()!= null
                        && !berserkList.getPageSetting().getTvSetting().getColor().equals("")){
                    if (berserkList.getPageSetting().getTvSetting().getColor().startsWith("#")){
                        tvShowBackLayout.setBackgroundColor(Color.parseColor(berserkList.getPageSetting().getTvSetting().getColor()));
                    }else {
                        tvShowBackLayout.setBackgroundColor(Color.parseColor("#"+berserkList.getPageSetting().getTvSetting().getColor()));
                    }
                }
            }
            Glide.with(getActivity()).load(berserkList.getTvData().get(0).getPic()).asBitmap().into(firstTvProductImg);
            firstTvProductTime.setText(berserkList.getTvData().get(0).getStartTime());
            firstTvProductName.setText(berserkList.getTvData().get(0).getName());
            if (berserkList.getTvData().get(0).getIsPro().equals("0")){
                firstTvProductPrice.setText("电话订购");
            }else {
                firstTvProductPrice.setText(String.format("￥%s", berserkList.getTvData().get(0).getDisprice()));
            }

            if (berserkList.getTvData().size() > 1){
                Glide.with(getActivity()).load(berserkList.getTvData().get(1).getPic()).asBitmap().into(secondTvProductImg);
                secondTvProductTime.setText(berserkList.getTvData().get(1).getStartTime());
                secondTvProductName.setText(berserkList.getTvData().get(1).getName());
                if (berserkList.getTvData().get(1).getIsPro().equals("0")){
                    secondTvProductPrice.setText("电话订购");
                }else {
                    secondTvProductPrice.setText(String.format("￥%s", berserkList.getTvData().get(1).getDisprice()));
                }
            }

            if (berserkList.getTvData().size() > 2){
                Glide.with(getActivity()).load(berserkList.getTvData().get(2).getPic()).asBitmap().into(thirdTvProductImg);
                thirdTvProductTime.setText(berserkList.getTvData().get(2).getStartTime());
                thirdTvProductName.setText(berserkList.getTvData().get(2).getName());
                if (berserkList.getTvData().get(2).getIsPro().equals("0")){
                    thirdTvProductPrice.setText("电话订购");
                }else {
                    thirdTvProductPrice.setText(String.format("￥%s", berserkList.getTvData().get(2).getDisprice()));
                }
            }

            firstTvProductImg.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), NewTVActivity.class));
            });

            secondTvProductImg.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), NewTVActivity.class));
            });

            thirdTvProductImg.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), NewTVActivity.class));
            });
        }else {
            tvShowBackLayout.setVisibility(View.GONE);
        }

        //设置勾选推荐背景色
        if (berserkList.getPageSetting() != null && berserkList.getPageSetting().getDayNewSetting() != null
                && berserkList.getPageSetting().getDayNewSetting().getColor() != null
                && !berserkList.getPageSetting().getDayNewSetting().getColor().equals("") ){
            if (berserkList.getPageSetting().getDayNewSetting().getColor().startsWith("#")) {
                gridView.setBackgroundColor(Color.parseColor(berserkList.getPageSetting().getDayNewSetting().getColor()));
                videoBackLayout.setBackgroundColor(Color.parseColor(berserkList.getPageSetting().getDayNewSetting().getColor()));
            }else {
                gridView.setBackgroundColor(Color.parseColor("#"+berserkList.getPageSetting().getDayNewSetting().getColor()));
                videoBackLayout.setBackgroundColor(Color.parseColor("#"+berserkList.getPageSetting().getDayNewSetting().getColor()));
            }
        }else {
            gridView.setBackgroundColor(Color.parseColor("#FFC82A3A"));
            videoBackLayout.setBackgroundColor(Color.parseColor("#FFC82A3A"));
        }
    }


    /**
     * 设置每日上新列表
     */
    private void setDayNewAdapter(List<MadrushListBean.DayNewProductListBean> data){
        if (dayNewAdapter != null){
            dayNewAdapter.notifyDataSetChanged();
            return;
        }
        dayNewAdapter = new CommonAdapter<MadrushListBean.DayNewProductListBean>(getActivity(),R.layout.day_new_item_layout,data) {
            @Override
            protected void convert(ViewHolder holder, MadrushListBean.DayNewProductListBean dayNewProductListBean, int position) {
                RoundImageView imageView = holder.getConvertView().findViewById(R.id.image);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.19);
                params.height = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.19);
                imageView.setLayoutParams(params);
                Glide.with(getActivity()).load(dayNewProductListBean.getPiclogo()).asBitmap().into(imageView);
                holder.setText(R.id.price,String.format("上新价￥%s",dayNewProductListBean.getDisprice()));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        if (dayNewProductListBean.getJumpAdress().contains("http")) {
                            intent.putExtra("all",true);
                        }else {
                            intent.putExtra("all",false);
                        }
                        intent.putExtra("url", dayNewProductListBean.getJumpAdress());
                        startActivity(intent);
                    }
                });
            }
        };
        dayNewRecycle.setAdapter(dayNewAdapter);
    }


    /**
     * 设置疯抢列表适配器
     */
    private void setBannerdAdapter(int i) {
        TimeCountUtils utils = TimeCountUtils.getInstance();
        utils.setOnTimeConuntListener(new TimeCountUtils.onTimeConunt() {
            @Override
            public void onProgress() {
                if (getActivity() != null)
                    refreshChild();
            }
        });
        if (berserkList != null && berserkList.getGrabData().size()>0) {
            madrushListAdapter = new MadrushListAdapter(getActivity(), berserkListTime);
            madrush_recycle.setAdapter(madrushListAdapter);
            madrushListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 定时器计算并给每一行更新数据
     */
    private void refreshChild() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               if (berserkList != null && berserkList.getGrabData().size()>0) {
                   Long[] time = new Long[0];
                   for (int i=0;i<berserkList.getGrabData().size();i++){
                       if (berserkList.getGrabData().get(i).getProduct() != null &&
                               berserkList.getGrabData().get(i).getProduct().size()>0){
                           time = setTime(berserkList.getGrabData().get(i).getTime());
                           if (time == null) {
                               berserkList.getGrabData().remove(0);
                               setBannerdAdapter(1);
                           } else {
                               hour.setText(time[0] + "");
                               minute.setText(time[1] + "");
                               second.setText(time[2] + "");
                           }
                           break;
                       }
                   }
               }
            }
        });
    }

    public Long[] setTime(long timeMinitute) {
        long nowtime = new Date().getTime();
        long dis = timeMinitute - nowtime;
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
    /**
     * 初始化界面,以下开始到420行为原HomeHeaderTwoFragment界面内容
     */
    private void initvion() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycle.setLayoutManager(manager);

        listBenyue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtils.isEmpty(threeBO.getTabs().get(position).getUrl())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("productTabId", "" + threeBO.getTabs().get(position).getId());
                    goToActivity(LabelActivity.class, bundle, false);
                } else {
                    if (threeBO.getTabs().get(position).getUrl().startsWith("/pages")) {
                        gotoMini(threeBO.getTabs().get(position).getUrl());
                    }else {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", threeBO.getTabs().get(position).getUrl());
                        intent.putExtra("all", true);
                        startActivity(intent);
                    }
                }
            }
        });
        seeMore.setOnClickListener(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HomeOneBO oneBO) {   //是否进入首页

//        loadHomeTwoData();
        loadNewHome();
    }

    /**
     * 获取新改版首页数据
     */
    private void loadNewHome(){
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.NEW_HOME, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSONObject.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    if (mianLayout == null) return;
                    mianLayout.setVisibility(View.VISIBLE);
                    newHomeBO = JSONObject.parseObject(result.getData(), NewHomeBO.class);
                    setNewUiOne();
                    setNewUiTwo();
                    setNewUiThree();
                    setNewUiFour();
                    setFAB();
                } else {
                    new_mian_layout.setVisibility(View.GONE);
                }
            }
        }, getActivity());
    }

    /**
     * 设置悬浮按钮
     */
    private void setFAB(){
        if (newHomeBO.getPendant() != null) {
            fab.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(newHomeBO.getPendant().getPic()).into(fab);
            fabUrl = newHomeBO.getPendant().getJumpUrl();
        }else {
            fab.setVisibility(View.GONE);
        }
    }

    /**
     * 获取积分,券
     */
    private void getPoint(String id){
        Map<String, Object> params = new HashMap<>();
        params.put("id",id);
        HttpClient.post(HttpClient.GET_POINT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSONObject.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    ToastUtils.showToast(getActivity(),result.getMessage());
                } else {
                    ToastUtils.showToast(getActivity(),result.getMessage());
                }
                if (fabUrl == null || fabUrl.length() < 10) return;
                if (fabUrl.startsWith("/pages")){
                    gotoMini(fabUrl);
                }else {
                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                    intent.putExtra("url", fabUrl);
                    intent.putExtra("all", true);
                    intent.putExtra("isTitle", true);
                    startActivity(intent);
                }
            }
        }, getActivity());
    }
    /**
     * 设置新改版首页第一部分数据
     */
    private void setNewUiOne() {
        if (newHomeBO.getUpper_recommend() != null && newHomeBO.getUpper_recommend().getPic() != null
                && !newHomeBO.getUpper_recommend().getPic().equals("")) {
            ivAfterMonthSell.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(newHomeBO.getUpper_recommend().getPic()).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivAfterMonthSell);
            ivAfterMonthSell.setOnClickListener(v -> {
                String url = newHomeBO.getUpper_recommend().getJumpUrl();
                if (url != null && url.length() > 10){
                    if (url.startsWith("/pages")){
                        gotoMini(url);
                    }else {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", url);
                        intent.putExtra("all", true);
                        intent.putExtra("isTitle", true);
                        startActivity(intent);
                    }
                }
            });
        }else {
            ivAfterMonthSell.setVisibility(View.GONE);
        }
        if (newHomeBO.getUpper_recommend() != null && newHomeBO.getUpper_recommend().getItems() != null &&
                newHomeBO.getUpper_recommend().getItems().size() > 0) {
            listAfterMonthSell.setVisibility(View.VISIBLE);
            NewHomeAdapter adapter1 = new NewHomeAdapter(getActivity(), disId,
                    newHomeBO.getUpper_recommend().getItems(),newHomeBO.getUpper_recommend().getColor());
            listAfterMonthSell.setAdapter(adapter1);
        }else {
            listAfterMonthSell.setVisibility(View.GONE);
        }
    }
    /**
     * 设置新改版首页第二部分图片
     */
    private void setNewUiTwo() {
        if (newHomeBO.getNewPeopleTab() != null && newHomeBO.getNewPeopleTab().getPic() != null) {
            ivHotSell.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(newHomeBO.getNewPeopleTab().getPic()).into(ivHotSell);
            ivHotSell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = newHomeBO.getNewPeopleTab().getJumpUrl();
                    if (url == null || url.length() < 10){
                        if (url.startsWith("/pages")){
                            gotoMini(url);
                        }else {
                            Intent intent3 = new Intent(getActivity(), LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", String.valueOf(newHomeBO.getNewPeopleTab().getTabId()));
                            intent3.putExtras(bundle);
                            startActivity(intent3);
                        }
                    }else {
                        if (url.startsWith("/pages")){
                            gotoMini(url);
                        }else {
                            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                            intent.putExtra("url", url);
                            intent.putExtra("all", true);
                            intent.putExtra("isTitle", true);
                            startActivity(intent);
                        }
                    }
                }
            });
        }else {
            ivHotSell.setVisibility(View.GONE);
        }
    }
    /**
     * 设置新改版首页第三部分图片,原热卖推荐
     */
    private void setNewUiThree() {
        if (newHomeBO.getRecommend() != null
                && newHomeBO.getRecommend().getColor() != null
                && !newHomeBO.getRecommend().getColor().equals("")){
            if (newHomeBO.getRecommend().getColor().startsWith("#")){
                hotBg.setBackgroundColor(Color.parseColor(newHomeBO.getRecommend().getColor()));
            }else {
                hotBg.setBackgroundColor(Color.parseColor("#"+newHomeBO.getRecommend().getColor()));
            }
        }
        if (newHomeBO.getRecommend() != null && newHomeBO.getRecommend().getItems() != null &&
                newHomeBO.getRecommend().getItems().size() > 0) {
            llNewThree.setVisibility(View.VISIBLE);
            NewHomeThreeAdapter adapter3 = new NewHomeThreeAdapter(getActivity(), disId, newHomeBO.getRecommend().getItems());
            list_new_three.setAdapter(adapter3);
        }else {
            llNewThree.setVisibility(View.GONE);
        }
    }

    /**
     * 设置新改版首页第四部分数据
     */
    private void setNewUiFour() {
        if (newHomeBO.getHotTab().getPic() != null) {
            ivHotFour.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(newHomeBO.getHotTab().getPic()).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivHotFour);
        }else {
            ivHotFour.setVisibility(View.GONE);
        }
        if (newHomeBO.getHotTab() != null && newHomeBO.getHotTab().getProducts() != null &&
                newHomeBO.getHotTab().getProducts().size() > 0) {
            llFour.setVisibility(View.VISIBLE);
            ivHotFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MApplication.labelParentLocation = "109";
                    Intent intent3 = new Intent(getActivity(), LabelActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("productTabId", newHomeBO.getHotTab().getTabid());
                    intent3.putExtras(bundle);
                    startActivity(intent3);
                }
            });
            newHomeFourData.clear();
            newHomeFourData.addAll(newHomeBO.getHotTab().getProducts());
            if (newHomeBO.getHotTab().getColor().contains("#")) {
                llFour.setBackgroundColor(Color.parseColor(newHomeBO.getHotTab().getColor()));
                mainBg.setBackgroundColor(Color.parseColor(newHomeBO.getHotTab().getColor()));
            }else {
                llFour.setBackgroundColor(Color.parseColor("#"+newHomeBO.getHotTab().getColor()));
                mainBg.setBackgroundColor(Color.parseColor("#"+newHomeBO.getHotTab().getColor()));
            }
            setNewUiFourAdapter();
        }else {
            llFour.setVisibility(View.GONE);
        }
    }

    private void setNewUiFourAdapter(){
        com.zhy.adapter.abslistview.CommonAdapter<NewHomeBO.HotTabBean.ProductsBean> commonAdapter = new
                com.zhy.adapter.abslistview.CommonAdapter<NewHomeBO.HotTabBean.ProductsBean>(getActivity(),R.layout.bao_item,newHomeFourData) {
                    @Override
                    protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, final NewHomeBO.HotTabBean.ProductsBean item, int position) {

                        ViewGroup.LayoutParams params21 = holder.getView(R.id.titleImg).getLayoutParams();
                        params21.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.45);
                        params21.width =(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.45);
                        holder.getView(R.id.titleImg).setLayoutParams(params21);

                        ViewGroup.LayoutParams params22 = holder.getView(R.id.titleImg2).getLayoutParams();
                        params22.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.45);
                        params22.width =(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.45);
                        holder.getView(R.id.titleImg2).setLayoutParams(params22);

                        Glide.with(getActivity()).load(item.getProductCase())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .override((int)(DisplayUtil.getMobileWidth(getActivity()) * 0.45),(int)(DisplayUtil.getMobileWidth(getActivity()) * 0.45))
                                .into((ImageView)holder.getView(R.id.titleImg));
                        Glide.with(getActivity()).load(item.getPiclogo())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .override((int)(DisplayUtil.getMobileWidth(getActivity()) * 0.45),(int)(DisplayUtil.getMobileWidth(getActivity()) * 0.45)).into((ImageView)holder.getView(R.id.titleImg2));
                        holder.setText(R.id.prodectTitle,item.getName());
                        holder.setText(R.id.prodectDes,item.getSecondTitle());
                        holder.setText(R.id.nowPrice,String.format("¥%s",item.getDisprice()));
                        TextView oldPrice = holder.getConvertView().findViewById(R.id.oldPrice);
                        oldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                        oldPrice.setText(String.format("¥%s",item.getPrice()));
                        if (item.getOtherDiscontent() != null && !item.getOtherDiscontent().equals("")){
                            holder.getView(R.id.rlActivity).setVisibility(View.VISIBLE);
                            holder.setText(R.id.tvCount,item.getOtherDiscontent());
                        }else {
                            holder.getView(R.id.rlActivity).setVisibility(View.GONE);
                        }
                        if (item.getCanUseCoupon() == 0){
                            holder.getView(R.id.rlTwo).setVisibility(View.GONE);
                        }else {
                            holder.getView(R.id.rlTwo).setVisibility(View.VISIBLE);
                        }
                        if (item.getDiscontent() != null && !item.getDiscontent().equals("")){
                            holder.getView(R.id.rlActivity2).setVisibility(View.VISIBLE);
                            holder.setText(R.id.tvCount2,item.getDiscontent());
                        }else {
                            holder.getView(R.id.rlActivity2).setVisibility(View.GONE);
                        }
                        holder.getView(R.id.buy).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = item.getJumpAdress();
                                if (url == null || url.length() < 10) return;
                                if (url.startsWith("/pages")){
                                    gotoMini(url);
                                }else {
                                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                                    intent.putExtra("url", url);
                                    intent.putExtra("all", true);
                                    intent.putExtra("isTitle", true);
                                    startActivity(intent);
                                }
                            }
                        });
                        holder.getView(R.id.llBack).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = item.getJumpAdress();
                                if (url == null || url.length() < 10) return;
                                if (url.startsWith("/pages")){
                                    gotoMini(url);
                                }else {
                                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                                    intent.putExtra("url", url);
                                    intent.putExtra("all", true);
                                    intent.putExtra("isTitle", true);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                };
        list_bao.setAdapter(commonAdapter);
    }

    /**
     * 初始化下拉刷新控件
     */
    protected void invitionSwipeRefresh(SwipeRefreshLayout mSwipeLayout) {
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
    }


    /**
     * 为商品列表添加头部布局
     */
    private void addHeaderLayout() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fra_home_header, null);
        rlTvMore = view.findViewById(R.id.rlTvMore);
        rlTop = view.findViewById(R.id.rlTop);
        recycle = view.findViewById(R.id.recycle);
        listBenyue = view.findViewById(R.id.list_benyue);
        listRenqi = view.findViewById(R.id.list_renqi);
        seeMore = view.findViewById(R.id.see_more);
        xinpin = view.findViewById(R.id.xinpin);
        llNewProduct = view.findViewById(R.id.llNewProduct);
        benyue = view.findViewById(R.id.benyue);
        renqi = view.findViewById(R.id.renqi);
        llRenqi = view.findViewById(R.id.llRenqi);
        zhuanti = view.findViewById(R.id.zhuanti);
        llZhuanTi = view.findViewById(R.id.llZhuanTi);
        viewBehaveZhuanTi =  view.findViewById(R.id.viewBehaveZhuanTi);
        mianLayout = view.findViewById(R.id.mian_layout);
        listZhuanti = view.findViewById(R.id.list_zhuanti);
        remai = view.findViewById(R.id.remai);
        llHotSell = view.findViewById(R.id.llHotSell);
        viewBlowHot = view.findViewById(R.id.viewBlowHot);
        listremai = view.findViewById(R.id.list_remai);
        mainBg = view.findViewById(R.id.mainBg);

        llVideo = view.findViewById(R.id.llVideo);
        videoBackLayout = view.findViewById(R.id.videoBackLayout);

        banner = view.findViewById(R.id.banner);
        bannerBackImg = view.findViewById(R.id.bannerBackImg);
        iconLayout = view.findViewById(R.id.iconLayout);

        ViewGroup.LayoutParams paramsIcon = iconLayout.getLayoutParams();
        paramsIcon.height = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.21);
        iconLayout.setLayoutParams(paramsIcon);


        videoImgTitle = view.findViewById(R.id.video_title_img);
        videotitlebg = view.findViewById(R.id.video_title_bg);
        oneuirecycle = view.findViewById(R.id.oneui_recycle);
        iconBackImg = view.findViewById(R.id.iconBackImg);
        oneuirecycle.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        oneuirecycle.setNestedScrollingEnabled(false);//禁止滑动
        buyLayout = view.findViewById(R.id.buyLayout);

        buyHeader = view.findViewById(R.id.buyHeader);
        buyPerson = view.findViewById(R.id.buyPerson);

        if (SPUtils.getInstance().getStringSet("maqueeData") != null &&
                SPUtils.getInstance().getStringSet("maqueeData").size()>0) {
//            buyLayout.setVisibility(View.VISIBLE);
            buyLayout.setVisibility(View.GONE);
            for (String txt : SPUtils.getInstance().getStringSet("maqueeData")) {
                List<String> listData = Arrays.asList(txt.split("&"));
                Marquee marquee = new Marquee(listData.get(2), listData.get(1));
                marqueesList.add(marquee);
            }
            buyPerson.setImage(true);
            buyPerson.startWithList(marqueesList);
        }else {
            buyLayout.setVisibility(View.GONE);
        }


        /** 限时购 */
        hour = view.findViewById(R.id.hour_text);
        minute = view.findViewById(R.id.minute_text);
        second = view.findViewById(R.id.second_text);
        rlMore = view.findViewById(R.id.rlMore);
        madrush_recycle = view.findViewById(R.id.madrush_recycle);
        llTime = view.findViewById(R.id.llTime);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView. HORIZONTAL);
        madrush_recycle.setLayoutManager(layoutManager);
        rlMore.setOnClickListener(this);

        dayNewRecycle = view.findViewById(R.id.dayNewRecycle);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(RecyclerView. HORIZONTAL);
        dayNewRecycle.setLayoutManager(layoutManager2);

        /** 新改版首页 **/
        new_mian_layout = view.findViewById(R.id.new_mian_layout);
        llAfterMonthSell = view.findViewById(R.id.llAfterMonthSell);
        ivAfterMonthSell = view.findViewById(R.id.ivAfterMonthSell);

        /** TV直播 */
        seeAllTvShow = view.findViewById(R.id.seeAllTvShow);
        tvShowLayout = view.findViewById(R.id.tvShowLayout);
        tvShowBackLayout = view.findViewById(R.id.tvShowBackLayout);
        firstTvProductImg = view.findViewById(R.id.firstTvProductImg);
        firstTvProductTime = view.findViewById(R.id.firstTvProductTime);
        firstTvProductName = view.findViewById(R.id.firstTvProductName);
        firstTvProductPrice = view.findViewById(R.id.firstTvProductPrice);
        secondTvProductImg = view.findViewById(R.id.secondTvProductImg);
        secondTvProductTime = view.findViewById(R.id.secondTvProductTime);
        secondTvProductName = view.findViewById(R.id.secondTvProductName);
        secondTvProductPrice = view.findViewById(R.id.secondTvProductPrice);
        thirdTvProductImg = view.findViewById(R.id.thirdTvProductImg);
        thirdTvProductTime = view.findViewById(R.id.thirdTvProductTime);
        thirdTvProductName = view.findViewById(R.id.thirdTvProductName);
        thirdTvProductPrice = view.findViewById(R.id.thirdTvProductPrice);
        imgAndTime1 = view.findViewById(R.id.imgAndTime1);
        imgAndTime2 = view.findViewById(R.id.imgAndTime2);
        imgAndTime3 = view.findViewById(R.id.imgAndTime3);

        ViewGroup.LayoutParams params11 = imgAndTime1.getLayoutParams();
        params11.width = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.3);
        params11.height = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.3);
        imgAndTime1.setLayoutParams(params11);

        ViewGroup.LayoutParams params22 = imgAndTime2.getLayoutParams();
        params22.width = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.3);
        params22.height = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.3);
        imgAndTime2.setLayoutParams(params22);

        ViewGroup.LayoutParams params33 = imgAndTime3.getLayoutParams();
        params33.width = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.3);
        params33.height = (int)(ScreenUtils.getScreenWidth(getActivity()) * 0.3);
        imgAndTime3.setLayoutParams(params33);

        rlMoreShow = view.findViewById(R.id.rlMoreShow);
        firstLayout = view.findViewById(R.id.firstLayout);
        firstLayoutImg = view.findViewById(R.id.firstLayoutImg);
        firstRecyclerView = view.findViewById(R.id.firstRecyclerView);
        firstRecyclerView.setNestedScrollingEnabled(false);


        /** 拼团 */
        groupProductOne = view.findViewById(R.id.groupProductOne);
        groupLayout = view.findViewById(R.id.groupLayout);
        groupBackLayout = view.findViewById(R.id.groupBackLayout);
        oneGroupProductPrice = view.findViewById(R.id.oneGroupProductPrice);
        twoGroupBtn = view.findViewById(R.id.twoGroupBtn);
        threeGroupBtn = view.findViewById(R.id.threeGroupBtn);
        groupProductTwoImg = view.findViewById(R.id.groupProductTwoImg);
        groupProductTwoDownPrice = view.findViewById(R.id.groupProductTwoDownPrice);
        groupProductTwoPrice = view.findViewById(R.id.groupProductTwoPrice);
        twoGroupProductGPrice = view.findViewById(R.id.twoGroupProductGPrice);
        twoGroupProductSell = view.findViewById(R.id.twoGroupProductSell);
        groupProductThreeImg = view.findViewById(R.id.groupProductThreeImg);
        groupProductThreeDownPrice = view.findViewById(R.id.groupProductThreeDownPrice);
        groupProductThreePrice = view.findViewById(R.id.groupProductThreePrice);
        threeGroupProductGPrice = view.findViewById(R.id.threeGroupProductGPrice);
        threeGroupProductSell = view.findViewById(R.id.threeGroupProductSell);
        allGroupProduct = view.findViewById(R.id.allGroupProduct);
        allGroupProduct.setOnClickListener(this);



        //设置图片高度
        ViewGroup.LayoutParams imagePar = firstLayoutImg.getLayoutParams();
        imagePar.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.37);
        firstLayoutImg.setLayoutParams(imagePar);

        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);
        firstRecyclerView.setLayoutManager(manager);

        seeAllTvShow.setOnClickListener(this);
        rlMoreShow.setOnClickListener(this);
        ViewGroup.LayoutParams params2 = ivAfterMonthSell.getLayoutParams();
        params2.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.37);
        params2.width =(int) (DisplayUtils.getScreenWidth(getActivity()));
        ivAfterMonthSell.setLayoutParams(params2);

        listAfterMonthSell = view.findViewById(R.id.listAfterMonthSell);
        ivHotSell = view.findViewById(R.id.ivHotSell);

        ViewGroup.LayoutParams params3 = ivHotSell.getLayoutParams();
        params3.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.37);
        params3.width =(int) (DisplayUtils.getScreenWidth(getActivity()));
        ivHotSell.setLayoutParams(params3);

        llNewThree = view.findViewById(R.id.llNewThree);
        list_new_three = view.findViewById(R.id.list_new_three);
        hotBg = view.findViewById(R.id.hotBg);
        ivHotFour = view.findViewById(R.id.ivHotFour);

        ViewGroup.LayoutParams params4 = ivHotFour.getLayoutParams();
        params4.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.37);
        params4.width =(int) (DisplayUtils.getScreenWidth(getActivity()));
        ivHotFour.setLayoutParams(params4);

        ViewGroup.LayoutParams params44 = videotitlebg.getLayoutParams();
        params44.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.37);
        params44.width =(int) (DisplayUtils.getScreenWidth(getActivity()));
        videotitlebg.setLayoutParams(params44);


        ViewGroup.LayoutParams params445 = videoImgTitle.getLayoutParams();
        params445.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.37);
        params445.width =(int) (DisplayUtils.getScreenWidth(getActivity()));
        videoImgTitle.setLayoutParams(params445);


        list_bao = view.findViewById(R.id.list_bao);
        llFour = view.findViewById(R.id.llFour);

        ViewGroup.LayoutParams params1 = banner.getLayoutParams();
        params1.height = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.5);
        banner.setLayoutParams(params1);

        ViewGroup.LayoutParams paramsBg = bannerBackImg.getLayoutParams();
        paramsBg.height = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.5);
        bannerBackImg.setLayoutParams(paramsBg);


        ViewGroup.LayoutParams params6 = oneuirecycle.getLayoutParams();
        params6.height = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.24);
        oneuirecycle.setLayoutParams(params6);

        ViewGroup.LayoutParams paramsIconBg = iconBackImg.getLayoutParams();
        paramsIconBg.height = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.21);
        iconBackImg.setLayoutParams(paramsIconBg);


//        ViewGroup.LayoutParams paramsTop = rlTop.getLayoutParams();
//        paramsTop.height = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.62);
//        rlTop.setLayoutParams(paramsTop);

        refreshLayout.setNestedScrollingEnabled(false);
        gridView.addHeaderView(view);
    }


    /**
     * 设置监听
     */
    private void setListener() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //滚动到底部
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//                        getShop(true);
                    }
                    isScooll = false;
                } else {
                    isScooll = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (Jzvd.CURRENT_JZVD == null) return;
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                int currentPlayPosition = Jzvd.CURRENT_JZVD.positionInList;
                if (currentPlayPosition >= 0) {
                    if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                        if (Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                            Jzvd.releaseAllVideos();//为什么最后一个视频横屏会调用这个，其他地方不会
                        }
                    }
                }
            }
        });
        videoImgTitle.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
//        refreshRoot.setOnRefreshListener(this);  //设置下拉刷新监听
        topButton.setOnClickListener(this);
        rlTvMore.setOnClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newHomeBO.getPendant().getTakeDis() == 1){
                    if (StateMessage.IS_LOGIN) {    //判断是否登陆
                        getPoint(String.valueOf(newHomeBO.getPendant().getSettingId()));
                    }else {
                        goToActivity(LoginActivity.class,false);
                    }
                }else {
                    if (fabUrl == null || fabUrl.length() < 10) return;
                    if (fabUrl.startsWith("/pages")){
                        gotoMini(fabUrl);
                    }else {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", fabUrl);
                        intent.putExtra("all", true);
                        intent.putExtra("isTitle", true);
                        startActivity(intent);
                    }
                }

            }
        });
    }


    public int getScrollY() {
        View c = gridView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = gridView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }


    /**
     * 设置视频部分
     */
    private void setVideo() {
//        //外部辅助的旋转，帮助全屏
//        orientationUtils = new OrientationUtils(getActivity(), detailPlayer);
//        //初始化不打开外部的旋转
//        orientationUtils.setEnable(false);
//        ImageView imageView = new ImageView(getActivity());
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
//                , ViewGroup.LayoutParams.MATCH_PARENT);
//        imageView.setLayoutParams(params);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        Glide.with(getActivity()).load(oneBO.getVideo().getImg()).into(imageView);
//        detailPlayer.setThumbImageView(imageView);
//        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
//        gsyVideoOption.setIsTouchWiget(true)
//                .setRotateViewAuto(false)
//                .setLockLand(false)
//                .setShowFullAnimation(false)
//                .setNeedLockFull(true)
//                .setSeekRatio(1)
//                .setUrl(oneBO.getVideo().getUrl())
//                .setCacheWithPlay(false)
//                .setVideoTitle("介绍")
//                .setVideoAllCallBack(new SampleListener() {
//                    @Override
//                    public void onPrepared(String url, Object... objects) {
//                        super.onPrepared(url, objects);
//                        isPlay = true;
//                    }
//                })
//                .build(detailPlayer);
//        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), FullVideoActivity.class);
//                intent.putExtra("url", oneBO.getVideo().getUrl());
//                intent.putExtra("startTime", detailPlayer.getCurrentPositionWhenPlaying());
//                startActivityForResult(intent, 1);
//            }
//        });
    }


    /**
     * 获取首页第一部分数据
     * <p>
     * (轮播图、视频、广告牌、五菜单)
     */
    private void loadHomeOne() {
        HttpClient.post(HttpClient.HOME_ONE, new HashMap<String, Object>(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                refreshRoot.setRefreshing(false);
                BaseResult result = JSONObject.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    oneBO = JSONObject.parseObject(result.getData(), HomeOneBO.class);
                    MApplication.teamActivity = oneBO.getGrouptip();
                    EventBus.getDefault().post("teamActivity");//通知MainActivity显示拼团活动标签
                    StateMessage.haveMes = String.valueOf(oneBO.getHavemes());
                    EventBus.getDefault().post(new com.haoyigou.hyg.config.MessageEvent("mes",oneBO.getHavemes()));
                    setBanner();
                    setOneUI();
//                    getShop(false);//6.14改的这里修改加载不全bug
                } else {
                    showToast(result.getMessage());
                }
            }
        }, getActivity());
    }

    /**
     * 设置轮播图
     */
    private void setBanner() {
        if (oneBO.getLunbopics() != null && oneBO.getLunbopics().size()>0) {
            if (oneBO.getPageSetting().getBannerSetting() != null &&
                    !oneBO.getPageSetting().getBannerSetting().equals("")){
                if (oneBO.getPageSetting().getBannerSetting().getBackpic() != null
                        && !oneBO.getPageSetting().getBannerSetting().getBackpic().equals("")){
                    bannerBackImg.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(oneBO.getPageSetting().getBannerSetting().getBackpic()).into(bannerBackImg);
                }else {
                    bannerBackImg.setVisibility(View.VISIBLE);
                    if (oneBO.getPageSetting().getBannerSetting().getColor() != null &&
                            !oneBO.getPageSetting().getBannerSetting().getColor().equals("")) {
                        if (oneBO.getPageSetting().getBannerSetting().getColor().startsWith("#")) {
                            bannerBackImg.setBackgroundColor(Color.parseColor(oneBO.getPageSetting().getBannerSetting().getColor()));
                        }else {
                            bannerBackImg.setBackgroundColor(Color.parseColor("#"+oneBO.getPageSetting().getBannerSetting().getColor()));
                        }
                    }
                }
            }else {
                bannerBackImg.setVisibility(View.GONE);
            }
            //设置广告图片点击事件
            banner.setOnItemClickListener((banner12, model, view, position) -> {
                String url = oneBO.getLunbopics().get(position).getAddress();
                if (url == null || url.length() < 10) return;
                if (url.startsWith("/pages")){
                    gotoMini(url);
                }else {
                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                    if (url.contains("?")) {
                        intent.putExtra("url", url + "&parentLocation=102");
                    } else {
                        intent.putExtra("url", url + "?parentLocation=102");
                    }
                    intent.putExtra("all", true);
                    intent.putExtra("isTitle", true);
                    startActivity(intent);
                }
            });
            //加载广告图片
            banner.loadImage((banner1, model, view, position) -> {
                RoundImageView image = view.findViewById(R.id.image);
                Glide.with(getActivity()).load(oneBO.getLunbopics().get(position).getPictureroot()).into(image);
            });
            banner.setAutoPlayAble(oneBO.getLunbopics().size() > 1);
            banner.setPointsIsVisible(false);
            banner.setIsClipChildrenMode(true);
            banner.setAutoPalyTime(oneBO.getLunbotime() * 1000);
            banner.setData(R.layout.home_banner_layout, oneBO.getLunbopics(), null);//banner_image_layout
        }
    }

    /**
     * 设置第一部分界面
     */
    private void setOneUI() {
        if (StringUtils.isEmpty(oneBO.getAd().getImg())) {
            videoImgTitle.setVisibility(View.GONE);
            videotitlebg.setVisibility(View.GONE);
        } else {
            videoImgTitle.setVisibility(View.VISIBLE);
            videotitlebg.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(oneBO.getAd().getImg()).into(videoImgTitle);
        }

        if (oneBO.getPageSetting().getIconSetting() != null){
            if (oneBO.getPageSetting().getIconSetting().getBackpic() != null &&
                    !oneBO.getPageSetting().getIconSetting().getBackpic().equals("")){
                iconBackImg.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(oneBO.getPageSetting().getIconSetting().getBackpic())
                        .into(iconBackImg);
            }else {
                iconBackImg.setVisibility(View.VISIBLE);
                if (oneBO.getPageSetting().getIconSetting().getColor() != null
                        && !oneBO.getPageSetting().getIconSetting().getColor().equals("")){
                    if (oneBO.getPageSetting().getIconSetting().getColor().startsWith("#")) {
                        iconBackImg.setBackgroundColor(Color.parseColor(oneBO.getPageSetting().getIconSetting().getColor()));
                    }else {
                        iconBackImg.setBackgroundColor(Color.parseColor("#"+oneBO.getPageSetting().getIconSetting().getColor()));
                    }
                }
            }
        }else {
            iconBackImg.setVisibility(View.GONE);
        }
        LGRecycleViewAdapter<MenuBO> adapter = new LGRecycleViewAdapter<MenuBO>(oneBO.getIcons()) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.item_oneui;
            }

            @Override
            public void convert(LGViewHolder holder, MenuBO menuBOs, int position) {
                ShapedImageView liveImg = (ShapedImageView) holder.getView(R.id.menu01_img);
                ViewGroup.LayoutParams params = liveImg.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.17);
                params.width = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.17);
                liveImg.setLayoutParams(params);

                TextView liveType = (TextView) holder.getView(R.id.menu01_title);
                Glide.with(getActivity()).load(menuBOs.getImg()).into(liveImg);
                    liveType.setText(menuBOs.getTitle());
            }
        };
        adapter.setOnItemClickListener(R.id.menu01, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
               if(oneBO.getIcons().get(position).getJumptype()==1){//充值中心
                   if (StateMessage.IS_LOGIN) {
                       Intent intentVoucher = new Intent(getActivity(), VoucherCenterAct.class);
                       intentVoucher.putExtra("parentLocation","103");
                       startActivity(intentVoucher);
                       return;
                   } else {
                       Intent intent = new Intent(getActivity(), LoginActivity.class);
                       intent.putExtra("addFinish", true);
                       startActivity(intent);
                       getActivity().finish();
                       return;
                   }
               }else if(oneBO.getIcons().get(position).getJumptype()==2){//TV直播
                   MApplication.tvParentLocation = "103";
                   MApplication.isTvShow = true;
                   Intent mIntent = new Intent(getActivity(), NewTVShowActivity.class);//NewTVShowActivity,TVLiveActivity
                   startActivity(mIntent);
//                   EventBus.getDefault().post("ShowTvFragment");
               }else if(oneBO.getIcons().get(position).getJumptype()==3||oneBO.getIcons().get(position).getJumptype()==6){//拼团
                   if (oneBO.getIcons().get(position).getUrl().startsWith("/pages")){
                       gotoMini(oneBO.getIcons().get(position).getUrl());
                   }else {
                       Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                       if (oneBO.getIcons().get(position).getUrl().contains("?")) {
                           intent.putExtra("url", oneBO.getIcons().get(position).getUrl() + "&parentLocation=103");
                       } else {
                           intent.putExtra("url", oneBO.getIcons().get(position).getUrl() + "?parentLocation=103");
                       }
                       GroupUrlBean groupUrlBean = new GroupUrlBean();
                       String a = oneBO.getIcons().get(position).getUrl();
                       groupUrlBean.setUrl(oneBO.getIcons().get(position).getUrl());
                       intent.putExtra("all", true);
                       intent.putExtra("isTitle", true);
                       startActivity(intent);
                   }
               }else if(oneBO.getIcons().get(position).getJumptype()==4){//新品
                   MApplication.labelParentLocation = "103";
                   Bundle bundle = new Bundle();
                   bundle.putString("productTabId", "-1");
                   goToActivity(LabelActivity.class, bundle, false);
               }else if(oneBO.getIcons().get(position).getJumptype()==5){//分类
                   MApplication.selectorParentLocation = "103";
                   Intent intent3 = new Intent(getActivity(), SelectorActivity.class);
                   String[] ids = oneBO.getIcons().get(position).getIdparam().split(",");
                   intent3.putExtra("Pfflid", ids[0]);
                   intent3.putExtra("pfflid", ids[1]);
                   startActivity(intent3);
               }else if(oneBO.getIcons().get(position).getJumptype()==7){//人气模块二级页面
                   MApplication.popularityParentLocation = "103";
                   Intent intent3 = new Intent(getActivity(), PopularityAct.class);
                   intent3.putExtra("tatle", oneBO.getIcons().get(position).getTitle());
                   intent3.putExtra("idparam", oneBO.getIcons().get(position).getIdparam());
                   startActivity(intent3);
               }else if(oneBO.getIcons().get(position).getJumptype()==8){//疯抢二级页面\
                   MApplication.madrushParentLocation = "103";
                   Intent intent3 = new Intent(getActivity(), MadrushAct.class);
                   startActivity(intent3);
               }else if(oneBO.getIcons().get(position).getJumptype()==9){//专题精选二级
                   MApplication.labelParentLocation = "103";
                   Intent intent3 = new Intent(getActivity(), SelectionAct.class);

                   startActivity(intent3);
               }else if(oneBO.getIcons().get(position).getJumptype()==10){//优选直播
                   startActivity(new Intent(getActivity(),ShowOnlineFragment.class));
               }
            }
        });
        oneuirecycle.setAdapter(adapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HomeThreeBO oneBO) {   //是否进入首页
//        getShop(true);//6.14改的这里修改加载不全bug
    }

    /**
     * 增加视频的商品列表
     */
    private com.zhy.adapter.recyclerview.CommonAdapter<NewProdectBO.ProductsBean>  newSmallAdapter;
    class HotProductAdapter extends BaseAdapter{

        Context context;
        LayoutInflater mInflater;

        public HotProductAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (videoBeanList != null && videoBeanList.size()>0){
                return prodectData.size() + videoBeanList.size();
            }else {
                return prodectData.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (videoBeanList != null && videoBeanList.size()>0){
                if (position < videoBeanList.size()){
                    VideoHolder holder;
                    if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
                        holder = (VideoHolder) convertView.getTag();
                    } else {
                        holder = new VideoHolder();
                        convertView = mInflater.inflate(R.layout.video_item_layout, null);
                        holder.videoplayer = convertView.findViewById(R.id.videoplayer);
                        holder.btnDetail = convertView.findViewById(R.id.btnDetail);
                        holder.name = convertView.findViewById(R.id.name);
                        holder.appPrice = convertView.findViewById(R.id.appPrice);
                        holder.hygPrice = convertView.findViewById(R.id.hygPrice);
                        holder.hygPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
                        holder.monthSell = convertView.findViewById(R.id.monthSell);
                        holder.imagePic = convertView.findViewById(R.id.imagePic);
                        convertView.setTag(holder);
                    }
                    holder.name.setText(videoBeanList.get(position).getName());
                    holder.hygPrice.setText(String.format("￥%s",videoBeanList.get(position).getPrice()));
                    holder.hygPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
                    holder.appPrice.setText(String.format("￥%s",videoBeanList.get(position).getDisprice()));
                    holder.monthSell.setText(String.format("已售%s件",videoBeanList.get(position).getSalenum()));
                    if (videoBeanList.get(position).getVideoUrl() == null ||
                            videoBeanList.get(position).getVideoUrl().equals("")){
                        holder.videoplayer.setVisibility(View.GONE);
                        holder.imagePic.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(videoBeanList.get(position).getPicUrl()).asBitmap().into(holder.imagePic);
                    }else {
                        holder.videoplayer.setVisibility(View.VISIBLE);
                        holder.imagePic.setVisibility(View.GONE);
                        holder.videoplayer.setUp(
                                videoBeanList.get(position).getVideoUrl(),
                                videoBeanList.get(position).getName(),videoBeanList.get(position).getJumpUrl());
                    }

                    Glide.with(getActivity())
                            .load(videoBeanList.get(position).getPicUrl())
                            .into(holder.videoplayer.thumbImageView);
                    holder.videoplayer.positionInList = position;
                    holder.videoplayer.productName.setText(videoBeanList.get(position).getName());
                    holder.btnDetail.setOnClickListener(v -> {//点击查看详情
                        String url = videoBeanList.get(position).getJumpUrl();
                        if (url == null || url.length() < 10) return;
                        if (url.startsWith("/pages")){
                            gotoMini(url);
                        }else {
                            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                            if (url.contains("?")) {
                                intent.putExtra("url", url + "&parentLocation=123");
                            } else {
                                intent.putExtra("url", url + "?parentLocation=123");
                            }
                            intent.putExtra("all", true);
                            intent.putExtra("isTitle", true);
                            startActivity(intent);
                        }
                    });
                    holder.imagePic.setOnClickListener(v -> {
                        String url = videoBeanList.get(position).getJumpUrl();
                        if (url == null || url.length() < 10) return;
                        if (url.startsWith("/pages")){
                            gotoMini(url);
                        }else {
                            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                            if (url.contains("?")) {
                                intent.putExtra("url", url + "&parentLocation=123");
                            } else {
                                intent.putExtra("url", url + "?parentLocation=123");
                            }
                            intent.putExtra("all", true);
                            intent.putExtra("isTitle", true);
                            startActivity(intent);
                        }
                    });
                }else {
                    NormalViewHolder viewHolder;
                    if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof NormalViewHolder) {
                        viewHolder = (NormalViewHolder) convertView.getTag();
                    } else {
                        viewHolder = new NormalViewHolder();
                        convertView = mInflater.inflate(R.layout.new_main_item_layout, null);
                        viewHolder.ivTitle = convertView.findViewById(R.id.ivTitle);
                        viewHolder.prodectList= convertView.findViewById(R.id.grid_view);
                        convertView.setTag(viewHolder);
                    }
                    Glide.with(getActivity()).load(prodectData.get(position - videoBeanList.size()).getCategoryIndexPic()).into(viewHolder.ivTitle);
                    viewHolder.prodectList.setNestedScrollingEnabled(false);
                    ViewGroup.LayoutParams params = viewHolder.ivTitle.getLayoutParams();
                    params.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.2);
                    params.width =(DisplayUtils.getScreenWidth(getActivity()));
                    viewHolder.ivTitle.setLayoutParams(params);
                    viewHolder.ivTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = prodectData.get(position - videoBeanList.size()).getCategoryUrl();
                            if (url == null || url.length() < 10) return;
                            if (url.startsWith("/pages")){
                                gotoMini(url);
                            }else {
                                Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                                if (url.contains("?")) {
                                    intent.putExtra("url", url + "&parentLocation=110");
                                } else {
                                    intent.putExtra("url", url + "?parentLocation=110");
                                }
                                intent.putExtra("all", true);
                                intent.putExtra("isTitle", true);
                                startActivity(intent);
                            }
                        }
                    });
                    //纵向网格布局
                    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
                    viewHolder.prodectList.setLayoutManager(layoutManager);
                    List<NewProdectBO.ProductsBean> data = new ArrayList<>();
                    data.addAll(prodectData.get(position - videoBeanList.size()).getProducts());
                    newSmallAdapter = new com.zhy.adapter.recyclerview.CommonAdapter<NewProdectBO.ProductsBean>(getActivity(),
                            R.layout.new_prodect_item_layout,data) {
                        @Override
                        protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, final NewProdectBO.ProductsBean productsBean, int position) {
                            RelativeLayout rlPic = holder.getConvertView().findViewById(R.id.rlPic);
                            ViewGroup.LayoutParams params3 = rlPic.getLayoutParams();
                            params3.height=(int) (DisplayUtil.getMobileWidth(getActivity()) * 0.44);
                            params3.width=(int) (DisplayUtil.getMobileWidth(getActivity()) * 0.44);
                            rlPic.setLayoutParams(params3);

                            Glide.with(getActivity()).load(productsBean.getProductCase()).into((ImageView) holder.getView(R.id.ivTitle));
                            Glide.with(getActivity()).load(productsBean.getPiclogo()).into((ImageView) holder.getView(R.id.ivTitle2));
                            holder.setText(R.id.txtTitle, productsBean.getName());
                            holder.setText(R.id.nowPrice, String.format("¥%s", productsBean.getDisprice()));
                            TextView oldPrice = (TextView) holder.getConvertView().findViewById(R.id.oldPrice);
                            oldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                            oldPrice.setText(String.format("¥%s", productsBean.getPrice()));
                            if (productsBean.getSecondTitle() != null && !productsBean.getSecondTitle().equals("")){
                                holder.getView(R.id.rlSecondtitle).setVisibility(View.VISIBLE);
                                holder.setText(R.id.txtSecondTitle,productsBean.getSecondTitle());
                            }else {
                                holder.getView(R.id.rlSecondtitle).setVisibility(View.GONE);
                            }
                            if (productsBean.getOtherDiscontent() != null && !productsBean.getOtherDiscontent().equals("")){
                                holder.getView(R.id.rlActivity).setVisibility(View.VISIBLE);
                                holder.setText(R.id.tvCount,productsBean.getOtherDiscontent());
                            }else {
                                holder.getView(R.id.rlActivity).setVisibility(View.GONE);
                            }
                            if (productsBean.getCanUseCoupon() == 0){
                                holder.getView(R.id.rlTwo).setVisibility(View.GONE);
                            }else {
                                holder.getView(R.id.rlTwo).setVisibility(View.VISIBLE);
                            }
                            if (productsBean.getDiscontent() != null && !productsBean.getDiscontent().equals("")){
                                holder.getView(R.id.rlActivity2).setVisibility(View.VISIBLE);
                                holder.setText(R.id.tvCount2,productsBean.getDiscontent());
                            }else {
                                holder.getView(R.id.rlActivity2).setVisibility(View.GONE);
                            }
                            holder.getView(R.id.ivTitle).setOnClickListener(v -> {
                                String url = productsBean.getJumpAdress();
                                if (url == null || url.length() < 10) return;
                                if (url.startsWith("/pages")){
                                    gotoMini(url);
                                }else {
                                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                                    if (url.contains("?")) {
                                        intent.putExtra("url", url + "&parentLocation=110");
                                    } else {
                                        intent.putExtra("url", url + "?parentLocation=110");
                                    }
                                    intent.putExtra("all", true);
                                    intent.putExtra("isTitle", true);
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    viewHolder.prodectList.setAdapter(newSmallAdapter);
                }
            }else {
                NormalViewHolder viewHolder;
                if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof NormalViewHolder) {
                    viewHolder = (NormalViewHolder) convertView.getTag();
                } else {
                    viewHolder = new NormalViewHolder();
                    convertView = mInflater.inflate(R.layout.new_main_item_layout, null);
                    viewHolder.ivTitle = convertView.findViewById(R.id.ivTitle);
                    viewHolder.prodectList= convertView.findViewById(R.id.grid_view);
                    convertView.setTag(viewHolder);
                }
                Glide.with(getActivity()).load(prodectData.get(position).getCategoryIndexPic()).into(viewHolder.ivTitle);
                viewHolder.prodectList.setNestedScrollingEnabled(false);
                ViewGroup.LayoutParams params = viewHolder.ivTitle.getLayoutParams();
                params.height=(int) (DisplayUtils.getScreenWidth(getActivity()) * 0.2);
                params.width =(DisplayUtils.getScreenWidth(getActivity()));
                viewHolder.ivTitle.setLayoutParams(params);
                viewHolder.ivTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = prodectData.get(position).getCategoryUrl();
                        if (url == null || url.length() < 10) return;
                        if (url.startsWith("/pages")){
                            gotoMini(url);
                        }else {
                            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                            if (url.contains("?")) {
                                intent.putExtra("url", url + "&parentLocation=110");
                            } else {
                                intent.putExtra("url", url + "?parentLocation=110");
                            }
                            intent.putExtra("all", true);
                            intent.putExtra("isTitle", true);
                            startActivity(intent);
                        }
                    }
                });
                //纵向网格布局
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
                viewHolder.prodectList.setLayoutManager(layoutManager);
                List<NewProdectBO.ProductsBean> data = new ArrayList<>();
                data.addAll(prodectData.get(position).getProducts());
                newSmallAdapter = new com.zhy.adapter.recyclerview.CommonAdapter<NewProdectBO.ProductsBean>(getActivity(),
                        R.layout.new_prodect_item_layout,data) {
                    @Override
                    protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, final NewProdectBO.ProductsBean productsBean, int position) {
                        RelativeLayout rlPic = holder.getConvertView().findViewById(R.id.rlPic);
                        ViewGroup.LayoutParams params3 = rlPic.getLayoutParams();
                        params3.height=(int) (DisplayUtil.getMobileWidth(getActivity()) * 0.44);
                        params3.width=(int) (DisplayUtil.getMobileWidth(getActivity()) * 0.44);
                        rlPic.setLayoutParams(params3);

                        Glide.with(getActivity()).load(productsBean.getProductCase()).into((ImageView) holder.getView(R.id.ivTitle));
                        Glide.with(getActivity()).load(productsBean.getPiclogo()).into((ImageView) holder.getView(R.id.ivTitle2));
                        holder.setText(R.id.txtTitle, productsBean.getName());
                        holder.setText(R.id.nowPrice, String.format("¥%s", productsBean.getDisprice()));
                        TextView oldPrice = (TextView) holder.getConvertView().findViewById(R.id.oldPrice);
                        oldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
                        oldPrice.setText(String.format("¥%s", productsBean.getPrice()));
                        if (productsBean.getSecondTitle() != null && !productsBean.getSecondTitle().equals("")){
                            holder.getView(R.id.rlSecondtitle).setVisibility(View.VISIBLE);
                            holder.setText(R.id.txtSecondTitle,productsBean.getSecondTitle());
                        }else {
                            holder.getView(R.id.rlSecondtitle).setVisibility(View.GONE);
                        }
                        if (productsBean.getOtherDiscontent() != null && !productsBean.getOtherDiscontent().equals("")){
                            holder.getView(R.id.rlActivity).setVisibility(View.VISIBLE);
                            holder.setText(R.id.tvCount,productsBean.getOtherDiscontent());
                        }else {
                            holder.getView(R.id.rlActivity).setVisibility(View.GONE);
                        }
                        if (productsBean.getCanUseCoupon() == 0){
                            holder.getView(R.id.rlTwo).setVisibility(View.GONE);
                        }else {
                            holder.getView(R.id.rlTwo).setVisibility(View.VISIBLE);
                        }
                        if (productsBean.getDiscontent() != null && !productsBean.getDiscontent().equals("")){
                            holder.getView(R.id.rlActivity2).setVisibility(View.VISIBLE);
                            holder.setText(R.id.tvCount2,productsBean.getDiscontent());
                        }else {
                            holder.getView(R.id.rlActivity2).setVisibility(View.GONE);
                        }
                        holder.getView(R.id.ivTitle).setOnClickListener(v -> {
                            String url = productsBean.getJumpAdress();
                            if (url == null || url.length() < 10) return;
                            if (url.startsWith("/pages")){
                                gotoMini(url);
                            }else {
                                Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                                if (url.contains("?")) {
                                    intent.putExtra("url", url + "&parentLocation=110");
                                } else {
                                    intent.putExtra("url", url + "?parentLocation=110");
                                }
                                intent.putExtra("all", true);
                                intent.putExtra("isTitle", true);
                                startActivity(intent);
                            }
                        });
                    }
                };
                viewHolder.prodectList.setAdapter(newSmallAdapter);
            }
            return convertView;
        }

        class VideoHolder {//视频布局
            JzvdStd videoplayer;
            LinearLayout btnDetail;
            TextView name;
            TextView hygPrice;
            TextView appPrice;
            TextView monthSell;
            ImageView imagePic;
        }

        class NormalViewHolder {//普通商品布局
            ImageView ivTitle;
            FixedRecyclerView prodectList;

        }

    }

    /**
     * 视频全屏点击购买
     * @param string
     */
    @Override
    public void goToBuy(String string) {
        if (string == null || string.length() < 10) return;
        if (string.startsWith("/pages")){
            gotoMini(string);
        }else {
            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
            if (string.contains("?")) {
                intent.putExtra("url", string + "&parentLocation=123");
            } else {
                intent.putExtra("url", string + "?parentLocation=123");
            }
            intent.putExtra("all", true);
            intent.putExtra("isTitle", true);
            startActivity(intent);
        }
    }

    /**
     * 获取新改版最下面单品数据
     */
    private void getNewShop() {
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.NEW_HOME_SHOP, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    List<NewProdectBO> mShop = JSONArray.parseArray(result.getData(), NewProdectBO.class);
                    prodectData.clear();
                    prodectData.addAll(mShop);
                    getVideoShop();
                }
            }
        }, getActivity());
    }


    /**
     * 获取新改版视频
     */
    private List<VideoBean> videoBeanList = new ArrayList<>();
    private void getVideoShop() {
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.NEW_HOME_VIDEO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    if (!result.getData().equals("")){
                        llVideo.setVisibility(View.VISIBLE);
                        videoBeanList = JSONArray.parseArray(result.getData(), VideoBean.class);
                    }else {
                        llVideo.setVisibility(View.GONE);
                    }
                    hotProductAdapter = new HotProductAdapter(getActivity());
                    gridView.setAdapter(hotProductAdapter);
                    hotProductAdapter.notifyDataSetChanged();
                }
            }
        }, getActivity());
    }

    /***
     * 判断是否有优惠券
     */
    private void isHasPromotion() {
        PromotionsPopEntity param = new PromotionsPopEntity();
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        HttpClient.promptionspop(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, String body) {
                if (statusCode != 200 || StringUtils.isEmpty(body)) {
                    return;
                }
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
            }
        }, getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_title_img:   //广告牌
                if (!StringUtils.isEmpty(oneBO.getAd().getUrl())) {
                    if (oneBO.getAd().getUrl().startsWith("/pages")){
                        gotoMini(oneBO.getAd().getUrl());
                    }else {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", oneBO.getAd().getUrl());
                        intent.putExtra("all", true);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.topButton:
                gridView.setSelection(0);
                gridView.smoothScrollToPosition(0);
                break;
            case R.id.see_more:
                Bundle bundle = new Bundle();
                bundle.putString("productTabId", "-1");
                goToActivity(LabelActivity.class, bundle, false);
                break;
            case R.id.rlMore://限时购查看更多
                MApplication.madrushParentLocation = "104";
                Intent intent3 = new Intent(getActivity(), MadrushAct.class);
                startActivity(intent3);
                break;
            case R.id.rlTvMore://每日上新查看更多
                MApplication.labelParentLocation = "103";
                Bundle bundle2 = new Bundle();
                bundle2.putString("productTabId", "-1");
                goToActivity(LabelActivity.class, bundle2, false);
                break;
            case R.id.rlMoreShow://优选直播
                startActivity(new Intent(getActivity(),ShowOnlineFragment.class));
                break;
            case R.id.seeAllTvShow://TV直播
                MApplication.isTvShow = true;
                startActivity(new Intent(getActivity(), NewTVActivity.class));
                break;
            case R.id.allGroupProduct://全部拼团
                EventBus.getDefault().post("ShowPinFragment");
                break;
            default:
                break;
        }
    }

    /**
     * 电话订购
     */
    private void showLoginDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//        tvTitle.setText("提示");
        TextPaint tp = tvTitle.getPaint();
        tp.setFakeBoldText(true);
        final TextView btnYes = (TextView) view.findViewById(R.id.f_quchecbutton_btn_queding);
        final TextView btlNo = (TextView) view.findViewById(R.id.f_quchecbutton_btn_quxiao);
        final MyDialog builder = new MyDialog(getActivity(),0,0,view,R.style.DialogTheme);

        builder.setCancelable(false);
        builder.show();
        //设置对话框显示的View
        //点击确定是的监听
        //拨打电话
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                call("4001188188");
                builder.cancel();
            }
        });


        btlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent intent1 = new Intent(getActivity(), ActivityCapture.class);
                    startActivity(intent1);
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "您没有申请权限", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResume() {
        if (MApplication.isFromAll){//从TV直播全屏界面返回要继续播放,其他情况播放
            MApplication.isFromAll = false;
        }
        loadFengQiang(1);//限时购
        getHuiProduct();//1元爆枪款
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        if (shopBOs.get(position - 1).getJumpAdress().startsWith("/pages")){
            gotoMini(shopBOs.get(position - 1).getJumpAdress());
        }else {
            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
            intent.putExtra("all", true);
            intent.putExtra("url", shopBOs.get(position - 1).getJumpAdress());
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 弹出网络环境提示弹窗
     */
    private void showHintDialog(final boolean isFirst) {
        LayoutInflater factory = LayoutInflater.from(getActivity());//提示框
        final View view = factory.inflate(R.layout.hint_dialog_layout, null);//这里必须是final的
        TextView cancle = (TextView) view.findViewById(R.id.off_commit);
        TextView commit = (TextView) view.findViewById(R.id.commit);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //事件
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /** 监测网络变化 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals("4G")) {
            showHintDialog(false);
        }else if (str.equals("wifi")){

        }
    }

    /*******************************     IM 相关  *********************************/
    /**
     * 设置腾讯IM相关
     */
    private void setIM(String groupId) {
        //判断是否登录IM
        String userName = null;
        String userSig;
        if (SharedPreferencesUtils.getInstance().getString("imName", "") == null){
            userName = String.valueOf(TimeUtils.getNowTimeString());
            SharedPreferencesUtils.getInstance().putString("imName", userName);
        }else {
            userName = SharedPreferencesUtils.getInstance().getString("imName", "");
        }
        if(userName.equals("")){
            userName = String.valueOf(TimeUtils.getNowTimeString());
        }
        userSig = GenerateTestUserSig.genTestUserSig(userName);

        TIMManager.getInstance().login(userName, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.d("腾讯IM登录", "错误代码" + code + "错误信息" + desc);
            }

            @Override
            public void onSuccess() {
                Log.d("腾讯IM登录", "成功");
                joinGroup(groupId);
            }
        });


    }


    /**
     * 加入群
     */
    private void joinGroup(String groupId) {
        TIMGroupManager.getInstance().applyJoinGroup(groupId, "some reason", new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //接口返回了错误码 code 和错误描述 desc，可用于原因
                //错误码 code 列表请参见错误码表
//                Log.e("加入群", "applyJoinGroup err code = " + code + ", desc = " + desc);
                if (code == 10013) {//已经在该群中

                }
            }

            @Override
            public void onSuccess() {
                Log.i("加入群", "applyJoinGroup success");

            }
        });

        //设置消息监听器，收到新消息时，通过此监听器回调
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {//消息监听器
            @Override
            public boolean onNewMessages(List<TIMMessage> msgs) {//收到新消息
                //消息的内容解析请参考消息收发文档中的消息解析说明
                resloveMessage(msgs);
                return false;
            }
        });

    }

    /**
     * 处理消息
     * @param msgs
     */
    Set<String> maqueeData = new HashSet<>();
    private void resloveMessage(List<TIMMessage> msgs){//messageType 1 普通 2 历史
        if (null != msgs && msgs.size() > 0) {
            for (final TIMMessage msg : msgs) {
                TIMConversation conversation = msg.getConversation();
                TIMConversationType type = conversation.getType();
                if (type == TIMConversationType.C2C) {

                } else if (type == TIMConversationType.Group) {
                    final TIMElem elem = msg.getElement(0);
                    if (elem.getType() == TIMElemType.Text) {//文本消息
                        final TIMTextElem textElem = (TIMTextElem) elem;
                        String text = textElem.getText();
                        List<String> list = Arrays.asList(text.split("&"));//第一项 type  第二项  id  第三项  内容
                        if (list.size()>0) {
                            if (list.get(0).equals("orderMessage") && list.size() >= 3) {//购买消息
//                                buyLayout.setVisibility(View.VISIBLE);
                                buyLayout.setVisibility(View.GONE);
                                if (maqueeData.size() > 20) {
                                    maqueeData.clear();
                                }
                                maqueeData.add(text);
                                SPUtils.getInstance().put("maqueeData", maqueeData);
                                for (String txt : SPUtils.getInstance().getStringSet("maqueeData")) {
                                    List<String> listData = Arrays.asList(txt.split("&"));
                                    Marquee marquee = new Marquee(listData.get(2), listData.get(1));
                                    marqueesList.add(marquee);
                                }
                                buyPerson.setImage(true);
                                buyPerson.startWithList(marqueesList);

//                            Glide.with(getActivity()).load(list.get(1)).asBitmap().into(buyHeader);
//                            buyPerson.setText(String.format("%s",list.get(2)));
                            }
                        }
                    }
                } else if (type == TIMConversationType.System) {

                }
            }
        }
    }

    /**
     *  协议
     */
    private void infoDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.information_dialog_layout, null);
        TextView cancle = v.findViewById(R.id.cancle);
        TextView sure = v.findViewById(R.id.sure);
        TextView txt = v.findViewById(R.id.txt2);

        // SpannableStringBuilder 用法
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
        spannableBuilder.append(txt.getText().toString());
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {//好易购用户协议
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getActivity(), userWebviewActivity.class);
                intent.putExtra("url", "/userAgreement");
                intent.putExtra("title", "用户协议");
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
//                设定的是span超链接的文本颜色，而不是点击后的颜色
                ds.setColor(Color.parseColor("#009FFF"));
                ds.setUnderlineText(false);    //去除超链接的下划线
                ds.clearShadowLayer();//清除阴影
            }

        };
        spannableBuilder.setSpan(clickableSpan, 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置部分文字点击事件
        ClickableSpan clickableSpan2 = new ClickableSpan() {//隐私声明
            @Override
            public void onClick(View widget) {
                Intent intent1 = new Intent(getActivity(), userWebviewActivity.class);
                intent1.putExtra("url", "/privacyNotice");
                intent1.putExtra("title", "隐私声明");
                startActivity(intent1);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
//                设定的是span超链接的文本颜色，而不是点击后的颜色
                ds.setColor(Color.parseColor("#009FFF"));
                ds.setUnderlineText(false);    //去除超链接的下划线
                ds.clearShadowLayer();//清除阴影
            }

        };
        spannableBuilder.setSpan(clickableSpan2, 13, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txt.setText(spannableBuilder);
        txt.setHighlightColor(getResources().getColor(android.R.color.transparent));//点击后的背景颜色，Android4.0以上默认是淡绿色，低版本的是黄色
        txt.setMovementMethod(LinkMovementMethod.getInstance());

        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.CENTER);
        noticeDialog.setCancelable(false);
        noticeDialog.show();

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
                mApplication.exit();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
                SPUtils.getInstance("code").put("codeNo", Util.getAppVersionName(getActivity()));
            }
        });

        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = (int)(DisplayUtils.getScreenWidth(getActivity())*0.75);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        noticeDialog.getWindow().setAttributes(layoutParams);
    }

    /**
     * 跳转到小程序
     * @param url
     */
    private void gotoMini(String url){
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), WXUtils.WX_APP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_ac15d3111f1f"; // 填小程序原始id
        req.path = url;//拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

}
