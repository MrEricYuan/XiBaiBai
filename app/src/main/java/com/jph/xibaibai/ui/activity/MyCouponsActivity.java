package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.MyGiftCoupon;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.parsejson.TicketParse;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/12 13:20
 * 描述：我的礼券
 */
public class MyCouponsActivity extends TitleActivity {
    @ViewInject(R.id.mycoupon_coupon_num)
    private TextView mycoupon_coupon_num;
    @ViewInject(R.id.mycoupon_points_num)
    private TextView mycoupon_points_num;

    private IAPIRequests mApiRequest;
    private int uid;
    private MyGiftCoupon giftCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycoupons);
    }

    @Override
    public void initData() {
        super.initData();
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        mApiRequest = new APIRequests(this);
        mApiRequest.getMyCouponInfo(uid);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.mycoupons_title));
    }

    @OnClick({R.id.mycoupon_change_layout, R.id.mycoupon_coupon_layout, R.id.mycoupon_points_layout})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.mycoupon_change_layout:
                startActivityForResult(new Intent(this, InputCouponActivity.class).putExtra("rule", giftCoupon.getChangeCouponUrl()), 101);
                break;
            case R.id.mycoupon_coupon_layout:
                startActivity(SelectTicketActivity.class);
                break;
            case R.id.mycoupon_points_layout:
                startActivity(IntegralActivity.class);
                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.MY_COUPONS:
                giftCoupon = TicketParse.getMyGiftCoupon(responseJson.getResult().toString());
                if (giftCoupon != null) {
                    mycoupon_coupon_num.setText(giftCoupon.getCouponAmount() + getString(R.string.mycoupons_coupon_unit));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case 101:
                    boolean hasSucceed = data.getBooleanExtra("succeed", false);
                    if (hasSucceed) {
                        if (mApiRequest == null)
                            mApiRequest = new APIRequests(this);
                        mApiRequest.getMyCouponInfo(uid);
                    }
                    break;
            }
        }
    }
}
