package com.haoyigou.hyg.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.BaseFragmentAdapter;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.MessageEvent;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.ui.LoginActivity;
import com.haoyigou.hyg.ui.MainActivity;
import com.haoyigou.hyg.ui.MessageBoxActivty;
import com.haoyigou.hyg.ui.NewTVActivity;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.view.viewpager.ViewPagerSlide;
import com.haoyigou.hyg.view.zxing.ActivityCapture;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import meijia.com.srdlibrary.liushibuju.BaseAdapter;
import meijia.com.srdlibrary.liushibuju.TagLayout;

/**
 * Created by witness on 2018/9/7.
 * 最新改版首页
 */

public class NewHomeFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.saoma)
    ImageView saoma;
    @BindView(R.id.select_edit)
    LinearLayout selectEdit;
    @BindView(R.id.xiaoxi)
    ImageView xiaoxi;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPagerSlide mViewPager;
    @BindView(R.id.search_text)
    TextView searchText;
    @BindView(R.id.rlDrop)
    RelativeLayout rlDrop;
    @BindView(R.id.header_layout)
    LinearLayout header_layout;
    @BindView(R.id.llTag)
    LinearLayout llTag;
    @BindView(R.id.messageNum)
    TextView messageNum;
    @BindView(R.id.tvShowList)
    ImageView tvShowList;
    @BindView(R.id.emptyImg)
    ImageView emptyImg;

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    TagLayout tag_layout_houseType;
    TextView textview;
    RelativeLayout rlClose;

    List<Fragment> mFragments;
    private HomeFragment homeFragment;
    private NewTVFragment newTVFragment;//电视直播界面
    private EverydayFragment everydayFragment;//每日一品
    private List<Map<String, String>> mainTabList = new ArrayList<>();
    private List<Map<String, String>> classifyTabList = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<Integer> titlesId = new ArrayList<>();
    private BaseFragmentAdapter adapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_home_fragment, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
        getLable();//首页导航栏数据
//        method1();
        EventBus.getDefault().register(this);
    }

    public void method1() {
        try {
            Thread.sleep(400L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (StateMessage.haveMes.equals("0")) {   //无未读消息
            xiaoxi.setImageResource(R.mipmap.white_message);
            messageNum.setVisibility(View.GONE);
        } else {
            messageNum.setVisibility(View.VISIBLE);
            if (Integer.valueOf(StateMessage.haveMes) > 99) {
                messageNum.setText("99+");
            } else {
                messageNum.setText(StateMessage.haveMes);
            }
        }
        if (GlobalApplication.user != null) {
            String search = GlobalApplication.user.getSearchdesc();
            if (search != null) {
                searchText.setText(search);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initListener() {
        saoma.setOnClickListener(this);
        xiaoxi.setOnClickListener(this);
        selectEdit.setOnClickListener(this);
        rlDrop.setOnClickListener(this);
        tvShowList.setOnClickListener(this);
    }

    private void setupViewPager() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        if (newTVFragment == null) {
            newTVFragment = new NewTVFragment();
        }
//        if (everydayFragment == null){
//            everydayFragment = new EverydayFragment();
//        }
        mFragments = new ArrayList<>();
        mFragments.clear();
        mFragments.add(homeFragment);
        mFragments.add(newTVFragment);
//        mFragments.add(everydayFragment);
        if (mainTabList.size() > 0) {
            for (int i = 0; i < mainTabList.size(); i++) {
                mFragments.add(ClassifyOneFragment.newInstance(mainTabList.get(i).get("id"), "0", titles.get(i + 1)));//长图界面
            }
        }
        if (classifyTabList.size() > 0) {
            for (int i = 0; i < classifyTabList.size(); i++) {
                mFragments.add(ClassifyOneFragment.newInstance("0", classifyTabList.get(i).get("id"), ""));//短图界面
            }
        }
        // 第二步：为ViewPager设置适配器
        adapter = new BaseFragmentAdapter(getChildFragmentManager(), mFragments, titles);
//
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(0);//设置预加载数量
        //  第三步：将ViewPager与TableLayout 绑定在一起
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabIndicatorFullWidth(false);
//        setTabWidth(mTabLayout, 20);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_layout,null);
                textView.setTextSize(15);
                textView.setText(tab.getText());
                tab.setCustomView(textView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    AlertView malertView;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saoma:
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        malertView = new AlertView("注意", "需要获取访问您设备相机权限进行扫码操作", null,
                                new String[]{"确定", "取消"}, null, getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onItemClick(Object o, int position) {
                                if (position == 0) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                                }
                                malertView.dismiss();
                            }
                        });
                        malertView.show();
                        return;
                    } else {

                        Intent intent1 = new Intent(getActivity(), ActivityCapture.class);
                        startActivity(intent1);
                    }
                } else {

                    Intent intent1 = new Intent(getActivity(), ActivityCapture.class);
                    startActivity(intent1);
                }


                break;
            case R.id.xiaoxi:
                if (StateMessage.IS_LOGIN) {
                    xiaoxi.setImageResource(R.mipmap.white_message);
                    StateMessage.haveMes = "0";
                    Intent intent2 = new Intent(getActivity(), MessageBoxActivty.class);
                    startActivity(intent2);
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("addFinish", true);
                    startActivity(intent);
                    getActivity().finish();
                    return;
                }
            case R.id.select_edit:
                goToActivity(SelectorActivity.class, false);
                break;
            case R.id.tvShowList://TV节目单
                MApplication.tvParentLocation = "105";
                startActivity(new Intent(getActivity(), NewTVActivity.class));
//                EventBus.getDefault().post("ShowTvFragment");
                break;
            case R.id.rlDrop:
//                MyDialog.show(getActivity());
                createCartDialog(header_layout);
                break;
            default:
                break;
        }
    }

    private void getLable() {
        Map<String, Object> params = new HashMap<>();

        HttpClient.post(HttpClient.HOME_LABLE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSON.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    emptyImg.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(result.getData());
                        JSONArray tab = jsonObject.getJSONArray("tab");
                        if (tab != null && tab.length() > 0) {
                            for (int i = 0; i < tab.length(); i++) {
                                JSONObject object = tab.getJSONObject(i);
                                Map<String, String> map = new HashMap<>();
                                Iterator iterator = object.keys();
                                while (iterator.hasNext()) {
                                    String json_key = iterator.next().toString();
                                    String json_value = object.get(json_key).toString();
                                    if (json_value == null) {
                                        json_value = "";
                                    }
                                    map.put(json_key, json_value);
                                }
                                mainTabList.add(map);
                            }
                        }
                        JSONArray first = jsonObject.getJSONArray("first");
                        if (first != null && first.length() > 0) {
                            for (int i = 0; i < first.length(); i++) {
                                JSONObject object = first.getJSONObject(i);
                                Map<String, String> map = new HashMap<>();
                                Iterator iterator = object.keys();
                                while (iterator.hasNext()) {
                                    String json_key = iterator.next().toString();
                                    String json_value = object.get(json_key).toString();
                                    if (json_value == null) {
                                        json_value = "";
                                    }
                                    map.put(json_key, json_value);
                                }
                                classifyTabList.add(map);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    titles.add("推荐");
                    titles.add("TV商品");
//                    titles.add("每日一品");
                    if (mainTabList.size() > 0) {
                        for (int i = 0; i < mainTabList.size(); i++) {
                            titles.add(mainTabList.get(i).get("name"));
                            titlesId.add(Integer.valueOf(mainTabList.get(i).get("id")));
                        }
                    }
                    if (classifyTabList.size() > 0) {
                        for (int i = 0; i < classifyTabList.size(); i++) {
                            titles.add(classifyTabList.get(i).get("name"));
                            titlesId.add(Integer.valueOf(classifyTabList.get(i).get("id")));
                        }
                    }
//                    Log.e("title",titles.toString());
                    setupViewPager();
                } else {
                    showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                super.onFailure(request, e);
                emptyImg.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                Glide.with(getActivity()).load("https://best1-static.oss-cn-hangzhou.aliyuncs.com/images/xtwh.jpg").into(emptyImg);
            }
        }, getActivity());
    }

    PopupWindow cartPopupWindow;

    @SuppressLint("WrongConstant")
    public void createCartDialog(View v) {
        View dialogCart = View.inflate(getActivity(), R.layout.my_dialog, null);

        tag_layout_houseType = (TagLayout) dialogCart.findViewById(R.id.tag_layout_rent);
        rlClose = (RelativeLayout) dialogCart.findViewById(R.id.rlClose);
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartPopupWindow.dismiss();
            }
        });
        setAdapter(tag_layout_houseType, titles);
        tag_layout_houseType.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                tag_layout_houseType.setItemSelecte(postion);
                for (int i = 0; i < titles.size(); i++) {
                    if (titles.get(postion).equals(titles.get(i))) {
                        if (mTabLayout.getTabAt(i) != null) {
                            mTabLayout.getTabAt(i).select();
                            MApplication.classifyParentLocation = "101";
                        }
                    }
                }
                cartPopupWindow.dismiss();
