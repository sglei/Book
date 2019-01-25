package com.sglei.routermodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.sglei.basemodule.util.ToastUtils;

/**
 * @creator zane
 * @time 2018/12/26 14:41
 */
public class ReflectUtils {

    public static Fragment getFragment(String name) {
        Fragment fragment;
        try {
            Class fragmentClass = Class.forName(name);
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.d("error--->", e.toString());
            return null;
        }
        return fragment;
    }

    public static Fragment getFragment(String name, Bundle bundle) {
        Fragment fragment;
        try {
            Class fragmentClass = Class.forName(name);
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            Log.d("error--->", e.toString());
            return null;
        }
        return fragment;
    }


    public static Object getModuleCall(String name) {
        Object object;
        try {
            Class aClass = Class.forName(name);
            object = aClass.newInstance();
        } catch (Exception e) {
            Log.d("error--->", e.toString());
            return null;
        }

        return object;
    }

    public static void startActivityWithName(Context context, String name) {
        try {
            Class clazz = Class.forName(name);
            startActivity(context, clazz);
        } catch (ClassNotFoundException e) {
            ToastUtils.showToast("业务组件单独调试不应该跟其他业务Module产生交互，如果你依然想要在运行期依赖其它组件");
            Log.d("error--->", e.toString());
        }
    }

    public static void startActivity(Context context, Class clazz) {
        context.startActivity(getIntent(context, clazz));
    }

    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static Intent getIntent(Context context, Class clazz) {
        return new Intent(context, clazz);
    }

    public static Intent getIntent(Context context, String name) {
        Class clazz = null;
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            ToastUtils.showToast("业务组件单独调试不应该跟其他业务Module产生交互，如果你依然想要在运行期依赖其它组件");
            Log.d("error--->", e.toString());
        }
        return new Intent(context, clazz);
    }
}
