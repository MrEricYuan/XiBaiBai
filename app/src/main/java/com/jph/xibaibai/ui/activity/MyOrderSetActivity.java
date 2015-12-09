package com.jph.xibaibai.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.MyOrderAdapter;
import com.jph.xibaibai.model.entity.MyOrder;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.parsejson.OrderParse;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/8 15:26
 * 描述：$TODO
 */
public class MyOrderSetActivity extends TitleActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {
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
        showLoading=true;
        getOrderList();
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
        myorder_list_lv.setOnItemClickListener(this);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
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
                        myOrderAdapter = new MyOrderAdapter(myOrderShowList, MyOrderSetActivity.this);

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
        showLoading=false;
        isAdd = true;
        pageindex++;
        getOrderList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 向服务器请求订单列表数据
     */
    private void getOrderList() {
        if (apiRequests == null)
            apiRequests = new APIRequests(this);
        apiRequests.getMyOrderList(uid, pageindex, pagesize);
    }
}
