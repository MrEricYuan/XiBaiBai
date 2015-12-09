package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.ChoiceBeautyAdapter;
import com.jph.xibaibai.adapter.ChoiceDIYAdapter;
import com.jph.xibaibai.model.entity.BeautyItemProduct;
import com.jph.xibaibai.model.entity.Coupon;
import com.jph.xibaibai.model.entity.DIYSubBean;
import com.jph.xibaibai.model.entity.MyCoupons;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.BaseActivity;
import com.jph.xibaibai.utils.Constants;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.BeautyServiceParse;
import com.jph.xibaibai.utils.parsejson.WashCarPrice;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eric on 2015/12/1.
 * 新下单页面
 */
public class PlaceOrdersActivity extends BaseActivity implements View.OnClickListener {
    // HomeWeb传入的选择的产品
    public static String HOMEWEB_PRODUCT_LIST = "homeWeb_productList";
    // HomeWeb传入的选择的产品的标志（美容或者DIY）
    public static String HOMEWEB_PRODUCT_FLAG = "homeWeb_productFlag";
    // 获取HomeWeb传入的标志
    private int homeWebFlag = -1;
    // 访问网络
    private IAPIRequests apiRequests;
    //用户的id
    private int uid;
    // 洗车方式的数据结果
    private List<Product> washCarPriceList = null;
    // 优惠券列表数据
    private List<Coupon> couponsList = null;
    // 车类型
    private int carType = 0;
    // 清洗和洗车方式选中记录
    private boolean[] checkState = new boolean[4];
    // DIY项目请求码
    private final int diyIntentCode = 1001;
    // 美容项目请求码
    private final int beautyIntentCode = 1002;
    // diy选中的列表
    private List<Product> diyProductList = null;
    // 选中diy的适配器
    private ChoiceDIYAdapter choiceDIYAdapter = null;
    // 美容项目的选中的列表
    private List<Product> beautyProductList = null;
    // 美容项目的选中的适配器
    private ChoiceBeautyAdapter choiceBeautyAdapter = null;
    // DIY总价格
    private double diyTotalPrice = 0.0;
    // 美容总价格
    private double beautyTotalPrice = 0.0;
    // 外部清洗
    private double extralWashPrice = 0.0;
    // 内饰+清洗
    private double allCarWashPrice = 0.0;
    // 总价格
    private double totalPrice = 0.0;
    // 外部洗车洗车方式的id
    private int extralWashCarId = 0;
    // 内+外洗车方式的id
    private int allWashCarId = 0;

