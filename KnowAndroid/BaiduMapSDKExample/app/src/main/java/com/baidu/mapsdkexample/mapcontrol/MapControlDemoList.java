package com.baidu.mapsdkexample.mapcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.common.adapter.DemoInfo;
import com.baidu.mapsdkexample.common.adapter.DemoListAdapter;
import com.baidu.mapsdkexample.mapcontrol.scrollpage.MapScrollActivity;

public class MapControlDemoList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(MapControlDemoList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(MapControlDemoList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_name_map_control_screen_shot,
                    R.string.demo_desc_map_control_screen_shot,
                    ScreenShotActivity.class),
            new DemoInfo(R.string.demo_name_map_control_screen_bounds,
                    R.string.demo_desc_map_control_screen_bounds,
                    BoundsPaddingActivity.class),
            new DemoInfo(R.string.demo_name_interact_scroll_page,
                    R.string.demo_desc_interact_scroll_page,
                    MapScrollActivity.class),

    };
}
