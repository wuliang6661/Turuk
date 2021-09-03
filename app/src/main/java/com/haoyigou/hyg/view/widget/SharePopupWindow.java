package com.haoyigou.hyg.view.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
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
import com.haoyigou.hyg.ui.ReportActivity;
import com.haoyigou.hyg.utils.DownloadImageToGalleryUtils;
import com.haoyigou.hyg.utils.ProgressUtils;
import com.haoyigou.hyg.utils.ShareUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuliang on 2016/9/18.
 * 分享弹出的弹窗
 */
public class SharePopupWindow extends PopupWindow {

    private View mView;
    private ImageView btnwechatbutton, btnwechatfriendbutton;
    private ImageView qqbutton, qqzoneButton, weiboButton;
    private ImageView copyButton;
    private LinearLayout ImageAndText,reportBtn;
    private Button btnsharecancle;
    private Activity context;
    private String url;
    private String copyUrl;
    private String[] images;   //图片集合
    private File[] files;

    public SharePopupWindow(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.sharepopupwindow, null);
        btnwechatbutton = (ImageView) mView.findViewById(R.id.share_wechat_button);
        btnsharecancle = (Button) mView.findViewById(R.id.share_cancle);
        btnwechatfriendbutton = (ImageView) mView.findViewById(R.id.share_wechatfriend_button);
        qqbutton = (ImageView) mView.findViewById(R.id.share_qq_button);
        qqzoneButton = (ImageView) mView.findViewById(R.id.share_qqzone_button);
        weiboButton = (ImageView) mView.findViewById(R.id.share_sinaweibo_button);
        copyButton = (ImageView) mView.findViewById(R.id.share_copy_button);
        ImageAndText = (LinearLayout) mView.findViewById(R.id.imageandtext);
        reportBtn = (LinearLayout) mView.findViewById(R.id.reportBtn);

        btnsharecancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnwechatbutton.setOnClickListener(listener);
        btnwechatfriendbutton.setOnClickListener(listener);
        qqbutton.setOnClickListener(listener);
        qqzoneButton.setOnClickListener(listener);
        weiboButton.setOnClickListener(listener);
        copyButton.setOnClickListener(listener);
        ImageAndText.setOnClickListener(listener);
        reportBtn.setOnClickListener(listener);

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
        this.setOnDismissListener(new OnDismissListener() {
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
                case R.id.share_qq_button:   //QQ
                    ShareUtils.shareQQ(context);
                    dismiss();
                    break;
                case R.id.share_qqzone_button:// QQ空间
                    ShareUtils.shareQQZone(context);
                    dismiss();
                    break;
                case R.id.share_sinaweibo_button:   //新浪微博
                    ShareUtils.shareWeiBo(context);
                    dismiss();
                    break;
                case R.id.share_copy_button:   //复制链接
                    copyText(url);
                    Toast.makeText(context, "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();
                    dismiss();
                    break;
                case R.id.imageandtext:     //图文分享
                    DownloadImageToGalleryUtils download = new DownloadImageToGalleryUtils(context, images);
                    download.setCommotListener(new DownloadImageToGalleryUtils.onCommotListener() {
                        @Override
                        public void onCommot(File[] files) {
                            ProgressUtils.stopProgressDialog();
                            SharePopupWindow.this.files = files;
                            showDialog();
                        }
                    });
                    download.startService();
                    ProgressUtils.startProgressDialog("", context);
                    copyText(copyUrl);
                    dismiss();
                    break;
                case R.id.reportBtn://举报
                    Intent intent = new Intent(context,ReportActivity.class);
                    if (url != null) {
                        intent.putExtra("id", url.substring(url.indexOf("productIndex/")+13,url.indexOf("productIndex/")+18));
                    }
                    context.startActivity(intent);
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
     * 设置显示图文分享和图文分享数据
     */
    public void setImageAndTextShare(String title, String logo, String content, String url, String memo, String pics) {
        ShareUtils.setShareMessage(title, content, logo, url);
        ImageAndText.setVisibility(View.VISIBLE);
        this.url = url;
        this.copyUrl = memo;
        images = pics.split(",");
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

    /**
     * 显示图文分享完成之后的弹窗
     */
    private void showDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.image_andtext_dialog, null);
        Button share = (Button) view.findViewById(R.id.share_weixin);
        Button cancle = (Button) view.findViewById(R.id.cancle);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {     //打开微信
                if (!isWeixinAvilible(context)) {
                    Toast.makeText(context, "请先安装微信！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                ComponentName comp = new ComponentName("com.tencent.mm",
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                intent.setComponent(comp);
                intent.setAction("android.intent.action.SEND_MULTIPLE");
                intent.setType("image/*");
                intent.putExtra("Kdescription", copyUrl);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                }
                ArrayList<Uri> imageUris = new ArrayList<>();
                for (File f : files) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUris.add(FileProvider.getUriForFile(context, "com.haoyigou.hyg.fileProvider", f));//通过FileProvider创建一个content类型的Uri
                    } else {
                        imageUris.add(Uri.fromFile(f));
                    }
                }
                intent.putExtra(Intent.EXTRA_STREAM, imageUris);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setView(view);
        dialog.show();
        dialog.getWindow().setLayout((int) (GlobalApplication.screen_width / 1.2), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /***
     * 检测微信是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

}
