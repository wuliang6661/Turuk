package com.haoyigou.hyg.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.TVLiveEntry;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DateTimeUtils;
import com.haoyigou.hyg.utils.MyDialog;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2019/3/13
 * Describe: 昨日直播
 */
public class YesterdayLive extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.live_list)
    ListView liveList;

    private List<TVLiveEntry> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yesterday_live, container, false);
        ButterKnife.bind(this, view);
        liveList.setOnItemClickListener(this);
        getData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if ("0".equals(data.get(position).getProductid())) {
            showLoginDialog();
            return;
        }
        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
        String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        String url = HttpClient.GOODWEBVIEW + "?distributorId=" + disId + "&source=1&productid=" + data.get(position).getProductid()+
                "&parentLocation="+MApplication.tvParentLocation;
        intent.putExtra("url", url);
        startActivity(intent);
    }

    /**
     * 设置直播列表适配器
     */
    private void setLiveAdapter() {
        CommonAdapter<TVLiveEntry> adapter = new CommonAdapter<TVLiveEntry>(getActivity(), R.layout.new_live_item, data) {
            @Override
            protected void convert(ViewHolder viewHolder, final TVLiveEntry item, int position) {
                TextView oldPrice = viewHolder.getConvertView().findViewById(R.id.listOldPrice);
                oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                ImageView ivPic = viewHolder.getView(R.id.ivPic);
                Date date = new Date();
                if (date.getTime() > item.getEndtime()) {  //已结束
                    viewHolder.setText(R.id.time,String.format("%s-%s",DateTimeUtils.formatTime(item.getBegintime(), DateTimeUtils.DF_HH_MM)
                    ,DateTimeUtils.formatTime(item.getBegintime(), DateTimeUtils.DF_HH_MM)));
                    Glide.with(getActivity()).load(item.getLogopic()).into(ivPic);
                    viewHolder.setText(R.id.title,String.format("%s",item.getProductname()));
                    if ("0".equals(item.getProductid())) {
                        viewHolder.setText(R.id.txtPrice,String.format("%s","【电话订购】"));
                        oldPrice.setVisibility(View.GONE);
                    }else {
                        oldPrice.setVisibility(View.VISIBLE);
                        viewHolder.setText(R.id.txtPrice,String.format("¥%s",item.getDisprice()));
                        oldPrice.setText(String.format("¥%s",item.getOldprice()));
                        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                    }
//                    viewHolder.setText(R.id.txtPrice,String.format("%s",item.getDisprice()));
//                    viewHolder.setText(R.id.listOldPrice,String.format("%s",item.getOldprice()));
                }
            }
        };
        liveList.setAdapter(adapter);
    }

    /**
     * 获取直播内容
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "1");
        HttpClient.post(HttpClient.LIVE_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                        String array = object.getString("result");
                        data = JSONArray.parseArray(array, TVLiveEntry.class);
                        setLiveAdapter();
                    } else {
                        showToast(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity());
    }

    private void showLoginDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//        tvTitle.setText("提示");
        TextPaint tp = tvTitle.getPaint();
        tp.setFakeBoldText(true);
        final TextView btnYes = (TextView) view.findViewById(R.id.f_quchecbutton_btn_queding);
        final TextView btlNo = (TextView) view.findViewById(R.id.f_quchecbutton_btn_quxiao);
        final MyDialog builder = new MyDialog(getActivity(),0,0,view,R.style.DialogTheme);

        builder.setCancelable(false);
        builder.show();
        //设置对话框显示的View
        //点击确定是的监听
        //拨打电话
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                call("4001188188");
                builder.cancel();
            }
        });


        btlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });

    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
