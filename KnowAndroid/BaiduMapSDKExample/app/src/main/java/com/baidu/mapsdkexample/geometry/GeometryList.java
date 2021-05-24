package com.baidu.mapsdkexample.geometry;

import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.common.adapter.DemoInfo;
import com.baidu.mapsdkexample.common.adapter.DemoListAdapter;
import com.baidu.mapsdkexample.geometry.markercluster.ClusterMarkerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class GeometryList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(GeometryList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(GeometryList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_name_marker_cluster,
                    R.string.demo_desc_custom_cluster,
                    ClusterMarkerActivity.class),
            new DemoInfo(R.string.demo_name_custom_marker,
                    R.string.demo_desc_custom_marker,
                    CustomMarkerActivity.class),
            new DemoInfo(R.string.demo_name_marker_zoom,
                    R.string.demo_desc_marker_zoom,
                    MarkerZoomActivity.class),
            new DemoInfo(R.string.demo_name_marker_animation, R.string.demo_desc_marker_animation,
                    MarkerTransformation.class),
            new DemoInfo(R.string.demo_name_map_more_window,
                    R.string.demo_desc_map_more_window,
                    SearchMarkerDemo.class),
            new DemoInfo(R.string.demo_name_map_click_district,
                    R.string.demo_desc_map_click_district,
                    DistrictClickDemo.class),
    };
}

