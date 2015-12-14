package com.jph.xibaibai.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Address;
import com.jph.xibaibai.model.entity.AllAddress;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Eric on 2015/12/9.
 * 车辆位置选择
 */
public class AddressSelectActivity extends TitleActivity implements View.OnClickListener {

    private IAPIRequests mAPIRequests;

    private int uid;
    // 保存获取的数据
    private AllAddress allAddress;

    private LocalBroadcastManager lBManager = null;

    private LocalReceiver localReceiver = null;

    @ViewInject(R.id.title_txt)
    private TextView title_txt;
    @ViewInject(R.id.address_home_locate)
    TextView mTxtHome; // 家庭住址
    @ViewInject(R.id.address_home_mark)
    TextView mTxtHomePark;
    @ViewInject(R.id.address_company_locate)
    TextView mTxtCpmpany;// 公司住址
    @ViewInject(R.id.address_company_mark)
    TextView mTxtCpmpanyPark;
    @ViewInject(R.id.address_home_ll)
    LinearLayout address_home_ll;
    @ViewInject(R.id.address_company_ll)
    LinearLayout address_company_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        mAPIRequests.getAddress(uid);
        getDataBroadcast();
    }

    @Override
    public void initData() {
        super.initData();
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        mAPIRequests = new APIRequests(this);
    }

    @Override
    public void initView() {
        super.initView();
        title_txt.setText(getString(R.string.address_select));
    }

    /**
     * 设置家庭地址
     */
    private void setAddressData() {
        Address addressHome = allAddress.getHomeAddress();
        Address addressCompany = allAddress.getCompanyAddress();
        if(addressHome != null){
            mTxtHome.setText(addressHome.getAddress());
            mTxtHomePark.setText(addressHome.getAddress_info());
            address_home_ll.setVisibility(View.VISIBLE);
        }
        if(addressCompany != null){
            mTxtCpmpany.setText(addressCompany.getAddress());
            mTxtCpmpanyPark.setText(addressCompany.getAddress_info());
            address_company_ll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.GETADDRESS:
                if (responseJson.getResult() != null) {
                    allAddress = JSON.parseObject(responseJson.getResult().toString(), AllAddress.class);
                    if (allAddress != null) {
                        SPUserInfo.getsInstance(this).setSP(SPUserInfo.KEY_ALL_ADDRESS, responseJson.getResult().toString());
                        setAddressData();
                    }
                }
                break;
        }
    }

    @OnClick({R.id.address_home_ll, R.id.address_company_ll,R.id.address_snap_rl})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.address_home_ll:
                Address addressHome = allAddress.getHomeAddress();
                intent.putExtra("LocateAddress",addressHome);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.address_company_ll:
                Address addressCompany = allAddress.getCompanyAddress();
                intent.putExtra("LocateAddress",addressCompany);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.address_snap_rl:
                intent.setClass(AddressSelectActivity.this, LocateSelectActivity.class);
                intent.putExtra(LocateSelectActivity.WHEREINTO,2);
                startActivity(intent);
                break;
        }
    }
    private void getDataBroadcast(){
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
            if(intent.getAction().equals("com.xbb.broadcast.UPDATE_ADDRESS")){
                finish();
            }
        }
    }
}
