package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.ClassifyEntry;
import com.haoyigou.hyg.entity.CouponBO;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.ui.TVLiveActivity;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by witness on 2016/11/22.
 * <p>
 * <p>
 * 优惠券适配器
 */

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.MyViewHolder> {


    private List<CouponBO> list;
    private Context context;

    public CouponListAdapter(Context context, List<CouponBO> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coupon_item_layout,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvPrice.setText(list.get(position).getDiscount());
        holder.tvCondition.setText(list.get(position).getLimitprice());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvPrice;
        TextView tvCondition;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvCondition =(TextView) itemView.findViewById(R.id.tvCondition);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PersonWebViewAct.class);
            intent.putExtra("url", list.get(getPosition()).getUrl());
            intent.putExtra("completeUrl","yes");
            context.startActivity(intent);
        }
    }

}
