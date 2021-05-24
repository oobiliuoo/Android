package com.baidu.mapsdkexample.mapshow.placechoose;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapsdkexample.R;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 地图选点demo
 */
public class MapPlaceChooseActivity extends AppCompatActivity
        implements BaiduMap.OnMapStatusChangeListener, PoiItemAdapter.MyOnItemClickListener
        , OnGetGeoCoderResultListener {

    // 默认逆地理编码半径范围
    private static final int sDefaultRGCRadius = 500;
    // 地图View实例
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng mCenter;
    private Handler mHandler;

    private RecyclerView mRecyclerView;

    private PoiItemAdapter mPoiItemAdapter;

    private GeoCoder mGeoCoder = null;

    private boolean mStatusChangeByItemClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_choose_place_main);
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

        if (null != mGeoCoder) {
            mGeoCoder.destroy();
        }

        if (null != mMapView) {
            mMapView.onDestroy();
        }
    }

    private void init() {
        initRecyclerView();

        mHandler = new Handler(this.getMainLooper());

        initMap();
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

        // 设置初始中心点为北京
        mCenter = new LatLng(39.963175, 116.400244);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(mCenter, 16);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                createCenterMarker();
                reverseRequest(mCenter);
            }
        });
    }

    /**
     * 创建地图中心点marker
     */
    private void createCenterMarker() {
        Projection projection = mBaiduMap.getProjection();
        if (null == projection) {
            return;
        }

        Point point = projection.toScreenLocation(mCenter);
        BitmapDescriptor bitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.icon_binding_point);
        if (null == bitmapDescriptor) {
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(mCenter)
                .icon(bitmapDescriptor)
                .flat(false)
                .fixedScreenPosition(point);
        mBaiduMap.addOverlay(markerOptions);
        bitmapDescriptor.recycle();
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.poi_list);
        if (null == mRecyclerView) {
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 逆地理编码请求
     *
     * @param latLng
     */
    private void reverseRequest(LatLng latLng) {
        if (null == latLng) {
            return;
        }

        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption().location(latLng)
                .newVersion(1) // 建议请求新版数据
                .radius(sDefaultRGCRadius);

        if (null == mGeoCoder) {
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.setOnGetGeoCodeResultListener(this);
        mGeoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult reverseGeoCodeResult) {
        if (null == reverseGeoCodeResult) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                updateUI(reverseGeoCodeResult);
            }
        });
    }

    /**
     * 更新UI
     *
     * @param reverseGeoCodeResult
     */
    private void updateUI(ReverseGeoCodeResult reverseGeoCodeResult) {
        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();

        PoiInfo curAddressPoiInfo = new PoiInfo();
        curAddressPoiInfo.address = reverseGeoCodeResult.getAddress();
        curAddressPoiInfo.location = reverseGeoCodeResult.getLocation();

        if (null == poiInfos) {
            poiInfos = new ArrayList<>(2);
        }

        poiInfos.add(0, curAddressPoiInfo);

        if (null == mPoiItemAdapter) {

            mPoiItemAdapter = new PoiItemAdapter(poiInfos);
            mRecyclerView.setAdapter(mPoiItemAdapter);
            mPoiItemAdapter.setOnItemClickListener(this);
        } else {
            mPoiItemAdapter.updateData(poiInfos);
        }
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        LatLng newCenter = mapStatus.target;

        // 如果是点击poi item导致的地图状态更新，则不用做后面的逆地理请求，
        if (mStatusChangeByItemClick) {
            if (!Utils.isLatlngEqual(mCenter, newCenter)) {
                mCenter = newCenter;
            }
            mStatusChangeByItemClick = false;
            return;
        }

        if (!Utils.isLatlngEqual(mCenter, newCenter)) {
            mCenter = newCenter;
            reverseRequest(mCenter);
        }
    }

    @Override
    public void onItemClick(int position, PoiInfo poiInfo) {
        if (null == poiInfo || null == poiInfo.getLocation()) {
            return;
        }

        mStatusChangeByItemClick = true;
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(poiInfo.getLocation());
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }
}
