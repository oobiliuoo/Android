package com.baidu.mapsdkexample.mapshow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapsdkexample.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomMapActivity extends AppCompatActivity implements View.OnClickListener {

    // 用于设置个性化地图的样式文件
    private static final String CUSTOM_FILE_NAME_GRAY = "custom_blacknight.sty";
    private static final String CUSTOM_FILE_NAME_WHITE = "custom_trip.sty";

    // 地图View实例
    private MapView mMapView;
    private Button mBlackMapBtn;
    private Button mWhiteMapBtn;
    private Button mCloseCustomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);
        initView();
    }

    // 初始化View
    private void initView() {
        mBlackMapBtn = findViewById(R.id.black_map);
        mWhiteMapBtn = findViewById(R.id.white_map);
        mCloseCustomBtn = findViewById(R.id.close_custom);
        mBlackMapBtn.setOnClickListener(this);
        mWhiteMapBtn.setOnClickListener(this);
        mCloseCustomBtn.setOnClickListener(this);
        mBlackMapBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_nopress));
        mMapView = findViewById(R.id.bmap_view);
        // 开启夜间模式个性化样式
        String customStyleFilePath = getCustomStyleFilePath(this, CUSTOM_FILE_NAME_GRAY);
        mMapView.setMapCustomStylePath(customStyleFilePath);
        mMapView.setMapCustomStyleEnable(true);
        // 解决圆角屏幕手机，地图loggo被遮挡的问题
        mMapView.setPadding(30, 0, 30, 20);
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
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }

    /**
     * 获取个性化地图存储路径
     */
    private String getCustomStyleFilePath(Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;

        try {
            inputStream = context.getAssets().open(customStyleFileName);
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
            Log.e("CustomMapDemo", "Copy custom style file failed", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("CustomMapDemo", "Close stream failed", e);
            }
        }

        return parentPath + "/" + customStyleFileName;
    }

    @Override
    public void onClick(View view) {
        String customStyleFilePath;
        switch (view.getId()) {
            case R.id.white_map:
                // 开启日间模式个性化样式
                customStyleFilePath = getCustomStyleFilePath(this, CUSTOM_FILE_NAME_WHITE);
                mMapView.setMapCustomStylePath(customStyleFilePath);
                mMapView.setMapCustomStyleEnable(true);
                mWhiteMapBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_nopress));
                mBlackMapBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_press));
                mCloseCustomBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_press));
                break;
            case R.id.black_map:
                // 开启夜间模式个性化样式
                customStyleFilePath = getCustomStyleFilePath(this, CUSTOM_FILE_NAME_GRAY);
                mMapView.setMapCustomStylePath(customStyleFilePath);
                mMapView.setMapCustomStyleEnable(true);
                mWhiteMapBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_press));
                mBlackMapBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_nopress));
                mCloseCustomBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_press));
                break;
            case R.id.close_custom:
                // 关闭个性化样式
                mMapView.setMapCustomStyleEnable(false);
                mWhiteMapBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_press));
                mBlackMapBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_press));
                mCloseCustomBtn.setBackground(getResources().getDrawable(R.drawable.custom_map_btn_nopress));
                break;
            default:
                Log.e("CustomMapDemo", "Invalid check");
                break;
        }
    }
}
