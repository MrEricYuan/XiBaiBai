package com.jph.xibaibai.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * 创建时间：2015/12/11 11:55
 * 描述：$TODO
 */
public class FeedbackActivity extends TitleActivity implements View.OnClickListener {
    @ViewInject(R.id.feedback_content_et)
    private EditText feedback_content_et;
    @ViewInject(R.id.feedback_text_count)
    private TextView feedback_text_count;
    @ViewInject(R.id.feedback_submit_btn)
    private Button feedback_submit_btn;

    private IAPIRequests mApiRequests;
    private int uid;
    private int textSum;
    private String feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public void initData() {
        super.initData();
        mApiRequests = new APIRequests(this);
        uid = SPUserInfo.getsInstance(this).getSPInt(SPUserInfo.KEY_USERID);
        textSum = 400;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.feeedback_title));
        feedback_content_et.addTextChangedListener(new TextWatcher() {
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
                feedback_text_count.setText("" + number);
                selectionStart = feedback_content_et.getSelectionStart();
                selectionEnd = feedback_content_et.getSelectionEnd();
                if (temp.length() > textSum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    feedback_content_et.setText(s);
                    feedback_content_et.setSelection(tempSelection);//设置光标在最后
                    showToast(getString(R.string.comment_content_long));
                }
            }
        });
    }


    @OnClick(R.id.feedback_submit_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_submit_btn:
                feedback = feedback_content_et.getText() != null ? feedback_content_et.getText().toString() : "";
                if (StringUtil.isNull(feedback)) {
                    showToast(getString(R.string.comment_hint));
                    return;
                }
                if (feedback.length() > 400) {
                    showToast(getString(R.string.feedback_content_long));
                    return;
                }
                mApiRequests.suggestion(uid, feedback);
                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Tasks.SUGGESTION:
                showToast(getString(R.string.opeartion_succeed));
                finish();
                break;
        }
    }
}
