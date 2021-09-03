package com.haoyigou.hyg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.GroupBean;
import com.haoyigou.hyg.entity.HomeOneBO;
import com.haoyigou.hyg.ui.JocellWebView;
import com.haoyigou.hyg.ui.RoundImageView3;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.userWebviewActivity;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.SharePopupWindow;
import com.haoyigou.hyg.view.widget.ShareWxPupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Witness on 2021/1/28
 * Describe: 拼团
 */
public class NewGroupFragment extends BaseFragment {

    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.titleTxt)
    TextView titleTxt;
    @BindView(R.id.groupBtn)
    ImageButton groupBtn;

    private CommonAdapter<GroupBean> adapter;
    private List<GroupBean> data = new ArrayList<>();

    private SharePopupWindow popWindow = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_group_layout, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getData();
        popWindow = new SharePopupWindow(getActivity());
        popWindow.setShareMessage("好易购商城", "", "这些好东西可以拼团了，多人拼团更超值！", "https://m.best1.com/groupbuy/productList/2/89622.html?visfrom=5");
    }

    private void initView() {
        // SpannableStringBuilder 用法
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
        spannableBuilder.append("超值拼团");
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {//好易购用户协议
            @Override
            public void onClick(View widget) {

            }
            @Override
            public void updateDrawState(TextPaint ds) {
//                设定的是span超链接的文本颜色，而不是点击后的颜色
                ds.setColor(Color.parseColor("#F6E724"));
                ds.setUnderlineText(false);    //去除超链接的下划线
                ds.clearShadowLayer();//清除阴影
            }

        };
        spannableBuilder.setSpan(clickableSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleTxt.setText(spannableBuilder);
        refreshLayout.setRefreshHeader(new SmartHeader(getActivity()));
        refreshLayout.setHeaderHeight(60);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
                refreshLayout.finishRefresh(1000);
            }
        });
        groupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.showAtLocation(getActivity().findViewById(R.id.mainLayout), Gravity.BOTTOM, 0, 0);
            }
        });
    }

    private void setAdapter(){
        if (adapter != null){
            adapter.notifyDataSetChanged();
            return;
        }
        adapter = new CommonAdapter<GroupBean>(getActivity(),R.layout.group_item_layout,data) {
            @Override
            protected void convert(ViewHolder holder, GroupBean item, int position) {
                RoundImageView3 pic = holder.getConvertView().findViewById(R.id.pic);
                ViewGroup.LayoutParams params = pic.getLayoutParams();
                params.height = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.35);
                params.width = (int)(DisplayUtils.getScreenWidth(getActivity()) * 0.35);
                pic.setLayoutParams(params);
                if (position == data.size()-1){
                    holder.getView(R.id.bgLine).setVisibility(View.GONE);
                }else {
                    holder.getView(R.id.bgLine).setVisibility(View.VISIBLE);
                }
                Glide.with(getActivity()).load(item.getImage()).into(pic);
                holder.setText(R.id.title,item.getTitle());
                holder.setText(R.id.groupType,String.format("%s人团",item.getNum()));
                holder.setText(R.id.groupSuccessTxt,String.format("已拼成%s团",item.getNowgroups()));
                holder.setText(R.id.price,item.getDisprice());
                holder.setText(R.id.singlePrice,String.format("单买价￥%s",item.getProduct().getPrice()));
                holder.getView(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", HttpClient.HTTP_DOMAIN+"/groupbuy/productGroupDetail?id="+item.getId());
                        intent.putExtra("all", true);
                        startActivity(intent);
                    }
                });

            }
        };
        listView.setAdapter(adapter);
    }

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("showplate",3);
        HttpClient.post(HttpClient.GROUP, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                data.clear();
                data.addAll(JSONObject.parseArray(content, GroupBean.class));
                setAdapter();
            }
        }, getActivity());
    }
}
