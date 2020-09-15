package com.huaheng.mobilewms.util;

import android.util.Log;

public class WMSLog {

    private final static String TAG = "WMSLog";
    private static boolean DEBUG = true;


    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if(DEBUG) {
            Log.i(TAG, msg);
        }
    }
}
