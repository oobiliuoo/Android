package com.oobiliuoo.hmcode;

import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;

public class SearchNearbyActivity extends PoiSearchBaseActivity {

    private Button btnSearch ;

    @Override
    protected void poiSearchInit() {
        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poiSearch.searchNearby(getSearchParams());
            }
        });

    }

    private PoiNearbySearchOption getSearchParams() {
        PoiNearbySearchOption params = new PoiNearbySearchOption();
        params.location(new LatLng(25.789994,113.107715));
        params.radius(1000);
        params.keyword("停车场");
        return params;
    }

}
