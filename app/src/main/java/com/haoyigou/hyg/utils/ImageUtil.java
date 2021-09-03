package com.haoyigou.hyg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Description：ImageUtil
 * Created by：CaMnter
 * Time：2015-10-19 14:06
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";

    /**
     * 获取圆角Bitmap
     *
     * @param srcBitmap
     * @param radius
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap srcBitmap, float radius) {
        Bitmap resultBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xBDBDBE);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBitmap, rect, rect, paint);
        return resultBitmap;
    }


    /**
     * 图片解析
     *
     * @param path
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeScaleImage(String path, int targetWidth, int targetHeight) {
        BitmapFactory.Options bitmapOptions = getBitmapOptions(path);
        bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, targetWidth, targetHeight);
        bitmapOptions.inJustDecodeBounds = false;
        Bitmap noRotatingBitmap = BitmapFactory.decodeFile(path, bitmapOptions);
        int degree = readPictureDegree(path);
        Bitmap rotatingBitmap;
        if (noRotatingBitmap != null && degree != 0) {
            rotatingBitmap = rotatingImageView(degree, noRotatingBitmap);
            noRotatingBitmap.recycle();
            return rotatingBitmap;
        } else {
            return noRotatingBitmap;
        }
    }


    /**
     * 获取缩略图
     *
     * @param path
     * @param targetWidth
     * @return
     */
    public static String getThumbnailImage(String path, int targetWidth) {
        Bitmap scaleImage = decodeScaleImage(path, targetWidth, targetWidth);
        try {
            File file = File.createTempFile("image", ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            scaleImage.compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream);
            fileOutputStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
    }

    /**
     * 图片解析
     *
     * @param context
     * @param resId
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeScaleImage(Context context, int resId, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return bitmap;
    }

    /**
     * 计算样本大小
     *
     * @param options
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int scale = 1;
        if (height > targetHeight || width > targetWidth) {
            int heightScale = Math.round((float) height / (float) targetHeight);
            int widthScale = Math.round((float) width / (float) targetWidth);
            scale = heightScale > widthScale ? heightScale : widthScale;
        }
        return scale;
    }


    /**
     * 获取BitmapFactory.Options
     *
     * @param pathName
     * @return
     */
    public static BitmapFactory.Options getBitmapOptions(String pathName) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);
        return opts;
    }

    /**
     * 获取图片角度
     *
     * @param filename
     * @return
     */
    public static int readPictureDegree(String filename) {
        short degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filename);
            int anInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (anInt) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                case ExifInterface.ORIENTATION_TRANSPOSE:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                default:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    /**
     * 旋转ImageView
     *
     * @param degree
     * @param source
     * @return
     */
    public static Bitmap rotatingImageView(int degree, Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    /**
     * 获取视频缩略图
     *
     * @param filePath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath, int width, int height, int kind) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    /**
     * Image resource ID was converted into a byte [] data
     * 图片资源ID 转换 为 图片 byte[] 数据
     *
     * @param context
     * @param imageResourceId
     * @return
     */
    public static byte[] toByteArray(Context context, int imageResourceId) {
        Bitmap bitmap = ImageUtil.toBitmap(context, imageResourceId);
        if (bitmap != null) {
            return ImageUtil.toByteArray(bitmap);
        } else {
            return null;
        }
    }

    /**
     * ImageView getDrawable () into a byte [] data
     * ImageView的getDrawable() 转换为 byte[] 数据
     *
     * @param imageView
     * @return
     */
    public static byte[] toByteArray(ImageView imageView) {
        Bitmap bitmap = ImageUtil.toBitmap(imageView);
        if (bitmap != null)
            return ImageUtil.toByteArray(bitmap);
        else {
            Log.w(ImageUtil.TAG,
                    "the ImageView imageView content was invalid");
            return null;
        }
    }

    /**
     * byte [] data type conversion for Bitmap data types
     * byte[]数据类型转换为 Bitmap数据类型
     *
     * @param imageData
     * @return
     */
    public static Bitmap toBitmap(byte[] imageData) {
        if ((imageData != null) && (imageData.length != 0)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0,
                    imageData.length);
            return bitmap;
        } else {
            Log.w(ImageUtil.TAG, "the byte[] imageData content was invalid");
            return null;
        }
    }

    /**
     * Image resource ID is converted to Bitmap type data
     * 资源ID 转换为 Bitmap类型数据
     *
     * @param context
     * @param imageResourceId
     * @return
     */
    public static Bitmap toBitmap(Context context, int imageResourceId) {
        // 将图片转化为位图
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                imageResourceId);
        if (bitmap != null) {
            return bitmap;
        } else {
            Log.w(ImageUtil.TAG,
                    "the int imageResourceId content was invalid");
            return null;
        }
    }

    /**
     * ImageView types into a Bitmap
     * ImageView类型转换为Bitmap
     *
     * @param imageView
     * @return
     */
    public static Bitmap toBitmap(ImageView imageView) {
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ImageUtil.toBitmap(imageView.getDrawable());
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * Bitmap type is converted into a image byte [] data
     * Bitmap类型 转换 为图片 byte[] 数据
     *
     * @param bitmap
     * @return
     */
    public static byte[] toByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            // 创建一个字节数组输出流，流的大小为size
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    byteArrayOutputStream);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imageData = byteArrayOutputStream.toByteArray();
            return imageData;
        } else {
            Log.w(ImageUtil.TAG,
                    "the Bitmap bitmap content was invalid");
            return null;
        }

    }

    /**
     * Drawable type into a Bitmap
     * Drawable 类型转换为 Bitmap类型
     *
     * @param drawable
     * @return
     */
    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            return bitmap;
        } else {
            Log.w(ImageUtil.TAG,
                    "the Drawable drawable content was invalid");
            return null;
        }
    }

    /**
     * Bitmap type into a Drawable
     * Bitmap 类型转换为 Drawable类型
     *
     * @param bitmap
     * @return
     */
    public static Drawable toDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            return drawable;
        } else {
            Log.w(ImageUtil.TAG,
                    "the Bitmap bitmap content was invalid");
            return null;
        }
    }

    /**
     * 网络url 获取图片
     *
     * @param url
     * @return
     */
    public static Bitmap toBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (fileUrl == null) return null;
        try {
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                Log.e("ImageUtil", "toBitmap timeout");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
