package com.oobiliuoo.hmcode;

import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.oobiliuoo.hmcode.overlayutil.DrivingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

public class DrivingSearchActivity extends RoutePlanSearchBaseActivity {

    private Button btnSearch;

    @Override
    protected void routePlanSearchInit() {
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routePlanSearch.drivingSearch(getDrivingSearchParams());
            }
        });

    }

    private DrivingRoutePlanOption getDrivingSearchParams() {
        DrivingRoutePlanOption params = new DrivingRoutePlanOption();
        List<PlanNode> notes = new ArrayList<PlanNode>();
        notes.add(PlanNode.withCityNameAndPlaceName("郴州市","王仙邻广场"));
        params.from(PlanNode.withLocation(xnxyPos));                //设置起点
        params.passBy(notes);                                       //设置途径点
        params.to(PlanNode.withLocation(northLakeParkPos));         //设置终点

        return params;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    /* 换乘（公交/地铁）搜索的回调方法*/
    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    /* 驾车搜索结果的回调方法 */
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
        mBaiduMap.setOnMarkerClickListener(overlay);
        // 获取所以的搜索路线,最优的在前
        List<DrivingRouteLine> routeLines = drivingRouteResult.getRouteLines();
        overlay.setData(routeLines.get(0));
        overlay.addToMap();         // 将覆盖物添加到地图
        overlay.zoomToSpan();       // 搜索结果在一个屏幕显示完

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
}
