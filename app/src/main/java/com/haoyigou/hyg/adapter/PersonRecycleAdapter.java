package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.LableEntry;
import com.haoyigou.hyg.entity.MenuEntry;
import com.haoyigou.hyg.ui.ChangeMyAddressWebviewActivity;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.SettingActivity;
import com.haoyigou.hyg.ui.TVLiveActivity;
import com.haoyigou.hyg.ui.VoucherCenterAct;
import com.haoyigou.hyg.ui.WebviewActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.view.dialog.CreateMemberDiaolg;
import com.haoyigou.hyg.view.widget.TaskDialog;

import java.util.List;

/**
 * Created by wuliang on 2016/12/7.
 * <p>
 * 个人中心显示的RecycleAdapter
 */

public class PersonRecycleAdapter extends RecyclerView.Adapter<PersonRecycleAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<String> titles;
    private List<Integer> images;
    CreateMemberDiaolg diaolg;
    private List<Integer> icons;
    private boolean isHaveGame = true;  //游戏是否能玩  默认为可以玩
    private boolean isReadVisable = false;   //阅读上是否显示小点，默认不显示

    private List<MenuEntry> menuData;      //自定义菜单内容

    private LableEntry entry;


    public PersonRecycleAdapter(Context context, List<String> titles, List<Integer> images) {
        this.context = context;
        this.titles = titles;
        this.images = images;
    }

    /**
     * 设置自定义菜单内容
     */
    public void setCustomMenu(List<MenuEntry> lists) {
        this.menuData = lists;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_girdview, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position >= titles.size()) {   //自定义菜单部分
            Glide.with(context).load(menuData.get(position - titles.size()).getImg()).into(holder.image);
            holder.title.setText(menuData.get(position - titles.size()).getTitle());
            holder.itemView.setTag(menuData.get(position - titles.size()).getTitle());
            holder.itemView.setOnClickListener(this);
        } else {
            holder.image.setImageResource(images.get(position));
            holder.title.setText(titles.get(position));
            holder.itemView.setTag(titles.get(position));
            holder.itemView.setOnClickListener(this);
            if (titles.get(position).equals("游戏")) {
                if (!isHaveGame) {
                    holder.image.setImageResource(R.drawable.game_lock);
                }
            }
            if (titles.get(position).equals("阅读")) {
                if (isReadVisable) {
                    holder.point.setVisibility(View.VISIBLE);
                } else {
                    holder.point.setVisibility(View.GONE);
                }
            }
            if (titles.get(position).equals("新人专享")) {
                holder.point.setVisibility(View.VISIBLE);
            }
            if (titles.get(position).equals("充值中心") || titles.get(position).equals("电子发票") ||
                    titles.get(position).equals("拼团")) {
//                holder.newImage.setVisibility(View.VISIBLE);
            } else {
                holder.newImage.setVisibility(View.GONE);
            }
        }
        if (position < titles.size()) {
            if (entry != null && titles.get(position) != null) {
                if (titles.get(position).equals("我的收益") && "1".equals(entry.getIncome())) {
                    holder.point.setVisibility(View.VISIBLE);
                } else {
                    holder.point.setVisibility(View.GONE);
                }
                if (titles.get(position).equals("优惠券") && "1".equals(entry.getDiscount())) {
                    holder.point.setVisibility(View.VISIBLE);
                } else {
                    holder.point.setVisibility(View.GONE);
                }
            }
        }


