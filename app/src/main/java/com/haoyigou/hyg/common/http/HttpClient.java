package com.haoyigou.hyg.common.http;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.OkHttpSSLUtils;
import com.haoyigou.hyg.base.OkhttpU;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.APPLoginEntity;
import com.haoyigou.hyg.entity.APPReFoudEntity;
import com.haoyigou.hyg.entity.APPWeChatLoginBindMobileEntity;
import com.haoyigou.hyg.entity.ActivityListEntity;
import com.haoyigou.hyg.entity.CreateMemberEntity;
import com.haoyigou.hyg.entity.GoodInfoEntity;
import com.haoyigou.hyg.entity.PromotionsPopEntity;
import com.haoyigou.hyg.entity.SavePersonalInfomationEntity;
import com.haoyigou.hyg.entity.SendCircleBean;
import com.haoyigou.hyg.entity.ShowRedPicEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.UserExperienceIdentifyCodeEntity;
import com.haoyigou.hyg.utils.MediaUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.widget.LoadingProgressDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.MultipartBody;

//
///**
// * HTTP请求类
// * Created by liuxuanyi on 16/8/1.
// */
public class HttpClient {
    private static Context context;
    private HttpClient(Context context){
        this.context=context;
    }
    public static final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static PersistentCookieStore cookieStore;
    public static final String UTF_8 = "UTF-8";


    public static final String HTTP_DOMAIN = "https://m.best1.com";//好易购正式环境
//    public static final String HTTP_DOMAIN = "http://192.168.1.101:8080";
//    public static final String HTTP_DOMAIN = "https://xgg.happydoit.com";
//    public static final String HTTP_DOMAIN = "https://hyg.happydoit.com";//好易购测试环境80端口





    public static final String UPDATEVERSION = HTTP_DOMAIN + "/appfile/ruiduo_app.xml";//更新版本号
    public static final String appFileName = "haoyigou.apk";  //版本
    public static final String DOWNLOAD_PATH = "/data/data/com.haoyigou.hyg/download";  //版本下载到手机的位置
    private static final String SendMobieCode = "/jz/distributor/sendcode";//发送验证码
//    private static final String SendMobieCode = "/distributor/sendcode2";//发送验证码
    private static final String APPLOGIN = "/applogin.do";//手机号登录
    public static final String LUNBO = "/distributor/applunbotu";//首页轮播图接口
    private static final String USERANDHEHUOREN = "/distributor/appbasedis";//登陆后获取用户以及合伙人信息
    private static final String ACTIVITYLIST = "/distributor/apptablist";//首页活动列表接口
    public static final String GETGOODINFO = "/distributor/indexdata";//首页获取商品信息
    private static final String SAVEPERSONALINFOMATION = "/distributor/appsettingSave";
    public static final String FAVORITE = "/mycollection/myproducts";//收藏夹网页链接
    public static final String VALUEVOUCHER = "/coupons/mycoupons";//优惠卷网页链接
    public static final String MYTEAM = "/distributor/team";//我的团对网页链接
    public static final String POINT_SCORE = "/exchange/index";//积分商城
    public static final String MYGAINS = "/distributor/income";//我的收获网页链接
    public static final String INVOICE = "/order/dzfporders";//电子发票
    public static final String MYREFLECT = "/distributor/cashIndex";//我要提现网页链接
    public static final String MYPRICE = "/distributor/mypoints";     //我的金币
    public static final String MYINTERNAGER = "/distributor/myintegration";   //我的积分
    public static final String POINTINTERGER = "/task/pointsExchangeIndex";   //积分兑换
    public static final String GAME = HTTP_DOMAIN + "/task/gameIndex";    //游戏
    public static final String RECOMMEND = "/task/recommend";   //推荐
    public static final String TASKINDEX = "/task/taskIndex";   //阅读
    public static final String RANKLISTT = "/distributor/rankinglist";//排行榜网页链接
    public static final String HELPERINDEX = "/help/helpIndex";//新手指南网页链接
    public static final String RUODUOCLASS = "/help/helpIndex2";//瑞朵课堂网页链接
    public static final String CONTACTUS = "/business/callservice";//联系我们网页连接
    public static final String APPLIVE = "/liveShow/appShow";//app直播网页链接
    public static final String CONTACTUSAAA = "/liveShow/showIndex";//app直播网页链接
    public static final String ALLORDERLINK = "/order/myallorder ";//全部订单的网页链接
    public static final String PAYMENTORDERLINK = "/order/myorder";//代付款订单网页链接
    public static final String GENEARTIONSHIPPINGORDERLINK = "/order/myorder";//代发货订单链接
    public static final String COLLECTIONORDERLINK = "/order/myorder";//待收货订单链接
    public static final String REFOUNDORDERLINK = "/order/myorder";//退款售后订单链接
    public static final String WECHATLOGINMOBILEBIND = "/distributor/appphonelogin";// app微信登录绑定手机号
    public static final String BINDCODE = "2";//微信登录
    public static final String CHARGELUN = "/recharge/chargeLunBo";//轮播图
    public static final String CHARGECOUPON = "/recharge/chargeCoupon";//优惠券列表

