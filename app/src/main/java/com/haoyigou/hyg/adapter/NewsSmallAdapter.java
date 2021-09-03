package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.ClassifyDataBO;
import com.haoyigou.hyg.ui.RoundImageView;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgy on 2017/2/26.
 */

public class NewsSmallAdapter extends RecyclerView.Adapter<NewsSmallAdapter.VH> {
    private Context context;
    private List<ClassifyDataBO> classifyDataBO = new ArrayList<>();
    public NewsSmallAdapter(Context context){
        this.context = context;
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_home_hot_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (StringUtils.isEmpty(classifyDataBO.get(position).getPiclogo())) {
            holder.shop_img.setImageResource(R.drawable.default_image);
        } else {
            Glide.with(context).load(classifyDataBO.get(position).getPiclogo()).into(holder.shop_img);
        }
        holder.shop_title.setText(classifyDataBO.get(position).getName());
        holder.price.setText(String.format("¥%s",classifyDataBO.get(position).getDisprice()));
        holder.shop_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeWebViewAct.class);
                intent.putExtra("url", classifyDataBO.get(position).getUrl());
                intent.putExtra("all", true);
                context.startActivity(intent);
            }
        });
        if (classifyDataBO.get(position).getStore() == 0) {
            holder.store_layout.setVisibility(View.VISIBLE);
        } else {
            holder.store_layout.setVisibility(View.GONE);
        }

        if (classifyDataBO.get(position).getPic5() != null) {
            if (!classifyDataBO.get(position).getPic5().equals("")) {
                holder.custom_pic.setVisibility(View.VISIBLE);
                Glide.with(context).load(classifyDataBO.get(position).getPic5()).into(holder.custom_pic);
            } else {
                holder.custom_pic.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return classifyDataBO == null ? 0 : classifyDataBO.size();
    }
    /** 更新数据，替换原有数据 */
    public void updateItems(List<ClassifyDataBO> items) {
//        classifyDataBO.clear();
//        classifyDataBO.addAll(items);
        classifyDataBO = items;
        notifyDataSetChanged();
    }

    /** 在列表尾添加一串数据 */
    public void addItems(List<ClassifyDataBO> items) {
        int start = classifyDataBO.size();
        classifyDataBO.addAll(items);
        // notifyItemRangeChanged(start, items.size());会闪屏
        notifyDataSetChanged();
    }

    public class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.shop_img)
        RoundImageView shop_img;
        @BindView(R.id.add_pic)
        RoundImageView add_pic;
        @BindView(R.id.store_layout)
        RelativeLayout store_layout;
        @BindView(R.id.shop_title)
        TextView shop_title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.custom_pic)
        RoundImageView custom_pic;

        public VH(View viewItem){
            super(viewItem);
            ButterKnife.bind(this, itemView);
        }
    }
}
