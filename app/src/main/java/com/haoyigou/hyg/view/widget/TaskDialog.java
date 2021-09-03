package com.haoyigou.hyg.view.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuliang on 2016/12/9.
 * <p>
 * 新手任务的弹窗
 */

public class TaskDialog {

    private Context context;
    private AlertDialog dialog;

    private String sign;   //签到获得金币数
    private String reading;  //阅读获得金币数
    private String buy;     //首次体验获得金币
    private String recommend;   //首次推荐获得金币

    private RelativeLayout signLayout;
    private RelativeLayout indexLayout;
    private RelativeLayout experienceLayout;
    private RelativeLayout recommendLayout;
    private TextView signText;
    private TextView indexText;
    private TextView experienceText;
    private TextView recommendText;
    private ImageView signComment;
    private ImageView indexComment;
    private ImageView experienceComment;
    private ImageView recommendComment;
    private ImageView deleteImage;
    /**
     * 默认任务都没完成
     */
    private boolean isSign = false;
    private boolean isReading = false;
    private boolean isBuy = false;
    private boolean isRecommend = false;

    public TaskDialog(Context context) {
        this.context = context;
    }


    /***
     * 显示弹窗
     */
    public void showDialog() {
        getDialogMessage();
    }


    private void getDialogMessage() {
        Map<String, Object> params = new HashMap<>();
        String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "1");
        params.put("distributorId", disid);
        HttpClient.post(HttpClient.GETTASKMESSAGE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Log.e("log--task", content);
                if (content == null || content.equals("")) {
                    Toast.makeText(context, "新手任务获取失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object = JSON.parseObject(content);
                if (object.getString("status").equals(HttpClient.RET_SUCCESS_CODE)) {
                    sign = object.getString("sign");
                    reading = object.getString("reading");
                    buy = object.getString("buy");
                    recommend = object.getString("recommend");
                    if (object.getString("hassign").equals("1")) {
                        isSign = true;
                    }
                    if (object.getString("hasreading").equals("1")) {
                        isReading = true;
                    }
                    if (object.getString("hasbuy").equals("1")) {
                        isBuy = true;
                    }
                    if (object.getString("hasrecommend").equals("1")) {
                        isRecommend = true;
                    }
                    inviDialog();
                }
            }
        }, context);
    }


    /***
     * 初始化弹窗
     */
    private void inviDialog() {
        dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.news_tasks_layout, null);
        signText = (TextView) view.findViewById(R.id.sign_text);
        indexText = (TextView) view.findViewById(R.id.index_text);
        experienceText = (TextView) view.findViewById(R.id.experience_text);
        recommendText = (TextView) view.findViewById(R.id.recommend_text);
        signLayout = (RelativeLayout) view.findViewById(R.id.sign_layout);
        indexLayout = (RelativeLayout) view.findViewById(R.id.index_layout);
        experienceLayout = (RelativeLayout) view.findViewById(R.id.experience_layout);
        recommendLayout = (RelativeLayout) view.findViewById(R.id.recommend_layout);
        signComment = (ImageView) view.findViewById(R.id.sign_comple);
        indexComment = (ImageView) view.findViewById(R.id.index_comple);
        experienceComment = (ImageView) view.findViewById(R.id.experience_comple);
        recommendComment = (ImageView) view.findViewById(R.id.recommend_comple);
        deleteImage = (ImageView) view.findViewById(R.id.delete_image);
        signLayout.setOnClickListener(listener);
        indexLayout.setOnClickListener(listener);
        experienceLayout.setOnClickListener(listener);
        recommendLayout.setOnClickListener(listener);
        deleteImage.setOnClickListener(listener);
        setViewVisitable(signComment, isSign);
        setViewVisitable(indexComment, isReading);
        setViewVisitable(experienceComment, isBuy);
        setViewVisitable(recommendComment, isRecommend);
        signText.setText("首次签到获得" + sign + "金币");
        indexText.setText("完成一次阅读任务获取" + reading + "金币");
        experienceText.setText("首次购买商品获得" + buy + "金币");
        recommendText.setText("招募合伙人获取" + recommend + "金币");
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getWindow().setLayout(GlobalApplication.screen_width - 100, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setViewVisitable(ImageView image, boolean isVisable) {
        if (isVisable) {
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.GONE);
        }
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, PersonWebViewAct.class);
            String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
            switch (view.getId()) {
                case R.id.sign_layout:
                    if (isSign) {
                        Toast.makeText(context, "任务已完成！", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        EventBus.getDefault().post(Long.valueOf(0x11));
                    }
                    break;
                case R.id.index_layout:
                    if (isReading) {
                        Toast.makeText(context, "任务已完成！", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        intent.putExtra("url", HttpClient.TASKINDEX + "?distributorId=" + disid);
                        context.startActivity(intent);
                    }
                    break;
                case R.id.experience_layout:
                    if (isBuy) {
                        Toast.makeText(context, "任务已完成！", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        EventBus.getDefault().post(Boolean.valueOf(true));
                    }
                    break;
                case R.id.recommend_layout:
                    if (isRecommend) {
                        Toast.makeText(context, "任务已完成！", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        intent.putExtra("url", HttpClient.RECOMMEND + "?distributorId=" + disid);
                        context.startActivity(intent);
                    }
                    break;
                case R.id.delete_image:
                    dialog.dismiss();
                    break;
            }
        }
    };
}
