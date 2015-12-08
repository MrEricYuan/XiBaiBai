package com.jph.xibaibai.utils.parsejson;

import com.jph.xibaibai.model.entity.HomeAdBean;
import com.jph.xibaibai.model.entity.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鹏 on 2015/12/3.
 */
public class HomeDataParse {
    public static List<HomeAdBean> adDataParse(String json){
        List<HomeAdBean> adList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if(jsonArray != null && jsonArray.length() > 0){
                adList = new ArrayList<>();
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject job = (JSONObject) jsonArray.get(i);
                    HomeAdBean homeAdBean = new HomeAdBean();
                    if(job.has("id")){
                        homeAdBean.setAdId(job.getString("id"));
                    }
                    if(job.has("title")){
                        homeAdBean.setAdName(job.getString("title"));
                    }
                    if(job.has("thumb")){
                        homeAdBean.setAdPath(job.getString("thumb"));
                    }
                    if(job.has("url")){
                        homeAdBean.setAdLinkPath(job.getString("url"));
                    }
                    adList.add(homeAdBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return adList;
    }

    /**
     * 首页DIY数据解析
     * @param json
     * @return
     */
    public static List<Product> diyHomeParse(String json) {
        List<Product> diyList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if(jsonArray != null && jsonArray.length() > 0){
                diyList = new ArrayList<>();
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject job = (JSONObject) jsonArray.get(i);
                    Product product = DIYSubParse.commonMethod(job);
                    diyList.add(product);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return diyList;
    }
}
