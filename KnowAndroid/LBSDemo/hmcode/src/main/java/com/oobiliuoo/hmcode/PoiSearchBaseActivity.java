package com.oobiliuoo.hmcode;

import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.oobiliuoo.hmcode.overlayutil.PoiOverlay;

public abstract class PoiSearchBaseActivity extends BaseActivity  implements OnGetPoiSearchResultListener {

    private static final String TAG = "PoiSearchBaseActivity";
    protected PoiSearch poiSearch;
    protected PoiOverlay poiOverlay;

    @Override
    public final void init() {

        // 因为其他搜索也需要这个实例，所以放在父类初始，其子类不需要再实例化
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        poiOverlay = new PoiOverlay(mBaiduMap){
            @Override
            public boolean onPoiClick(int i) {

                return PoiSearchBaseActivity.this.onPoiClick(i);
            }
        };
        mBaiduMap.setOnMarkerClickListener(poiOverlay);

        poiSearchInit();

    }

    /* poi搜索的初始化代码写在这个方法里*/
    protected abstract void poiSearchInit();

    /*
    *  生成这个方法是为了子类能够覆盖
    * */
    public boolean onPoiClick(int i) {
        PoiInfo poiInfo = poiOverlay.getPoiResult().getAllPoi().get(i);

        Toast.makeText(PoiSearchBaseActivity.this,poiInfo.name + "," + poiInfo.address,Toast.LENGTH_SHORT).show();

        return true;
    }


    // 因为其他搜索结果处理是相同的，所以放在父类
    /* 获取兴趣点信息*/
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR){

            Toast.makeText(this,"没有搜索到结果",Toast.LENGTH_SHORT).show();

            Log.i(TAG, "onGetPoiResult: " + poiResult.error.toString());

            return;

        }

        poiOverlay.setData(poiResult);  // 把数据设置给覆盖物
        poiOverlay.addToMap();          // 把所有的数据变成覆盖物添加到baiduMap中
        poiOverlay.zoomToSpan();        // 把所有的搜索结果在屏幕内显示出来

        Log.i(TAG, "onGetPoiResult: " + poiResult.getTotalPoiNum());
    }

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
