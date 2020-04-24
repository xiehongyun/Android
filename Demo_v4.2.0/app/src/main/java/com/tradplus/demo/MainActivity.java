package com.tradplus.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tradplus.ads.mobileads.util.TestDeviceUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //测试设备添加
        //正式上线前注释
        TestDeviceUtil.getInstance().setNeedTestDevice(true);
        //facebook测试设备
        TestDeviceUtil.getInstance().setFacebookTestDevice("8ea15fb0-9e58-497e-a83e-9316d8f4d741");
        //Google Admob测试设备
        TestDeviceUtil.getInstance().setAdmobTestDevice("6B4BBE4293D1782CAF89EB361E013970");


        //横幅广告
        requestBanner();
        //插屏广告
        requestInterstitial();
        //激励视频广告
        requestRewardedVideo();
        //原生广告
        requestNative();
        //原生信息流广告
        requestNativeListView();
    }

    private void requestNativeListView() {
        Button nativelist_ad = (Button)findViewById(R.id.nativelist_ad);
        nativelist_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NativeListViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestNative() {
        Button native_advanced_btn = (Button)findViewById(R.id.native_ad_advanced);
        native_advanced_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NativeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestRewardedVideo() {
        Button rewarded_video_btn = (Button)findViewById(R.id.rewarded_video_ad);
        rewarded_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RewardedVideoActivity.class);
                startActivity(intent);
            }
        });
        
    }

    private void requestInterstitial() {
        Button Interstitial_btn = (Button)findViewById(R.id.interstitial_ad);
        Interstitial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InterstitialActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestBanner() {
        Button banner_btn = (Button)findViewById(R.id.banner_ad);
        banner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BannerActivity.class);
                startActivity(intent);
            }
        });
    }
}
