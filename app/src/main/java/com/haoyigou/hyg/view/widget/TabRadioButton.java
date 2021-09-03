package com.haoyigou.hyg.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import com.haoyigou.hyg.R;


/**
 * Created by Administrator on 13-10-21.
 */
public class TabRadioButton extends RadioButton implements OnCheckedChangeListener {

    private int mBtnNor, mBtnSel;
    private Context mContext;

    public TabRadioButton(Context context) {
        super(context);
    }

    public TabRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabRadioButton);
        mBtnNor = typedArray.getResourceId(R.styleable.TabRadioButton_mBtnNor, R.drawable.tab_invitation_nor);
        mBtnSel = typedArray.getResourceId(R.styleable.TabRadioButton_mBtnSel, R.drawable.tab_invitation_sel);
        this.mContext = context;
        this.setOnCheckedChangeListener(this);
        Drawable btnSelDrawable = this.getResources().getDrawable(mBtnSel);
        this.setCompoundDrawablesWithIntrinsicBounds(null, btnSelDrawable, null, null);
    }

    public int getmBtnNor() {
        return mBtnNor;
    }

    public void setmBtnNor(int mBtnNor) {
        this.mBtnNor = mBtnNor;
    }

    public int getmBtnSel() {
        return mBtnSel;
    }

    public void setmBtnSel(int mBtnSel) {
        this.mBtnSel = mBtnSel;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            Drawable btnNorDrawable = this.getResources().getDrawable(mBtnNor);
            compoundButton.setCompoundDrawablesWithIntrinsicBounds(null, btnNorDrawable, null, null);
        }else{
            Drawable btnSelDrawable = this.getResources().getDrawable(mBtnSel);
            compoundButton.setCompoundDrawablesWithIntrinsicBounds(null, btnSelDrawable, null, null);
        }
    }
}
