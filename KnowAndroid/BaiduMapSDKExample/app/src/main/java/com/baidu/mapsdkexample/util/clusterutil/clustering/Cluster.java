/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.baidu.mapsdkexample.util.clusterutil.clustering;

import java.util.Collection;

import com.baidu.mapapi.model.LatLng;

/**
 * A collection of ClusterItems that are nearby each other.
 */
public interface Cluster<T extends com.baidu.mapsdkexample.util.clusterutil.clustering.ClusterItem> {
    public LatLng getPosition();

    Collection<T> getItems();

    int getSize();
}