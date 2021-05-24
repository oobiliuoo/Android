package com.baidu.mapsdkexample.geometry;

import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.Transformation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkexample.R;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
public class MarkerTransformation extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarker;
    private LatLng mLatLng;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_animation);

        initMap();
    }

    private void initMap() {
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setViewPadding(30, 0, 0, 20);

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                initOverlay();
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMarker == null) {
                    return;
                }
                mMarker.cancelAnimation();
                mLatLng = latLng;
                startTransformation();
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                if (mMarker == null || mapPoi == null) {
                    return;
                }
                mMarker.cancelAnimation();
                mLatLng = mapPoi.getPosition();
                startTransformation();
            }
        });
    }

    /**
     * 初始化Overlay
     */
    public void initOverlay() {
        LatLng latLngF = mBaiduMap.getMapStatus().target;
        MarkerOptions markerOptionsF = new MarkerOptions().position(latLngF).icon(mBitmap);
        mMarker = (Marker) (mBaiduMap.addOverlay(markerOptionsF));
    }

    /**
     * 开启平移动画
     */
    public void startTransformation() {
        if (mMarker == null || mLatLng == null) {
            return;
        }

        Transformation transformation = new Transformation(mMarker.getPosition(), mLatLng);
        transformation.setDuration(3000);
        transformation.setAnimationListener(new Animation.AnimationListener() {
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
        mMarker.setAnimation(transformation);
        mMarker.startAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMarker.cancelAnimation();

        mBitmap.recycle();

        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
    }

}
