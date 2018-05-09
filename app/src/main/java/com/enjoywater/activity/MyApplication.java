package com.enjoywater.activity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.enjoywater.service.MainService;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private MainService mainService;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public MainService getMainService() {
        if (mainService == null) {
            mainService = MainService.Factory.create();
        }
        return mainService;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
