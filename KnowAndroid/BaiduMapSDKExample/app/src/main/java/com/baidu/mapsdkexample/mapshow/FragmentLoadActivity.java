package com.baidu.mapsdkexample.mapshow;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapFragment;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapsdkexample.R;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class FragmentLoadActivity extends FragmentActivity {

    private static final String sNormalFragmentTag = "map_fragment";

    private FragmentManager mFragmentManager = null;

    private MapFragment mMapFragment;

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_main);
        initMapFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null == mMapFragment) {
            return;
        }

        // fragment形式的地图只有在onResume里才能获取到MapView
        mMapView = mMapFragment.getMapView();
        if (null == mMapView) {
            return;
        }

        BaiduMap baiduMap = mMapView.getMap();
        if (null != baiduMap) {
            // 解决圆角屏幕手机，地图loggo被遮挡的问题
            baiduMap.setViewPadding(30, 0, 30, 20);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化地图
     */
    private void initMapFragment() {
        mFragmentManager = getFragmentManager();

        BaiduMapOptions baiduMapOptions = new BaiduMapOptions();
        baiduMapOptions.mapType(BaiduMap.MAP_TYPE_SATELLITE);
        mMapFragment = MapFragment.newInstance(baiduMapOptions);

        mFragmentManager.beginTransaction()
                .add(R.id.map
                        , mMapFragment
                        , sNormalFragmentTag)
                .commit();
    }

}
