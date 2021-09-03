package com.haoyigou.hyg.view.widget;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class JocellChromeClient extends WebChromeClient {

    Context context;
    Activity activity;

    @Override
    public void onCloseWindow(WebView window) {
        // Log.i("System.out", "onCloseWindow");
        super.onCloseWindow(window);
    }

    public JocellChromeClient() {

    }


    public JocellChromeClient(ValueCallback<Uri> mUploadMessage,
                              Context context, Activity activity) {
        this.mUploadMessage = mUploadMessage;
        this.context = context;
        this.activity = activity;
    }

    public JocellChromeClient(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog,
                                  boolean isUserGesture, Message resultMsg) {
        // Log.i("System.out", "onCreateWindow");
        return true;
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        // Log.i("System.out", "onProgressChanged");
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        // Log.i("System.out", "onReceivedTitle");
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        // Log.i("System.out", "onReceivedIcon");
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url,
                                       boolean precomposed) {
        // Log.i("System.out", "onReceivedTouchIconUrl url"+url);
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        // Log.i("System.out", "onShowCustomView");
        super.onShowCustomView(view, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation,
                                 CustomViewCallback callback) {
        // Log.i("System.out", "onShowCustomView");
        super.onShowCustomView(view, requestedOrientation, callback);
    }

    @Override
    public void onHideCustomView() {
        // Log.i("System.out", "onHideCustomView");
        super.onHideCustomView();
    }

    @Override
    public void onRequestFocus(WebView view) {
        // Log.i("System.out", "onRequestFocus");
        super.onRequestFocus(view);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        // Log.i("System.out", "onJsAlert url="+url);
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               JsResult result) {
        // Log.i("System.out", "onJsConfirm url="+url);
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, JsPromptResult result) {
        // Log.i("System.out", "onJsPrompt url="+url);
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message,
                                    JsResult result) {
        // Log.i("System.out", "onJsBeforeUnload url="+url);
        return super.onJsBeforeUnload(view, url, message, result);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   Callback callback) {
        // Log.i("System.out", "onGeolocationPermissionsShowPrompt");
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        // Log.i("System.out", "onGeolocationPermissionsHidePrompt");
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public boolean onJsTimeout() {
        // Log.i("System.out", "onJsTimeout");
        return super.onJsTimeout();
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        // Log.i("System.out", "onConsoleMessage");
        super.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        // Log.i("System.out", "onConsoleMessage");
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        // Log.i("System.out", "getDefaultVideoPoster");
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        // Log.i("System.out", "getVideoLoadingProgressView");
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        // Log.i("System.out", "getVisitedHistory");
        super.getVisitedHistory(callback);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier,
                                        long currentQuota, long estimatedSize, long totalUsedQuota,
                                        QuotaUpdater quotaUpdater) {
        // Log.i("System.out", "onExceededDatabaseQuota");
        super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
                estimatedSize, totalUsedQuota, quotaUpdater);
    }

    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota,
                                         QuotaUpdater quotaUpdater) {
        // Log.i("System.out", "onReachedMaxAppCacheSize");
        super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota,
                quotaUpdater);
    }


    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        Log.e("log--", "H5点击添加图片");
        mUploadCallbackAboveL = filePathCallback;
        ((PersonWebViewAct) context).setFileMessage(filePathCallback);
        take();
        return true;
    }

    //<3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        ((PersonWebViewAct) context).setFileMessag(uploadMsg);
        take();
    }

    //>3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMessage = uploadMsg;
        ((PersonWebViewAct) context).setFileMessag(uploadMsg);
        take();
    }

    //>4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mUploadMessage = uploadMsg;
        ((PersonWebViewAct) context).setFileMessag(uploadMsg);
        take();
    }


    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        ((PersonWebViewAct) context).setImageUri(imageUri);
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        ((PersonWebViewAct) context).startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调
    private Uri imageUri;


//    // For Android 3.0+
//    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//        if (uploadMsg == null) {
//            return;
//        }
//        ((PersonWebViewAct) activity).setmUploadMessage(uploadMsg);
//        // mUploadMessage = uploadMsg;
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        activity.startActivityForResult(
//                Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//
//    }
//
//    // For Android 3.0+
//    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//        if (uploadMsg == null) {
//            return;
//        }
//        ((PersonWebViewAct) activity).setmUploadMessage(uploadMsg);
//        // mUploadMessage = uploadMsg;
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("*/*");
//        activity.startActivityForResult(
//                Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
//    }
//
//    // For Android 4.1
//    public void openFileChooser(ValueCallback<Uri> uploadMsg,
//                                String acceptType, String capture) {
//        if (uploadMsg == null) {
//            return;
//        }
//
//        ((PersonWebViewAct) activity).setmUploadMessage(uploadMsg);
//
//        // mUploadMessage = uploadMsg;
//        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        // i.addCategory(Intent.CATEGORY_OPENABLE);
//        // i.setType("image/*");
//        // activity.startActivityForResult(
//        // Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//        new ActionSheetDialog(context, uploadMsg)
//                .builder()
//                .setCancelable(false)
//                .setCanceledOnTouchOutside(false)
//                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Red,
//                        new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//
//                                Intent intent = new Intent(
//                                        MediaStore.ACTION_IMAGE_CAPTURE); //
//
//                                File out = new File(Environment
//                                        .getExternalStorageDirectory(),
//                                        "image.jpg");
//
//                                Uri uri = Uri.fromFile(out);
//
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//                                //
//                                activity.startActivityForResult(intent,
//                                        REQUEST_CODE_CAPTURE_CAMEIA); //
//
//                            }
//                        })
//                .addSheetItem(" ", ActionSheetDialog.SheetItemColor.Red,
//                        new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                Intent albumIntent = new Intent(
//                                        Intent.ACTION_PICK, null);
//                                albumIntent
//                                        .setDataAndType(
//                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                                "image/*");
//                                activity.startActivityForResult(albumIntent,
//                                        FILECHOOSER_RESULTCODE);
//
//                            }
//                        })
//
//                //
//                .show();
//
//    }
//
//
//    private Intent createDefaultOpenableIntent() {
//        // Create and return a chooser with the default OPENABLE
//        // actions including the camera, camcorder and sound
//        // recorder where available.
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("*/*");
//
//        Intent chooser = createChooserIntent(createCameraIntent(),
//                createCamcorderIntent(), createSoundRecorderIntent());
//        chooser.putExtra(Intent.EXTRA_INTENT, i);
//        return chooser;
//    }
//
//    private Intent createChooserIntent(Intent... intents) {
//        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
//        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
//        chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
//        return chooser;
//    }
//
//    private Intent createCameraIntent() {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File externalDataDir = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        System.out.println("externalDataDir:" + externalDataDir);
//        File cameraDataDir = new File(externalDataDir.getAbsolutePath()
//                + File.separator + "browser-photo");
//        cameraDataDir.mkdirs();
//        mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator
//                + System.currentTimeMillis() + ".jpg";
//        System.out.println("mcamerafilepath:" + mCameraFilePath);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(new File(mCameraFilePath)));
//
//        return cameraIntent;
//    }

    private Intent createCamcorderIntent() {
        return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    }

    private Intent createSoundRecorderIntent() {
        return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
    }

}
