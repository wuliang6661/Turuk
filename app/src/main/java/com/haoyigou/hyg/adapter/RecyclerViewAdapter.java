package com.haoyigou.hyg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.ClassifyEntry;
import com.haoyigou.hyg.ui.LoginActivity;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.ui.TVLiveActivity;
import com.haoyigou.hyg.ui.VoucherCenterAct;

import java.util.List;

/**
 * Created by wuliang on 2016/11/22.
 * <p>
 * <p>
 * RecyclerView的适配器初体验
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private List<ClassifyEntry> list;
    private Context context;
    private static int index = 0;   //默认第一个为选中的

    public RecyclerViewAdapter(Context context, List<ClassifyEntry> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classify_layout, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 1) {
            holder.item_text.setText("充值中心");
        } else if (position == 2) {
            holder.item_text.setText("TV直播");
        } else if (position == 0) {
            holder.item_text.setText(list.get(position).getName());
        } else {
            holder.item_text.setText(list.get(position - 2).getName());
        }
        if (index == position) {
            holder.item_text.setTextColor(Color.parseColor("#e60012"));
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.item_text.setTextColor(Color.parseColor("#666666"));
            holder.line.setVisibility(View.GONE);
        }
    }


    /**
     * 用来设置选中的显示
     */
    public void setIndex(int aaindex) {
        index = aaindex;
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView item_text;
        View line;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_text = (TextView) itemView.findViewById(R.id.sift_text);
            line = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            setIndex(getPosition());
            if (getPosition() == 0) return;
            if (getPosition() == 1) {
                if (StateMessage.IS_LOGIN) {
                    Intent intentVoucher = new Intent(context, VoucherCenterAct.class);
                    context.startActivity(intentVoucher);
                    return;
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("addFinish", true);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    return;
                }
            }
            if (getPosition() == 2) {
                Intent mIntent = new Intent(context, TVLiveActivity.class);
                context.startActivity(mIntent);
                return;
            }
            Intent intent = new Intent(context, SelectorActivity.class);
            intent.putExtra("Pfflid", list.get(getPosition() - 2).getId());
            context.startActivity(intent);
        }
    }

}
