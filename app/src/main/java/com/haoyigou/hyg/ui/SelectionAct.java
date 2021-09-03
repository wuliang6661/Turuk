package com.haoyigou.hyg.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.WeekRecylerAdapter;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.PopularityBo;
import com.haoyigou.hyg.entity.SelectionBo;
import com.haoyigou.hyg.entity.ZhuanTiBO;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/7/3.
 */
public class SelectionAct extends BaseActivity {


    private List<ZhuanTiBO> threeBO;

    @BindView(R.id.selection_list)
    ListView selectionlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_act);
        ButterKnife.bind(this);
        setTitleText("专题精选");
        goBack();
        getData();
    }

    /**
     * 获取充值记录数据
     */
    private void getData() {
        Map<String, Object> params = new HashMap<>();
        if (!MApplication.labelParentLocation.equals("")) {
            params.put("parentLocation", MApplication.labelParentLocation);
        }
        HttpClient.post(HttpClient.JUNIOR_CULLING, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                SelectionBo result = JSONObject.parseObject(content, SelectionBo.class);
                if (result.getStatus()==1) {
                    threeBO = result.getData();
                    //人气推荐
                    setUIData3();
                }
            }
        }, this);
    }
    private void setUIData3() {
        if (threeBO == null || threeBO.size() == 0) {
            selectionlist.setVisibility(View.GONE);
        } else {
            setZhuantiAdapter();
        }
    }
    /**
     * 设置专题推荐数据
     */
    private void setZhuantiAdapter() {
        CommonAdapter<ZhuanTiBO> adapter = new CommonAdapter<ZhuanTiBO>(SelectionAct.this,
                R.layout.item_zhuanti, threeBO) {
            @Override
            protected void convert(ViewHolder viewHolder, final ZhuanTiBO item, int position) {
                ImageView imageView = viewHolder.getView(R.id.zhuanti_img);
                RecyclerView recycler = viewHolder.getView(R.id.recycle_zhuanti);
                LinearLayoutManager manager1 = new LinearLayoutManager(SelectionAct.this);
                manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(SelectionAct.this) / 2.58);
                imageView.setLayoutParams(params);
                recycler.setLayoutManager(manager1);
                Picasso.with(SelectionAct.this).load(item.getCull().getPic()).into(imageView);
                WeekRecylerAdapter adapter2 = new WeekRecylerAdapter(SelectionAct.this, item.getCullpro());
                adapter2.setAddMore(true, item.getCull().getId());
                recycler.setAdapter(adapter2);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("productTabId", item.getCull().getId() + "");
                        goToActivity(LabelActivity.class, bundle1, false);
                    }
                });
            }
        };
//        listZhuanti.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        return true;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
        selectionlist.setAdapter(adapter);

    }
}