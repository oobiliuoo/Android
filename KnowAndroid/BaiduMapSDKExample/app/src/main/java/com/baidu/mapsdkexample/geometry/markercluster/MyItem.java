package com.baidu.mapsdkexample.geometry.markercluster;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.util.clusterutil.clustering.ClusterItem;

/**
 * 每个Marker点，包含Marker点坐标以及图标
 */
public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    MyItem(LatLng latLng) {
        mPosition = latLng;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    }
}
