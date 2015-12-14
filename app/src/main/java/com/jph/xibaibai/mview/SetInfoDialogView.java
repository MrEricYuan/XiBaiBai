package com.jph.xibaibai.mview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jph.xibaibai.R;

/**
 * 自定义dialog,
 */
public class SetInfoDialogView extends Dialog {

    private SetClickListener clickListener;
    private TextView my_dialog_message;
    private TextView my_dialog_message1;
    private TextView dialog_cancel_tv;
    private TextView dialog_sure_tv;
    private String message;
    private String msgTips;

    public SetInfoDialogView(Context context) {
        super(context, R.style.DialogStyleBottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setinfo_dialog);
        my_dialog_message = (TextView) findViewById(R.id.my_dialog_message);
        my_dialog_message1 = (TextView) findViewById(R.id.my_dialog_message1);
        dialog_cancel_tv = (TextView) findViewById(R.id.dialog_cancel_tv);
        dialog_sure_tv = (TextView) findViewById(R.id.dialog_sure_tv);
        dialog_cancel_tv.setOnClickListener(listener);
        dialog_sure_tv.setOnClickListener(listener);
        my_dialog_message.setText(msgTips);
        my_dialog_message1.setText(message);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMsgTips(String msgTips) {
        this.msgTips = msgTips;
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                switch (v.getId()) {
                    case R.id.dialog_sure_tv:
                        clickListener.confirm();
                        break;
                    case R.id.dialog_cancel_tv:
                        clickListener.cancel();
                        break;
                }
            }
        }
    };

    public void setClickListener(SetClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface SetClickListener {
        public void cancel();
        public void confirm();
    }
}
