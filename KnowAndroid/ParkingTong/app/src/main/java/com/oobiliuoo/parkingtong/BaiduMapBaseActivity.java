package com.oobiliuoo.parkingtong;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.oobiliuoo.parkingtong.utils.overlayutil.PoiOverlay;

import java.util.ArrayList;
import java.util.List;

public abstract class BaiduMapBaseActivity extends AppCompatActivity implements OnGetPoiSearchResultListener, OnGetRoutePlanResultListener {

    private static final String TAG = "BaiduMapBaseActivity";

    /**湘南学院坐标*/
    protected LatLng xnxyPos = new LatLng(25.789994,113.107715);
    /**北湖公园坐标*/
    protected LatLng northLakeParkPos= new LatLng(25.805296,113.033332);
    /**当前位置坐标，初始化为湘南学院*/
    protected LatLng currentPos  = xnxyPos;

    /**是否第一次定位*/
    boolean isFirstLocate = true;
    /**定位需要的位置对象*/
    protected LocationClient mLocationClient;


    protected MapView mMapView ;
    /**百度地图对象*/
    protected BaiduMap mBaiduMap = null;

    /**兴趣点搜索对象*/
    protected PoiSearch poiSearch;
    /**兴趣点覆盖物对象*/
    protected PoiOverlay poiOverlay;
    /**路线规划对象*/
    protected RoutePlanSearch routePlanSearch;

    /**加final 是为了不让子类覆盖,预防一些类还没初始化就被子类调用*/
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_baidu_map);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationLister());

        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 设置地图风格为卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 设置可以进行当前位置定位
        mBaiduMap.setMyLocationEnabled(true);

        // 保村需要动态获取权限的列表
        List<String> permissionList = new ArrayList<String>();

        // 判断是否获取到对应权限
        if(ContextCompat.checkSelfPermission(BaiduMapBaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(BaiduMapBaseActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(BaiduMapBaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            // 请求权限
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(BaiduMapBaseActivity.this,permissions,1);
        }else {
            requestLocation();
        }

        // 因为其他搜索也需要这个实例，所以放在父类初始，其子类不需要再实例化
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        poiOverlay = new PoiOverlay(mBaiduMap){
            @Override
            public boolean onPoiClick(int i) {

                return BaiduMapBaseActivity.this.onPoiClick(i);
            }
        };
        mBaiduMap.setOnMarkerClickListener(poiOverlay);

        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);

        init();

    }

    /**提供给子类的初始化函数*/
    public abstract void init();

    /**
     *  验证是否获取到需要的权限
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所以权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                }
        }
    }


    /**定位监听类*/
    private class MyLocationLister extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            /*
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度:").append(bdLocation.getLatitude()).append("\n");
            currentPosition.append("经度:").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append("国家:").append(bdLocation.getCountry()).append("\n");
            currentPosition.append("省:").append(bdLocation.getProvince()).append("\n");
            currentPosition.append("市:").append(bdLocation.getCity()).append("\n");
            currentPosition.append("区:").append(bdLocation.getDistrict()).append("\n");
            currentPosition.append("村镇:").append(bdLocation.getTown()).append("\n");
            currentPosition.append("街道:").append(bdLocation.getStreet()).append("\n");
            currentPosition.append("地址:").append(bdLocation.getAddrStr()).append("\n");
            currentPosition.append("定位方式：");
            if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }
            locationInfo.setText(currentPosition);
            */
            navigateTo(bdLocation);
        }

    }

    /**
     *   请求定位
     * */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    /**
     *   百度地图初始化
     * */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(2000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true


        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明


    }

    /**将当前位置在地图上显示出来*/
    private void navigateTo(BDLocation bdLocation) {
        if(isFirstLocate) {
            // 保存当前位置坐标
            currentPos = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(currentPos);
            mBaiduMap.animateMapStatus(update);
            // 缩放地图
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }

        // 将当前位置在地图上标识出来
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.longitude(bdLocation.getLongitude());
        locationBuilder.latitude(bdLocation.getLatitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);

    }


    /**
     *  生成这个方法是为了子类能够覆盖
     *  兴趣点的点击事件
     * */
    public boolean onPoiClick(int i) {
        PoiInfo poiInfo = poiOverlay.getPoiResult().getAllPoi().get(i);

        Toast.makeText(BaiduMapBaseActivity.this,poiInfo.name + "," + poiInfo.address,Toast.LENGTH_SHORT).show();

        return true;
    }


    // 因为其他搜索结果处理是相同的，所以放在父类
    /** 获取兴趣点信息*/
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR){

            Toast.makeText(this,"没有搜索到结果",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onGetPoiResult: " + poiResult.error.toString());
            return;
        }
        // 把数据设置给覆盖物
        poiOverlay.setData(poiResult);
        // 把所有的数据变成覆盖物添加到baiduMap中
        poiOverlay.addToMap();
        // 把所有的搜索结果在屏幕内显示出来
        poiOverlay.zoomToSpan();
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


    // 路线规划相关接口函数
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }


    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

}
