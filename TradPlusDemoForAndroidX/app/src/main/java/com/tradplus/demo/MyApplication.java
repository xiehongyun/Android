package com.tradplus.demo;


import android.app.Application;

import com.flute.ads.mobileads.Flute;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Init TradPlus SDK
        Flute.invoker().initPub(this);
    }
}
