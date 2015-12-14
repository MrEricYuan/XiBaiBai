package com.jph.xibaibai.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.ConfirmProductAdapter;
import com.jph.xibaibai.alipay.Alipay;
import com.jph.xibaibai.alipay.Product;
import com.jph.xibaibai.model.entity.ConfirmOrder;
import com.jph.xibaibai.model.entity.ConfirmOrderData;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.entity.UserInfo;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.BaseAPIRequest;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.ConfirmOrderParse;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by Eric on 2015/11/8.
 * 订单确认详情页面
 */
public class PlaceOrderDetailActivity extends TitleActivity implements View.OnClickListener {

    public static String ODERDATAS = "confirmOrder";

    public static String CARTYOEFLAG = "carType";

    public static String SERVICETIMEFLAG = "serviceTimeFlag";

    private IAPIRequests mAPIRequests = null;

    private ConfirmOrder confirmOrder = null; // 上个界面传来的数据

    private ConfirmOrderData confirmData = null; //提交订单后解析的数据

    private int carType = 0;

    private ConfirmProductAdapter confirmProductAdapter = null;

    private List<com.jph.xibaibai.model.entity.Product> cachProductList = null; // 缓存选择洗车服务

    private double totalPrice = 0.0;

    private int serviceTime = 0;

    @ViewInject(R.id.dorder_name_tv)
    TextView dorder_name_tv; //车主
    @ViewInject(R.id.dorder_phone_tv)
    TextView dorder_phone_tv; // 车主电话
    @ViewInject(R.id.dorder_carnum_tv)
    TextView dorder_carnum_tv; // 车牌号
    @ViewInject(R.id.dorder_service_time)
    TextView dorder_service_time; // 服务时间
    @ViewInject(R.id.dorder_carlocate_tv)
    TextView dorder_carlocate_tv; // 车位置
    @ViewInject(R.id.detail_product_lv)
    ListView detail_product_lv; // 产品列表
    @ViewInject(R.id.detail_coupons_tv)
    TextView detail_coupons_tv; // 优惠券
    @ViewInject(R.id.common_total_price)
    TextView detail_totalprice_tv; // 总价

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    public void initView() {
        setTitle("支付订单");
        if(serviceTime == 2){
            dorder_service_time.setText(getString(R.string.main_txt_now));
        }else if(serviceTime == 3){
            dorder_service_time.setText(getIntent().getStringExtra(SERVICETIMEFLAG));
        }
    }

    @Override
    public void initData() {
        super.initData();
        mAPIRequests = new APIRequests(this);
        confirmOrder = (ConfirmOrder) getIntent().getSerializableExtra(ODERDATAS);
        carType = getIntent().getIntExtra(CARTYOEFLAG, 1);
        serviceTime = getIntent().getIntExtra(SERVICETIMEFLAG,0);
        if (confirmOrder != null) {
            cachProductList = confirmOrder.getCachProductList();
            setConfirmDatas();
        }
        detail_coupons_tv.setText("暂无可用的优惠券");
    }

    /**
     * 设置数据
     */
    private void setConfirmDatas() {
        String jsonUserInfo = SPUserInfo.getsInstance(this).getSP(SPUserInfo.KEY_USERINFO);
        String addressRemark = "";
        if (!TextUtils.isEmpty(jsonUserInfo)) {
            UserInfo userInfo = JSON.parseObject(jsonUserInfo, UserInfo.class);
            dorder_name_tv.setText(userInfo.getUname());
            dorder_phone_tv.setText(userInfo.getIphone());
        }
        if(SystermUtils.defaultCar != null){
            confirmOrder.setCarsId(SystermUtils.defaultCar.getId() + "");
            dorder_carnum_tv.setText(SystermUtils.defaultCar.getC_plate_num() + "\t" + SystermUtils.defaultCar.getC_color() + "\t" + SystermUtils.defaultCar.getC_brand()); // 车牌号
        }
        if (cachProductList != null) {
            confirmProductAdapter = new ConfirmProductAdapter(cachProductList, carType);
            detail_product_lv.setAdapter(confirmProductAdapter);
            SystermUtils.setListViewHeight(detail_product_lv);
        }
        addressRemark = confirmOrder.getReMark();
        if(StringUtil.isNull(addressRemark)){
            addressRemark = "";
        }
        if (confirmOrder.getCarAddress().contains(getString(R.string.place_provence))) {
            String[] str = confirmOrder.getCarAddress().split(getString(R.string.place_provence));
            dorder_carlocate_tv.setText(str[1] + addressRemark);
        }else {
            dorder_carlocate_tv.setText(confirmOrder.getCarAddress() + addressRemark);
        }
        detail_totalprice_tv.setText("￥" + confirmOrder.getAllTotalPrice());
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.CONFIMORDER:
                Log.i("Tag", "确认订单=》" + responseJson.getResult().toString());
                if (!StringUtil.isNull(responseJson.getResult().toString())) {
                    confirmData = ConfirmOrderParse.parseResult(responseJson.getResult().toString());
                    if (confirmData != null) {
                        pay();
                    }
                }
                break;
        }
    }

    private void pay() {
        /*Product product = new Product(order.getOrder_name(),
                order.getOrder_name(), order.getTotal_price(), order.getOrder_num());*/
        if (StringUtil.isNull(confirmData.getOrderPrice())) {
            return;
        }
        if(confirmOrder == null){
            return;
        }
        Product product = new Product("洗车服务",
                "洗车服务", Double.parseDouble(confirmData.getOrderPrice()), confirmData.getOrderNum());

        Alipay alipay = new Alipay(this, BaseAPIRequest.URL_API
                + "/alipay_return");
        alipay.setCallBack(new Alipay.CallBack() {
            @Override
            public void onSuccess() {
                Intent intentResult = new Intent(PlaceOrderDetailActivity.this, OrderResultActivity.class);
                intentResult.putExtra(PayOrderActivity.EXTRA_ORDER, confirmOrder);
                startActivity(intentResult);
                sendLocalBroadCast();
                showToast("支付成功");
                finish();
            }

            @Override
            public void onFailed() {
                showToast("支付失败");
            }
        });
        alipay.pay(product);
    }

    @OnClick({R.id.common_submit_tv})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_submit_tv:
                showDialogConfirmOrder();
                break;
        }
    }

    /**
     * 弹窗确认订单
     */
    private void showDialogConfirmOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否确认提交该订单？");
        builder.setTitle("确认订单");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                mApiRequests.newOrder(order);
                mAPIRequests.confirmOrderInfo(confirmOrder);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 预约成功后关闭预约界面
     */
    private void sendLocalBroadCast(){
        LocalBroadcastManager localBroadcastManager = null;
        //通过LocalBroadcastManager的getInstance()方法得到它的一个实例
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("com.xbb.broadcast.LOCAL_FINISH_SUBSCRIBE");
        localBroadcastManager.sendBroadcast(intent);//调用sendBroadcast()方法发送广播
    }
}
