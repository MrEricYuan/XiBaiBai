package com.jph.xibaibai.utils.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.jph.xibaibai.model.http.LocationBean;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;

/**
 * Created by Eric on 2015/11/20.
 */
public class SharePerferenceUtil {
    /*** 版本更新状态 ***/
    public static void saveState(Context con, boolean state) {
        SharedPreferences sp = con.getSharedPreferences("UpdateState",
                Context.MODE_PRIVATE);
        sp.edit().putBoolean("state", state).commit();
    }
    /**
     * 保存当前更新提示的时间
     */
    public static void setVersionTime(Context con,long time) {
        SharedPreferences sp = con.getSharedPreferences("VersionTime",
                Context.MODE_PRIVATE);
        sp.edit().putLong("time", time).commit();
    }

    /**
     * 更新清除信息
     * @param con
     */
    public static void clear(Context con) {
        SharedPreferences sp = con.getSharedPreferences("UpdateState",
                Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
    /**
     * 保存当前位置信息
     * @param context
     * @param locationBean
     */
    public static void setLocationInfo(Context context,
                                       LocationBean locationBean) {
        SharedPreferences defaultSharedPreferences = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        if (null == locationBean) {
        } else {
            SharedPreferences.Editor editor = defaultSharedPreferences.edit();
            if (!StringUtil.isNull(locationBean.getCity())) {
                editor.putString("city", locationBean.getCity());
            }
            if (!StringUtil.isNull(locationBean.getDetailAddress())) {
                editor.putString("detailAddress", locationBean.getDetailAddress());
            }
            if (!StringUtil.isNull(locationBean.getLon()) && !StringUtil.isNull(locationBean.getLat())) {
                editor.putString("lon", locationBean.getLon()).putString("lat",
                        locationBean.getLat());
            }
            editor.commit();
        }
    }
    /**
     * 获取保存的位置信息
     * @param context
     * @return
     */
    public static LocationBean getLocationInfo(Context context) {
        SharedPreferences defaultSharedPreferences = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        LocationBean locationBean = new LocationBean();
        locationBean.setLon(defaultSharedPreferences.getString("lon", null));
        locationBean.setLat(defaultSharedPreferences.getString("lat", null));
        locationBean.setCity(defaultSharedPreferences.getString("city", null));
        locationBean.setDetailAddress(defaultSharedPreferences.getString("detailAddress", null));
        return locationBean;
    }
}
