package com.enjoywater.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activity.MyApplication;
import com.enjoywater.adapter.ConfirmOrderAdapter;
import com.enjoywater.entity.Product;
import com.enjoywater.entity.UserInfo;
import com.enjoywater.service.BaseResponse;
import com.enjoywater.service.MainService;
import com.enjoywater.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

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
    @BindView(R.id.tv_transport_fee)
    TvSegoeuiSb tvTransportFee;
    @BindView(R.id.tv_total_price)
    TvSegoeuiSb tvTotalPrice;
    @BindView(R.id.srcollView)
    NestedScrollView srcollView;
    @BindView(R.id.btn_order)
    TvSegoeuiBolb btnOrder;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;

    private Context mContext;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private Message mMessage;
    private ArrayList<Product> mListProduct;
    private LinearLayoutManager mLayoutManager;
    private ConfirmOrderAdapter mProductAdapter;
    private UserInfo mOrderInfo;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");
    private int mTransportFee = 20000;
    private long mTotalPrice;
    private boolean isLoading = false;
    private MainService mainService;

    public DialogConfirmOrder(Context context, Handler callBackHandler, ArrayList<Product> confirmProducts, UserInfo orderInfo) {
        this.mContext = context;
        this.mCallBackHandler = callBackHandler;
        this.mMessage = new Message();
        this.mListProduct = confirmProducts;
        this.mOrderInfo = orderInfo;
        this.mainService = MyApplication.getInstance().getMainService();
        initUI();
    }

    private void initUI() {
        mDialog = new Dialog(mContext, R.style.AppTheme_NoActionBar);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirm_order);
        ButterKnife.bind(DialogConfirmOrder.this, mDialog);
        mDialog.getWindow().setWindowAnimations(R.style.CustomDialogRightInAnimation);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvListProduct.setLayoutManager(mLayoutManager);
        tvNameAndPhone.setText(mOrderInfo.getFulname() + " - " + mOrderInfo.getPhone());
        tvAddress.setText(mOrderInfo.getAddress());
        mProductAdapter = new ConfirmOrderAdapter(mContext, mListProduct);
        rvListProduct.setAdapter(mProductAdapter);
        tvTransportFee.setText(formatter.format(mTransportFee) + " đ");
        mTotalPrice = 0;
        for (Product product : mListProduct) {
            mTotalPrice += product.getCount() * product.getPrice();
        }
        mTotalPrice += mTransportFee;
        tvTotalPrice.setText(formatter.format(mTotalPrice) + " đ");
        //action button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessage.what = ACTION_ClOSE;
                mCallBackHandler.sendMessage(mMessage);
                mDialog.dismiss();
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    if (Utils.isInternetOn(mContext)) makeOrder();
                    else {
                        Toast.makeText(mContext, R.string.alert_no_internet_signal, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //loading
        progressLoading.setProgress(0.5f);
        progressLoading.setCallback(new ProgressWheel.ProgressCallback() {
            @Override
            public void onProgressUpdate(float progress) {
                if (progress == 0) {
                    progressLoading.setProgress(1.0f);
                } else if (progress == 1.0f) {
                    progressLoading.setProgress(0.0f);
                }
            }
        });
        mDialog.show();
    }

    private void makeOrder() {
        isLoading = true;
        layoutLoading.setVisibility(View.VISIBLE);
        //Call<BaseResponse> makeOrder = mainService.order(mOrderInfo.getToken(), mOrderInfo.getLatitude(), mOrderInfo.getLongitude(), mOrderInfo.getAddress(), )
        mMessage.what = ACTION_SEND;
        mMessage.obj = mListProduct;
        mCallBackHandler.sendMessage(mMessage);
    }
}
