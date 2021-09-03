package com.haoyigou.hyg.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.haoyigou.hyg.R;
import com.haoyigou.hyg.utils.UploadPhotoUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class ImageAddGridViewAdapter extends BaseAdapter {

    Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private onDeleteOnClice listener;
    Bitmap bitmap;

    public ImageAddGridViewAdapter(Context context, List<String> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeList(List<String> list) {
        this.list = list;
    }

    public void setOnDeleteListener(onDeleteOnClice listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.image_add_gridview_items, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.theme_picture);
            holder.delete_img = (ImageView) convertView.findViewById(R.id.delete_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            InputStream is = new FileInputStream(list.get(position));    //防止位图内存溢出
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inSampleSize = 4;
            opts.inInputShareable = true;
            bitmap = BitmapFactory.decodeStream(is, null, opts);
            holder.imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDelele(position);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView imageView;
        ImageView delete_img;
    }

    public interface onDeleteOnClice {
        void onDelele(int position);
    }

}
