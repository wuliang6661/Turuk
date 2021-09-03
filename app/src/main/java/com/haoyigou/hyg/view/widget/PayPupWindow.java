package com.haoyigou.hyg.view.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.haoyigou.hyg.R;

/**
 * Created by wuliang on 2017/4/7.
 * <p>
 * 付款弹窗
 */

public class PayPupWindow extends PopupWindow implements View.OnClickListener {

    private View mView;
    private Activity context;
    private RelativeLayout wexinLayout;
    private RelativeLayout zhifubaoLayout;
    private Button zhifuButton;
    private CheckBox zhifuCheck;
    private CheckBox weixinCheck;

    private int TYPE = 1;    //默认微信支付为1

    public PayPupWindow(Activity context) {
        super(context);
        this.context = context;
        mView = LayoutInflater.from(context).inflate(R.layout.pay_dialog_layout, null);
        wexinLayout = (RelativeLayout) mView.findViewById(R.id.weixin_layout);
        zhifubaoLayout = (RelativeLayout) mView.findViewById(R.id.zhifubao_layout);
        zhifuButton = (Button) mView.findViewById(R.id.pay_button);
        weixinCheck = (CheckBox) mView.findViewById(R.id.weixin_check);
        zhifuCheck = (CheckBox) mView.findViewById(R.id.zhifubao_check);
        wexinLayout.setOnClickListener(this);
        zhifubaoLayout.setOnClickListener(this);
        zhifuButton.setOnClickListener(this);
        this.setBackgroundDrawable(new ColorDrawable(0));
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.anim_menu_bottombar);
//        实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0x808080);
//        设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    /***
     * 显示时将屏幕置为透明
     */
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin_layout:
                weixinCheck.setChecked(true);
                zhifuCheck.setChecked(false);
                TYPE = 1;
                break;
            case R.id.zhifubao_layout:
                zhifuCheck.setChecked(true);
                weixinCheck.setChecked(false);
                TYPE = 2;
                break;
            case R.id.pay_button:
                if (payButton != null) {
                    payButton.onClick(TYPE);
                }
                break;
        }
    }

    private onPayButton payButton;

    public void setOnPayButton(onPayButton payButton) {
        this.payButton = payButton;
    }


    public interface onPayButton {
        void onClick(int type);
    }

}
