package com.haoyigou.hyg.view.ninepic;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.shehuan.niv.NiceImageView;

import java.util.List;

/**
 * @author KCrason
 * @date 2018/4/27
 */
public class NineImageAdapter implements NineGridView.NineGridAdapter<String> {

    private List<String> mImageBeans;

    private Context mContext;


    public NineImageAdapter(Context context, List<String> imageBeans) {
        this.mContext = context;
        this.mImageBeans = imageBeans;
    }

    @Override
    public int getCount() {
        return mImageBeans == null ? 0 : mImageBeans.size();
    }

    @Override
    public String getItem(int position) {
        return mImageBeans == null ? null :
                position < mImageBeans.size() ? mImageBeans.get(position) : null;
    }

    @Override
    public View getView(int position, View itemView) {
        NiceImageView imageView;
        if (itemView == null) {
            imageView = new NiceImageView(mContext);
            imageView.setCornerRadius(8);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.act_bg01));
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        } else {
            imageView = (NiceImageView) itemView;
        }
        String url = mImageBeans.get(position);
        int itemSize = (DisplayUtils.getScreenWidth(mContext) - 2 * DisplayUtils.dip2px(mContext,4) - DisplayUtils.dip2px(mContext,54)) / 3;
        Glide.with(mContext).load(url)
                .override(itemSize, itemSize).into(imageView);


        return imageView;
    }
}
