package com.haoyigou.hyg.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.ui.FollowActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/29.
 */
public class FollowFragment extends BaseFragment {
    private View view;
    private RecyclerView res;
    private CommonAdapter<String> mAdapter;
    private List<String> mData = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fra_follow_app, container, false);
        ButterKnife.bind(this, view);
        initview();
        initdata();
        return view;

    }
    private void initview() {
        res = (RecyclerView)view.findViewById(R.id.res);
        res.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter =new CommonAdapter<String>(getActivity(), R.layout.fra_follow_item, mData)
        {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setOnClickListener(R.id.follow_header, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),FollowActivity.class);
                        startActivity(intent);
                    }
                });
                if(position==0){
                    holder.setBackgroundRes(R.id.follow_status,R.drawable.follow_status);
                    holder.setText(R.id.follow_status, "已关注");
                    holder.setTextColor(R.id.follow_status, Color.parseColor("#333333"));
                }else if(position==1){
                    holder.setBackgroundRes(R.id.follow_status,R.drawable.follow_statu);
                    holder.setText(R.id.follow_status, "关注");
                    holder.setTextColor(R.id.follow_status, Color.parseColor("#e60012"));
                }
            }
        };
        res.setAdapter(mAdapter);

    }
    private void initdata() {
        for (int i = 0; i < 10; i++) {

            mData.add(i+"");
        }
    }
}
