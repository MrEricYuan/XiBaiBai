package com.jph.xibaibai.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.OrderProductAdapter;
import com.jph.xibaibai.adapter.OrderRecommandAdapter;
import com.jph.xibaibai.alipay.Alipay;
import com.jph.xibaibai.alipay.Product;
import com.jph.xibaibai.model.entity.ConfirmPay;
import com.jph.xibaibai.model.entity.MyOrderInformation;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.BaseAPIRequest;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.model.utils.Constants;
import com.jph.xibaibai.mview.CustomListView;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.MImageLoader;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.OrderParse;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/9 16:29
 * 描述：订单详情页面
 */
public class OrderInfoActivity extends TitleActivity implements View.OnClickListener {
    private String orderId;
    private int uid;
    private MyOrderInformation myOrderInformation;
    private IAPIRequests mApiRequest;
    private OrderProductAdapter orderProductAdapter;
    private OrderRecommandAdapter recommandAdapter;

    @ViewInject(R.id.order_info_scLayout)
    private ScrollView order_info_scLayout;
    @ViewInject(R.id.order_pay_layout)
    private LinearLayout order_pay_layout;
    @ViewInject(R.id.pay_btn)
    private Button pay_btn;
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
    @ViewInject(R.id.order_info_goComent)
    private TextView order_info_goComent;
    @ViewInject(R.id.order_info_employeeName)
    private TextView order_info_employeeName;
    @ViewInject(R.id.order_info_serviceTime)
    private TextView order_info_serviceTime;
    @ViewInject(R.id.order_info_couponAmount)
    private TextView order_info_couponAmount;
    @ViewInject(R.id.order_info_employeeTel)
    private ImageView order_info_employeeTel;
    @ViewInject(R.id.order_before_album)
    private ImageView order_before_album;
    @ViewInject(R.id.order_after_album)
    private ImageView order_after_album;
    @ViewInject(R.id.order_info_commentLabel)
    private TextView order_info_commentLabel;
    @ViewInject(R.id.order_info_commentContent)
    private TextView order_info_commentContent;

    @ViewInject(R.id.order_info_commentLayout)
    private LinearLayout order_info_commentLayout;
    @ViewInject(R.id.order_info_commentInfoLayout)
    private LinearLayout order_info_commentInfoLayout;
    @ViewInject(R.id.order_info_artificerLayout)
    private LinearLayout order_info_artificerLayout;
    @ViewInject(R.id.order_info_opLayout)
    private LinearLayout order_info_opLayout;
    @ViewInject(R.id.order_info_couponLayout)
    private LinearLayout order_info_couponLayout;
    @ViewInject(R.id.order_info_priceLayout)
    private LinearLayout order_info_priceLayout;
    @ViewInject(R.id.order_info_proLayout)
    private LinearLayout order_info_proLayout;
    @ViewInject(R.id.order_info_washLayout)
    private LinearLayout order_info_washLayout;
    @ViewInject(R.id.order_info_artificerRecommand)
    private CustomListView order_info_artificerRecommand;

    @ViewInject(R.id.coment_level)
    private RatingBar coment_level;

    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (com.jph.xibaibai.utils.Constants.IntentAction.COMMENT_SUCCEED.equals(action)) {
                if (mApiRequest == null)
                    mApiRequest = new APIRequests(OrderInfoActivity.this);
                mApiRequest.getOrderInformation(orderId);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
    }

