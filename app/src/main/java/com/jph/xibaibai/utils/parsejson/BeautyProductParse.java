package com.jph.xibaibai.utils.parsejson;

import com.jph.xibaibai.model.entity.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/12/5.
 * 改版美容项目的解析
 */
public class BeautyProductParse {
    public static List<Product> beautyDataParse(String json) {
        List<Product> beautyList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if(jsonArray != null && jsonArray.length() > 0){
                beautyList = new ArrayList<>();
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject job = (JSONObject) jsonArray.get(i);
                    Product product = DIYSubParse.commonMethod(job);
                    beautyList.add(product);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beautyList;
    }
}
