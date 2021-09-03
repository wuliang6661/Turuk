package com.haoyigou.hyg.view.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.utils.DownloadImageToGalleryUtils;
import com.haoyigou.hyg.utils.ProgressUtils;
import com.haoyigou.hyg.utils.ShareUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 * <p>
 * 只分享到微信、朋友圈的弹窗
 */

public class ShareWxPupWindow extends PopupWindow {


    private View mView;
    private ImageView btnwechatbutton, btnwechatfriendbutton;
    private ImageView copyButton;
    private Button btnsharecancle;
    private Activity context;
    private String url;
    private String copyUrl;
    private String[] images;   //图片集合
    private File[] files;

    public ShareWxPupWindow(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.share_wx_pup, null);
        btnwechatbutton = (ImageView) mView.findViewById(R.id.share_wechat_button);
        btnsharecancle = (Button) mView.findViewById(R.id.share_cancle);
        btnwechatfriendbutton = (ImageView) mView.findViewById(R.id.share_wechatfriend_button);
        copyButton = (ImageView) mView.findViewById(R.id.share_copy_button);

        btnsharecancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnwechatbutton.setOnClickListener(listener);
        btnwechatfriendbutton.setOnClickListener(listener);
        copyButton.setOnClickListener(listener);

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
        //实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0x808080);
        //设置SelectPicPopupWindow弹出窗体的背景
        // this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /***
     * 显示时将屏幕置为透明
     */
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.share_wechat_button:    //微信
                    ShareUtils.showShare(context, 1);
                    dismiss();
                    break;
                case R.id.share_wechatfriend_button:   //朋友圈
                    ShareUtils.showShare(context, 2);
                    dismiss();
                    break;
                case R.id.share_copy_button:   //复制链接
                    copyText(url);
                    Toast.makeText(context, "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();
                    dismiss();
                    break;
            }
        }
    };

    /**
     * 设置分享内容
     */
    public void setShareMessage(String title, String logo, String content, String url) {
        this.url = url;
        Log.e("shareTitle", title);
        Log.e("shareContent", content);
        Log.e("shareLogo", logo);
        Log.e("shareUrl", url);
        ShareUtils.setShareMessage(title, content, logo, url);
    }

    /***
     * 设置复制链接
     */
    public void setCopyUrl(String url) {
        this.url = url;
    }


    /***
     * 复制到系统粘贴板的功能
     */
    public void copyText(String text) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(text);
    }

}