    public static final String ACTIVITYLISTWEBVIEW = "/distributor/productTab";//活动item点击进入webview
    public static final String GOODWEBVIEW = "/pix";//商品item点击进入webview
    public static final String APPPAY = "/order/confirmsave";
    public static final String PROMOTIONSPOP = "/distributor/appactivity";//优惠活动弹窗
    public static final String CREATEMEMBER = "/order/bindcus";//创建好易购会员手机号绑定
    public static String appwxpaysign_url = HTTP_DOMAIN + "/order/confirmsave";
    public static String appwxpaysign_url1 = HTTP_DOMAIN + "/order/appconfirmsave";
    public static String appwxpaysign_url2 = HTTP_DOMAIN + "/order/appvippay";
    public static String APPREFUND = "/order/appdrawreason";//APP退款页面加载退款原因
    public static String APPFIRSTFENLEI = "/distributor/appfirst";//app首页一级分类
    public static String MYADDRESS = "/address/myaddress";//修改我的地址网页连接
    public static String UPLOADPICS = "/order/apppicsave";//app退款原因上传图片（一张张的来）

    public static String SAVEREFUNDREASON = "/order/appcanceldata";//app保存退款原因
    public static String SAVEAPPHEADPIC = "/distributor/azappsettingSave";//保存头像
    public static String APPSETTING = "/distributor/appsetting";//设置保存信息
    public static String APPPOP = "/mycoupon/share";//弹窗的链接
    public static String YOUHUIPOP = "/coupons/mycoupons";
    public static String SHOWIMAGE = "/distributor/appdatas";//个人中心下面9个图标的标记
    public static String SHOWRED = "/distributor/apporders";//代付款代发货标记
    public static String ALIPAYADRESS = "/order/confirmsave";  //支付宝直接支付
    public static final String AlipayOrder = "/order/aliappconfirmsave";   //未支付订单直接支付
    public static final String AlipayRuiduo = "/order/aliappvippay";   //入驻瑞朵支付
    public static final String EXCHANGEPAY = "/exchange/pay/params";
    public static final String HOMEDATA = "/distributor/appindexbase"; //整合之后的首页接口，获取很多数据
    public static final String HOMEFENGQIANG = "/distributor/appDisActivity";   //正在疯抢

    public static final String YAOQINGMA = "/distributor/toyqm";   //邀请码页面
    public static final String SHOPMESSAGE = "/pix";    //每周上新连接
    public static final String SIGN = "/point/sign";    //积分签到
    public static final String SHAREURL = HTTP_DOMAIN + "/distributor/index/";

    public static final String SIGNIN = "/task/signin";   //签到接口
    public static final String COUPON = "/center/myCoupons";//优惠券接口

    public static final String GETTASKMESSAGE = "/distributor/appnewmission";  //获取新手任务弹窗数据

    public static final String MESSAGEBOX = "/distributor/allmes";   //消息盒子界面
    public static final String MESSAGEDETAILS = "/distributor/tpmes";  //消息盒子详情列表
    public static final String READPUSH = "/distributor/readpush";    //达人消息页

    public static final String ORDERDETAILS = "/order/detail";

    public static final String CLASSIFY = "/distributor/appysclassify";  //分类页面数据

    public static final String CUSTOMMUNE = "/distributor/appself"; //自定义菜单

    public static final String LABELDATA = "/distributor/appproductTabData";   //获取标签数据的接口

    public static final String SHOPCARDATA = "/carts/appmycartsdata";    //查询购物车商品接口

    public static final String EXCHANGE = "/priceincrease/activityresult";    //查询购物车商品是否满足换购条件

    public static final String EXCHANGE_LIST = "/priceincrease/productList";    //换购商品列表

    public static final String EXCHANGE_DETAIL = "/attr";    //换购商品详情

    public static final String ADD_CART = "/increase/carts/addcart";    //加入购物车


    public static final String DELCARTS = "/carts/delcart";   //删除购物车商品

