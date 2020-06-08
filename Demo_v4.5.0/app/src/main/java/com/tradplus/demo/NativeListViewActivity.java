package com.tradplus.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tradplus.ads.common.util.LogUtil;
import com.tradplus.ads.mobileads.TradPlusErrorCode;
import com.tradplus.ads.mobileads.TradPlusSlot;
import com.tradplus.ads.mobileads.TradPlusView;
import com.tradplus.ads.mobileads.TradPlusViewExt;
import com.tradplus.ads.mobileads.util.TradPlusListNativeOption;

import java.util.ArrayList;
import java.util.List;


public class NativeListViewActivity extends AppCompatActivity {

    private TradPlusListNativeOption option;
    private RadioGroup mRadioGroupManager;
    private RadioGroup mRadioGroupOri;
    private int mScrollOrientation = RecyclerView.VERTICAL;
    private RecyclerView mListView;
    private List<TradPlusView> mData;
    private TPAdapter myAdapter;
    private Button btn_add;

    private static int LIST_ITEM_COUNT = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nativelistview);


        //参数一interval，表示间隔
        //参数二maxLength 表示当前列表可支持的最大行数
        option = new TradPlusListNativeOption(6,50);
        LIST_ITEM_COUNT = option.getMaxLength();

        initListView();
    }

    private void loadListAd(){
        /*
         *  AdUnitID，TradPlus后台设置对应广告类型的广告位（非三方广告网络的placementId）
         *          这里添加的是供测试使用的原生广告位，正式上线前必须替换成您申请的广告位
         *
         *          注意广告位不能填错，否则无法拿到广告
         *
         * native_ad_list_item.xml 和 video_ad_list_item.xml布局文件，您可以在Demo中或者下载下来的zip文件中获取
         * 可自定义布局，但布局文件中View对应的id不可改变
         *
         * 每次最优获取5条广告
         */
        TradPlusSlot tradPlusSlot = new TradPlusSlot.Builder()
                .setUnitId("DDBF26FBDA47FBE2765F1A089F1356BF")
                .setLayoutName("native_ad_list_item")
                .setLayoutNameEx("video_ad_list_item")
                .setAdCount(5)
                .build();


        TradPlusViewExt tradPlusViewExt = new TradPlusViewExt();
        tradPlusViewExt.loadFeedAd(this, tradPlusSlot, new TradPlusViewExt.TradPlusFeedListener() {
            @Override
            public void onFeedAdLoad(List<TradPlusView> ads) {
                LogUtil.ownShow("TradPlusViews size = " + ads.size());
                for (int i = 0; i < LIST_ITEM_COUNT; i++) {
                    mData.add(null);
                }

                int count = mData.size();
                int index = 0;
                for (TradPlusView ad : ads) {

                    int random = index * option.getInterval() + count - LIST_ITEM_COUNT;
                    LogUtil.ownShow("random = " + random);
                    mData.set(random, ad);
                    index++;
                    myAdapter.notifyItemInserted(random);
                }


                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClicked(String unitid, String networkName) {
                //广告被点击
            }

            @Override
            public void onError(TradPlusErrorCode errorCode) {
                //广告加载失败
            }

            @Override
            public void onAdsSourceLoad(List<Object> adsSource) {
                LogUtil.ownShow("TradPlusViews adsSource size = " + adsSource.size());
                for(Object ads : adsSource){
                    LogUtil.ownShow("TradPlusViewSource networkname = " +ads);
                    LogUtil.ownShow("TradPlusViewSource networkname = " +compareAdSources(ads));
                }
            }
        });
    }

    private String compareAdSources(Object obj){
        if(obj instanceof com.mopub.nativeads.StaticNativeAd){
            return "mopub";
        }else if(obj instanceof com.google.android.gms.ads.formats.NativeContentAd){
            return "admob";
        }else if(obj instanceof com.facebook.ads.NativeAd){
            return "facebook";
        }else{
            return "unknown";
        }
    }

    @SuppressWarnings("RedundantCast")
    private void initListView() {
        mListView = (RecyclerView) findViewById(R.id.my_list);
        mListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mData = new ArrayList<>();
        myAdapter = new TPAdapter(this, mData);
        mListView.setAdapter(myAdapter);


        loadListAd();
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadListAd();
            }
        });
    }

    public class TPAdapter extends RecyclerView.Adapter {

        private static final int ITEM_VIEW_TYPE_NORMAL = 0;
        private static final int ITEM_VIEW_TYPE_TRADPLUS = 6;//竖版图片

        private List<TradPlusView> mData;
        private Context mContext;
        private RecyclerView mRecyclerView;

        public TPAdapter(Context context, List<TradPlusView> data) {
            this.mContext = context;
            this.mData = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW_TYPE_TRADPLUS) {
                return new AdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad, parent,false));
            }
            return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_normal, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int count = mData.size();
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                normalViewHolder.idle.setText("Recycler item " + position);
            } else {
                LogUtil.ownShow("-----position = "+position);
                AdViewHolder adViewHolder = (AdViewHolder) holder;
                TradPlusView tpView = mData.get(position);
                ViewGroup adCardView = (ViewGroup) adViewHolder.itemView;

                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (tpView.getParent() != null) {
                    ((ViewGroup) tpView.getParent()).removeView(tpView);
                }

                // Add the banner ad to the ad view.
                adCardView.addView(tpView);

            }
        }

        @Override
        public int getItemCount() {
            int count = mData == null ? 0 : mData.size();
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (mData != null) {
                TradPlusView ad = mData.get(position);
                if (ad == null) {
                    return ITEM_VIEW_TYPE_NORMAL;
                } else {
                    return ITEM_VIEW_TYPE_TRADPLUS;
                }
            }
            return super.getItemViewType(position);
        }


        private class AdViewHolder extends RecyclerView.ViewHolder {
            LinearLayout tradPlusView;

            public AdViewHolder(View itemView) {
                super(itemView);

                tradPlusView = (LinearLayout) itemView.findViewById(R.id.tpview);
            }
        }

        private class NormalViewHolder extends RecyclerView.ViewHolder {
            TextView idle;

            @SuppressWarnings("RedundantCast")
            public NormalViewHolder(View itemView) {
                super(itemView);

                idle = (TextView) itemView.findViewById(R.id.text_idle);

            }
        }

    }
}
