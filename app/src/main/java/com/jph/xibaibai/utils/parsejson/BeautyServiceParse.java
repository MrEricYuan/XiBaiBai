package com.jph.xibaibai.utils.parsejson;

import android.util.Log;

import com.jph.xibaibai.model.entity.BeautyItemProduct;
import com.jph.xibaibai.model.entity.BeautyService;
import com.jph.xibaibai.model.entity.DIYMeals;
import com.jph.xibaibai.model.entity.MyCoupons;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/11/9.
 * 美容服务的数据解析
 */
public class BeautyServiceParse {
    /**
     * 解析美容中打蜡和非必洗的项目
     * @param json
     * @return
     */
    public static BeautyService getResult(String json) {
        BeautyService beautyService = new BeautyService();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("wax") && !StringUtil.isNull(jsonObject.getString("wax"))) {
                JSONObject waxJsonb = new JSONObject(jsonObject.getString("wax"));
                if (waxJsonb.has("result") && !StringUtil.isNull(waxJsonb.getString("result"))) {
                    JSONArray waxJArray = waxJsonb.getJSONArray("result");
                    List<Product> waxList = null;
                    if (waxJArray != null && waxJArray.length() > 0) {
                        waxList = new ArrayList<>();
                        for (int i = 0; i < waxJArray.length(); i++) {
                            JSONObject job = (JSONObject) waxJArray.get(i);
                            Product product = DIYSubParse.commonMethod(job);
                            waxList.add(product);
                        }
                    }
                    beautyService.setWaxList(waxList);
                }
            }

            if (jsonObject.has("notwash") && !StringUtil.isNull(jsonObject.getString("notwash"))) {
                JSONObject notwashJsonb = new JSONObject(jsonObject.getString("notwash"));
                if (notwashJsonb.has("result") && !StringUtil.isNull(notwashJsonb.getString("result"))) {
                    JSONArray notwashJArray = notwashJsonb.getJSONArray("result");
                    List<Product> notWashList = null;
                    if (notwashJArray != null && notwashJArray.length() > 0) {
                        notWashList = new ArrayList<>();
                        for (int i = 0; i < notwashJArray.length(); i++) {
                            JSONObject job = (JSONObject) notwashJArray.get(i);
                            Product product = DIYSubParse.commonMethod(job);
                            notWashList.add(product);
                        }
                    }
                    beautyService.setNotWashList(notWashList);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beautyService;
    }

    public static List<BeautyItemProduct> getWashInfo(String json) {
        List<BeautyItemProduct> list = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.has("washinfo") && !StringUtil.isNull(jsonObject.getString("washinfo"))) {
                JSONArray groupArray = jsonObject.getJSONArray("washinfo");
                list = commonParse(groupArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<BeautyItemProduct> commonParse(JSONArray jsonArray) throws JSONException {
        List<BeautyItemProduct> itemList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            BeautyItemProduct itemProduct = new BeautyItemProduct();
            JSONObject itemJson = (JSONObject) jsonArray.get(i);
            if (itemJson.has("id") && !StringUtil.isNull(itemJson.getString("id"))) {
                itemProduct.setId(itemJson.getString("id"));
            }
            if (itemJson.has("p_name") && !StringUtil.isNull(itemJson.getString("p_name"))) {
                itemProduct.setP_name(itemJson.getString("p_name"));
            }
            if (itemJson.has("p_price") && !StringUtil.isNull(itemJson.getString("p_price"))) {
                itemProduct.setP_price(itemJson.getString("p_price"));
            }
            if (itemJson.has("p_price2") && !StringUtil.isNull(itemJson.getString("p_price2"))) {
                itemProduct.setP_price2(itemJson.getString("p_price2"));
            }
            if (itemJson.has("p_info") && !StringUtil.isNull(itemJson.getString("p_info"))) {
                itemProduct.setP_info(itemJson.getString("p_info"));
            }
            if (itemJson.has("p_info_detail") && !StringUtil.isNull(itemJson.getString("p_info_detail"))) {
                itemProduct.setP_info_detail(itemJson.getString("p_info_detail"));
            }
            if (itemJson.has("p_type") && !StringUtil.isNull(itemJson.getString("p_type"))) {
                itemProduct.setP_type(itemJson.getString("p_type"));
            }
            if (itemJson.has("p_type_t") && !StringUtil.isNull(itemJson.getString("p_type_t"))) {
                itemProduct.setP_type_t(itemJson.getString("p_type_t"));
            }
            if (itemJson.has("p_cuo") && !StringUtil.isNull(itemJson.getString("p_cuo"))) {
                itemProduct.setP_cuo(itemJson.getString("p_cuo"));
            }
            if (itemJson.has("p_time") && !StringUtil.isNull(itemJson.getString("p_time"))) {
                itemProduct.setP_time(itemJson.getString("p_time"));
            }
            if (itemJson.has("p_wimg") && !StringUtil.isNull(itemJson.getString("p_wimg"))) {
                itemProduct.setP_wimg(itemJson.getString("p_wimg"));
            }
            if (itemJson.has("p_ximg") && !StringUtil.isNull(itemJson.getString("p_ximg"))) {
                itemProduct.setP_ximg(itemJson.getString("p_ximg"));
            }
            if (itemJson.has("is_show") && !StringUtil.isNull(itemJson.getString("is_show"))) {
                itemProduct.setIs_show(itemJson.getString("is_show"));
            }
            itemList.add(itemProduct);
        }
        return itemList;
    }

    //解析优惠券
    public static List<MyCoupons> getCouponsData(String json) {
        List<MyCoupons> couponsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("counons") && !StringUtil.isNull(jsonObject.getString("counons"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("counons");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MyCoupons myCoupons = new MyCoupons();
                        JSONObject job = (JSONObject) jsonArray.get(i);
                        if (job.has("id") && !StringUtil.isNull(job.getString("id"))) {
                            myCoupons.setCoupons_id(job.getString("id"));
                        }
                        if (job.has("coupons_price") && !StringUtil.isNull(job.getString("coupons_price"))) {
                            myCoupons.setCoupons_price(job.getString("coupons_price"));
                        }
                        if (job.has("coupons_name") && !StringUtil.isNull(job.getString("coupons_name"))) {
                            myCoupons.setCoupons_name(job.getString("coupons_name"));
                        }
                        if (job.has("coupons_remark") && !StringUtil.isNull(job.getString("coupons_remark"))) {
                            myCoupons.setCoupons_remark(job.getString("coupons_remark"));
                        }
                        if (job.has("expired_time") && !StringUtil.isNull(job.getString("expired_time"))) {
                            myCoupons.setExpired_time(job.getString("expired_time"));
                        }
                        couponsList.add(myCoupons);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return couponsList;
    }
}