    public static final String CHANGESHOP = "/carts/changeitemnum";   //改变购物车商品数量

    public static final String SUBMITSHOP = "/carts/pushids";    //提交结算购物车商品

    public static final String ALLREAD = "/pushmes/readAll";   //消息盒子全部阅读功能

    public static final String GETDISCODE = "/mycoupon/getdis";   //领取优惠卷功能

    public static final String SELETORHISTORY = "/distributor/appsearch";   //搜索历史接口

    public static final String SELETORNAME = "/distributor/appproSearch";   //根据关键词搜索商品

    public static final String DELETESELETOR = "/distributor/delHiswords";   //删除个人搜索历史记录接口

    public static final String VOUCHER = "/recharge/appNewrecharge";  //充值接口
    public static final String PAY_ACTIVITY = "/api/scs/couponTitle";  //充值送券活动接口
    public static final String GET_PAY_PAKCAGE = "/api/scs/takeRechargeCoupon";  //领取充值送的红包
    public static final String VOUCHERPAY = "/recharge/confirmsave";  //充值付款接口
    public static final String VOUCHERRECORDS = "/recharge/appreorders";   //查询充值记录

    public static final String IMG_VERSITION = "/distributor/verifyCode";   //获取图文验证

    public static final String LIVE_LIST = "/liveTelecast/appLive";   //获取视频直播列表
    public static final String LIVE_SHOP = "/liveTelecast/appHotProduct";   //获取直播商品数据
    public static final String LIVE_TISHI = "/liveTelecast/liveNotice";     //直播提醒
    public static final String LIVE_URL = "/liveTelecast/appLiveUrl";     //直播地址
    public static final String LIVEs_URL = "/liveShow/appShowIndex";     //直播地址

    public static final String ORDER_DATA = "/order/orderappdata";   //获取订单列表
    public static final String ORDER_MESSAGE = "/order/appOrderDetail";    //获取订单详情
    public static final String ORDER_DELETE = "/order/myallorderdelete";    //删除订单
    public static final String ORDER_CANCLE = "/order/cancel";      //取消订单
    public static final String ORDER_REMIND = "/jz/order/redOrder";    //提醒发货
    public static final String ORDER_CONFIRM = "/order/takeover";   //确认收货
    public static final String ORDER_AGAGIN = "/carts/addcartAgain";    //再次购买
    public static final String ORDER_PAYALI = "/order/aliappconfirmsave";   //支付宝支付
    public static final String ORDER_RED_LOG = "/order/logistics";     //查看物流
    public static final String HOME_LABLE = "/distributor/navigationBar";//首页导航栏数据
    public static final String CLASSIFY_DATA = "/second/productData";//分类界面数据
    public static final String CLASSIFY_LABLE = "/classify/firstContent";//分类页面轮播图和标签
    public static final String HOME_ONE = "/distributor/appIndexOne";// 首页第一部分
    public static final String HOME_TWO = "/distributor/appIndexNewTwo";   //首页第二部分
    public static final String MADRUSH = "/distributor/appIndexTwo";   //正在疯抢
    public static final String HOME_THREE = "/distributor/appIndexThree";  //首页第三部分
    public static final String NEW_HOME = "/distributor/appIndexThreeNew";  //首页新改版
    public static final String GET_POINT = "/mycoupon/indexCoupon";  //获取券
    public static final String HOME_SHOP = "/distributor/appIndexFour";   //首页最后一部分
    public static final String NEW_HOME_SHOP = "/distributor/appIndexFourNew";   //新改版首页最后一部分
    public static final String NEW_HOME_VIDEO = "/distributor/appIndexVideoProduct";   //新改版首页视频
    public static final String ONLINE_LIVE = "/liveStream/liveData";//直播首页列表数据
    public static final String ONLINE_LIVEDETAIL = "/liveStream/detailData";//直播详细数据数据
    public static final String FOCUS = "/liveStream/focusAnchor";//关注主播
    public static final String HISTORY = "/liveStream/msgHistory";//历史消息
    public static final String LIKE = "/liveStream/tip";//点赞
    public static final String GET_PRIZE = "/liveStream/getCoupon";//领取福袋
    public static final String NOTICE = "/liveStream/notice";//直播提醒
    public static final String ONLINE_LUNBO = "/liveStream/carouselData";//直播首页轮播图
    public static final String JUNIOR_RECOMMEND = "/junior/recommend";   //原生人气推荐
    public static final String JUNIOR_CULLING = "/junior/culling";   //原生专题精选
    public static final String PINTUAN = "/groupbuy/productList";   //拼团链接
    public static final String ALLGROUP = "/order/myAllGroupData";//拼团订单
    public static final String SUBMITURL = "/order/confirm?confirmType=1&source=1&buyway=2";
    public static final String RIGHT_LABLE = "/junior/tvCateList";   //ＴＶ直播页面右面列表数据
    public static final String LEFT_DATA = "/junior/tvProductList";//ＴＶ直播页面左面商品数据
    public static final String PAY_PRESELL = "/order/advanceConfirm";//预售支付界面
    public static final String PAY_ORDER_MONEY = "/order/endorder";//预售支付定金
    public static final String DAILY_NEW = "/dailyNew/appList";//每日一品

