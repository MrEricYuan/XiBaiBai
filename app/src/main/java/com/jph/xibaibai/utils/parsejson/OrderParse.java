package com.jph.xibaibai.utils.parsejson;

import android.util.Log;

import com.jph.xibaibai.model.entity.ArtificerRecommand;
import com.jph.xibaibai.model.entity.ConfirmPay;
import com.jph.xibaibai.model.entity.MyOrder;
import com.jph.xibaibai.model.entity.MyOrderInformation;
import com.jph.xibaibai.model.entity.Product;
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

    /**
     * 解析订单列表数据
     *
     * @param data
     * @return
     */
    public List<MyOrder> parseMyOrderList(String data) {
        Log.v("parse", "data:" + data);
        if (StringUtil.isNull(data))
            return null;
        List<MyOrder> myOrderList = null;
        try {
            JSONArray mArray = new JSONArray(data);
            if (mArray != null && mArray.length() > 0) {
                myOrderList = new ArrayList<MyOrder>();
                for (int i = 0; i < mArray.length(); i++) {
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
                        if (mObject.has("servicetime") && !mObject.isNull("servicetime") && !StringUtil.isNull(mObject.getString("servicetime")))
                            myOrder.setServiceTime(mObject.getString("servicetime"));
                        myOrderList.add(myOrder);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myOrderList;
    }


    /**
     * 解析订单详情
     *
     * @param data
     * @return
     */
    public MyOrderInformation parseOrderInfo(String data) {
        MyOrderInformation orderinfo = null;
        try {
            JSONObject mObject = new JSONObject(data);
            if (mObject != null) {
                orderinfo = new MyOrderInformation();
                if (mObject.has("id") && !mObject.isNull("id") && !StringUtil.isNull(mObject.getString("id")))
                    orderinfo.setOrderId(mObject.getString("id"));
                if (mObject.has("order_state") && !mObject.isNull("order_state") && StringUtil.isNumeric(mObject.getString("order_state")))
                    orderinfo.setState(Integer.parseInt(mObject.getString("order_state")));
                if (mObject.has("orderstate") && !mObject.isNull("orderstate") && !StringUtil.isNull(mObject.getString("orderstate")))
                    orderinfo.setOrderSate(mObject.getString("orderstate"));
                if (mObject.has("location") && !mObject.isNull("location") && !StringUtil.isNull(mObject.getString("location")))
                    orderinfo.setCarLocation(mObject.getString("location"));
                if (mObject.has("order_num") && !mObject.isNull("order_num") && !StringUtil.isNull(mObject.getString("order_num")))
                    orderinfo.setOrderNo(mObject.getString("order_num"));
                if (mObject.has("pay_type") && !mObject.isNull("pay_type") && !StringUtil.isNull(mObject.getString("pay_type")))
                    orderinfo.setPayType(mObject.getString("pay_type"));
                if (mObject.has("carinfo") && !mObject.isNull("carinfo") && !StringUtil.isNull(mObject.getString("carinfo")))
                    orderinfo.setCarInfo(mObject.getString("carinfo"));
                if (mObject.has("c_plate_num") && !mObject.isNull("c_plate_num") && !StringUtil.isNull(mObject.getString("c_plate_num")))
                    orderinfo.setCarplateNo(mObject.getString("c_plate_num"));
                if (mObject.has("cartype") && !mObject.isNull("cartype") && !StringUtil.isNull(mObject.getString("cartype")))
                    orderinfo.setCarType(mObject.getString("cartype"));
                if (mObject.has("uname") && !mObject.isNull("uname") && !StringUtil.isNull(mObject.getString("uname")))
                    orderinfo.setDriverName(mObject.getString("uname"));
                if (mObject.has("emp_name") && !mObject.isNull("emp_name") && !StringUtil.isNull(mObject.getString("emp_name")))
                    orderinfo.setArtificerName(mObject.getString("emp_name"));
                if (mObject.has("emp_iphone") && !mObject.isNull("emp_iphone") && !StringUtil.isNull(mObject.getString("emp_iphone")))
                    orderinfo.setArtificerTel(mObject.getString("emp_iphone"));
                if (mObject.has("p_order_time") && !mObject.isNull("p_order_time") && !StringUtil.isNull(mObject.getString("p_order_time")))
                    orderinfo.setOrderTime(mObject.getString("p_order_time"));
                if (mObject.has("totalprice") && !mObject.isNull("totalprice") && !StringUtil.isNull(mObject.getString("totalprice")))
                    orderinfo.setOrderPrice(mObject.getString("totalprice"));
                if (mObject.has("pay_num") && !mObject.isNull("pay_num") && !StringUtil.isNull(mObject.getString("pay_num")))
                    orderinfo.setPayPrice(mObject.getString("pay_num"));
                if (mObject.has("servicetime") && !mObject.isNull("servicetime") && !StringUtil.isNull(mObject.getString("servicetime")))
                    orderinfo.setServiceTime(mObject.getString("servicetime"));
                if (mObject.has("coupons") && !mObject.isNull("coupons") && !StringUtil.isNull(mObject.getString("coupons")))
                    orderinfo.setCouponOffset(mObject.getString("coupons"));
                if (mObject.has("washimg") && !mObject.isNull("washimg")) {
                    JSONObject jsonObject = mObject.getJSONObject("washimg");
                    if (jsonObject != null) {
                        if (jsonObject.has("before") && !jsonObject.isNull("before") && !StringUtil.isNull(jsonObject.getString("before"))) {
                            JSONArray oArray = jsonObject.getJSONArray("before");
                            if (oArray != null && oArray.length() > 0) {
                                List<String> beforeAlbum = new ArrayList<String>();
                                for (int i = 0; i < oArray.length(); i++) {
                                    Object o = oArray.get(i);
                                    String path = o == null ? "" : o.toString();
                                    if (!StringUtil.isNull(path))
                                        beforeAlbum.add(path);
                                }
                                orderinfo.setBeforeAlbum(beforeAlbum);
                            }
                        }
                        if (jsonObject.has("after") && !jsonObject.isNull("after") && !StringUtil.isNull(jsonObject.getString("after"))) {
                            JSONArray oArray = jsonObject.getJSONArray("after");
                            if (oArray != null && oArray.length() > 0) {
                                List<String> afterAlbum = new ArrayList<String>();
                                for (int i = 0; i < oArray.length(); i++) {
                                    Object o = oArray.get(i);
                                    String path = o == null ? "" : o.toString();
                                    if (!StringUtil.isNull(path))
                                        afterAlbum.add(path);
                                }
                                orderinfo.setAfterAlbum(afterAlbum);
                            }
                        }
                    }
                }
                if (mObject.has("advice") && !mObject.isNull("advice") && !StringUtil.isNull(mObject.getString("advice"))) {
                    JSONArray mArray = mObject.getJSONArray("advice");
                    if (mArray != null && mArray.length() > 0) {
                        List<ArtificerRecommand> recommandList = new ArrayList<ArtificerRecommand>();
                        for (int i = 0; i < mArray.length(); i++) {
                            JSONObject jsonObject = mArray.getJSONObject(i);
                            if (jsonObject != null) {
                                ArtificerRecommand recommand = new ArtificerRecommand();
                                Product product = new Product();
                                recommand.setDiyData(product);
                                if (jsonObject.has("p_id") && !jsonObject.isNull("p_id") && !StringUtil.isNumeric(jsonObject.getString("p_id")))
                                    recommand.getDiyData().setId(Integer.parseInt(jsonObject.getString("p_id")));
                                if (jsonObject.has("p_name") && !jsonObject.isNull("p_name") && !StringUtil.isNull(jsonObject.getString("p_name")))
                                    recommand.getDiyData().setP_name(jsonObject.getString("p_name"));
                                if (jsonObject.has("remark") && !jsonObject.isNull("remark") && !StringUtil.isNull(jsonObject.getString("remark")))
                                    recommand.setRemark(jsonObject.getString("remark"));
                                if (jsonObject.has("images") && !jsonObject.isNull("images")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        List<String> explainAlbum = new ArrayList<String>();
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            Object o = jsonArray.get(i);
                                            String path = o == null ? "" : o.toString();
                                            if (!StringUtil.isNull(path))
                                                explainAlbum.add(path);
                                        }
                                        recommand.setExplainAlbum(explainAlbum);
                                    }
                                }
                                recommandList.add(recommand);

                            }

                        }
                        orderinfo.setRecommandList(recommandList);
                    }
                }
                if (mObject.has("prolist") && !mObject.isNull("prolist") && !StringUtil.isNull(mObject.getString("prolist"))) {
                    JSONArray mArray = mObject.getJSONArray("prolist");
                    if (mArray != null && mArray.length() > 0) {
                        Log.v("parse", "marry'size()=" + mArray.length());
                        List<Product> productList = new ArrayList<Product>();
                        for (int i = 0; i < mArray.length(); i++) {
                            JSONObject jsonObject = mArray.getJSONObject(i);
                            if (jsonObject != null) {
                                Product product = new Product();
                                if (jsonObject.has("p_name") && !jsonObject.isNull("p_name") && !StringUtil.isNull(jsonObject.getString("p_name")))
                                    product.setP_name(jsonObject.getString("p_name"));
                                if (jsonObject.has("price") && !jsonObject.isNull("price") && !StringUtil.isNull(jsonObject.getString("price")))
                                    product.setP_price(Double.parseDouble(jsonObject.getString("price")));
                                productList.add(product);
                            }


                        }
                        Log.v("parse", "prolist'size()=" + productList.size());
                        orderinfo.setServiceList(productList);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("parse", "info-error:" + e.getMessage());
        }

        return orderinfo;
    }

    /**
     * 解析优惠券确认结果
     *
     * @param data
     * @return
     */
    public ConfirmPay parseConfirmPay(String data) {
        if (StringUtil.isNull(data))
            return null;
        ConfirmPay confirm = null;
        try {
            JSONObject mObject = new JSONObject(data);
            if (mObject != null) {
                confirm = new ConfirmPay();
                if (mObject.has("") && !mObject.isNull("") && !StringUtil.isNull(mObject.getString("")))
                    confirm.setPayPrice(mObject.getString(""));
                if (mObject.has("") && !mObject.isNull("") && !StringUtil.isNull(mObject.getString("")))
                    confirm.setCouponPrice(mObject.getString(""));
                if (mObject.has("") && !mObject.isNull("") && !StringUtil.isNull(mObject.getString("")))
                    confirm.setExtra(mObject.getString(""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("parse", "confrim-error:" + e.getMessage());
        }

        return confirm;
    }
}
