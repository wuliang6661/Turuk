package com.haoyigou.hyg.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.NewViewPagerAdapter;
import com.haoyigou.hyg.base.BaseFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2019/3/27
 * Describe:  新版直播界面
 */
public class NewTVShowFragment extends BaseFragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.rlTitleBg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.llTop)
    LinearLayout llTop;

    private NewViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {"昨日直播", "今日直播"};
    private boolean isYesterday = false;//是否是在昨日直播页面
    private YesterdayLive yesterdayLive = new YesterdayLive();
    private LiveAPPFragment liveAPPFragment = new LiveAPPFragment();

    private boolean isVisible = false;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_tv_layout, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
//        setFlgsbg(R.color.white);
//        setStatusBar();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (isYesterday && !isVisible){
//            liveAPPFragment.setVoiceNone(true);
//        }else {
//            liveAPPFragment.setVoiceNone(false);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        tablayout.post(new Runnable() {
            @Override
            public void run() {
//                setIndicator(tablayout, 20, 20);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        llTop.setVisibility(View.VISIBLE);
        title_text.setTextColor(Color.parseColor("#ffffff"));
        liveAPPFragment.setVoiceNone(true);
        title_text.setText("TV直播");
        back.setVisibility(View.GONE);
        //设置TabLayout标签的显示方式
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        //循环注入标签
        for (String tab : titles) {
            tablayout.addTab(tablayout.newTab().setText(tab));
        }
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }
        fragments.add(yesterdayLive);
        fragments.add(liveAPPFragment);

        viewPagerAdapter = new NewViewPagerAdapter(getChildFragmentManager(), titles, fragments, getActivity());
        viewpager.setAdapter(viewPagerAdapter);
//        viewpager.setPageTransformer(true, new DepthPageTransformer());
        tablayout.setupWithViewPager(viewpager);
        tablayout.getTabAt(1).select();
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("昨日直播")) {
                    isYesterday = true;
                    liveAPPFragment.setVoiceNone(false);
                } else {
                    isYesterday = false;
                    liveAPPFragment.setVoiceNone(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置tablayout下划线长度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisible = hidden;
        if (hidden) {
            liveAPPFragment.setVoiceNone(false);
        } else {
            if (!isYesterday) {
                liveAPPFragment.setVoiceNone(true);
            }else {
                liveAPPFragment.setVoiceNone(false);
            }
        }
    }

    public void setVoice(boolean open){
        if (open){
            if (!isYesterday){
                liveAPPFragment.setVoiceNone(true);
            }else {
                liveAPPFragment.setVoiceNone(false);
            }
        }else {
            liveAPPFragment.setVoiceNone(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
