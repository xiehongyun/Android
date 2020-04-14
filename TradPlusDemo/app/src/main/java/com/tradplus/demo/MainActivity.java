package com.tradplus.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.flute.ads.mobileads.FluteErrorCode;
import com.flute.ads.mobileads.FluteInterstitial;
import com.flute.ads.mobileads.FluteInterstitialExt;
import com.flute.ads.mobileads.FluteView;
import com.flute.ads.network.CanLoadListener;
import com.flute.ads.network.OnAllInterstatitialLoadedStatusListener;


public class MainActivity extends AppCompatActivity {


    FluteView mFluteViewBanner ,mFluteNativeView;
    FluteInterstitialExt mInterstitial,mRewardedVideo;
    Button interstitial_load, interstitial_show,reward_load,reward_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Request Banner
        requestBanner();
        //Request Native
        requestNative();
        //Request Interstitial
        requestInterstitial();
        //Request RewardVideo
        requestRewardVideo();


    }

    private void requestRewardVideo() {
        reward_load = findViewById(R.id.reward_load);
        mRewardedVideo = new FluteInterstitialExt(this, "39DAC7EAC046676C5404004A311D1DB1", new CanLoadListener() {
            @Override
            public void canLoad() {

                reward_load.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mRewardedVideo.setOnAllInterstatitialLoadedStatusListener(new OnAllInterstatitialLoadedStatusListener() {
                            @Override
                            public void onLoadStatus(boolean isLoadedSuccess, String unitId) {
                                Log.d("TradPlus","All Rewardedvideo ads finish loaded.");

                            }
                        });

                        mRewardedVideo.setInterstitialAdListener(new FluteInterstitial.InterstitialAdListener() {
                            @Override
                            public void onInterstitialLoaded(FluteInterstitial fluteInterstitial) {
                                String channelName = fluteInterstitial.getChannelName();
                                Log.d("TradPlus","Rewardedvideo ads loaded. ChannelName: " + channelName );
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Rewardedvideo ads loaded." , Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                            @Override
                            public void onInterstitialFailed(final FluteInterstitial fluteInterstitial, final FluteErrorCode tradPlusErrorCode) {
                                String channelName = fluteInterstitial.getChannelName();
                                Log.d("TradPlus","Rewardedvideo ads failed. ChannelName: " + channelName );

                            }

                            @Override
                            public void onInterstitialShown(final FluteInterstitial fluteInterstitial) {
                                Log.d("TradPlus","Rewardedvideo ads displayed");
                            }

                            @Override
                            public void onInterstitialClicked(FluteInterstitial fluteInterstitial) {
                                Log.d("TradPlus","Rewardedvideo ads clicked");
                            }

                            @Override
                            public void onInterstitialDismissed(FluteInterstitial fluteInterstitial) {
                                Log.d("TradPlus","Rewardedvideo ads closed.");
                            }

                            @Override
                            public void onInterstitialRewarded(FluteInterstitial fluteInterstitial, final String currencyName,final int amount) {
                                Log.d("TradPlus","Rewardedvideo ads onVideoComplete.");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (currencyName != null) {
                                            Toast.makeText(MainActivity.this, "onInterstitialRewarded : " + currencyName + " : " + amount, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });

                        mRewardedVideo.load();
                    }
                });
            }
        });

        reward_show = findViewById(R.id.reward_show);
        reward_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideo.isReady()) {
                    mRewardedVideo.show();
                }else{
                    Log.d("TradPlus","Rewardvideo ads not Ready");
                }
            }

        });
    }



    private void requestInterstitial() {
        interstitial_load = findViewById(R.id.interstitial_load);
        mInterstitial = new FluteInterstitialExt(this, "E609A0A67AF53299F2176C3A7783C46D", new CanLoadListener() {
            @Override
            public void canLoad() {
                interstitial_load.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mInterstitial.setOnAllInterstatitialLoadedStatusListener(new OnAllInterstatitialLoadedStatusListener() {
                            @Override
                            public void onLoadStatus(boolean isLoadedSuccess, String unitId) {
                                Log.d("TradPlus","All Interstitial ads finish loaded.");
                            }
                        });
                        mInterstitial.setInterstitialAdListener(new FluteInterstitial.InterstitialAdListener() {
                            @Override
                            public void onInterstitialLoaded(final FluteInterstitial fluteInterstitial) {
                                String channelName = fluteInterstitial.getChannelName();
                                Log.d("TradPlus","Interstitial ads loaded. ChannelName: " + channelName );
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Interstitial ads loaded." , Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onInterstitialFailed(FluteInterstitial fluteInterstitial, FluteErrorCode fluteErrorCode) {
                                String channelName = fluteInterstitial.getChannelName();
                                Log.d("TradPlus","Interstitial ads failed. ChannelName: " + channelName );

                            }

                            @Override
                            public void onInterstitialShown(FluteInterstitial fluteInterstitial) {
                                Log.d("TradPlus","Interstitial ads displayed");
                            }

                            @Override
                            public void onInterstitialClicked(FluteInterstitial fluteInterstitial) {
                                Log.d("TradPlus","Interstitial ads clicked");
                            }

                            @Override
                            public void onInterstitialDismissed(final FluteInterstitial fluteInterstitial) {
                                Log.d("TradPlus","Interstitial ads closed");
                            }

                            //Don't use it when you need Interstitial Ads
                            @Override
                            public void onInterstitialRewarded(FluteInterstitial fluteInterstitial, String s, int i) {

                            }
                        });
                        mInterstitial.load();
                    }
                });


            }
        });

        interstitial_show = findViewById(R.id.interstitial_show);
        interstitial_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitial.isReady()) {
                    mInterstitial.show();
                }else {
                    Log.d("TradPlus","Interstitial ads not Ready");
                }
            }
        });

    }

    private void requestNative() {
        Button native_ad_advanced_1 = (Button)findViewById(R.id.native_ad_advanced);
        native_ad_advanced_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFluteNativeView = findViewById(R.id.NativeView);
                mFluteNativeView.setAdUnitId("DDBF26FBDA47FBE2765F1A089F1356BF");
                mFluteNativeView.setAdLayoutName("native_ad_list_item");
                mFluteNativeView.setAdViewListener(new FluteView.FSAdViewListener() {
                    @Override
                    public void onAdViewLoaded(FluteView fluteView) {
                        String channelName = fluteView.getChannelName();
                        String adUnitId = fluteView.getAdUnitId();
                        Log.d("TradPlus","Native Loaded!  ChannelName: " + channelName + ", adUnitId: "+  adUnitId);
                    }

                    @Override
                    public void onAdViewFailed(FluteView fluteView, FluteErrorCode fluteErrorCode) {
                        String channelName = fluteView.getChannelName();
                        String adUnitId = fluteView.getAdUnitId();
                        Log.d("TradPlus","Native Failed!  ChannelName: " + channelName + ", adUnitId: "+  adUnitId);
                    }

                    @Override
                    public void onAdViewClicked(FluteView fluteView) {
                        Log.d("TradPlus","Native Clicked!");
                    }

                    @Override
                    public void onAdViewExpanded(FluteView fluteView) {

                    }

                    @Override
                    public void onAdViewCollapsed(FluteView fluteView) {

                    }
                });

                mFluteNativeView.loadAd();

            }
        });
    }

    private void requestBanner() {
        Button banner_btn = (Button)findViewById(R.id.banner_ad);
        banner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFluteViewBanner = findViewById(R.id.BannerView);
                mFluteViewBanner.setAdUnitId("A24091715B4FCD50C0F2039A5AF7C4BB");
                mFluteViewBanner.setAdViewListener(new FluteView.FSAdViewListener() {
                    @Override
                    public void onAdViewLoaded(FluteView fluteView) {
                        String channelName = fluteView.getChannelName();
                        String adUnitId = fluteView.getAdUnitId();
                        Log.d("TradPlus","Banner Loaded!  ChannelName: " + channelName + ", adUnitId: "+  adUnitId);
                    }

                    @Override
                    public void onAdViewFailed(FluteView fluteView, FluteErrorCode fluteErrorCode) {
                        String channelName = fluteView.getChannelName();
                        String adUnitId = fluteView.getAdUnitId();
                        Log.d("TradPlus","Banner Failed!  ChannelName: " + channelName + ", adUnitId: "+  adUnitId);
                    }

                    @Override
                    public void onAdViewClicked(FluteView fluteView) {
                        Log.d("TradPlus","Banner Clicked!");
                    }

                    @Override
                    public void onAdViewExpanded(FluteView fluteView) {

                    }

                    @Override
                    public void onAdViewCollapsed(FluteView fluteView) {

                    }
                });
                mFluteViewBanner.loadAd();
            }
        });
    }


    @Override
    protected void onResume() {
        if (mInterstitial != null) {
            mInterstitial.onResume();
        }
        if(mRewardedVideo != null) {
            mRewardedVideo.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mInterstitial != null) {
            mInterstitial.onPause();
        }
        if(mRewardedVideo != null) {
            mRewardedVideo.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mFluteNativeView != null) {
            mFluteNativeView.destroy();
        }
        if (mFluteViewBanner != null) {
            mFluteViewBanner.destroy();
        }
        if (mInterstitial != null) {
            mInterstitial.destroy();
        }
        if(mRewardedVideo != null) {
            mRewardedVideo.destroy();
        }
        super.onDestroy();
    }
}
