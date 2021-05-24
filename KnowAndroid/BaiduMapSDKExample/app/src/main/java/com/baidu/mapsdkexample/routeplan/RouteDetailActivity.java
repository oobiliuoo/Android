package com.baidu.mapsdkexample.routeplan;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapsdkexample.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RouteDetailActivity extends AppCompatActivity {

    private RouteLine mRouteLine;
    private int mRouteLineType;

    private TextView mRouteETAInfo = null;
    private ListView mRouteDetailListView = null;
    private RouteLineDetailAdapter mRouteLineDetailAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routeplan_detail_info);
        getIntentData();
        initView();
        initData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mRouteLine = intent.getParcelableExtra("route_plan_result");
        mRouteLineType = intent.getIntExtra("route_plan_type", 0);
    }

    private void initView() {
        mRouteETAInfo = (TextView) findViewById(R.id.route_eta_info);
        mRouteDetailListView = (ListView) findViewById(R.id.route_detail_listview);
    }

    private void initData() {
        if (mRouteLine == null) {
            return;
        }

        initRouteOverViewCard(mRouteLine);

        switch (mRouteLineType) {
            case 1: // 公交
                List<TransitRouteLine.TransitStep> routeTransitSteps = new ArrayList<>();
                routeTransitSteps.add(new TransitRouteLine.TransitStep());
                routeTransitSteps.addAll(mRouteLine.getAllStep());
                routeTransitSteps.add(new TransitRouteLine.TransitStep());
                mRouteLineDetailAdapter =
                        new RouteLineDetailAdapter(RouteDetailActivity.this.getApplicationContext(),
                                routeTransitSteps, mRouteLineType);
                break;
            case 2: // 步行
                List<BikingRouteLine.BikingStep> routeBikingSteps = new ArrayList<>();
                routeBikingSteps.add(new BikingRouteLine.BikingStep());
                routeBikingSteps.addAll(mRouteLine.getAllStep());
                routeBikingSteps.add(new BikingRouteLine.BikingStep());
                mRouteLineDetailAdapter =
                        new RouteLineDetailAdapter(RouteDetailActivity.this.getApplicationContext(),
                                routeBikingSteps, mRouteLineType);
                break;
            case 3: // 骑行
                List<WalkingRouteLine.WalkingStep> routeWalkingSteps = new ArrayList<>();
                routeWalkingSteps.add(new WalkingRouteLine.WalkingStep());
                routeWalkingSteps.addAll(mRouteLine.getAllStep());
                routeWalkingSteps.add(new WalkingRouteLine.WalkingStep());
                mRouteLineDetailAdapter =
                        new RouteLineDetailAdapter(RouteDetailActivity.this.getApplicationContext(),
                                routeWalkingSteps, mRouteLineType);
                break;
            default:
                break;
        }

        mRouteDetailListView.setAdapter(mRouteLineDetailAdapter);
    }

    private void initRouteOverViewCard(RouteLine routeLine) {
        if (routeLine == null) {
            return;
        }
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

        mRouteETAInfo.setText(totalTime + " (" + totalDistance + ")");
    }

}