    @ViewInject(R.id.title_txt)
    private TextView title_txt;
    @ViewInject(R.id.place_extral_check)
    private ImageView extral_check; // 外部清洗
    @ViewInject(R.id.place_extral_price)
    private TextView extral_price;
    @ViewInject(R.id.place_allwash_check)
    private ImageView allwash_check; // 外部+内饰
    @ViewInject(R.id.place_allwash_price)
    private TextView allwash_price;
    @ViewInject(R.id.place_choicediy_lv)
    private ListView place_choicediy_lv; // 已经选中的DIY项目
    @ViewInject(R.id.place_choicebeauty_lv)
    private ListView place_choicebeauty_lv; // 已选中的美容项目
    @ViewInject(R.id.common_total_price)
    private TextView total_price; // 总价
    @ViewInject(R.id.place_yuyue_time)
    private TextView yuyue_time; // 预约时间
    @ViewInject(R.id.place_shangmen_rb)
    private ImageView shangmen_rb;
    @ViewInject(R.id.place_reserver_rb)
    private ImageView reserver_rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_orders);
    }

    @Override
    public void initData() {
        super.initData();
        initOtherDatas();
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        apiRequests = new APIRequests(this);
        apiRequests.getWashPrice();
    }

    /**
     * 初始化或者获取其他数据信息
     */
    private void initOtherDatas() {
        SystermUtils.diySubBean = new DIYSubBean();
        homeWebFlag = getIntent().getIntExtra(HOMEWEB_PRODUCT_FLAG, -1);
        if (homeWebFlag == 0) {
            beautyProductList = (List<Product>) getIntent().getSerializableExtra(HOMEWEB_PRODUCT_LIST);
            initBeautyAdapter();
        } else if (homeWebFlag == 1) {
            diyProductList = (List<Product>) getIntent().getSerializableExtra(HOMEWEB_PRODUCT_LIST);
            initDIYAdapter();
        }
        for (int i = 0; i < 4; i++) {
            checkState[i] = false;
        }
    }

    @Override
    public void initView() {
        super.initView();
        title_txt.setText(getString(R.string.place_order));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case diyIntentCode:
                diyProductList = (List<Product>) data.getSerializableExtra("DIYProductsList");
                initDIYAdapter();
                break;
            case beautyIntentCode:
                beautyProductList = (List<Product>) data.getSerializableExtra("beautyProductList");
                initBeautyAdapter();
                break;
        }
    }

    /**
     * 初始化DIY选中项目的适配器
     */
    private void initDIYAdapter() {
        if (diyProductList != null && diyProductList.size() > 0) {
            place_choicediy_lv.setVisibility(View.VISIBLE);
            choiceDIYAdapter = new ChoiceDIYAdapter(diyProductList);
            place_choicediy_lv.setAdapter(choiceDIYAdapter);
            caculateTotalPrice();
        } else {
            place_choicediy_lv.setAdapter(null);
            place_choicediy_lv.setVisibility(View.GONE);
        }
        SystermUtils.setListViewHeight(place_choicediy_lv);
    }

    /**
     * 初始化美容选中项目的适配器
     */
    private void initBeautyAdapter() {
        if(isFreeWashCar()){
            if(checkState[0]){
                checkState[0] = false;
                extral_check.setImageResource(R.mipmap.place_unchecked);
            }
        }
        if (beautyProductList != null && beautyProductList.size() > 0) {
            place_choicebeauty_lv.setVisibility(View.VISIBLE);
            choiceBeautyAdapter = new ChoiceBeautyAdapter(beautyProductList, carType);
            place_choicebeauty_lv.setAdapter(choiceBeautyAdapter);
            caculateTotalPrice();
        } else {
            place_choicebeauty_lv.setVisibility(View.GONE);
        }
        SystermUtils.setListViewHeight(place_choicebeauty_lv);
    }

    /**
     * 初始化洗车的价格
     */
    private void getWashCarPrice() {
        if (washCarPriceList != null && washCarPriceList.get(0) != null) {
            extralWashCarId = washCarPriceList.get(0).getId();
            if (carType == 0) {
                extralWashPrice = washCarPriceList.get(0).getP_price();
            } else if (carType == 1) {
                extralWashPrice = washCarPriceList.get(0).getP_price2();
            }
            extral_price.setText("￥" + extralWashPrice);
        }
        if (washCarPriceList != null && washCarPriceList.get(1) != null) {
            allWashCarId = washCarPriceList.get(1).getId();
            if (carType == 0) {
                allCarWashPrice = washCarPriceList.get(1).getP_price();
            } else if (carType == 1) {
                allCarWashPrice = washCarPriceList.get(1).getP_price2();
            }
            allwash_price.setText("￥" + allCarWashPrice);
        }

    }

    /**
     * 计算总价
     */
    private void caculateTotalPrice() {
        diyTotalPrice = 0.0;
        beautyTotalPrice = 0.0;
        if (diyProductList != null) {
            for (Product product : diyProductList) {
                diyTotalPrice = diyTotalPrice + product.getP_price();
            }
        }
        if (beautyProductList != null) {
            for (Product product : beautyProductList) {
                if (carType == 0) {
                    beautyTotalPrice = beautyTotalPrice + product.getP_price();
                } else if (carType == 1) {
                    beautyTotalPrice = beautyTotalPrice + product.getP_price2();
                }
            }
        }
        if (checkState[0]) {
            totalPrice = diyTotalPrice + beautyTotalPrice + extralWashPrice;
        } else if (checkState[1]) {
            totalPrice = diyTotalPrice + beautyTotalPrice + allCarWashPrice;
        } else {
            totalPrice = diyTotalPrice + beautyTotalPrice;
        }
        total_price.setText(getString(R.string.DIYSub_totalPrice) + totalPrice);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.GEWASHCAR_PRICE:// 清洗的数据
                if (!StringUtil.isNull(responseJson.getResult().toString())) {
                    washCarPriceList = WashCarPrice.getWashPrice(responseJson.getResult().toString());
                    getWashCarPrice();
                }
                break;
            case Tasks.GETCOUPONS://得到优惠券
                if (!StringUtil.isNull(responseJson.getResult().toString())) {
                    couponsList = JSON.parseArray(responseJson.getResult().toString(), Coupon.class);
                }
        }
    }

    @OnClick({R.id.title_img_left, R.id.place_extral_rl, R.id.place_allwash_rl, R.id.place_diyitem_rl, R.id.place_beauty_rl,
            R.id.place_lacation_rl,R.id.place_shangmen_rl,R.id.place_yuyue_rl,R.id.common_submit_tv})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_img_left:
                finish();
                break;
            case R.id.place_extral_rl: // 外部清洗
                if(isFreeWashCar()){
                    showToast(getString(R.string.place_freewash));
                    return;
                }
                if (checkState[0]) {
                    checkState[0] = false;
                    extral_check.setImageResource(R.mipmap.place_unchecked);
                } else {
                    checkState[0] = true;
                    extral_check.setImageResource(R.mipmap.place_checked);
                    if (checkState[1]) {
                        checkState[1] = false;
                        allwash_check.setImageResource(R.mipmap.place_unchecked);
                    }
                }
                caculateTotalPrice();
                break;
            case R.id.place_allwash_rl: // 外部+内饰
                if (checkState[1]) {
                    checkState[1] = false;
                    allwash_check.setImageResource(R.mipmap.place_unchecked);
                } else {
                    checkState[1] = true;
                    allwash_check.setImageResource(R.mipmap.place_checked);
                    if (checkState[0]) {
                        checkState[0] = false;
                        extral_check.setImageResource(R.mipmap.place_unchecked);
                    }
                }
                caculateTotalPrice();
                break;
            case R.id.place_diyitem_rl:// DIY项目选择
                intent.setClass(PlaceOrdersActivity.this, DIYSubActivity.class);
                intent.putExtra(HOMEWEB_PRODUCT_LIST, (Serializable) diyProductList);
                startActivityForResult(intent, diyIntentCode);
                break;
            case R.id.place_beauty_rl:// 美容项目选择
                intent.setClass(PlaceOrdersActivity.this, BeautyServiceActivity.class);
                intent.putExtra(HOMEWEB_PRODUCT_LIST, (Serializable) beautyProductList);
                startActivityForResult(intent, beautyIntentCode);
                break;
            case R.id.place_lacation_rl: // 获取车的位置信息

                break;
            case R.id.place_shangmen_rl: // 即刻上门
                if(checkState[2]){
                    checkState[2] = false;
                    shangmen_rb.setImageResource(R.mipmap.place_unchecked);
                }else {
                    checkState[2] = true;
                    shangmen_rb.setImageResource(R.mipmap.place_checked);
                    if(checkState[3]){
                        checkState[3] = false;
                        reserver_rb.setImageResource(R.mipmap.place_unchecked);
                    }
                }
                 break;
            case R.id.place_yuyue_rl: // 预约
                if(checkState[3]){
                    checkState[3] = false;
                    reserver_rb.setImageResource(R.mipmap.place_unchecked);
                }else {
                    checkState[3] = true;
                    reserver_rb.setImageResource(R.mipmap.place_checked);
                    if(checkState[2]){
                        checkState[2] = false;
                        shangmen_rb.setImageResource(R.mipmap.place_unchecked);
                    }
                }
                break;
            case R.id.common_submit_tv: // 提交订单

                break;
        }
    }

    /**
     * 判断是否免费外观清洗
     * @return
     */
    private boolean isFreeWashCar(){
        if(beautyProductList != null){
            for(Product product:beautyProductList){
                if(product.getP_freewash() == 1){
                    return true;
                }
            }
        }
        return false;
    }
}
