package com.oobiliuoo.parkingtong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.utils.DistanceUtil;
import com.oobiliuoo.parkingtong.database.OrderInfo;
import com.oobiliuoo.parkingtong.net.TcpClient;
import com.oobiliuoo.parkingtong.obj.MyTime;
import com.oobiliuoo.parkingtong.ui.RegisterActivity;
import com.oobiliuoo.parkingtong.utils.Utils;
import com.oobiliuoo.parkingtong.utils.overlayutil.DrivingRouteOverlay;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author biliu
 */
public class HelloBaiduMapActivity extends BaiduMapBaseActivity {

    private static final String TAG = "HelloBMActivity";

    private BroadcastReceiver receiver;

    private Button btnSearch, btnNearby;

    /**
     * 目标点的坐标
     */
    private LatLng target;
    private int pageNum = 1;

    /**
     * 是否弹出车库信息
     */
    boolean flagParkInfoView = false;

    /**
     * 百度地图布局
     */
    private RelativeLayout relativeLayout;

    /**
     * 停车场信息布局
     */
    private View layoutParkInFo;
    private Handler mHandler;
    String orderMsg = null;

    @Override
    public void init() {
        // 检查SDK是否出错
        registerSDKCheckReceiver();

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            //在Marker拖拽过程中回调此方法，这个Marker的位置可以通过getPosition()方法获取
            //marker 被拖动的Marker对象
            @Override
            public void onMarkerDrag(Marker marker) {
                //对marker处理拖拽逻辑
            }

            //在Marker拖动完成后回调此方法， 这个Marker的位可以通过getPosition()方法获取
            //marker 被拖拽的Marker对象
            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (pageNum == 3) {
                    pageNum = 1;
                }
                poiSearch.searchNearby(getNearbySearchParams(marker.getPosition()));
            }

