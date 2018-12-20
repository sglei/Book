package com.sglei.basemodule.util;

import android.util.Log;
import com.sglei.basemodule.BuildConfig;

/**
 * @creator sglei
 * @time 2018/12/19 14:23
 */
public class Logger {
    private static final int LEVEL_V = 0x00;
    private static final int LEVEL_D = 0x01;
    private static final int LEVEL_I = 0x02;
    private static final int LEVEL_W = 0x03;
    private static final int LEVEL_E = 0x04;
    private static final boolean isDebug = BuildConfig.DEBUG;
    private static String TAG = "zane-book";

    public static void setTag(String tag) {
        TAG = tag;
    }

    public static void v(String message) {
        printLog(LEVEL_V, message);
    }

    public static void d(String message) {
        printLog(LEVEL_D, message);
    }

    public static void i(String message) {
        printLog(LEVEL_I, message);
    }

    public static void w(String message) {
        printLog(LEVEL_W, message);
    }

    public static void e(String message) {
        printLog(LEVEL_E, message);
    }

    public static void v(String tag, String message) {
        TAG = tag;
        printLog(LEVEL_V, message);
    }

    public static void d(String tag, String message) {
        TAG = tag;
        printLog(LEVEL_D, message);
    }

    public static void i(String tag, String message) {
        TAG = tag;
        printLog(LEVEL_I, message);
    }

    public static void w(String tag, String message) {
        TAG = tag;
        printLog(LEVEL_W, message);
    }

    public static void e(String tag, String message) {
        TAG = tag;
        printLog(LEVEL_E, message);
    }

    private static void printLog(int level, String message) {
        if (!isDebug && level != LEVEL_E) {
            return;
        }

        if (message == null) {
            Log.e(TAG, "message = null");
            return;
        }

        switch (level) {
            case LEVEL_V:
                Log.v(TAG, message);
                break;
            case LEVEL_D:
                Log.d(TAG, message);
                break;
            case LEVEL_I:
                Log.i(TAG, message);
                break;
            case LEVEL_W:
                Log.w(TAG, message);
                break;
            case LEVEL_E:
                Log.e(TAG, message);
                break;
            default:
                Log.d(TAG, message);
        }
    }
}
