package com.tradplus.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tradplus.ads.mobileads.TradPlusErrorCode;
import com.tradplus.ads.mobileads.TradPlusInterstitial;
import com.tradplus.ads.mobileads.TradPlusInterstitialExt;
import com.tradplus.ads.network.CanLoadListener;


public class OfferWallActivity extends AppCompatActivity implements TradPlusInterstitial.InterstitialAdListener{

    TradPlusInterstitialExt mTradPlusOfferWall;
    Button show,load;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        tv = findViewById(R.id.tv);
        load = findViewById(R.id.load);
        show = findViewById(R.id.show);

        /*
         * 1、参数2：AdUnitID，TradPlus后台设置 对应广告类型的广告位（非三方广告网络的placementId）
         *          这里添加的是供测试使用的插屏广告位，正式上线前必须替换成您申请的广告位
         *
         *          注意广告位不能填错，否则无法拿到广告
         */
        mTradPlusOfferWall = new TradPlusInterstitialExt(this, "0704BA87BDE496D391E5174CDD6B5E08");
        //设置canload监听，确保在广告位初始化成功的时候进行广告的第一次加载
        mTradPlusOfferWall.setCanLoadListener(new CanLoadListener() {
            @Override
            public void canLoad() {
//**********注意事项（1）第一次加载广告
                load.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTradPlusOfferWall.load();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText("first loading......");
                            }
                        });
                    }
                });


            }
        });
        mTradPlusOfferWall.loadConfig();

        //进入广告位场景的时候调用
        mTradPlusOfferWall.confirmUWSAd();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * 判断是否有广告加载成功：
                 * 可以直接调用obj.show(),当返回true时会自动展示广告；
                 */
                //方式一：
                if (!mTradPlusOfferWall.show()) {
                    Log.d("TradPlus", "No ads is Ready,loading...");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("no ads is ready,loading...");
                        }
                    });
                    mTradPlusOfferWall.load();
                }

            }
        });

        //设置监听
        mTradPlusOfferWall.setInterstitialAdListener(this);
    }

    @Override
    public void onInterstitialLoaded(TradPlusInterstitial tradPlusInterstitial) {
        //广告加载成功，获取加载成功的三方广告网络名称和广告位
        if(tradPlusInterstitial != null) {
            String channelName = tradPlusInterstitial.getChannelName();
            String adUnitId = tradPlusInterstitial.getAdUnitId();
            Log.d("TradPlus", "OfferWall Loaded");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText("OfferWall Loaded");
                }
            });
        }
    }

    @Override
    public void onInterstitialFailed(TradPlusInterstitial tradPlusInterstitial, TradPlusErrorCode tradPlusErrorCode) {
        //广告加载成功，获取加载成功的三方广告网络名称和广告位
        if(tradPlusInterstitial != null) {
            String channelName = tradPlusInterstitial.getChannelName();
            String adUnitId = tradPlusInterstitial.getAdUnitId();
            Log.d("TradPlus", "OfferWall Failed");
        }
    }

    @Override
    public void onInterstitialShown(TradPlusInterstitial tradPlusInterstitial) {
        //广告开始展示
    }

    @Override
    public void onInterstitialClicked(TradPlusInterstitial tradPlusInterstitial) {
        //广告被点击
    }

    @Override
    public void onInterstitialDismissed(TradPlusInterstitial tradPlusInterstitial) {
        //广告被关闭
//**********注意事项（4）
        //建议：在广告关闭时重新加载广告
//        mTradPlusOfferWall.load();

    }

    @Override
    public void onInterstitialRewarded(TradPlusInterstitial tradPlusInterstitial, String s, int i) {
        //奖励
    }

    @Override
    protected void onResume() {
        mTradPlusOfferWall.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        mTradPlusOfferWall.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        //释放资源
        mTradPlusOfferWall.destroy();
        super.onDestroy();
    }
}