            //在Marker开始被拖拽时回调此方法， 这个Marker的位可以通过getPosition()方法获取
            //marker 被拖拽的Marker对象
            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });

        relativeLayout = findViewById(R.id.rl_baimap);
        findViewById(R.id.layout_park).setVisibility(View.VISIBLE);

        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    // 连接成功
                    case 1:
                        break;
                    // 连接失败
                    case 2:
                        Utils.showToast(HelloBaiduMapActivity.this, "服务器连接失败，请检查网络设置");
                        break;
                    // 接收结果
                    case 3:
                        handMsg(msg.obj.toString());
                        break;
                    default:
                        break;
                }

            }
        };

        initButton();

    }

    private void handMsg(String string) {
        // 下单成功
        if (string.equals(Utils.RESPOND_ADD_ORDER_OK)) {
            // 保持订单信息到本地
            String[] msg = orderMsg.split("::");
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderID(msg[1]);
            orderInfo.setOrderTel(msg[2]);
            orderInfo.setParkName(msg[3]);
            orderInfo.setOderTime(msg[4]);
            orderInfo.setInTime(msg[5]);
            orderInfo.setOutTime(msg[6]);
            orderInfo.setMoney(msg[7]);
            orderInfo.save();
        }
    }

    /**
     * 按键初始化
     */
    private void initButton() {
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            /**
             *  搜索按键点击事件
             * */
            @Override
            public void onClick(View v) {
                // 清除地图上的车库信息
                if (flagParkInfoView) {
                    removeParkInfoView();
                    flagParkInfoView = false;
                }
                if (pageNum == 3) {
                    pageNum = 1;
                }
                EditText editText = findViewById(R.id.et_place);
                // 在城市中搜索
                poiSearch.searchInCity(getSearchInCityParams(editText.getText().toString()));

            }
        });

        btnNearby = findViewById(R.id.btn_nearby);
        btnNearby.setOnClickListener(new View.OnClickListener() {
            /**
             *  周边按键点击事件
             * */
            @Override
            public void onClick(View v) {
                if (flagParkInfoView) {
                    removeParkInfoView();
                    flagParkInfoView = false;
                }
                if (pageNum == 3) {
                    pageNum = 1;
                }
                // 在给定点附近搜索
                poiSearch.searchNearby(getNearbySearchParams(currentPos));
            }
        });

    }

    /**
     * 城市搜索参数设置
     */
    private PoiCitySearchOption getSearchInCityParams(String keyWork) {
        PoiCitySearchOption params = new PoiCitySearchOption();
        // 指定搜索城市
        params.city("郴州");
        // 指定搜索内容
        params.keyword(keyWork);
        // 指定一页1条数据
        params.pageCapacity(1);
        // 指定获取那一页
        params.pageNum(pageNum);
        pageNum++;
        return params;
    }

    /**
     * 附近搜索参数设置
     */
    private PoiNearbySearchOption getNearbySearchParams(LatLng pos) {
        PoiNearbySearchOption params = new PoiNearbySearchOption();
        params.location(pos);
        params.radius(1000);
        params.keyword("停车场");
        params.pageCapacity(10);
        params.pageNum(pageNum);
        pageNum++;
        return params;
    }

    @Override
    public boolean onPoiClick(int i) {
        if (flagParkInfoView) {
            removeParkInfoView();
            flagParkInfoView = false;
        }
        // 当点击一个搜索出来的兴趣点后，在去搜索更详细内容
        PoiInfo poiInfo = poiOverlay.getPoiResult().getAllPoi().get(i);
        target = poiInfo.location;
        poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUids(poiInfo.getUid()));
        //addParkInfoView();

        return true;

    }

    /**
     * 停车场布局初始化
     * pName:停车场名字
     * pHours:营业时间
     * pTel:联系电话
     * pAddress:具体地址
     * distance:距离
     */
    private void initParkViewInfo(String pName, String pHours, String pAddress, String distance) {
        layoutParkInFo = LayoutInflater.from(this).inflate(R.layout.layout_parkinfo, null, false);

        TextView parkName = layoutParkInFo.findViewById(R.id.tv_porkName);
        parkName.setText(pName);

        TextView parkHours = layoutParkInFo.findViewById(R.id.tv_porkHours);
        parkHours.setText(pHours);

        TextView parkAddress = layoutParkInFo.findViewById(R.id.tv_porkAddress);
        parkAddress.setText(pAddress);

        TextView parkDistance = layoutParkInFo.findViewById(R.id.tv_porkDistance);
        parkDistance.setText(distance);

        layoutParkInFo.findViewById(R.id.btn_parkInfo_go).setOnClickListener(new View.OnClickListener() {
            /**到这去按钮点击事件*/
            @Override
            public void onClick(View v) {
                mBaiduMap.clear();
                // 驾车路线规划
                routePlanSearch.drivingSearch(new DrivingRoutePlanOption()
                        .from(PlanNode.withLocation(currentPos))
                        .to(PlanNode.withLocation(target))
                );
            }
        });

        layoutParkInFo.findViewById(R.id.btn_parkInfo_order).setOnClickListener(new View.OnClickListener() {
            /**预订按钮点击事件*/
            @Override
            public void onClick(View v) {

                // 读取当前登录帐号
                String tel = Utils.readCurrentUser(HelloBaiduMapActivity.this);
                if (tel != "") {


                    View view2 = View.inflate(HelloBaiduMapActivity.this, R.layout.layout_add_order, null);
                    new AlertDialog.Builder(HelloBaiduMapActivity.this)
                            .setTitle("确定订单")
                            .setView(view2)
                            .setMessage("确定在此地下单？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(HelloBaiduMapActivity.this, "确定" + tel, Toast.LENGTH_SHORT).show();

                                    EditText inTime = view2.findViewById(R.id.add_order_inTime);
                                    EditText outTime = view2.findViewById(R.id.add_order_outTime);
                                    TextView money = view2.findViewById(R.id.add_order_money);
                                    String in = inTime.getText().toString();
                                    String out = outTime.getText().toString();

                                    String mMoney = MyTime.calTime(in, out) * 0.5 + "";
                                    // money.setText(mMoney);

                                    new AlertDialog.Builder(HelloBaiduMapActivity.this)
                                            .setTitle("预计收费" + mMoney)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    MyTime myTime = new MyTime();
                                                    String time = myTime.getTime();
                                                    // 创建订单号
                                                    String orderNum = myTime.getXxTime() + tel.substring(7, 11);

                                                    orderMsg = Utils.REQUEST_ADD_ORDER + orderNum + Utils.DIVISION + tel
                                                            + Utils.DIVISION + pName + Utils.DIVISION + time
                                                            + Utils.DIVISION + in + Utils.DIVISION + out + Utils.DIVISION + mMoney;

                                                    Utils.sendMessage(mHandler, 3, Utils.RESPOND_ADD_ORDER_OK);

                                                    /*
                                                    // TODO 将信息发送至远程服务器
                                                    TcpClient tcpClient = new TcpClient(mHandler, Utils.IP_ADDRESS, Utils.IP_PORT);
                                                    // 请求信息
                                                    String msg = Utils.REQUEST_ADD_ORDER + orderNum + Utils.DIVISION + tel
                                                            + Utils.DIVISION + pName + Utils.DIVISION + time;
                                                    tcpClient.setSendMsg(msg);
                                                    tcpClient.send();
                                                     */

                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Toast.makeText(HelloBaiduMapActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                                }
                                            }).show();


                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Toast.makeText(HelloBaiduMapActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                } else {
                    Utils.showToast(HelloBaiduMapActivity.this, "请登录后再来");
                }

            }
        });

    }


    /**
     * 添加停车场信息布局到视图中
     */
    private void addParkInfoView() {

        relativeLayout.addView(layoutParkInFo);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParkInFo.setLayoutParams(lp);
        flagParkInfoView = true;
    }

    /**
     * 移除停车场信息布局
     */
    private void removeParkInfoView() {
        Log.i(TAG, "removeParkInfoView: ");
        relativeLayout.removeView(layoutParkInFo);
        flagParkInfoView = false;
    }


    /**
     * 验证网络和ak是否正确
     */
    private void registerSDKCheckReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR.equals(action)) {
                    Toast.makeText(HelloBaiduMapActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                } else if (SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR.equals(action)) {
                    Toast.makeText(HelloBaiduMapActivity.this, "SDK KEY 错误", Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        // 监听网络错误
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        // 监听百度地图sdk 的key是否错误
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        registerReceiver(receiver, filter);
    }


    /**
     * 兴趣点搜索的回调函数
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "没有搜索到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        mBaiduMap.clear();

        // 把数据设置给覆盖物
        poiOverlay.setData(poiResult);
        // 把所有的数据变成覆盖物添加到baiduMap中
        poiOverlay.addToMap();
        // 把所有的搜索结果在屏幕内显示出来
        poiOverlay.zoomToSpan();

        //  根据单页容量判断搜索类型
        if (poiResult.getCurrentPageCapacity() == 1) {
            mBaiduMap.clear();
            //定义Maker坐标点
            LatLng point = poiOverlay.getPoiResult().getAllPoi().get(0).location;
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.location);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    //必传参数 绘制点坐标
                    .position(point)
                    //必传参数 覆盖物图标
                    .icon(bitmap)
                    .draggable(true)
                    //设置平贴地图，在地图中双指下拉查看效果
                    .flat(true);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);

        }
    }


    /**
     * 兴趣点详细搜索的回调函数
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult == null || poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "没有搜索到详细信息", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onGetPoiDetailResult: " + poiDetailSearchResult.error.toString());
            return;
        }
        // 获取结果列表
        List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
        // 计算距离
        int d = (int) DistanceUtil.getDistance(currentPos, poiDetailInfoList.get(0).getLocation());

        String hours = poiDetailInfoList.get(0).getShopHours();
        if (hours == "") {
            hours = "0:00-0:00";
        }
        // 将得到的信息传给停车场布局
        initParkViewInfo(poiDetailInfoList.get(0).getName(), hours
                , poiDetailInfoList.get(0).getAddress(), d + "");

        addParkInfoView();

    }


    /**
     * 驾车路线规划
     */
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
        mBaiduMap.setOnMarkerClickListener(overlay);
        // 获取所以的搜索路线,最优的在前
        List<DrivingRouteLine> routeLines = drivingRouteResult.getRouteLines();
        overlay.setData(routeLines.get(0));
        overlay.addToMap();         // 将覆盖物添加到地图
        overlay.zoomToSpan();       // 搜索结果在一个屏幕显示完

    }

    @Override
    public void onBackPressed() {
        if (flagParkInfoView) {
            removeParkInfoView();
            Log.i(TAG, "onBackPressed: ");
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }
}