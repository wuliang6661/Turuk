package com.haoyigou.hyg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.HomeRenQiAdapter;
import com.haoyigou.hyg.adapter.WeekRecylerAdapter;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.BenyueBO;
import com.haoyigou.hyg.entity.HomeOneBO;
import com.haoyigou.hyg.entity.HomeThreeBO;
import com.haoyigou.hyg.entity.RenQiBO;
import com.haoyigou.hyg.entity.ZhuanTiBO;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/10/18.
 * <p>
 * 首页新品首发、本月活动、人气推荐、专题精选Fragment
 */

public class HomeHeaderTwoFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.list_benyue)
    ListViewForScrollView listBenyue;
    @BindView(R.id.list_renqi)
    ListViewForScrollView listRenqi;
    @BindView(R.id.see_more)
    TextView seeMore;
    @BindView(R.id.xinpin)
    TextView xinpin;
    @BindView(R.id.benyue)
    TextView benyue;
    @BindView(R.id.renqi)
    TextView renqi;
    @BindView(R.id.zhuanti)
    TextView zhuanti;
    @BindView(R.id.mian_layout)
    LinearLayout mianLayout;
    @BindView(R.id.list_zhuanti)
    ListViewForScrollView listZhuanti;

    @BindView(R.id.remai)
    TextView remai;
    @BindView(R.id.list_remai)
    ListViewForScrollView listremai;
    HomeThreeBO threeBO;
    private ListViewForScrollView listViewHeightBasedOnChildren;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fra_home_two, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initvion();
        loadHomeTwoData();
    }


    /**
     * 初始化界面
     */
    private void initvion() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycle.setLayoutManager(manager);

        listBenyue.setOnItemClickListener(this);
        seeMore.setOnClickListener(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HomeOneBO oneBO) {   //是否进入首页

        loadHomeTwoData();
    }


    /**
     * 获取首页数据
     */
    private void loadHomeTwoData() {
        mianLayout.setVisibility(View.GONE);
        Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.HOME_THREE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSONObject.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    if (mianLayout == null) return;
                    mianLayout.setVisibility(View.VISIBLE);
                    threeBO = JSONObject.parseObject(result.getData(), HomeThreeBO.class);
                    //热卖推荐
                    setUIData0();
                    //新品首发
                    setUIData();
                    //本月活动
                    setUIData1();
                    //人气推荐
                    setUIData2();
                    //专题精选
                    setUIData3();
                    EventBus.getDefault().post(new HomeThreeBO());
                } else {
                    mianLayout.setVisibility(View.GONE);
                }
            }
        }, getActivity());
    }



    /**
     * //专题精选
     */
    private void setUIData3() {
        if (threeBO.getCullings() == null || threeBO.getCullings().size() == 0) {
            zhuanti.setVisibility(View.GONE);
            listZhuanti.setVisibility(View.GONE);
        } else {
            setZhuantiAdapter();
        }
    }
    /**
     * //人气推荐
     */
    private void setUIData2() {
        if (threeBO.getRecommend() == null || threeBO.getRecommend().size() == 0) {
            renqi.setVisibility(View.GONE);
            listRenqi.setVisibility(View.GONE);
        } else {
            HomeRenQiAdapter adapter3 = new HomeRenQiAdapter(getActivity(), disId, threeBO.getRecommend());
            List<RenQiBO> s = threeBO.getRecommend();
            listRenqi.setAdapter(adapter3);

        }
    }
    private void setUIData0() {
        if (threeBO.getUpper_recommend() == null || threeBO.getUpper_recommend().size() == 0) {
            remai.setVisibility(View.GONE);
            listremai.setVisibility(View.GONE);
        } else {
            HomeRenQiAdapter adapter3 = new HomeRenQiAdapter(getActivity(), disId, threeBO.getUpper_recommend());
            listremai.setAdapter(adapter3);

        }
    }
    /**
     * //本月活动
     */
    private void setUIData1() {
        if (threeBO.getTabs() == null || threeBO.getTabs().size() == 0) {
            benyue.setVisibility(View.GONE);
            listBenyue.setVisibility(View.GONE);
        } else {
            setBenYueAdapter();
        }
    }


    /**
     * //新品首发
     */
    private void setUIData() {
        //新品首发
        if (threeBO.getNewpros() == null || threeBO.getNewpros().size() == 0) {
            recycle.setVisibility(View.GONE);
            xinpin.setVisibility(View.GONE);
        } else {
            WeekRecylerAdapter adapter1 = new WeekRecylerAdapter(getActivity(), threeBO.getNewpros());
            recycle.setAdapter(adapter1);
        }
    }


    /**
     * 设置本月活动数据
     */
    private void setBenYueAdapter() {
        CommonAdapter<BenyueBO> adapter3 = new CommonAdapter<BenyueBO>(getActivity(), R.layout.item_hot_banner, threeBO.getTabs()) {
            @Override
            protected void convert(ViewHolder helper, BenyueBO item, int position) {
                ImageView image = helper.getView(R.id.banner_img);
                ViewGroup.LayoutParams params = image.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(getActivity()) / 2.58);
                image.setLayoutParams(params);
                helper.setText(R.id.banner_name, item.getTitle());
                if(item.getPic()!=null){
                    Glide.with(getActivity()).load(item.getPic()).into(image);
                }
//                helper.setImageUrl(R.id.banner_img, item.getPic(), true);
                helper.setText(R.id.banner_message, item.getShortDesc());
                helper.setText(R.id.banner_price, "¥ " + String.format("%.2f", item.getMinPrice()) + " 起");

            }
        };

        listBenyue.setAdapter(adapter3);


    }


    /**
     * 设置专题推荐数据
     */
    private void setZhuantiAdapter() {
        CommonAdapter<ZhuanTiBO> adapter = new CommonAdapter<ZhuanTiBO>(getActivity(),
                R.layout.item_zhuanti, threeBO.getCullings()) {
            @Override
            protected void convert(ViewHolder viewHolder, final ZhuanTiBO item, int position) {
                ImageView imageView = viewHolder.getView(R.id.zhuanti_img);
                RecyclerView recycler = viewHolder.getView(R.id.recycle_zhuanti);
                LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
                manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = (int) (DisplayUtil.getMobileWidth(getActivity()) / 2.58);
                imageView.setLayoutParams(params);
                recycler.setLayoutManager(manager1);
                Picasso.with(getActivity()).load(item.getCull().getPic()).into(imageView);
                WeekRecylerAdapter adapter2 = new WeekRecylerAdapter(getActivity(), item.getCullpro());
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
        listZhuanti.setAdapter(adapter);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (StringUtils.isEmpty(threeBO.getTabs().get(position).getUrl())) {
            Bundle bundle = new Bundle();
            bundle.putString("productTabId", "" + threeBO.getTabs().get(position).getId());
            goToActivity(LabelActivity.class, bundle, false);
        } else {
            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
            intent.putExtra("url", threeBO.getTabs().get(position).getUrl());
            intent.putExtra("all", true);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_more:
                Bundle bundle = new Bundle();
                bundle.putString("productTabId", "-1");
                goToActivity(LabelActivity.class, bundle, false);
                break;
        }
    }


}
