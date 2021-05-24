package com.oobiliuoo.hmcode;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.oobiliuoo.hmcode.overlayutil.PoiOverlay;

public class SearchInBoundActivity extends PoiSearchBaseActivity  {

    private static final String TAG = "SearchInBoundActivity";
    private Button btnSearch ;


    @Override
    protected void poiSearchInit() {
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "setOnClick: " );
                poiSearch.searchInBound(getSearchParams());
            }
        });
    }

    private PoiBoundSearchOption getSearchParams() {

        Log.i(TAG, "getSearchParams: ");
        PoiBoundSearchOption params = new PoiBoundSearchOption();
        // 指定搜索范围，由西南到东北的矩形范围
        LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(40.048459,116.302072))
                .include(new LatLng(40.050675,116.304317)).build();

        params.bound(bounds);  // 指定搜索范围
        params.keyword("加油站");  // 指定搜索内容

        return params;
    }


    /* 获取兴趣点详情信息*/
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
}
