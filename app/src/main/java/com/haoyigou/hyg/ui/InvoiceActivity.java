package com.haoyigou.hyg.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.FileProvider;

import android.provider.Browser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.utils.HorizontalProgressBarWithNumber;
import com.haoyigou.hyg.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Witness on 2019/4/2
 * Describe: pdf查看
 */
public class InvoiceActivity{

    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    /* 更新进度条 */
    private HorizontalProgressBarWithNumber mProgress;

    private Dialog mDownloadDialog;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private File docFile;//下载的doc文档


    private String murl;
    private Context mContext;

    public InvoiceActivity(Context context, String murl) {
        this.murl = murl;
        this.mContext = context;
    }

    public void viewPdf(){
        if (murl != null && !murl.equals("")) {
//            showDownloadDialog();  //下载到本地打开
            openPDFInBrowser(mContext,murl);//浏览器打开
        }else {
            ToastUtils.showToast(mContext,"电子发票下载失败");
        }
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.invoice_layout);
//    }

    public void openPDFInBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w("error", "Activity was not found for intent, " + intent.toString());
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
//                    finish();
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
                        getWordFileIntent(sdDir.toString() + "/hyg/invoice.pdf");
                    } else {
                        getWordFileIntent(StateMessage.DOWNLOAD_PATH + "/invoice.pdf");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("下载电子发票");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (HorizontalProgressBarWithNumber) v.findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setCancelable(false);
        // 取消更新
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                /*if(updateSign==2){
                    ((Activity)mContext).finish();
				}*/
                    }
                });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 下载文件
        downloadDoc();
    }

    /**
     * 下载文件
     */
    private void downloadDoc() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author shichenwei
     * @date 2012-4-26
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
                    mSavePath = sdDir.toString() + "/hyg";
                } else {
                    mSavePath = StateMessage.DOWNLOAD_PATH; //手机内存位置
                }
                URL url = new URL(murl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();
                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                docFile = new File(mSavePath, "invoice.pdf");
                //修改权限，使系统的intent可以去安装
                String[] command = {"chmod", "777", mSavePath + "/" + "invoice.pdf"};
                String[] command2 = {"chmod", "777", mSavePath};
                ProcessBuilder builder = new ProcessBuilder(command);
                ProcessBuilder builder2 = new ProcessBuilder(command2);
                try {
                    builder.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = new FileOutputStream(docFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                    if (numread <= 0) {
                        //下载完成后修改已下载文件的读写权限
                        builder2.start();
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);// 点击取消就停止下载.
                fos.close();
                is.close();
//				}
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    //android获取一个用于打开Word文件的intent
    public void getWordFileIntent(String Path){
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = ".pdf";
            //设置intent的data和Type属性。android 7.0以上crash,改用provider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri fileUri = FileProvider.getUriForFile(mContext, "com.haoyigou.hyg.fileProvider", docFile);//android 7.0以上
                intent.setDataAndType(fileUri, type);
                grantUriPermission(mContext, fileUri, intent);
            }else {
                intent.setDataAndType(/*uri*/Uri.fromFile(docFile), type);
            }
            //跳转
            mContext.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            ToastUtils.showToast(mContext,"打开失败，请重新下载或者检查手机是否安装word等应用程序");
        }
    }

    private static void grantUriPermission (Context context, Uri fileUri, Intent intent) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }
}
