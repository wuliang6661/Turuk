package com.haoyigou.hyg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/29.
 */
public class TVLiVEFragment extends BaseFragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fra_lives_app, container, false);
        ButterKnife.bind(this, view);

        return view;

    }
}
