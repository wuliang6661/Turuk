package com.haoyigou.hyg.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.GenerateTestUserSig;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.HistoryBean;
import com.haoyigou.hyg.entity.LiveDetailBean;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.MessageEvent;
import com.haoyigou.hyg.utils.NetworkUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.TimeUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.utils.fw_permission.FloatWinPermissionCompat;
import com.haoyigou.hyg.view.circlephoto.RoundImageView;
import com.haoyigou.hyg.view.floatview.FloatWindowManager;
import com.haoyigou.hyg.view.viewpager.ViewPagerSlide;
import com.haoyigou.hyg.view.widget.SharePopupWindow;
import com.kaisengao.likeview.like.KsgLikeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.yunfan.player.widget.YfCloudPlayer;
import com.yunfan.player.widget.YfPlayerKit;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelLinearLayout;
import cn.jzvd.Jzvd;
import tcking.github.com.giraffeplayer.GiraffePlayer;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;

/**
 * Created by Witness on 2019-12-11
 * Describe: 直播界面
 */
public class ShowingRoom extends BaseFloatActivity implements View.OnClickListener {

    @BindView(R.id.viewPager)
    ViewPagerSlide viewPager;
    @BindView(R.id.talkList)
    RecyclerView talkList;
    @BindView(R.id.refresh_root)
    SmartRefreshLayout refreshRoot;
    @BindView(R.id.rlMain)
    RelativeLayout rlMain;
    @BindView(R.id.aboveName)
    TextView aboveName;
    @BindView(R.id.aboveContent)
    TextView aboveContent;
    @BindView(R.id.aboveTextLayout)
    LinearLayout aboveTextLayout;
    @BindView(R.id.live_view)
    KsgLikeView mLikeView;
    @BindView(R.id.headImage)
    RoundImageView headImage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.seeNum)
    TextView seeNum;
    @BindView(R.id.closeRoom)
    RelativeLayout closeRoom;
    @BindView(R.id.focusTxt)
    TextView focusTxt;
    @BindView(R.id.focusBtn)
    RelativeLayout focusBtn;
    @BindView(R.id.sendGift)
    ImageView sendGift;
    @BindView(R.id.headLayout)
    RelativeLayout headLayout;
    @BindView(R.id.layoutView)
    RelativeLayout layoutView;
    @BindView(R.id.productNumMain)
    TextView productNumMain;

    /**
     * 直播界面
     */
    private ImageView shopBtn;
