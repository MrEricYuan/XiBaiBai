package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jph.xibaibai.DIYMoreTypeAdapter;
import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.DIYOneTypeAdapter;
import com.jph.xibaibai.model.entity.DIYSubBean;
import com.jph.xibaibai.model.entity.MoreTypeDIY;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.BaseActivity;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.Constants;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.DIYSubParse;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/12/3.
 * DIY子项目选择
 */
public class DIYSubActivity extends TitleActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    // H5详情的标志
    public static String HOMEWEB_FLAG = "HOMEWEB_FLAG";
    // 从H5详情进入此界面的标志数字
    private int homeWebFlag = -1;
    // 访问网络
    private IAPIRequests apiRequests;
    // 封装DIY单个产品和多类型产品
    private DIYSubBean diyBean = null;
    // 多类型的Adapter
    private DIYMoreTypeAdapter moreTypeAdapter = null;
    // 单类型的Adapter
    private DIYOneTypeAdapter oneTypeAdapter = null;
    // 多类型的数据源
    private List<MoreTypeDIY> moreTypeDIYList = null;
    // 单类型的数据源
    private List<Product> oneTypeDIYList = null;
    // 单类型DIY选中的状态记录
    private boolean[] oneTypeCheckSta;
    // 选择后的总价
    private double totalPrice = 0.0;
    // 多类型的总价
    private double moreTypePrice = 0.0;
    // 单类型的总价
    private double oneTypePrice = 0.0;
    // 选中DIY的List封装
    private List<Product> diyProductList = null;

    @ViewInject(R.id.title_txt)
    private TextView title_txt;
    @ViewInject(R.id.diy_moretype_lv)
    private ListView diy_moretype_lv;
    @ViewInject(R.id.diy_onetype_lv)
    private ListView diy_onetype_lv;
    @ViewInject(R.id.common_total_price)
    private TextView common_total_price;
    @ViewInject(R.id.common_submit_tv)
    private TextView common_submit_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_subpj);
