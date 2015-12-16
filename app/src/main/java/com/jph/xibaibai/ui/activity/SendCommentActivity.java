package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.Constants;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/10 10:27
 * 描述：$TODO
 */
public class SendCommentActivity extends TitleActivity implements View.OnClickListener {


    @ViewInject(R.id.coment_level)
    private RatingBar coment_level;
    @ViewInject(R.id.comment_content_et)
    private EditText comment_content_et;
    @ViewInject(R.id.comment_text_count)
    private TextView comment_text_count;
    @ViewInject(R.id.comment_submit_btn)
    private Button comment_submit_btn;

    private IAPIRequests mApiRequests;
    private String orderId;
    private int uid;
    private int textSum;

    private int level;
    private String comment;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);
    }

    @Override
    public void initData() {
        super.initData();
        mApiRequests = new APIRequests(this);
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        orderId = getIntent().getStringExtra("orderId");
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        textSum = 500;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.comment_title));
        comment_content_et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            public void afterTextChanged(Editable s) {
                int number = s.length();
                comment_text_count.setText("" + number);
                selectionStart = comment_content_et.getSelectionStart();
                selectionEnd = comment_content_et.getSelectionEnd();
                if (temp.length() > textSum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    comment_content_et.setText(s);
                    comment_content_et.setSelection(tempSelection);//设置光标在最后
                    showToast(getString(R.string.comment_content_long));
                }
            }
        });
        coment_level.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                level = (int) rating;
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        comment_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_submit_btn:
                comment = comment_content_et.getText() != null ? comment_content_et.getText().toString() : "";
                if (StringUtil.isNull(comment)) {
                    showToast(getString(R.string.comment_hint));
                    return;
                }
                if (comment.length() > 500) {
                    showToast(getString(R.string.comment_content_long));
                    return;
                }
                mApiRequests.sendComment(orderId, uid, comment, level);
                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Tasks.ORDER_COMMENT:
                showToast(getString(R.string.comment_succeed));
                localBroadcastManager.sendBroadcast(new Intent(Constants.IntentAction.COMMENT_SUCCEED).putExtra("orderId",orderId));
                finish();
                break;
        }
    }
}
