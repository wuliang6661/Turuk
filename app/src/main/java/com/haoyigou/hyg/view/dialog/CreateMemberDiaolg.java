package com.haoyigou.hyg.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.CreateMemberEntity;
import com.haoyigou.hyg.entity.UserExperienceIdentifyCodeEntity;
import com.haoyigou.hyg.utils.RegUtils;
import com.haoyigou.hyg.view.widget.TimeButton;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * 新建好易购会员弹窗
 * Created by Wuliang on 2016/10/26
 */
public class CreateMemberDiaolg {

    private Context context;
    private Dialog dialog;
    private LayoutInflater inflater;
    private View dialogView;

    private int widt;

    private onBindingListener listener;

    //弹窗里面的控件
    private EditText create_member_name;
    private EditText create_member_number;
    private EditText create_member_code;
    private EditText create_invite_number;
    private Button create_member_sure_button;
    private TextView create_member_cancle_button;
    private TimeButton create_member_send_code;


    public CreateMemberDiaolg(Context context) {
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        widt = wm.getDefaultDisplay().getWidth() - 100;
        inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.dialog_create_member, null);
        dialog = new Dialog(context, R.style.MyInputDialog);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = widt;
        initnotion();
        setListener();
        dialog.setContentView(dialogView);
    }

    /***
     * 初始化弹窗控件
     */
    private void initnotion() {
        create_member_sure_button = (Button) dialogView.findViewById(R.id.create_member_sure_button);
        create_member_cancle_button = (TextView) dialogView.findViewById(R.id.create_member_cancle_button);
        create_member_name = (EditText) dialogView.findViewById(R.id.create_member_name);
        create_member_number = (EditText) dialogView.findViewById(R.id.create_member_number);
        create_member_code = (EditText) dialogView.findViewById(R.id.create_member_code);
        create_member_send_code = (TimeButton) dialogView.findViewById(R.id.create_member_sendcode);
        create_invite_number = (EditText) dialogView.findViewById(R.id.create_invite_number);
        String interted = GlobalApplication.user.getInvitation();
        if (interted != null) {
            if (!"1".equals(interted)) {   //显示邀请码
                create_invite_number.setVisibility(View.GONE);
            }
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {    //回调通知弹窗消失
                    listener.isBinding(false);
                }
            }
        });
    }

    public void setOnBinding(onBindingListener listener) {
        this.listener = listener;
    }


    /***
     * 调用此方法显示弹窗
     */
    public void showDialog() {
        dialog.show();
    }

    /***
     * 调用此方法销毁弹窗
     */
    public void dismissDialog() {
        dialog.dismiss();
    }


    private void setListener() {
        create_member_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (create_member_number.length() == 0) {
                    Toast.makeText(context, "手机号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (create_member_number.length() < 11) {
                    Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (!RegUtils.isMobileNumber(create_member_number.getText().toString())) {
//                    Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                create_member_send_code.startTimer();
                Toast.makeText(context, "发送验证码成功", Toast.LENGTH_SHORT).show();
                getVersition();

            }
        });
        create_member_sure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMemberEntity param = new CreateMemberEntity();
                param.setMunbertype("1005");
                param.setCusname(create_member_name.getText().toString());
                param.setCusmunber(create_member_number.getText().toString());
                param.setPhoneCode(create_member_code.getText().toString());
                param.setInviteNumber(create_invite_number.getText().toString());
                Log.e("TAG", create_member_name.getText().toString());
                HttpClient.creatmember(param, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(String body) {
                        Log.e("getMobileCode", "返回数据:" + body);
                        JSONObject object = JSON.parseObject(body);
                        if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                            Toast.makeText(context, "绑定好易购会员成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (listener != null) {    //回调通知绑定成功
                                listener.isBinding(true);
                            }
                        } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                            Toast.makeText(context, "绑定失败", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }
                }, context);
            }
        });
        create_member_cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 获取验证码
     */
    private void getVersition() {
        UserExperienceIdentifyCodeEntity param = new UserExperienceIdentifyCodeEntity();
        param.setPhone(create_member_number.getText().toString());
        Log.e("DDD", create_member_send_code.getText().toString());
        param.setCodetype("3");

        HttpClient.sendMobileCode(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
                Log.e("getMobileCode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    JSONObject data = JSON.parseObject(object.getString("data"));
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, context);
    }


    /**
     * 创建好易购会员
     */

    private void createmember() {
        CreateMemberEntity param = new CreateMemberEntity();
        param.setCusname(create_member_name.getText().toString());
        param.setCusmunber(create_member_number.getText().toString());
        param.setPhoneCode(create_member_code.getText().toString());
        Log.e("TAG", create_member_name.getText().toString());
        HttpClient.creatmember(param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String body) {
                Log.e("getCreateMemberCode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    Toast.makeText(context, "绑定好易购会员成功", Toast.LENGTH_SHORT).show();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                    Toast.makeText(context, "绑定失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, context);
    }


    public interface onBindingListener {
        void isBinding(boolean isBinding);
    }
}
