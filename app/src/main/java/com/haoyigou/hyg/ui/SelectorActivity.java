package com.haoyigou.hyg.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.fragment.SelectorShopFrament;
import com.haoyigou.hyg.fragment.SeletorHistoryFrament;
import com.haoyigou.hyg.base.BaseFragmentActivity;
import com.haoyigou.hyg.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuliang on 2017/3/14.
 * <p>
 * 搜索页面
 */

public class SelectorActivity extends BaseFragmentActivity implements View.OnClickListener {


    @BindView(R.id.finish_act)
    LinearLayout finishAct;
    @BindView(R.id.select_edit)
    EditText selectEdit;
    @BindView(R.id.select_button)
    TextView selectButton;

    SeletorHistoryFrament historyFrament;
    SelectorShopFrament shopFrament;

    String Pfflid;
    String pfflid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_layout);
        ButterKnife.bind(this);
        setsStatusBar();
        finishAct.setOnClickListener(this);
        selectButton.setOnClickListener(this);
        shopFrament = new SelectorShopFrament();
        historyFrament = new SeletorHistoryFrament();
        if (GlobalApplication.user != null) {
            String search = GlobalApplication.user.getSearchdesc();
            if (search != null) {
                selectEdit.setHint(search);
            }
        }
        selectEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                EditText _v=(EditText)v;
                if (!hasFocus) {// 失去焦点
                    _v.setHint(_v.getTag().toString());
                } else {
                    String hint=_v.getHint().toString();
                    _v.setTag(hint);
                    _v.setHint("");
                }
            }
        });
        historyFrament.setOnClick(new SeletorHistoryFrament.OnItemClick() {
            @Override
            public void Callback(String name) {
                shopFrament.setSelectorName(name,1,SelectorActivity.this);
                selectEdit.setText(name);
                switchFragment(shopFrament);
            }
        });
        Pfflid = getIntent().getStringExtra("Pfflid");
        pfflid = getIntent().getStringExtra("pfflid");
        if (StringUtils.isEmpty(Pfflid)) {
            switchFragment(historyFrament);
        } else {
            shopFrament.setClassFiy(Pfflid, pfflid,SelectorActivity.this);
            switchFragment(shopFrament);
        }
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    protected void setsStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish_act:   //返回
                finish();
                break;
            case R.id.select_button:   //搜索
                if (selectEdit.getText() != null) {
                    String edit = selectEdit.getText().toString().trim();
                    String text = StringUtils.isEmpty(edit) ? selectEdit.getHint().toString() : edit;
                    if (StringUtils.isEmpty(text)) {
                        Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
//                    switchFragment(historyFrament);
                    } else {
                        shopFrament.setSelectorName(text, 1, SelectorActivity.this);
                        switchFragment(shopFrament);
                    }
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    }
                }
                break;
        }
    }


    /**
     * 显示指定Fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.select_fragment, fragment);// 将帧布局替换成Fragment
        ft.commit();// 提交
    }
}
