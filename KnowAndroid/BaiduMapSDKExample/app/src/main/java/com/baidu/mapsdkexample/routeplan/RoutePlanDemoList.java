package com.baidu.mapsdkexample.routeplan;

import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.common.adapter.DemoInfo;
import com.baidu.mapsdkexample.common.adapter.DemoListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class RoutePlanDemoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(RoutePlanDemoList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(RoutePlanDemoList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_name_transit_route_plan,
                    R.string.demo_desc_transit_route_plan,
                    TransitRoutePlanActivity.class),
            new DemoInfo(R.string.demo_name_walking_route_plan,
                    R.string.demo_desc_walking_route_plan,
                    WalkingRoutePlanActivity.class),
            new DemoInfo(R.string.demo_name_biking_route_plan,
                    R.string.demo_desc_biking_route_plan, BikingRoutePlanActivity.class),
    };
}


