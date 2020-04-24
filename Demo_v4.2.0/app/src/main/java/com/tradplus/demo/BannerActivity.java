package com.tradplus.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tradplus.ads.mobileads.TradPlusErrorCode;
import com.tradplus.ads.mobileads.TradPlusView;


public class BannerActivity extends AppCompatActivity implements TradPlusView.FSAdViewListener {

    private TradPlusView mTradPlusView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        mTradPlusView = findViewById(R.id.BannerView);
        //创建广告位，AdUnitId是广告位ID，您需要在开发者后台申请
        //A24091715B4FCD50C0F2039A5AF7C4BB是专门的测试广告位Id
        //正式上线时需要替换成您申请的广告位ID
        mTradPlusView.setAdUnitId("A24091715B4FCD50C0F2039A5AF7C4BB");
        //设置广告尺寸，可以选择在布局文件中设置；单位：dp
        mTradPlusView.setAdSize(320, 50);
        mTradPlusView.setAdViewListener(this);
        //加载广告
        mTradPlusView.loadAd();
    }

    @Override
    public void onAdViewLoaded(TradPlusView tradPlusView) {
        //广告加载成功，获取加载成功的三方广告网络名称和广告位
        String channelName = tradPlusView.getChannelName();
        String adUnitId = tradPlusView.getAdUnitId();
        Log.d("TradPlus","Banner Loaded");
    }

    @Override
    public void onAdViewFailed(TradPlusView tradPlusView, TradPlusErrorCode tradPlusErrorCode) {
        //广告加载失败，获取加载失败的三方三方广告网络名称和广告位
        String channelName = tradPlusView.getChannelName();
        String adUnitId = tradPlusView.getAdUnitId();
        Log.d("TradPlus","Banner Failed");
    }

    @Override
    public void onAdViewClicked(TradPlusView tradPlusView) {
        //广告被点击
        Log.d("TradPlus","Native Clicked");
    }

    @Override
    public void onAdViewExpanded(TradPlusView tradPlusView) {

    }

    @Override
    public void onAdViewCollapsed(TradPlusView tradPlusView) {

    }

    @Override
    protected void onDestroy() {
        //释放资源
        mTradPlusView.destroy();
        super.onDestroy();
    }

}