    public static final String SEND_CIRCLE = "/community/recommend/save";//推荐发布圈子
    public static final String PERSONSEND_CIRCLE = "/community/show/save";//买家秀发布圈子
    public static final String SHOWPRODUCT_LIST = "/community/product/list";//关联商品列表
    public static final String ONELIST = "/community/recommend/list";//推荐列表
    public static final String ONEBANNER = "/community/recommend/banners";//推荐轮播图
    public static final String TWOLIST = "/community/show/list";//买家秀列表
    public static final String SAVEONECOMMENT = "/community/recommend/comment/save";//推荐评论
    public static final String SAVETWOCOMMENT = "/community/show/comment/save";//买家秀评论
    public static final String ONELIKE = "/community/recommend/tip";//推荐点赞
    public static final String TWOLIKE = "/community/show/tip";//买家秀点赞
    public static final String COMMENTLIKE = "/community/comment/tip";//评论点赞
    public static final String COMMENTLIST = "/community/comment/list";//评论列表
    public static final String VIDEONUM = "/community/watch";//视频播放次数
    public static final String REPORT = "/complaint/save";//举报
    public static final String LOGOFF = "/distributor/logOff";//注销
    public static final String HUIPRODUCT = "/api/doubleEleven/list";//1元爆枪款
    public static final String GROUP = "/groupbuy/dataList";//团购

    //热卖
    public static final String HOT = "https://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/system_image/icon_hot.png";
    //新品
    public static final String NEWSHOP = "https://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/system_image/icon_new_dis.png";
    //促销
    public static final String SALESHOP = "https://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/system_image/icon_sale.png";
    //爆款
    public static final String BOOMSHOP = "https://best1-wexin.oss-cn-hangzhou.aliyuncs.com/images/system_image/icon_boom.png";
    public static final String RET_SUCCESS_CODE = "1";//成功
    public static final String UNLOGIN_CODE = "0";//失败
    public static final String UNSUCESS_CODE = "404";//404


    private static LoadingProgressDialog proDialog = null;






