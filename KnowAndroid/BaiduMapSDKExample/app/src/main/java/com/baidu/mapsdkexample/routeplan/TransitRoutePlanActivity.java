/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapsdkexample.routeplan;

import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.util.Utils;
import com.baidu.mapsdkexample.util.overlayutil.TransitRouteOverlay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 此demo用来展示如何进行公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class TransitRoutePlanActivity extends AppCompatActivity
        implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener, View.OnClickListener {

    private RouteLine mRouteLine = null;

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null;    // 地图View
    private BaiduMap mBaidumap = null;
    // 搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

    private AutoCompleteTextView mStrartNodeView;
    private AutoCompleteTextView mEndNodeView;

    private View mBottomOverviewCard = null;
    private TextView mETAText = null;

    private List<? extends RouteLine> mRouteLines;
    private ListView mRouteListView = null;
    private RouteLineListAdapter mRouteLineListAdapter = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_route);

        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();

        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        initMapView();
        initView();
    }

    private void initMapView() {
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mBaidumap.setViewPadding(20, 0, 0, 20);
    }

    private void initRouteOverViewData() {
        mRouteLineListAdapter =
                new RouteLineListAdapter(TransitRoutePlanActivity.this.getApplicationContext(),
                        mRouteLines,
                        RouteLineListAdapter.Type.TRANSIT_ROUTE);
        mRouteListView.setAdapter(mRouteLineListAdapter);
    }

    private void initView() {
        // 路线概览卡片
        mBottomOverviewCard = findViewById(R.id.rooter_search);
        mETAText = (TextView) findViewById(R.id.eta_text);
        Button detailButton = (Button) findViewById(R.id.route_detail_button);
        detailButton.setOnClickListener(this);

        // 起终点输入
        mStrartNodeView = (AutoCompleteTextView) findViewById(R.id.route_search_input_start_text);
        mEndNodeView = (AutoCompleteTextView) findViewById(R.id.route_search_input_end_text);

        // 路线列表
        mRouteListView = (ListView) findViewById(R.id.route_result_listview);
        mRouteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRouteLines == null) {
                    return;
                }
                mRouteListView.setVisibility(View.GONE);
                mBottomOverviewCard.setVisibility(View.VISIBLE);

                mRouteLine = mRouteLines.get(position);

                // 路线概览
                updateRouteOverViewCard((TransitRouteLine) mRouteLine);

                // 路线绘制
                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData((TransitRouteLine) mRouteLine);
                overlay.addToMap();
                overlay.zoomToSpanPaddingBounds(100, 500, 100, 500);
            }
        });

    }

    private void updateRouteOverViewCard(RouteLine routeLine) {
        String totalTime = "";
        String totalDistance = "";
        int time = routeLine.getDuration();
        if (time / 3600 == 0) {
            totalTime = time / 60 + "分钟";
        } else {
            totalTime = time / 3600 + "小时" + (time % 3600) / 60 + "分钟";
        }

        int distance = routeLine.getDistance();
        if (distance / 1000 == 0) {
            totalDistance = distance + "米";
        } else {
            totalDistance = String.format("%.1f", distance / 1000f) + "公里";
        }

        mETAText.setText(totalTime + " " + totalDistance);
    }

    /**
     * 发起路线规划搜索示例
     */
    public void searchButtonProcess(View v) {
        Utils.hideKeyBoard(TransitRoutePlanActivity.this);
        if (mRouteLines != null) {
            // 重置路线数据
            mRouteLines.clear();
            mRouteLines = null;
        }

        if (mBottomOverviewCard.getVisibility() == View.VISIBLE) {
            mBottomOverviewCard.setVisibility(View.GONE);
        }

        // 清除之前的覆盖物
        mBaidumap.clear();
        // 设置起终点信息 起点参数
        PlanNode startNode = PlanNode.withCityNameAndPlaceName("北京",
                mStrartNodeView.getText().toString().trim());
        // 终点参数
        PlanNode endNode = PlanNode.withCityNameAndPlaceName("北京",
                mEndNodeView.getText().toString().trim());
        // 创建换乘路线规划Option
        TransitRoutePlanOption transitRoutePlanOption =
                new TransitRoutePlanOption().from(startNode).to(endNode).city("北京");
        // 发起换乘路线规划
        mSearch.transitSearch(transitRoutePlanOption);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result != null && result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            Toast.makeText(TransitRoutePlanActivity.this,
                    "起终点或途经点地址有岐义，通过result.getSuggestAddrInfo()接口获取建议查询信息",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(TransitRoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mRouteListView.setVisibility(View.VISIBLE);
            if (mRouteLines != null) {
                mRouteLines.clear();
            }
            mRouteLines = result.getRouteLines();
            initRouteOverViewData();
            mRouteLineListAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {

    }

    private static class MyTransitRouteOverlay extends TransitRouteOverlay {

        private MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
        Utils.hideKeyBoard(this);
    }

    @Override
    public void onMapPoiClick(MapPoi poi) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.route_detail_button:
                Intent intent = new Intent(TransitRoutePlanActivity.this.getApplicationContext(),
                        RouteDetailActivity.class);
                intent.putExtra("route_plan_result", mRouteLine);
                intent.putExtra("route_plan_type", 1);
                startActivity(intent);
                break;

            default:
                break;
        }
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
        if (mSearch != null) {
            mSearch.destroy();
        }
        mBaidumap.clear();
        mMapView.onDestroy();
    }
}
