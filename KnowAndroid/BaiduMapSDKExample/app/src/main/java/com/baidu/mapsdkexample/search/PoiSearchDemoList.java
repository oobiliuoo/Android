package com.baidu.mapsdkexample.search;

import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.common.adapter.DemoInfo;
import com.baidu.mapsdkexample.common.adapter.DemoListAdapter;
import com.baidu.mapsdkexample.search.poisearch.PoiCitySearchActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 检索demo入口
 */
public class PoiSearchDemoList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(PoiSearchDemoList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(PoiSearchDemoList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_name_suggestion_search, R.string.demo_desc_suggestion_search,
                    PoiCitySearchActivity.class),
    };
}
