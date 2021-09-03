package com.haoyigou.hyg.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.andrjhf.storage.encrypt.AES256SharedPreferences;
import com.haoyigou.hyg.application.GlobalApplication;
import com.haoyigou.hyg.common.security.MD5;

import static com.haoyigou.hyg.application.GlobalApplication.mAES256SharedPreferences;
import static com.tencent.open.utils.Global.getSharedPreferences;

/**
 * Created by kristain on 15/12/20.
 */
public class SharedPreferencesUtils {

    private static final String SP_NAME = "haoyigou";
    public static final String KEY_LOGIN_TOKEN = "token";
    public static final String KEY_LOGIN_First = "first";
    public static final String JSESSIONID="jsessionid";


    private static SharedPreferencesUtils instance = new SharedPreferencesUtils();

    public SharedPreferencesUtils() {
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new SharedPreferencesUtils();
        }
    }

    public static SharedPreferencesUtils getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    private android.content.SharedPreferences getSp() {
        return GlobalApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public int getInt(String key, int def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getInt(key, def);
                def = mAES256SharedPreferences.getInt(key,def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putInt(key, val);
                e.commit();
            }
            mAES256SharedPreferences.edit().putInt(key,val).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key, long def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putLong(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getString(key, def);
                def = mAES256SharedPreferences.getString(key,def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putString(key, val);
                e.commit();
            }
            mAES256SharedPreferences.edit().putString(key,val).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean def) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null)
                def = sp.getBoolean(key, def);
                def = mAES256SharedPreferences.getBoolean(key,def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.putBoolean(key, val);
                e.commit();
            }
            mAES256SharedPreferences.edit().putBoolean(key,val).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(String key) {
        try {
            android.content.SharedPreferences sp = getSp();
            if (sp != null) {
                Editor e = sp.edit();
                e.remove(key);
                e.commit();
            }
            mAES256SharedPreferences.edit().remove(key).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
