/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.mapsdkexample;

import java.util.ArrayList;

import com.baidu.mapsdkexample.geometry.GeometryList;
import com.baidu.mapsdkexample.mapcontrol.MapControlDemoList;
import com.baidu.mapsdkexample.mapshow.MapCreateDemoList;
import com.baidu.mapsdkexample.routeplan.RoutePlanDemoList;
import com.baidu.mapsdkexample.search.PoiSearchDemoList;
import com.baidu.mapsdkexample.trace.TraceDemoList;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BMapApiDemoMain extends AppCompatActivity {

    private boolean isPermissionRequested;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mListView = (ListView) findViewById(R.id.listView);
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });

        // 申请动态权限
        requestPermission();
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(BMapApiDemoMain.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.drawable.map,
                    R.string.demo_list_map_crearte,
                    R.string.demo_desc_map_create,
                    MapCreateDemoList.class),
            new DemoInfo(R.drawable.control,
                    R.string.demo_list_map_control,
                    R.string.demo_desc_map_control,
                    MapControlDemoList.class),
            new DemoInfo(R.drawable.geometry_draw,
                    R.string.demo_list_geometry_draw,
                    R.string.demo_desc_geometry_draw,
                    GeometryList.class),
            new DemoInfo(R.drawable.layers,
                    R.string.demo_list_trace,
                    R.string.demo_desc_trace,
                    TraceDemoList.class),
            new DemoInfo(R.drawable.search,
                    R.string.demo_list_search,
                    R.string.demo_desc_search,
                    PoiSearchDemoList.class),
            new DemoInfo(R.drawable.route_plan,
                    R.string.demo_list_route_plan,
                    R.string.demo_desc_routeplan,
                    RoutePlanDemoList.class),
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Android6.0之后需要动态申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {
            isPermissionRequested = true;
            ArrayList<String> permissionsList = new ArrayList<>();
            String[] permissions = {
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
            };

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                    // 进入到这里代表没有权限.
                }
            }

            if (!permissionsList.isEmpty()) {
                String[] strings = new String[permissionsList.size()];
                requestPermissions(permissionsList.toArray(strings), 0);
            }
        }
    }

    private class DemoListAdapter extends BaseAdapter {
        DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(BMapApiDemoMain.this, R.layout.demo_list_item, null);
            }
            ImageView imageView = convertView.findViewById(R.id.image);
            TextView title = convertView.findViewById(R.id.title);
            TextView desc = convertView.findViewById(R.id.desc);
            imageView.setBackgroundResource(DEMOS[index].image);
            title.setText(DEMOS[index].title);
            desc.setText(DEMOS[index].desc);
            return convertView;
        }

        @Override
        public int getCount() {
            return DEMOS.length;
        }

        @Override
        public Object getItem(int index) {
            return DEMOS[index];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }

    private static class DemoInfo {
        private final int image;
        private final int title;
        private final int desc;
        private final Class<? extends Activity> demoClass;

        DemoInfo(int image, int title, int desc, Class<? extends Activity> demoClass) {
            this.image = image;
            this.title = title;
            this.desc = desc;
            this.demoClass = demoClass;
        }
    }
}