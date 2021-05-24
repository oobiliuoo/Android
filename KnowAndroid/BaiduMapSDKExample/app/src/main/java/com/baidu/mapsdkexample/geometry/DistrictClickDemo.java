package com.baidu.mapsdkexample.geometry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.baidu.mapsdkexample.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DistrictClickDemo extends AppCompatActivity implements OnGetDistricSearchResultListener,
        Button.OnClickListener, BaiduMap.OnMapClickListener {

    private DistrictSearch mDistrictSearch;
    private EditText mCity;
    private EditText mDistrict;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mSearchBtn;
    private Marker mMarker;
    private List<List<LatLng>> mLatLngList;
    private List<Polygon> mPolygonList = new ArrayList<>();

    private final String CUSTOM_FILE_NAME_CT = "custom_map_config_CT.sty";
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.marker_red);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_click);

        // 初始化检索对象
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);

        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setViewPadding(30, 0, 30, 20);

        // 设置个性化地图（茶田）
        String customStyleFilePath = getCustomStyleFilePath(DistrictClickDemo.this, CUSTOM_FILE_NAME_CT);
        mMapView.setMapCustomStylePath(customStyleFilePath);
        mMapView.setMapCustomStyleEnable(true);

        mCity = (EditText) findViewById(R.id.city);
        mDistrict = (EditText) findViewById(R.id.district);
        mSearchBtn = (Button) findViewById(R.id.districSearch);
        mSearchBtn.setOnClickListener(this);
        mBaiduMap.setOnMapClickListener(this);
    }

    /**
     * 检索回调结果
     *
     * @param districtResult 行政区域信息查询结果
     */
    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        closeKeybord(this);
        if (districtResult == null) {
            return;
        }
        mBaiduMap.clear();
        mMarker = null;
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<List<LatLng>> polyLines = districtResult.getPolylines();
            if (polyLines == null) {
                return;
            }
            mLatLngList = polyLines;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolyline = new PolylineOptions().width(10)
                        .points(polyline).dottedLine(true).color(0xAA00FF00);
                mBaiduMap.addOverlay(ooPolyline);
                OverlayOptions ooPolygon = new PolygonOptions().points(polyline).stroke(new Stroke(5, 0xAA00CD00))
                        .fillColor(0xAA90EE90);
                Polygon polygon = (Polygon) mBaiduMap.addOverlay(ooPolygon);
                mPolygonList.add(polygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmapA.recycle();
        // 释放检索对象
        mDistrictSearch.destroy();
        // 清除所有图层
        mBaiduMap.clear();
        // 在activity执行onDestroy时必须调用mMapView. onDestroy ()
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        String city = mCity.getText().toString().trim();
        String district = mDistrict.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(DistrictClickDemo.this, "城市名字必填", Toast.LENGTH_LONG).show();
        } else {
            mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(city).districtName(district));
        }
    }

    @Override
    public void onMapClick(LatLng point) {

        if (mMarker != null) {
            mMarker.setPosition(point);
            mMarker.setIcon(bitmapA);
            mMarker.setZIndex(9);
        } else {
            MarkerOptions markerOptions = new MarkerOptions().position(point).icon(bitmapA).zIndex(9);
            mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
        }

        if (mLatLngList == null || mPolygonList == null) {
            return;
        }
        boolean isPolygonContains = false;
        for (List<LatLng> list : mLatLngList) {
            // 判断点是否在多边形内
            isPolygonContains = SpatialRelationUtil.isPolygonContainsPoint(list, point);
            if (isPolygonContains) {
                break;
            }
        }

        int fillColor = isPolygonContains ? 0xAA008B00 : 0xAA90EE90;
        for (Polygon polygon : mPolygonList) {
            polygon.setFillColor(fillColor);
        }

        Toast.makeText(DistrictClickDemo.this, "点击overlay可选中某个行政区域", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {

    }

    /**
     * 关闭软键盘
     */
    public void closeKeybord(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 获取个性化地图地址
     *
     * @param context
     * @param customStyleFileName
     * @return
     */
    private String getCustomStyleFilePath(Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;

        try {
            inputStream = context.getAssets().open("customConfigdir/" + customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists()) {
                customStyleFile.delete();
            }
            customStyleFile.createNewFile();

            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            Log.e("DistrictClickDemo", "Copy custom style file failed", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("DistrictClickDemo", "Close stream failed", e);
            }
        }

        return parentPath + "/" + customStyleFileName;
    }
}
