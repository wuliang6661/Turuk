package com.haoyigou.hyg.ui;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.utils.ClipImageLayout;
import com.haoyigou.hyg.utils.FileUtils;


/**
 * @author zhoujiawei
 */
public class TailorActivity extends Activity {
    private ClipImageLayout mClipImageLayout;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tailor);
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        Bundle bundle = this.getIntent().getExtras();
        //
        Uri uri = (Uri) bundle.get("result");
        try {
            mClipImageLayout.setMZoomImageView(new BitmapDrawable(FileUtils.sizeImage(MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(), uri), this)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private final static int IMAGETAILOR_RESULTCODE = 2;


    /**
     * @param v
     */
    public void cancelClickListener(View v) {
        setResult(IMAGETAILOR_RESULTCODE, null);
        finish();
    }

    /**
     * @param v
     */
    public void okClickListener(View v) {
        Bitmap bitmap = mClipImageLayout.clip();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();

        Intent intent = new Intent();
        intent.putExtra("bitmap", datas);

        setResult(IMAGETAILOR_RESULTCODE, intent);
        finish();
    }

}
