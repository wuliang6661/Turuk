package com.haoyigou.hyg.ui;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.NewViewPagerAdapter;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.fragment.LiveAPPFragment;
import com.haoyigou.hyg.fragment.YesterdayLive;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2020-04-23
 * Describe:
 */
public class NewTVActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tv_layout);
        ButterKnife.bind(this);
        initView();
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
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        viewPagerAdapter = new NewViewPagerAdapter(getSupportFragmentManager(), titles, fragments, this);
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
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//通知栏所需颜色
        }
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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

}
