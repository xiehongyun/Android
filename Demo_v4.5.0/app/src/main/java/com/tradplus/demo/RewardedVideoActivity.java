package com.tradplus.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tradplus.ads.mobileads.TradPlusErrorCode;
import com.tradplus.ads.mobileads.TradPlusInterstitial;
import com.tradplus.ads.mobileads.TradPlusInterstitialExt;
import com.tradplus.ads.network.CanLoadListener;
import com.tradplus.ads.network.OnAllInterstatitialLoadedStatusListener;


public class RewardedVideoActivity extends AppCompatActivity implements TradPlusInterstitial.InterstitialAdListener, OnAllInterstatitialLoadedStatusListener {

    private TradPlusInterstitialExt mRewardInterstitial;
    Button reward_show,reward_load;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        tv = findViewById(R.id.tv);
        reward_load = (Button)findViewById(R.id.load);
        reward_show = (Button)findViewById(R.id.show);

        /*
         * 1、参数2：AdUnitID，TradPlus后台设置 对应广告类型的广告位（非三方广告网络的placementId）
         *          这里添加的是供测试使用的激励视频广告位，正式上线前必须替换成您申请的广告位
         *
         *          注意广告位不能填错，否则无法拿到广告
         *
         * 2、参数3：自动reload，设置true，TradPlus SDK会在三个地方帮您自动加载广告，无需手动调用load
         *           （1）初始化广告位成功的时候，setCanLoadListener的canLoad()回调中；
         *           （2）调用obj.show()返回false时 此时说明该广告位下没有广告加载成功；
         *           （3）当广告关闭，onInterstitialDismissed回调中
         *
         *          不传默认false，则您需要在上述三个地方手动调用obj.load()方法以保证有广告的填充。
         */
        mRewardInterstitial = new TradPlusInterstitialExt(this, "39DAC7EAC046676C5404004A311D1DB1");
//        mRewardInterstitial = new TradPlusInterstitialExt(this, "39DAC7EAC046676C5404004A311D1DB1",true);

        /*
         * AutoReload设为false，或者不使用；
         * 您需要设置canload监听，并在canLoad回调中进行第一次广告请求；
         * 否则不需要调用load()
         */
        mRewardInterstitial.setCanLoadListener( new CanLoadListener() {
            @Override
            public void canLoad() {
                reward_load.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 注意事项：设置自动reload，不需要在canload回调中调用load
                        mRewardInterstitial.load();
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("first Reload");
                    }
                });

            }
        });

        mRewardInterstitial.loadConfig();


        //进入广告位场景时调用
        mRewardInterstitial.confirmUWSAd();
        reward_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * 判断是否有广告加载成功：
                 * 可以直接调用obj.show(),当返回true时会自动展示广告；
                 * 也可以通过obj.isReady()判断是否有广告填充；
                 *
                 * AutoReload设为false，或者不使用；
                 * 您需要在obj.isReady()或者obj.show()返回为false的情况下调用load()
                 * 否则不需要调用
                 */

                //方式一：自动reload设置为true（推荐使用）
//                mRewardInterstitial.show();

                //方式二：：自动reload不设置或设置为false
                if (!mRewardInterstitial.show()) {
                    Log.d("TradPlus","No ads is Ready,loading...");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("no ads is ready,loading...");
                        }
                    });
                    mRewardInterstitial.load();
                }

            }
        });


//**********注意事项（3）设置监听
        //必须调用，针对每一个您添加的广告网络回调（多缓存）
        mRewardInterstitial.setInterstitialAdListener(this);

        //可选,该监听会针对整个广告位加载的结果做一个状态的返回（多缓存）
        //建议，监听该方法的false返回，当整个广告位加载失败的时候，手动load一次广告
        mRewardInterstitial.setOnAllInterstatitialLoadedStatusListener(this);
    }

    @Override
    public void onInterstitialLoaded(TradPlusInterstitial tradPlusInterstitial) {
        //广告加载成功，获取加载成功的三方广告网络名称和广告位
        if(tradPlusInterstitial != null) {
            String channelName = tradPlusInterstitial.getChannelName();
            String adUnitId = tradPlusInterstitial.getAdUnitId();
        }
        Log.d("TradPlus","RewardedVideo Loaded");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("RewardedVideo Loaded");
            }
        });
    }

    @Override
    public void onInterstitialFailed(TradPlusInterstitial tradPlusInterstitial, TradPlusErrorCode tradPlusErrorCode) {
        //广告加载失败
        Log.d("TradPlus","RewardedVideo Failed");
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
        /* 广告关闭：只要关闭广告就会回调
         * AutoReload设为false，或者不使用；
         * 则您需要在关闭广告的情况下调用load()，重新加载广告以保证填充率。
         * AutoReload设为true，则不需要调用
         */
//        mRewardInterstitial.load();
    }

    @Override
    public void onInterstitialRewarded(TradPlusInterstitial tradPlusInterstitial, String s, int i) {
        //奖励，当视频播放完毕，会回调奖励方法
    }

    @Override
    public void onLoadStatus(boolean b, String s) {
        //设置可选监听，当缓存广告网络全部加载结束，该方法才会被调用；
        //isLoadedSuccess ture表明单次广告源加载全部完毕，并且有广告源加载成功；
        //false为全部的广告网络加载失败；
        //unitId 是广告位ID
        //建议，监听该方法的false返回，当整个广告位加载失败的时候，手动load一次广告
        Log.d("TradPlus","RewardedVideo onLoadStatus = "+ b);
        if (!b) {
            mRewardInterstitial.load();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!b) {
                    tv.setText("All ads failed" );
                }
            }
        });

    }

    @Override
    protected void onResume() {
        mRewardInterstitial.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        mRewardInterstitial.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        //释放资源
        mRewardInterstitial.destroy();
        super.onDestroy();
    }
}
