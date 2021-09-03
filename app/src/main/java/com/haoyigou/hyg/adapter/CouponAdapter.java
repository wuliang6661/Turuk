package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.DiscountEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuliang on 2017/2/24.
 * <p>
 * 优惠卷适配器
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<DiscountEntity> list;

    private Map<String, Boolean> isGetList;   //已领取的优惠卷列表

    public CouponAdapter(Context context, List<DiscountEntity> list) {
        this.context = context;
        this.list = list;
        isGetList = new HashMap<>();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DiscountEntity entity = list.get(position);
        holder.price.setText(entity.getDiscount());
        if ("3".equals(entity.getDistype())) {
            holder.couponMessage.setText("仅限单品使用");
        }
        if ("4".equals(entity.getDistype())) {
            holder.couponMessage.setText("满" + entity.getLimitprice() + "元可用");
        }

//        if (isGetList.size() != 0) {
        if (isGetList.get(entity.getDiscode()) != null && isGetList.get(entity.getDiscode())) {
            holder.couponGet.setVisibility(View.VISIBLE);
        } else {
            if ("1".equals(entity.getHaveGet())) {
                holder.couponGet.setVisibility(View.VISIBLE);
            } else {
                holder.couponGet.setVisibility(View.GONE);
            }
        }
//        }
        holder.itemLayout.setTag(entity);
        holder.itemLayout.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        final DiscountEntity entity = (DiscountEntity) view.getTag();
        Map<String, Object> params = new HashMap<>();
        params.put("discode", entity.getDiscode());
        HttpClient.post(HttpClient.GETDISCODE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    isGetList.put(entity.getDiscode(), true);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, context);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemLayout;
        TextView price;
        TextView couponMessage;
        ImageView couponGet;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.item_view);
            price = (TextView) itemView.findViewById(R.id.price);
            couponMessage = (TextView) itemView.findViewById(R.id.coupon_message);
            couponGet = (ImageView) itemView.findViewById(R.id.coupon_get);
        }
    }

}
