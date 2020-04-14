package com.tradplus.tradplusdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.tradplus.ads.mobileads.TradPlusErrorCode;
import com.tradplus.ads.mobileads.TradPlusView;

public class NativeActivity extends AppCompatActivity implements TradPlusView.FSAdViewListener {

    private TradPlusView mTradPlusNative;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        RelativeLayout main_layout = (RelativeLayout)findViewById(R.id.activity_native);
        main_layout.setBackgroundColor(Color.parseColor("#E6E6FA"));

        mTradPlusNative = findViewById(R.id.NativeView);
        //创建广告位，AdUnitId是广告位ID，您需要在开发者后台申请
        //DDBF26FBDA47FBE2765F1A089F1356BF是专门的测试广告位Id
        //正式上线时需要替换成您申请的广告位ID
        mTradPlusNative.setAdUnitId("DDBF26FBDA47FBE2765F1A089F1356BF");
        //设置广告尺寸，可以选择在布局文件中设置
        //单位：dp，宽度：320左右 高度：300～480
        mTradPlusNative.setAdSize(320, 340);

        //native_ad_list_item.xml布局文件
        //可自定义布局，但布局文件中View对应的id不可改变；
        mTradPlusNative.setAdLayoutName("native_ad_list_item");

        //video_ad_item.xml只用于mopub的原生视频，如您不需要接入该类型，则无需添加；
        //可自定义布局，但布局文件中View对应的id不可改变；切勿调换位置
//				mTradPlusNative.setAdLayoutName("native_ad_list_item","video_ad_item");

        //设置监听
        mTradPlusNative.setAdViewListener(this);
        //加载广告
        mTradPlusNative.loadAd();

    }

    @Override
    public void onAdViewLoaded(TradPlusView tradPlusView) {
        //广告加载成功，获取加载成功的三方广告网络名称和广告位
        String channelName = tradPlusView.getChannelName();
        String adUnitId = tradPlusView.getAdUnitId();
        Log.d("TradPlus","Native Loaded");
    }

    @Override
    public void onAdViewFailed(TradPlusView tradPlusView, TradPlusErrorCode tradPlusErrorCode) {
        //广告加载失败，获取加载失败的三方三方广告网络名称和广告位
        String channelName = tradPlusView.getChannelName();
        String adUnitId = tradPlusView.getAdUnitId();
        Log.d("TradPlus","Native Failed");
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
        mTradPlusNative.destroy();
        super.onDestroy();
    }

}
