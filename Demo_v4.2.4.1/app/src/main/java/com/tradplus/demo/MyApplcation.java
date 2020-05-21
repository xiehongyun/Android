package com.tradplus.demo;

import android.app.Application;

import com.tradplus.ads.mobileads.TradPlus;

public class MyApplcation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化TradPlus SDK
        TradPlus.invoker().initSDK(this);
    }
}
