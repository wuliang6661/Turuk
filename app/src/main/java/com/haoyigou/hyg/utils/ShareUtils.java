package com.haoyigou.hyg.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.UserBean;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by wuliang on 2016/11/25.
 * <p>
 * 分享工具类
 */

public class ShareUtils {

    private static String CLASS = Wechat.NAME;   //分享微信
    private static String CLASSALL = WechatMoments.NAME;   //分享朋友圈
    private static UserBean user = GlobalApplication.user;
    private static String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
    private static String url = HttpClient.HTTP_DOMAIN + "/distributor/index/" + disid + ".html";
    private static String QQZoneSiteUrl = "http://www.best1.com/index.php/article-activity-870.html";  //app地址
    private static String QQZoneSite = "好易购商城";

    private static boolean isShareMessage = false;
    private static String shareTitle, shareContent, shareLogo, shareUrl;

    public static void setShareMessage(String title, String content, String logo, String url) {
        isShareMessage = true;
        shareTitle = title;
        shareContent = content;
        shareLogo = logo;
        shareUrl = url;
    }


    /***
     * 微信分享
     *
     * @param i 1为微信，2为朋友圈
     */
    public static void showShare(Context context, int i) {
        Log.e("log--", "分享微信启动！");
        Platform weixin;
        if (i == 1) {
            weixin = ShareSDK.getPlatform(context, CLASS);
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setUrl(isShareMessage ? shareUrl : url);
            sp.setTitle(isShareMessage ? shareTitle : user.getSharetitle());
            sp.setText(isShareMessage ? shareContent : user.getSharetext());
            sp.setImageUrl(isShareMessage ? shareLogo : user.getShareimage());
            weixin.share(sp);
        } else if (i == 2) {
            weixin = ShareSDK.getPlatform(context, CLASSALL);
            WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
            sp.setShareType(Platform.SHARE_WEBPAGE);
            sp.setUrl(isShareMessage ? shareUrl : url);
            sp.setTitle(isShareMessage ? shareTitle : user.getSharetitle());
            sp.setText(isShareMessage ? shareContent : user.getSharetext());
            sp.setImageUrl(isShareMessage ? shareLogo : user.getShareimage());
            weixin.share(sp);
        }
        isShareMessage = false;
    }


    /***
     * 分享到微博
     */
    public static void shareWeiBo(final Context context) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(isShareMessage ? shareContent + "  " + shareUrl : user.getSharetext() + "  " + url);
        sp.setImageUrl(isShareMessage ? shareLogo : user.getShareimage());
        Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(context, "分享失败!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(context, "取消分享!", Toast.LENGTH_SHORT).show();
            }
        }); // 设置分享事件回调
//        weibo.SSOSetting(true);
        // 执行图文分享
        weibo.share(sp);
        isShareMessage = false;
    }


    /**
     * 分享到QQ空间
     */
    public static void shareQQZone(final Context context) {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(isShareMessage ? shareTitle : user.getSharetitle());
        sp.setTitleUrl(isShareMessage ? shareUrl : url);
        sp.setText(isShareMessage ? shareContent : user.getSharetext());
        sp.setImageUrl(isShareMessage ? shareLogo : user.getShareimage());
        sp.setSite(QQZoneSite);
        sp.setSiteUrl(url);
        Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);
//        qzone.setPlatformActionListener(); // 设置分享事件回调
        // 执行图文分享
        qzone.share(sp);
        isShareMessage = false;
    }


    /***
     * 分享到QQ
     */
    public static void shareQQ(Context context) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(isShareMessage ? shareTitle : user.getSharetitle());
        sp.setTitleUrl(isShareMessage ? shareUrl : url);
        sp.setText(isShareMessage ? shareContent : user.getSharetext());
        sp.setImageUrl(isShareMessage ? shareLogo : user.getShareimage());
        Platform qq = ShareSDK.getPlatform(context, QQ.NAME);
        qq.share(sp);
        isShareMessage = false;
    }


}
