package com.enjoywater.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.enjoywater.R;
import com.enjoywater.adapter.ProductConfirmAdapter;
import com.enjoywater.entity.Product;
import com.enjoywater.entity.UserInfo;
import com.enjoywater.view.TvSegoeuiBolb;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSb;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmOrderActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.layout_tab)
    LinearLayout layoutTab;
    @BindView(R.id.tv_name_and_phone)
    TvSegoeuiSb tvNameAndPhone;
    @BindView(R.id.tv_address)
    TvSegoeuiRegular tvAddress;
    @BindView(R.id.cardview_info)
    CardView cardviewInfo;
    @BindView(R.id.rv_list_product)
    RecyclerView rvListProduct;
    @BindView(R.id.cardview_products)
    CardView cardviewProducts;
    @BindView(R.id.btn_order)
    TvSegoeuiBolb btnOrder;
    @BindView(R.id.layout_main)
    RelativeLayout layoutMain;
    private ArrayList<Product> mListProduct;
    private UserInfo mOrderInfo;
    private LinearLayoutManager mLayoutManager;
    private ProductConfirmAdapter mProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
    }

    private void initUI() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvListProduct.setLayoutManager(mLayoutManager);
        //mProductAdapter = new ProductConfirmAdapter(mContext, mListProduct, itemClickListener);
        //rvListProduct.setAdapter(mProductAdapter);

        //
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
    }
}
