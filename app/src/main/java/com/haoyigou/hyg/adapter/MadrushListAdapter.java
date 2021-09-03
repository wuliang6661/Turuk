package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.entity.BerserkEntry;
import com.haoyigou.hyg.entity.BerserkListEntry;
import com.haoyigou.hyg.entity.CouponBO;
import com.haoyigou.hyg.entity.MadrushListBean;
import com.haoyigou.hyg.ui.RoundImageView;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.yalantis.ucrop.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by witness on 2016/11/22.
 * <p>
 * <p>
 * 首页限时购适配器
 */

public class MadrushListAdapter extends RecyclerView.Adapter<MadrushListAdapter.MyViewHolder> {


    private List<MadrushListBean.GrabDataBean.ProductBean> list;
    private Context context;
    private List<MadrushListBean.GrabDataBean.ProductBean> listT ;
    public MadrushListAdapter(Context context, List<MadrushListBean.GrabDataBean.ProductBean> list) {
        this.context = context;
        this.list = list;
//        listT = list.get(0).getProduct();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.madrush_item_layout,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvnewPrice.setText(String.format("¥%s",list.get(position).getDisprice()));
        holder.tvOldPrice.setText(String.format("¥%s",list.get(position).getPrice()));
        ViewGroup.LayoutParams params = holder.ivPic.getLayoutParams();
        params.height = (int)(ScreenUtils.getScreenWidth(context) * 0.19);
        params.width = (int)(ScreenUtils.getScreenWidth(context) * 0.19);
        holder.ivPic.setLayoutParams(params);
        Glide.with(context)
                .load(list.get(position).getPiclogo())
                .into(holder.ivPic);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RoundImageView ivPic;
        TextView tvnewPrice;
        TextView tvOldPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivPic = (RoundImageView) itemView.findViewById(R.id.ivPic);
            tvnewPrice = (TextView) itemView.findViewById(R.id.tvnewPrice);
            tvOldPrice =(TextView) itemView.findViewById(R.id.tvOldPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, HomeWebViewAct.class);
            intent.putExtra("all", true);
            if (list.get(getPosition()).getJumpAdress().contains("?")) {
                intent.putExtra("url", list.get(getPosition()).getJumpAdress()
                        +"&parentLocation="+MApplication.madrushParentLocation);
            }else {
                intent.putExtra("url", list.get(getPosition()).getJumpAdress()
                        +"?parentLocation="+MApplication.madrushParentLocation);
            }
            context.startActivity(intent);
        }
    }

}
