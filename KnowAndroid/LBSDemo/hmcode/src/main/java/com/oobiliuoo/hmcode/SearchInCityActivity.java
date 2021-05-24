package com.oobiliuoo.hmcode;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;

import java.util.List;

public class SearchInCityActivity extends PoiSearchBaseActivity {
    private static final String TAG = "SearchInCityActivity";
    private int pageNum = 1;
    private Button btnSearch;

    @Override
    protected void poiSearchInit() {
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poiSearch.searchInCity(getSearchParams());
            }
        });

    }

    private PoiCitySearchOption getSearchParams() {
        PoiCitySearchOption params = new PoiCitySearchOption();
        params.city("北京");               // 指定搜索城市
        params.keyword("加油站");            // 指定搜索内容
        params.pageCapacity(10);            // 指定一页10条数据
        params.pageNum(pageNum);            // 指定获取那一页
        pageNum++;
        return params;
    }

    @Override
    public boolean onPoiClick(int i) {
        // 当点击一个搜索出来的兴趣点后，在去搜索更详细内容
        PoiInfo poiInfo = poiOverlay.getPoiResult().getAllPoi().get(i);
        Toast.makeText(SearchInCityActivity.this, poiInfo.name + "," + poiInfo.address, Toast.LENGTH_SHORT).show();
        boolean b = poiSearch.searchPoiDetail(getSearchDetailParams(poiInfo.uid));
        return true;
    }

    private PoiDetailSearchOption getSearchDetailParams(String poiUid) {
        PoiDetailSearchOption params = new PoiDetailSearchOption();
        params.poiUids(poiUid);
        return params;
    }


    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult == null || poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "没有搜索到详细信息", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onGetPoiDetailResult: " + poiDetailSearchResult.error.toString());
            return;
        }
        List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
        Toast.makeText(SearchInCityActivity.this, poiDetailInfoList.get(0).getShopHours()
                + ",TEL:" + poiDetailInfoList.get(0).getTelephone(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        poiSearch.destroy();
        super.onDestroy();
    }
}
