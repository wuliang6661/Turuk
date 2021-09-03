package com.haoyigou.hyg.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.haoyigou.hyg.R;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SelectSexPopupWindow extends PopupWindow {
    private Button btn_man,btn_women;
    private View mMenuView;
    private Context context;
    public SelectSexPopupWindow(Activity context, View.OnClickListener itemsOnClick) {

        View inflater = LayoutInflater.from(context).inflate(R.layout.selsect_sex_dialog, null);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.selsect_sex_dialog, null);
        btn_man= (Button) root.findViewById(R.id.tv_selsect_man_button);
        btn_women= (Button) root.findViewById(R.id.tv_selsect_women_button);
        btn_man.setOnClickListener(itemsOnClick);
        btn_women.setOnClickListener(itemsOnClick);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.setContentView(root);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);


//    btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
//    btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
//    btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮

    }
}
