package com.haoyigou.hyg.ui;

import android.os.Bundle;
import android.view.WindowManager;



import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;

/**
 * Created by Administrator on 2018/5/30.
 */
public class HorizontalLiveActivity extends BaseActivity{

    private String lid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_herizontal_live);


    }


}
