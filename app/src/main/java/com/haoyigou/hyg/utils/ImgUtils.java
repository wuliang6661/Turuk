package com.haoyigou.hyg.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImgUtils {
	/**
	 * 图片质量压缩
	 * 
	 * @param size
	 *            图片文件的大小
	 * @param bitmap
	 * @param photoUri
	 *            存储的uri
	 */

	public static void imgCompress(int size, Bitmap bitmap, Uri photoUri) {
		try {
			OutputStream os = new FileOutputStream(photoUri.getPath());
			// Bitmap bitmap = decodeUriAsBitmap(photoUri);
			if (size >= 1000) {// size 大于1000kb时
//				Log.e("LOG_TAG", "15");
				bitmap.compress(CompressFormat.JPEG, 15, os);// 质量压缩为原图的20%
			} else if (size < 1000 && size >= 500) {
//				Log.e("LOG_TAG", "20");
				bitmap.compress(CompressFormat.JPEG, 20, os);// 质量压缩为原图的30%
			} else if (size < 500 && size >= 250) {
//				Log.e("LOG_TAG", "40");
				bitmap.compress(CompressFormat.JPEG, 40, os);// 质量压缩为原图的50%
			} else {
//				Log.e("LOG_TAG", "80");
				bitmap.compress(CompressFormat.JPEG, 80, os);// 不压缩
			}
			os.flush();
			os.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static Bitmap readBitmap(Bitmap bitmap) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 2;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;// 表示16位位图 565代表对应三原色占的位数
		opt.inInputShareable = true;
		opt.inPurgeable = true;// 设置图片可以被回收
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 20, baos);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		return BitmapFactory.decodeStream(isBm, null, opt);
	}

	/** 图片回收 */
	public static void releaseImageViewResouce(ImageView img) {
		if (img == null)
			return;// 判断imageview是否存在
		Drawable drawable = img.getDrawable();
		if (drawable != null && drawable instanceof BitmapDrawable) {// 判断是否为bitmap
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if (bitmap != null && !bitmap.isRecycled()) {// 判断bitmap是否回收
				bitmap.recycle();
			}
		}
	}
}