package com.enjoywater.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.fragment.LoginFragment;
import com.enjoywater.utils.Utils;

public class LoginActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private String mEmail, mPassword, mName, mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        fragmentManager = getSupportFragmentManager();
        LoginFragment fragmentEmail = LoginFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.frame_login_container, fragmentEmail).addToBackStack(fragmentEmail.getClass().getName()).commit();
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
