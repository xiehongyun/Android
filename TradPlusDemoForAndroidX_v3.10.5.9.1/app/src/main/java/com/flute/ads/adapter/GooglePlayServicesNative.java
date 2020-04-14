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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.flute.ads.common.DataKeys;
import com.flute.ads.common.util.Views;
import com.flute.ads.mobileads.CustomEventAdView;
import com.flute.ads.mobileads.Flute;
import com.flute.ads.mobileads.FluteErrorCode;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GooglePlayServicesNative extends CustomEventAdView {

    public static final String AD_UNIT_ID_KEY = "placementId";
    public static final String APP_ID_KEY = "appId";

    private WeakReference<Context> mContextWeakReference;

    private CustomEventAdViewListener mNativeListener;
    private AdLoader mAdLoader;
    private NativeAppInstallAdView mNativeAppInstallAdView;
    private NativeContentAdView mNativeContentAdView;
    private Boolean mIsContentAdView = false;
    private RelativeLayout mNativeAdView, mNativeAdViewEx;
    private int mAdWidth, mAdHeight;
    private float density;

    private long mAdLoadTimeStamp;
    private ArrayList<HashMap<String, String>> mAdEvents;
    private String mAdUnitId, mAdLayoutName, mAdLayoutNameEx;
    private String mParams;

    @Override
    protected void loadAdView(
            final Context context,
            final CustomEventAdViewListener customEventNativeListener,
            final Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {
        mNativeListener = customEventNativeListener;
        mContextWeakReference = new WeakReference(context);

        final String adUnitId;
        final String adAppId;

        adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
        adAppId = serverExtras.get(APP_ID_KEY);

        mAdUnitId = (String)localExtras.get(DataKeys.AD_UNIT_ID_KEY);
        mAdLayoutName = (String)localExtras.get(DataKeys.AD_LAYOUT_NAME);
        mAdLayoutNameEx = (String)localExtras.get(DataKeys.AD_LAYOUT_NAME_EX);
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

        if (mAdLayoutNameEx != null && mAdLayoutNameEx.length() > 0) {
            int layoutId = context.getResources().getIdentifier(mAdLayoutNameEx, "layout", context.getPackageName());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mNativeAdViewEx = (RelativeLayout) inflater.inflate(layoutId, null);
        } else {
            mNativeAdViewEx = null;
        }

        mNativeAppInstallAdView = new NativeAppInstallAdView(context);
        mNativeContentAdView = new NativeContentAdView(context);

        if (adAppId != null && adAppId.length() > 0) {
            MobileAds.initialize(context,adAppId);
        }

        AdLoader.Builder builder = new AdLoader.Builder(context, adUnitId);
        
        builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
            @Override
            public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                Log.d("Flute", "Google Play Services native ad onAppInstallAdLoaded.");

                try {
                    RelativeLayout.LayoutParams myadview_lp = new RelativeLayout.LayoutParams((int) (mAdWidth * density), (int) (mAdHeight * density));
                    if (mNativeAdViewEx != null) {
                        mNativeAppInstallAdView.addView(mNativeAdViewEx, myadview_lp);
                        populateAppInstallAdView(ad, mNativeAppInstallAdView, false);
                    } else {
                        mNativeAppInstallAdView.addView(mNativeAdView, myadview_lp);
                        populateAppInstallAdView(ad, mNativeAppInstallAdView, true);
                    }

                    if (mNativeListener != null) {
                        mNativeListener.onAdViewLoaded(mNativeAppInstallAdView);
                        mIsContentAdView = false;
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }

                mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdLoaded", ""));
            }
        });

        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {
                Log.d("Flute", "Google Play Services native ad onContentAdLoaded.");

                try {
                    RelativeLayout.LayoutParams myadview_lp = new RelativeLayout.LayoutParams((int) (mAdWidth * density), (int) (mAdHeight * density));
                    mNativeContentAdView.addView(mNativeAdView, myadview_lp);
                    populateContentAdView(ad, mNativeContentAdView);

                    if (mNativeListener != null) {
                        mNativeListener.onAdViewLoaded(mNativeContentAdView);
                        mIsContentAdView = true;
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }

                mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdLoaded", ""));
            }
        });

        mAdLoader = builder.withAdListener(new AdViewListener()).build();

