package com.baidu.mapsdkexample.geometry;

import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.ScaleAnimation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkexample.R;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Marker缩放动画示例
 */
public class MarkerZoomActivity extends AppCompatActivity {

    // 地图View实例
    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private Marker mMarker;

    private ViewGroup mAddAniLayout;

    private ViewGroup mRemoveAniLayout;

    private Animation mAnimation;

    private BitmapDescriptor mBitmapDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_zoom_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mMapView) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mMapView) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mMarker) {
            mMarker.cancelAnimation();
        }

        if (null != mMapView) {
            mMapView.onDestroy();
        }

        if (null != mBitmapDescriptor) {
            mBitmapDescriptor.recycle();
        }
    }

    private void init() {
        initView();
        initMap();
    }

    private void initMap() {
        if (null == mMapView) {
            return;
        }

        mBaiduMap = mMapView.getMap();
        if (null == mBaiduMap) {
            return;
        }

        // 解决圆角屏幕手机，地图loggo被遮挡的问题
        mBaiduMap.setViewPadding(30, 0, 30, 20);

        // 设置初始中心点为北京
        LatLng center = new LatLng(39.963175, 116.400244);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 12);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        mAnimation = createScaleAnimation();

        // 设置底图加载完成回调
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            /**
             * 地图加载完成，才能进行overlay添加
             */
            @Override
            public void onMapLoaded() {
                createCenterMarker();
            }
        });
    }

    private void initView() {
        mMapView = findViewById(R.id.mapview);
        mAddAniLayout = findViewById(R.id.add_ani);
        mAddAniLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarker.cancelAnimation();
                mMarker.setAnimation(mAnimation);
                mMarker.startAnimation();
            }
        });

        mRemoveAniLayout = findViewById(R.id.remove_ani);
        mRemoveAniLayout.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mMarker.cancelAnimation();
                mMarker.setScale(1.0f);
            }
        });
    }

    /**
     * 创建地图中心点marker
     */
    private void createCenterMarker() {
        LatLng center = new LatLng(39.963175, 116.400244);
        mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(mBitmapDescriptor);
        mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
    }

    /**
     * 创建缩放动画
     */
    private Animation createScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f, 1f);
        scaleAnimation.setDuration(2000);   // 动画播放时间
        scaleAnimation.setRepeatCount(2000);
        scaleAnimation.setRepeatMode(Animation.RepeatMode.RESTART); // 动画重复模式
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        return scaleAnimation;
    }
}