    @Override
    public void initData() {
        super.initData();
        orderId = getIntent().getStringExtra("orderId");
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        mApiRequest = new APIRequests(this);
        mApiRequest.getOrderInformation(orderId);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(com.jph.xibaibai.utils.Constants.IntentAction.COMMENT_SUCCEED);
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.order_info_title));
    }

    @Override
    public void initListener() {
        super.initListener();
        pay_btn.setOnClickListener(this);
        order_info_goComent.setOnClickListener(this);
    }

    @Override
    public void onSuccess(int taskId, String flag, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.ORDER_INFO:
                myOrderInformation = OrderParse.getInstance().parseOrderInfo(responseJson.getResult().toString());
                setOrderData();
                break;
            case Tasks.CONFIRM_PAY:
                ConfirmPay confirmPay = OrderParse.getInstance().parseConfirmPay(responseJson.getResult().toString());
                if (confirmPay != null) {
                    if (!StringUtil.isNull(confirmPay.getExtra())) showToast(confirmPay.getExtra());
                    pay(confirmPay);
                }

                break;
        }
    }

    private void setOrderData() {
        if (myOrderInformation != null) {
            int currentState = myOrderInformation.getState();
            if (currentState == 0) {
                order_pay_layout.setVisibility(View.VISIBLE);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                layoutParams.bottomMargin = 0;
                order_info_scLayout.setLayoutParams(layoutParams);
                order_pay_layout.setVisibility(View.GONE);
                order_info_priceLayout.setVisibility(View.VISIBLE);
            }
            switch (currentState) {
                case 0:
                    order_info_commentLayout.setVisibility(View.GONE);
                    break;
                case 1:
                    order_info_commentLayout.setVisibility(View.GONE);
                    break;
                case 2:
                    order_info_opLayout.setVisibility(View.VISIBLE);
                    order_info_commentLayout.setVisibility(View.GONE);
                    break;
                case 3:
                    order_info_opLayout.setVisibility(View.VISIBLE);

                    order_info_commentLayout.setVisibility(View.GONE);
                    break;
                case 4:
                    order_info_opLayout.setVisibility(View.VISIBLE);
                    order_info_commentLayout.setVisibility(View.GONE);
                    break;
                case 5:
                    order_info_opLayout.setVisibility(View.VISIBLE);
                    order_info_goComent.setVisibility(View.VISIBLE);
                    order_info_commentLayout.setVisibility(View.VISIBLE);
                    order_info_commentInfoLayout.setVisibility(View.GONE);
                    break;
                case 6:
                    order_info_commentLayout.setVisibility(View.VISIBLE);
                    order_info_commentLabel.setText(getString(R.string.order_info_commented));
                    order_info_opLayout.setVisibility(View.VISIBLE);
                    order_info_goComent.setVisibility(View.GONE);
                    order_info_commentInfoLayout.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    order_info_commentLayout.setVisibility(View.GONE);
                    break;
            }
            if(myOrderInformation.getBeforeAlbum() != null && myOrderInformation.getAfterAlbum() != null){
                order_info_washLayout.setVisibility(View.VISIBLE);
            }
            if (!"0.0".equals(myOrderInformation.getCouponOffset())) {
                order_info_couponAmount.setText(getString(R.string.sign_yuan) +" "+ myOrderInformation.getCouponOffset());
                order_info_couponLayout.setVisibility(View.VISIBLE);
            }

            if (myOrderInformation.getServiceList() != null && !myOrderInformation.getServiceList().isEmpty()) {
                orderProductAdapter = new OrderProductAdapter(myOrderInformation.getServiceList(), this);
                order_pro_lv.setAdapter(orderProductAdapter);
                order_info_proLayout.setVisibility(View.VISIBLE);
            }
            if (myOrderInformation.getRecommandList() != null && !myOrderInformation.getRecommandList().isEmpty()) {
                recommandAdapter = new OrderRecommandAdapter(myOrderInformation.getRecommandList(), this);
                order_info_artificerRecommand.setAdapter(recommandAdapter);

            }
            if (StringUtil.isNull(myOrderInformation.getArtificerName()) || StringUtil.isNull(myOrderInformation.getArtificerTel()))
                order_info_artificerLayout.setVisibility(View.GONE);
            else {
                order_info_employeeName.setText(myOrderInformation.getArtificerName());
                order_info_employeeTel.setOnClickListener(this);
            }

            if (myOrderInformation.getBeforeAlbum() != null && !myOrderInformation.getBeforeAlbum().isEmpty()) {
                MImageLoader.getInstance(this).displayImage(Constants.BASE_URL + SystermUtils.replacePicpath(myOrderInformation.getBeforeAlbum().get(0)), order_before_album);
                order_before_album.setOnClickListener(this);
            }
            if (myOrderInformation.getAfterAlbum() != null && !myOrderInformation.getAfterAlbum().isEmpty()) {
                MImageLoader.getInstance(this).displayImage(Constants.BASE_URL + SystermUtils.replacePicpath(myOrderInformation.getAfterAlbum().get(0)), order_after_album);
                order_after_album.setOnClickListener(this);
            }
            order_info_payAmount.setText(getString(R.string.sign_yuan) +" "+ myOrderInformation.getPayPrice());
            order_info_total.setText(getString(R.string.sign_yuan) +" "+ myOrderInformation.getOrderPrice());
            order_info_serviceTime.setText(myOrderInformation.getServiceTime());
            order_info_carinfo.setText(myOrderInformation.getCarInfo());
            order_info_cartype.setText(myOrderInformation.getCarType());
            order_info_orderNo.setText(myOrderInformation.getOrderNo());
            order_info_location.setText(myOrderInformation.getCarLocation());
            order_info_carplateno.setText(myOrderInformation.getCarplateNo());
            order_info_driver.setText(myOrderInformation.getDriverName());
            order_info_time.setText(myOrderInformation.getOrderTime());
            order_info_paytype.setText(myOrderInformation.getPayType());
            coment_level.setProgress(myOrderInformation.getCommentLevel());
            order_info_commentContent.setText(myOrderInformation.getCommentContent());


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_info_employeeTel:
                if (StringUtil.isNumeric(myOrderInformation.getArtificerTel())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    //需要拨打的号码
                    intent.setData(Uri.parse("tel:" + myOrderInformation.getArtificerTel()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else
                    showToast(getString(R.string.order_tel_error));
                break;
            case R.id.pay_btn:
                if (mApiRequest == null)
                    mApiRequest = new APIRequests(this);
                mApiRequest.confirmPay(orderId, uid);
                break;
            case R.id.order_after_album:
                showAlbum(myOrderInformation.getAfterAlbum());
                break;
            case R.id.order_before_album:
                showAlbum(myOrderInformation.getBeforeAlbum());
                break;
            case R.id.order_info_goComent:
                Intent intent = new Intent(this, SendCommentActivity.class);
                intent.putExtra("orderId", orderId);
                startActivity(intent);
                break;
        }
    }

    private void pay(final ConfirmPay confirmPay) {
        if ("0.0".equals(confirmPay.getPayPrice())) {
            return;
        }
        Product product = new Product("洗车服务",
                "洗车服务", Double.parseDouble(confirmPay.getPayPrice()), myOrderInformation.getOrderNo());

        Alipay alipay = new Alipay(this, BaseAPIRequest.URL_API
                + "/alipay_return");
        alipay.setCallBack(new Alipay.CallBack() {
            @Override
            public void onSuccess() {
                myOrderInformation.setPayPrice(confirmPay.getPayPrice());
                myOrderInformation.setCouponOffset(confirmPay.getCouponPrice());
                Intent intentResult = new Intent(OrderInfoActivity.this, AfterPayActivity.class);
                intentResult.putExtra("my_order_info", myOrderInformation);
                intentResult.putExtra("confirm_pay", confirmPay);
                intentResult.putExtra("oderId", myOrderInformation.getOrderId());
                startActivity(intentResult);
                localBroadcastManager.sendBroadcast(new Intent(com.jph.xibaibai.utils.Constants.IntentAction.PAY_SUCCEED).putExtra("orderId",orderId));
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

    private void showAlbum(List<String> dataList) {
        if (dataList == null || dataList.isEmpty()) {
//            showToast(getString(R.string.tost_no_pic));
            return;
        }
        ArrayList<String> picList = new ArrayList<String>();
        picList.addAll(dataList);
        Intent intent = new Intent(this,
                BigImageActivity.class);

        intent.putStringArrayListExtra("img_pics", picList);
        intent.putExtra("pos", 0);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
    }
}
