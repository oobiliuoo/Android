package com.baidu.mapsdkexample.mapcontrol.scrollpage;

import java.util.ArrayList;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapsdkexample.R;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 此demo用于展示地图滑动与view滑动之间手势冲突的解决
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class MapScrollActivity extends AppCompatActivity
        implements ViewPager.OnScrollChangeListener, ViewPager.OnPageChangeListener {

    private ScrollViewPager mViewPager;
    private ArrayList<View> views = new ArrayList<>();

    private Button mBtnStandardMap;
    private Button mBtnSatelliteMap;
    private Button mBtnNoneMap;

    private TextureMapView mStandardMapView;    // 标准地图
    private TextureMapView mSatelliteMapView;   // 卫星地图
    private TextureMapView mNoneMapView;       // 空白地图

    private int mCurMapType = BaiduMap.MAP_TYPE_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_scroll);
        mViewPager = findViewById(R.id.scroll_page);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (mCurMapType) {
            case BaiduMap.MAP_TYPE_NORMAL:
                mStandardMapView.onResume();
                break;
            case BaiduMap.MAP_TYPE_SATELLITE:
                mSatelliteMapView.onResume();
                break;
            case BaiduMap.MAP_TYPE_NONE:
                mNoneMapView.onResume();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        switch (mCurMapType) {
            case BaiduMap.MAP_TYPE_NORMAL:
                mStandardMapView.onPause();
                break;
            case BaiduMap.MAP_TYPE_SATELLITE:
                mSatelliteMapView.onPause();
                break;
            case BaiduMap.MAP_TYPE_NONE:
                mNoneMapView.onPause();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mStandardMapView) {
            mStandardMapView.onDestroy();
        }

        if (null != mSatelliteMapView) {
            mSatelliteMapView.onDestroy();
        }

        if (null != mNoneMapView) {
            mNoneMapView.onDestroy();
        }
    }

    private void changePageState(int position) {
        switch (position) {
            case 0:
                mCurMapType = BaiduMap.MAP_TYPE_NORMAL;
                mBtnStandardMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.heavy_color_btn));
                mBtnSatelliteMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.light_color_btn));
                mBtnNoneMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.light_color_btn));
                mStandardMapView.onResume();
                break;
            case 1:
                mCurMapType = BaiduMap.MAP_TYPE_SATELLITE;
                mBtnStandardMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.light_color_btn));
                mBtnSatelliteMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.heavy_color_btn));
                mBtnNoneMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.light_color_btn));
                mSatelliteMapView.onResume();
                break;
            case 2:
                mCurMapType = BaiduMap.MAP_TYPE_NONE;
                mBtnStandardMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.light_color_btn));
                mBtnSatelliteMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.light_color_btn));
                mBtnNoneMap
                        .setBackgroundColor(ContextCompat.getColor(this, R.color.heavy_color_btn));
                mNoneMapView.onResume();
                break;
            default:
                break;
        }
    }

    private void initView() {
        mBtnStandardMap = findViewById(R.id.scroll_btn0);
        mBtnStandardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });

        mBtnSatelliteMap = findViewById(R.id.scroll_btn1);
        mBtnSatelliteMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        mBtnNoneMap = findViewById(R.id.scroll_btn2);
        mBtnNoneMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });

        mViewPager = findViewById(R.id.scroll_page);
        View viewStandard =
                LayoutInflater.from(this).inflate(R.layout.layout_scroll_mapview0, null);
        View viewSatellite = LayoutInflater.from(this).inflate(R.layout.layout_scroll_mapview1,
                null);
        View viewNone = LayoutInflater.from(this).inflate(R.layout.layout_scroll_mapview2, null);
        views.add(viewStandard);
        views.add(viewSatellite);
        views.add(viewNone);

        initMapView(viewStandard, viewSatellite, viewNone);

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            @NonNull
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position,
                                    @NonNull Object object) {
                container.removeView(views.get(position));
            }
        });

        mViewPager.setOnPageChangeListener(this);

    }

    private void initMapView(View viewStandard, View viewSatellite, View viewNone) {
        BaiduMap baiduMap;
        if (null != viewStandard) {
            mStandardMapView = viewStandard.findViewById(R.id.mapview_standart);
            baiduMap = mStandardMapView.getMap();
            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            // 解决圆角屏幕手机，地图loggo被遮挡的问题
            baiduMap.setViewPadding(30, 0, 30, 20);
        }

        if (null != viewSatellite) {
            mSatelliteMapView = viewSatellite.findViewById(R.id.mapview_statellite);
            baiduMap = mSatelliteMapView.getMap();
            baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            // 解决圆角屏幕手机，地图loggo被遮挡的问题
            baiduMap.setViewPadding(30, 0, 30, 20);
        }

        if (null != viewNone) {
            mNoneMapView = viewNone.findViewById(R.id.mapview_none);
            baiduMap = mNoneMapView.getMap();
            baiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
            // 解决圆角屏幕手机，地图loggo被遮挡的问题
            baiduMap.setViewPadding(30, 0, 30, 20);
        }

    }

    /**
     * Called when the scroll position of a view changes.
     *
     * @param v          The view whose scroll position has changed.
     * @param scrollX    Current horizontal scroll origin.
     * @param scrollY    Current vertical scroll origin.
     * @param oldScrollX Previous horizontal scroll origin.
     * @param oldScrollY Previous vertical scroll origin.
     */
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at
     *                             position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        changePageState(position);
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