    static {
        mOkHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        cookieStore = new PersistentCookieStore(GlobalApplication.getInstance());
        mOkHttpClient.setCookieHandler(new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL));
        mOkHttpClient.setProxy(Proxy.NO_PROXY);//禁止代理模式,防止抓包
        //忽略https证书
//        mOkHttpClient.setSslSocketFactory(SSLSocketClient.getSSLSocketFactory());
//        mOkHttpClient.setHostnameVerifier(SSLSocketClient.getHostnameVerifier());
        //proDialog = LoadingProgressDialog.getDialog();

    }


    /**
     * 不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param httpResponseHandler
     */
    public static SSLContext sslContext;
    public static void enqueue(Request request, final AsyncHttpResponseHandler httpResponseHandler, Context mcontext, final boolean load_flag) {
        if (load_flag) {
            if (!((Activity) mcontext).isFinishing()) {
                startProgressDialog("加载中", mcontext);
            }
        }
        if (isWifiProxy()){
            ToastUtils.showToast(MApplication.gainContext(),"请先关闭代理,然后重新打开好易购");
            return;
        }
//        try {
//            InputStream inputStream = MApplication.gainContext().getAssets().open("hyg1.cer");
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{OkhttpU.trustManagerForCertificates(inputStream)}, null);
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//            mOkHttpClient.setSslSocketFactory(SSLSocketClient.getSSLSocketFactory(inputStream));
//            mOkHttpClient.setSslSocketFactory(sslSocketFactory, OkhttpU.trustManagerForCertificates(inputStream));
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        mOkHttpClient.setSslSocketFactory(SSLSocketClient.getSSLSocketFactory());

        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                httpResponseHandler.sendSuccessMessage(response);
                if (load_flag) {
                    stopProgressDialog();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                httpResponseHandler.sendFailureMessage(request, e);
                if (load_flag) {
                    stopProgressDialog();
                }
            }
        });
    }


    public static boolean isWifiProxy() {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 这里使用了HttpClient的API。只是为了方便
     *
     * @param params
     * @return
     */
    public static String formatParams(List<BasicNameValuePair> params) {
        return URLEncodedUtils.format(params, UTF_8);
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     *
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params) {
        return url + "?" + formatParams(params);
    }

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     *
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) GlobalApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
//            Log.v("Connectivity", e.getMessage());
        }
        return false;
    }

    private static String encodeParams(Map<String, Object> params) {
        String param = "{}";
        if (params != null) {
            JSONObject json = new JSONObject();
            try {
                for (String key : params.keySet()) {
                    json.put(key, params.get(key));
                }
//                Log.i("net_params", json.toString());
                param = Base64.encodeToString(json.toString().getBytes(UTF_8), Base64.DEFAULT);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return param;
    }


    public static void get(String url, Map<String, Object> params, AsyncHttpResponseHandler httpResponseHandler, Context mcontext, final boolean load_flag) {
        if (!isNetworkAvailable()) {
            Toast.makeText(GlobalApplication.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
//        Log.i("GET请求交易TAG", "请求url:----------》" + url + "---------请求参数:" + JSON.toJSONString(params));
        List<BasicNameValuePair> rq = new ArrayList<BasicNameValuePair>();
        if (params != null) {
            for (String key : params.keySet()) {
                rq.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
        }
        Request request = new Request.Builder().url(attachHttpGetParams(HTTP_DOMAIN + url, rq)).build();
//        Log.i("GET请求交易TAG", "请求查询的url:----------》" + attachHttpGetParams(HTTP_DOMAIN + url, rq));

        enqueue(request, httpResponseHandler, mcontext, load_flag);
    }


    /**
     * 请求外部链接的方法
     *
     * @param url
     * @param params
     * @param httpResponseHandler
     * @param mcontext
     */
    public static void get(String url, Map<String, Object> params, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {

        if (!isNetworkAvailable()) {
            Toast.makeText(GlobalApplication.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        Request request = new Request.Builder().url(url).build();
//        Log.i("GET请求交易TAG", "请求查询的url:----------》" + url);
        enqueue(request, httpResponseHandler, mcontext, false);
    }


    public static void post(String url, Map<String, Object> params, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {

        if (!isNetworkAvailable()) {
            Toast.makeText(mcontext, R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
//        OkHttpClient client = new OkHttpClient();
//        client.setCookieHandler(new CookieManager(new PersistentCookieStore(mcontext.getApplicationContext()), CookiePolicy.ACCEPT_ALL));
//        Log.e("Post请求交易TAG", "请求url:" + url + "请求参数:" + JSON.toJSONString(params));
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addEncoded(key, params.get(key) == null ? "" : params.get(key).toString());
            }
        }
        //Request request = new Request.Builder().url(HTTP_DOMAIN + url).addHeader("access-type", "app").post(builder.build()).build();
        // Response response = execute(request);
        Request request = null;
        if (StateMessage.IS_LOGIN || url.equals(APPLOGIN)) {
            request = new Request.Builder().url(HTTP_DOMAIN + url).addHeader("access-type", "android").post(builder.build()).build();
        } else {
            request = new Request.Builder().url(HTTP_DOMAIN + url).post(builder.build()).build();
        }
//        Log.e("post请求UrL-->", HTTP_DOMAIN + url);
        enqueue(request, httpResponseHandler, mcontext, false);
    }


    public static void delete(String url, Map<String, Object> params, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        if (!isNetworkAvailable()) {
            Toast.makeText(GlobalApplication.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
//        Log.i("DELETE请求交易TAG", "请求url:" + HTTP_DOMAIN + url + "请求参数:" + JSON.toJSONString(params));
        Request request = new Request.Builder().delete().url(HTTP_DOMAIN + url).build();
        enqueue(request, httpResponseHandler, mcontext, false);
    }


    public static void startProgressDialog(String progressMsg, Context mcontext) {
        if (proDialog == null) {
            proDialog = LoadingProgressDialog.createDialog(mcontext);
            proDialog.setMessage(progressMsg);
            proDialog.setCanceledOnTouchOutside(false);
        }
        proDialog.show();
    }

    public static void stopProgressDialog() {
        if (proDialog != null) {
            proDialog.dismiss();
            proDialog = null;
        }
    }

    public static void sendMobileCode(UserExperienceIdentifyCodeEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getPhone())) {
            params.put("phone", param.getPhone());
        }
        if (!StringUtils.isEmpty(param.getCodetype())) {
            params.put("codetype", param.getCodetype());
        }
        if (!StringUtils.isEmpty(param.getVerifycode())) {
            params.put("verifycode", param.getVerifycode());
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(mcontext, R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("Post请求交易TAG", "请求url:" + SendMobieCode + "请求参数:" + JSON.toJSONString(params));
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addEncoded(key, params.get(key).toString());
            }
        }
        Request request = new Request.Builder().url(HTTP_DOMAIN + SendMobieCode).addHeader("access-type", "hyghlbw")
                .post(builder.build()).build();
        Log.e("post请求UrL-->", HTTP_DOMAIN + SendMobieCode);
        enqueue(request, httpResponseHandler, mcontext, true);
        //post(SendMobieCode, params,httpResponseHandler,mcontext);
    }

    /**
     * 获取图文验证码
     */
    public static void getImageVersition(AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Request request = new Request.Builder().url(HTTP_DOMAIN + IMG_VERSITION).addHeader("access-type", "hyghlbw")
                .build();
        enqueue(request, httpResponseHandler, mcontext, true);
    }


    public static void MobileLogin(APPLoginEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(String.valueOf(param.getApptype()))) {
            params.put("apptype", param.getApptype());
        }
        if (!StringUtils.isEmpty(param.getTel())) {
            params.put("tel", param.getTel());
        }
        if (!StringUtils.isEmpty(param.getMobileCode())) {
            params.put("mobileCode", param.getMobileCode());
        }
        post(APPLOGIN, params, httpResponseHandler, mcontext);
    }

    public static void tokenlogin(APPLoginEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        params.put("apptype", param.getApptype());
        params.put("token", param.getToken());
        post(APPLOGIN, params, httpResponseHandler, mcontext);
    }

    public static void userinfo(StoreInfoEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        PackageManager packageManager = mcontext.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(mcontext.getPackageName(), 0);
//            params.put("appVersion", packInfo.versionCode);
            params.put("appVersion", 6);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        post(USERANDHEHUOREN, params, httpResponseHandler, mcontext);
    }

    public static void activitylist(ActivityListEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        post(ACTIVITYLIST, params, httpResponseHandler, mcontext);
    }

    public static void goodinfo(GoodInfoEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        if (!StringUtils.isEmpty(String.valueOf(param.getCuurPageNo()))) {
            params.put("currPageNo", param.getCuurPageNo());
        }
        if (!StringUtils.isEmpty(String.valueOf(param.getPageSize()))) {
            params.put("pageSize", param.getPageSize());
        }
        post(GETGOODINFO, params, httpResponseHandler, mcontext);
    }

    public static void uploadheadpic(SavePersonalInfomationEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getSaveType())) {
            params.put("saveType", param.getSaveType());
        }
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        if (!StringUtils.isEmpty(param.getRelative_ur())) {
            params.put("relative_ur", param.getRelative_ur());
        }
        post(SAVEPERSONALINFOMATION, params, httpResponseHandler, mcontext);
    }

    public static void changeshopname(SavePersonalInfomationEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getSaveType())) {
            params.put("saveType", param.getSaveType());
        }
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());

        }
        if (!StringUtils.isEmpty(param.getName())) {
            params.put("name", param.getName());
        }
        post(SAVEPERSONALINFOMATION, params, httpResponseHandler, mcontext);
    }

    public static void changnickname(SavePersonalInfomationEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontetx) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getSaveType())) {
            params.put("saveType", param.getSaveType());
        }
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        if (!StringUtils.isEmpty(param.getNickname())) {
            params.put("nickname", param.getNickname());

        }
        post(SAVEPERSONALINFOMATION, params, httpResponseHandler, mcontetx);
    }

    public static void changesex(SavePersonalInfomationEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getSaveType())) {
            params.put("saveType", param.getSaveType());
        }
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        if (!StringUtils.isEmpty(param.getSex())) {
            params.put("sex", param.getSex());
        }
        post(SAVEPERSONALINFOMATION, params, httpResponseHandler, mcontext);
    }

    public static void changebirthday(SavePersonalInfomationEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getSaveType())) {
            params.put("saveType", param.getSaveType());
        }
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        if (!StringUtils.isEmpty(param.getBirthday())) {
            params.put("birthday", param.getBirthday());
        }
        post(SAVEPERSONALINFOMATION, params, httpResponseHandler, mcontext);
    }

    public static void wechatlogin(APPLoginEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(String.valueOf(param.getApptype()))) {
            params.put("apptype", param.getApptype());
        }
        if (!StringUtils.isEmpty(param.getCode())) {
            params.put("code", param.getCode());
        }
        post(APPLOGIN, params, httpResponseHandler, mcontext);
    }

    public static void wechatloginbindmobile(APPWeChatLoginBindMobileEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getPhone())) {
            params.put("phone", param.getPhone());
        }
        if (!StringUtils.isEmpty(param.getMesscode())) {
            params.put("messcode", param.getMesscode());
        }
        if (!StringUtils.isEmpty(param.getAppopenid())) {
            params.put("appopenid", param.getAppopenid());
        }
