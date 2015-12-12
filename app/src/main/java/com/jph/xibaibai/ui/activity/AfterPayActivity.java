package com.jph.xibaibai.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.OrderProductAdapter;
import com.jph.xibaibai.model.entity.ConfirmPay;
import com.jph.xibaibai.model.entity.MyOrderInformation;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.mview.CustomListView;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.parsejson.OrderParse;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/11 16:47
 * 描述：$TODO
 */
public class AfterPayActivity extends TitleActivity {
    private MyOrderInformation myOrderInformation;
    private OrderProductAdapter orderProductAdapter;

    @ViewInject(R.id.order_pro_lv)
    private CustomListView order_pro_lv;
    @ViewInject(R.id.order_info_total)
    private TextView order_info_total;
    @ViewInject(R.id.order_info_orderNo)
    private TextView order_info_orderNo;
    @ViewInject(R.id.order_info_time)
    private TextView order_info_time;
    @ViewInject(R.id.order_info_paytype)
    private TextView order_info_paytype;
    @ViewInject(R.id.order_info_driver)
    private TextView order_info_driver;
    @ViewInject(R.id.order_info_carinfo)
    private TextView order_info_carinfo;
    @ViewInject(R.id.order_info_cartype)
    private TextView order_info_cartype;
    @ViewInject(R.id.order_info_carplateno)
    private TextView order_info_carplateno;
    @ViewInject(R.id.order_info_location)
    private TextView order_info_location;
    @ViewInject(R.id.order_info_payAmount)
    private TextView order_info_payAmount;
    @ViewInject(R.id.order_info_serviceTime)
    private TextView order_info_serviceTime;
    @ViewInject(R.id.order_info_couponAmount)
    private TextView order_info_couponAmount;

    @ViewInject(R.id.order_info_couponLayout)
    private LinearLayout order_info_couponLayout;

    private IAPIRequests mApiRequest;
    private String orderId;
    private ConfirmPay confirmPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_afterpay);
    }

    @Override
    public void initData() {
        super.initData();
        orderId = getIntent().getStringExtra("orderId");
        myOrderInformation = (MyOrderInformation) getIntent().getSerializableExtra("my_order_info");
        confirmPay = (ConfirmPay) getIntent().getSerializableExtra("confirm_pay");
        if (myOrderInformation != null)
            setOrderData();
        else {
            mApiRequest = new APIRequests(this);
            mApiRequest.getOrderInformation(orderId);
        }

    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.order_info_title));
    }

    private void setOrderData() {
        if (myOrderInformation != null && confirmPay != null) {
            myOrderInformation.setPayPrice(confirmPay.getPayPrice());
            myOrderInformation.setCouponOffset(confirmPay.getCouponPrice());
            if (myOrderInformation.getServiceList() != null && !myOrderInformation.getServiceList().isEmpty()) {
                orderProductAdapter = new OrderProductAdapter(myOrderInformation.getServiceList(), this);
                order_pro_lv.setAdapter(orderProductAdapter);

            }
            if (!"0.0".equals(myOrderInformation.getCouponOffset())) {
                order_info_couponAmount.setText(getString(R.string.sign_yuan) + myOrderInformation.getCouponOffset());
                order_info_couponLayout.setVisibility(View.VISIBLE);
            }

            order_info_total.setText(getString(R.string.sign_yuan) + myOrderInformation.getOrderPrice());
            order_info_payAmount.setText(getString(R.string.sign_yuan) + myOrderInformation.getPayPrice());

            order_info_serviceTime.setText(myOrderInformation.getServiceTime());
            order_info_carinfo.setText(myOrderInformation.getCarInfo());
            order_info_cartype.setText(myOrderInformation.getCarType());
            order_info_orderNo.setText(myOrderInformation.getOrderNo());
            order_info_location.setText(myOrderInformation.getCarLocation());
            order_info_carplateno.setText(myOrderInformation.getCarplateNo());
            order_info_driver.setText(myOrderInformation.getDriverName());
            order_info_time.setText(myOrderInformation.getOrderTime());
            order_info_paytype.setText(myOrderInformation.getPayType());


        }
    }

    @Override
    public void onSuccess(int taskId, String flag, Object... params) {
        super.onSuccess(taskId, flag, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.ORDER_INFO:
                myOrderInformation = OrderParse.getInstance().parseOrderInfo(responseJson.getResult().toString());
                setOrderData();
                break;
        }
    }
}
