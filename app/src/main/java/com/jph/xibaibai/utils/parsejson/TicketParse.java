package com.jph.xibaibai.utils.parsejson;

import com.jph.xibaibai.model.entity.Coupon;
import com.jph.xibaibai.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/7 12:01
 * 描述：$TODO
 */
public class TicketParse {
    public static List<Coupon> getCouponList(String json) {
        if (StringUtil.isNull(json)) return null;
        List<Coupon> ticketList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray != null && jsonArray.length() > 0) {
                ticketList = new ArrayList<Coupon>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject mObject = jsonArray.getJSONObject(i);
                    if (mObject != null) {
                        Coupon coupon = new Coupon();
                        if (mObject.has("id") && !mObject.isNull("id") && !StringUtil.isNull(mObject.getString("id")))
                            coupon.setId(Integer.parseInt(mObject.getString("id")));
                        if (mObject.has("number") && !mObject.isNull("number") && !StringUtil.isNull(mObject.getString("number")))
                            coupon.setNumber(Integer.parseInt(mObject.getString("number")));
                        if (mObject.has("type") && !mObject.isNull("type") && !StringUtil.isNull(mObject.getString("type")))
                            coupon.setType(Integer.parseInt(mObject.getString("type")));
                        if (mObject.has("state") && !mObject.isNull("state") && !StringUtil.isNull(mObject.getString("state")))
                            coupon.setType(Integer.parseInt(mObject.getString("state")));
                        if (mObject.has("coupons_price") && !mObject.isNull("coupons_price") && !StringUtil.isNull(mObject.getString("coupons_price")))
                            coupon.setCoupons_price(mObject.getString("coupons_price"));
                        if (mObject.has("coupons_name") && !mObject.isNull("coupons_name") && !StringUtil.isNull(mObject.getString("coupons_name")))
                            coupon.setCoupons_name(mObject.getString("coupons_name"));
                        if (mObject.has("coupons_remark") && !mObject.isNull("coupons_remark") && !StringUtil.isNull(mObject.getString("coupons_remark")))
                            coupon.setCoupons_remark(mObject.getString("coupons_remark"));
                        if (mObject.has("effective_time") && !mObject.isNull("effective_time") && !StringUtil.isNull(mObject.getString("effective_time")))
                            coupon.setEffective_time(mObject.getString("effective_time"));
                        if (mObject.has("expired_time") && !mObject.isNull("expired_time") && !StringUtil.isNull(mObject.getString("expired_time")))
                            coupon.setExpired_time(mObject.getString("expired_time"));
                        if (mObject.has("server_time") && !mObject.isNull("server_time") && !StringUtil.isNull(mObject.getString("server_time")))
                            coupon.setServer_time(mObject.getString("server_time"));
                        ticketList.add(coupon);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketList;
    }
}
