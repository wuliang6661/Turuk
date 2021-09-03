package com.haoyigou.hyg.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/29.
 */
public class FollowActivity extends BaseActivity{
    private RecyclerView reee;
    private CommonAdapter<String> mAdapter;
    private List<String> mData = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setIsSunce(false);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_follow);
        setTitleText("");
        goBack();
        initview();
        initdate();
    }
    private void initdate() {
        for (int i = 0; i < 2; i++) {

            mData.add(i+"");
        }
    }

    private void initview() {
        reee = (RecyclerView)findViewById(R.id.reee);
        reee.setLayoutManager(new LinearLayoutManager(this));
        mAdapter =new CommonAdapter<String>(this, R.layout.follow_ac_item, mData)
        {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {


            }

        };
        reee.setAdapter(mAdapter);
    }
}
