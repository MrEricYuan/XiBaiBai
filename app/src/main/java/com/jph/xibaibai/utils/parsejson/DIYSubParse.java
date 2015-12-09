package com.jph.xibaibai.utils.parsejson;

import com.jph.xibaibai.model.entity.DIYSubBean;
import com.jph.xibaibai.model.entity.MoreTypeDIY;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/12/4.
 * DIY数据解析（半车，全车和单个产品）
 */
public class DIYSubParse {
    public static DIYSubBean subDataParse(String json) {
        DIYSubBean bean = null;
        try {
            bean = new DIYSubBean();
            JSONObject job = new JSONObject(json);
            if (job.has("common") && !StringUtil.isNull(job.getString("common"))) {
                List<Product> commonList = null;
                JSONArray jsonArray = job.getJSONArray("common");
                if (jsonArray != null && jsonArray.length() > 0) {
                    commonList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject pJson = (JSONObject) jsonArray.get(i);
                        commonList.add(commonMethod(pJson));
                    }
                }
                bean.setOneTypeList(commonList);
            }
            if(job.has("group") && !StringUtil.isNull(job.getString("group"))){
                List<MoreTypeDIY> groupList = null;
                JSONArray jsonArray = job.getJSONArray("group");
                if (jsonArray != null && jsonArray.length() > 0) {
                    groupList = new ArrayList<>();
                    for(int i = 0; i < jsonArray.length(); i++){
                        MoreTypeDIY mTypeBean = new MoreTypeDIY();
                        JSONObject mJob = (JSONObject) jsonArray.get(i);
                        if(mJob.has("half") && !StringUtil.isNull(mJob.getString("half"))){
                            JSONObject halfJson = mJob.getJSONObject("half");
                            mTypeBean.setHalfCarType(commonMethod(halfJson));
                        }
                        if(mJob.has("all") && !StringUtil.isNull(mJob.getString("all"))){
                            JSONObject allJson = mJob.getJSONObject("all");
                            mTypeBean.setAllCarType(commonMethod(allJson));
                        }
                        if(mJob.has("name") && !StringUtil.isNull(mJob.getString("name"))){
                            mTypeBean.setGroupName(mJob.getString("name"));
                        }
                        groupList.add(mTypeBean);
                    }
                }
                bean.setMoreTypeList(groupList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static Product commonMethod(JSONObject jsonObject) throws JSONException {
        Product product = new Product();
        if(jsonObject.has("id") && !StringUtil.isNull(jsonObject.getString("id"))){
            product.setId(Integer.parseInt(jsonObject.getString("id")));
        }
        if(jsonObject.has("p_name") && !StringUtil.isNull(jsonObject.getString("p_name"))){
            product.setP_name(jsonObject.getString("p_name"));
        }
        if(jsonObject.has("p_price") && !StringUtil.isNull(jsonObject.getString("p_price"))){
            product.setP_price(Double.parseDouble(jsonObject.getString("p_price")));
        }
        if(jsonObject.has("p_price2") && !StringUtil.isNull(jsonObject.getString("p_price2"))){
            product.setP_price2(Double.parseDouble(jsonObject.getString("p_price2")));
        }
        if(jsonObject.has("p_info") && !StringUtil.isNull(jsonObject.getString("p_info"))){
            product.setP_info(jsonObject.getString("p_info"));
        }
        if(jsonObject.has("detailurl") && !StringUtil.isNull(jsonObject.getString("detailurl"))){
            product.setLinkPath(jsonObject.getString("detailurl"));
        }
        if(jsonObject.has("thumb_url") && !StringUtil.isNull(jsonObject.getString("thumb_url"))){
            product.setP_picPath(jsonObject.getString("thumb_url"));
        }
        if(jsonObject.has("p_wash_free") && !StringUtil.isNull(jsonObject.getString("p_wash_free"))){
            product.setP_freewash(Integer.parseInt(jsonObject.getString("p_wash_free")));
        }
        if(jsonObject.has("sort") && !StringUtil.isNull(jsonObject.getString("sort"))){
            product.setP_sort(Integer.parseInt(jsonObject.getString("sort")));
        }
        return product;
    }
}
