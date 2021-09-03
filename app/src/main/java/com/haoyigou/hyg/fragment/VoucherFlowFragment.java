package com.haoyigou.hyg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.VoucherAdapter;
import com.haoyigou.hyg.entity.VoucherEntry;
import com.haoyigou.hyg.view.superadapter.CommonAdapter;
import com.haoyigou.hyg.view.superadapter.ViewHolder;
import com.haoyigou.hyg.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/3/30.
 * <p>
 * 充值流量界面
 */

public class VoucherFlowFragment extends BaseFragment {


    @BindView(R.id.grid_view)
    GridView gridView;

    String[] flows = new String[]{"30M", "50M", "100M", "200M", "300M", "500M", "1000M"};
    List<String> flowList;
    String[] flows2 = new String[]{"30M", "50M", "100M", "200M", "300M", "500M", "1000M"};
    List<String> flowList2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.voucher_fragemnt, null);
        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flowList = new ArrayList<>();
        for (int i = 0; i < flows.length; i++) {
            flowList.add(flows[i]);
        }
        flowList2 = new ArrayList<>();
        for (int i = 0; i < flows2.length; i++) {
            flowList2.add(flows2[i]);
        }
        setDetailsData();
    }


    /**
     * 设置默认数据
     */
    private void setDetailsData() {
        CommonAdapter<String> adapter = new CommonAdapter<String>(getActivity(), R.layout.item_voucher_layout, flowList) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.change, item);
                viewHolder.setText(R.id.change_price, flowList2.get(position));
            }
        };
        gridView.setAdapter(adapter);
    }

    /**
     * 设置购买数据
     */
    public void setGridAdapter(List<VoucherEntry> list) {
        VoucherAdapter adapter = new VoucherAdapter(getActivity(), list, 2);
        adapter.setClick(new VoucherAdapter.onClick() {
            @Override
            public void onClickData(VoucherEntry entry) {
                if (callBack != null) {
                    callBack.onClick(entry);
                }
            }
        });
        gridView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private onCallBack callBack;

    public void setCallBack(onCallBack callBack) {
        this.callBack = callBack;
    }


    public interface onCallBack {
        void onClick(VoucherEntry entry);
    }
}
