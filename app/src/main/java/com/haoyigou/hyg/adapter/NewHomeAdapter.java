package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.entity.HomeThreeBO;
import com.haoyigou.hyg.entity.NewHomeBO;
import com.haoyigou.hyg.entity.RenQiBO;
import com.haoyigou.hyg.ui.LabelActivity;
import com.haoyigou.hyg.ui.MadrushAct;
import com.haoyigou.hyg.ui.PopularityAct;
import com.haoyigou.hyg.ui.RoundImageView;
import com.haoyigou.hyg.ui.SelectionAct;
import com.haoyigou.hyg.ui.SelectorActivity;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wuliang on 2017/10/20.
 * <p>
 * 首页新改版
 */

public class NewHomeAdapter extends BaseAdapter {


    List<NewHomeBO.UpperRecommendBean.ItemsBean> recommend;
    Context context;
    String disId;
    private String color;

    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;
    final int TYPE_4 = 3;
    final int TYPE_5 = 4;
    final int TYPE_6 = 5;
    final int TYPE_7 = 6;


    public NewHomeAdapter(Context context, String disId, List<NewHomeBO.UpperRecommendBean.ItemsBean> recommend,String color) {
        this.context = context;
        this.disId = disId;
        this.recommend = recommend;
        if (color.contains("#")) {
            this.color = color;
        }else {
            this.color = "#" + color;
        }
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
        NewHomeBO.UpperRecommendBean.ItemsBean renQiBO = (NewHomeBO.UpperRecommendBean.ItemsBean) getItem(position);
        switch (renQiBO.getMoudule_id()) {
            case 1210:
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
            case 1304:
                return TYPE_7;
            default:
                return TYPE_1;
        }
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 7;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewHomeBO.UpperRecommendBean.ItemsBean renQiBO = (NewHomeBO.UpperRecommendBean.ItemsBean) getItem(position);
        final List<NewHomeBO.UpperRecommendBean.ItemsBean.ModuleElementBean> shop = renQiBO.getModuleElement();
        Holder1 holder1 = null;
        Hodler2 holder2 = null;
        Hodler3 holder3 = null;
        Holder4 holder4 = null;
        Holder5 holder5 = null;
        Holder6 holder6 = null;
        Holder7 holder7 = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_item_01, null);
                    holder1 = new Holder1(convertView);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_item_02, null);
                    holder2 = new Hodler2(convertView);
                    convertView.setTag(holder2);
                    break;
                case TYPE_3:
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_item_03, null);
                    holder3 = new Hodler3(convertView);
                    convertView.setTag(holder3);
                    break;
                case TYPE_4:
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_item_04, null);
                    holder4 = new Holder4(convertView);
                    convertView.setTag(holder4);
                    break;
                case TYPE_5:
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_item_05, null);
                    holder5 = new Holder5(convertView);
                    convertView.setTag(holder5);
                    break;
                case TYPE_6:
                    convertView = LayoutInflater.from(context).inflate(R.layout.new_item_06, null);
                    holder6 = new Holder6(convertView);
                    convertView.setTag(holder6);
                    break;
                case TYPE_7:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_renqi01, null);
                    holder7 = new Holder7(convertView);
                    convertView.setTag(holder7);
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
                case TYPE_7:
                    holder7 = (Holder7) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_1:
                setImageUrl(shop.get(0).getImgurl(), holder1.image1);

                ViewGroup.LayoutParams params = holder1.image1.getLayoutParams();
                params.height=(int) (DisplayUtils.getScreenWidth(context) * 0.3);
                params.width =(int) (DisplayUtils.getScreenWidth(context) * 0.97);
                holder1.image1.setLayoutParams(params);

                holder1.llOne.setBackgroundColor(Color.parseColor(color));
                holder1.image1.setOnClickListener(new View.OnClickListener() {
                 //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(0).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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

                ViewGroup.LayoutParams params21 = holder2.image1.getLayoutParams();
                params21.height=(int) (DisplayUtils.getScreenWidth(context) * 0.23);
                params21.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder2.image1.setLayoutParams(params21);

                ViewGroup.LayoutParams params22 = holder2.image2.getLayoutParams();
                params22.height=(int) (DisplayUtils.getScreenWidth(context) * 0.23);
                params22.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder2.image2.setLayoutParams(params22);

                ViewGroup.LayoutParams params23 = holder2.image3.getLayoutParams();
                params23.height=(int) (DisplayUtils.getScreenWidth(context) * 0.5);
                params23.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder2.image3.setLayoutParams(params23);


                holder2.llOne.setBackgroundColor(Color.parseColor(color));
                holder2.image1.setTag(shop.get(0).getUrladdress());
                holder2.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){

                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(0).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(1).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(1).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(2).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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

                ViewGroup.LayoutParams params31 = holder3.image1.getLayoutParams();
                params31.height=(int) (DisplayUtils.getScreenWidth(context) * 0.23);
                params31.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder3.image1.setLayoutParams(params31);

                ViewGroup.LayoutParams params32 = holder3.image2.getLayoutParams();
                params32.height=(int) (DisplayUtils.getScreenWidth(context) * 0.23);
                params32.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder3.image2.setLayoutParams(params32);

                ViewGroup.LayoutParams params33 = holder3.image3.getLayoutParams();
                params33.height=(int) (DisplayUtils.getScreenWidth(context) * 0.5);
                params33.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder3.image3.setLayoutParams(params33);

                holder3.llThree.setBackgroundColor(Color.parseColor(color));
                holder3.image1.setTag(shop.get(1).getUrladdress());
                holder3.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
//                        Log.d("adsfadfasd", "onClick: "+shop.get(1).getJumptype());
                        if(shop.get(1).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(1).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(1).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
//                            Log.d("adsfadfasd", "onClick: "+shop.get(1).getIdparam());
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
//                        Log.d("adsfadfasd", "onClick: "+shop.get(2).getJumptype());
                        if(shop.get(2).getJumptype()==0){

                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(2).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
//                            Log.d("adsfadfasd", "onClick: "+shop.get(2).getIdparam());
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
//                        Log.d("adsfadfasd", "onClick: "+shop.get(0).getJumptype());
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(0).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
//                            Log.d("adsfadfasd", "onClick: "+shop.get(0).getIdparam());
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
//                            Log.d("adsfadfasd", "onClick: "+shop.get(0).getIdparam());
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

                ViewGroup.LayoutParams params41 = holder4.image1.getLayoutParams();
                params41.height=(int) (DisplayUtils.getScreenWidth(context) * 0.23);
                params41.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder4.image1.setLayoutParams(params41);

                ViewGroup.LayoutParams params42 = holder4.image2.getLayoutParams();
                params42.height=(int) (DisplayUtils.getScreenWidth(context) * 0.23);
                params42.width =(int) (DisplayUtils.getScreenWidth(context) * 0.47);
                holder4.image2.setLayoutParams(params42);

                holder4.llFour.setBackgroundColor(Color.parseColor(color));
                holder4.image1.setTag(shop.get(0).getUrladdress());
                holder4.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(0).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(1).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(1).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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

                ViewGroup.LayoutParams params51 = holder5.image1.getLayoutParams();
                params51.height=(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                params51.width =(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                holder5.image1.setLayoutParams(params51);

                ViewGroup.LayoutParams params52 = holder5.image2.getLayoutParams();
                params52.height=(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                params52.width =(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                holder5.image1.setLayoutParams(params52);

                ViewGroup.LayoutParams params53 = holder5.image3.getLayoutParams();
                params53.height=(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                params53.width =(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                holder5.image1.setLayoutParams(params53);

                ViewGroup.LayoutParams params54 = holder5.image4.getLayoutParams();
                params54.height=(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                params54.width =(int) (DisplayUtils.getScreenWidth(context) * 0.22);
                holder5.image1.setLayoutParams(params54);

                holder5.llFive.setBackgroundColor(Color.parseColor(color));
                holder5.image1.setTag(shop.get(0).getUrladdress());
                holder5.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(0).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);

                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(0).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(1).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(2).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(3).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(3).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(3).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(3).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(3).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(3).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(3).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(3).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(3).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(3).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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

                ViewGroup.LayoutParams params61 = holder6.image1.getLayoutParams();
                params61.height=(int) (DisplayUtils.getScreenWidth(context) * 0.3);
                params61.width =(int) (DisplayUtils.getScreenWidth(context) * 0.3);
                holder6.image1.setLayoutParams(params61);

                ViewGroup.LayoutParams params62 = holder6.image2.getLayoutParams();
                params62.height=(int) (DisplayUtils.getScreenWidth(context) * 0.3);
                params62.width =(int) (DisplayUtils.getScreenWidth(context) * 0.3);
                holder6.image1.setLayoutParams(params62);

                ViewGroup.LayoutParams params63 = holder6.image3.getLayoutParams();
                params63.height=(int) (DisplayUtils.getScreenWidth(context) * 0.3);
                params63.width =(int) (DisplayUtils.getScreenWidth(context) * 0.3);
                holder6.image1.setLayoutParams(params63);

                holder6.llSix.setBackgroundColor(Color.parseColor(color));
                holder6.image1.setTag(shop.get(0).getUrladdress());
                holder6.image1.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(0).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(0).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(1).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(1).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(1).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(1).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(1).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(1).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
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
                            if (shop.get(2).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(2).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(2).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(2).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(2).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(2).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);

                        }else if(shop.get(2).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(2).getIdparam().split(",");
                            intent3.putExtra("Pfflid", ids[0]);
                            intent3.putExtra("pfflid", ids[1]);
                            context.startActivity(intent3);
                        }
                    }
                });
                break;
            case TYPE_7:
                setImageUrl(shop.get(0).getImgurl(), holder7.shopImg);
                holder7.commodityName.setText(shop.get(0).getProduct_name());
                holder7.llBg.setBackgroundColor(Color.parseColor(color));
                holder7.commodityOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder7.commodityOldPrice.setText("市场价：¥ " + String.format("%s", shop.get(0).getProduct_oldprice()));
                holder7.price.setText("" + String.format("%s", shop.get(0).getProduct_disprice()));
                holder7.itemLayout.setTag(shop.get(0).getUrladdress());
                holder7.itemLayout.setOnClickListener(new View.OnClickListener() {
                    //   0：H5；1 疯抢原生；2 精选原生；3 人气原生；4 标签原生；
                    @Override
                    public void onClick(View v) {
                        if(shop.get(0).getJumptype()==0){
                            Intent intent = new Intent(context, HomeWebViewAct.class);
                            if (shop.get(0).getUrladdress().contains("?")) {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"&parentLocation=106");
                            }else {
                                intent.putExtra("url", shop.get(0).getUrladdress()+"?parentLocation=106");
                            }
                            intent.putExtra("all", true);
                            context.startActivity(intent);
                        }else if(shop.get(0).getJumptype()==1){//疯抢
                            MApplication.madrushParentLocation = "106";
                            Intent intent3 = new Intent(context, MadrushAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==2){//精选
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectionAct.class);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==3){//人气
                            MApplication.popularityParentLocation = "106";
                            Intent intent3 = new Intent(context,PopularityAct.class);
                            intent3.putExtra("idparam", shop.get(0).getIdparam());
                            intent3.putExtra("tatle", "人气推荐");
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==4){//标签
                            MApplication.labelParentLocation = "106";
                            Intent intent3 = new Intent(context, LabelActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("productTabId", "" + shop.get(0).getIdparam());
                            intent3.putExtras(bundle);
                            context.startActivity(intent3);
                        }else if(shop.get(0).getJumptype()==5){//分类
                            MApplication.selectorParentLocation = "106";
                            Intent intent3 = new Intent(context, SelectorActivity.class);
                            String[] ids = shop.get(0).getIdparam().split(",");
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
            Picasso.with(context).load(url).fit().into(imageView);
        }
    }


    class Holder1 {
        LinearLayout llOne;
        ImageView image1;


        public Holder1(View view) {
            llOne = (LinearLayout) view.findViewById(R.id.llOne);
            image1 = view.findViewById(R.id.image1);
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
        LinearLayout llOne;

        public Hodler2(View view) {
            image1 = view.findViewById(R.id.image1);
            image2 =view.findViewById(R.id.image2);
            image3 = view.findViewById(R.id.image3);
            llOne = view.findViewById(R.id.llOne);
        }
    }

    class Hodler3 {

        ImageView image1;
        ImageView image2;
        ImageView image3;
        LinearLayout llThree;

        public Hodler3(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);
            llThree = view.findViewById(R.id.llThree);
        }
    }


    class Holder4 {

        ImageView image1;
        ImageView image2;
        LinearLayout llFour;

        public Holder4(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            llFour = view.findViewById(R.id.llFour);
        }
    }


    class Holder5 {

        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        LinearLayout llFive;

        public Holder5(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);
            image4 = (ImageView) view.findViewById(R.id.image4);
            llFive = view.findViewById(R.id.llFive);
        }

    }


    class Holder6 {

        ImageView image1;
        ImageView image2;
        ImageView image3;
        LinearLayout llSix;

        public Holder6(View view) {
            image1 = (ImageView) view.findViewById(R.id.image1);
            image2 = (ImageView) view.findViewById(R.id.image2);
            image3 = (ImageView) view.findViewById(R.id.image3);
            llSix = view.findViewById(R.id.llSix);
        }

    }

    class Holder7 {
        LinearLayout itemLayout;
        ImageView shopImg;
        TextView commodityName;
        TextView commodityOldPrice;
        TextView price;
        LinearLayout llBg;


        public Holder7(View view) {
            itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            shopImg = (ImageView) view.findViewById(R.id.commodity_img);
            commodityName = (TextView) view.findViewById(R.id.commodity_name);
            commodityOldPrice = (TextView) view.findViewById(R.id.commodity_old_price);
            price = (TextView) view.findViewById(R.id.commodity_price);
            llBg = view.findViewById(R.id.llBg);
        }

    }

}
