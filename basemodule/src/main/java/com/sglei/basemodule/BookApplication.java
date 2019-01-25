package com.sglei.basemodule;

import androidx.multidex.MultiDexApplication;

public class BookApplication extends MultiDexApplication {

    private static BookApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new BookApplication();
    }

    public static BookApplication getInstance() {
        return instance;
    }

}
