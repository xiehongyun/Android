package com.tradplus.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tradplus.ads.facebook.FacebookAdRenderer;
import com.tradplus.ads.google.GooglePlayServicesAdRenderer;
import com.tradplus.ads.mobileads.util.TradPlusListNativeOption;
import com.tradplus.ads.nativeads.MediaViewBinder;
import com.tradplus.ads.nativeads.TradPlusNativeAdPositioning;
import com.tradplus.ads.nativeads.TradPlusRecyclerAdapter;

import java.util.Locale;


public class NativeListViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TradPlusRecyclerAdapter mRecyclerAdapter;
    private TradPlusListNativeOption option;
    private int ITEM_COUNT = 150;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nativelistview);

        //参数一interval，表示间隔
        //参数二maxLength 表示当前列表可支持的最大行数
        //建议如果间隔参数interval是10 ，maxLength设置500
        option = new TradPlusListNativeOption(10,500);
        //设置指定位置
        option.addFixedPosition(10);

        final RecyclerView.Adapter originalAdapter = new DemoRecyclerAdapter();

        recyclerView = findViewById(R.id.recycleview);
        mRecyclerAdapter = new TradPlusRecyclerAdapter(this, originalAdapter,
                new TradPlusNativeAdPositioning.TradPlusClientPositioning(),option);
        mRecyclerAdapter.setOption(option);

        //设置Admob的渲染器，并添加布局
        //video_ad_list_item.xml布局文件，您可以在Demo中或者下载下来的zip文件中获取
        //可自定义布局，但布局文件中View对应的id不可改变
        GooglePlayServicesAdRenderer googlePlayServicesAdRenderer = new GooglePlayServicesAdRenderer(
                new MediaViewBinder.Builder(R.layout.video_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mediaLayoutId(R.id.native_media_layout)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());

        //设置Facebook的渲染器，并添加布局
        //native_ad_fan_list_item.xml布局文件，您可以在Demo中或者下载下来的zip文件中获取
        //可自定义布局，但布局文件中View对应的id不可改变
        final FacebookAdRenderer facebookAdRenderer = new FacebookAdRenderer(
                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.native_ad_fan_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mediaViewId(R.id.native_media_view)
                        .adIconViewId(R.id.native_icon)
                        .callToActionId(R.id.native_cta)
                        .adChoicesRelativeLayoutId(R.id.native_privacy_information_icon_layout)
                        .build());

        mRecyclerAdapter.registerAdRenderer(googlePlayServicesAdRenderer);
        mRecyclerAdapter.registerAdRenderer(facebookAdRenderer);

        recyclerView.setAdapter(mRecyclerAdapter);

        //如果是GridLayoutManager，可根据isADSView判断SpanSize
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int i) {
//                if(mRecyclerAdapter.isADSView(i)){
//                    return 2;
//                }
//                return 1;
//            }
//        });
//        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //AdUnitId是广告位ID，需要您到开发者后台申请
        //您需要在设置Facebook或Google的渲染器和添加adapter后才能调用
        mRecyclerAdapter.loadAds("DDBF26FBDA47FBE2765F1A089F1356BF");

        //加载更新数据后，可执行如下代码，进行新的信息流广告获取
//        mRecyclerAdapter.setItemCount();
//        mRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        //释放资源
        mRecyclerAdapter.destroy();
        super.onDestroy();
    }

    private class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoViewHolder> {

        @Override
        public DemoViewHolder onCreateViewHolder(final ViewGroup parent,
                                                 final int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_demo_item, parent, false);
            return new DemoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DemoViewHolder holder, final int position) {
            holder.textView.setText(String.format(Locale.US, "Content Item #%d", position));
        }

        @Override
        public long getItemId(final int position) {
            return (long) position;
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

    private static class DemoViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public DemoViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
