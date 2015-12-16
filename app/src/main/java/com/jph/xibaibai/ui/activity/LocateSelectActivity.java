package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Address;
import com.jph.xibaibai.model.entity.AllAddress;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Eric on 2015/12/9.
 */
public class LocateSelectActivity extends TitleActivity implements BDLocationListener,
        BaiduMap.OnMapStatusChangeListener, OnGetGeoCoderResultListener, BaiduMap.OnMapLoadedCallback, View.OnClickListener {

    public static String WHEREINTO = "where_into_locate";

    private BaiduMap mBaiduMap;
    private LocationClient locationClient;
    private GeoCoder geoCoder;
    //当前位置
    private LatLng latLngCurrent;

    private final int remarkCode = 1010;

    private Address addressRecode = null;

    private AllAddress allAddress = null;

    private int whereInto = 0;

    @ViewInject(R.id.locate_main_map)
    private MapView mapView;
    @ViewInject(R.id.locate_detail_show)
    private TextView txtAddress;
    @ViewInject(R.id.title_btn_right)
    private Button title_btn_right;
    @ViewInject(R.id.locate_home_img)
    ImageView locate_home_img;
    @ViewInject(R.id.locate_company_img)
    ImageView locate_company_img;
    @ViewInject(R.id.locate_remark_tv)
    TextView locate_remark_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_select);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        if (locationClient != null) {
            locationClient.start();
        }
    }

    @Override
    public void initData() {
        super.initData();
        whereInto = getIntent().getIntExtra(WHEREINTO,1);
        Log.i("Tag","WHEREINTO=>"+whereInto);
        String addressData = SPUserInfo.getsInstance(this).getSP(SPUserInfo.KEY_ALL_ADDRESS);
        initMapData();
        if (!StringUtil.isNull(addressData)) {
            allAddress = JSON.parseObject(addressData, AllAddress.class);
        }
    }

    private void initMapData() {
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(20000);
        locationClient.setLocOption(option);
        geoCoder = GeoCoder.newInstance();
        int childCount = mapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        if(zoom != null){
            zoom.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("地图位置");
        title_btn_right.setVisibility(View.VISIBLE);
        if(whereInto == 1){ // 跳进下单页面
            title_btn_right.setText(getString(R.string.place_order));
        }else if(whereInto == 2){// 回到下单页面
            title_btn_right.setText(getString(R.string.DIYSub_submit));
        }else {
            title_btn_right.setText(getString(R.string.DIYSub_submit));
            locate_home_img.setVisibility(View.INVISIBLE);
            locate_company_img.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        mapView.getMap().setOnMapLoadedCallback(this);
        mapView.getMap().setOnMapStatusChangeListener(this);
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
        }
        mapView.onDestroy();
        mapView = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case remarkCode:
                if (addressRecode != null) {
                    addressRecode.setAddress_info(data.getStringExtra("getRemarks"));
                    locate_remark_tv.setText(data.getStringExtra("getRemarks"));
                }
                break;
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null || mapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        mapView.getMap().setMyLocationData(locData);
        LatLng latLng = new LatLng(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        Log.i("Tag", "getLatitude=>" + bdLocation.getLatitude() + "/getLongitude=>" + bdLocation.getLongitude());
        if (latLngCurrent == null) {
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
            mapView.getMap().animateMapStatus(u);
        }
        latLngCurrent = latLng;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLngCurrent));
        if (locationClient != null) {
            locationClient.stop();
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (addressRecode == null) {
            addressRecode = new Address();
        }
        txtAddress.setText(reverseGeoCodeResult.getAddress());
        addressRecode.setAddress(reverseGeoCodeResult.getAddress());
        addressRecode.setAddress_lt(reverseGeoCodeResult.getLocation().latitude + "");
        addressRecode.setAddress_lg(reverseGeoCodeResult.getLocation().longitude + "");
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus.target));
    }

    @Override
    protected void onClickTitleRight(View v) {
        super.onClickTitleRight(v);
        switch (whereInto){
            case 1: //首页右上角进入
                Intent intent = new Intent(LocateSelectActivity.this,PlaceOrdersActivity.class);
                intent.putExtra(PlaceOrdersActivity.MAPADDRESS, addressRecode);
                startActivity(intent);
                break;
            case 2:// 下单界面和H5详情进入
                sendFinishBroadCast();
                finish();
                break;
            case 3:
                Intent intents = new Intent();
                intents.putExtra(AddressActivity.RESULT_ADDRESS,addressRecode);
                setResult(RESULT_OK,intents);
                finish();
                break;
        }

    }

    private void sendFinishBroadCast() {
        LocalBroadcastManager lBManager = null;
        lBManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("com.xbb.broadcast.UPDATE_ADDRESS");
        intent.putExtra("LocateAddress", addressRecode);
        lBManager.sendBroadcast(intent);//调用sendBroadcast()方法发送广播
    }

    @OnClick({R.id.locate_remark_btn, R.id.title_btn_right, R.id.locate_home_img, R.id.locate_company_img, R.id.locate_current_img})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.locate_remark_btn://备注
                intent.setClass(LocateSelectActivity.this, LocateRemarkActivity.class);
                startActivityForResult(intent, remarkCode);
                break;
            case R.id.locate_home_img:
                if (allAddress != null && allAddress.getHomeAddress() != null &&
                        allAddress.getHomeAddress().getAddress_lt() != null && allAddress.getHomeAddress().getAddress_lg() != null) {
                    MapStatusUpdate uHome = MapStatusUpdateFactory.newLatLng(
                            new LatLng(Double.valueOf(allAddress.getHomeAddress().getAddress_lt()),
                                    Double.valueOf(allAddress.getHomeAddress().getAddress_lg())));
                    mapView.getMap().animateMapStatus(uHome);
                } else {
                    showToast("请设置地址");
                    startActivity(AddressActivity.class);
                }
                break;
            case R.id.locate_company_img:
                //定位到公司
                if (allAddress != null && allAddress.getCompanyAddress() != null &&
                        allAddress.getCompanyAddress().getAddress_lt() != null && allAddress.getCompanyAddress().getAddress_lg() != null) {
                    MapStatusUpdate uCompany = MapStatusUpdateFactory.newLatLng(
                            new LatLng(Double.valueOf(allAddress.getCompanyAddress().getAddress_lt()),
                                    Double.valueOf(allAddress.getCompanyAddress().getAddress_lg())));
                    mapView.getMap().animateMapStatus(uCompany);
                } else {
                    showToast("请设置地址");
                    startActivity(AddressActivity.class);
                }
                break;
            case R.id.locate_current_img:
                if(latLngCurrent != null){
                    MapStatusUpdate uCompany = MapStatusUpdateFactory.newLatLng(
                            new LatLng(latLngCurrent.latitude,latLngCurrent.longitude));
                    mapView.getMap().animateMapStatus(uCompany);
                } else {
                    showToast("未成功定位");
                }
                break;
        }
    }
}