//        final AdRequest adRequest = new AdRequest.Builder().build();
      final AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice("E4922E7C16241B536BE5A7CE33BD50B2")
            .build();

        try {
            mAdLoader.loadAd(adRequest);
        } catch (NoClassDefFoundError e) {
            mNativeListener.onAdViewFailed(FluteErrorCode.NETWORK_NO_FILL);
        }
    }

    @Override
    protected void onInvalidate() {
        if (mIsContentAdView) {
            Views.removeFromParent(mNativeContentAdView);
        } else {
            Views.removeFromParent(mNativeAppInstallAdView);
        }
        if (mNativeContentAdView != null) {
            mNativeContentAdView.removeAllViews();
            mNativeContentAdView.destroy();
        }
        if (mNativeAppInstallAdView != null) {
            mNativeAppInstallAdView.removeAllViews();
            mNativeAppInstallAdView.destroy();
        }

        Flute.sendDebugLog("native", "admob", mAdUnitId, mParams, mAdLoadTimeStamp, mAdEvents.toString());
    }

    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd, NativeAppInstallAdView adView, boolean mixMode) {

        if (mContextWeakReference.get() == null) {
            return;
        }

        Context context = mContextWeakReference.get();

        int native_title_id = context.getResources().getIdentifier("native_title", "id", context.getPackageName());
        adView.setHeadlineView(adView.findViewById(native_title_id));
        int native_text_id = context.getResources().getIdentifier("native_text", "id", context.getPackageName());
        adView.setBodyView(adView.findViewById(native_text_id));
        int native_cta_btn_id = context.getResources().getIdentifier("native_cta_btn", "id", context.getPackageName());
        adView.setCallToActionView(adView.findViewById(native_cta_btn_id));
        int native_cta_text_id = context.getResources().getIdentifier("native_cta_text", "id", context.getPackageName());
        TextView nativeAdCtaText = (TextView)adView.findViewById(native_cta_text_id);

        if (!mixMode) {
            int native_star_id = context.getResources().getIdentifier("native_star", "id", context.getPackageName());
            adView.setStarRatingView(adView.findViewById(native_star_id));
            int native_price_id = context.getResources().getIdentifier("native_price", "id", context.getPackageName());
            adView.setPriceView(adView.findViewById(native_price_id));
            int native_store_id = context.getResources().getIdentifier("native_store", "id", context.getPackageName());
            adView.setStoreView(adView.findViewById(native_store_id));
        }

        int native_icon_image_id = context.getResources().getIdentifier("native_icon_image", "id", context.getPackageName());
        adView.setIconView(adView.findViewById(native_icon_image_id));
        int native_main_image_id = context.getResources().getIdentifier("native_main_image", "id", context.getPackageName());
        RelativeLayout nativeAdImage = (RelativeLayout) mNativeAdView.findViewById(native_main_image_id);

        ImageView nativeAdMedia = new ImageView(context);
        nativeAdMedia.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams media_lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        nativeAdImage.addView(nativeAdMedia, media_lp);
        adView.setImageView(nativeAdMedia);

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        nativeAdCtaText.setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
                .getDrawable());

        List<NativeAd.Image> images = nativeAppInstallAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        if (!mixMode) {
            if (nativeAppInstallAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
            }

            if (nativeAppInstallAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
            }

            if (nativeAppInstallAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAppInstallAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    private void populateContentAdView(NativeContentAd nativeContentAd, NativeContentAdView adView) {

        if (mContextWeakReference.get() == null) {
            return;
        }

        Context context = mContextWeakReference.get();

        int native_title_id = context.getResources().getIdentifier("native_title", "id", context.getPackageName());
        adView.setHeadlineView(adView.findViewById(native_title_id));
        int native_text_id = context.getResources().getIdentifier("native_text", "id", context.getPackageName());
        adView.setBodyView(adView.findViewById(native_text_id));
        int native_cta_btn_id = context.getResources().getIdentifier("native_cta_btn", "id", context.getPackageName());
        adView.setCallToActionView(adView.findViewById(native_cta_btn_id));
        int native_cta_text_id = context.getResources().getIdentifier("native_cta_text", "id", context.getPackageName());
        TextView nativeAdCtaText = (TextView)adView.findViewById(native_cta_text_id);

        int native_icon_image_id = context.getResources().getIdentifier("native_icon_image", "id", context.getPackageName());
        adView.setLogoView(adView.findViewById(native_icon_image_id));
        int native_main_image_id = context.getResources().getIdentifier("native_main_image", "id", context.getPackageName());
        RelativeLayout nativeAdImage = (RelativeLayout) mNativeAdView.findViewById(native_main_image_id);

        ImageView nativeAdMedia = new ImageView(context);
        nativeAdMedia.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams media_lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        nativeAdImage.addView(nativeAdMedia, media_lp);
        adView.setImageView(nativeAdMedia);

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        nativeAdCtaText.setText(nativeContentAd.getCallToAction());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    private class AdViewListener extends AdListener {
        @Override
        public void onAdClosed() {
            mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdClosed", ""));
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            Log.d("Flute", "Google Play Services native ad failed to load.");
            if (mNativeListener != null) {
                mNativeListener.onAdViewFailed(FluteErrorCode.NETWORK_NO_FILL);
            }

            mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdFailedToLoad", "errorCode : " + errorCode));
        }

        @Override
        public void onAdLeftApplication() {
            mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdLeftApplication", ""));
        }

        @Override
        public void onAdLoaded() {
            Log.d("Flute", "Google Play Services native ad loaded successfully. Showing ad...");
        }

        @Override
        public void onAdOpened() {
            Log.d("Flute", "Google Play Services native ad clicked.");
            if (mNativeListener != null) {
                mNativeListener.onAdViewClicked();
            }

            mAdEvents.add(Flute.getDebugEvent(System.currentTimeMillis(), "onAdOpened", ""));
        }
    }

    private boolean localExtrasAreValid(@NonNull final Map<String, Object> localExtras) {
        return localExtras.get(DataKeys.AD_WIDTH) instanceof Integer
                && localExtras.get(DataKeys.AD_HEIGHT) instanceof Integer;
    }
}
