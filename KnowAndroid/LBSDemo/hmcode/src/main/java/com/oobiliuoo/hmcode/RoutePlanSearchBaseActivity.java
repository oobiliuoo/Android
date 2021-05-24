package com.oobiliuoo.hmcode;

import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;

public abstract class RoutePlanSearchBaseActivity extends BaseActivity implements OnGetRoutePlanResultListener {
    protected RoutePlanSearch routePlanSearch;
    @Override
    public final void init() {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);

        routePlanSearchInit();
    }

    /* 路线规划的初始化代码*/
    protected abstract void routePlanSearchInit();
}
