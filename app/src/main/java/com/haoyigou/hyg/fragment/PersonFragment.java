package com.haoyigou.hyg.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.CouponListAdapter;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.CouponBO;
import com.haoyigou.hyg.entity.LableEntry;
import com.haoyigou.hyg.ui.MessageBoxActivty;
import com.haoyigou.hyg.ui.OrderAllAct;
import com.haoyigou.hyg.ui.SettingActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.adapter.CustomGridLayoutManager;
import com.haoyigou.hyg.adapter.PersonRecycleAdapter;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.MenuEntry;
import com.haoyigou.hyg.entity.ShowRedPicEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.UserBean;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.circlephoto.RoundImageView;
import com.haoyigou.hyg.base.BaseFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2016/12/5.
 * <p>
 * 新改版的个人中心页面
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.setting_img)
    ImageView settingImg;
    @BindView(R.id.message_img)
    ImageView messageImg;
    @BindView(R.id.headar_layout)
    RelativeLayout headerLayout;
    @BindView(R.id.head_avatar)
    RoundImageView headAvatar;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.person_price)
    TextView personPrice;
    @BindView(R.id.sign_layout)
    LinearLayout signLayout;
    @BindView(R.id.sign_person)
    TextView signPerson;
    @BindView(R.id.qiandao_layout)
    RelativeLayout qiandaoLayout;
    @BindView(R.id.person_integral)
    TextView personIntegral;
    @BindView(R.id.linear_search_order)
    RelativeLayout linearSearchOrder;
    @BindView(R.id.ll_coupon)
    RelativeLayout ll_coupon;
    @BindView(R.id.fukuan_point)
    TextView fukuanPoint;
    @BindView(R.id.dai_fukuan)
    RelativeLayout daiFukuan;
    @BindView(R.id.fahuo_point)
    TextView fahuoPoint;
    @BindView(R.id.dai_fahuo)
    RelativeLayout daiFahuo;
    @BindView(R.id.shouhuo_point)
    TextView shouhuoPoint;
    @BindView(R.id.dai_shouhuo)
    RelativeLayout daiShouhuo;
    @BindView(R.id.tuikuan_point)
    TextView tuikuanPoint;
    @BindView(R.id.tuikuan_shouhou)
    RelativeLayout tuikuanShouhou;
    @BindView(R.id.manage_recycle)
    RecyclerView manageRecycle;
    @BindView(R.id.coupon_recycle)
    RecyclerView coupon_recycle;
