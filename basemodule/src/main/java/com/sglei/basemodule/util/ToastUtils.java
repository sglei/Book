package com.sglei.basemodule.util;

import android.widget.Toast;
import com.sglei.basemodule.BookApplication;

public class ToastUtils {
    private static Toast toast;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;

    /**
     * 显示Toast
     *
     * @param message
     */
    public static void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(BookApplication.getInstance(), message, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > 2000) {
                    //防止快速点击后toast不展示，需要重新创建toast
                    toast = Toast.makeText(BookApplication.getInstance(), message, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    return;
                }
            } else {
                oldMsg = message;
                toast = Toast.makeText(BookApplication.getInstance(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(int resId) {
        showToast(BookApplication.getInstance().getString(resId));
    }
}
