package com.enjoywater.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.enjoywater.R;
import com.enjoywater.fragment.main.HomeFragment;

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
    @BindView(R.id.layout_tabbar)
    LinearLayout layoutTabbar;
    @BindView(R.id.btn_setting)
    ImageView btnSetting;
    @BindView(R.id.layout_left)
    RelativeLayout layoutLeft;
    @BindView(R.id.frame_main_container)
    FrameLayout frameMainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        HomeFragment homeFragment = HomeFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_main_container, homeFragment).addToBackStack(homeFragment.getClass().getName()).commit();
    }
}
