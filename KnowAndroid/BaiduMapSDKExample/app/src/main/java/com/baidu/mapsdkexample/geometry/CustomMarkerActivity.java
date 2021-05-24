package com.baidu.mapsdkexample.geometry;

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
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 自定义markerdemo
 */
public class CustomMarkerActivity extends AppCompatActivity {
    // 地图View实例
    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private Marker mMarker;

    private Handler mHandler;

    private boolean mIsStart = true;

    private BitmapDescriptor mBitmapDescriptorStart;

    private BitmapDescriptor mBitmapDescriptorEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_marker_main);
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

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (null != mMarker) {
            mMarker.cancelAnimation();
        }

        if (null != mMapView) {
            mMapView.onDestroy();
        }

        if (null != mBitmapDescriptorStart) {
            mBitmapDescriptorStart.recycle();
        }

        if (null != mBitmapDescriptorEnd) {
            mBitmapDescriptorEnd.recycle();
        }
    }

    private void init() {
        initMap();

        mHandler = new Handler(this.getMainLooper());

        mBitmapDescriptorStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_start_custom);

        mBitmapDescriptorEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_end_custom);

        updateMarker();
    }

    private void initMap() {
        mMapView = findViewById(R.id.mapview);
        if (null == mMapView) {
            return;
        }

        mBaiduMap = mMapView.getMap();
        if (null == mBaiduMap) {
            return;
        }

        // 使得圆角地图情况下，地图log不会被遮盖
        mBaiduMap.setViewPadding(30, 0, 30, 20);

        // 设置初始中心点为北京
        LatLng center = new LatLng(39.963175, 116.400244);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 12);
        mBaiduMap.setMapStatus(mapStatusUpdate);

    }

    /**
     * 更新marker状态
     */
    private void updateMarker() {
        if (null == mBitmapDescriptorStart || null == mBitmapDescriptorEnd) {
            return;
        }

        if (null == mMarker) {
            LatLng center = new LatLng(39.963175, 116.400244);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(center)
                    .icon(mBitmapDescriptorStart);
            mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
        } else {
            if (mIsStart) {
                mMarker.setIcon(mBitmapDescriptorStart);
                mIsStart = false;
            } else {
                mMarker.setIcon(mBitmapDescriptorEnd);
                mIsStart = true;
            }
        }

        // 1秒后再次更新
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateMarker();
            }
        }, 1000);
    }
}
