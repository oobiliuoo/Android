package com.baidu.mapsdkexample.geometry;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapsdkexample.R;

import java.util.ArrayList;
import java.util.List;

public class SearchMarkerDemo extends AppCompatActivity implements OnGetPoiSearchResultListener {

    private static final int MAX_POI_SIZE = 10;

    private PoiSearch mPoiSearch = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private int mLoadIndex = 0;

    private List<OverlayOptions> mOverlayOptionList = new ArrayList<>();
    private List<Overlay> mOverlayList = new ArrayList<>();

    private BitmapDescriptor mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_red);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_marker);

        // 创建map
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setViewPadding(30, 0, 30, 20);

        // 创建poi检索实例，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        searchProcess();
    }

    private void searchProcess() {
        // 发起请求
        if (mPoiSearch == null) {
            return;
        }

        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword("北京大学")
                .pageCapacity(10)
                .pageNum(mLoadIndex) // 分页编号
                .cityLimit(true)
                .scope(2));
    }

    /**
     * 获取城市poi检索结果
     *
     * @param result poi查询结果
     */
    @Override
    public void onGetPoiResult(final PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            mLoadIndex = 0;
            mBaiduMap.clear();
            Toast.makeText(SearchMarkerDemo.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            // 添加poi
            addToMap(result);
            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表

            StringBuilder mBuilder = new StringBuilder("在");
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                mBuilder.append(cityInfo.city);
                mBuilder.append(",");
            }

            mBuilder.append("找到结果");
            Toast.makeText(SearchMarkerDemo.this, mBuilder.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult result) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放检索对象
        mPoiSearch.destroy();
        // 清空地图所有的覆盖物
        mBaiduMap.clear();
        // 释放地图组件
        mMapView.onDestroy();
    }

    public List<OverlayOptions> getOverlayOptions(final PoiResult mPoiResult) {
        if (mPoiResult == null || mPoiResult.getAllPoi() == null) {
            return null;
        }

        List<OverlayOptions> markerList = new ArrayList<>();
        List<InfoWindow> infoWindowList = new ArrayList<>();
        int markerSize = 0;

        for (int i = 0; i < mPoiResult.getAllPoi().size() && markerSize < MAX_POI_SIZE; i++) {
            if (mPoiResult.getAllPoi().get(i).location == null) {
                continue;
            }

            markerSize++;


            if (mBitmapDescriptor == null) {
                continue;
            }
            markerList.add(new MarkerOptions()
                    .icon(mBitmapDescriptor)
                    .position(mPoiResult.getAllPoi().get(i).location));

            // 自定义InfoWindow
            Button button = new Button(getApplicationContext());
            button.setBackgroundResource(R.drawable.bubble);
            button.setText(mPoiResult.getAllPoi().get(i).address);
            button.setTextColor(Color.WHITE);
            button.setPadding(0, 0, 0, 5);
            button.setTextSize(10);
            button.setWidth(300);
            // 创建InfoWindow
            InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button)
                    , mPoiResult.getAllPoi().get(i).location, -95, null);
            infoWindowList.add(mInfoWindow);
        }

        mBaiduMap.showInfoWindows(infoWindowList);
        return markerList;
    }

    /**
     * 将所有Overlay 添加到地图上
     */
    public void addToMap(PoiResult result) {
        if (mBaiduMap == null) {
            return;
        }

        if (result == null) {
            return;
        }
        removeFromMap();
        List<OverlayOptions> overlayOptions = getOverlayOptions(result);
        if (overlayOptions != null) {
            mOverlayOptionList.addAll(overlayOptions);
        }

        if (mOverlayOptionList == null) {
            return;
        }

        for (OverlayOptions option : mOverlayOptionList) {
            mOverlayList.add(mBaiduMap.addOverlay(option));
        }

        if (result.getAllPoi() != null) {
            List<PoiInfo> mAllPoi = result.getAllPoi();
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(
                    new MapStatus.Builder().target(mAllPoi.get(0).getLocation()).zoom(13).build()));
        }
    }

    /**
     * 将所有Overlay 从 地图上消除
     */
    public void removeFromMap() {
        if (mBaiduMap == null) {
            return;
        }
        mBaiduMap.clear();
        mOverlayOptionList.clear();
        mOverlayList.clear();

    }
}
