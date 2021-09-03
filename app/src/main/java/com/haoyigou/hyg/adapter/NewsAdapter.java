package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.haoyigou.hyg.entity.ClassifyDataBO;
import com.haoyigou.hyg.ui.RoundImageView;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgy on 2017/2/26.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {
    private Context context;
    private List<ClassifyDataBO> classifyDataBO = new ArrayList<>();
    public NewsAdapter(Context context){
        this.context = context;
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_classify, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.tvTitle.setText(classifyDataBO.get(position).getName());
        if (classifyDataBO.get(position).getDiscontent().equals("")){
            holder.tvCount.setText(classifyDataBO.get(position).getContent());
            Drawable drawableLeft = context.getResources().getDrawable(
                    R.mipmap.hui);
            holder.tvCount.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null, null, null);
            holder.rlCount.setVisibility(View.GONE);
            holder.custom_pic.setVisibility(View.VISIBLE);
            Glide.with(context).load(classifyDataBO.get(position).getPic5())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.custom_pic);
        }else {
            holder.tvCount.setText(classifyDataBO.get(position).getDiscontent());
            Drawable drawableLeft = context.getResources().getDrawable(
                    R.mipmap.count);
            holder.tvCount.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null, null, null);
            holder.rlCount.setVisibility(View.VISIBLE);
            holder.tv1.setText(String.valueOf(classifyDataBO.get(position).getUnitCount()));
            holder.tv2.setText(String.valueOf(classifyDataBO.get(position).getDecimalCount()));
            holder.custom_pic.setVisibility(View.GONE);
        }
        holder.tvOldPrice.setText(String.format("¥%s",classifyDataBO.get(position).getPrice()));
        holder.tvnewPrice.setText(String.format("¥%s",classifyDataBO.get(position).getDisprice()));
        Glide.with(context).load(classifyDataBO.get(position).getPiclogo()).error(R.drawable.default_image)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.ivPic);
        holder.rlAddShopCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeWebViewAct.class);
                intent.putExtra("url", classifyDataBO.get(position).getUrl());
                intent.putExtra("all", true);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.ivPic)
        RoundImageView ivPic;
        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.tvOldPrice)
        TextView tvOldPrice;
        @BindView(R.id.tvnewPrice)
        TextView tvnewPrice;
        @BindView(R.id.rlCount)
        RelativeLayout rlCount;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.rlAddShopCar)
        RelativeLayout rlAddShopCar;
        @BindView(R.id.store_layout)
        RelativeLayout store_layout;
        @BindView(R.id.custom_pic)
        ImageView custom_pic;
        public VH(View viewItem){
            super(viewItem);
            ButterKnife.bind(this, itemView);
        }
    }
}
