package com.baidu.mapsdkexample.mapshow;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkexample.R;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 海外地图demo
 */
public class OverSeaActivity extends AppCompatActivity {

    // 地图View实例
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_oversea_main);
        initMapView();
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
        if (null != mMapView) {
            mMapView.onDestroy();
        }
    }

    private void initMapView() {
        mMapView = findViewById(R.id.mapview);
        if (null == mMapView) {
            return;
        }

        BaiduMap baiduMap = mMapView.getMap();
        if (null == baiduMap) {
            return;
        }

        // 解决圆角屏幕手机，地图loggo被遮挡的问题
        baiduMap.setViewPadding(30, 0, 30, 20);

        // 伦敦经纬度
        LatLng center = new LatLng(51.50015, 0.12623);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 8);
        if (null == mapStatusUpdate) {
            return;
        }

        baiduMap.setMapStatus(mapStatusUpdate);
    }
}
