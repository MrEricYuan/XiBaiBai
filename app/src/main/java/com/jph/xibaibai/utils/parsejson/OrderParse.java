package com.jph.xibaibai.utils.parsejson;

import com.jph.xibaibai.model.entity.MyOrder;
import com.jph.xibaibai.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/9 11:56
 * 描述：订单解析类
 */
public class OrderParse {

    private static OrderParse instance = null;

    public static OrderParse getInstance() {
        if (instance == null)
            instance = new OrderParse();
        return instance;
    }

    public List<MyOrder> parseMyOrderList(String data) {
        if (StringUtil.isNull(data))
            return null;
        List<MyOrder> myOrderList = null;
        try {
            JSONArray mArray = new JSONArray(data);
            if (mArray != null && mArray.length() > 0) {
                for (int i = 0; i < mArray.length(); i++) {
                    myOrderList = new ArrayList<MyOrder>();
                    JSONObject mObject = mArray.getJSONObject(i);
                    if (mObject != null) {
                        MyOrder myOrder = new MyOrder();
                        if (mObject.has("order_name") && !mObject.isNull("order_name") && !StringUtil.isNull(mObject.getString("order_name")))
                            myOrder.setOrderName(mObject.getString("order_name"));
                        if (mObject.has("p_order_time") && !mObject.isNull("p_order_time") && !StringUtil.isNull(mObject.getString("p_order_time")))
                            myOrder.setOrderTime(mObject.getString("p_order_time"));
                        if (mObject.has("location") && !mObject.isNull("location") && !StringUtil.isNull(mObject.getString("location")))
                            myOrder.setCarLocation(mObject.getString("location"));
                        if (mObject.has("total_price") && !mObject.isNull("total_price") && !StringUtil.isNull(mObject.getString("total_price")))
                            myOrder.setPrice(mObject.getString("total_price"));
                        if (mObject.has("carinfo") && !mObject.isNull("carinfo") && !StringUtil.isNull(mObject.getString("carinfo")))
                            myOrder.setCarInfo(mObject.getString("carinfo"));
                        if (mObject.has("cartype") && !mObject.isNull("cartype") && !StringUtil.isNull(mObject.getString("cartype")))
                            myOrder.setCarType(mObject.getString("cartype"));
                        if (mObject.has("c_plate_num") && !mObject.isNull("c_plate_num") && !StringUtil.isNull(mObject.getString("c_plate_num")))
                            myOrder.setCarPlateNo(mObject.getString("c_plate_num"));
                        if (mObject.has("id") && !mObject.isNull("id") && !StringUtil.isNull(mObject.getString("id")))
                            myOrder.setOrderId(mObject.getString("id"));
                        if (mObject.has("orderstate") && !mObject.isNull("orderstate") && !StringUtil.isNull(mObject.getString("orderstate")))
                            myOrder.setState(mObject.getString("orderstate"));
                        if (mObject.has("order_state") && !mObject.isNull("order_state") && !StringUtil.isNull(mObject.getString("order_state")))
                            myOrder.setCurrentState(Integer.parseInt(mObject.getString("order_state")));
                        myOrderList.add(myOrder);

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myOrderList;
    }
}
