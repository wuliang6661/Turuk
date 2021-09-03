package com.haoyigou.hyg.base;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by Witness on 2020-04-28
 * Describe:
 */
public class OkHttpSSLUtils {
    public static OkHttpClient.Builder getOkHttpBuilder(Context context, String baseHostname, String... assetNames) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            InputStream[] inputStreams = new InputStream[assetNames.length];
            for (int i = 0; i < assetNames.length; i++) {
                inputStreams[i] = context.getAssets().open(assetNames[i]);
            }
            GeneralHostnameVerifier hostnameVerifier = GeneralHostnameVerifier.getInstance(baseHostname, inputStreams);
            if (hostnameVerifier != null) {
                builder.hostnameVerifier(hostnameVerifier);
            }
            for (InputStream item : inputStreams) {
                item.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }

    public static OkHttpClient.Builder getOkHttpBuilder(String baseHostname, File... files) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            InputStream[] inputStreams = new InputStream[files.length];
            for (int i = 0; i < files.length; i++) {
                inputStreams[i] = new FileInputStream(files[i]);
            }
            GeneralHostnameVerifier hostnameVerifier = GeneralHostnameVerifier.getInstance(baseHostname, inputStreams);
            if (hostnameVerifier != null) {
                builder.hostnameVerifier(hostnameVerifier);
            }
            for (InputStream item : inputStreams) {
                item.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }
}
