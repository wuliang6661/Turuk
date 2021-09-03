package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.entity.VoucherEntry;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuliang on 2017/3/31.
 * <p>
 * 充值话费和流量的适配器
 */

public class VoucherAdapter extends BaseAdapter {

    private Context context;
    private List<VoucherEntry> list;
    private int type;    //1为话费充值

    private int disButton = -1;   //默认一个都没选中
    private onClick click;


    public VoucherAdapter(Context context, List<VoucherEntry> list, int type) {
        this.type = type;
        this.context = context;
        this.list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (type == 1 && list.get(i).getOrdertype().equals("01")) {
                this.list.add(list.get(i));
            }
            if (type == 2 && list.get(i).getOrdertype().equals("02")) {
                this.list.add(list.get(i));
            }
        }
    }

    /**
     * 设置接口回调
     */
    public void setClick(onClick click) {
        this.click = click;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_voucher_layout, null);
            holder = new ViewHolder();
            holder.vocherBg = (LinearLayout) convertView.findViewById(R.id.item_voucher_bg);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.vocherPrice = (TextView) convertView.findViewById(R.id.change);
            holder.vocherDisPrice = (TextView) convertView.findViewById(R.id.change_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(list.get(position).getIsShowIcon()==1){
            holder.image.setVisibility(View.VISIBLE);
        }
        holder.vocherPrice.setText(list.get(position).getRechargeName());
        if (disButton == position) {


            holder.vocherBg.setBackgroundResource(R.drawable.item_voucher_checkd);
            holder.vocherDisPrice.setTextColor(Color.parseColor("#0E4394"));
            holder.vocherPrice.setTextColor(Color.parseColor("#0E4394"));
        } else {
            holder.vocherBg.setBackgroundResource(R.drawable.item_voucher_nocheck);
            holder.vocherDisPrice.setTextColor(Color.parseColor("#B8B8B8"));
            holder.vocherPrice.setTextColor(Color.parseColor("#2b2b2b"));
        }
        holder.vocherBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disButton = position;
                notifyDataSetChanged();
                if (click != null) {
                    click.onClickData(list.get(position));
                }
            }
        });
        holder.vocherDisPrice.setVisibility(View.VISIBLE);
        holder.vocherDisPrice.setText("售价：" + list.get(position).getPricetemp() + "元");
        return convertView;
    }


    class ViewHolder {
        LinearLayout vocherBg;
        TextView vocherPrice;
        TextView vocherDisPrice;
        ImageView image;
    }

    public interface onClick {

        void onClickData(VoucherEntry entry);
    }

}
