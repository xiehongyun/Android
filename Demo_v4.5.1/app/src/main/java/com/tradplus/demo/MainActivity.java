package com.tradplus.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tradplus.ads.mobileads.TradPlus;
import com.tradplus.ads.mobileads.gdpr.ATGDPRAuthCallback;
import com.tradplus.ads.mobileads.gdpr.Const;
import com.tradplus.ads.mobileads.util.TestDeviceUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button gdpr_view;
    private Button initSDK;
    private TextView gdprStatus;
    private TextView gdprCutomStatus;
    private RelativeLayout unknownCountry;
    private Button yeu;
    private Button neu;
    private boolean isAccept = false;
    private TextView tx_tip;
    private RelativeLayout gdpr_container;
    private Button gdpr_neu;
    private Button gdpr_yeu;
    private Button custom_gdpr_sdk_init;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSDK = (Button) findViewById(R.id.btn_gdpr_sdk_init);
        initSDK.setOnClickListener(this);
        gdpr_view = (Button) findViewById(R.id.gdpr_view);
        gdpr_view.setOnClickListener(this);
        gdprStatus = (TextView) findViewById(R.id.gdpr_status);
        gdprCutomStatus = (TextView) findViewById(R.id.gdpr_custom_status);
        unknownCountry = (RelativeLayout) findViewById(R.id.unknown_country);
        tx_tip = (TextView) findViewById(R.id.tip);
        yeu = (Button) findViewById(R.id.btn_yeu);
        yeu.setOnClickListener(this);
        neu = (Button) findViewById(R.id.btn_neu);
        neu.setOnClickListener(this);
        gdpr_container = (RelativeLayout) findViewById(R.id.gdpr_container);
        gdpr_neu = (Button) findViewById(R.id.btn_gdpr_neu);
        gdpr_neu.setOnClickListener(this);
        gdpr_yeu = (Button) findViewById(R.id.btn_gdpr_yeu);
        gdpr_yeu.setOnClickListener(this);

        custom_gdpr_sdk_init = (Button) findViewById(R.id.btn_custom_gdpr_sdk_init);
        custom_gdpr_sdk_init.setOnClickListener(this);


        //横幅广告
        requestBanner();
        //插屏广告
        requestInterstitial();
        //激励视频广告
        requestRewardedVideo();
        //积分墙广告
        requestOfferWall();
        //原生广告
        requestNative();
        //原生信息流广告
        requestNativeListView();
        //原生广告
        requestNativeBanner();
    }

    private void setGDPR(){
        //方法一：通过TradPlus提供的授权页面进行设置
        //第一步：调用TradPlus GDPR监听
        TradPlus.invoker().setmGDPRListener(new TradPlus.IGDPRListener() {
            @Override
            public void success(String s) {
                //成功
                //第二步：判断是否是欧盟用户
                if (TradPlus.isEUTraffic(MainActivity.this)){
                    //第三步：是，调用授权页面，让用户自己选择
                    TradPlus.showUploadDataNotifyDialog(MainActivity.this, new ATGDPRAuthCallback() {
                        @Override
                        public void onAuthResult(int level) {
                            Log.i("level", "onAuthResult: "+level);
                        }
                    }, Const.URL.GDPR_URL);
                }   //否，则默认上报数据

            }

            @Override
            public void failed(String s) {
                //失败：未知国家
                //方式一：调用授权页面让用户自己选择
                TradPlus.showUploadDataNotifyDialog(MainActivity.this, new ATGDPRAuthCallback() {
                    @Override
                    public void onAuthResult(int level) {
                        Log.i("level", "onAuthResult: "+level);
                    }
                }, Const.URL.GDPR_URL);

                //方法二：设置TradPlus GDPR等级，让客户自己选择
                int level = 0; //0,同意；1，拒绝
                TradPlus.setGDPRUploadDataLevel(MainActivity.this,level);
            }
        });

        //方法二：不使用TradPlus 提供的授权页面进行判断

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

    private void requestOfferWall() {
        Button offerwall_ad = (Button)findViewById(R.id.offerwall_ad);
        offerwall_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OfferWallActivity.class);
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

    private void requestNativeBanner() {
        Button banner_btn = (Button)findViewById(R.id.nativebanner_ad);
        banner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NativeBannerViewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gdpr_view:
                showGDPR();
                break;
            case R.id.btn_gdpr_sdk_init:
                if (!TradPlus.isInit) {
                    initSdk();
                }
                break;
            case R.id.btn_yeu:
                TradPlus.setEUTraffic(MainActivity.this,true);
                if(isAccept){
                    hasShowGDPR();
                }else {
                    if (gdpr_container != null) {
                        gdpr_container.setVisibility(View.VISIBLE);
                    }
                    if (unknownCountry != null) {
                        unknownCountry.setVisibility(View.GONE);

                    }
                }
                break;
            case R.id.btn_neu:
                TradPlus.setEUTraffic(MainActivity.this,false);
                if (unknownCountry != null) {
                    unknownCountry.setVisibility(View.GONE);
                }
                if (gdpr_container!=null && gdpr_container.getVisibility()== View.VISIBLE) {
                    gdpr_container.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_gdpr_neu:
                TradPlus.setGDPRUploadDataLevel(MainActivity.this,TradPlus.NONPERSONALIZED);
                if (gdpr_container != null) {
                    gdpr_container.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_gdpr_yeu:
                TradPlus.setGDPRUploadDataLevel(MainActivity.this,TradPlus.PERSONALIZED);
                if (gdpr_container != null) {
                    gdpr_container.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_custom_gdpr_sdk_init:
                if (!TradPlus.isInit) {
                    initSdk1();
                }
                break;
        }
    }

    private void hasShowGDPR() {
        if (!TradPlus.isFirstShowGDPR(MainActivity.this)) {
            showGDPR();
        } else {
            Toast.makeText(MainActivity.this, "您已经选择过，想修改请点击修改按钮！", Toast.LENGTH_LONG).show();
        }
    }

    private void showGDPR() {
        TradPlus.showUploadDataNotifyDialog(this, new ATGDPRAuthCallback() {
            @Override
            public void onAuthResult(int level) {
                Log.i("level", "onAuthResult: "+level);
                TradPlus.setIsFirstShowGDPR(MainActivity.this,true);
            }
        }, Const.URL.GDPR_URL);
    }

    private void initSdk() {
        isAccept = true;
        TradPlus.invoker().setmGDPRListener(new TradPlus.IGDPRListener() {
            @Override
            public void success(String msg) {

                if (TradPlus.isEUTraffic(MainActivity.this)) {
                    if (gdprStatus != null) {
                        gdprStatus.setText("欧盟用户！");
                    }
                    if (gdpr_view != null) {
                        gdpr_view.setVisibility(View.VISIBLE);
                    }
                    hasShowGDPR();

                }else {
                    if (gdprStatus != null) {
                        gdprStatus.setText("非欧盟用户！");
                    }
                }

            }

            @Override
            public void failed(String errorMsg) {
                Log.i("tradplus", "failed: "+errorMsg);
                //未知是否为欧盟
                if (gdprStatus != null) {
                    gdprStatus.setText("未知国家");
                }
                if (unknownCountry != null) {
                    unknownCountry.setVisibility(View.VISIBLE);
                }
            }
        });
        //初始化SDK
        TradPlus.invoker().initSDK(this);
//        TradPlus.setIsCNLanguageLog(true);//Log中文模式
        //设置测试模式，正式上线前注释
        TestDeviceUtil.getInstance().setNeedTestDevice(true);
        TradPlus.setLocalDebugMode(true);
        TradPlus.setDebugMode(true);
    }

    private void initSdk1() {
        isAccept = false;
        TradPlus.invoker().setmGDPRListener(new TradPlus.IGDPRListener() {
            @Override
            public void success(String msg) {

                if (TradPlus.isEUTraffic(MainActivity.this)) {
                    if (gdprCutomStatus != null) {
                        gdprCutomStatus.setText("欧盟用户！");
                    }
                    if (gdpr_container != null) {
                        gdpr_container.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (gdprCutomStatus != null) {
                        gdprCutomStatus.setText("非欧盟用户！");
                    }
                }

            }

            @Override
            public void failed(String errorMsg) {
                Log.i("tradplus", "failed: "+errorMsg);
                //未知是否为欧盟
                if (gdprCutomStatus != null) {
                    gdprCutomStatus.setText("未知国家");
                }
                if (unknownCountry != null) {
                    unknownCountry.setVisibility(View.VISIBLE);
                }
            }
        });
        //初始化SDK
        TradPlus.invoker().initSDK(this);
//        TradPlus.setIsCNLanguageLog(true);//Log中文模式

        //设置测试模式，正式上线前注释
        TestDeviceUtil.getInstance().setNeedTestDevice(true);
    }
}
