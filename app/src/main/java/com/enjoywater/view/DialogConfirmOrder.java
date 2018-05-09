package com.enjoywater.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.enjoywater.R;
import com.enjoywater.adapter.ProductConfirmAdapter;
import com.enjoywater.entity.Product;
import com.enjoywater.entity.UserInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogConfirmOrder {
    public static int ACTION_ClOSE = 0x101;
    public static int ACTION_SEND = 0x010;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_name_and_phone)
    TvSegoeuiSb tvNameAndPhone;
    @BindView(R.id.tv_address)
    TvSegoeuiRegular tvAddress;
    @BindView(R.id.rv_list_product)
    RecyclerView rvListProduct;

    private Context mContext;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private Message mMessage;
    private ArrayList<Product> mListProduct;
    private LinearLayoutManager mLayoutManager;
    private ProductConfirmAdapter mProductAdapter;
    private UserInfo mOrderInfo;

    public DialogConfirmOrder(Context context, Handler callBackHandler, ArrayList<Product> confirmProducts, UserInfo orderInfo) {
        this.mContext = context;
        this.mCallBackHandler = callBackHandler;
        this.mMessage = new Message();
        this.mListProduct = confirmProducts;
        this.mOrderInfo = orderInfo;
        initUI();
    }

    private void initUI() {
        mDialog = new Dialog(mContext, R.style.AppTheme_NoActionBar);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(DialogConfirmOrder.this, mDialog);
        mDialog.getWindow().setWindowAnimations(R.style.CustomDialogRightInAnimation);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvListProduct.setLayoutManager(mLayoutManager);
        //mProductAdapter = new ProductConfirmAdapter(mContext, mListProduct, itemClickListener);
        //rvListProduct.setAdapter(mProductAdapter);

        //
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessage.what = ACTION_ClOSE;
                mCallBackHandler.sendMessage(mMessage);
                mDialog.dismiss();
            }
        });
        /*btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessage.what = ACTION_SEND;
                mMessage.obj = mListProduct;
                mCallBackHandler.sendMessage(mMessage);
                mDialog.dismiss();
            }
        });*/
        mDialog.show();
    }
}
