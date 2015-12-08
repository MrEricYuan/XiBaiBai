package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.BeautyDIYItemAdapter;
import com.jph.xibaibai.adapter.BeautyDIYMealsAdapter;
import com.jph.xibaibai.adapter.BeautyWaxAdapter;
import com.jph.xibaibai.adapter.NotwashAdapter;
import com.jph.xibaibai.model.entity.BeautyItemProduct;
import com.jph.xibaibai.model.entity.BeautyService;
import com.jph.xibaibai.model.entity.DIYMeals;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.Constants;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.BeautyServiceParse;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/11/8.
 * 美容服务选项
 */
public class BeautyServiceActivity extends TitleActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static String beautyTotalPrice = "beautyTotalPrice";

    private IAPIRequests mAPIRequests;
    // 车型的判别
    private int carType = 0;
    // 解析数据的封装类
    private BeautyService beautyService = null;
    // 打蜡的种类的封装
    private List<Product> waxList;
    // 非必须洗车服务的封装
    private List<Product> notWashList;

    private BeautyWaxAdapter waxAdapter = null;

    private NotwashAdapter notwashAdapter = null;

    private boolean[] wCheckState = null;

    private boolean[] nCheckState = null;

    private double totalPrice = 0.0;

    private List<Product> beautyProductList = null; // 缓存选择的美容服务

    @ViewInject(R.id.beauty_wax_lv)
    ListView beauty_wax_lv; // 打蜡套餐选择
    @ViewInject(R.id.beauty_notwash_lv)
    ListView beauty_notwash_lv; // 不需要清洗的服务
    @ViewInject(R.id.common_total_price)
    TextView total_price;
    @ViewInject(R.id.common_submit_tv)
    TextView submit_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_service);
    }

    @Override
    public void initData() {
        mAPIRequests = new APIRequests(this);
        mAPIRequests.getBeautyService();
        carType = getIntent().getIntExtra("carType", 1);
        Log.i("Tag", "Beauty carType=>" + carType);
//        beauty_service_total_money.setText("￥" + getIntent().getDoubleExtra(beautyTotalPrice, 0.0));
        beauty_wax_lv.setFocusable(false);
        beauty_wax_lv.setOnItemClickListener(this);
        beauty_notwash_lv.setFocusable(false);
        beauty_notwash_lv.setOnItemClickListener(this);
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.beauty_title));
        submit_tv.setText(getString(R.string.DIYSub_submit));
    }

    /**
     * 再次进入此界面的初始化数据
     */
    private void initAginIntoData() {
        //打蜡
        waxList = beautyService.getWaxList();
        if (waxList != null) {
            waxAdapter = new BeautyWaxAdapter(waxList, carType);
            if (Constants.beautyCheckList != null && Constants.beautyCheckList.size() > 0) {
                wCheckState = Constants.beautyCheckList.get(0);
                waxAdapter.setCheckState(wCheckState);
            }
            beauty_wax_lv.setAdapter(waxAdapter);
            SystermUtils.setListViewHeight(beauty_wax_lv);
        }
        //非清洗服务
        notWashList = beautyService.getNotWashList();
        if (beautyService.getNotWashList() != null) {
            notwashAdapter = new NotwashAdapter(notWashList, carType);
            if (Constants.beautyCheckList != null && Constants.beautyCheckList.size() > 1) {
                nCheckState = Constants.beautyCheckList.get(1);
                notwashAdapter.setCheckState(nCheckState);
            }
            beauty_notwash_lv.setAdapter(notwashAdapter);
            SystermUtils.setListViewHeight(beauty_notwash_lv);
        }
        updateTotalPrice();
    }


    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.GETBEAUTY_SERVICE:
                if (!StringUtil.isNull(responseJson.getResult().toString())) {
                    beautyService = BeautyServiceParse.getResult(responseJson.getResult().toString());
                    if (beautyService == null) {
                        return;
                    }
                    initAginIntoData();
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        wCheckState = waxAdapter.getCheckState();  // 打蜡
        nCheckState = notwashAdapter.getCheckState(); // 非清洗的服务
        int listId = parent.getId();
        switch (listId) {
            case R.id.beauty_wax_lv: // 打蜡
                for (int i = 0; i < wCheckState.length; i++) {
                    if (i == position) {
                        if (wCheckState[i]) {
                            wCheckState[i] = false;
                        } else {
                            wCheckState[i] = true;
                        }
                    } else {
                        wCheckState[i] = false;
                    }
                }
                waxAdapter.setCheckState(wCheckState);
                waxAdapter.notifyDataSetChanged();
                break;
            case R.id.beauty_notwash_lv:
                // 非清洗服务
                if (nCheckState[position]) {
                    nCheckState[position] = false;
                } else {
                    nCheckState[position] = true;
                }
                notwashAdapter.setCheckState(nCheckState);
                notwashAdapter.notifyDataSetChanged();
                break;
        }
        updateTotalPrice();
    }

    /**
     * 更新总价格
     */
    private void updateTotalPrice() {
        totalPrice = 0.0;
        if (beautyProductList != null && beautyProductList.size() > 0) {
            beautyProductList.removeAll(beautyProductList);
        } else {
            beautyProductList = new ArrayList<>();
        }
        if (waxList == null || wCheckState == null) {
            return;
        }
        if (notWashList == null || wCheckState == null) {
            return;
        }
        /**计算打蜡的价格*/
        commonMethod(wCheckState, waxList);
        /**计算非必须洗项目的价格*/
        commonMethod(nCheckState, notWashList);
        total_price.setText(getString(R.string.DIYSub_totalPrice) + totalPrice);
    }

    private void commonMethod(boolean[] checkState, List<Product> productList) {
        for (int i = 0; i < checkState.length; i++) {
            if (checkState[i]) {
                beautyProductList.add(productList.get(i));
                if (carType == 1) {
                    totalPrice += productList.get(i).getP_price();
                } else if (carType == 2) {
                    totalPrice += productList.get(i).getP_price2();
                }
            }
        }
    }

    @OnClick({R.id.common_submit_tv})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_submit_tv:
                pottingData();
                Intent intent = new Intent();
                intent.putExtra("beautyProductList", (Serializable) beautyProductList);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    /**
     * 缓存选中状态
     */
    private void pottingData() {
        Constants.beautyCheckList.removeAll(Constants.beautyCheckList);
        if (waxAdapter != null) {
            wCheckState = waxAdapter.getCheckState();
            Constants.beautyCheckList.add(wCheckState);
        }
        if (notwashAdapter != null) {
            nCheckState = notwashAdapter.getCheckState();
            Constants.beautyCheckList.add(nCheckState);
        }
    }
}