//    @InjectView(R.id.task_recycle)
//    RecyclerView taskRecycle;
    @BindView(R.id.scollview)
    NestedScrollView scollview;
    @BindView(R.id.sign_num)
    TextView signNum;
    @BindView(R.id.my_price_layout)
    LinearLayout myPriceLayout;
    @BindView(R.id.my_integral_layout)
    LinearLayout myIntegralLayout;
    @BindView(R.id.text01)
    TextView text01;
    @BindView(R.id.person_cash)
    TextView personCash;
    @BindView(R.id.my_cash_layout)
    LinearLayout myCashLayout;
    @BindView(R.id.level_icon)
    ImageView level_icon;
    @BindView(R.id.level_name)
    TextView level_name;
    @BindView(R.id.rlNoCoupons)
    RelativeLayout rlNoCoupons;

    private PersonRecycleAdapter manageAdapter;  //管理中心适配器
    private PersonRecycleAdapter taskAdapter;    //任务中心适配器
    private CouponListAdapter couponListAdapter;//优惠券适配器
    private List<CouponBO> list = new ArrayList<>();

    private ArrayList<Integer> image_icon;

    List<String> maageTexts;
    List<Integer> maageImage;
    List<String> taskTexts;
    List<Integer> taskImages;

    private int personGold = 0;
    private int onReadnum = 0;   //阅读上面的小红点，默认不显示

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_fragment, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        invitionRecycle();
        setListener();
        getCoupon();
    }

    /***
     * 初始化控件属性
     */
    private void invitionRecycle() {
        CustomGridLayoutManager manager = new CustomGridLayoutManager(getActivity(), 4);
//        manager.setScrollEnabled(false);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manageRecycle.setNestedScrollingEnabled(false);
        manageRecycle.setHasFixedSize(true);
        manageRecycle.setLayoutManager(manager);
        CustomGridLayoutManager manager2 = new CustomGridLayoutManager(getActivity(), 4);
//        manager2.setScrollEnabled(false);
        manager2.setOrientation(GridLayoutManager.VERTICAL);
//        taskRecycle.setLayoutManager(manager2);
//        taskRecycle.setNestedScrollingEnabled(false);
//        taskRecycle.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        coupon_recycle.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        setFlgsbg(R.color.live_slider);
//        setStatusBar();
    }

    public void setFlgsbg(int color) {
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    /***
     * 设置点击事件
     */
    private void setListener() {
        linearSearchOrder.setOnClickListener(this);
        daiFukuan.setOnClickListener(this);
        daiFahuo.setOnClickListener(this);
        daiShouhuo.setOnClickListener(this);
        tuikuanShouhou.setOnClickListener(this);
        qiandaoLayout.setOnClickListener(this);
        signLayout.setOnClickListener(this);
        headerLayout.setOnClickListener(this);
        settingImg.setOnClickListener(this);
        myPriceLayout.setOnClickListener(this);
        myIntegralLayout.setOnClickListener(this);
        messageImg.setOnClickListener(this);
        myCashLayout.setOnClickListener(this);
        ll_coupon.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Long click) {
        String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
//        signMessage(disid);
    }


    /***
     * 设置显示
     */
    private void setRecycleMessage() {
        if (StateMessage.IS_LOGIN && GlobalApplication.user != null) {
            String pic = GlobalApplication.user.getHeaderpic();
            nickName.setText(GlobalApplication.user.getNickname());
            if (GlobalApplication.user.getLevel_type().equals("1")){
                Picasso.with(getActivity()).load(R.drawable.zhuce).into(level_icon);
                level_name.setText(GlobalApplication.user.getLevel_name());
            }else if (GlobalApplication.user.getLevel_type().equals("2")){
                Picasso.with(getActivity()).load(R.drawable.xing).into(level_icon);
                level_name.setText(GlobalApplication.user.getLevel_name());
            }else if (GlobalApplication.user.getLevel_type().equals("3")){
                Picasso.with(getActivity()).load(R.drawable.neibu).into(level_icon);
                level_name.setText(GlobalApplication.user.getLevel_name());
            }
            String hassigin = GlobalApplication.user.getHassigin();
            if (!StringUtils.isEmpty(pic)) {   //显示头像
                if ("/".equals(pic.substring(0, 1))) {
                    Picasso.with(getActivity()).load(HttpClient.HTTP_DOMAIN + pic).into(headAvatar);
                } else {
                    Picasso.with(getActivity()).load(pic).into(headAvatar);
                }
            } else {
                headAvatar.setImageResource(R.drawable.logo_tubiao);
            }
            if (hassigin != null) {
                if (hassigin.equals("1")) {
                    signLayout.setVisibility(View.VISIBLE);
                    qiandaoLayout.setVisibility(View.GONE);
                } else {
                    signLayout.setVisibility(View.GONE);
                    qiandaoLayout.setVisibility(View.VISIBLE);
                }
            }
            inviManage();
            inviTask();
        }
    }

    /***
     * 为管理中心添加数据
     */
    private void inviManage() {
        String bind = GlobalApplication.user.getBind();
        String interted = GlobalApplication.user.getInvitation();
        String newHave = GlobalApplication.user.getNewhave();
        if (maageTexts == null) {
            maageTexts = new ArrayList<>();
        }
        if (maageImage == null) {
            maageImage = new ArrayList<>();
        }
        maageTexts.clear();
        maageImage.clear();
        maageTexts.add("积分商城");
        maageImage.add(R.mipmap.score_store_icon);
//        maageTexts.add("充值中心");
//        maageImage.add(R.mipmap.four_recharge);
        maageTexts.add("电子发票");
        maageImage.add(R.mipmap.invoice);
        maageTexts.add("拼团订单");
        maageImage.add(R.mipmap.pintuan);
        maageTexts.add("邀请好友");
        maageImage.add(R.mipmap.four_recommend);
        maageTexts.add("我的收藏");
        maageImage.add(R.mipmap.four_favorite);
        maageTexts.add("地址管理");
        maageImage.add(R.mipmap.four_college);
        maageTexts.add("在线客服");
        maageImage.add(R.mipmap.four_contact);
        maageTexts.add("我的粉丝");
        maageImage.add(R.mipmap.four_team);
        if (bind != null) {
            if (bind.equals("0")) {
                maageTexts.add("绑定会员");
                maageImage.add(R.mipmap.four_bound);
            }
        }
        if (manageAdapter != null) {
            manageAdapter.notifyDataSetChanged();
            showimage();
            return;
        }
        manageAdapter = new PersonRecycleAdapter(getActivity(), maageTexts, maageImage);
        manageAdapter.setonDissmissListener(new PersonRecycleAdapter.onDissmissListener() {
            @Override
            public void onDissmiss() {
                loaduserinfo();
                showred();
            }
        });
//        if (couponListAdapter != null){
//            couponListAdapter.notifyDataSetChanged();
//        }
//        couponListAdapter = new CouponListAdapter(getActivity(),list);
        manageRecycle.setAdapter(manageAdapter);
//        coupon_recycle.setAdapter(couponListAdapter);
        setCustomMenu();
        showimage();
    }

    /**
     * 为任务中心添加数据
     */
    private void inviTask() {
        String gameopen = GlobalApplication.user.getGameopen();
        String mission = GlobalApplication.user.getMission();
        if (taskTexts == null) {
            taskTexts = new ArrayList<>();
        }
        if (taskImages == null) {
            taskImages = new ArrayList<>();
        }
        taskTexts.clear();
        taskImages.clear();
        taskTexts.add("推荐");
        taskImages.add(R.drawable.four_recommend);
        taskTexts.add("阅读");
        taskImages.add(R.drawable.four_read);
        if (mission != null) {
            if (mission.equals("1")) {
                taskTexts.add("新手任务");
                taskImages.add(R.drawable.four_task);
            }
        }
        if (taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
        taskAdapter = new PersonRecycleAdapter(getActivity(), taskTexts, taskImages);
        if (gameopen != null) {
            if (!gameopen.equals("1")) {   //游戏不能玩，设置加锁
                taskAdapter.IsHaveGame(false);
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), PersonWebViewAct.class);
        String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
        switch (view.getId()) {
            case R.id.linear_search_order:   //全部订单
                intent = new Intent(getActivity(), OrderAllAct.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.dai_fukuan:   //代付款
                intent = new Intent(getActivity(), OrderAllAct.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.dai_fahuo:    //待付款
                intent = new Intent(getActivity(), OrderAllAct.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.dai_shouhuo:   //待收货
                intent = new Intent(getActivity(), OrderAllAct.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.ll_coupon://优惠券
                intent.putExtra("url", HttpClient.VALUEVOUCHER + "?distributorId=" + disid);
                startActivity(intent);
                break;
            case R.id.tuikuan_shouhou:   //退款/售后
                intent = new Intent(getActivity(), OrderAllAct.class);
                intent.putExtra("type", 4);
                startActivity(intent);
                break;
            case R.id.qiandao_layout:   //签到
            case R.id.sign_layout:
//                signMessage(disid);
                Intent intent2 = new Intent(getActivity(), HomeWebViewAct.class);
                intent2.putExtra("all", false);
                intent2.putExtra("url", "/point/sign"+"?distributorId=" + disid);
                startActivity(intent2);
                break;
            case R.id.headar_layout:   //点击头像
            case R.id.setting_img:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.my_price_layout:   //我的金币
                intent.putExtra("url", HttpClient.MYPRICE + "?distributorId=" + disid);
                startActivity(intent);
                break;
            case R.id.my_integral_layout:   //我的积分
                intent.putExtra("url", HttpClient.MYINTERNAGER + "?distributorId=" + disid);
                startActivity(intent);
                break;
            case R.id.message_img:     //  消息盒子
                messageImg.setImageResource(R.mipmap.person_msg);
                StateMessage.haveMes = "0";
                goToActivity(MessageBoxActivty.class, false);
                break;
            case R.id.my_cash_layout:   //现金账户
                intent.putExtra("url", HttpClient.MYREFLECT + "?distributorId=" + disid);
                startActivity(intent);
                break;
        }
    }


    /***
     * 签到接口调用
     */
//    private void signMessage(String disId) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("distributorId", disId);
//        params.put("source", "1");
//        HttpClient.post(HttpClient.SIGNIN, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String content) {
//                super.onSuccess(content);
//                Log.e("log--sign", content);
//                if (content == null || content.equals("")) {
//                    Toast.makeText(getActivity(), "签到失败!", Toast.LENGTH_SHORT).show();
//                }
//                JSONObject object = JSON.parseObject(content);
//                if (object.getString("successFlag").equals("1")) {
//                    int num = object.getInteger("points");
//                    personGold += num;
//                    personPrice.setText(personGold + "");
//                    signLayout.setVisibility(View.VISIBLE);
//                    qiandaoLayout.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), "恭喜你，获取" + num + "个金币！", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), object.getString("error"), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, getActivity());
//    }


    /***
     * 优惠券接口
     */
    private void getCoupon() {
        Map<String, Object> params = new HashMap<>();

        HttpClient.post(HttpClient.COUPON, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--sign", content);
                BaseResult baseResult = JSONObject.parseObject(content, BaseResult.class);
                list.clear();
                list = JSONArray.parseArray(baseResult.getData(),CouponBO.class);
                if (list != null && list.size()>0){
                    coupon_recycle.setVisibility(View.VISIBLE);
                    rlNoCoupons.setVisibility(View.GONE);
                    couponListAdapter = new CouponListAdapter(getActivity(),list);
                    coupon_recycle.setAdapter(couponListAdapter);
                }else {
                    coupon_recycle.setVisibility(View.GONE);
                    rlNoCoupons.setVisibility(View.VISIBLE);
                }
                if (couponListAdapter != null) {
                    couponListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
            }
        }, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (StateMessage.haveMes.equals("0")) {   //无未读消息
            messageImg.setImageResource(R.mipmap.person_msg);
        } else {
            messageImg.setImageResource(R.mipmap.person_msg);
        }
        loaduserinfo();
        showred();
        getCoupon();
//        getCoupon();
    }

    /***
     * 更新用户信息
     */
    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        HttpClient.userinfo(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getMobileCodeuser", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if ("1".equals(object.getString("status"))) {
                    UserBean bean = JSON.parseObject(body, UserBean.class);
                    GlobalApplication.setUserBean(bean);
                    setRecycleMessage();
                }
            }

            public void onFailure(Request request, IOException e) {
            }
        }, getActivity());
    }

    /**
     * 获取自定义菜单数据
     */
    private void setCustomMenu() {
        HttpClient.post(HttpClient.CUSTOMMUNE, new HashMap<String, Object>(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--custom", content);
                if (StringUtils.isEmpty(content)) return;
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String data = object.getString("result");
                    List<MenuEntry> lists = JSONArray.parseArray(data, MenuEntry.class);
                    if (manageAdapter != null) {
                        manageAdapter.setCustomMenu(lists);
                    }
                }
            }
        }, getActivity());
    }


    //个人中心代发货 代付款 红色标记
    private void showred() {
        ShowRedPicEntity param = new ShowRedPicEntity();
        HttpClient.showRed(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getRed", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    if (object.getInteger("1") >= 1) {
                        fukuanPoint.setVisibility(View.VISIBLE);
                        fukuanPoint.setText(object.getInteger("1") + "");
                    } else {
                        fukuanPoint.setVisibility(View.GONE);
                    }
                    if (object.getInteger("2") >= 1) {
                        fahuoPoint.setVisibility(View.VISIBLE);
                        fahuoPoint.setText(object.getInteger("2") + "");
                    } else {
                        fahuoPoint.setVisibility(View.GONE);
                    }
                    if (object.getInteger("3") >= 1) {
                        shouhuoPoint.setVisibility(View.VISIBLE);
                        shouhuoPoint.setText(object.getInteger("3") + "");
                    } else {
                        shouhuoPoint.setVisibility(View.GONE);
                    }
                    if (object.getInteger("4") >= 1) {
                        tuikuanPoint.setVisibility(View.VISIBLE);
                        tuikuanPoint.setText(object.getInteger("4") + "");
                    } else {
                        tuikuanPoint.setVisibility(View.GONE);
                    }
                    personGold = object.getInteger("5");
                    personPrice.setText(String.valueOf(personGold));   //金币
                    signNum.setText("+" + object.getString("6"));   //签到增加的金币
                    personIntegral.setText(object.getString("7"));  //积分
                    personCash.setText(object.getString("9"));
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, getActivity());
    }


    //个人中心girdview的红色标记
    private void showimage() {
        image_icon = new ArrayList<>();
        ShowRedPicEntity param = new ShowRedPicEntity();
        HttpClient.showred(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getRedCode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    LableEntry entry = JSONObject.parseObject(body, LableEntry.class);
//                    image_icon.add(Integer.parseInt(object.getString("1")));
//                    image_icon.add(Integer.parseInt(object.getString("2")));
//                    image_icon.add(Integer.parseInt(object.getString("3")));
//                    image_icon.add(Integer.parseInt(object.getString("4")));
//                    image_icon.add(Integer.parseInt(object.getString("5")));
//                    image_icon.add(Integer.parseInt(object.getString("6")));
//                    image_icon.add(Integer.parseInt(object.getString("7")));
//                    image_icon.add(Integer.parseInt(object.getString("8")));
//                    image_icon.add(Integer.parseInt(object.getString("9")));
//                    image_icon.add(Integer.parseInt(object.getString("10")));
                    onReadnum = Integer.parseInt(entry.getRead());
                    if (taskAdapter != null) {
                        if (onReadnum >= 1) {
                            taskAdapter.IsReadPoint(true);
                        }
                    }
                    manageAdapter.setLable(entry);
//                    manageAdapter.setIcons(image_icon);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
