package com.jph.xibaibai.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.ChoiceBeautyAdapter;
import com.jph.xibaibai.adapter.ChoiceDIYAdapter;
import com.jph.xibaibai.model.entity.Address;
import com.jph.xibaibai.model.entity.BeautyItemProduct;
import com.jph.xibaibai.model.entity.ConfirmOrder;
import com.jph.xibaibai.model.entity.Coupon;
import com.jph.xibaibai.model.entity.DIYSubBean;
import com.jph.xibaibai.model.entity.MyCoupons;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.BaseActivity;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.Constants;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.BeautyServiceParse;
import com.jph.xibaibai.utils.parsejson.WashCarPrice;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Eric on 2015/12/1.
 * 新下单页面
 */
public class PlaceOrdersActivity extends TitleActivity implements View.OnClickListener {
    // HomeWeb传入的选择的产品
    public static String HOMEWEB_PRODUCT_LIST = "homeWeb_productList";
    // HomeWeb传入的选择的产品的标志（美容或者DIY）
    public static String HOMEWEB_PRODUCT_FLAG = "homeWeb_productFlag";
    // 地图页面进入下单页面
    public static String MAPADDRESS = "mapAddressInfo";
    // 优惠券的标志
    public static String COUPONSFLAG = "couponsFlag";
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
    // 车辆位置
    private final int locateIntentCode = 1003;
    // 预约时间点
    public final int timeScopeCode = 1010;
    // 优惠券
    public final int couponsCode = 1020;
    // diy选中的列表
    private List<Product> diyProductList = null;
    // 选中diy的适配器
    private ChoiceDIYAdapter choiceDIYAdapter = null;
    // 美容项目的选中的列表
    private List<Product> beautyProductList = null;
    // 美容项目的选中的适配器
    private ChoiceBeautyAdapter choiceBeautyAdapter = null;
    // 所有产品的封装
    private List<Product> allProductList = null;
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
    // 选中产品的id的拼接
    private String productId = "";
    // 外部洗车洗车方式的id
    private int extralWashCarId = 0;
    // 内+外洗车方式的id
    private int allWashCarId = 0;
    // 车辆位置
    private Address address = null;

    private LocalBroadcastManager lBManager = null;

    private LocalReceiver localReceiver = null;
    // 预约的日期
    private long appointDay = 0;
    // 预约的时间段
    private int appointTimeId = 0;
    // 确认订单数据的封装
    private ConfirmOrder confirmOrder = null;
    // 从优惠券界面选中的优惠券
    private Coupon choiceCoupon = null;
    // 抵用券价格
    private double couponsPrice = 0.0;
    // 抵用券价格
    private double couponsPriceSave = 0.0;
    // 优惠券的id
    private int couponsId = -1;
    // 优惠券的位置
    private int position = -1;
    // 是否用户自己选择了优惠券
    private boolean isChoiceCoupons = false;

