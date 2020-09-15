package com.huaheng.mobilewms;

import android.app.Application;
import android.content.Context;

import com.huaheng.mobilewms.util.CrashHandler;
import com.huaheng.mobilewms.util.SpeechUtil;

import org.xutils.x;

public class WMSApplication extends Application {

    private static Context mContext;
    private static WMSApplication instance;
    private static String cookie;
    private static String token = "";
    public static boolean bluetoothConnect = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initData();
    }

    public static WMSApplication getInstance() {
        if(instance == null) {
            instance = new WMSApplication();
        }
        return instance;
    }

    public  void initData() {
        mContext = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        CrashHandler.getInstance().init(this);
        SpeechUtil.getInstance(mContext).initSpeech();
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getOkhttpCookie() {
        return cookie;
    }

    public static void setOkhttpCookie(String result) {
        cookie = result;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        WMSApplication.token = token;
    }

    public static boolean isBluetoothConnect() {
        return bluetoothConnect;
    }

    public static void setBluetoothConnect(boolean bluetoothConnect) {
        WMSApplication.bluetoothConnect = bluetoothConnect;
    }
}
