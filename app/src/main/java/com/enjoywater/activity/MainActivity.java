package com.enjoywater.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.enjoywater.R;
import com.enjoywater.fragment.main.HomeFragment;
import com.enjoywater.fragment.main.ProductFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.v_cordinator_home)
    View vCordinatorHome;
    @BindView(R.id.iv_tab_home)
    ImageView ivTabHome;
    @BindView(R.id.tab_home)
    RelativeLayout tabHome;
    @BindView(R.id.v_cordinator_product)
    View vCordinatorProduct;
    @BindView(R.id.iv_tab_product)
    ImageView ivTabProduct;
    @BindView(R.id.tab_product)
    RelativeLayout tabProduct;
    @BindView(R.id.btn_setting)
    ImageView btnSetting;
    @BindView(R.id.layout_left)
    RelativeLayout layoutLeft;
    @BindView(R.id.frame_main_container)
    FrameLayout frameMainContainer;
    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;
    private ProductFragment mProductFragment;
    private int mCurrentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = HomeFragment.getInstance();
        mFragmentManager.beginTransaction().add(R.id.frame_main_container, mHomeFragment).commit();
        mCurrentTab = 0;
        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTab != 0) {
                    changeTab(0);
                }
            }
        });
        tabProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTab != 1) {
                    changeTab(1);
                }
            }
        });
    }

    private void changeTab(int tab) {
        Log.e("changeTab", tab+"");
        switch (tab) {
            case 0: {
                tabHome.setBackgroundResource(R.color.bg_home_tab_selected);
                vCordinatorHome.setBackgroundResource(R.color.colorMain);
                ivTabHome.setImageResource(R.drawable.ic_home_active);
                tabProduct.setBackgroundResource(R.color.bg_home_tab_bar);
                vCordinatorProduct.setBackgroundResource(R.color.bg_home_tab_bar);
                ivTabProduct.setImageResource(R.drawable.ic_product);
                mFragmentManager.beginTransaction().replace(R.id.frame_main_container, mHomeFragment).commit();
                mCurrentTab = 0;
                break;
            }
            case 1: {
                tabHome.setBackgroundResource(R.color.bg_home_tab_bar);
                vCordinatorHome.setBackgroundResource(R.color.bg_home_tab_bar);
                ivTabHome.setImageResource(R.drawable.ic_home);
                tabProduct.setBackgroundResource(R.color.bg_home_tab_selected);
                vCordinatorProduct.setBackgroundResource(R.color.colorMain);
                ivTabProduct.setImageResource(R.drawable.ic_product_active);
                if (mProductFragment == null) mProductFragment = ProductFragment.getInstance();
                mFragmentManager.beginTransaction().replace(R.id.frame_main_container, mProductFragment).commit();
                mCurrentTab = 1;
                break;
            }
            default:break;
        }
    }
}
