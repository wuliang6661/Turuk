package com.haoyigou.hyg.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.common.http.PersistentCookieStore;
import com.haoyigou.hyg.entity.Constants;
import com.haoyigou.hyg.entity.SavePersonalInfomationEntity;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.utils.DataCleanManager;
import com.haoyigou.hyg.utils.HttpUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.utils.Util;
import com.haoyigou.hyg.view.circlephoto.AvatarImageView;
import com.haoyigou.hyg.base.BaseFragmentActivity;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * 点击设置进入的窗体 个人信息设置页面
 */


public class SettingActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView iv_setting_backto;
    private ImageView iv_arrrow2;
    private Button logoutbutton;//退出按钮
    private TextView tv_nickname;//昵称名字
    private TextView tv_select_birthday;//选择生日
    private GlobalApplication mApplication = null;
    private TextView tv_myaddress;
    private TextView tv_clear;
    private TextView tv_selectsex;
    private LinearLayout linear_head;
    private LinearLayout linear_nickname;
    private LinearLayout linear_sex;
    private LinearLayout linear_birthday;
    private LinearLayout linear_address;
    private LinearLayout linear_clear;
    private RelativeLayout linear_setting;
    private TextView version;   //版本号
    private Button setButton;
    private Switch mesBtn;
    private LinearLayout changeBtn;

    private String sex;
    private AvatarImageView headpict;
    private String headpic;

    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> context = null;
    private LinearLayout userprotocol;
    private LinearLayout privacystatement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);
        mApplication = GlobalApplication.getInstance();
        //将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        mApplication.pushTask(context);
        initview();
        initevent();
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        boolean isOpened = manager.areNotificationsEnabled();
        if (isOpened) {
            mesBtn.setChecked(true);
        }else {
            mesBtn.setChecked(false);
        }
    }


    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.user_protocol):
                Intent intent = new Intent(SettingActivity.this, userWebviewActivity.class);
                intent.putExtra("url", "/userAgreement");
                intent.putExtra("title", "用户协议");
                startActivity(intent);
                break;
            case (R.id.privacy_statement):
                Intent intent1 = new Intent(SettingActivity.this, userWebviewActivity.class);
                intent1.putExtra("url", "/privacyNotice");
                intent1.putExtra("title", "隐私声明");
                startActivity(intent1);
                break;
            case (R.id.setting_select_sex):
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("退出")
                        .setMessage("请选择性别")
                        .setPositiveButton("男", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("女", null)
                        .show();
                break;
            case (R.id.setting_backto):
                finish();
                break;
            case (R.id.logout_button):
                SharedPreferencesUtils.getInstance().remove("token");
                SharedPreferencesUtils.getInstance().remove("jsessionid");
                CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(SettingActivity.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                (new PersistentCookieStore(SettingActivity.this.getApplicationContext())).removeAll();
                new ExitThread().start();
                JPushInterface.deleteAlias(getApplicationContext(),111);
//                Set<String> tags = new HashSet<>();
//                tags.add(GlobalApplication);
//                JPushInterface.deleteTags(getApplicationContext(),222,tags);
                break;
            case (R.id.setting_nickname):
                Intent intent2 = new Intent(SettingActivity.this, ChangeNickNameActivity.class);
                startActivity(intent2);
                break;
            case (R.id.setting_arrow2):
                Intent intent3 = new Intent(SettingActivity.this, ChangeNickNameActivity.class);
                startActivity(intent3);
                break;
            case (R.id.tv_myaddress):
                Intent intent5 = new Intent(SettingActivity.this, ChangeMyAddressWebviewActivity.class);
                startActivity(intent5);
                break;
            case (R.id.tv_clear):
                clean();
                break;
            case (R.id.lineralayout_changenickname):
                Intent intent6 = new Intent(SettingActivity.this, ChangeNickNameActivity.class);
                startActivity(intent6);
                break;
            case (R.id.linear_sex):
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("设置")
                        .setMessage("请选择性别")
                        .setPositiveButton("男", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_selectsex.setText("男");
                                sex = "1";
                                selectsex();

                            }
                        })
                        .setNegativeButton("女", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_selectsex.setText("女");
                                sex = "2";
                                selectsex();

                            }
                        })
                        .show();
                break;
            case R.id.linear_address:
                Intent intent7 = new Intent(SettingActivity.this, ChangeMyAddressWebviewActivity.class);
                startActivity(intent7);
                break;
            case R.id.linear_clear:
                clean();
                break;
            case R.id.linear_setting:
                finish();
                break;
            case R.id.linear_birthday:
                tv_select_birthday = (TextView) findViewById(R.id.tv_selsct_birthday);
                final Calendar c1 = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(SettingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c1.set(year, monthOfYear, dayOfMonth);
                        Calendar calendar = Calendar.getInstance();
                        if (c1.after(calendar)) {
                            Toast.makeText(SettingActivity.this, "生日不能超过当前日期", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        tv_select_birthday.setText(DateFormat.format("yyy-MM-dd", c1));
                        birthday();
                        Toast.makeText(SettingActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }
                }, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
        }
    }

    private void initevent() {
//       点击选择性别
        tv_selectsex.setOnClickListener(this);
        //设置的回退键
        iv_setting_backto.setOnClickListener(this);
        logoutbutton.setOnClickListener(this);
        tv_nickname.setOnClickListener(this);
        iv_arrrow2.setOnClickListener(this);
        tv_myaddress.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        linear_nickname.setOnClickListener(this);
        linear_sex.setOnClickListener(this);
        linear_address.setOnClickListener(this);
        linear_clear.setOnClickListener(this);
        //设置的返回按钮
        linear_setting.setOnClickListener(this);
        linear_birthday.setOnClickListener(this);
        privacystatement.setOnClickListener(this);
        userprotocol.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    AlertView malertView;
    private void initview() {
        linear_birthday = (LinearLayout) findViewById(R.id.linear_birthday);
        headpict = (AvatarImageView) findViewById(R.id.settting_headpic);
        headpict.setContext(this);
        GlobalApplication application = (GlobalApplication) this.getApplicationContext();
        String nickname = application.getF();
        linear_setting = (RelativeLayout) findViewById(R.id.linear_setting);
        linear_address = (LinearLayout) findViewById(R.id.linear_address);
        linear_birthday = (LinearLayout) findViewById(R.id.linear_birthday);
        linear_clear = (LinearLayout) findViewById(R.id.linear_clear);
        linear_head = (LinearLayout) findViewById(R.id.linera_head);
        linear_sex = (LinearLayout) findViewById(R.id.linear_sex);
        linear_nickname = (LinearLayout) findViewById(R.id.lineralayout_changenickname);
        tv_selectsex = (TextView) findViewById(R.id.setting_select_sex);
        tv_select_birthday = (TextView) findViewById(R.id.tv_selsct_birthday);
        iv_arrrow2 = (ImageView) findViewById(R.id.setting_arrow2);
        tv_nickname = (TextView) findViewById(R.id.setting_nickname);
        logoutbutton = (Button) findViewById(R.id.logout_button);
        iv_setting_backto = (ImageView) findViewById(R.id.setting_backto);
        setButton = (Button) findViewById(R.id.setButton);
        headpict.setFragmentManager(getSupportFragmentManager());
        mesBtn = (Switch)findViewById(R.id.mesBtn);
        changeBtn = (LinearLayout) findViewById(R.id.changeBtn);
        headpict.setAfterCropListener(new AvatarImageView.AfterCropListener() {
            @Override
            public void afterCrop(Bitmap photo, String url) {
                uploadUserImage(url);
            }
        });
        tv_myaddress = (TextView) findViewById(R.id.tv_myaddress);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        version = (TextView) findViewById(R.id.version);
        userprotocol = (LinearLayout) findViewById(R.id.user_protocol);

        privacystatement = (LinearLayout) findViewById(R.id.privacy_statement);

        version.setText("V " + Util.getAppVersionName(this));
        if (nickname != null) {
            tv_nickname.setText(application.getF());
        }
        try {
            String v = DataCleanManager.getTotalCacheSize(SettingActivity.this);
            tv_clear.setText(v);
        } catch (Exception e) {

        }

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//注销
                malertView = new AlertView("注意", "确定注销该账户以及清空全部信息吗", null,
                        new String[]{"确定", "取消"}, null, SettingActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
//                            malertView.dismiss();
                            //注销操作
                            logOff();
                        }else {
//                            malertView.dismiss();
                        }
                    }
                });
                malertView.show();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNotificationSetting(SettingActivity.this);
            }
        });
    }

    public void gotoNotificationSetting(Activity activity) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, 100);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                activity.startActivityForResult(intent, 100);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, 100);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, 100);
        }
    }



    //上传头像
    private void uploadUserImage(final String localPath) {
        SavePersonalInfomationEntity param = new SavePersonalInfomationEntity();
        param.setRelative_ur(localPath);
        HttpClient.uploadUserImage(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getUSERIMG", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String nowpic = object.getString("nowpic");
                    if (nowpic != null) {
//                        Picasso.with(SettingActivity.this).load(BaseAdapterHelper.getURLWithSize(nowpic))
//                                //.placeholder(R.drawable.user_logo)
//                                // .error(R.drawable.user_logo)
//                                .tag(this)
//                                .into(avatarImageView);
                    }
                    Toast.makeText(SettingActivity.this, "设置新的头像成功", Toast.LENGTH_SHORT).show();
                    loaduserinfo();
                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                    Toast.makeText(SettingActivity.this, "设置新的头像失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(SettingActivity.this, request.toString(), Toast.LENGTH_LONG).show();
            }
        }, SettingActivity.this);

    }

    //选择性别
    private void selectsex() {
        //String selectsex=niceSpinner.getText().toString();
        //Log.e("EEE",selectsex);
        SavePersonalInfomationEntity param = new SavePersonalInfomationEntity();
        param.setSaveType("4");
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        param.setSex(sex);
        HttpClient.changesex(param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String body) {
//                Log.e("getMobileCode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {

                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, SettingActivity.this);
    }

    //上传生日
    private void birthday() {
        SavePersonalInfomationEntity param = new SavePersonalInfomationEntity();
        param.setSaveType("5");
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        param.setBirthday(tv_select_birthday.getText().toString());
        HttpClient.changebirthday(param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String body) {
//                Log.e("getMobileCode", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {

                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {

                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        }, SettingActivity.this);
    }

    /**
     * 清理缓存
     */
    public void clean() {
        new AlertDialog.Builder(SettingActivity.this)
               /* .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("退出")*/
                .setMessage("确定清除缓存吗?")
                .setPositiveButton("马上清理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        try {
                            String v = DataCleanManager.getTotalCacheSize(SettingActivity.this);
                            tv_clear.setText(v);
                        } catch (Exception e) {

                        }

                        Toast.makeText(SettingActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("暂不清除", null)
                .show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (headpict != null) {
            headpict.onActivityResult(requestCode, resultCode, intent);
        }
        if (requestCode == 100){
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            boolean isOpened = manager.areNotificationsEnabled();
            if (isOpened) {
                mesBtn.setChecked(true);
            }else {
                mesBtn.setChecked(false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AvatarImageView.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }


    class ExitThread extends Thread {
        @Override
        public void run() {
            try {
                HttpUtils._supermember_token = null;
                HttpUtils.islogin = false;
                HttpUtils.JSESSIONID = null;
                SharedPreferencesUtils.getInstance().remove("token");
                SharedPreferencesUtils.getInstance().remove("jsessionid");
                SharedPreferencesUtils.getInstance().putString("distributorId", "1");
                CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(SettingActivity.this.getApplicationContext());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                cookieManager.removeAllCookie();
                cookieSyncMngr.stopSync();
                HttpClient.cookieStore.removeAll();
                handler.sendEmptyMessage(1);
            } catch (Exception e) {

                e.printStackTrace();
            }
            super.run();
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DataCleanManager.clearAllCache(SettingActivity.this);
                    Intent intent = new Intent();
                    intent.setClass(SettingActivity.this, LoginActivity.class);
//                    intent.putExtra("addFinish", true);
                    SettingActivity.this.startActivity(intent);
                    SettingActivity.this.finish();
                    Constants.mainActivity.finish();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loaduserinfo();
        Log.e("TAGG", "123");
    }

    /**
     * 加载个人信息 获取头像合伙人ID等信息回填到个人设置里面
     */
    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        HttpClient.appsettinguserinfo(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getMobileCodeuser", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if ("1".equals(object.getString("status"))) {
                    String nicjname = object.getString("nickname");
                    if (nicjname != null) {
                        tv_nickname.setText(nicjname);
                    }
                    headpic = object.getString("pic");
                    String birthday = object.getString("birthday");
                    if (birthday != null) {
                        String newbirthday = birthday.substring(0, 10);
                        tv_select_birthday.setText(newbirthday);
                    }
                    String sex = object.getString("sex");
                    if (sex != null && sex.equals("1")) {
                        tv_selectsex.setText("男");
                    } else if (sex != null && sex.equals("2")) {
                        tv_selectsex.setText("女");
                    }
                    if (StringUtils.isEmpty(headpic)) {
                        SettingActivity.this.headpict.setImageResource(R.drawable.logo_tubiao);
                    } else {
                        Picasso.with(SettingActivity.this).load(headpic).into(SettingActivity.this.headpict);
                    }
                }
            }

            public void onFailure(Request request, IOException e) {
            }
        }, SettingActivity.this);
    }



    /**
     * 注销
     */
    private void logOff() {
        final Map<String, Object> params = new HashMap<>();
        HttpClient.post(HttpClient.LOGOFF, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
//                showToast("注销成功");
                showQuitDialog();
            }
        }, this);
    }


    private void showQuitDialog() {
        LayoutInflater factory = LayoutInflater.from(this);//提示框
        final View view = factory.inflate(R.layout.hint_dialog_layout, null);//这里必须是final的
        TextView cancle = (TextView) view.findViewById(R.id.off_commit);
        TextView commit = (TextView) view.findViewById(R.id.commit);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView content = (TextView) view.findViewById(R.id.content);
        title.setText("注销申请已提交");
        content.setText("请等待审核");
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        cancle.setOnClickListener(view1 -> dialog.dismiss());
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //事件
                //退出操作
                dialog.dismiss();
                SharedPreferencesUtils.getInstance().remove("token");
                SharedPreferencesUtils.getInstance().remove("jsessionid");
                CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(SettingActivity.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                (new PersistentCookieStore(SettingActivity.this.getApplicationContext())).removeAll();
                new ExitThread().start();
                finish();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
}
