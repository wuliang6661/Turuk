package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.entity.BerserkEntry;
import com.haoyigou.hyg.entity.BerserkListEntry;
import com.haoyigou.hyg.entity.MadrushBO;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.view.widget.ListViewForScrollView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuliang on 2016/11/25.
 * <p>
 * 尝试优化疯抢倒计时2.0
 */

public class BerserListAdapterBase extends BaseAdapter implements View.OnClickListener {

    private static int index = 1;   //为1时切换为正在疯抢，为2时切换下期预告
    private List<MadrushBO> data;
    private Context context;
    private boolean isaa = true;
    private Map<Integer, View> maps = new HashMap<>();

    public BerserListAdapterBase(Context context, List<MadrushBO> list) {
        this.data = list;
        this.context = context;
    }

    /***
     * 此方法提供刷新适配器数据
     */
    public void setIndex(int indexa) {
        index = indexa;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_fengqiang_layout, null);
            holder = new ViewHolder();
            holder.list = (ListViewForScrollView) view.findViewById(R.id.item_fengqiang_list);
            holder.time_text = (TextView) view.findViewById(R.id.time_text);
            holder.hour = (TextView) view.findViewById(R.id.hour_text);
            holder.minute = (TextView) view.findViewById(R.id.minute_text);
            holder.second = (TextView) view.findViewById(R.id.second_text);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        holder.list.setAdapter(setItemListAdapter(data.get(i).getProduct()));
        if (index == 1) {
            holder.time_text.setText("距结束还剩：");
        } else if (index == 2) {
            holder.time_text.setText("距开始还剩：");
        }
        return view;
    }

    class ViewHolder {
        ListViewForScrollView list;
        TextView hour;
        TextView minute;
        TextView second;
        TextView time_text;
    }


    QuickAdapter<MadrushBO.ProductBean> adapter;


    private QuickAdapter setItemListAdapter(List<MadrushBO.ProductBean> data) {
        adapter = new QuickAdapter<MadrushBO.ProductBean>(context, R.layout.item_berserk_layout, data) {
            @Override
            protected void convert(BaseAdapterHelper helper, MadrushBO.ProductBean item) {
                if (item.getPiclogo() != null && !item.getPiclogo().equals("")) {
//                    helper.setImageUrl(R.id.commodity_img, item.getPiclogo() == null ? "" : item.getPiclogo());
                    Glide.with(context).load(item.getPiclogo()).asBitmap().into((ImageView)helper.getView(R.id.commodity_img));
                } else {
                    helper.setImageResource(R.id.commodity_img, R.drawable.default_image);
                }
                helper.setText(R.id.commodity_name, item.getName());
                helper.setText(R.id.commodity_price, String.format("%s", item.getDisprice()));
                TextView view = helper.getView(R.id.commodity_old_price);
                view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                view.setText(String.format("市场价：¥ %s", item.getPrice()));
                if (index == 1) {
                    helper.setText(R.id.go_price, "去抢购");
                } else if (index == 2) {
                    helper.setText(R.id.go_price, "即将开抢");
                }
                if ("0".equals(item.getOverplus())) {
//                    helper.setBackgroundRes(R.id.go_price, R.drawable.button_red2_brolder);
//                    helper.setTextColor(R.id.go_price, Color.parseColor("#ffffff"));
                    helper.setText(R.id.go_price, "已抢完");
                } else {
//                    helper.setBackgroundRes(R.id.go_price, R.drawable.button_red2_brolder);
                }
                helper.getView(R.id.go_price).setTag(item);
//                helper.getView(R.id.commodity_img).setTag(item);
                helper.getView(R.id.go_price).setOnClickListener(BerserListAdapterBase.this);
                helper.getView(R.id.commodity_img).setOnClickListener(BerserListAdapterBase.this);
                helper.getView(R.id.button).setOnClickListener(v -> {
                    Intent intent = new Intent(context, HomeWebViewAct.class);
                    intent.putExtra("all", true);
                    if (item.getJumpAdress().contains("?")) {
                        intent.putExtra("url", item.getJumpAdress()+"&parentLocation="+MApplication.madrushParentLocation);
                    }else {
                        intent.putExtra("url", item.getJumpAdress()+"?parentLocation="+MApplication.madrushParentLocation);
                    }
                    context.startActivity(intent);
                });
                helper.getView(R.id.commodity_img).setOnClickListener(v -> {
                    Intent intent = new Intent(context, HomeWebViewAct.class);
                    intent.putExtra("all", true);
                    if (item.getJumpAdress().contains("?")) {
                        intent.putExtra("url", item.getJumpAdress()+"&parentLocation="+MApplication.madrushParentLocation);
                    }else {
                        intent.putExtra("url", item.getJumpAdress()+"?parentLocation="+MApplication.madrushParentLocation);
                    }
                    context.startActivity(intent);
                });
            }
        };
        return adapter;
    }


    @Override
    public void onClick(View view) {
//        MadrushBO.ProductBean entry = (MadrushBO.ProductBean) view.getTag();
//        Intent intent = new Intent(context, HomeWebViewAct.class);
//        intent.putExtra("all", true);
//        if (entry.getJumpAdress().contains("?")) {
//            intent.putExtra("url", entry.getJumpAdress()+"&parentLocation="+MApplication.madrushParentLocation);
//        }else {
//            intent.putExtra("url", entry.getJumpAdress()+"?parentLocation="+MApplication.madrushParentLocation);
//        }
//        context.startActivity(intent);
    }
}
