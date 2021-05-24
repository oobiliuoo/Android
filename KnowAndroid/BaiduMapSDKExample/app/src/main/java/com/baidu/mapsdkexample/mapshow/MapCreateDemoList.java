package com.baidu.mapsdkexample.mapshow;

import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.common.adapter.DemoInfo;
import com.baidu.mapsdkexample.common.adapter.DemoListAdapter;
import com.baidu.mapsdkexample.mapshow.placechoose.MapPlaceChooseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MapCreateDemoList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(MapCreateDemoList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(MapCreateDemoList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_name_map_create_fragment,
                    R.string.demo_desc_map_create_fragment,
                    FragmentLoadActivity.class),
            new DemoInfo(R.string.demo_name_map_place_choose,
                    R.string.demo_desc_map_place_choose,
                    MapPlaceChooseActivity.class),
            new DemoInfo(R.string.demo_name_map_create_oversea,
                    R.string.demo_desc_map_create_oversea,
                    OverSeaActivity.class),
            new DemoInfo(R.string.demo_name_map_create_custom_map,
                    R.string.demo_desc_map_create_custom_map,
                    CustomMapActivity.class),
    };
}
