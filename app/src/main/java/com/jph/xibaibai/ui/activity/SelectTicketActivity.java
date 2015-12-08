package com.jph.xibaibai.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.TicketAdapter;
import com.jph.xibaibai.model.entity.Coupon;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.parsejson.TicketParse;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/7 11:30
 * 描述：$TODO
 */
public class SelectTicketActivity extends TitleActivity {

    @ViewInject(R.id.ticket_rule_layout)
    LinearLayout ticket_rule_layout;
    @ViewInject(R.id.ticket_lv)
    ListView ticket_lv;

    private TicketAdapter ticketAdapter;

    private IAPIRequests mAPIRequests;

    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        mAPIRequests.getTicketList(155);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.ticket_title));
    }

    @Override
    public void initData() {
        super.initData();
        mAPIRequests = new APIRequests(this);
        uid = SPUserInfo.getsInstance(SelectTicketActivity.this).getSPInt(SPUserInfo.KEY_USERID);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.TICKET_LIST:
                List<Coupon> ticketList = TicketParse.getCouponList(responseJson.getResult() + "");
                if (ticketList != null && !ticketList.isEmpty()) {
                    ticketAdapter = new TicketAdapter(ticketList, this);
                    ticket_lv.setAdapter(ticketAdapter);
                }
                break;
        }
    }
}
