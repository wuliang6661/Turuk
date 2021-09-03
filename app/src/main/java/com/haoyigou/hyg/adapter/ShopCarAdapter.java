package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.ShopCarBean;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.ProgressUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.swipemenu.SwipeMenuLayout;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuliang on 2017/2/15.
 * <p>
 * 购物车适配器
 */

public class ShopCarAdapter extends BaseAdapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private List<ShopCarBean> list;   //全部购物车商品
    private Map<String, ShopCarBean> selectList;    //选中的购物车商品,用它的Id来记录
    private Map<String, Integer> shopnums;    //记录各个商品当前的数量
    private Context context;
    private onClickItemsAll listener;

    private boolean isCheck = true;

    public ShopCarAdapter(Context context, List<ShopCarBean> list) {
        this.context = context;
        this.list = list;
        selectList = new HashMap<>();
        shopnums = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            shopnums.put(list.get(i).getId(), list.get(i).getQuantity());   //初始化所有的商品数量
        }
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void setShopList(List<ShopCarBean> list) {
        this.list = list;
        shopnums = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            shopnums.put(list.get(i).getId(), list.get(i).getQuantity());   //初始化所有的商品数量
        }
        notifyDataSetChanged();
    }

    /**
     * 选择是否全选
     *
     * @param isAll
     */
    public void setAllChecked(boolean isAll) {
        selectList.clear();
        if (isAll) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getQuantity() != 0) {
                    selectList.put(list.get(i).getId(), list.get(i));
                }
            }
        }
        if (listener != null) {
            listener.onDeleteClick(list, selectList, shopnums);
        }
        notifyDataSetChanged();
    }


    public void setOnClickItem(onClickItemsAll listener) {
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_shopcar_layout, null);
            holder = getHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final ShopCarBean bean = (ShopCarBean) getItem(i);
        holder.shopName.setText(bean.getProductname());
        if (StringUtils.isEmpty(bean.getPiclogo())) {
            holder.shopImage.setImageResource(R.drawable.default_image);
        } else {
            Picasso.with(context).load(bean.getPiclogo()).into(holder.shopImage);
        }
        if (StringUtils.isEmpty(bean.getColorname()) || bean.getColorname().equals("共同")) {
            holder.shopColor.setVisibility(View.GONE);
        } else {
            holder.shopColor.setVisibility(View.VISIBLE);
            holder.shopColor.setText(bean.getColorname());
        }
        if (StringUtils.isEmpty(bean.getSizename())|| bean.getSizename().equals("共同")) {
            holder.shopSize.setVisibility(View.GONE);
        } else {
            holder.shopSize.setVisibility(View.VISIBLE);
            holder.shopSize.setText( bean.getSizename());
        }
