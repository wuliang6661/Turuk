package com.haoyigou.hyg.view.dialog;

/**
 * Created by Administrator on 2018/9/12.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.haoyigou.hyg.R;

import java.util.ArrayList;
import java.util.List;

import meijia.com.srdlibrary.liushibuju.BaseAdapter;
import meijia.com.srdlibrary.liushibuju.TagLayout;


public class MyDialog extends Dialog {

    /**
     * LoadDialog
     */
    private static MyDialog loadDialog;
    /**
     * canNotCancel, the mDialogTextView dimiss or undimiss flag
     */
    private boolean canNotCancel;
    /**
     * if the mDialogTextView don't dimiss, what is the tips.
     */
    private String tipMsg;

    private TextView mShowMessage;

    private Context context;
    TagLayout tag_layout_houseType;
    TextView textview;

    /**
     * the LoadDialog constructor
     *
     * @param ctx          Context
     * @param canNotCancel boolean
     * @param tipMsg       String
     */
    public MyDialog(final Context ctx, boolean canNotCancel, String tipMsg) {
        super(ctx);

        this.canNotCancel = canNotCancel;
        this.tipMsg = tipMsg;
        this.context = ctx;
        this.getContext().setTheme(R.style.Dialog);
        setContentView(R.layout.my_dialog);

        final List<String> dataSource = new ArrayList<>();
        dataSource.add("空调");
        dataSource.add("电视机");
        dataSource.add("洗衣机");
        dataSource.add("热水器");
        dataSource.add("冰箱");
        dataSource.add("可做饭");
        dataSource.add("WIFI");
        dataSource.add("洗衣机");
        dataSource.add("热水器");
        dataSource.add("冰箱");
        dataSource.add("洗衣机");
        dataSource.add("热水器");
        dataSource.add("冰箱");

        tag_layout_houseType = (TagLayout) findViewById(R.id.tag_layout_rent);
        setAdapter(tag_layout_houseType,dataSource);
        tag_layout_houseType.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                tag_layout_houseType.setItemSelecte(postion);
//                tagHouseType = postion;
                Toast.makeText(context,dataSource.get(postion),Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        Window window = getWindow();
        WindowManager.LayoutParams attributesParams = window.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
//        attributesParams.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
//        attributesParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        attributesParams.dimAmount = 2f;
        window.setGravity(Gravity.TOP);
        attributesParams.y = 200;
        window.setAttributes(attributesParams);


        window.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    }

    private void setTextBigSize(TextView textView) {
        String text = textView.getText().toString().trim();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        builder.setSpan(colorSpan, 0, text.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    private void setTextBigSize2(TextView textView) {
        String text = textView.getText().toString().trim();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ffffff"));
        builder.setSpan(colorSpan, 0, text.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canNotCancel) {
//                Toast.showShortToast(getContext(), tipMsg);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setAdapter(TagLayout tagLayout, final List<String> dataSource){

        tagLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return dataSource.size();
            }
            @Override
            public View getView(int position, ViewGroup parent) {
                textview = (TextView) LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
                textview.setText(dataSource.get(position));
                return textview;
            }
        });
    }

    /**
     * show the mDialogTextView
     *
     * @param context
     */
    public static void show(Context context) {
        show(context, null, false);
    }

    /**
     * show the mDialogTextView
     *
     * @param context Context
     * @param message String
     */
    public static void show(Context context, String message) {
        show(context, message, false);
    }

    /**
     * show the mDialogTextView
     *
     * @param context  Context
     * @param message  String, show the message to user when isCancel is true.
     * @param isCancel boolean, true is can't dimiss，false is can dimiss
     */
    private static void show(Context context, String message, boolean isCancel) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (loadDialog != null && loadDialog.isShowing()) {
            return;
        }
        loadDialog = new MyDialog(context, isCancel, message);
        loadDialog.show();
    }

    /**
     * dismiss the mDialogTextView
     */
    public static void dismiss(Context context) {
        try {
            if (context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    loadDialog = null;
                    return;
                }
            }

            if (loadDialog != null && loadDialog.isShowing()) {
                Context loadContext = loadDialog.getContext();
                if (loadContext != null && loadContext instanceof Activity) {
                    if (((Activity) loadContext).isFinishing()) {
                        loadDialog = null;
                        return;
                    }
                }
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadDialog = null;
        }
    }
}
