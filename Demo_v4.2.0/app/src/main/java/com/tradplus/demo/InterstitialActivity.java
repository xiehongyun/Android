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


public class InterstitialActivity extends AppCompatActivity implements TradPlusInterstitial.InterstitialAdListener, OnAllInterstatitialLoadedStatusListener {

    private TradPlusInterstitialExt mTradPlusInterstitial;
    Button interstitial_show;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        tv = findViewById(R.id.tv);
        interstitial_show = (Button)findViewById(R.id.show);

        //创建广告对象；设置广告位，AdUnitId是广告位Id，开发者后台添加
        //E609A0A67AF53299F2176C3A7783C46D是专门的测试广告位Id
        //正式上线时需要替换成您申请的广告位ID
        mTradPlusInterstitial = new TradPlusInterstitialExt(this, "E609A0A67AF53299F2176C3A7783C46D");
        //设置canload监听，确保在广告位初始化成功的时候进行广告的第一次加载
        mTradPlusInterstitial.setCanLoadListener( new CanLoadListener() {
            @Override
            public void canLoad() {
//**********注意事项（1）第一次加载广告
                mTradPlusInterstitial.load();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("first loading......");
                    }
                });

            }
        });
        mTradPlusInterstitial.loadConfig();

        interstitial_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否有广告加载成功
                if (mTradPlusInterstitial.isReady()) {
                    //展示广告
                    mTradPlusInterstitial.show();
                }else {
//**********注意事项（2）重新加载广告
                    Log.d("TradPlus","No ads is Ready,loading...");
                    mTradPlusInterstitial.load();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("no ads is ready,loading...");
                        }
                    });
                }

            }
        });

//**********注意事项（3）设置监听
        //必须调用，针对每一个您添加的广告网络回调（多缓存）
        mTradPlusInterstitial.setInterstitialAdListener(this);

        //可选,该监听会针对整个广告位加载的结果做一个状态的返回（多缓存）
        mTradPlusInterstitial.setOnAllInterstatitialLoadedStatusListener(this);
    }

    @Override
    public void onInterstitialLoaded(TradPlusInterstitial tradPlusInterstitial) {
        //广告加载成功，获取加载成功的三方广告网络名称和广告位
        String channelName = tradPlusInterstitial.getChannelName();
        String adUnitId = tradPlusInterstitial.getAdUnitId();
        Log.d("TradPlus","Interstitial Loaded");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("Interstitial Loaded");
            }
        });
    }

    @Override
    public void onInterstitialFailed(TradPlusInterstitial tradPlusInterstitial, TradPlusErrorCode tradPlusErrorCode) {
        //广告加载成功，获取加载成功的三方广告网络名称和广告位
        String channelName = tradPlusInterstitial.getChannelName();
        String adUnitId = tradPlusInterstitial.getAdUnitId();
        Log.d("TradPlus","Interstitial Failed");
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
        mTradPlusInterstitial.load();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("Interstitial is closed , loading again...");
            }
        });
    }

    @Override
    public void onInterstitialRewarded(TradPlusInterstitial tradPlusInterstitial, String s, int i) {
        //集成插屏广告中不使用该方法
    }

    @Override
    public void onLoadStatus(boolean b, String s) {
        //设置可选监听，当缓存广告网络全部加载结束，该方法才会被调用；
        //isLoadedSuccess ture表明单次广告源加载全部完毕，并且有广告源加载成功；
        //false为全部的广告网络加载失败；
        //unitId 是广告位ID
        Log.d("TradPlus","Interstitial onLoadStatus = "+ b);
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
        mTradPlusInterstitial.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        mTradPlusInterstitial.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        //释放资源
        mTradPlusInterstitial.destroy();
        super.onDestroy();
    }
}
