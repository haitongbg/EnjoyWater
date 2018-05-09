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
import android.widget.Button;
import android.widget.LinearLayout;

import com.enjoywater.R;
import com.enjoywater.adapter.ProductConfirmAdapter;
import com.enjoywater.entity.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogConfirmProduct {
    public static int ACTION_ClOSE = 0x101;
    public static int ACTION_SEND = 0x010;
    @BindView(R.id.rv_list_product)
    RecyclerView rvListProduct;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.layout_action)
    LinearLayout layoutAction;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    @BindView(R.id.tv_total_price)
    TvSegoeuiRegular tvTotalPrice;
    private Context mContext;
    private Dialog mDialog;
    private Handler mCallBackHandler;
    private Message mMessage;
    private ArrayList<Product> mListProduct;
    private LinearLayoutManager mLayoutManager;
    private ProductConfirmAdapter mProductAdapter;

    public DialogConfirmProduct(Context context, Handler callBackHandler, ArrayList<Product> selectedProducts) {
        this.mContext = context;
        this.mCallBackHandler = callBackHandler;
        this.mMessage = new Message();
        this.mListProduct = selectedProducts;
        initUI();
    }

    private void initUI() {
        mDialog = new Dialog(mContext, R.style.AppTheme_NoActionBar);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirm_product);
        ButterKnife.bind(DialogConfirmProduct.this, mDialog);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvListProduct.setLayoutManager(mLayoutManager);
        mProductAdapter = new ProductConfirmAdapter(mContext, mListProduct, itemClickListener);
        rvListProduct.setAdapter(mProductAdapter);
        updateTotalPrice();
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
                mMessage.what = ACTION_SEND;
                mMessage.obj = mListProduct;
                mCallBackHandler.sendMessage(mMessage);
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public interface ItemClickListener {
        void changeProductCount(int position, int count);

        void removeProduct(int position);
    }

    ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void changeProductCount(int position, int count) {
            mListProduct.get(position).setCount(count);
            updateTotalPrice();
        }

        @Override
        public void removeProduct(int position) {
            mListProduct.remove(position);
        }
    };

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (Product product : mListProduct) {
            totalPrice += product.getPrice() * product.getCount();
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        tvTotalPrice.setText(formatter.format(totalPrice) + " đ");
    }
}
