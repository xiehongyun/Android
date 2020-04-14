package com.flute.ads.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.flute.ads.common.DataKeys;
import com.flute.ads.common.util.Views;
import com.flute.ads.mobileads.CustomEventAdView;
import com.flute.ads.mobileads.Flute;
import com.flute.ads.mobileads.FluteErrorCode;
import com.flute.ads.mopub.NativeImageHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacebookNativeAdvanced extends CustomEventAdView implements AdListener, NativeAdListener {
    public static final String PLACEMENT_ID_KEY = "placementId";

    private NativeAd mFacebookNative;
    private CustomEventAdViewListener mNativeListener;

    private WeakReference<Context> mContextWeakReference;

    private long mAdLoadTimeStamp;
    private ArrayList<HashMap<String, String>> mAdEvents;
    private String mAdUnitId, mAdLayoutName;
    private String mParams;

    private RelativeLayout mNativeAdView;
    private int mAdWidth;
    private int mAdHeight;
    private float density;

    private boolean onlyCtaBtnAvailable;

    @Override
    protected void loadAdView(final Context context,
            final CustomEventAdViewListener customEventNativeListener,
            final Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {
        mNativeListener = customEventNativeListener;
        mContextWeakReference = new WeakReference(context);

        AudienceNetworkAds.initialize(context);
        AudienceNetworkAds.isInAdsProcess(context);
        final String placementId;
        if (serverExtrasAreValid(serverExtras)) {
            placementId = serverExtras.get(PLACEMENT_ID_KEY);
        } else {
            mNativeListener.onAdViewFailed(FluteErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mAdUnitId = (String)localExtras.get(DataKeys.AD_UNIT_ID_KEY);
        mAdLayoutName = (String)localExtras.get(DataKeys.AD_LAYOUT_NAME);
        mParams = serverExtras.toString();

        mAdLoadTimeStamp = System.currentTimeMillis();
        mAdEvents = new ArrayList<>();
        mAdEvents.add(Flute.getDebugEvent(mAdLoadTimeStamp, "loadAd", ""));

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        density = context.getResources().getDisplayMetrics().density;

        if (localExtrasAreValid(localExtras)) {
            mAdWidth = (Integer) localExtras.get(DataKeys.AD_WIDTH);
            mAdHeight = (Integer) localExtras.get(DataKeys.AD_HEIGHT);
        } else {
            mNativeListener.onAdViewFailed(FluteErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        if (mAdLayoutName != null && mAdLayoutName.length() > 0) {
            int layoutId = context.getResources().getIdentifier(mAdLayoutName, "layout", context.getPackageName());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mNativeAdView = (RelativeLayout) inflater.inflate(layoutId, null);
        } else {
            mNativeListener.onAdViewFailed(FluteErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        AdSettings.addTestDevice("ee1cc351-69e0-40ce-a866-fb91a937778c");

        mFacebookNative = new NativeAd(context, placementId);
        mFacebookNative.setAdListener(this);

        onlyCtaBtnAvailable = false;

        mFacebookNative.loadAd();
    }

    @Override
    protected void onInvalidate() {

        AdSettings.clearTestDevices();

        if (mNativeAdView != null) {
            mNativeAdView.removeAllViews();
            Views.removeFromParent(mNativeAdView);
            mNativeAdView = null;
        }

        if (mFacebookNative != null) {
            mFacebookNative.unregisterView();
            mFacebookNative.setAdListener(null);
            mFacebookNative.destroy();
            mFacebookNative = null;
        }

        Flute.sendDebugLog("native", "facebook", mAdUnitId, mParams, mAdLoadTimeStamp, mAdEvents.toString());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        Log.d("Flute", "Facebook native ad loaded successfully. Showing ad...");
        mFacebookNative.unregisterView();

        setFaceBookAdContent(mFacebookNative);

        mNativeListener.onAdViewLoaded(mNativeAdView);

        mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdLoaded", ""));
    }

    @Override
    public void onError(final Ad ad, final AdError error) {
        Log.d("Flute", "Facebook native ad failed to load.");
        if (error == AdError.NO_FILL) {
            mNativeListener.onAdViewFailed(FluteErrorCode.NETWORK_NO_FILL);
        } else if (error == AdError.INTERNAL_ERROR) {
            mNativeListener.onAdViewFailed(FluteErrorCode.NETWORK_INVALID_STATE);
        } else {
            mNativeListener.onAdViewFailed(FluteErrorCode.UNSPECIFIED);
        }

        mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onError", error.getErrorCode() + " : " + error.getErrorMessage()));
    }

    @Override
    public void onAdClicked(Ad ad) {
        Log.d("Flute", "Facebook native ad clicked.");
        mNativeListener.onAdViewClicked();

        mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdClicked", ""));
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        Log.d("Flute", "Facebook native ad onLoggingImpression.");
        mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onLoggingImpression", ""));
    }    

    private boolean serverExtrasAreValid(final Map<String, String> serverExtras) {
        final String placementId = serverExtras.get(PLACEMENT_ID_KEY);
        return (placementId != null && placementId.length() > 0);
    }

    //TODO 设置facebook广告栏内容
    private void setFaceBookAdContent(NativeAd nativeAd) {

        if (mContextWeakReference.get() == null) {
            return;
        }

        Context context = mContextWeakReference.get();

        int native_title_id = context.getResources().getIdentifier("native_title", "id", context.getPackageName());
        TextView nativeAdTitle = (TextView) mNativeAdView.findViewById(native_title_id);
        int native_text_id = context.getResources().getIdentifier("native_text", "id", context.getPackageName());
        TextView nativeAdBody = (TextView) mNativeAdView.findViewById(native_text_id);
        int native_cta_btn_id = context.getResources().getIdentifier("native_cta_btn", "id", context.getPackageName());
        Button nativeAdCallToAction = (Button) mNativeAdView.findViewById(native_cta_btn_id);
        int native_cta_text_id = context.getResources().getIdentifier("native_cta_text", "id", context.getPackageName());
        TextView nativeAdSocialContext = (TextView)mNativeAdView.findViewById(native_cta_text_id);

        int native_icon_image_id = context.getResources().getIdentifier("native_icon_image", "id", context.getPackageName());
        ImageView nativeAdIcon = (ImageView) mNativeAdView.findViewById(native_icon_image_id);
        int native_main_image_id = context.getResources().getIdentifier("native_main_image", "id", context.getPackageName());
        RelativeLayout nativeAdImage = (RelativeLayout) mNativeAdView.findViewById(native_main_image_id);

        com.facebook.ads.MediaView nativeAdMedia = new com.facebook.ads.MediaView(context);
        RelativeLayout.LayoutParams media_lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        nativeAdImage.addView(nativeAdMedia, media_lp);

        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdSocialContext.setText(nativeAd.getAdCallToAction());
        nativeAdSocialContext.setVisibility(
                nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());

        NativeAd.Image adIcon = nativeAd.getAdIcon();

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(
                mNativeAdView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
//        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

//        nativeAdMedia.setNativeAd(nativeAd);

        com.facebook.ads.AdChoicesView adChoicesView = new com.facebook.ads.AdChoicesView(context, nativeAd, true);
        RelativeLayout.LayoutParams adchoicesview_lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adchoicesview_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adchoicesview_lp.setMargins((int)(mAdWidth * density * 0.9), 0, 0, 0);
        mNativeAdView.addView(adChoicesView, adchoicesview_lp);

        ImageView nativeAdLabel = new ImageView(context);
        RelativeLayout.LayoutParams nativeAdLabel_lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nativeAdLabel_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        nativeAdLabel_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nativeAdLabel_lp.setMargins((int)(10 * density), 0, 0, 0);
        mNativeAdView.addView(nativeAdLabel, nativeAdLabel_lp);

        NativeImageHelper.loadImageView(Flute.adexpressAdLable, nativeAdLabel);

//		if (onlyCtaBtnAvailable) {
//			List<View> clickableViews = new ArrayList<>();
//			clickableViews.add(nativeAdCallToAction);
//			nativeAd.registerViewForInteraction(mNativeAdView, clickableViews);
//		} else {
//			nativeAd.registerViewForInteraction(mNativeAdView);
//		}
    }

    private boolean localExtrasAreValid(@NonNull final Map<String, Object> localExtras) {
        return localExtras.get(DataKeys.AD_WIDTH) instanceof Integer
                && localExtras.get(DataKeys.AD_HEIGHT) instanceof Integer;
    }

    @Override
    public void onMediaDownloaded(Ad ad) {

    }
}
