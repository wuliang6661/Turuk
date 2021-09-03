package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.haoyigou.hyg.R;
import com.haoyigou.hyg.entity.RenQiBO;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.MadrushAct;
import com.haoyigou.hyg.ui.PopularityAct;
import com.haoyigou.hyg.ui.SelectionAct;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wuliang on 2017/10/20.
 * <p>
 * 人气推荐适配器
 */

public class HomeRenQiAdapter extends BaseAdapter {


    List<RenQiBO> recommend;
    Context context;
    String disId;

    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;
    final int TYPE_4 = 3;
    final int TYPE_5 = 4;
    final int TYPE_6 = 5;


    public HomeRenQiAdapter(Context context, String disId, List<RenQiBO> recommend) {
        this.context = context;
        this.disId = disId;
        this.recommend = recommend;
    }


    @Override
    public int getCount() {
        return recommend.size();
    }

    @Override
    public Object getItem(int position) {
        return recommend.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        RenQiBO renQiBO = (RenQiBO) getItem(position);
        switch (renQiBO.getMoudule_id()) {
            case 1304:
                return TYPE_1;
            case 1207:
                return TYPE_2;
            case 1203:
                return TYPE_3;
            case 1208:
                return TYPE_4;
            case 1202:
                return TYPE_5;
            case 1209:
                return TYPE_6;
            default:
                return TYPE_1;
        }
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 6;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RenQiBO renQiBO = (RenQiBO) getItem(position);
        final List<RenQiBO.ModuleElementBo> shop = renQiBO.getModuleElement();
        Holder1 holder1 = null;
        Hodler2 holder2 = null;
        Hodler3 holder3 = null;
        Holder4 holder4 = null;
        Holder5 holder5 = null;
        Holder6 holder6 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_renqi01, null);
                    holder1 = new Holder1(convertView);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_reqi02, null);
                    holder2 = new Hodler2(convertView);
                    convertView.setTag(holder2);
                    break;
                case TYPE_3:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_renqi03, null);
                    holder3 = new Hodler3(convertView);
                    convertView.setTag(holder3);
                    break;
                case TYPE_4:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_renqi04, null);
                    holder4 = new Holder4(convertView);
                    convertView.setTag(holder4);
                    break;
                case TYPE_5:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_renqi05, null);
                    holder5 = new Holder5(convertView);
                    convertView.setTag(holder5);
                    break;
                case TYPE_6:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_renqi06, null);
                    holder6 = new Holder6(convertView);
                    convertView.setTag(holder6);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    holder1 = (Holder1) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (Hodler2) convertView.getTag();
                    break;
                case TYPE_3:
                    holder3 = (Hodler3) convertView.getTag();
                    break;
                case TYPE_4:
                    holder4 = (Holder4) convertView.getTag();
                    break;
                case TYPE_5:
                    holder5 = (Holder5) convertView.getTag();
                    break;
                case TYPE_6:
                    holder6 = (Holder6) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_1:
                setImageUrl(shop.get(0).getImgurl(), holder1.shopImg);
                holder1.commodityName.setText(shop.get(0).getProduct_name());
                holder1.commodityOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder1.commodityOldPrice.setText("市场价：¥ " + String.format("%.2f", shop.get(0).getProduct_oldprice()));
                holder1.price.setText("" + String.format("%.2f", shop.get(0).getProduct_disprice()));
                holder1.itemLayout.setTag(shop.get(0).getUrladdress());
                holder1.itemLayout.setOnClickListener(new View.OnClickListener() {
                 //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(0).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(0).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                break;
            case TYPE_2:
                setImageUrl(shop.get(0).getImgurl(), holder2.image1);
                setImageUrl(shop.get(1).getImgurl(), holder2.image2);
                setImageUrl(shop.get(2).getImgurl(), holder2.image3);
                holder2.image1.setTag(shop.get(0).getUrladdress());
                holder2.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){

                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(0).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(0).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder2.image2.setTag(shop.get(1).getUrladdress());
                holder2.image2.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(1).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(1).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(1).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(1).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder2.image3.setTag(shop.get(2).getUrladdress());
                holder2.image3.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(2).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(2).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(2).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                break;
            case TYPE_3:
                setImageUrl(shop.get(0).getImgurl(), holder3.image3);
                setImageUrl(shop.get(1).getImgurl(), holder3.image1);
                setImageUrl(shop.get(2).getImgurl(), holder3.image2);
                holder3.image1.setTag(shop.get(1).getUrladdress());
                holder3.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        Log.d("adsfadfasd", "onClick: "+shop.get(1).getJumptype());
                        if(shop.get(1).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(1).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(1).getJumptype()==5){//分类
                            Log.d("adsfadfasd", "onClick: "+shop.get(1).getIdparam());
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(1).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder3.image2.setTag(shop.get(2).getUrladdress());
                holder3.image2.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        Log.d("adsfadfasd", "onClick: "+shop.get(2).getJumptype());
                        if(shop.get(2).getJumptype()==0){

                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(2).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            Log.d("adsfadfasd", "onClick: "+shop.get(2).getIdparam());
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(2).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder3.image3.setTag(shop.get(0).getUrladdress());
                holder3.image3.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        Log.d("adsfadfasd", "onClick: "+shop.get(0).getJumptype());
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(0).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            Log.d("adsfadfasd", "onClick: "+shop.get(0).getIdparam());
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            Log.d("adsfadfasd", "onClick: "+shop.get(0).getIdparam());
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(0).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                break;
            case TYPE_4:
                setImageUrl(shop.get(0).getImgurl(), holder4.image1);
                setImageUrl(shop.get(1).getImgurl(), holder4.image2);
                holder4.image1.setTag(shop.get(0).getUrladdress());
                holder4.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(0).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(0).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder4.image2.setTag(shop.get(1).getUrladdress());
                holder4.image2.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(1).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(1).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(1).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(1).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                break;
            case TYPE_5:
                setImageUrl(shop.get(0).getImgurl(), holder5.image1);
                setImageUrl(shop.get(1).getImgurl(), holder5.image2);
                setImageUrl(shop.get(2).getImgurl(), holder5.image3);
                setImageUrl(shop.get(3).getImgurl(), holder5.image4);
                holder5.image1.setTag(shop.get(0).getUrladdress());
                holder5.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(0).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(0).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(0).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder5.image2.setTag(shop.get(1).getUrladdress());
                holder5.image2.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(1).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(1).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签
                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(1).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder5.image3.setTag(shop.get(2).getUrladdress());
                holder5.image3.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(2).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(2).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(2).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder5.image4.setTag(shop.get(3).getUrladdress());
                holder5.image4.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(3).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(3).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(3).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(3).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(3).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(3).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(3).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(3).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(3).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(3).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                break;
            case TYPE_6:
                setImageUrl(shop.get(0).getImgurl(), holder6.image1);
                setImageUrl(shop.get(1).getImgurl(), holder6.image2);
                setImageUrl(shop.get(2).getImgurl(), holder6.image3);
                holder6.image1.setTag(shop.get(0).getUrladdress());
                holder6.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(0).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(0).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(0).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder6.image2.setTag(shop.get(1).getUrladdress());
                holder6.image2.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(1).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(1).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(1).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(1).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                holder6.image3.setTag(shop.get(2).getUrladdress());
                holder6.image3.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(2).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            intent.putExtra("url", shop.get(2).getUrladdress());
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签

                                Intent intent3 = new Intent(context, LabelActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                                intent3.putExtras(bundle);
                                context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(2).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                break;
        }
        return convertView;
    }

    private void setImageUrl(String url, ImageView imageView) {
        if (StringUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.default_image);
        } else {
            Picasso.with(context).load(url).into(imageView);
        }
    }


    class Holder1 {
        LinearLayout itemLayout;
        ImageView shopImg;
        TextView commodityName;
        TextView commodityOldPrice;
        TextView price;

        public Holder1(View view) {
            itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            shopImg = (ImageView) view.findViewById(R.id.commodity_img);
            commodityName = (TextView) view.findViewById(R.id.commodity_name);
            commodityOldPrice = (TextView) view.findViewById(R.id.commodity_old_price);
            price = (TextView) view.findViewById(R.id.commodity_price);
        }

    }

//    View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            String url = (String) v.getTag();
//            Intent intent = new Intent(context, HomeWebViewAct.class);
//            intent.putExtra("url", url);
//            intent.putExtra("all", true);
//            context.startActivity(intent);
//        }
//    };


    class Hodler2 {

        ImageView image1;
        ImageView image2;
        ImageView image3;

        public Hodler2(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);
        }
    }

    class Hodler3 {

        ImageView image1;
        ImageView image2;
        ImageView image3;

        public Hodler3(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);
        }
    }


    class Holder4 {

        ImageView image1;
        ImageView image2;

        public Holder4(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
        }
    }


    class Holder5 {

        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;

        public Holder5(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);
            image4 = (ImageView) view.findViewById(R.id.image4);
        }

    }


    class Holder6 {

        ImageView image1;
        ImageView image2;
        ImageView image3;

        public Holder6(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);
        }

    }

}