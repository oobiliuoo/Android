package com.baidu.mapsdkexample.geometry.markercluster;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.util.clusterutil.clustering.Cluster;
import com.baidu.mapsdkexample.util.clusterutil.clustering.ClusterManager;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 此Demo用来说明点聚合功能,适合添加大批量marker场景
 */
public class ClusterMarkerActivity extends AppCompatActivity {

    private static final String TAG = ClusterMarkerActivity.class.getSimpleName();

    // 地图View实例
    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_marker_main);
        initMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mMapView) {
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mMapView) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mMapView) {
            mMapView.onDestroy();
        }
    }

    private void initMap() {
        mMapView = findViewById(R.id.mapview);
        if (null == mMapView) {
            return;
        }

        mBaiduMap = mMapView.getMap();
        if (null == mBaiduMap) {
            return;
        }

        mBaiduMap.setViewPadding(30, 0, 30, 20);

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                // 添加marker
                initCluster();
                addMarkers();

                // 设置初始中心点为北京
                LatLng center = new LatLng(39.963175, 116.400244);
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 10);
                mBaiduMap.setMapStatus(mapStatusUpdate);
            }
        });
    }

    private void initCluster() {
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {

                    @Override
                    public boolean onClusterClick(Cluster<MyItem> cluster) {
                        Toast.makeText(ClusterMarkerActivity.this, "有" + cluster.getSize() + "个点",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<MyItem>() {
                    @Override
                    public boolean onClusterItemClick(MyItem item) {
                        Toast.makeText(ClusterMarkerActivity.this, "点击单个Item", Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                });
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        // 添加Marker点
        LatLng llA = new LatLng(40.109965, 116.380244);
        LatLng llB = new LatLng(40.106965, 116.359199);
        LatLng llC = new LatLng(40.105965, 116.405541);
        LatLng llD = new LatLng(40.103175, 116.401394);
        LatLng llE = new LatLng(40.102821, 116.421394);

        LatLng llF = new LatLng(39.993175, 116.432394);
        LatLng llG = new LatLng(39.992821, 116.431394);
        LatLng llH = new LatLng(39.999723, 116.451394);
        LatLng llI = new LatLng(39.996965, 116.460244);
        LatLng llJ = new LatLng(39.999965, 116.489199);

        LatLng llK = new LatLng(39.999723, 116.315541);
        LatLng llL = new LatLng(39.996965, 116.291394);
        LatLng llM = new LatLng(40.010065, 116.351394);
        LatLng llN = new LatLng(40.016965, 116.331394);
        LatLng llO = new LatLng(40.015965, 116.361394);
        LatLng llP = new LatLng(40.017965, 116.291394);

        LatLng llQ = new LatLng(39.899723, 116.315541);
        LatLng llR = new LatLng(39.896965, 116.341394);
        LatLng llS = new LatLng(39.895065, 116.351394);
        LatLng llT = new LatLng(39.916965, 116.341394);
        LatLng llU = new LatLng(39.915965, 116.331394);
        LatLng llV = new LatLng(39.917965, 116.321394);

        LatLng llW = new LatLng(39.893175, 116.412394);
        LatLng llX = new LatLng(39.892821, 116.411394);
        LatLng llY = new LatLng(39.899723, 116.431394);
        LatLng llZ = new LatLng(39.896965, 116.440244);
        LatLng llA0 = new LatLng(39.899965, 116.469199);

        List<MyItem> items = new ArrayList<MyItem>();
        items.add(new MyItem(llA));
        items.add(new MyItem(llB));
        items.add(new MyItem(llC));
        items.add(new MyItem(llD));
        items.add(new MyItem(llE));
        items.add(new MyItem(llF));
        items.add(new MyItem(llG));
        items.add(new MyItem(llH));
        items.add(new MyItem(llI));
        items.add(new MyItem(llJ));
        items.add(new MyItem(llK));
        items.add(new MyItem(llL));
        items.add(new MyItem(llM));
        items.add(new MyItem(llN));
        items.add(new MyItem(llO));
        items.add(new MyItem(llP));
        items.add(new MyItem(llQ));
        items.add(new MyItem(llR));
        items.add(new MyItem(llS));
        items.add(new MyItem(llT));
        items.add(new MyItem(llU));
        items.add(new MyItem(llV));
        items.add(new MyItem(llW));
        items.add(new MyItem(llX));
        items.add(new MyItem(llY));
        items.add(new MyItem(llZ));
        items.add(new MyItem(llA0));

        mClusterManager.addItems(items);
    }
}
