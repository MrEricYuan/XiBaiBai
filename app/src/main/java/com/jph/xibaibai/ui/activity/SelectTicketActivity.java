package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.TicketAdapter;
import com.jph.xibaibai.model.entity.Coupon;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.mview.CustomListView;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.parsejson.TicketParse;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import javax.sql.RowSetReader;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/7 11:30
 * 描述：$TODO
 */
public class SelectTicketActivity extends TitleActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    @ViewInject(R.id.ticket_rule_layout)
    LinearLayout ticket_rule_layout;
    @ViewInject(R.id.ticket_lv)
    CustomListView ticket_lv;

    private List<Coupon> ticketList = null;

    private TicketAdapter ticketAdapter;

    private IAPIRequests mAPIRequests;

    private int uid;
    // 从下单页面进入
    private boolean isPlaceOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        mAPIRequests.getTicketList(uid);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.ticket_title));
        ticket_lv.setAdapter(null);
        ticket_lv.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mAPIRequests = new APIRequests(this);
        uid = SPUserInfo.getsInstance(SelectTicketActivity.this).getSPInt(SPUserInfo.KEY_USERID);
        isPlaceOrder = getIntent().getBooleanExtra("isPlaceOrder",false);
    }

    @Override
    public void initListener() {
        super.initListener();
        ticket_rule_layout.setOnClickListener(this);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.TICKET_LIST:
                ticketList = TicketParse.getCouponList(responseJson.getResult().toString());
                if (ticketList != null && !ticketList.isEmpty()) {
                    ticketAdapter = new TicketAdapter(ticketList, this);
                    ticket_lv.setAdapter(ticketAdapter);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticket_rule_layout:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!isPlaceOrder){
            return;
        }
        if (ticketList != null && !ticketList.isEmpty()) {
            if(ticketList.get(position).getState() == 1){
                showToast(getString(R.string.mycoupons_used));
                return;
            }
            if(ticketList.get(position).getState() == 2){
                showToast(getString(R.string.mycoupons_dated));
                return;
            }
            if(ticketList.get(position).getState() == 3){
                showToast(getString(R.string.mycoupons_dating));
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(PlaceOrdersActivity.COUPONSFLAG,ticketList.get(position));
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
