package com.haoyigou.hyg.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.ui.SettingActivity;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.ShowRedPicEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.UserBean;
import com.haoyigou.hyg.utils.FuncUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.view.circlephoto.RoundImageView;
import com.haoyigou.hyg.view.dialog.CreateMemberDiaolg;
import com.haoyigou.hyg.view.widget.MyGridView;
import com.haoyigou.hyg.ui.LoginActivity;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * 个人中心页面
 */
public class PersonalFragment extends Fragment {

    private MyGridView gridView;
    private View root;
    String income;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;
    private Button head_Setting;//设置按钮
    //图片的文字标题
    private TextView tvhead_name;//个人中心上面的名字
    private RoundImageView roundImageView;
    private ArrayList<Integer> image_icon;
    private RelativeLayout paymentorder;//代付款
    private RelativeLayout generationshiping;//代发货
    private RelativeLayout collectionorder;//待收货
    private RelativeLayout refound;//退款、售后；
    private TextView head_user_money;//收益
    private TextView tv_paymentorder;   //提示的小点
    private TextView tv_generationshiping;
    private TextView tv_collectionorder;
    private TextView tv_refound;
    private RoundImageView iv_headavater;
    CreateMemberDiaolg diaolg;
    private LinearLayout linear_search_order;
    PictureAdapter adapter;
    String name;

    List<String> title;           //显示gridview中的选项
    List<Integer> image;         //需要显示图片的ID

