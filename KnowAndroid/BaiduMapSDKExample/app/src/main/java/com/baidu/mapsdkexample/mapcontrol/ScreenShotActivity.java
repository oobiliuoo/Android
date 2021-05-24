package com.baidu.mapsdkexample.mapcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkexample.R;

import java.io.File;

public class ScreenShotActivity extends AppCompatActivity implements BaiduMap.SnapshotReadyCallback
        , View.OnClickListener {

    private MapView mMapView;
    private TextView mMarkerDesTv;
    private TextView mMarkerNameTv;
    private BaiduMap mBaiduMap;
    private ViewGroup mViewGroup;
    private LinearLayout mLinearLayout;
    private PopupWindow mPopupWindow;
    private BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 0) {
                if (null != mPopupWindow) {
                    mPopupWindow.dismiss();
                }
            } else if (message.what == 1) {
                showScreenshot();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
        initView();
        addMarker();
    }

    private void initView() {
        Button screenshotBtn = findViewById(R.id.screenshot);
        mMapView = findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mViewGroup = findViewById(R.id.screenshot_group);
        mMarkerDesTv = findViewById(R.id.marker_describe);
        mMarkerNameTv = findViewById(R.id.marker_name);
        mLinearLayout = findViewById(R.id.screen_linear);
        screenshotBtn.setOnClickListener(this);
        mMapView.showZoomControls(false);
        // 解决圆角屏幕手机，地图loggo被遮挡的问题
        mMapView.setPadding(30, 0, 30, 20);
    }

    private void addMarker() {
        LatLng latLng = new LatLng( 39.947583 , 116.391128 );
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(bitmap) // 设置 Marker 覆盖物的图标
                .zIndex(9); // 设置 marker 覆盖物的 zIndex
        mBaiduMap.addOverlay(markerOptions);
        mMarkerNameTv.setText("什刹海公园");
        mMarkerDesTv.setText("地址: " + "北京市西城区地安门西大街49号" );
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
        bitmap.recycle();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.screenshot) {
            mBaiduMap.snapshot(this);
        }
    }

    /**
     * 显示PopupWindow
     */
    private void showScreenshot() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setContentView(contentView);
        ImageView imageView = contentView.findViewById(R.id.screenshot_image);
        Bitmap bitmap = getBitmap(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "test.png");
        if ( bitmap != null ) {
            mPopupWindow.setWidth(bitmap.getWidth() / 2);
            mPopupWindow.setHeight(bitmap.getHeight() / 2);
            mPopupWindow.setOutsideTouchable(true);
            imageView.setImageBitmap(bitmap);
            // 显示mPopupWindow
            View rootview = LayoutInflater.from(this).inflate(R.layout.activity_screen_shot, null);
            mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
            mHandler.sendEmptyMessageDelayed( 0 ,  3000 );
        } else {
            Toast.makeText( this , "没有图片" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        ScreenShotHelper.saveScreenShot(bitmap, mViewGroup, mMapView, mLinearLayout, mMapView);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        Toast.makeText(getApplicationContext(), " SD卡的 " + path + " 目录下查看截图后的文件 ", Toast.LENGTH_SHORT).show();
        mHandler.sendEmptyMessageDelayed( 1 , 1000 );
    }

    /**
     * 获取bitmap
     *
     * @param pathString 图片存储路径
     * @return 返回bitmap
     */
    private Bitmap getBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
