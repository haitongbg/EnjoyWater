package com.enjoywater.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.enjoywater.R;
import com.enjoywater.entity.UserLoginInfo;
import com.enjoywater.fragment.login.LoginFragment;
import com.enjoywater.utils.Constant;
import com.enjoywater.utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.frame_login_container)
    FrameLayout frameLoginContainer;
    private String mEmail, mPassword, mName, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        String json = Utils.getStringNotNull(this, Constant.USER_LOGIN_INFO);
        if (!json.isEmpty()) {
            UserLoginInfo mUserLoginInfo = (new Gson()).fromJson(json, UserLoginInfo.class);
            if (mUserLoginInfo != null && mUserLoginInfo.getToken() != null && !mUserLoginInfo.getToken().isEmpty()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                LoginFragment fragmentEmail = LoginFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.frame_login_container, fragmentEmail).addToBackStack(fragmentEmail.getClass().getName()).commit();
            }
        } else {
            LoginFragment fragmentEmail = LoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_login_container, fragmentEmail).addToBackStack(fragmentEmail.getClass().getName()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out);
        }
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
    }
}
