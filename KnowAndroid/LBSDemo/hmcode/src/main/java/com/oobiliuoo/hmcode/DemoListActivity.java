package com.oobiliuoo.hmcode;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DemoListActivity extends ListActivity {


    ClassAndName[] datas = {
            new ClassAndName(HelloBaiduMapActivity.class,"HelloBaiduMap"),
            new ClassAndName(SearchInBoundActivity.class,"在范围内搜索"),
            new ClassAndName(SearchInCityActivity.class,"在城市内搜索"),
            new ClassAndName(SearchNearbyActivity.class,"在附近搜索"),
            new ClassAndName(DrivingSearchActivity.class,"驾车路线搜索"),
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<ClassAndName> adapter = new ArrayAdapter<ClassAndName>(this, android.R.layout.simple_list_item_1,datas);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ClassAndName classAndName =(ClassAndName) l.getItemAtPosition(position);
        startActivities(new Intent[]{new Intent(this,classAndName.cls)});
    }

    class ClassAndName {
        public Class<?> cls;
        public String name;

        public ClassAndName(Class<?> cls, String name) {
            this.cls = cls;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