//        diyProductList = new ArrayList<>();
    }

    @Override
    public void initData() {
        super.initData();
        apiRequests = new APIRequests(this);
        homeWebFlag = getIntent().getIntExtra(HOMEWEB_FLAG,-1);
        diyProductList = (List<Product>) getIntent().getSerializableExtra(PlaceOrdersActivity.HOMEWEB_PRODUCT_LIST);
        apiRequests.getDIYDatas();
    }

    @Override
    public void initView() {
        super.initView();
        title_txt.setText("DIY");
        common_submit_tv.setText(getString(R.string.DIYSub_submit));
        diy_onetype_lv.setOnItemClickListener(this);
        common_submit_tv.setOnClickListener(this);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.DIYDATACODE:
                diyBean = DIYSubParse.subDataParse(responseJson.getResult().toString());
                initAdapterData();
                break;
        }
    }

    /**
     * 初始化Adapter数据
     */
    private void initAdapterData() {
        if (diyBean != null) {
            oneTypeDIYList = diyBean.getOneTypeList();
            moreTypeDIYList = diyBean.getMoreTypeList();
            if (oneTypeDIYList != null) {
                oneTypeAdapter = new DIYOneTypeAdapter(oneTypeDIYList);
                getOneTypeState();
                if(oneTypeCheckSta != null){
                    oneTypeAdapter.setOneTypeState(oneTypeCheckSta);
                }
                diy_onetype_lv.setAdapter(oneTypeAdapter);
                SystermUtils.setListViewHeight(diy_onetype_lv);
            }
            if (moreTypeDIYList != null) {
                getMoreTypeState();
                moreTypeAdapter = new DIYMoreTypeAdapter(DIYSubActivity.this, moreTypeDIYList);
                diy_moretype_lv.setAdapter(moreTypeAdapter);
                SystermUtils.setListViewHeight(diy_moretype_lv);
            }
        }
        calculateTotalPrice();
    }

    /**
     * 初始化只有一种类型的产品选中状态
     */
    private void getOneTypeState(){
        if(diyProductList != null && diyProductList.size() > 0){
            oneTypeCheckSta = new boolean[oneTypeDIYList.size()];
            Log.i("Tag","getOneTypeState=>diyProductList="+diyProductList.size());
        }else {
            Log.i("Tag","diyProductList is null");
            return;
        }
        for(int i = 0;i<oneTypeDIYList.size();i++){
            for(int k = 0;k<diyProductList.size();k++){
                if(diyProductList.get(k).getId() == oneTypeDIYList.get(i).getId()){
                    oneTypeCheckSta[i] = true;
                }
            }
        }
    }
    /**
     * 初始化多种类型的产品选中状态
     */
    private void getMoreTypeState(){
        if(diyProductList != null && diyProductList.size() > 0){
            Log.i("Tag","getOneTypeState=>diyProductList="+diyProductList.size());
        }else {
            Log.i("Tag","diyProductList is null");
            return;
        }
        for(int i = 0;i<moreTypeDIYList.size();i++){
            for(int k = 0;k<diyProductList.size();k++){
                MoreTypeDIY moreTypeDIY = moreTypeDIYList.get(i);
                if(moreTypeDIY.getHalfCarType().getId() == diyProductList.get(k).getId()){
                    moreTypeDIY.getHalfCarType().setIsChecked(true);
                }else if(moreTypeDIY.getAllCarType().getId() == diyProductList.get(k).getId()){
                    moreTypeDIY.getAllCarType().setIsChecked(true);
                }
            }
        }
    }

    /**
     * 多类型DIY状态更改
     */
    public void changeMoreTypeState(int position, int flag) {
        MoreTypeDIY mTBean = moreTypeDIYList.get(position);
        switch (flag) {
            case 0:
                if (mTBean.getHalfCarType().isChecked()) {
                    mTBean.getHalfCarType().setIsChecked(false);
                } else {
                    mTBean.getHalfCarType().setIsChecked(true);
                    mTBean.getAllCarType().setIsChecked(false);
                }
                break;
            case 1:
                if (mTBean.getAllCarType().isChecked()) {
                    mTBean.getAllCarType().setIsChecked(false);
                } else {
                    mTBean.getAllCarType().setIsChecked(true);
                    mTBean.getHalfCarType().setIsChecked(false);
                }
                break;
        }
        moreTypeAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    /**
     * 单类型DIY选择
     */
    private void changeOneTypeState(int position) {
        oneTypeCheckSta = oneTypeAdapter.getOneTypeState();
        if (oneTypeCheckSta[position]) {
            oneTypeCheckSta[position] = false;
        } else {
            oneTypeCheckSta[position] = true;
        }
        oneTypeAdapter.notifyDataSetChanged();
    }

    /**
     * 计算总价
     */
    private void calculateTotalPrice() {
        moreTypePrice = 0.0;
        oneTypePrice = 0.0;
        if (diyProductList == null) {
            diyProductList = new ArrayList<>();
        } else {
            diyProductList.removeAll(diyProductList);
        }
        for (int i = 0; i < moreTypeDIYList.size(); i++) {
            MoreTypeDIY moreTypeDIY = moreTypeDIYList.get(i);
            if (moreTypeDIY.getHalfCarType().isChecked()) {
                moreTypePrice = moreTypePrice + moreTypeDIY.getHalfCarType().getP_price();
                diyProductList.add(moreTypeDIY.getHalfCarType());
            }
            if (moreTypeDIY.getAllCarType().isChecked()) {
                moreTypePrice = moreTypePrice + moreTypeDIY.getAllCarType().getP_price();
                diyProductList.add(moreTypeDIY.getAllCarType());
            }
        }
        oneTypeCheckSta = oneTypeAdapter.getOneTypeState();
//        Log.i("Tag","oneTypeDIYList=>"+oneTypeDIYList.size());
        for (int i = 0; i < oneTypeDIYList.size(); i++) {
            if (oneTypeCheckSta[i]) {
                oneTypePrice = oneTypePrice + oneTypeDIYList.get(i).getP_price();
                diyProductList.add(oneTypeDIYList.get(i));
            }
        }
        totalPrice = moreTypePrice + oneTypePrice;
        common_total_price.setText(getString(R.string.DIYSub_totalPrice) + totalPrice);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int listId = parent.getId();
        switch (listId) {
            case R.id.diy_moretype_lv:

                break;
            case R.id.diy_onetype_lv:
                changeOneTypeState(position);
                calculateTotalPrice();
                break;
        }
    }

    @OnClick({R.id.common_submit_tv})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_submit_tv:
                Intent intent = new Intent();
                if(homeWebFlag == 100){
                    intent.setClass(DIYSubActivity.this, PlaceOrdersActivity.class);
                    intent.putExtra(PlaceOrdersActivity.HOMEWEB_PRODUCT_LIST, (Serializable) diyProductList);
                    intent.putExtra(PlaceOrdersActivity.HOMEWEB_PRODUCT_FLAG, 1);
                    startActivity(intent);
                }else {
                    intent.putExtra("DIYProductsList", (Serializable) diyProductList);
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
        }


    }
}
