package com.tradplus.demo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tradplus.ads.mobileads.TradPlusErrorCode;
import com.tradplus.ads.mobileads.TradPlusView;

public class NativeBannerViewActivity extends AppCompatActivity implements TradPlusView.FSAdViewListener {

    private TradPlusView mTradPlusNativeBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nativebanner);

        mTradPlusNativeBanner = findViewById(R.id.native_banner);
        //创建广告位，AdUnitId是广告位ID，您需要在开发者后台申请
        //正式上线时需要替换成您申请的广告位ID
        mTradPlusNativeBanner.setAdUnitId("9F4D76E204326B16BD42FA877AFE8E7D");

        //native_banner_ad_unit.xml布局文件
        //可自定义布局，但布局文件中View对应的id不可改变；
        mTradPlusNativeBanner.setAdLayoutName("native_banner_ad_unit");

        //设置监听
        mTradPlusNativeBanner.setAdViewListener(this);
        //加载广告
        mTradPlusNativeBanner.loadAd();

    }

    @Override
    public void onAdViewLoaded(TradPlusView tradPlusView) {
        //广告加载成功
        Log.d("TradPlus","Native Banner Loaded");
    }

    @Override
    public void onAdViewFailed(TradPlusView tradPlusView, TradPlusErrorCode tradPlusErrorCode) {
        //广告加载失败
        Log.d("TradPlus","Native Banner Failed");
    }

    @Override
    public void onAdViewClicked(TradPlusView tradPlusView) {
        //广告被点击
        Log.d("TradPlus","Native Banner Clicked");
    }

    @Override
    public void onAdViewExpanded(TradPlusView tradPlusView) {

    }

    @Override
    public void onAdViewCollapsed(TradPlusView tradPlusView) {

    }

    @Override
    public void onAdsSourceLoaded(Object o) {

    }

    @Override
    protected void onDestroy() {
        //释放资源
        mTradPlusNativeBanner.destroy();
        super.onDestroy();
    }

}
