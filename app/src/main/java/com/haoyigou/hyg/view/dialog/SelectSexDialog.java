package com.haoyigou.hyg.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/9/13.
 */
public class SelectSexDialog extends Dialog {
    int layoutRes;//布局文件
    Context context;
    public SelectSexDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SelectSexDialog(Context context, int resLayout) {
        super(context, resLayout);
        this.context = context;
        this.layoutRes=resLayout;
    }

    public SelectSexDialog(Context context, boolean cancelable, OnCancelListener cancelListener, Context context1, int layoutRes) {
        super(context, cancelable, cancelListener);
        context = context1;
        this.layoutRes = layoutRes;
    }

    public SelectSexDialog(Context context,  int theme,int resLayout) {
        super( context, resLayout);
        this.context = context;

        this.layoutRes = resLayout;
    }

//    public SelectSexDialog(OnClickListener onClickListener, int theme, int resLayout) {
//        super((Context) onClickListener,theme,resLayout);
//        this.context = context;
//
//        this.layoutRes = resLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);
    }
}