    @ViewInject(R.id.places_submit_tv)
    TextView common_submit_tv;
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
    @ViewInject(R.id.place_total_price)
    private TextView total_price; // 总价
    @ViewInject(R.id.place_yuyue_time)
    private TextView yuyue_time; // 预约时间
    @ViewInject(R.id.place_shangmen_rb)
    private ImageView shangmen_rb;
    @ViewInject(R.id.place_reserver_rb)
    private ImageView reserver_rb;
    @ViewInject(R.id.place_location_tv)
    TextView location_tv;
    @ViewInject(R.id.place_coupons_tv)
    TextView coupons_tv; // 优惠券显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_orders);
        getDataBroadcast();
    }

    @Override
    public void initData() {
        super.initData();
        initOtherDatas();
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        address = (Address) getIntent().getSerializableExtra(MAPADDRESS);
        apiRequests = new APIRequests(this);
        apiRequests.getWashPrice();
        apiRequests.getCoupons(uid);
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
        setAddressInfo();
        common_submit_tv.setOnClickListener(this);
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
            case locateIntentCode:
                address = (Address) data.getSerializableExtra("LocateAddress");
                if (address != null) {
                    if (address.getAddress().contains(getString(R.string.place_provence))) {
                        String[] str = address.getAddress().split(getString(R.string.place_provence));
                        Log.i("Tag", "str=>" + str.length);
                        location_tv.setText(str[1] + address.getAddress_info());
                    } else {
                        location_tv.setText(address.getAddress_info() + address.getAddress_info());
                    }
                }
                break;
            case timeScopeCode:
                appointDay = data.getLongExtra("selectedDay", 0);
                appointTimeId = data.getIntExtra("selectedTimeScopeId", 0);
                if (appointDay != 0) {
                    yuyue_time.setText(data.getStringExtra("selectedDate") + data.getStringExtra("selectedTimeScope"));
                }
                break;
            case couponsCode:
                Coupon coupon = (Coupon) data.getSerializableExtra(COUPONSFLAG);
                if(coupon != null){
                    couponsId = coupon.getId();
                    couponsPrice = Double.parseDouble(coupon.getCoupons_price());
                    if(totalPrice == 0.0){
                        coupons_tv.setText(getString(R.string.coupons_use) + couponsPrice);
                    }else {
                        caculateTotalPrice();
                    }
                }
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
        caculateTotalPrice();
    }

    /**
     * 初始化美容选中项目的适配器
     */
    private void initBeautyAdapter() {
        if (isFreeWashCar()) {
            if (checkState[0]) {
                checkState[0] = false;
                extral_check.setImageResource(R.mipmap.place_unchecked);
            }
        }
        if (beautyProductList != null && beautyProductList.size() > 0) {
            place_choicebeauty_lv.setVisibility(View.VISIBLE);
            choiceBeautyAdapter = new ChoiceBeautyAdapter(beautyProductList, carType);
            place_choicebeauty_lv.setAdapter(choiceBeautyAdapter);
        } else {
            place_choicebeauty_lv.setVisibility(View.GONE);
        }
        caculateTotalPrice();
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
        if(totalPrice != 0.0){
            if(couponsPrice >= totalPrice){
                coupons_tv.setText(getString(R.string.coupons_use)+totalPrice);
                couponsPriceSave = totalPrice;
                totalPrice = 0.0;
            }else {
                coupons_tv.setText(getString(R.string.coupons_use) + couponsPrice);
                totalPrice = totalPrice - couponsPrice;
                couponsPriceSave = couponsPrice;
            }
        }else {
            if(couponsList != null && couponsList.size() > 0){
                coupons_tv.setText(couponsList.size() + getString(R.string.coupons_size));
            }else {
                coupons_tv.setText(getString(R.string.no_coupons));
            }
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
                    getCouponsMoney();
                    if(couponsId != -1){
                        couponsPrice = Double.parseDouble(couponsList.get(position).getCoupons_price());
                    }
                    Log.i("Tag", "couponsPrice=>" + couponsPrice + "/couponsId=" + couponsId);
                }
        }
    }

    private void packageData() {
        productId = "";
        if (address == null) {
            showToast("请先设置车辆位置");
            return;
        }
        if (allProductList == null) {
            allProductList = new ArrayList<>();
        } else {
            allProductList.removeAll(allProductList);
        }
        if (checkState[0] && washCarPriceList != null) {
            allProductList.add(washCarPriceList.get(0));
            productId = productId + extralWashCarId + ",";
        }
        if (checkState[1] && washCarPriceList != null) {
            allProductList.add(washCarPriceList.get(1));
            productId = productId + allWashCarId + ",";
        }
        if (diyProductList != null) {
            allProductList.addAll(diyProductList);
            for (Product product : diyProductList) {
                productId = productId + product.getId() + ",";
            }
        }
        if (beautyProductList != null) {
            allProductList.addAll(beautyProductList);
            for (Product product : beautyProductList) {
                productId = productId + product.getId() + ",";
            }
        }
        if (StringUtil.isNull(productId)) {
            showToast("请选择服务项目");
            return;
        }
        if (checkState[3] && appointDay == 0) {
            showToast("请设置预约时间点");
            return;
        }
        if (!checkState[2] && !checkState[3]) {
            showToast("请设置服务时间");
            return;
        }
        if (confirmOrder == null) {
            confirmOrder = new ConfirmOrder();
        }
        confirmOrder.setUserId(uid + "");
        confirmOrder.setCarAddress(address.getAddress());
        confirmOrder.setReMark(address.getAddress_info());
        confirmOrder.setCarLocateLg(address.getAddress_lg());  // 经度
        confirmOrder.setCarLocateLt(address.getAddress_lt()); //纬度
        confirmOrder.setCachProductList(allProductList);
        Log.i("Tag", "产品总价:" + totalPrice);
        confirmOrder.setAllTotalPrice(totalPrice);
        confirmOrder.setAppointDay(appointDay);
        confirmOrder.setAppointTimeId(appointTimeId);
        if (!StringUtil.isNull(productId)) {
            productId = productId.substring(0, productId.length() - 1);
        }
        confirmOrder.setProductId(productId);
        confirmOrder.setCouponsPrice(couponsPriceSave);
        Intent intent = new Intent();
        intent.setClass(PlaceOrdersActivity.this, PlaceOrderDetailActivity.class);
        intent.putExtra(PlaceOrderDetailActivity.ODERDATAS, confirmOrder);
        intent.putExtra(PlaceOrderDetailActivity.CARTYOEFLAG, carType);
        if(checkState[2]){
            intent.putExtra(PlaceOrderDetailActivity.SERVICETIMEFLAG, 2);
        }else if(checkState[3]){
            intent.putExtra(PlaceOrderDetailActivity.SERVICETIMEFLAG, 3);
            intent.putExtra(PlaceOrderDetailActivity.SERVICETIMESTR, yuyue_time.getText().toString());
            Log.i("Tag","yuyue=>"+yuyue_time.getText().toString());
        }
        startActivity(intent);
    }

    @OnClick({R.id.title_img_left, R.id.place_extral_rl, R.id.place_allwash_rl, R.id.place_diyitem_rl, R.id.place_beauty_rl,
            R.id.place_lacation_rl, R.id.place_shangmen_rl, R.id.place_yuyue_rl,R.id.place_coupons_rl})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_img_left:
                finish();
                break;
            case R.id.place_extral_rl: // 外部清洗
                if (isFreeWashCar()) {
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
                intent.setClass(PlaceOrdersActivity.this, AddressSelectActivity.class);
                startActivityForResult(intent, locateIntentCode);
                break;
            case R.id.place_shangmen_rl: // 即刻上门
                if (checkState[2]) {
                    checkState[2] = false;
                    shangmen_rb.setImageResource(R.mipmap.place_unchecked);
                } else {
                    checkState[2] = true;
                    shangmen_rb.setImageResource(R.mipmap.place_checked);
                    if (checkState[3]) {
                        appointDay = 0;
                        appointTimeId = 0;
                        checkState[3] = false;
                        reserver_rb.setImageResource(R.mipmap.place_unchecked);
                    }
                }
                break;
            case R.id.place_yuyue_rl: // 预约
                if (checkState[3]) {
                    appointDay = 0;
                    appointTimeId = 0;
                    checkState[3] = false;
                    reserver_rb.setImageResource(R.mipmap.place_unchecked);
                    yuyue_time.setText("");
                } else {
                    checkState[3] = true;
                    reserver_rb.setImageResource(R.mipmap.place_checked);
                    if (checkState[2]) {
                        checkState[2] = false;
                        shangmen_rb.setImageResource(R.mipmap.place_unchecked);
                    }
                    intent.setClass(PlaceOrdersActivity.this, ApointmentTimeActivity.class);
                    startActivityForResult(intent, timeScopeCode);
                }
                break;
            case R.id.places_submit_tv: // 提交订单
                packageData();
                break;
            case R.id.place_coupons_rl:// 优惠券
                intent.setClass(PlaceOrdersActivity.this, SelectTicketActivity.class);
                intent.putExtra("isPlaceOrder",true);
                startActivityForResult(intent, couponsCode);
                break;
        }
    }

    /**
     * 判断是否免费外观清洗
     *
     * @return
     */
    private boolean isFreeWashCar() {
        if (beautyProductList != null) {
            for (Product product : beautyProductList) {
                if (product.getP_freewash() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getDataBroadcast() {
        localReceiver = new LocalReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xbb.broadcast.UPDATE_ADDRESS");
        //通过LocalBroadcastManager的getInstance()方法得到它的一个实例
        lBManager = LocalBroadcastManager.getInstance(this);
        lBManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.xbb.broadcast.UPDATE_ADDRESS")) {
                if (intent == null) {
                    return;
                }
                address = (Address) intent.getSerializableExtra("LocateAddress");
                setAddressInfo();
            }
        }
    }

    /**
     * 设置车辆位置信息
     */
    private void setAddressInfo() {
        String location = "";
        String carLocation = "";
        if (address != null) {
            location = address.getAddress();
            if (StringUtil.isNull(location)) {
                return;
            }
            if (address.getAddress().contains(getString(R.string.place_provence))) {
                String[] str = address.getAddress().split(getString(R.string.place_provence));
                carLocation = address.getAddress_info();
                if (!StringUtil.isNull(carLocation)) {
                    location_tv.setText(str[1] + address.getAddress_info());
                } else {
                    location_tv.setText(str[1]);
                }
            } else {
                if (!StringUtil.isNull(carLocation)) {
                    location_tv.setText(address.getAddress_info() + address.getAddress_info());
                } else {
                    location_tv.setText(address.getAddress_info());
                }
            }
        }
    }

    /**
     * 得到优惠的价格
     */
    private void getCouponsMoney() {
        if (couponsList == null) {
            return;
        }
        if (couponsList.size() == 0) {
            coupons_tv.setText(getString(R.string.no_coupons));
            return;
        }
        coupons_tv.setText(couponsList.size() + getString(R.string.coupons_size));
        long litleTime = Long.parseLong(couponsList.get(0).getExpired_time());
        couponsId = couponsList.get(0).getId();
        position = 0;
        for (int i = 0; i < couponsList.size(); i++) {
            Coupon coupon = couponsList.get(i);
            // 得到现金抵用券
            if (coupon.getState() == 0) {
                if (litleTime > Long.parseLong(coupon.getExpired_time())) {
                    litleTime = Long.parseLong(coupon.getExpired_time());
                    position = i;
                    couponsId = coupon.getId();
                }
            }
        }
    }
}
