package com.haoyigou.hyg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by wuliang on 2016/12/5.
 * <p>
 * 下载图片到相册
 */

public class DownloadImageToGalleryUtils {


    private Activity context;
    private String[] urls;
    private static int sun = 0;   //已下载的图片数

    private onCommotListener listener;
    private File[] files;

    private static int IMAGE_NAME = 0;

    public DownloadImageToGalleryUtils(Activity context, String[] urls) {
        this.context = context;
        this.urls = urls;
        this.files = new File[urls.length];
    }


    public void startService() {
        for (String url : urls) {
            if (url != null && !url.equals(""))
                new DownloadImageAsyncTask(context).execute(url);
        }
    }

    public void setCommotListener(onCommotListener listener) {
        this.listener = listener;
    }


    public interface onCommotListener {

        void onCommot(File[] files);
    }


    /**
     * 下载图片异步任务
     */
    private class DownloadImageAsyncTask extends AsyncTask<String, Integer, String> {

        private Activity activity;
        private String localFilePath;

        DownloadImageAsyncTask(Activity activity) {
            super();
            this.activity = activity;
        }

        /**
         * 对应AsyncTask第一个参数
         * 异步操作，不在主UI线程中，不能对控件进行修改
         * 可以调用publishProgress方法中转到onProgressUpdate(这里完成了一个handler.sendMessage(...)的过程)
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            // TODO 注意这里
            /**
             * 这里接入你所用的网络框架去下载图片，只要保证this.localFilePath的值有就可以了
             */
            URL fileUrl = null;
            try {
                fileUrl = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (fileUrl == null) return null;
            try {
                HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                //计算文件长度
                int lengthOfFile = connection.getContentLength();
                /**
                 * 不存在SD卡，就放到缓存文件夹内
                 */
                File cacheDir = this.activity.getCacheDir();
                File downloadFile = new File(cacheDir, UUID.randomUUID().toString() + ".jpg");
                this.localFilePath = downloadFile.getPath();
                if (!downloadFile.exists()) {
                    File parent = downloadFile.getParentFile();
                    if (parent != null) parent.mkdirs();
                }
                FileOutputStream output = new FileOutputStream(downloadFile);
                InputStream input = connection.getInputStream();
                //下载
                byte[] buffer = new byte[1024];
                int len;
                long total = 0;
                // 计算进度
                while ((len = input.read(buffer)) > 0) {
                    total += len;
                    this.publishProgress((int) ((total * 100) / lengthOfFile));
                    output.write(buffer, 0, len);
                }
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 对应AsyncTask第三个参数 (接受doInBackground的返回值)
         * 在doInBackground方法执行结束之后在运行，此时已经回来主UI线程当中 能对UI控件进行修改
         *
         * @param string The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String string) {
            /**
             * 设置按钮可用，并隐藏Dialog
             */
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;
            /**
             * ImageUtil.decodeScaleImage 解析图片
             */
            Bitmap bitmap = ImageUtil.decodeScaleImage(this.localFilePath, screenWidth, screenHeight);
            /**
             * 保存图片到相册
             */
//            String imageName = System.currentTimeMillis() + ".jpg";
//            String file = MediaStore.Images.Media.insertImage(context.getApplicationContext().getContentResolver(), bitmap, imageName, "好易购");
            File file = saveImageToGallery(context, bitmap);
            files[sun] = file;
            sun++;
            if (sun == urls.length) {
                if (listener != null) {
                    listener.onCommot(files);
                    sun = 0;
                    IMAGE_NAME = 0;
                }
            }
        }


        /**
         * 对应AsyncTask第二个参数
         * 在doInBackground方法当中，每次调用publishProgress方法都会中转(handler.sendMessage(...))到onProgressUpdate
         * 在主UI线程中，可以对控件进行修改
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        /**
         * 运行在主UI线程中，此时是预执行状态，下一步是doInBackground
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * <p>Applications should preferably override {@link #onCancelled(Object)}.
         * This method is invoked by the default implementation of
         * {@link #onCancelled(Object)}.</p>
         * <p/>
         * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
         * {@link #doInBackground(Object[])} has finished.</p>
         *
         * @see #onCancelled(Object)
         * @see #cancel(boolean)
         * @see #isCancelled()
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }


    /***
     * 将下载的图片保存到本地
     *
     * @param bitmap
     * @return
     */
    public File saveImageToSdCard(Bitmap bitmap) {
        boolean success = false;
        // Encode the file as a PNG image.
        File file = null;
        try {
            file = createStableImageFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            //           100 to keep full quality of the image
            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            //          Toast.makeText(getApplicationContext(), "Image saved with success",
            //                  Toast.LENGTH_LONG).show();
            return file;
        } else {
            return null;
        }
    }


    File createStableImageFile() throws IOException {
        IMAGE_NAME++;
        String imageFileName = Integer.toString(IMAGE_NAME) + ".jpg";
        File storageDir = context.getExternalCacheDir();
        //        File image = File.createTempFile(
        //            imageFileName,  /* prefix */
        //            ".jpg",         /* suffix */
        //            storageDir      /* directory */
        //        );
        File image = new File(storageDir, imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        //        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public static File saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return file;
            } else {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
