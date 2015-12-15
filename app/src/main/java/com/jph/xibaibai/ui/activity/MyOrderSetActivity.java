package com.jph.xibaibai.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.MyOrderAdapter;
import com.jph.xibaibai.alipay.Alipay;
import com.jph.xibaibai.alipay.Product;
import com.jph.xibaibai.model.entity.ConfirmPay;
import com.jph.xibaibai.model.entity.MyOrder;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.BaseAPIRequest;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.Constants;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.parsejson.OrderParse;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.TreeMap;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/8 15:26
 * 描述：$TODO
 */
public class MyOrderSetActivity extends TitleActivity implements PullToRefreshBase.OnRefreshListener2<ListView> {
    @ViewInject(R.id.myorder_list_lv)
    private PullToRefreshListView myorder_list_lv;

    private IAPIRequests apiRequests;
    private MyOrderAdapter myOrderAdapter;

    private List<MyOrder> myOrderDataList;
    private List<MyOrder> myOrderShowList;

    private boolean isAdd;
    private int uid;
    private int pageindex;
    private int pagesize;

    private boolean showLoading;

    private TreeMap<String, MyOrder> opMap;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyOrder opOrder = (MyOrder) msg.obj;

            if (opOrder != null) {
                String orderId = opOrder.getOrderId();
                Intent intent = null;
                switch (msg.what) {
                    case 100://删除

                        break;
                    case 101://取消
                        if (apiRequests == null)
                            apiRequests = new APIRequests(MyOrderSetActivity.this);
                        apiRequests.cancelOrder(orderId, uid);
                        break;
                    case 102://支付
                        if (apiRequests == null)
                            apiRequests = new APIRequests(MyOrderSetActivity.this);
                        apiRequests.confirmPay(orderId, uid);
                        break;
                    case 103://评价
                        intent = new Intent(MyOrderSetActivity.this, SendCommentActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        break;
                }
                opMap.put(orderId, opOrder);
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.IntentAction.COMMENT_SUCCEED.equals(action)) {
                String orderId = intent.getStringExtra("orderId");
                if (opMap != null && opMap.containsKey(orderId)) {
                    MyOrder opOrder = opMap.get(orderId);
                    opOrder.setState(getString(R.string.after_comment));
                    opOrder.setCurrentState(6);
                    myOrderAdapter.notifyDataSetChanged();
                } else {
                    for (MyOrder mo : myOrderShowList) {
                        if (mo.getOrderId().equals(orderId)) {
                            mo.setState(getString(R.string.after_comment));
                            mo.setCurrentState(6);
                            myOrderAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
            if (Constants.IntentAction.PAY_SUCCEED.equals(action)) {
                String orderId = intent.getStringExtra("orderId");
                if (opMap != null && opMap.containsKey(orderId)) {
                    MyOrder opOrder = opMap.get(orderId);
                    opOrder.setCurrentState(1);
                    opOrder.setState(getString(R.string.myorder_deliverying));
                    myOrderAdapter.notifyDataSetChanged();
                } else {
                    for (MyOrder mo : myOrderShowList) {
                        if (mo.getOrderId().equals(orderId)) {
                            mo.setCurrentState(1);
                            mo.setState(getString(R.string.myorder_deliverying));
                            myOrderAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
    }

    @Override
    public void initData() {
        super.initData();
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        pagesize = 10;
        pageindex = 1;
        showLoading = true;
        getOrderList();
        opMap = new TreeMap<String, MyOrder>();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.IntentAction.COMMENT_SUCCEED);
        intentFilter.addAction(Constants.IntentAction.PAY_SUCCEED);
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.myorder_title));
        myorder_list_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    @Override
    public void initListener() {
        super.initListener();
        myorder_list_lv.setOnRefreshListener(this);
    }

    @Override
    public void onSuccess(int taskId, String flag, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.ORDER_LIST:
                myOrderDataList = OrderParse.getInstance().parseMyOrderList(responseJson.getResult().toString());
                if (myOrderDataList != null && !myOrderDataList.isEmpty()) {
                    if (isAdd) {
                        myOrderShowList.addAll(myOrderDataList);
                        myOrderAdapter.notifyDataSetChanged();
                        isAdd = false;
                    } else {
                        myOrderShowList = myOrderDataList;
                        myOrderAdapter = new MyOrderAdapter(myOrderShowList, MyOrderSetActivity.this, mHandler);

                        myorder_list_lv.setAdapter(myOrderAdapter);
                    }
                } else {
                    if (isAdd) {
                        pageindex--;
                        showToast(getString(R.string.no_more_data));
                        isAdd = false;
                    }
                }
                break;
            case Tasks.CANCEL_ORDER:
                if (opMap != null && opMap.containsKey(flag)) {
                    MyOrder currentOpOrder = opMap.get(flag);
                    currentOpOrder.setCurrentState(7);
                    currentOpOrder.setState(getString(R.string.myorder_canceled));
                    myOrderAdapter.notifyDataSetChanged();

                }
                break;
            case Tasks.DELETE_ORDER:
                if (opMap != null && opMap.containsKey(flag)) {
                    MyOrder currentOpOrder = opMap.get(flag);
                    if (myOrderShowList.contains(currentOpOrder))
                        myOrderShowList.remove(currentOpOrder);
                    opMap.remove(flag);
                    myOrderAdapter.notifyDataSetChanged();

                }
                break;
            case Tasks.CONFIRM_PAY:
                ConfirmPay confirmPay = OrderParse.getInstance().parseConfirmPay(responseJson.getResult().toString());
                if (confirmPay != null) {
                    if (!StringUtil.isNull(confirmPay.getExtra())) showToast(confirmPay.getExtra());
                    if (opMap != null && opMap.containsKey(flag)) {
                        MyOrder currentOpOrder = opMap.get(flag);
                        currentOpOrder.setCurrentState(1);
                        currentOpOrder.setState(getString(R.string.myorder_deliverying));
                        pay(confirmPay, currentOpOrder);
                    }


                }
                break;
        }
    }

    @Override
    public void onPrepare(int taskId) {
        if (showLoading)
            showProgressDialog();
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        myorder_list_lv.onRefreshComplete();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        showLoading = false;
        isAdd = true;
        pageindex++;
        getOrderList();
    }


    /**
     * 向服务器请求订单列表数据
     */
    private void getOrderList() {
        if (apiRequests == null)
            apiRequests = new APIRequests(this);
        apiRequests.getMyOrderList(uid, pageindex, pagesize);
    }

    /**
     * 删除订单
     */
    private void deleteOrder(MyOrder opOrder) {

    }

    /**
     * 取消订单
     */
    private void cancelOrder(MyOrder opOrder) {

    }

    /**
     * 评价订单
     */
    private void commentOrder(MyOrder opOrder) {

    }

    /**
     * 支付订单
     */
    private void payOrder(MyOrder opOrder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
        opMap.clear();

    }

    private void pay(final ConfirmPay confirmPay, final MyOrder myOrder) {
        if ("0.0".equals(confirmPay.getPayPrice())) {
            return;
        }
        Product product = new Product("洗车服务",
                "洗车服务", Double.parseDouble(confirmPay.getPayPrice()), myOrder.getOrderNo());

        Alipay alipay = new Alipay(this, BaseAPIRequest.URL_API
                + "/alipay_return");
        alipay.setCallBack(new Alipay.CallBack() {
            @Override
            public void onSuccess() {
                Intent intentResult = new Intent(MyOrderSetActivity.this, AfterPayActivity.class);
                intentResult.putExtra("orderId", myOrder.getOrderId());
                intentResult.putExtra("confirm_pay", confirmPay);
                startActivity(intentResult);
                myOrderAdapter.notifyDataSetChanged();
                showToast("支付成功");

//                finish();
            }

            @Override
            public void onFailed() {
                showToast("支付失败");
            }
        });
        alipay.pay(product);
    }


}