//        if (position > 1) {
//            if (icons != null && icons.size() != 0) {
//                if (position - 1 < icons.size()) {
//                    if (icons.get(position - 1) >= 1) {  //原先为1
//                        holder.point.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.point.setVisibility(View.GONE);
//                    }
//                }
//            }
//        }
    }

    @Override
    public int getItemCount() {
        if (menuData == null || menuData.size() == 0) {
            return titles.size();
        }
        return titles.size() + menuData.size();
    }


    public void setLable(LableEntry entry) {
        this.entry = entry;
        notifyDataSetChanged();
    }


    /**
     * 设置小点
     */
    public void setIcons(List<Integer> icons) {
        Log.e("log--", icons.toString());
        if (this.icons != null) {
            if (IsrefreshAdapter(icons)) {
                this.icons.clear();
                this.icons.addAll(icons);
                notifyDataSetChanged();
            }
        } else {
            this.icons = icons;
            notifyDataSetChanged();
        }
        Log.e("log--", icons.toString());
    }

    /**
     * 检测是否数据有改变，否则不刷新布局
     */
    private boolean IsrefreshAdapter(List<Integer> imgs) {
        if (icons.size() != imgs.size()) return false;
        for (int i = 0; i < icons.size(); i++) {
            if (icons.get(i) != imgs.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置阅读的小点是否显示
     */
    public void IsReadPoint(boolean isReadVisable) {
        this.isReadVisable = isReadVisable;
        this.notifyDataSetChanged();
    }


    /***
     * 设置游戏是否能玩
     */
    public void IsHaveGame(boolean isHaveGame) {
        this.isHaveGame = isHaveGame;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, PersonWebViewAct.class);
        String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
        String title = (String) view.getTag();
        Log.e("log--", title);
        switch (title.trim()) {
            case "积分商城":
                intent.putExtra("url", HttpClient.POINT_SCORE + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "我的粉丝":
                intent.putExtra("url", HttpClient.MYTEAM + "?newversion=1&distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "我的收益":
                intent.putExtra("url", HttpClient.MYGAINS + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "我要提现":
                intent.putExtra("url", HttpClient.MYREFLECT + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "我的金币":
                intent.putExtra("url", HttpClient.MYPRICE + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "我的积分":
                intent.putExtra("url", HttpClient.MYINTERNAGER + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "积分兑换":
                intent.putExtra("url", HttpClient.POINTINTERGER + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "优惠券":
                intent.putExtra("url", HttpClient.VALUEVOUCHER + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "我的收藏":
                intent.putExtra("url", HttpClient.FAVORITE + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "好易购学院":
                intent.putExtra("url", HttpClient.RUODUOCLASS + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "在线客服":
                intent.putExtra("url", HttpClient.CONTACTUS + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "充值中心":
                Intent intentVoucher = new Intent(context, VoucherCenterAct.class);
                intentVoucher.putExtra("parentLocation","112");
                context.startActivity(intentVoucher);
                break;
            case "地址管理":
                Intent intent5 = new Intent(context, ChangeMyAddressWebviewActivity.class);
                context.startActivity(intent5);
                break;
            case "电子发票":
                intent.putExtra("url", HttpClient.INVOICE + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "拼团订单":
                intent.putExtra("url", HttpClient.ALLGROUP + "?distributorId=" + disid+"&parentLocation=112");
//                intent.putExtra("title","拼团订单");
                context.startActivity(intent);
                break;
            case "邀请码":
                intent.putExtra("url", HttpClient.YAOQINGMA + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "绑定会员":
                if (diaolg != null) {
                    diaolg.showDialog();
                    break;
                }
                diaolg = new CreateMemberDiaolg(context);
                diaolg.setOnBinding(new CreateMemberDiaolg.onBindingListener() {
                    @Override
                    public void isBinding(boolean isBinding) {  //隐藏会员绑定
                        if (dissmissListener != null) {
                            dissmissListener.onDissmiss();
                        }
                    }
                });
                diaolg.showDialog();
                break;
            case "新人专享":
//                intent.putExtra("url", HttpClient.ACTIVITYLISTWEBVIEW + "?productTabId=-3&distributorId=" + disid);
//                context.startActivity(intent);
                MApplication.labelParentLocation = "2";
                Intent labelIntent = new Intent(context, LabelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productTabId", "-3");
                labelIntent.putExtras(bundle);
                context.startActivity(labelIntent);
                break;
            case "游戏":
                if (isHaveGame) {
                    Intent intent1 = new Intent(context, WebviewActivity.class);
                    intent1.putExtra("url", HttpClient.GAME + "?distributorId=" + disid);
                    context.startActivity(intent1);
                } else {
                    Toast.makeText(context, "游戏暂未开通！", Toast.LENGTH_SHORT).show();
                }
                break;
            case "邀请好友":
                intent.putExtra("url", HttpClient.RECOMMEND + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "阅读":
                intent.putExtra("url", HttpClient.TASKINDEX + "?distributorId=" + disid);
                context.startActivity(intent);
                break;
            case "新手任务":
                TaskDialog dialog = new TaskDialog(context);
                dialog.showDialog();
                break;
            default:    //自定义菜单部分
                for (int i = 0; i < menuData.size(); i++) {
                    if (title.equals(menuData.get(i).getTitle())) {
                        Intent menuIntent = new Intent(context, HomeWebViewAct.class);
                        menuIntent.putExtra("all", true);
                        menuIntent.putExtra("url", menuData.get(i).getUrl());
                        context.startActivity(menuIntent);
                    }
                }
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        ImageView point;
        ImageView newImage;

        MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            point = (ImageView) itemView.findViewById(R.id.ruiduo_image);
            newImage = (ImageView) itemView.findViewById(R.id.new_img);
        }
    }

    private onDissmissListener dissmissListener;

    public void setonDissmissListener(onDissmissListener listener) {
        dissmissListener = listener;
    }

    public interface onDissmissListener {

        void onDissmiss();
    }

}