    private boolean isYaoqingVisiable = false;
    private LinearLayout headlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fra_personal, container, false);
        ShareSDK.initSDK(getActivity());
        EventBus.getDefault().register(this);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview();
        initevent();
        setSetingListener();
        isLoginShow(false);   //默认先显示未入驻瑞朵
        adapter = new PictureAdapter(getActivity());
        gridView.setAdapter(adapter);
        if (StateMessage.IS_LOGIN) {  //判断是否登陆
            head_Setting.setText("设置");
        } else {
            head_Setting.setText("登录");
        }
        loadData();
    }

    /***
     * 是否登陆时显示
     */
    private void isLoginShow(boolean isEnter) {
//        title = new ArrayList<>();
//        image = new ArrayList<>();
//        title.add("收藏夹");
//        title.add("优惠券");
//        image.add(R.drawable.favorite);
//        image.add(R.drawable.value_voucher);
//        if (isEnter) {   //入驻瑞朵,则显示
//            title.add("我的团队");
//            title.add("我的奖励");
//            title.add("我要提现");
//            image.add(R.drawable.my_team);
//            image.add(R.drawable.my_gains);
//            image.add(R.drawable.withdraws_cash);
//        }
//        title.add("新手指南");
//        title.add("好易购学院");
//        title.add("联系客服");
//        image.add(R.drawable.newbie_guide);
//        image.add(R.drawable.ruiduo_classroom);
//        image.add(R.drawable.contact_us);
//        if (!StateMessage.IS_LOGIN) {
//            title.add("会员绑定");
//            image.add(R.drawable.member_binding);
//        }
    }


    /***
     * 设置按钮在是否登陆状态下的点击事件
     */
    private void setSetingListener() {
        head_Setting.setOnClickListener(listener);
        headlayout.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            if (StateMessage.IS_LOGIN) {
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("addFinish", true);
                startActivity(intent);
                getActivity().finish();
            }
        }
    };


    //个人中心代发货 代付款 红色标记
    private void showred() {
        ShowRedPicEntity param = new ShowRedPicEntity();
        HttpClient.showRed(param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String body) {
//                Log.e("getRed", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    if (object.getInteger("1") == 1) {
                        tv_paymentorder.setVisibility(View.VISIBLE);
                    } else {
                        tv_paymentorder.setVisibility(View.GONE);
                    }
                    if (object.getInteger("2") == 1) {
                        tv_generationshiping.setVisibility(View.VISIBLE);
                    } else {
                        tv_generationshiping.setVisibility(View.GONE);
                    }
                    if (object.getInteger("3") == 1) {
                        tv_collectionorder.setVisibility(View.VISIBLE);
                    } else {
                        tv_collectionorder.setVisibility(View.GONE);
                    }
                    if (object.getInteger("4") == 1) {
                        tv_refound.setVisibility(View.VISIBLE);
                    } else {
                        tv_refound.setVisibility(View.GONE);
                    }
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

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
                String num1 = object.getString("1");
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    image_icon.add(Integer.parseInt(object.getString("1")));
                    image_icon.add(Integer.parseInt(object.getString("2")));
                    image_icon.add(Integer.parseInt(object.getString("3")));
                    image_icon.add(Integer.parseInt(object.getString("4")));
                    image_icon.add(Integer.parseInt(object.getString("5")));
                    image_icon.add(Integer.parseInt(object.getString("6")));
                    int a = Integer.parseInt(SharedPreferencesUtils.getInstance().getString("distributorId", null));
                    if (a > 1) {
                        image_icon.add(Integer.parseInt(object.getString("7")));
                        image_icon.add(Integer.parseInt(object.getString("8")));
                        image_icon.add(Integer.parseInt(object.getString("9")));
                    }
                    adapter.notifyDataSetChanged();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, getActivity());
    }


    /**
     * 绑定girdview数据 点击跳转
     */

    private void loadData() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int a = Integer.parseInt(SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
                Intent intent = new Intent(getActivity(), PersonWebViewAct.class);
                String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
                if (isYaoqingVisiable && position == title.size() - 1) {
                    intent.putExtra("url", HttpClient.YAOQINGMA + "?distributorId=" + disid);
                    startActivity(intent);
                    return;
                }
                switch (position) {
                    case 0:
                        intent.putExtra("url", HttpClient.FAVORITE + "?distributorId=" + disid);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("url", HttpClient.VALUEVOUCHER + "?distributorId=" + disid);
                        startActivity(intent);
                        break;
                    case 2:
                        if (a > 1) {
                            intent.putExtra("url", HttpClient.MYTEAM + "?distributorId=" + disid);
                            startActivity(intent);
                            break;
                        }
                        intent.putExtra("url", HttpClient.HELPERINDEX + "?distributorId=" + disid);
                        startActivity(intent);
                        break;
                    case 3:
                        if (a > 1) {
                            intent.putExtra("url", HttpClient.MYGAINS + "?distributorId=" + disid);
                            startActivity(intent);
                            break;
                        }
                        intent.putExtra("url", HttpClient.RUODUOCLASS + "?distributorId=" + disid);
                        startActivity(intent);
                        break;
                    case 4:
                        if (a > 1) {
                            intent.putExtra("url", HttpClient.MYREFLECT + "?distributorId=" + disid);
                            startActivity(intent);
                            break;
                        }
                        intent.putExtra("url", HttpClient.CONTACTUS + "?distributorId=" + disid);
                        startActivity(intent);
                        break;
                    case 5:
                        if (!StateMessage.IS_LOGIN) {
                            Toast.makeText(getActivity(), "需要登录后才能继续操作!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        //根据合伙人ID 切换girdview布局显示 合伙人id大于1
                        if (a > 1) {
                            intent.putExtra("url", HttpClient.HELPERINDEX + "?distributorId=" + disid);
                            startActivity(intent);
                            break;
                        }
                        if (diaolg != null) {
                            diaolg.showDialog();
                            break;
                        }
                        diaolg = new CreateMemberDiaolg(getActivity());
                        diaolg.setOnBinding(new CreateMemberDiaolg.onBindingListener() {
                            @Override
                            public void isBinding(boolean isBinding) {
                                handler.sendEmptyMessage(1);
                            }
                        });
                        diaolg.showDialog();
                        break;
                    case 6:
                        intent.putExtra("url", HttpClient.RUODUOCLASS + "?distributorId=" + disid);
                        startActivity(intent);
                        break;
                    case 7:
                        intent.putExtra("url", HttpClient.CONTACTUS + "?distributorId=" + disid);
                        startActivity(intent);
                        break;
                    case 8:
                        if (diaolg != null) {
                            diaolg.showDialog();
                            break;
                        }
                        diaolg = new CreateMemberDiaolg(getActivity());
                        diaolg.setOnBinding(new CreateMemberDiaolg.onBindingListener() {
                            @Override
                            public void isBinding(boolean isBinding) {
                                handler.sendEmptyMessage(1);
                            }
                        });
                        diaolg.showDialog();
                        break;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        if (str.equals("person_refresh")) {
            showred();
            showimage();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化数据
     */

    private void initview() {
        GlobalApplication application = (GlobalApplication) getActivity().getApplicationContext();
        linear_search_order = (LinearLayout) root.findViewById(R.id.linear_search_order);
        gridView = (MyGridView) root.findViewById(R.id.girdview);
        iv_headavater = (RoundImageView) root.findViewById(R.id.head_avatar);
        head_user_money = (TextView) root.findViewById(R.id.head_user_money);
        roundImageView = (RoundImageView) root.findViewById(R.id.head_avatar);
        tvhead_name = (TextView) root.findViewById(R.id.head_user_name);
        head_Setting = (Button) root.findViewById(R.id.head_setting);
        tvhead_name.setText(application.getF());
        if (tvhead_name != null && tvhead_name.length() > 1) {
            head_user_money.setText(FuncUtils.getDouble(application.getD()));
        }
        paymentorder = (RelativeLayout) root.findViewById(R.id.dai_fukuan);
        generationshiping = (RelativeLayout) root.findViewById(R.id.dai_fahuo);
        collectionorder = (RelativeLayout) root.findViewById(R.id.dai_shouhuo);
        refound = (RelativeLayout) root.findViewById(R.id.tuikuan_shouhou);
        tv_paymentorder = (TextView) root.findViewById(R.id.fukuan_point);
        tv_generationshiping = (TextView) root.findViewById(R.id.fahuo_point);
        tv_collectionorder = (TextView) root.findViewById(R.id.shouhuo_point);
        tv_refound = (TextView) root.findViewById(R.id.tuikuan_point);
        headlayout = (LinearLayout) root.findViewById(R.id.head_layout);
        Picasso.with(getActivity()).load(application.getC()).into(roundImageView);
    }

    /**
     * 点击事件
     */

    private void initevent() {
        //查看全部订单
        linear_search_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonWebViewAct.class);
                String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
                intent.putExtra("url", HttpClient.ALLORDERLINK + "?distributorId=" + disid);
                startActivity(intent);
            }
        });
        //代付款
        paymentorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonWebViewAct.class);
                String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
                intent.putExtra("url", HttpClient.PAYMENTORDERLINK + "?ordertype=0&distributorId=" + disid);
                startActivity(intent);
            }
        });
        //代发货
        generationshiping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonWebViewAct.class);
                String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
                intent.putExtra("url", HttpClient.GENEARTIONSHIPPINGORDERLINK + "?ordertype=1&distributorId=" + disid);
                startActivity(intent);
            }
        });
        //待收货
        collectionorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonWebViewAct.class);
                String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
                intent.putExtra("url", HttpClient.COLLECTIONORDERLINK + "?ordertype=2&distributorId=" + disid);
                startActivity(intent);
            }
        });
        //退款售后
        refound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonWebViewAct.class);
                String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
                intent.putExtra("url", HttpClient.REFOUNDORDERLINK + "?ordertype=3&distributorId=" + disid);
                startActivity(intent);
            }
        });
    }

    /***
     * Application中邀请码数据是否改变
     */

    @Override
    public void onResume() {
        super.onResume();
        loaduserinfo();
        showred();
        showimage();
        if (isYaoqingVisiable && !GlobalApplication.user.getInvitation().equals("1")) {
            title.remove(title.size() - 1);
            image.remove(image.size() - 1);
            isYaoqingVisiable = false;
            adapter.notifyDataSetChanged();
        }
    }

    //自定义适配器
    class PictureAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public PictureAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return title.size();
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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_girdview, null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                viewHolder.ruiduo_image = (ImageView) convertView.findViewById(R.id.ruiduo_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //判断小圆点显示
            if (image_icon != null && image_icon.size() != 0 && image_icon.size() >= getCount()) {
                if (image_icon.get(position) == 1) {
                    viewHolder.ruiduo_image.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.ruiduo_image.setVisibility(View.GONE);
                }
            }
            viewHolder.title.setText(title.get(position));
            viewHolder.image.setBackgroundResource(image.get(position));
            return convertView;
        }

        /**
         * 内部缓存
         */
        class ViewHolder {
            public TextView title;
            public ImageView image;
            public ImageView ruiduo_image;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:    //会员绑定成功之后 ， 会员绑定删除
                    title.remove(title.size() - 1);
                    image.remove(title.size() - 1);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:     //入驻瑞朵，会员未绑定  //则显示9个
                    if (title.size() == 9) {
                        return;
                    }
                    isLoginShow(true);
                    title.add("会员绑定");
                    image.add(R.drawable.member_binding);
                    adapter.notifyDataSetChanged();
                    break;
                case 3:   //入驻瑞朵，会员已绑定  //则显示8个
                    if (title.size() == 8) {
                        return;
                    }
                    isLoginShow(true);
                    adapter.notifyDataSetChanged();
                    break;
                case 4:   //未入驻瑞朵 ， 会员未绑定
                    if (title.size() == 6) {
                        return;
                    }
                    title.add("会员绑定");
                    image.add(R.drawable.member_binding);
                    adapter.notifyDataSetChanged();
                    break;
            }
            if (GlobalApplication.user != null) {
                if (GlobalApplication.user.getInvitation().equals("1")) {
                    title.add("邀请码");
                    image.add(R.drawable.yaoqing_img);
                    isYaoqingVisiable = true;
                }
            }
        }
    };

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
                    String pic = object.getString("headerpic");
                    income = object.getString("income");
                    name = object.getString("nickname");
                    int bind = object.getInteger("bind");
                    int distributorId = object.getInteger("distributorId");
                    if (distributorId == 1) {   //未入驻瑞朵
                        if (bind == 0) {   //未绑定，显示会员绑定
                            handler.sendEmptyMessage(4);
                        }
                    } else if (distributorId > 1) {   //已入驻瑞朵
                        if (bind == 0) {   //未绑定，显示会员绑定
                            handler.sendEmptyMessage(2);
                        } else {     //  已绑定 ，显示8个
                            handler.sendEmptyMessage(3);
                        }
                    }
                    if (name != null) {
                        tvhead_name.setText(name);
                    }
                    if (pic != null) {
                        if (pic.substring(0, 1).equals("/")) {
                            Picasso.with(getActivity()).load(HttpClient.HTTP_DOMAIN + pic).into(roundImageView);
                        } else
                            Picasso.with(getActivity()).load(pic).into(roundImageView);
                    }
                    if (income != null) {
                        head_user_money.setText(FuncUtils.getDouble(income));
                    }
                } else if ("0".equals(object.getString("status"))) {

                }
            }

            public void onFailure(Request request, IOException e) {
            }
        }, getActivity());
    }
}
