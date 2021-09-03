package com.haoyigou.hyg.application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.haoyigou.hyg.ui.MessageBoxActivty;
import com.haoyigou.hyg.ui.MessageBoxDetailsAct;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.ui.MainActivity;
import com.haoyigou.hyg.ui.TVLiveActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONObject;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MessageReceiver extends XGPushBaseReceiver {

    public static final String LogTag = "TPushReceiver";

    private void show(Context context, String text) {
//        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        if (context == null || message == null) {
            return;
        }
        String customContent = message.getCustomContent();
        String value = "";
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("pushtype")) {
                    value = obj.getString("pushtype");
                    Log.e("log--消息推送", value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            //判断app进程是否存活
            if (isAppAlive(context, "com.haoyigou.hyg")) {
                Log.e("log--", "app进程存活！");
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if ("5".equals(value)) {
                    Intent liveIntent = new Intent(context, TVLiveActivity.class);
                    Intent[] intents = {mainIntent, liveIntent};
                    context.startActivities(intents);
                } else {
                    Intent boxIntent = new Intent(context, MessageBoxActivty.class);
                    Intent detailIntent = new Intent(context, MessageBoxDetailsAct.class);
                    detailIntent.putExtra("Type", value);
                    Intent[] intents = {mainIntent, boxIntent, detailIntent};
                    context.startActivities(intents);
                }
            } else {
                Log.e("log--", "app进程死亡！");
                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage("com.haoyigou.hyg");
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(launchIntent);
            }
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
        }
    }

    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败，错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);
    }

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        String text = "收到消息:" + message.toString();
        // 获取自定义key-value,设置自定义的角标数
//        String num = message.getContent();
//        Log.d(LogTag, "get custom value:" + num);
//        if (!StringUtils.isEmpty(num)) {
//            ShortcutBadger.applyCount(context, Integer.valueOf(message.getContent()));
//        }
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("badge")) {
                    String value = obj.getString("badge");
                    Log.d(LogTag, "get custom value:" + value);
                    int num = Integer.valueOf(value);
                    StateMessage.badgeNum = num;
                    ShortcutBadger.applyCount(context, num);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // APP自主处理消息的过程...
        Log.d(LogTag, text);
    }

    private void sendIconCountMessage(Context context) {
        Intent it = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
        it.putExtra("android.intent.extra.update_application_component_name", "com.example.wujie.xungetest/.MainActivity");
        String iconCount = "50";
        it.putExtra("android.intent.extra.update_application_message_text", iconCount);
        context.sendBroadcast(it);
    }


    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }


}
