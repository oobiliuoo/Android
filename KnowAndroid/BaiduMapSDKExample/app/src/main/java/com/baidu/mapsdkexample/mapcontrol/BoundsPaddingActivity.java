package com.baidu.mapsdkexample.mapcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.util.GetJsonUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BoundsPaddingActivity extends AppCompatActivity implements BaiduMap.OnMapLoadedCallback
        , View.OnClickListener {

    private MapView mMapView;
    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    private BaiduMap mBaiduMap;
    private ArrayList<LatLng> mLatLngs;
    private BottomSheetBehavior mBehavior;
    private ListView mListView;
    private View mBottomSheet;
    private ArrayList<String> mPoiName;
    private ArrayList<String> mAddressName;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounds_padding);
        mMapView = findViewById(R.id.mapview);
        mListView = (ListView) findViewById(R.id.list_item);
        mImageView = findViewById(R.id.image_view);
        mImageView.setOnClickListener(this);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        initBottomSheet();
        parseJson();
        initListView();
    }

    /**
     * 初始化 BottomSheet 控件
     */
    private void initBottomSheet() {
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (null == mLatLngs) {
                            return;
                        }
                        setBounds(mLatLngs , mBehavior.getPeekHeight());
                        mImageView.setImageResource(R.mipmap.showout);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if (null == mLatLngs) {
                            return;
                        }
                        setBounds(mLatLngs , mBottomSheet.getHeight());
                        mImageView.setImageResource(R.mipmap.showin);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                int height = mImageView.getDrawable().getBounds().height();
                mBehavior.setPeekHeight(height);
                // 设置地图上控件与地图边界的距离，包含比例尺、缩放控件、logo、指南针的位置
                mBaiduMap.setViewPadding( 0 , 0 , 0 , height);
            }
        });
    }

    /**
     * 解析Json数据
     */
    private void parseJson() {
        String json = GetJsonUtil.getJson(this, "poiInfo.json");
        try {
            JSONArray jsonArray = new JSONArray(json);
            mLatLngs = new ArrayList<>();
            mPoiName = new ArrayList<>();
            mAddressName = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                double lng = jsonObject.getDouble("lng");
                double lat = jsonObject.getDouble("lat");
                LatLng latLng = new LatLng(lat, lng );
                mLatLngs.add(latLng);
                String name = jsonObject.getString("name");
                mPoiName.add(name);
                String address = jsonObject.getString("address");
                mAddressName.add(address);
            }
            if (null != mLatLngs) {
                addMarker(mLatLngs);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        if (null == mPoiName || null == mAddressName) {
            return;
        }
        // 定义一个HashMap构成的列表以键值对的方式存放数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < mPoiName.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", mPoiName.get(i));
            map.put("address", mAddressName.get(i));
            listItem.add(map);
        }
        // 构造SimpleAdapter对象，设置适配器
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this , listItem ,  // 需要绑定的数据
                R.layout.item_layout ,  // 每一行的布局
                new String[]{ "name" , "address" } ,
                new int[] { R.id.poi_name , R.id.poi_address });
        mListView.setAdapter(mSimpleAdapter);
        // 处理listView 和 BottomSheet 事件冲突
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                if (!mListView.canScrollVertically(-1)) {
                    mListView.requestDisallowInterceptTouchEvent(false);
                } else {
                    mListView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    /**
     *  添加Marker
     */
    private void addMarker(ArrayList<LatLng> mLatLngs) {
        ArrayList<OverlayOptions> overlayOptions = new ArrayList<>();
        for (LatLng latLng : mLatLngs) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(mBitmap) // 设置 Marker 覆盖物的图标
                    .zIndex(9);
            overlayOptions.add(markerOptions);
        }
        // 设置 marker 覆盖物的 zIndex
        mBaiduMap.addOverlays(overlayOptions);

    }

    /**
     * 最佳视野内显示所有点标记
     */
    private void setBounds(ArrayList<LatLng> mLatLngs , int paddingBottom ) {
        int padding = 120;
        // 构造地理范围对象
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        // 让该地理范围包含一组地理位置坐标
        builder.include(mLatLngs);

        // 设置显示在指定相对于MapView的padding中的地图地理范围
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build(), padding, padding,
                padding, paddingBottom);
        // 更新地图
        mBaiduMap.setMapStatus(mapStatusUpdate);
        // 设置地图上控件与地图边界的距离，包含比例尺、缩放控件、logo、指南针的位置
        mBaiduMap.setViewPadding( 0 , 0 , 0 , paddingBottom);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBitmap.recycle();
    }

    @Override
    public void onMapLoaded() {
        if (null == mLatLngs || null == mBehavior) {
            return;
        }
        setBounds(mLatLngs , mBehavior.getPeekHeight());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_view) {
            if (mBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }
}