//    private TextView productNumMain;
    private TextView inputEdit;
    private RelativeLayout rlSend;
    private Button sendBtn;
    private ImageView likeBtn;
    private ImageView shareBtn;
    private RelativeLayout bottomLayout;
    private TextView mlikeNum;//点赞数量


    private boolean isFocus = false;//是否关注了主播
    private boolean isLike = false;//是否喜欢

    public static String TAG = "ShowingRoom";
    /**
     * 弹幕列表
     */
    private CommonAdapter<HistoryBean> adapter;
    private List<String> data = new ArrayList<>();
    private List<TIMMessage> msgList = new ArrayList<>();
    private LinearLayoutManager manager;
    /**
     * 商品弹窗列表
     */
    private CommonAdapter<LiveDetailBean.ProductListBean> productAdapter;
    private List<LiveDetailBean.ProductListBean> productData = new ArrayList<>();

    /**
     * viewPager相关
     */
    private List<View> viewListData = new ArrayList<>();//view数组
    private PagerAdapter pagerAdapter;
    private View liveView;
    private View emptyView;

    private KPSwitchFSPanelLinearLayout panelRoot;
    private String groupId = "";//群组id
    private String liveUrl = "";

    private SharePopupWindow popWindow = null;//分享弹窗

    private LiveDetailBean detail;
    private String personName = "";//主播名字
    private String personHeader = "";//主播头像
    private int likeNum = 1;//点赞数量
    private String roomId;//房间id
    private String couponId = "";//礼品ID
    private String myNickName = "";//自己的昵称

    private boolean isActivityShow = true;

    private GiraffePlayer player;
    private Integer[] images = {R.mipmap.gift1, R.mipmap.gift2, R.mipmap.gift3, R.mipmap.gift4, R.mipmap.gift5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initMainView();
        player = new GiraffePlayer(this);
        mLikeView.addLikeImages(images);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.show_room_layout;
    }

    @Override
    protected void initData() {
        floatWindowType = FloatWindowManager.FW_TYPE_ALERT_WINDOW;
        if (getIntent().getStringExtra("id") != null) {
            startProgressDialog("", ShowingRoom.this);
            getLiveDetail(getIntent().getStringExtra("id"));
        } else {
            ToastUtils.showToast(ShowingRoom.this, "房间打开失败,请退出重试");
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        closeFloatWindow();
        EventBus.getDefault().post(new MessageEvent("closeSound", ""));
        isActivityShow = true;
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivityShow = false;
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        quitGroup();
        focusTimer.cancel();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    protected void onFloatWindowClick() {
        super.onFloatWindowClick();
        EventBus.getDefault().post(new MessageEvent("closeSound", ""));
        EventBus.getDefault().post(new MessageEvent("HomeWebViewAct", "finishActivity"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageType().equals("permission")) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 200);
            }
        }else if (messageEvent.getMessageType().equals("product")){
            showProductDialog();
            if (StateMessage.IS_LOGIN) {
                sendMes(myNickName + "正在购买");
            }
        }else if (messageEvent.getMessageType().equals("input")){
            showInputLayout();
            showSoft();
        }else if (messageEvent.getMessageType().equals("like")){
            if (StateMessage.IS_LOGIN) {
                if (roomId != null && groupId != null) {
                    goLike(roomId, groupId);
                }
            } else {
//                                heart.addHeart(1);
                mLikeView.addFavor();
                likeNum++;
                mlikeNum.setText(String.valueOf(likeNum));
                if (player != null) {
                    player.setLikeNum(String.valueOf(likeNum));
                }
                if (roomId != null && groupId != null) {
                    goLike(roomId, groupId);
                }
            }
        }else if (messageEvent.getMessageType().equals("share")){
            popWindow.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
        }
    }

    TIMConversation conversation;

    protected void initMainView() {
        ButterKnife.bind(this);
        LayoutInflater inflater = getLayoutInflater();
        liveView = inflater.inflate(R.layout.live_view, null);
        emptyView = inflater.inflate(R.layout.live_empty, null);
        initLiveView(liveView);//初始化搜索页面
        viewListData.add(emptyView);
        viewListData.add(liveView);
        setViewPagerAdapter();
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    talkList.setVisibility(View.GONE);
                    aboveTextLayout.setVisibility(View.GONE);
                } else {
                    talkList.setVisibility(View.VISIBLE);
                    aboveTextLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        popWindow = new SharePopupWindow(ShowingRoom.this);
    }

    /**
     * 初始化播放操作界面
     **/
    private void initLiveView(View view) {
        shopBtn = view.findViewById(R.id.shopBtn);
        inputEdit = view.findViewById(R.id.inputEdit);
        rlSend = view.findViewById(R.id.rlSend);
        sendBtn = view.findViewById(R.id.sendBtn);
//        productNumMain = view.findViewById(R.id.productNumMain);
        likeBtn = view.findViewById(R.id.likeBtn);
        shareBtn = view.findViewById(R.id.shareBtn);
        bottomLayout = view.findViewById(R.id.bottomLayout);
        mlikeNum = view.findViewById(R.id.likeNum);
        panelRoot = view.findViewById(R.id.panel_root);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        talkList.setLayoutManager(manager);
        talkList.setHasFixedSize(true);

        ViewGroup.LayoutParams paramsTvbg = rlSend.getLayoutParams();
        paramsTvbg.width = (int) (DisplayUtils.getScreenWidth(this) * 0.5);
        rlSend.setLayoutParams(paramsTvbg);


        focusBtn.setOnClickListener(this);
        closeRoom.setOnClickListener(this);
        shopBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        rlSend.setOnClickListener(this);
        sendGift.setOnClickListener(this);
        /** 防止键盘弹出过程中屏幕整体上移 */
//        KeyboardUtil.attach(this, panelRoot);
//        KPSwitchConflictUtil.attach(panelRoot, null, inputEdit,
//                new KPSwitchConflictUtil.SwitchClickListener() {
//                    @Override
//                    public void onClickSwitch(boolean switchToPanel) {
//                        if (switchToPanel) {
//                            inputEdit.clearFocus();
//                        } else {
//                            inputEdit.requestFocus();
//                        }
//                    }
//                });

        talkList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
//                    smoothMoveToPosition(talkList, mToPosition);
                }
            }
        });

    }

    /**
     * 设置腾讯IM相关
     */
    private void setIM() {
        //判断是否登录IM
        if (TIMManager.getInstance().getLoginUser() != null &&
                TIMManager.getInstance().getLoginUser().length() != 0) {//登录了
            getAndQuitGroup();
            joinGroup();
        } else {//没登录
            String userName;
            String userSig;
            if (StateMessage.IS_LOGIN) {//登录则用登录手机号
                userName = SharedPreferencesUtils.getInstance().getString("userPhone", "");
                userSig = GenerateTestUserSig.genTestUserSig(userName);

                TIMManager.getInstance().login(userName, userSig, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 列表请参见错误码表
                        Log.d("腾讯IM登录", "错误代码" + code + "错误信息" + desc);
                        ToastUtils.showToast(ShowingRoom.this, "加入房间失败" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("腾讯IM登录", "成功");
                        getAndQuitGroup();
                        joinGroup();
                    }
                });
            } else {//未登录用毫秒时间戳
                // 登录腾讯IM,username 为用户名，userSig 为用户登录凭证
                userName = String.valueOf(TimeUtils.getNowTimeMills());
                userSig = GenerateTestUserSig.genTestUserSig(String.valueOf(TimeUtils.getNowTimeMills()));
            }

        }

    }

    /**
     * 获取加入的群并退出
     */
    private void getAndQuitGroup() {
        //退出群组回调
        final TIMCallBack quit = new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
            }

            @Override
            public void onSuccess() {
//                Log.e(tag, "quit group succ");
            }
        };

        //创建回调
        TIMValueCallBack<List<TIMGroupBaseInfo>> cb = new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
