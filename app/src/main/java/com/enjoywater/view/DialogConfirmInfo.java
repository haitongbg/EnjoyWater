package com.enjoywater.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.entity.UserInfo;
import com.enjoywater.utils.Constant;
import com.enjoywater.utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogConfirmInfo {
    public static int ACTION_ClOSE = 0x101;
    public static int ACTION_SEND = 0x010;
    @BindView(R.id.edt_submit_name)
    EditText edtSubmitName;
    @BindView(R.id.edt_submit_phone)
    EditText edtSubmitPhone;
    @BindView(R.id.edt_submit_note)
    EditText edtSubmitNote;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_send)
    Button btnSend;
    private Gson gson = new Gson();
    private Context mContext;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private Message mMessage;
    private UserInfo mInfo;

    public DialogConfirmInfo(Context context, Handler callBackHandler) {
        this.mContext = context;
        this.mCallBackHandler = callBackHandler;
        this.mMessage = new Message();
        this.mInfo = gson.fromJson(Utils.getStringNotNull(mContext, Constant.USER_LOGIN_INFO), UserInfo.class);
        initUI();
    }

    private void initUI() {
        mDialog = new Dialog(mContext, R.style.AppTheme_NoActionBar);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirm_info);
        ButterKnife.bind(DialogConfirmInfo.this, mDialog);
        String name = mInfo.getFulname();
        if (name != null && !name.isEmpty()) edtSubmitName.setText(name);
        String phone = mInfo.getPhone();
        if (phone != null && !phone.isEmpty()) edtSubmitPhone.setText(phone);
        //
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessage.what = ACTION_ClOSE;
                mCallBackHandler.sendMessage(mMessage);
                mDialog.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtSubmitName.getText().toString();
                String phone = edtSubmitPhone.getText().toString();
                String note = edtSubmitNote.getText().toString();
                if (name.isEmpty()) Toast.makeText(mContext, "Vui lòng nhập họ tên người nhận hàng.", Toast.LENGTH_SHORT).show();
                else if (phone.isEmpty() || !Utils.isValidPhone(phone)) Toast.makeText(mContext, "Vui lòng nhập số điện thoại hợp lệ.", Toast.LENGTH_SHORT).show();
                else {
                    mInfo.setFulname(name);
                    mInfo.setPhone(phone);
                    mInfo.setNote(note);
                    mMessage.what = ACTION_SEND;
                    mMessage.obj = mInfo;
                    mCallBackHandler.sendMessage(mMessage);
                    mDialog.dismiss();
                }
            }
        });
        mDialog.show();
    }
}