//        DecimalFormat df = new DecimalFormat("#.00");
        if (shopnums.get(bean.getId()) != null) {
            String price = String.format("%.2f", shopnums.get(bean.getId()) * bean.getRightprice());
            holder.shopPrice.setText("¥" + price);
            holder.shopNum.setText(shopnums.get(bean.getId()) + "");
        }
        isCheck = false;
        if (!StringUtils.isEmpty(bean.getIschecked()) && "1".equals(bean.getIschecked())) {
            holder.itemCheck.setChecked(true);
            selectList.put(bean.getId(), bean);
            if (listener != null) {
                listener.onCheckClick(selectList, shopnums);
                bean.setIschecked("0");
            }
        } else {
            holder.itemCheck.setChecked(false);
        }
        if (selectList.get(bean.getId()) != null) {
            holder.itemCheck.setChecked(true);
        } else {
            holder.itemCheck.setChecked(false);
        }
        isCheck = true;
        holder.itemCheck.setTag(bean);
        holder.itemCheck.setOnCheckedChangeListener(this);
        holder.btnDelete.setTag(bean);
        holder.btnDelete.setOnClickListener(this);
        holder.shopAdd.setTag(bean);
        holder.shopAdd.setOnClickListener(this);
        holder.shopCut.setTag(bean);
        holder.shopCut.setOnClickListener(this);
        holder.itemShopCar.setTag(bean);
        holder.itemShopCar.setOnClickListener(this);
        holder.checkLayout.setTag(bean);
        holder.checkLayout.setOnClickListener(this);
        return view;
    }

    /**
     * 处理各类点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        ShopCarBean bean = (ShopCarBean) view.getTag();
        switch (view.getId()) {
            case R.id.btnDelete:   //删除
                ProgressUtils.startProgressDialog("", context);
                deleteItems(new String[]{bean.getId()});
                //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
                ((SwipeMenuLayout) view.getParent()).quickClose();
                break;
            case R.id.shop_add:   //增加数量
                if (bean.getQuantity() == 0) {
                    Toast.makeText(context, "本商品库存只剩0件", Toast.LENGTH_SHORT).show();
                    return;
                }
                int maxnum;
                if (bean.getMaxnum() == 0) {
                    maxnum = bean.getStore();
                } else {
                    maxnum = bean.getMaxnum() < bean.getStore() ? bean.getMaxnum() : bean.getStore();  //获取最大增加数量
                }
                if (shopnums.get(bean.getId()) != null) {
                    int num = shopnums.get(bean.getId());
                    if (num < maxnum) {
                        num++;
                        ProgressUtils.startProgressDialog("", context);
                        addAndCutItems(bean, num);
                    } else if (bean.getMaxnum() == 0 && maxnum >= num) {
                        Toast.makeText(context, "本商品库存只剩" + maxnum + "件", Toast.LENGTH_SHORT).show();
                    } else if (bean.getMaxnum() != 0 && maxnum >= num) {
                        Toast.makeText(context, "本商品限购" + maxnum + "件", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.shop_cut:   //减少数量
                if (shopnums.get(bean.getId()) != null) {
                    int cutnum = shopnums.get(bean.getId());
                    if (cutnum > 1) {
                        cutnum--;
                        ProgressUtils.startProgressDialog("", context);
                        addAndCutItems(bean, cutnum);
                    }
                }
                break;
            case R.id.item_shopcar:
                Intent intent = new Intent(context, HomeWebViewAct.class);
                String disId = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
                intent.putExtra("url", HttpClient.GOODWEBVIEW + "?distributorId=" + disId
                        + "&showplat=1&accessType=1&productid=" + bean.getProductid());
//                StateMessage.isShopCarFinnish = true;
//                StateMessage.url = intent.getStringExtra("url");
                context.startActivity(intent);
                break;
            case R.id.radio_layout:
                if (bean.getQuantity() == 0) {
                    return;
                }
                CheckBox box = (CheckBox) ((RelativeLayout) view.getParent()).findViewById(R.id.item_radio);
                if (box.isChecked()) {
                    box.setChecked(false);
                } else {
                    box.setChecked(true);
                }
                break;
        }
    }

    /**
     * 处理选择框事件
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!isCheck) {
            return;
        }
        ShopCarBean bean = (ShopCarBean) compoundButton.getTag();
        if (b) {
            selectList.put(bean.getId(), bean);
        } else {
            selectList.remove(bean.getId());
        }
        if (listener != null) {
            listener.onCheckClick(selectList, shopnums);
        }
    }


    /**
     * 删除商品
     */
    public void deleteItems(final String[] items) {
        Map<String, Object> params = new HashMap<>();
        String itemIds = "";
        for (int i = 0; i < items.length; i++) {
            if (i == items.length - 1) {   //最后一个
                itemIds += items[i];
            } else {
                itemIds += items[i] + ",";
            }
        }
        params.put("Itemids", itemIds);
        HttpClient.post(HttpClient.DELCARTS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Log.e("log--del", content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    for (int a = 0; a < items.length; a++) {
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).getId().equals(items[a])) {
                                list.remove(j);
                                shopnums.remove(items[a]);
                                selectList.remove(items[a]);
                            }
                        }
                    }
                    if (listener != null) {
                        listener.onDeleteClick(list, selectList, shopnums);
                    }
                    ProgressUtils.stopProgressDialog();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, context);
    }


    /**
     * 增加减少商品数量
     * <p>
     * num: 改变后的商品数量
     */
    private void addAndCutItems(final ShopCarBean bean, final int num) {
        Map<String, Object> params = new HashMap<>();
        params.put("itemid", bean.getId());
        params.put("Num", num + "");
        HttpClient.post(HttpClient.CHANGESHOP, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Log.e("log--chageShop", content);
                JSONObject object = JSON.parseObject(content);
                ProgressUtils.stopProgressDialog();
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    shopnums.put(bean.getId(), num);
                    if (listener != null) {
                        listener.onChangeNumClick(shopnums);
                    }
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        }, context);
    }


    /**
     * 初始化控件
     */
    private ViewHolder getHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.btnDelete = (Button) view.findViewById(R.id.btnDelete);
        holder.itemCheck = (CheckBox) view.findViewById(R.id.item_radio);
        holder.shopImage = (ImageView) view.findViewById(R.id.item_shop_img);
        holder.shopName = (TextView) view.findViewById(R.id.item_shop_name);
        holder.shopColor = (TextView) view.findViewById(R.id.item_shop_content);
        holder.shopSize = (TextView) view.findViewById(R.id.item_shop_content01);
        holder.shopAdd = (ImageButton) view.findViewById(R.id.shop_add);
        holder.shopCut = (ImageButton) view.findViewById(R.id.shop_cut);
        holder.shopNum = (TextView) view.findViewById(R.id.shop_num);
        holder.shopPrice = (TextView) view.findViewById(R.id.shop_price);
        holder.itemShopCar = (RelativeLayout) view.findViewById(R.id.item_shopcar);
        holder.checkLayout = (LinearLayout) view.findViewById(R.id.radio_layout);
        return holder;
    }

    class ViewHolder {
        RelativeLayout itemShopCar;
        ImageView shopImage;
        TextView shopName;
        LinearLayout checkLayout;
        CheckBox itemCheck;
        TextView shopColor;
        TextView shopSize;
        TextView shopPrice;
        TextView shopNum;
        ImageView shopAdd;
        ImageView shopCut;
        Button btnDelete;
    }

    public interface onClickItemsAll {

        /**
         * 改变商品数量
         */
        void onChangeNumClick(Map<String, Integer> shopnums);

        /**
         * 点击选择框
         * <p>
         * select: 选中的商品集合
         * nums ： 记录各个商品数量的集合
         */
        void onCheckClick(Map<String, ShopCarBean> select, Map<String, Integer> nums);

        /**
         * 删除item
         */
        void onDeleteClick(List<ShopCarBean> allShop, Map<String, ShopCarBean> newShop, Map<String, Integer> nums);
    }

}
