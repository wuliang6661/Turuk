package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.entity.ClassifyShopEntry;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wuliang  on 2017/1/12.
 * <p>
 * 分类导航栏的适配器
 */

public class ClassifyAdapter extends BaseAdapter {

    private Context context;
    private List<ClassifyShopEntry> lists;
    private int oldPosition = 0;


    public ClassifyAdapter(Context context, List<ClassifyShopEntry> lists) {
        this.context = context;
        this.lists = lists;
    }

    public void setPosition(int position) {
        if (oldPosition != position) {
            oldPosition = position;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_classify_list, null);
            holder = new ViewHolder();
            holder.itemName = (TextView) view.findViewById(R.id.classify_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.itemName.setText(lists.get(i).getName());
        if (i == oldPosition) {
            holder.itemName.setBackgroundColor(context.getResources().getColor(R.color.title_bg));
            holder.itemName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.itemName.setBackgroundColor(Color.parseColor("#f0f0f0"));
            holder.itemName.setTextColor(Color.parseColor("#2b2b2b"));
        }
        return view;
    }

    class ViewHolder {
        TextView itemName;
    }
}
