package com.oobiliuoo.parkingtong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.List;

public class HelloBaiduMapActivity extends BaiduMapBaseActivity {

    private static final String TAG = "HelloBMActivity";

    private BroadcastReceiver receiver;

    private Button btnSearch,btnNearby ;

    private LatLng target;
    private int pageNum = 1;

    boolean flagParkInfoView = false;

    private RelativeLayout relativeLayout;

    private View layoutParkInFo;

    @Override
    public void init() {
        registerSDKCheckReceiver();

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                target = marker.getPosition();
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });


        relativeLayout = findViewById(R.id.rl_baimap);
        findViewById(R.id.layout_park).setVisibility(View.VISIBLE);

        initButton();



    }

    private void initButton(){
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.et_place);

                poiSearch.searchInCity(getSearchInCityParams(editText.getText().toString()));


                //poiSearch.searchNearby(getNearbySearchParams(target));
            }
        });

        btnNearby = findViewById(R.id.btn_nearby);
        btnNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poiSearch.searchNearby(getNearbySearchParams(currentPos));
            }
        });

    }
    private PoiCitySearchOption getSearchInCityParams(String keyWork) {
        PoiCitySearchOption params = new PoiCitySearchOption();
        params.city("郴州");               // 指定搜索城市
        params.keyword(keyWork);            // 指定搜索内容
        params.pageCapacity(1);            // 指定一页10条数据
        params.pageNum(pageNum);            // 指定获取那一页
        pageNum++;
        return params;
    }

    private PoiNearbySearchOption getNearbySearchParams(LatLng pos) {
        PoiNearbySearchOption params = new PoiNearbySearchOption();
        params.location(pos);
        params.radius(1000);
        params.keyword("停车场");
        params.pageCapacity(10);
        params.pageNum(pageNum);
        pageNum++;
        return params;
    }

    @Override
    public boolean onPoiClick(int i) {
        if(flagParkInfoView){
            removeParkInfoView();
            flagParkInfoView = false;
        }
        // 当点击一个搜索出来的兴趣点后，在去搜索更详细内容
        PoiInfo poiInfo = poiOverlay.getPoiResult().getAllPoi().get(i);
        target = poiInfo.location;
        poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUids(poiInfo.getUid()));
        //addParkInfoView();

        return true;

    }

    private void initParkViewInfo(String pName, String pHours,String pTel,String pAddress,String distance) {
        layoutParkInFo = LayoutInflater.from(this).inflate(R.layout.layout_parkinfo , null , false) ;


        TextView parkName = layoutParkInFo.findViewById(R.id.tv_porkName);
        parkName.setText(pName);

        TextView parkHours = layoutParkInFo.findViewById(R.id.tv_porkHours);
        parkHours.setText(pHours);

        TextView parkTel = layoutParkInFo.findViewById(R.id.tv_porkTel);
        parkTel.setText("TEL: "+ pTel);

        TextView parkAddress = layoutParkInFo.findViewById(R.id.tv_porkAddress);
        parkAddress.setText(pAddress);

        TextView parkDistance = layoutParkInFo.findViewById(R.id.tv_porkDistance);
        parkDistance.setText(distance);


    }

    private void addParkInfoView(){

        relativeLayout.addView(layoutParkInFo);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParkInFo.setLayoutParams(lp);
        flagParkInfoView = true;
    }

    private void removeParkInfoView(){
        Log.i(TAG, "removeParkInfoView: ");
        relativeLayout.removeView(layoutParkInFo);
        flagParkInfoView = false;
    }


    /*
     *   验证网络和ak是否正确
     * */
    private void registerSDKCheckReceiver(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(action)){
                    Toast.makeText(HelloBaiduMapActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                }else if(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(action)){
                    Toast.makeText(HelloBaiduMapActivity.this,"SDK KEY 错误",Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        // 监听网络错误
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        // 监听百度地图sdk 的key是否错误
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        registerReceiver(receiver,filter);
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR){

            Toast.makeText(this,"没有搜索到结果",Toast.LENGTH_SHORT).show();
            return;

        }
        mBaiduMap.clear();

        poiOverlay.setData(poiResult);  // 把数据设置给覆盖物
        poiOverlay.addToMap();          // 把所有的数据变成覆盖物添加到baiduMap中
        poiOverlay.zoomToSpan();        // 把所有的搜索结果在屏幕内显示出来

        //定义Maker坐标点
        LatLng point = poiOverlay.getPoiResult().getAllPoi().get(0).location;
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.location);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point) //必传参数
                .icon(bitmap) //必传参数
                .draggable(true)
        //设置平贴地图，在地图中双指下拉查看效果
                .flat(true);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);


    }


    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult == null || poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "没有搜索到详细信息", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onGetPoiDetailResult: " + poiDetailSearchResult.error.toString());
            return;
        }
        List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();

        int d = (int) DistanceUtil.getDistance(currentPos,poiDetailInfoList.get(0).getLocation());


        initParkViewInfo(poiDetailInfoList.get(0).getName(),poiDetailInfoList.get(0).getShopHours()
                ,poiDetailInfoList.get(0).getTelephone(),poiDetailInfoList.get(0).getAddress(),d+" m");

        addParkInfoView();


        Toast.makeText(HelloBaiduMapActivity.this, poiDetailInfoList.get(0).getShopHours()
                + ",TEL:" + poiDetailInfoList.get(0).getTelephone(), Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onBackPressed() {
        if(flagParkInfoView){
            removeParkInfoView();
            Log.i(TAG, "onBackPressed: ");
        }else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }
}