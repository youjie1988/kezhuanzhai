package com.huaheng.mobilewms.download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * Created by youjie on 2018/4/2.
 */

public class NetworkUtils {

    /*
    ** 判断当前网络是可用并且网络良好
     */
    public static boolean isGoodNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        int wifiState = mWifiManager.getWifiState();
        if(wifiState == WifiManager.WIFI_STATE_ENABLED) {
            NetworkInfo wifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isAvailable() && wifiInfo.isConnected()) {
                return true;
            }
        }
        TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = mgr.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                    return false; // 2g
                }
                return true;
            }
        }
        NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (info!=null&&info.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }
}
