package com.jph.xibaibai;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.jph.xibaibai.model.http.LocationBean;
import com.jph.xibaibai.utils.sp.SharePerferenceUtil;

/**
 * Created by Administrator on 2015/7/29.
 */
public class XBBApplication extends Application {

    public LocationClient mLocationClient;

    public GeofenceClient mGeofenceClient;

    public MyLocationListener mMyLocationListener;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        initLocate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopLocate();
    }

    /**初始化地图信息*/
    private void initLocate() {
        Log.i("Tag","初始化地图！");
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mGeofenceClient = new GeofenceClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(15 * 1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }
    /**停止定位*/
    public void stopLocate() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
            mLocationClient.stop();
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                Log.i("Tag", "Application==>" + location.getLatitude() + " " + location.getLongitude()
                        + "" + location.getCity() + location.getAddrStr());
                LocationBean locationBean = new LocationBean();
                locationBean.setCity(location.getCity());
                locationBean.setLat("" + location.getLatitude());
                locationBean.setLon("" + location.getLongitude());
                locationBean.setDetailAddress(location.getAddrStr());
                SharePerferenceUtil.setLocationInfo(getApplicationContext(),
                        locationBean);
                stopLocate();
            }else {
                //定位失败
            }
        }
    }
}
