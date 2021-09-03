package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.MonthNewsEntry;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by wuliang on 2016/11/23.
 * 每周上新适配器
 */

public class WeekRecylerAdapter extends RecyclerView.Adapter<WeekRecylerAdapter.MyViewHodler> {

    private Context context;
    private List<MonthNewsEntry> list;

    private boolean isAddMore = false;
    private int id;

    public WeekRecylerAdapter(Context context, List<MonthNewsEntry> list) {
        this.context = context;
        this.list = list;
    }

    public void setAddMore(boolean isAddMore, int id) {
        this.isAddMore = isAddMore;
        this.id = id;
    }


    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_week_list, null);
        return new MyViewHodler(view);
    }

    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        if (isAddMore && position >= list.size()) {
            holder.weekText.setText("");
            holder.price.setText("");
            holder.weekImg.setImageResource(R.drawable.see_more);
        } else {
            if (StringUtils.isEmpty(list.get(position).getPiclogo())) {
                holder.weekImg.setImageResource(R.drawable.default_image);
            } else {
                Glide.with(context).load(list.get(position).getPiclogo())
                        .error(R.drawable.default_image).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        into(holder.weekImg);
            }
            holder.weekText.setText(list.get(position).getName());
            holder.price.setText("¥ " + String.format("%.2f", list.get(position).getDisprice()));
        }
    }

    @Override
    public int getItemCount() {
        if (isAddMore) {
            return list.size() + 1;
        }
        return list.size();
    }

    class MyViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView weekImg;
        TextView weekText;
        TextView price;

        public MyViewHodler(View itemView) {
            super(itemView);
            weekImg = (ImageView) itemView.findViewById(R.id.week_img);
            weekText = (TextView) itemView.findViewById(R.id.week_text);
            price = (TextView) itemView.findViewById(R.id.week_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getPosition() >= list.size()) {
                Intent intent1 = new Intent(context, LabelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productTabId", id + "");
                intent1.putExtras(bundle);
                context.startActivity(intent1);
            } else {
                Intent intent = new Intent(context, HomeWebViewAct.class);
                intent.putExtra("url", list.get(getPosition()).getJumpAdress());
                intent.putExtra("all", true);
                context.startActivity(intent);
            }
        }
    }

}
