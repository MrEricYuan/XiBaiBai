package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/12 13:45
 * 描述：兑换优惠码
 */
public class InputCouponActivity extends TitleActivity {
    @ViewInject(R.id.coupon_code_et)
    private EditText coupon_code_et;

    private String ruleUrl;
    private int uid;
    private String couponCode;
    private IAPIRequests mApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_coupon);
    }

    @Override
    public void initData() {
        super.initData();
        ruleUrl = getIntent().getStringExtra("rule");
        uid= SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        mApiRequest = new APIRequests(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.input_coupon_title));
    }

    @OnClick({R.id.coupon_submit_btn, R.id.rule_layout})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.coupon_submit_btn:
                couponCode = coupon_code_et != null ? coupon_code_et.getText().toString() : "";
                if (StringUtil.isNull(couponCode)) {
                    return;
                }
                mApiRequest.exchangeCoupon(uid, couponCode);
                break;
            case R.id.rule_layout:

                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Tasks.EXCHANGE_COUPON:
                showToast(getString(R.string.exchange_succed));
                Intent intent = new Intent();
                intent.putExtra("succeed", true);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
