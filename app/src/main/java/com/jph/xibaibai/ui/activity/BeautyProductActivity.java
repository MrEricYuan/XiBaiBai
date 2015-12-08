package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.BeautyProductAdapter;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.BeautyProductParse;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/12/5.
 * 美容界面
 */
public class BeautyProductActivity extends TitleActivity implements View.OnClickListener{
    // 访问网络
    private IAPIRequests apiRequests;
    // 车的类型0代表轿车，1代表SUV和MPV
    private int carType = 0;
    // 美容项目适配器
    private BeautyProductAdapter beautyAdapter = null;
    // 美容项目数据源
    private List<Product> beautyList = null;
    // 美容项目分离后数据源
    private List<Product> newBeautyList = null;
    // 美容项目的选中状态
    private boolean[] beautyState = null;
    // 美容项目的选中的列表
    private List<Product> choiceBeautyList = null;
    private double totalPrice = 0.0;

    @ViewInject(R.id.title_txt)
    private TextView title_txt;
    @ViewInject(R.id.beauty_product_lv)
    private ListView beauty_product_lv;
    @ViewInject(R.id.common_total_price)
    private TextView common_total_price;
    @ViewInject(R.id.common_submit_tv)
    private TextView common_submit_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_product);
    }

    @Override
    public void initData() {
        super.initData();
        apiRequests = new APIRequests(this);
        apiRequests.getBeatyDatas();
    }

    @Override
    public void initView() {
        super.initView();
        title_txt.setText(getString(R.string.beauty_title));
        common_submit_tv.setText(getString(R.string.DIYSub_submit));
    }

    /**
     * 请求数据源后做一次分离
     */
    private void separateList() {
        newBeautyList = new ArrayList<>();
        List<Product> tempList1 = new ArrayList<>();
        List<Product> tempList2 = new ArrayList<>();
        for (Product product : beautyList) {
            if (product.getP_price() == product.getP_price2()) {
                tempList2.add(product);
            } else {
                tempList1.add(product);
            }
        }
        newBeautyList.addAll(tempList1);
        newBeautyList.addAll(tempList2);
    }

    /**
     * 初始换Adapter
     */
    private void initBeautyAdapter() {
        beautyAdapter = new BeautyProductAdapter(BeautyProductActivity.this, newBeautyList, carType);
        if(SystermUtils.beautyChoiceState != null && SystermUtils.beautyChoiceState.length > 0){
            beautyAdapter.setBeautyState(SystermUtils.beautyChoiceState);
        }
        beauty_product_lv.setAdapter(beautyAdapter);
        calculateTotalPrice();
    }

    /**
     * 重置美容项目的选中状态
     */
    public void changeBeautyState(int position) {
        beautyState = beautyAdapter.getBeautyState();
        if (beautyState[position]) {
            beautyState[position] = false;
        } else {
            beautyState[position] = true;
        }
        beautyAdapter.setBeautyState(beautyState);
        beautyAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    /**
     * 计算总价
     */
    private void calculateTotalPrice(){
        if(beautyAdapter != null){
            beautyState = beautyAdapter.getBeautyState();
        }else {
            return;
        }
        totalPrice = 0.0;
        for(int i = 0;i<newBeautyList.size();i++){
            if(beautyState[i]){
                if(carType == 0){
                    totalPrice = totalPrice + newBeautyList.get(i).getP_price();
                }else {
                    totalPrice = totalPrice + newBeautyList.get(i).getP_price2();
                }
            }
        }
        common_total_price.setText(getString(R.string.DIYSub_totalPrice) + totalPrice);
    }

    /**
     * 得到选中项目的列表
     */
    private void getChoiceList(){
        beautyState = beautyAdapter.getBeautyState();
        if(choiceBeautyList == null){
            choiceBeautyList = new ArrayList<>();
        }else {
            choiceBeautyList.removeAll(choiceBeautyList);
        }
        for(int i = 0;i<newBeautyList.size();i++){
            if(beautyState[i]){
                choiceBeautyList.add(newBeautyList.get(i));
            }
        }
        SystermUtils.beautyChoiceState = beautyState;
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.BEAUTYATACODE:
                beautyList = BeautyProductParse.beautyDataParse(responseJson.getResult().toString());
                if (beautyList != null) {
                    separateList();
                    initBeautyAdapter();
                }
                break;
        }
    }

    @OnClick({R.id.common_submit_tv})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_submit_tv:
                getChoiceList();
                Intent intent = new Intent();
                intent.putExtra("beautyProductList",(Serializable)choiceBeautyList);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
