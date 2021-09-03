package com.haoyigou.hyg.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseFragment;


/**
 * Created by Administrator on 2018/5/29.
 */
public class LivesFragment extends BaseFragment implements View.OnClickListener{
    private View view;

    private LiveBroadcastFragment liveFragment;
    private TVLiVEFragment tvLiVEFragment;
    private FollowFragment followFragment;
    private TextView txZb;
    private TextView teTv;
    private TextView teGz;
    private View vZb;
    private View vTv;
    private View vGz;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fra_lives_app, container, false);
        txZb = (TextView)view.findViewById(R.id.tx_zb);
        teTv = (TextView)view.findViewById(R.id.te_tv);
        teGz = (TextView)view.findViewById(R.id.te_gz);
        vZb= (View)view.findViewById(R.id.v_zb);
        vTv= (View)view.findViewById(R.id.v_tv);
        vGz= (View)view.findViewById(R.id.v_gz);
        initview();
        return view;

    }

    private void initview() {
        txZb.setOnClickListener(this);
        teTv.setOnClickListener(this);
        teGz.setOnClickListener(this);
        txZb.setTextColor(Color.parseColor("#E60012"));
        teTv.setTextColor(Color.parseColor("#333333"));
        teGz.setTextColor(Color.parseColor("#333333"));
        vZb.setVisibility(View.VISIBLE);
        vTv.setVisibility(View.GONE);
        vGz.setVisibility(View.GONE);
        liveFragment = new LiveBroadcastFragment();
        tvLiVEFragment = new TVLiVEFragment();
        followFragment = new FollowFragment();
        goToFragment(liveFragment);
    }
    private Fragment mContent = null;
    public void goToFragment(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getChildFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                if (mContent != null)
                    transaction.hide(mContent).add(R.id.fragment_container, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
                else
                    transaction.add(R.id.fragment_container, to).commitAllowingStateLoss();
            } else {
                if (mContent != null)
                    transaction.hide(mContent).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
                else
                    transaction.show(to).commitAllowingStateLoss();
            }
            mContent = to;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_zb:
                Log.d("asdfsdafasd", "onClick1: ");
                goToFragment(liveFragment);
                txZb.setTextColor(Color.parseColor("#E60012"));
                teTv.setTextColor(Color.parseColor("#333333"));
                teGz.setTextColor(Color.parseColor("#333333"));
                vZb.setVisibility(View.VISIBLE);
                vTv.setVisibility(View.GONE);
                vGz.setVisibility(View.GONE);
                break;
            case R.id.te_tv:
                Log.d("asdfsdafasd", "onClick2: ");
                goToFragment(tvLiVEFragment);
                txZb.setTextColor(Color.parseColor("#333333"));
                teTv.setTextColor(Color.parseColor("#E60012"));
                teGz.setTextColor(Color.parseColor("#333333"));
                vZb.setVisibility(View.GONE);
                vTv.setVisibility(View.VISIBLE);
                vGz.setVisibility(View.GONE);
                break;
            case R.id.te_gz:
                Log.d("asdfsdafasd", "onClick3: ");
                goToFragment(followFragment);
                txZb.setTextColor(Color.parseColor("#333333"));
                teTv.setTextColor(Color.parseColor("#333333"));
                teGz.setTextColor(Color.parseColor("#E60012"));
                vZb.setVisibility(View.GONE);
                vTv.setVisibility(View.GONE);
                vGz.setVisibility(View.VISIBLE);
                break;
        }
    }
}