//                Toast.makeText(getActivity(),titles.get(postion),Toast.LENGTH_SHORT).show();
            }
        });
        //获取PopupWindow中View的宽高
        dialogCart.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        cartPopupWindow = new PopupWindow(dialogCart, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        cartPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
//                setBackgroundAlpha(1.0f);
            }
        });

        // 设置弹出窗体可点击
        cartPopupWindow.setFocusable(true);
        cartPopupWindow.setBackgroundDrawable(new BitmapDrawable());//加上该属性点击外部会关闭popupWindow
        // 点击其他地方消失
        cartPopupWindow.setOutsideTouchable(false);
        //防止PopupWindow被软件盘挡住
        cartPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        cartPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        cartPopupWindow.showAsDropDown(v);
//        setBackgroundAlpha(0.5f);

    }

    private void setAdapter(TagLayout tagLayout, final List<String> dataSource) {

        tagLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return dataSource.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                textview = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_tag, parent, false);
                ViewGroup.LayoutParams params2 = textview.getLayoutParams();
                params2.height = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.06);
                params2.width = (int) (DisplayUtil.getMobileWidth(getActivity()) * 0.19);
                textview.setLayoutParams(params2);
                textview.setText(dataSource.get(position));
                return textview;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageType().equals("tabid")) {
            for (int i = 0; i < titlesId.size(); i++) {
                if (messageEvent.getPassValue().equals(String.valueOf(titlesId.get(i)))) {
                    if (mTabLayout.getTabAt(i + 2) != null) {
                        mTabLayout.getTabAt(i + 2).select();
                    }
                }
            }
        } else if (messageEvent.getMessageType().equals("mes")) {
            if (StateMessage.haveMes.equals("0")) {   //无未读消息
                xiaoxi.setImageResource(R.mipmap.white_message);
                messageNum.setVisibility(View.GONE);
            } else {
                messageNum.setVisibility(View.VISIBLE);
                if (Integer.valueOf(StateMessage.haveMes) > 99) {
                    messageNum.setText("99+");
                } else {
                    messageNum.setText(StateMessage.haveMes);
                }
            }
        }
    }

    /**
     * 设置下划线长度
     *
     * @param tabLayout
     * @param padding
     */
    public void setTabWidth(final TabLayout tabLayout, final int padding) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);


                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距 注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = padding;
                        params.rightMargin = padding;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
