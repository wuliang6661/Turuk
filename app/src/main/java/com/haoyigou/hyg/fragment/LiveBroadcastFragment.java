package com.haoyigou.hyg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.HomeOneBO;
import com.haoyigou.hyg.entity.LiveBroadcasteBO;
import com.haoyigou.hyg.ui.HorizontalLiveActivity;
import com.haoyigou.hyg.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/29.
 */

public class LiveBroadcastFragment extends BaseFragment {
    private View view;
    private RecyclerView re;
    private CommonAdapter<String> mAdapter;
    private CommonAdapter<LiveBroadcasteBO.LiveBo> mAdapter2;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<String> mData2 = new ArrayList();
    private List<LiveBroadcasteBO.LiveBo> liveData = new ArrayList();
    private List<LiveBroadcasteBO.PreviewBo> previewData = new ArrayList();
    private LiveBroadcasteBO oneBO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fra_live_broadcast, container, false);
        initview();
        initdata();
        initdata2();
        return view;

    }
    private void initview() {
        re = (RecyclerView)view.findViewById(R.id.re);
        re.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter =new CommonAdapter<String>(getActivity(), R.layout.live_two_item, mData2)
        {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }

        };
        re.setAdapter(mAdapter);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.live_broadcast_heard, null);
        RecyclerView ree = (RecyclerView)headView.findViewById(R.id.ree);
        ree.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter2 =new CommonAdapter<LiveBroadcasteBO.LiveBo>(getActivity(), R.layout.live_one_item, liveData)
        {
            @Override
            protected void convert(ViewHolder holder, LiveBroadcasteBO.LiveBo s, int position) {

            }

        };
        ree.setAdapter(mAdapter2);
        mAdapter2.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(position==0){
                    startActivity(new Intent(getActivity(),HorizontalLiveActivity.class));
                }
// else {
//                    startActivity(new Intent(getActivity(),ShuLiveActivity.class));
//                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mHeaderAndFooterWrapper.addHeaderView(headView);
        re.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }
    private void initdata2() {
        for (int i = 0; i < 5; i++) {

            mData2.add(i+"");
        }
    }

    private void initdata() {
        HttpClient.post(HttpClient.LIVEs_URL, new HashMap<String, Object>(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = com.alibaba.fastjson.JSONObject.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    oneBO = com.alibaba.fastjson.JSONObject.parseObject(result.getData(),LiveBroadcasteBO.class);
                    liveData.addAll(oneBO.getLive());
                    previewData.addAll(oneBO.getPreview());
                } else {
                    showToast(result.getMessage());
                }
            }
        }, getActivity());

    }
}
