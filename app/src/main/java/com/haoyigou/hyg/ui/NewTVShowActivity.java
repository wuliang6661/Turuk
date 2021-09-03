package com.haoyigou.hyg.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.NewViewPagerAdapter;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.fragment.LiveAPPFragment;
import com.haoyigou.hyg.fragment.YesterdayLive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2019/3/13
 * Describe: 新改版TV直播界面
 */
public class NewTVShowActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private NewViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments=new ArrayList<>();
    private String[] titles = {"昨日直播", "今日直播"};
    private boolean isYesterday = false;//是否是在昨日直播页面

    private YesterdayLive yesterdayLive = new YesterdayLive();
    private LiveAPPFragment liveAPPFragment = new LiveAPPFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tv_layout);
        ButterKnife.bind(this);
        goBack();
        setTitleText("直播特卖");
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        tablayout.post(new Runnable() {
//            @Override
//            public void run() {
//                setIndicator(tablayout, 50, 50);
//            }
//        });
    }

    private void initView(){
        //设置TabLayout标签的显示方式
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setTabIndicatorFullWidth(false);
        //循环注入标签
        for (String tab:titles){
            tablayout.addTab(tablayout.newTab().setText(tab));
        }
        if (fragments != null && fragments.size()>0){
            fragments.clear();
        }
        fragments.add(yesterdayLive);
        fragments.add(liveAPPFragment);
        viewPagerAdapter=new NewViewPagerAdapter(getSupportFragmentManager(),titles,fragments,this);
        viewpager.setAdapter(viewPagerAdapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.getTabAt(1).select();
        liveAPPFragment.setVoiceNone(true);
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
}