//        FormEncodingBuilder builder = new FormEncodingBuilder();
//        if (params != null) {
//            for (String key : params.keySet()) {
//                builder.addEncoded(key, params.get(key).toString());
//            }

        post(WECHATLOGINMOBILEBIND, params, httpResponseHandler, mcontext);
//        Request request = new Request.Builder().url(HTTP_DOMAIN + WECHATLOGINMOBILEBIND).addHeader("access-type", "app").addHeader("Set-Cookie", SharedPreferencesUtils.getInstance().getString("jsessionid",null)).post(builder.build()).build();
//        Log.e("post请求UrL-->", HTTP_DOMAIN + WECHATLOGINMOBILEBIND);
//        enqueue(request, httpResponseHandler, mcontext, true);;

    }
//    public static void apppay(AppPayEntity param,AsyncHttpResponseHandler httpResponseHandler,Context mcontext)
//    {
//        Map<String,Object> params=new HashMap<>();
//        if(!StringUtils.isEmpty(param.getStrdata()))
//        {
//            params.put("strdata",param.getStrdata());
//        }
//        if(!StringUtils.isEmpty(String.valueOf(param.getPaytype())))
//        {
//            params.put("paytype",param.getPaytype());
//        }
//        post(APPPAY,params,httpResponseHandler,mcontext);

    //  }
    public static void promptionspop(PromotionsPopEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        post(PROMOTIONSPOP, params, httpResponseHandler, mcontext);

    }

    public static void creatmember(CreateMemberEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getMunbertype())) {
            params.put("munbertype", param.getMunbertype());
        }
        if (!StringUtils.isEmpty(param.getCusname())) {
            params.put("cusname", param.getCusname());

        }
        if (!StringUtils.isEmpty(param.getCusmunber())) {
            params.put("cusmunber", param.getCusmunber());
        }
        if (!StringUtils.isEmpty(param.getPhoneCode())) {
            params.put("phoneCode", param.getPhoneCode());
        }
        if (!StringUtils.isEmpty(param.getInviteNumber())) {
            params.put("yqm", param.getInviteNumber());
        }
        params.put("source", "1");
        post(CREATEMEMBER, params, httpResponseHandler, mcontext);
    }

    public static void lunbo(StoreInfoEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        get(LUNBO, params, httpResponseHandler, mcontext, true);
    }

    public static void apprefundresaon(APPReFoudEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getOrdernum())) {
            params.put("ordernum", param.getOrdernum());
        }
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());
        }
        post(APPREFUND, params, httpResponseHandler, mcontext);

    }

    public static void fenlei(APPReFoudEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        get(APPFIRSTFENLEI, params, httpResponseHandler, mcontext, true);
    }

    //上传退款原因
    public static void savedrawbackreson(APPReFoudEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getOrdernum())) {
            params.put("ordernum", param.getOrdernum());
        }
        if (!StringUtils.isEmpty(param.getMemo())) {
            params.put("memo", param.getMemo());
        }
        if (!StringUtils.isEmpty(param.getPicurl())) {
            params.put("picurl", param.getPicurl());
        }
        post(SAVEREFUNDREASON, params, httpResponseHandler, mcontext);

    }
    //上传退款原因的图片

    /**
     * 上传图片文件
     *
     * @param param
     * @param httpResponseHandler
     */
    public static void uploadImage(APPReFoudEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (!isNetworkAvailable()) {
            Toast.makeText(mcontext, R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isEmpty(param.getLocalPath())) {
            params.put("localPath", param.getLocalPath());
        }
        Log.i("Post请求交易TAG", "请求url:" + UPLOADPICS + "请求参数:" + JSON.toJSONString(params));
        MediaType mediaType = MediaType.parse("image/jpeg; charset=utf-8");
        File file = new File(param.getLocalPath());
        //RequestBody body = RequestBody.create(mediaType, file);
        RequestBody body = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addFormDataPart("relative_url", file.getName(), RequestBody.create(mediaType, new File(param.getLocalPath())))
                //.addPart(
                //        Headers.of("Content-Disposition", "form-data; name=\"file\""),
                //        RequestBody.create(mediaType, new File(param.getLocalPath())))
                .build();
        Request request = new Request.Builder().url(HTTP_DOMAIN + UPLOADPICS).post(body).build();
        enqueue(request, httpResponseHandler, mcontext, false);
    }

    /**
     * 上传首页头像图片文件
     *
     * @param param
     * @param httpResponseHandler
     */
    public static void uploadUserImage(SavePersonalInfomationEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        //HttpClient.cookieStore.removeAll();
        Map<String, Object> params = new HashMap<String, Object>();
       /* if (!isNetworkAvailable()) {
            Toast.makeText(mcontext, R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (!StringUtils.isEmpty(param.getRelative_ur())) {
            params.put("localpath", param.getRelative_ur());
        }


        Log.e("Post请求交易TAG", "请求url:" + SAVEPERSONALINFOMATION + "请求参数:" + JSON.toJSONString(params));

        MediaType mediaType = MediaType.parse("image/png");
        File file = new File(param.getRelative_ur());

        //RequestBody body = RequestBody.create(mediaType, file);

        RequestBody body = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, file))

                //.addPart(
                //        Headers.of("Content-Disposition", "form-data; name=\"file\""),
                //        RequestBody.create(mediaType, new File(param.getLocalPath())))
                .build();
      /*  FormEncodingBuilder builder = new FormEncodingBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addEncoded(key, params.get(key).toString());
            }
        }*/

        Request request = new Request.Builder().url(HTTP_DOMAIN + SAVEAPPHEADPIC).post(body).build();
        Log.e("ASCEE", HTTP_DOMAIN + SAVEAPPHEADPIC);
        enqueue(request, httpResponseHandler, mcontext, true);
    }

    public static void appsettinguserinfo(StoreInfoEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getDistributorId())) {
            params.put("distributorId", param.getDistributorId());

        }
        post(APPSETTING, params, httpResponseHandler, mcontext);

    }

    public static void popaddress(PromotionsPopEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(param.getBusinessid())) {
            params.put("businessid", param.getBusinessid());

        }
        if (!StringUtils.isEmpty(param.getSource())) {
            params.put("source", param.getSource());
        }
        post(APPPOP, params, httpResponseHandler, mcontext);
    }

    public static void showred(ShowRedPicEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        post(SHOWIMAGE, params, httpResponseHandler, mcontext);
    }

    public static void showRed(ShowRedPicEntity param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        Map<String, Object> params = new HashMap<>();
        post(SHOWRED, params, httpResponseHandler, mcontext);
    }


    /**
     * 上传圈子图片,视频文件
     *
     * @param param
     * @param httpResponseHandler
     */
    public static void sendImage(SendCircleBean param, AsyncHttpResponseHandler httpResponseHandler, Context mcontext) {
        if (!isNetworkAvailable()) {
            Toast.makeText(mcontext, R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        MediaType mediaType = MediaType.parse("multipart/form-data; charset=utf-8");
        MultipartBuilder buildernew = new MultipartBuilder().type(MultipartBuilder.FORM);

        if (param.getType().equals("2")){//视频封面
            if (param.getVideoImg() != null) {
                buildernew.addFormDataPart("relative_url", param.getVideoImg().getName(), RequestBody.create(mediaType, param.getVideoImg()));
            }
        }

        for (int i = 0; i < param.getFileupload().size(); i++) {
            if (param.getFileupload().get(i).getRealPath() != null) {
                buildernew.addFormDataPart("relative_url", new File(param.getFileupload().get(i).getRealPath()).getName(), RequestBody.create(mediaType, new File(param.getFileupload().get(i).getRealPath())));
            }else {
                buildernew.addFormDataPart("relative_url", new File(param.getFileupload().get(i).getPath()).getName(), RequestBody.create(mediaType, new File(param.getFileupload().get(i).getPath())));
            }
        }
        buildernew.addFormDataPart("type",param.getType());
        buildernew.addFormDataPart("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        buildernew.addFormDataPart("productId",param.getProductId());
        buildernew.addFormDataPart("text",param.getText());
        RequestBody requestBody = buildernew.build();
        Request request;
        if (param.getSendType().equals("1")) {//推荐发布
            request = new Request.Builder().url(HTTP_DOMAIN + SEND_CIRCLE).post(requestBody).build();
        }else {
            request = new Request.Builder().url(HTTP_DOMAIN + PERSONSEND_CIRCLE).post(requestBody).build();
        }
        enqueue(request, httpResponseHandler, mcontext, false);
    }

}
