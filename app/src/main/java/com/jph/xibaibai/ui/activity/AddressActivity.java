package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
 * 地址车位
 * Created by Eric on 2015/12/12.
 */
public class AddressActivity extends TitleActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_HOME = 1023;
    private static final int REQUEST_CODE_COMPANY = 1025;
    public static final String RESULT_ADDRESS = "resultAddress";

    private IAPIRequests mAPIRequests;
    private AllAddress allAddress;
    private int uid;

    @ViewInject(R.id.usead_home_locate)
    TextView mTxtHome;
    @ViewInject(R.id.usead_home_mark)
    TextView mTxtHomePark;
    @ViewInject(R.id.usead_company_locate)
    TextView mTxtCpmpany;
    @ViewInject(R.id.usead_company_mark)
    TextView mTxtCpmpanyPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        mAPIRequests.getAddress(uid);
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
        setTitle("常用地址");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Address address = (Address) data.getSerializableExtra(RESULT_ADDRESS);
        if(address == null){
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_HOME:
                mTxtHome.setText(address.getAddress());
                mTxtHomePark.setText(address.getAddress_info());
                address.setAddress_type(0);
                break;
            case REQUEST_CODE_COMPANY:
                mTxtCpmpany.setText(address.getAddress());
                mTxtCpmpanyPark.setText(address.getAddress_info());
                address.setAddress_type(1);
                break;
        }
        address.setUid(uid);
        mAPIRequests.setAddress(address);
    }

    @OnClick({R.id.usead_home_ll, R.id.usead_company_ll})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.usead_home_ll:
                //选择家地址
                intent.setClass(AddressActivity.this, LocateSelectActivity.class);
                intent.putExtra(LocateSelectActivity.WHEREINTO, 3);
                startActivityForResult(intent, REQUEST_CODE_HOME);
                break;
            case R.id.usead_company_ll:
                //家停车位
                intent.setClass(AddressActivity.this, LocateSelectActivity.class);
                intent.putExtra(LocateSelectActivity.WHEREINTO, 3);
                startActivityForResult(intent, REQUEST_CODE_COMPANY);
                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.GETADDRESS:
                //查询用户地址
                allAddress = null;
                if (responseJson.getResult() != null) {
                    Log.i("Tag","Address=>"+responseJson.getResult().toString());
                    allAddress = JSON.parseObject(responseJson.getResult().toString(), AllAddress.class);
                }
                if (allAddress != null) {
                    Address addressHome = allAddress.getHomeAddress();
                    if (addressHome != null) {
                        mTxtHome.setText(addressHome.getAddress());
                        mTxtHomePark.setText(addressHome.getAddress_info());
                    }

                    Address addressCompany = allAddress.getCompanyAddress();
                    if (addressCompany != null) {
                        mTxtCpmpany.setText(addressCompany.getAddress());
                        mTxtCpmpanyPark.setText(addressCompany.getAddress_info());
                    }
                }
                break;
        }
    }
}
