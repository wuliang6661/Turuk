package com.haoyigou.hyg.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.NewViewPagerAdapter;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.view.viewpager.ViewPagerSlide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2019-12-10
 * Describe: 新版直播特卖界面
 */
public class NewShowFragment extends BaseFragment {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_image01)
    ImageView rightImage01;
    @BindView(R.id.right_image)
    ImageView rightImage;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.rlTitleBg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPagerSlide viewpager;
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;

    private NewViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {"优选直播", "TV直播"};
//    private ShowOnlineFragment showOnline = new ShowOnlineFragment();
    private NewTVShowFragment liveAPPFragment = new NewTVShowFragment();

    private boolean isVisible = false;//是否可见
    private boolean isTVLive = false;//是否是TV直播页面

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_show_layout, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible && isTVLive){
            liveAPPFragment.setVoice(true);
        }else {
            liveAPPFragment.setVoice(false);
        }
        if (MApplication.tvFragment == 1){
            layoutBack.setVisibility(View.VISIBLE);
        }else {
            layoutBack.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isVisible = !hidden;
        if (MApplication.tvFragment == 1){
            layoutBack.setVisibility(View.VISIBLE);
        }else {
            layoutBack.setVisibility(View.INVISIBLE);
        }
        if (hidden){
            liveAPPFragment.setVoice(false);
        }else {
            if (isTVLive) {
                isTVLive = true;
                liveAPPFragment.setVoice(true);
            }else {
                isTVLive = false;
                liveAPPFragment.setVoice(false);
            }
        }
    }

    protected void initView() {
        titleText.setText("直播特卖");
        back.setVisibility(View.GONE);
        //设置TabLayout标签的显示方式
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //循环注入标签
        for (String tab : titles) {
            tablayout.addTab(tablayout.newTab().setText(tab));
        }
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
        }
//        fragments.add(showOnline);
        fragments.add(liveAPPFragment);
        viewPagerAdapter = new NewViewPagerAdapter(getChildFragmentManager(), titles, fragments, getActivity());
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setScanScroll(false);
        tablayout.setupWithViewPager(viewpager);
        if (MApplication.isTvShow){
            tablayout.getTabAt(1).select();
            MApplication.isTvShow = false;
        }else {
            tablayout.getTabAt(0).select();
        }
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    isTVLive = false;
                    liveAPPFragment.setVoice(false);
                }else if (tab.getPosition() == 1){
                    isTVLive = true;
                    liveAPPFragment.setVoice(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post("CloseTvFragment");//发送给MainActivity,关闭当前Fragment
            }
        });
    }

    public void setBack(){
        if (MApplication.tvFragment == 1){
            layoutBack.setVisibility(View.VISIBLE);
        }else {
            layoutBack.setVisibility(View.INVISIBLE);
        }
    }

}
