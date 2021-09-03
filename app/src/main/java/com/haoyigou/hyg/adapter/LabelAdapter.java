package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.entity.ShopEntry;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;

import java.util.List;

/**
 * Created by wuliang on 2017/3/13.
 * <p>
 * 标签页商品列表适配器
 */

public class LabelAdapter extends BaseAdapter implements View.OnClickListener {

    private List<ShopEntry> list;
    private Context context;
    private String productTabId;

    public LabelAdapter(Context context, String productTabId, List<ShopEntry> list) {
        this.context = context;
        this.list = list;
        this.productTabId = productTabId;
    }


    @Override
    public int getCount() {
        if (list.size() % 2 == 0) {
            return list.size() / 2;
        } else {
            return list.size() / 2 + 1;
        }
    }

    @Override
    public Object getItem(int i) {   //只返回奇数项数据
        i = i * 2;
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_label_layout, null);
            holder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            holder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            holder.shopImage = (ImageView) view.findViewById(R.id.shop_image);
            holder.shopImage02 = (ImageView) view.findViewById(R.id.shop_image02);
            holder.shopMessage = (TextView) view.findViewById(R.id.shop_message);
            holder.shopMessage02 = (TextView) view.findViewById(R.id.shop_message02);
            holder.marketPrice = (TextView) view.findViewById(R.id.market_price);
            holder.marketPrice02 = (TextView) view.findViewById(R.id.market_price02);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.price02 = (TextView) view.findViewById(R.id.price02);
            holder.customPic = (ImageView) view.findViewById(R.id.custom_pic);
            holder.customPic02 = (ImageView) view.findViewById(R.id.custom_pic02);
            holder.storeLayout = (RelativeLayout) view.findViewById(R.id.store_layout);
            holder.storeLayout02 = (RelativeLayout) view.findViewById(R.id.store_layout02);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ShopEntry item = (ShopEntry) getItem(i);
        holder.leftLayout.setTag(item);
        holder.leftLayout.setOnClickListener(this);
        if (StringUtils.isEmpty(item.getPiclogo())) {
            holder.shopImage.setImageResource(R.drawable.default_image);
        } else {
            Glide.with(context).load(item.getPiclogo()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.shopImage);
        }
        holder.shopMessage.setText(item.getName());
        holder.marketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.marketPrice.setVisibility(View.VISIBLE);
        if ("-3".equals(productTabId)) {
            holder.marketPrice.setText("市场价：¥" + String.format("%.2f", Double.parseDouble(item.getPrice())));
            holder.price.setText("¥" + String.format("%.2f", Double.parseDouble(item.getNewprice())));
        } else {
            holder.marketPrice.setText("市场价：¥" + String.format("%.2f", Double.parseDouble(item.getOldprice())));
            holder.price.setText("¥" + String.format("%.2f", Double.parseDouble(item.getDisprice())));
        }
        if (StringUtils.isEmpty(item.getPic5())) {
            holder.customPic.setVisibility(View.GONE);
        } else {
            holder.customPic.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getPic5())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.customPic);
        }
        if ("0".equals(item.getStore())) {
            holder.storeLayout.setVisibility(View.VISIBLE);
        } else {
            holder.storeLayout.setVisibility(View.GONE);
        }
        i = 2 * i + 1;
        if (i >= list.size()) {
            holder.rightLayout.setVisibility(View.INVISIBLE);
        } else {
            holder.rightLayout.setVisibility(View.VISIBLE);
            ShopEntry item02 = list.get(i);
            holder.rightLayout.setTag(item02);
            holder.rightLayout.setOnClickListener(this);
            if (StringUtils.isEmpty(item02.getPiclogo())) {
                holder.shopImage02.setImageResource(R.drawable.default_image);
            } else {
                Glide.with(context).load(item02.getPiclogo()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.shopImage02);
            }
            holder.shopMessage02.setText(item02.getName());
            holder.marketPrice02.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.marketPrice02.setVisibility(View.VISIBLE);
            if ("-3".equals(productTabId)) {
                holder.marketPrice02.setText("市场价：¥" + String.format("%.2f", Double.parseDouble(item02.getPrice())));
                holder.price02.setText("¥" + String.format("%.2f", Double.parseDouble(item02.getNewprice())));
            } else {
                holder.marketPrice02.setText("市场价：¥" + String.format("%.2f", Double.parseDouble(item02.getOldprice())));
                holder.price02.setText("¥" + String.format("%.2f", Double.parseDouble(item02.getDisprice())));
            }
            if (StringUtils.isEmpty(item02.getPic5())) {
                holder.customPic02.setVisibility(View.GONE);
            } else {
                holder.customPic02.setVisibility(View.VISIBLE);
                Glide.with(context).load(item02.getPic5())
                        .diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.customPic02);
            }
            if ("0".equals(item02.getStore())) {
                holder.storeLayout02.setVisibility(View.VISIBLE);
            } else {
                holder.storeLayout02.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        ShopEntry entry = (ShopEntry) view.getTag();
        Intent intent = new Intent(context, HomeWebViewAct.class);
        if (!MApplication.labelParentLocation.equals("")) {
            intent.putExtra("url", "/pix/" + entry.getId() + "/" +
                    SharedPreferencesUtils.getInstance().getString("distributorId", "1")
                    + ".html?accessType=1&source=1&showplat=2&parentLocation="+MApplication.labelParentLocation);
        }else {
            intent.putExtra("url", "/pix/" + entry.getId() + "/" +
                    SharedPreferencesUtils.getInstance().getString("distributorId", "1")
                    + ".html?accessType=1&source=1&showplat=2");
        }
        context.startActivity(intent);
    }

    class ViewHolder {
        LinearLayout leftLayout, rightLayout;
        ImageView shopImage, shopImage02;
        TextView shopMessage, shopMessage02;
        TextView marketPrice, marketPrice02;
        TextView price, price02;
        ImageView customPic, customPic02;
        RelativeLayout storeLayout, storeLayout02;
    }
}