//                Log.e(tag, "get gruop list failed: " + code + " desc");
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupInfos) {//参数返回各群组基本信息
                for (TIMGroupBaseInfo info : timGroupInfos) {
                    //获取所有群组id,并且退出群组
                    if (!info.getGroupId().equals(groupId)) {
                        TIMGroupManager.getInstance().quitGroup(
                                info.getGroupId(),  //群组 ID
                                quit);      //回调
                    }
                }
            }
        };
        //获取已加入的群组列表
        TIMGroupManager.getInstance().getGroupList(cb);
    }

    /**
     * 加入群
     */
    private void joinGroup() {
        TIMGroupManager.getInstance().applyJoinGroup(groupId, "some reason", new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //接口返回了错误码 code 和错误描述 desc，可用于原因
                //错误码 code 列表请参见错误码表
//                Log.e("加入群", "applyJoinGroup err code = " + code + ", desc = " + desc);
                if (code == 10013) {//已经在该群中
                    if (StateMessage.IS_LOGIN && GlobalApplication.user != null) {
                        if (!myNickName.equals("")) {
                            updateProfile(myNickName);//设置昵称
                        } else {
                            updateProfile(GlobalApplication.user.getNickname());//设置昵称
                        }
                        getCloudMessage();
                    } else {
//                        updateProfile("游客");//设置昵称
                    }
                } else {
                    ToastUtils.showToast(ShowingRoom.this, "当前发言人数过多，请稍后重试");
                }
            }

            @Override
            public void onSuccess() {
                Log.i("加入群", "applyJoinGroup success");
                if (StateMessage.IS_LOGIN && GlobalApplication.user != null) {
                    if (!myNickName.equals("")) {
                        updateProfile(myNickName);//设置昵称
                    } else {
                        updateProfile(GlobalApplication.user.getNickname());//设置昵称
                    }
                    getCloudMessage();
                } else {
//                    updateProfile("游客");//设置昵称
                }
            }
        });

    }

    /**
     * 设置昵称
     */
    private void updateProfile(final String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        // 昵称
        hashMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, name);
        TIMFriendshipManager.getInstance().modifySelfProfile(hashMap, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
//                Log.e("ShowingRoom", "修改昵称" + s);
            }

            @Override
            public void onSuccess() {
                Log.e("ShowingRoom", "修改昵称成功");
                sendFirstMes(name);
            }
        });
    }

    /**
     * 获取历史消息
     */
    private void getCloudMessage() {
        //获取会话扩展实例
        TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
        //获取群聊会话
        conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,      //会话类型：群组
                groupId);                       //群组 ID

        //获取此会话的消息
        conversation.getMessage(10, //获取此会话最近的 50 条消息
                null, //不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
                new TIMValueCallBack<List<TIMMessage>>() {//回调接口
                    @Override
                    public void onError(int code, String desc) {//获取消息失败
                        //接口返回了错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 含义请参见错误码表
                        Log.d("", "get message failed. code: " + code + " errmsg: " + desc);
                    }

                    @Override
                    public void onSuccess(List<TIMMessage> msgs) {//获取消息成功
                        //消息的内容解析请参考消息收发文档中的消息解析说明
                        List<TIMMessage> msgList = new ArrayList<>(msgs);
                        Collections.reverse(msgList);//对histories 集合中的数据进行倒叙排序
                        resloveMessage(msgList, 2);
                    }
                });
    }

    /**
     * 发消息,刚进入的时候
     */
    private void sendFirstMes(final String name) {
        if (name.startsWith("1") && name.length() == 11) {
            sendMes(name.substring(0, 3) + "****" + name.substring(7) + " 进来了");//
        } else {
            if (name.length() > 8) {
                sendMes(name.substring(0, 8) + "..." + " 进来了");//
            } else {
                sendMes(name + " 进来了");//
            }

        }
//        getGroupInfo(groupId);
        //设置消息监听器，收到新消息时，通过此监听器回调
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {//消息监听器
            @Override
            public boolean onNewMessages(List<TIMMessage> msgs) {//收到新消息
                //消息的内容解析请参考消息收发文档中的消息解析说明
                resloveMessage(msgs, 1);
                return false;
            }
        });
    }

    /**
     * 处理消息
     *
     * @param msgs
     */
    private void resloveMessage(List<TIMMessage> msgs, final int messageType) {//messageType 1 普通 2 历史
        if (null != msgs && msgs.size() > 0) {
            for (final TIMMessage msg : msgs) {
                TIMConversation conversation = msg.getConversation();
                TIMConversationType type = conversation.getType();
                if (type == TIMConversationType.C2C) {

                } else if (type == TIMConversationType.Group) {
                    final TIMElem elem = msg.getElement(0);
                    if (elem.getType() == TIMElemType.Text) {//文本消息
                        msg.getSenderProfile(new TIMValueCallBack<TIMUserProfile>() {
                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onSuccess(TIMUserProfile timUserProfile) {
                                if (timUserProfile.getIdentifier().equals("jzkj")) {//系统消息
                                    final TIMTextElem textElem = (TIMTextElem) elem;
                                    String text = textElem.getText();
                                    List<String> list = Arrays.asList(text.split("&"));//第一项 type  第二项  id  第三项  内容
                                    if (roomId.equals(list.get(1))) {//与当前房间id相匹配
                                        switch (list.get(0)) {
                                            case "1"://浏览量
                                                seeNum.setText(String.format("%s 人", list.get(2)));
                                                break;
                                            case "2"://发券
                                                if (messageType == 1) {
                                                    sendGift.setVisibility(View.VISIBLE);
                                                    couponId = list.get(2);
                                                }
                                                break;
                                            case "3"://关注
                                                aboveTextLayout.setVisibility(View.VISIBLE);
                                                aboveName.setText(String.format("%s", list.get(2)));
                                                aboveContent.setText(" 关注了主播");
                                                aboveTextLayout.setBackground(getResources().getDrawable(R.drawable.above_bg_zi));
                                                break;
                                            case "4"://点赞
                                                if (messageType == 1) {
                                                    if (Integer.valueOf(list.get(2)) > 100000) {
                                                        mlikeNum.setText(String.format("%s万", div(list.get(2), "10000", 1)));
//                                                        heart.addHeart(1);
                                                        mLikeView.addFavor();
                                                        if (player != null) {
                                                            player.setLikeNum(String.format("%s万", div(list.get(2), "10000", 1)));
                                                        }

                                                    } else {
                                                        mlikeNum.setText(list.get(2));
//                                                        heart.addHeart(1);
                                                        mLikeView.addFavor();
                                                        if (player != null) {
                                                            player.setLikeNum(list.get(2));
                                                        }
                                                    }
                                                }
                                                break;
                                            case "5"://隐藏红包
                                                if (list.get(2).equals(couponId)) {
                                                    sendGift.setVisibility(View.GONE);
                                                }

                                                break;
                                        }
                                    }
                                } else {//正常发送消息
                                    final TIMTextElem textElem = (TIMTextElem) elem;
                                    String text = textElem.getText();
                                    if (text.contains("正在购买")) {//购买商品
                                        aboveTextLayout.setVisibility(View.VISIBLE);
                                        String nickName;
                                        if (timUserProfile.getNickName().length() > 1 && timUserProfile.getNickName().startsWith("1") && timUserProfile.getNickName().length() == 11) {
                                            nickName = timUserProfile.getNickName().substring(0, 3) + "****" + timUserProfile.getNickName().substring(7);//
                                        } else {
                                            nickName = timUserProfile.getNickName();//
                                            if (nickName.length() > 8) {
                                                nickName = nickName.substring(0, 8) + "...";
                                            }
                                        }
                                        aboveName.setText(String.format("%s", nickName));
                                        aboveContent.setText(" 正在购买");
                                        aboveTextLayout.setBackground(getResources().getDrawable(R.drawable.above_bg_huang));
                                    } else if (text.contains("进来了")) {
                                        aboveTextLayout.setVisibility(View.VISIBLE);
                                        String nickName;
                                        if (timUserProfile.getNickName().length() > 1 && timUserProfile.getNickName().startsWith("1") && timUserProfile.getNickName().length() == 11) {
                                            nickName = timUserProfile.getNickName().substring(0, 3) + "****" + timUserProfile.getNickName().substring(7);//
                                        } else {
                                            nickName = timUserProfile.getNickName();//
                                            if (nickName.length() > 8) {
                                                nickName = nickName.substring(0, 8) + "...";
                                            }
                                        }
                                        aboveName.setText(String.format("%s", nickName));
                                        aboveContent.setText(" 进来了");
                                        aboveTextLayout.setBackground(getResources().getDrawable(R.drawable.above_bg_hong));
                                    } else {
                                        //处理消息
                                        TIMElem elem = msg.getElement(0);
                                        final TIMTextElem textElemHis = (TIMTextElem) elem;
                                        msg.getSenderProfile(new TIMValueCallBack<TIMUserProfile>() {
                                            @Override
                                            public void onError(int i, String s) {

                                            }

                                            @Override
                                            public void onSuccess(TIMUserProfile timUserProfile) {
                                                String nickName;
                                                if (timUserProfile.getNickName().length() > 1 && timUserProfile.getNickName().startsWith("1") && timUserProfile.getNickName().length() == 11) {
                                                    nickName = timUserProfile.getNickName().substring(0, 3) + "****" + timUserProfile.getNickName().substring(7);//
                                                } else {
                                                    nickName = timUserProfile.getNickName();//
                                                    if (nickName.length() > 8) {
                                                        nickName = nickName.substring(0, 8) + "...";
                                                    }
                                                }
                                                historyBeanList.add(new HistoryBean(textElemHis.getText(), nickName));
                                                adapter.notifyDataSetChanged();
                                                smoothMoveToPosition(talkList, talkList.getBottom());
                                            }
                                        });
//                                        msgList.add(msg);
                                    }
                                }
                            }
                        });
                    }
                } else if (type == TIMConversationType.System) {

                }
            }
        }
    }

    /**
     * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public String div(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("保留的小数位数必须大于零");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal divide = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
        return divide.toString();
    }


    /**
     * 发送消息
     */
    private void sendMes(String message) {
        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(message);

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d("", "addElement failed");
            return;
        }

        if (conversation == null) {
            ToastUtils.showToast(ShowingRoom.this, "暂时无法发送消息,请退出重试");
            if (pop != null) {
                pop.dismiss();
            }
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 含义请参见错误码表
//                Log.d("发送消息", "send message failed. code: " + code + " errmsg: " + desc);
                ToastUtils.showToast(ShowingRoom.this, "当前发言人数过多，请稍后重试");
                if (pop != null) {
                    pop.dismiss();
                }
            }

            @Override
            public void onSuccess(final TIMMessage msg) {//发送消息成功
                Log.e("发送消息", "SendMsg ok");
                if (pop != null) {
                    pop.dismiss();
                }
                TIMConversation conversation = msg.getConversation();
                TIMConversationType type = conversation.getType();
                if (type == TIMConversationType.C2C) {

                } else if (type == TIMConversationType.Group) {
                    final TIMElem elem = msg.getElement(0);
                    if (elem.getType() == TIMElemType.Text) {//文本消息
                        msg.getSenderProfile(new TIMValueCallBack<TIMUserProfile>() {
                            @Override
                            public void onError(int i, String s) {
                                Log.e("", s);
                            }

                            @Override
                            public void onSuccess(TIMUserProfile timUserProfile) {
                                if (timUserProfile.getIdentifier().equals("jzkj")) {//系统消息
                                    final TIMTextElem textElem = (TIMTextElem) elem;
                                    String text = textElem.getText();
                                    List<String> list = Arrays.asList(text.split("&"));//第一项 type  第二项  id  第三项  内容
                                    if (roomId.equals(list.get(1))) {//与当前房间id相匹配
                                        switch (list.get(0)) {
                                            case "1"://浏览量
                                                seeNum.setText(String.format("%s 人", list.get(2)));
                                                break;
                                            case "2"://发券
                                                sendGift.setVisibility(View.VISIBLE);
                                                couponId = list.get(2);
                                                break;
                                            case "3"://关注
                                                aboveTextLayout.setVisibility(View.VISIBLE);
                                                aboveName.setText(String.format("%s", list.get(2)));
                                                aboveContent.setText(" 关注了主播");
                                                aboveTextLayout.setBackground(getResources().getDrawable(R.drawable.above_bg_zi));
                                                break;
                                            case "4"://点赞
                                                if (Integer.valueOf(list.get(2)) > 100000) {
                                                    mlikeNum.setText(String.format("%s万", div(list.get(2), "10000", 1)));
//                                                    heart.addHeart(1);
                                                    mLikeView.addFavor();
                                                    if (player != null) {
                                                        player.setLikeNum(String.format("%s万", div(list.get(2), "10000", 1)));
                                                    }
                                                } else {
                                                    mlikeNum.setText(list.get(2));
//                                                    heart.addHeart(1);
                                                    mLikeView.addFavor();
                                                    if (player != null) {
                                                        player.setLikeNum(list.get(2));
                                                    }

                                                }
                                                break;
                                            case "5"://隐藏红包
                                                if (list.get(2).equals(couponId)) {
                                                    sendGift.setVisibility(View.GONE);
                                                }
                                                break;
                                        }
                                    }
                                } else {//正常发送消息
                                    final TIMTextElem textElem = (TIMTextElem) elem;
                                    String text = textElem.getText();
                                    if (text.contains("正在购买")) {//购买商品
                                        if (!timUserProfile.getNickName().equals(myNickName)) {
                                            String nickName;
                                            if (timUserProfile.getNickName().length() > 1 && timUserProfile.getNickName().length() > 1 && timUserProfile.getNickName().startsWith("1") && timUserProfile.getNickName().length() == 11) {
                                                nickName = timUserProfile.getNickName().substring(0, 3) + "****" + timUserProfile.getNickName().substring(7);//
                                            } else {
                                                nickName = timUserProfile.getNickName();//
                                                if (nickName.length() > 8) {
                                                    nickName = nickName.substring(0, 8) + "...";
                                                }
                                            }
                                            aboveTextLayout.setVisibility(View.VISIBLE);
                                            aboveName.setText(String.format("%s", nickName));
                                            aboveContent.setText(" 正在购买");
                                            aboveTextLayout.setBackground(getResources().getDrawable(R.drawable.above_bg_huang));
                                        }
                                    } else if (text.contains("进来了")) {
                                        aboveTextLayout.setVisibility(View.VISIBLE);
                                        String nickName;
                                        if (timUserProfile.getNickName().length() > 1 && timUserProfile.getNickName().startsWith("1") && timUserProfile.getNickName().length() == 11) {
                                            nickName = timUserProfile.getNickName().substring(0, 3) + "****" + timUserProfile.getNickName().substring(7);//
                                        } else {
                                            nickName = timUserProfile.getNickName();//
                                            if (nickName.length() > 8) {
                                                nickName = nickName.substring(0, 8) + "...";
                                            }
                                        }
                                        aboveName.setText(String.format("%s", nickName));
                                        aboveContent.setText(" 进来了");
                                        aboveTextLayout.setBackground(getResources().getDrawable(R.drawable.above_bg_hong));
                                    } else {
                                        //处理消息
                                        TIMElem elem = msg.getElement(0);
                                        final TIMTextElem textElemHis = (TIMTextElem) elem;
                                        msg.getSenderProfile(new TIMValueCallBack<TIMUserProfile>() {
                                            @Override
                                            public void onError(int i, String s) {

                                            }

                                            @Override
                                            public void onSuccess(TIMUserProfile timUserProfile) {
                                                String nickName;
                                                if (timUserProfile.getNickName().length() > 1 && timUserProfile.getNickName().startsWith("1") && timUserProfile.getNickName().length() == 11) {
                                                    nickName = timUserProfile.getNickName().substring(0, 3) + "****" + timUserProfile.getNickName().substring(7);//
                                                } else {
                                                    nickName = timUserProfile.getNickName();//
                                                    if (nickName.length() > 8) {
                                                        nickName = nickName.substring(0, 8) + "...";
                                                    }
                                                }
                                                historyBeanList.add(new HistoryBean(textElemHis.getText(), nickName));
                                                adapter.notifyDataSetChanged();
                                                smoothMoveToPosition(talkList, talkList.getBottom());
                                            }
                                        });
//                                        msgList.add(msg);
                                    }
                                }
                            }
                        });
                    }
                } else if (type == TIMConversationType.System) {

                }


            }
        });
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    /**
     * 获取群组信息
     */
    private void getGroupInfo(String groupId) {
        List<String> groupList = new ArrayList<>();
        groupList.add(groupId);
        TIMGroupManager.getInstance().getGroupInfo(groupList, new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
            @Override
            public void onError(final int code, final String desc) {
//                Log.e("error", desc);
            }

            @Override
            public void onSuccess(final List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
                if (timGroupDetailInfoResults.size() > 0) {
                    for (TIMGroupDetailInfoResult info : timGroupDetailInfoResults) {
                        Log.d("", "groupId: " + info.getGroupId()           //群组 ID
                                + " group name: " + info.getGroupName()              //群组名称
                                + " group owner: " + info.getGroupOwner()            //群组创建者帐号
                                + " group create time: " + info.getCreateTime()      //群组创建时间
                                + " group last info time: " + info.getLastInfoTime() //群组信息最后修改时间
                                + " group last msg time: " + info.getLastMsgTime()  //最新群组消息时间
                                + " group member num: " + info.getMemberNum());      //群组成员数量
                        seeNum.setText(String.format("%s 人", info.getMemberNum() * 5 + 500));
                    }

                }
            }
        });
    }

    /**
     * 退出群组
     */
    private void quitGroup() {
        TIMGroupManager.getInstance().quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Log.e("ShowingRoom", "退出群组失败" + desc);
            }

            @Override
            public void onSuccess() {
                Log.e("ShowingRoom", "退出群组成功");
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.focusBtn:
                if (StateMessage.IS_LOGIN) {
                    if (detail != null) {
                        if (isFocus) {
                            goFocus(String.valueOf(detail.getAnchor().getId()), "1");
                        } else {
                            goFocus(String.valueOf(detail.getAnchor().getId()), "0");
                        }
                    }
                } else {
                    startActivity(new Intent(ShowingRoom.this, LoginActivity.class));
                }
                break;
            case R.id.closeRoom:
                finish();
                break;
            case R.id.shopBtn:
                showProductDialog();
                if (StateMessage.IS_LOGIN) {
                    sendMes(myNickName + "正在购买");
                }
                break;
            case R.id.sendBtn:
                break;
            case R.id.rlSend:
                showInputLayout();
                showSoft();
                break;
            case R.id.likeBtn:
                if (StateMessage.IS_LOGIN) {
//                    likeNum++;
//                    mlikeNum.setText(String.valueOf(likeNum));
                    if (roomId != null && groupId != null) {
                        goLike(roomId, groupId);
                    }
                } else {
//                    heart.addHeart(1);
                    likeNum++;
                    mlikeNum.setText(String.valueOf(likeNum));
                    if (player != null) {
                        player.setLikeNum(String.valueOf(likeNum));
                    }
                    if (roomId != null && groupId != null) {
                        goLike(roomId, groupId);
                    }
                }

                break;
            case R.id.shareBtn:
                popWindow.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.sendGift://领取礼品
                if (roomId != null && groupId != null && couponId != null
                        && !roomId.equals("") && !groupId.equals("") && !couponId.equals("")) {
                    if (StateMessage.IS_LOGIN) {
                        getPrize(roomId, groupId, couponId);
                    } else {
                        startActivity(new Intent(ShowingRoom.this, LoginActivity.class));
                    }
                } else {
                    ToastUtils.showToast(ShowingRoom.this, "礼品领取失败");
                    sendGift.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void setViewPagerAdapter() {
        pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewListData.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewListData.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewListData.get(position));


                return viewListData.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * ******************************* 弹出网络环境提示弹窗 *******************************
     */
    private void showHintDialog(final boolean isFirst) {
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.hint_dialog_layout, null);//这里必须是final的
        TextView cancle = (TextView) view.findViewById(R.id.off_commit);
        TextView commit = (TextView) view.findViewById(R.id.commit);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
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
                if (isFirst) {
                    if (player != null && liveUrl != null && !liveUrl.equals("")){
                        player.play(liveUrl);
                    }
                } else {
                    if (player != null){
                        player.onResume();
                    }
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /**
     * ******************************* 监测网络变化
     *********************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals("4G")) {
            showHintDialog(false);
        } else if (str.equals("wifi")) {
            if (player != null){
                player.onResume();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 设置聊天列表
     */
    private void setAdapter() {
        adapter = new CommonAdapter<HistoryBean>(ShowingRoom.this, R.layout.talk_item_layout, historyBeanList) {
            @Override
            protected void convert(final ViewHolder holder, final HistoryBean s, final int position) {
                final TextView userName = holder.getConvertView().findViewById(R.id.name);
                Random random = new Random();
                String rom = String.valueOf(random.nextInt());
                if (rom.endsWith("1")) {
                    userName.setTextColor(Color.parseColor("#3FAAFD"));
                } else if (rom.endsWith("3")) {
                    userName.setTextColor(Color.parseColor("#19FCF9"));
                } else if (rom.endsWith("6")) {
                    userName.setTextColor(Color.parseColor("#FC66FE"));
                } else if (rom.endsWith("7")) {
                    userName.setTextColor(Color.parseColor("#F28848"));
                } else if (rom.endsWith("8")) {
                    userName.setTextColor(Color.parseColor("#67CA49"));
                }


                if (s.getText().contains("进来了")) {
                    holder.getView(R.id.name).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.name).setVisibility(View.VISIBLE);
                    holder.setText(R.id.name, String.format("%s:", s.getName()));
                }
                holder.setText(R.id.content, String.format("%s", s.getText()));
                //处理文本消息
//                if (s.isSelf()) {
//                    holder.setText(R.id.content, String.format("%s", textElem.getText()));
//                } else {
//                    holder.setText(R.id.content, String.format("%s", textElem.getText()));
//                }

            }

        };
        talkList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 每隔5分钟刷新一下关注主播
     */
    CountDownTimer focusTimer = new CountDownTimer(1000000, 5 * 60 * 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (isActivityShow) {
                showFocusDialog();
            }
        }

        @Override
        public void onFinish() {

        }
    };

    /**
     * 商品弹窗
     */
    private void showProductDialog() {
        // 构造对话框
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        } else {
            builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        }
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.show_product_dialog, null);
        final TextView productNum = v.findViewById(R.id.productNum);//商品数量
        RelativeLayout rlClose = v.findViewById(R.id.rlClose);
        SmartRefreshLayout refresh_root = v.findViewById(R.id.refresh_root);
        final RecyclerView productList = v.findViewById(R.id.productList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        productList.setLayoutManager(manager);
        productAdapter = new CommonAdapter<LiveDetailBean.ProductListBean>(ShowingRoom.this, R.layout.product_dialog_item_layout, productData) {
            @Override
            protected void convert(ViewHolder holder, final LiveDetailBean.ProductListBean s, final int position) {
                Glide.with(ShowingRoom.this)
                        .load(s.getLogo()).asBitmap().into((ImageView) holder.getView(R.id.productImage));
                holder.setText(R.id.title, s.getName());
                if (s.getPrice() != null && isIntegerForDouble(Double.valueOf(s.getPrice()))) {
                    holder.setText(R.id.price, String.format("￥%s", s.getPrice().substring(0, s.getPrice().indexOf("."))));
                } else {
                    holder.setText(R.id.price, String.format("￥%s", s.getPrice()));
                }
                if (productData != null) {
                    productNum.setText(String.valueOf(productData.size()));
                }
                holder.setText(R.id.productSingleNum, String.valueOf(Math.abs(position - productData.size())));
                holder.getView(R.id.buy).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 检查是否已经授权
                        if (FloatWinPermissionCompat.getInstance().check(mContext)) {
                            String url = s.getUrl();
                            if (url == null || url.length() < 10) return;
                            Intent intent = new Intent(ShowingRoom.this, HomeWebViewAct.class);
                            intent.putExtra("url", url);
                            intent.putExtra("all", true);
                            intent.putExtra("isTitle", true);
                            startActivity(intent);
                            floatWindowType = FloatWindowManager.FW_TYPE_ALERT_WINDOW;
                            showFloatWindowDelay(player.getCurrentPosition(),liveUrl);
                        } else {
                            // 授权提示
                            new AlertDialog.Builder(mContext).setTitle("悬浮窗权限未开启")
                                    .setMessage("你的手机没有授权" + mContext.getString(R.string.app_name) + "获得悬浮窗权限，视频悬浮窗功能将无法正常使用")
                                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 显示授权界面
                                            try {
                                                FloatWinPermissionCompat.getInstance().apply(mContext);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null).show();
                        }
                    }
                });
                holder.getView(R.id.rlMain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (FloatWinPermissionCompat.getInstance().check(mContext)) {
                            String url = s.getUrl();
                            if (url == null || url.length() < 10) return;
                            Intent intent = new Intent(ShowingRoom.this, HomeWebViewAct.class);
                            intent.putExtra("url", url);
                            intent.putExtra("all", true);
                            intent.putExtra("isTitle", true);
                            startActivity(intent);
                            floatWindowType = FloatWindowManager.FW_TYPE_ALERT_WINDOW;
                            showFloatWindowDelay(player.getCurrentPosition(),liveUrl);
                        } else {
                            // 授权提示
                            new AlertDialog.Builder(mContext).setTitle("悬浮窗权限未开启")
                                    .setMessage("你的手机没有授权" + mContext.getString(R.string.app_name) + "获得悬浮窗权限，视频悬浮窗功能将无法正常使用")
                                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 显示授权界面
                                            try {
                                                FloatWinPermissionCompat.getInstance().apply(mContext);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null).show();
                        }
                    }
                });
            }
        };
        productList.setAdapter(productAdapter);
        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.BOTTOM);
        noticeDialog.getWindow().setWindowAnimations(R.style.anim_menu_bottombar);
        noticeDialog.show();
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (DisplayUtils.getScreenWidth(ShowingRoom.this) * 1.08);
        noticeDialog.getWindow().setAttributes(layoutParams);
    }

    /**
     * 判断double是否是整数
     *
     * @param obj
     * @return
     */
    public boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj - Math.floor(obj) < eps;
    }


    /**
     * 获取直播详情
     */
    private void getLiveDetail(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("showplat", "2");
        params.put("parentLocation", "122");
        params.put("id", id);
        HttpClient.post(HttpClient.ONLINE_LIVEDETAIL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    detail = JSONArray.parseObject(result.getData(), LiveDetailBean.class);
                    Glide.with(ShowingRoom.this).load(detail.getAnchor().getHeader()).asBitmap().into(headImage);
                    name.setText(detail.getAnchor().getName());
                    roomId = String.valueOf(detail.getId());
                    groupId = detail.getGroupId();
                    liveUrl = detail.getRtmpUrl();
                    myNickName = detail.getUserName();
                    if (liveUrl == null || liveUrl.equals("")) {//直播结束,暂未生成回放时候
                        ToastUtils.showToast(ShowingRoom.this, "直播已结束,回放正在生成中,请稍后再试");
                        finish();
                        return;
                    }
//                    if (getIntent().getIntExtra("status",0) == 1){//直播没有进度条
//                        player.live(true);
//                    }else {
//                        player.live(false);
//                    }
                    player.play(liveUrl);
                    player.onSendMessListener(new GiraffePlayer.OnSendMessListener() {
                        @Override
                        public void clickInput() {
                            showInputLayout();
                            showSoft();
                        }

                        @Override
                        public void clickProduct() {
                            showProductDialog();
                            if (StateMessage.IS_LOGIN) {
                                sendMes(myNickName + "正在购买");
                            }
                        }

                        @Override
                        public void clickLike() {
                            if (StateMessage.IS_LOGIN) {
                                if (roomId != null && groupId != null) {
                                    goLike(roomId, groupId);
                                }
                            } else {
//                                heart.addHeart(1);
                                mLikeView.addFavor();
                                likeNum++;
                                mlikeNum.setText(String.valueOf(likeNum));
                                if (player != null) {
                                    player.setLikeNum(String.valueOf(likeNum));
                                }
                                if (roomId != null && groupId != null) {
                                    goLike(roomId, groupId);
                                }
                            }
                        }

                        @Override
                        public void shareClick() {
                            popWindow.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
                        }

                        @Override
                        public void removeView(boolean show) {
                            if (show){
                                refreshRoot.setVisibility(View.INVISIBLE);
                                layoutView.setVisibility(View.INVISIBLE);
                                aboveTextLayout.setVisibility(View.INVISIBLE);
                                refreshRoot.setAnimation(AnimationUtils.makeOutAnimation(ShowingRoom.this,true));
                                layoutView.setAnimation(AnimationUtils.makeOutAnimation(ShowingRoom.this,true));
                                aboveTextLayout.setAnimation(AnimationUtils.makeOutAnimation(ShowingRoom.this,true));
                            }else {
                                refreshRoot.setVisibility(View.VISIBLE);
                                layoutView.setVisibility(View.VISIBLE);
                                aboveTextLayout.setVisibility(View.VISIBLE);
                                refreshRoot.setAnimation(AnimationUtils.makeInAnimation(ShowingRoom.this,false));
                                layoutView.setAnimation(AnimationUtils.makeInAnimation(ShowingRoom.this,false));
                                aboveTextLayout.setAnimation(AnimationUtils.makeInAnimation(ShowingRoom.this,false));
                            }
                        }
                    });

                    player.onScreenChangeListener(land -> {
                        if (land){
                            headLayout.setVisibility(View.VISIBLE);
                        }else {
                            headLayout.setVisibility(View.GONE);
                        }
                    });
                    if (detail.getCouponId() != null && !detail.getCouponId().equals("")) {//礼品
                        couponId = detail.getCouponId();
                        sendGift.setVisibility(View.VISIBLE);
                    } else {
                        couponId = "";
                        sendGift.setVisibility(View.GONE);
                    }
                    setIM();//设置IM
                    Glide.with(ShowingRoom.this).load(detail.getAnchor().getHeader()).asBitmap().into(headImage);
                    name.setText(detail.getAnchor().getName());
//                    seeNum.setText(String.format("%s 人", detail.getCounts()));
                    if (detail.getPv() != null && !detail.getPv().equals("")) {
                        if (Integer.valueOf(detail.getPv()) > 100000) {
                            seeNum.setText(String.format("%s 万人", div(detail.getPv(), "10000", 1)));
                        } else {
                            seeNum.setText(String.format("%s 人", detail.getPv()));
                        }

                    }
                    if (detail.getTips() != null && !detail.getTips().equals("")) {
                        likeNum = Integer.valueOf(detail.getTips());
                        if (Integer.valueOf(detail.getTips()) > 100000) {
                            mlikeNum.setText(String.format("%s万", div(detail.getTips(), "10000", 1)));
                            if (player != null) {
                                player.setLikeNum(String.format("%s万", div(detail.getTips(), "10000", 1)));
                            }
                        } else {
                            mlikeNum.setText(String.valueOf(likeNum));
                            if (player != null) {
                                player.setLikeNum(String.valueOf(likeNum));
                            }
                        }
                    }
                    if (detail.getFocus() == 1) {//已关注
                        focusBtn.setBackground(getResources().getDrawable(R.drawable.foucs_not_bg));
                        focusTxt.setText("已关注");
                        focusTxt.setTextColor(Color.parseColor("#E60012"));
                        focusTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                        isFocus = true;
                        focusBtn.setEnabled(false);
                        focusTimer.cancel();
                    } else {//未关注
                        focusBtn.setBackground(getResources().getDrawable(R.drawable.foucs_bg));
                        focusTxt.setText("关注");
                        focusTxt.setTextColor(Color.parseColor("#ffffff"));
                        focusTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.room_foucs), null, null, null);
                        isFocus = false;
                        focusBtn.setEnabled(true);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                focusTimer.start();
                            }
                        }, 5 * 60 * 1000);
                    }
                    productData.clear();
                    productData.addAll(detail.getProductList());
                    productNumMain.setText(String.valueOf(productData.size()));
                    popWindow.setShareMessage(detail.getTitle(), detail.getLogoPic(), detail.getAnchor().getName(), detail.getShareUrl());//设置分享信息
                    getHistory(groupId);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e("", e.getMessage());
            }
        }, this);
    }

    /**
     * 关注
     */
    private void goFocus(String id, final String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("showplat", "2");
        params.put("anchorId", id);
        params.put("type", type);
        params.put("groupId", groupId);
        HttpClient.post(HttpClient.FOCUS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    if (type.equals("0")) {
                        focusBtn.setBackground(getResources().getDrawable(R.drawable.foucs_not_bg));
                        focusTxt.setText("已关注");
                        focusTxt.setTextColor(Color.parseColor("#E60012"));
                        focusTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                        isFocus = true;
                        focusBtn.setEnabled(false);
//                        sendMes(myNickName+" 关注了主播");
                    } else {
                        focusBtn.setBackground(getResources().getDrawable(R.drawable.foucs_bg));
                        focusTxt.setText("关注");
                        focusTxt.setTextColor(Color.parseColor("#ffffff"));
                        focusTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.room_foucs), null, null, null);
                        isFocus = false;
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e("", e.getMessage());
            }
        }, this);
    }

    /**
     * 历史消息
     */
    private List<HistoryBean> historyBeanList = new ArrayList<>();

    private void getHistory(String groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        HttpClient.post(HttpClient.HISTORY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    historyBeanList = JSON.parseArray(result.getData(), HistoryBean.class);
                    setAdapter();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e("", e.getMessage());
            }
        }, this);
    }


    /**
     * 点赞
     */
    private void goLike(String id, String groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("groupId", groupId);
        HttpClient.post(HttpClient.LIKE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e("", e.getMessage());
            }
        }, this);
    }


    /**
     * 领取福袋
     */
    private void getPrize(String id, String groupId, String couponId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("groupId", groupId);
        params.put("couponId", couponId);
        params.put("showplat", "1");
        HttpClient.post(HttpClient.GET_PRIZE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    sendGift.setVisibility(View.GONE);
                    ToastUtils.showToast(ShowingRoom.this, result.getMessage());
                } else {
                    sendGift.setVisibility(View.GONE);
                    ToastUtils.showToast(ShowingRoom.this, result.getMessage());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                Log.e("", e.getMessage());
                sendGift.setVisibility(View.GONE);
            }
        }, this);
    }


    PopupWindow pop;
    EditText myInputEdit;

    private void showInputLayout() {
        View layout = getLayoutInflater().inflate(R.layout.input_layout, null);
        myInputEdit = layout.findViewById(R.id.inputEdit);
        myInputEdit.requestFocus();
        myInputEdit.setInputType(TYPE_TEXT_FLAG_MULTI_LINE);
        myInputEdit.setSingleLine(false);
        pop = new PopupWindow(layout, DisplayUtils.getScreenWidth(ShowingRoom.this), RelativeLayout.LayoutParams.WRAP_CONTENT);

        // 设置出现和消失样式
        pop.setAnimationStyle(R.style.anim_menu_bottombar);

        pop.update();
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setTouchable(true); // 设置popupwindow可点击
        pop.setOutsideTouchable(true); // 设置popupwindow外部可点击
        pop.setFocusable(true); // 获取焦点
        pop.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                KPSwitchConflictUtil.hidePanelAndKeyboard(panelRoot);
            }
        });

        myInputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    if (StateMessage.IS_LOGIN && GlobalApplication.user != null) {
                        if (TextUtils.isEmpty(myInputEdit.getText())) {
                            ToastUtils.showToast(ShowingRoom.this, "请输入消息");
                            InputMethodManager inputMethodManager = (InputMethodManager) myInputEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(myInputEdit, 0);
                        } else {
                            sendMes(myInputEdit.getText().toString());
                            KPSwitchConflictUtil.hidePanelAndKeyboard(panelRoot);
                            pop.dismiss();
                        }
                    } else {
                        startActivity(new Intent(ShowingRoom.this, LoginActivity.class));
                    }
                }
                return false;
            }
        });

        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pop.dismiss();
                    KPSwitchConflictUtil.hidePanelAndKeyboard(panelRoot);
                    return true;
                }
                return false;
            }

        });

    }

    private void showSoft() {
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) myInputEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(myInputEdit, 0);
            }
        }, 10);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 关注主播
     */
    private void showFocusDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.focus_dialog, null);
        RelativeLayout focusBtn = v.findViewById(R.id.focusBtn);
        RelativeLayout rlClose = v.findViewById(R.id.rlClose);

        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.CENTER);
        noticeDialog.show();
        focusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StateMessage.IS_LOGIN) {
                    goFocus(String.valueOf(detail.getAnchor().getId()), "0");
                    noticeDialog.dismiss();
                } else {
                    startActivity(new Intent(ShowingRoom.this, LoginActivity.class));
                }
            }
        });
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();

            }
        });
        noticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                noticeDialog.dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = (int) (DisplayUtil.getMobileWidth(this) * 0.4);
        layoutParams.height = (int) (DisplayUtil.getMobileWidth(this) * 0.4);
        noticeDialog.getWindow().setAttributes(layoutParams);
    }


    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        destroyWindow();
        super.onBackPressed();
    }

    @OnClick({R.id.focusBtn, R.id.closeRoom,R.id.sendGift})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.focusBtn:
                if (StateMessage.IS_LOGIN) {
                    if (detail != null) {
                        if (isFocus) {
                            goFocus(String.valueOf(detail.getAnchor().getId()), "1");
                        } else {
                            goFocus(String.valueOf(detail.getAnchor().getId()), "0");
                        }
                    }
                } else {
                    startActivity(new Intent(ShowingRoom.this, LoginActivity.class));
                }
                break;
            case R.id.closeRoom:
                finish();
                break;
            case R.id.sendGift:
                if (roomId != null && groupId != null && couponId != null
                        && !roomId.equals("") && !groupId.equals("") && !couponId.equals("")) {
                    if (StateMessage.IS_LOGIN) {
                        getPrize(roomId, groupId, couponId);
                    } else {
                        startActivity(new Intent(ShowingRoom.this, LoginActivity.class));
                    }
                } else {
                    ToastUtils.showToast(ShowingRoom.this, "礼品领取失败");
                    sendGift.setVisibility(View.GONE);
                }
                break;
        }
    }
}
