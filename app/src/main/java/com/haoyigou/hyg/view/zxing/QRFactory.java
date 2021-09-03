package com.haoyigou.hyg.view.zxing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.zip.GZIPOutputStream;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/***
 * 二维码生成
 * 
 * @author li'mingqi
 * 
 */
public final class QRFactory {
	private static final int BLACK = 0xff000000;

	/***
	 * 生成二维码
	 * 
	 * @param str
	 *            二维码蕴含的数据
	 * @param widthAndHeight
	 *            二维码的宽和高
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		try {
			String str2 = compress("{\"codeType\":\"2\",\"content\":{\"codeid\":\"" + str + "\"}}");// 设置json格式
			String str3 = "{\"codeType\":\"2\",\"content\":{\"codeid\":\"" + str + "\"}}";// 设置json格式
			Log.i("xbee", "修改前数据大小===" + str3.getBytes().length);
			Log.i("xbee", "修改后数据===" + str2);
			Log.i("xbee", "修改后数据大小===" + str2.getBytes().length);
			BitMatrix matrix = new MultiFormatWriter().encode(str2, BarcodeFormat.QR_CODE, widthAndHeight,
					widthAndHeight, hints);

			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = BLACK;
					}
				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将数据压缩
	 */

	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

}
