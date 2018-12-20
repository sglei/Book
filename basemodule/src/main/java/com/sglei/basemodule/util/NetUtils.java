package com.sglei.basemodule.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.sglei.basemodule.BookApplication;

/**
 * @creator zane
 * @time 2018/12/19 14:33
 */
public class NetUtils {

    private static final String NETWORK_TYPE_WIFI = "wifi";
    private static final String NETWORK_TYPE_3G = "eg";
    private static final String NETWORK_TYPE_2G = "2g";
    private static final String NETWORK_TYPE_WAP = "wap";
    private static final String NETWORK_TYPE_UNKNOWN = "unknown";
    private static final String NETWORK_TYPE_DISCONNECT = "disconnect";

    /**
     * Get network type
     */
    public static int getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BookApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }

    /**
     * Get network type name
     */
    public static String getNetworkTypeName() {
        ConnectivityManager manager =
                (ConnectivityManager) BookApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = NETWORK_TYPE_DISCONNECT;
        if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        }

        if (networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(typeName)) {
                type = NETWORK_TYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork() ? NETWORK_TYPE_3G
                        : NETWORK_TYPE_2G)
                        : NETWORK_TYPE_WAP;
            } else {
                type = NETWORK_TYPE_UNKNOWN;
            }
        }
        return type;
    }

    /**
     * Whether is fast mobile network
     */
    private static boolean isFastMobileNetwork() {
        TelephonyManager telephonyManager =
                (TelephonyManager) BookApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) BookApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info;
            if (cm != null) {
                info = cm.getActiveNetworkInfo();
                return null != info && info.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected() {
        if (BookApplication.getInstance() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) BookApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = null;
            if (mConnectivityManager != null) {
                mMobileNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            }
            if (mMobileNetworkInfo != null && mMobileNetworkInfo.isAvailable()) {
                return mMobileNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected() {
        if (BookApplication.getInstance() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) BookApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = null;
            if (mConnectivityManager != null) {
                mWiFiNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            }
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isAvailable()) {
                return mWiFiNetworkInfo.isConnected();
            }
        }
        return false;
    }
}
