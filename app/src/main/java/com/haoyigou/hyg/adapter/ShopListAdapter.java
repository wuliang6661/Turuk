package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.ShopEntry;
import com.haoyigou.hyg.utils.DisplayUtil;

import java.util.List;

/**
 * Created by wuliang on 2016/12/6.
 * <p>
 * 商品列表适配器，防止图片错位
 */

public class ShopListAdapter extends BaseAdapter {

    private Context context;
    private List<ShopEntry> shopList;

    public ShopListAdapter(Context context, List<ShopEntry> list) {
        this.context = context;
        this.shopList = list;
    }

    @Override
    public int getCount() {
        return shopList.size();
    }

    @Override
    public Object getItem(int i) {
        return shopList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_shop_layout, null);
            holder = new ViewHolder();
            holder.shopImg = (ImageView) itemView.findViewById(R.id.shop_img);
            holder.shopLayout = (RelativeLayout) itemView.findViewById(R.id.shop_img_layout);
            ViewGroup.LayoutParams params = holder.shopLayout.getLayoutParams();
            params.height = (int) (DisplayUtil.getMobileWidth(context) / 1.83);
            holder.shopLayout.setLayoutParams(params);
            holder.shopName = (TextView) itemView.findViewById(R.id.shop_name);
            holder.image = (ImageView) itemView.findViewById(R.id.add_pic);
            holder.oldPrice = (TextView) itemView.findViewById(R.id.old_price);
            holder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.price = (TextView) itemView.findViewById(R.id.price);
            holder.youhuijuan = (ImageView) itemView.findViewById(R.id.youhui);
            holder.customPic = (ImageView) itemView.findViewById(R.id.custom_pic);
            holder.storeLayout = (RelativeLayout) itemView.findViewById(R.id.store_layout);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }
        Glide.with(context).load(shopList.get(position).getPiclogo2()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.shopImg);
        holder.shopName.setText(shopList.get(position).getName());
        holder.image.setVisibility(View.VISIBLE);
        if (shopList.get(position).getTabType() != null) {
            switch (shopList.get(position).getTabType()) {
                case "5":
                    Glide.with(context).load(shopList.get(position).getPic4())
                            .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.customPic);
                    holder.customPic.setVisibility(View.VISIBLE);
                    holder.image.setVisibility(View.GONE);
                    break;
                case "6":   //显示限时折扣图片
//                    holder.image.setImageResource(R.drawable.icon_sale);
                    Glide.with(context).load(HttpClient.SALESHOP)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.image);
                    holder.customPic.setVisibility(View.GONE);
                    holder.image.setVisibility(View.VISIBLE);
                    break;
                case "7":   //显示爆款
//                    holder.image.setImageResource(R.drawable.icon_boom);
                    Glide.with(context).load(HttpClient.BOOMSHOP)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.image);
                    holder.customPic.setVisibility(View.GONE);
                    holder.image.setVisibility(View.VISIBLE);
                    break;
                case "8":     //显示新品
//                    holder.image.setImageResource(R.drawable.icon_new_dis);
                    Glide.with(context).load(HttpClient.NEWSHOP)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.image);
                    holder.customPic.setVisibility(View.GONE);
                    holder.image.setVisibility(View.VISIBLE);
                    break;
                case "9":     //显示热卖
//                    holder.image.setImageResource(R.drawable.icon_hot);
                    Glide.with(context).load(HttpClient.HOT)
                            .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.image);
                    holder.customPic.setVisibility(View.GONE);
                    holder.image.setVisibility(View.VISIBLE);
                    break;
                default:
                    holder.image.setVisibility(View.GONE);
                    holder.customPic.setVisibility(View.GONE);
                    break;
            }
        }
        if (shopList.get(position).getHasCoupons()) {   //优惠券是否显示
            holder.youhuijuan.setVisibility(View.VISIBLE);
        } else {
            holder.youhuijuan.setVisibility(View.GONE);
        }
        if (shopList.get(position).getPrice().equals(shopList.get(position).getDisprice())) {
            if (shopList.get(position).getOldprice().equals(shopList.get(position).getDisprice())) {   //只显示折扣价
                holder.oldPrice.setVisibility(View.INVISIBLE);   //不显示但占位置
                holder.price.setText("¥ " + shopList.get(position).getDisprice());
            } else {
                holder.oldPrice.setVisibility(View.VISIBLE);
                holder.price.setText("¥ " + shopList.get(position).getDisprice());
                holder.oldPrice.setText("市场价：¥ " + shopList.get(position).getOldprice());
            }
        } else {
            holder.oldPrice.setVisibility(View.VISIBLE);
            holder.price.setText("¥ " + shopList.get(position).getDisprice());
            holder.oldPrice.setText("原价：¥ " + shopList.get(position).getPrice());
        }
        if ("0".equals(shopList.get(position).getStore())) {
            holder.storeLayout.setVisibility(View.VISIBLE);
        } else {
            holder.storeLayout.setVisibility(View.GONE);
        }
        return itemView;
    }

    class ViewHolder {
        ImageView shopImg;
        TextView shopName;
        ImageView image;
        TextView oldPrice;
        TextView price;
        ImageView youhuijuan;
        ImageView customPic;
        RelativeLayout storeLayout;
        RelativeLayout shopLayout;
    }

}
