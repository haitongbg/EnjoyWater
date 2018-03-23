package com.enjoywater.fragment.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.activity.LoginActivity;
import com.enjoywater.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPasswordFragment extends Fragment {
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.iv_submit_password)
    ImageView ivSubmitPassword;
    @BindView(R.id.edt_submit_password)
    EditText edtSubmitPassword;
    @BindView(R.id.btn_see_password)
    ImageView btnSeePassword;
    @BindView(R.id.layout_submit_password)
    RelativeLayout layoutSubmitPassword;
    @BindView(R.id.btn_next)
    CircleImageView btnNext;
    @BindView(R.id.layout_fill_password)
    LinearLayout layoutFillPassword;
    @BindView(R.id.tv_alert_password_error)
    TextView tvAlertPasswordError;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public static RegisterPasswordFragment newInstance() {
        RegisterPasswordFragment fragment = new RegisterPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_password, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    private void initUI() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.popBackStackImmediate();
            }
        });
        btnNext.setClickable(false);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnNext.isClickable()) {
                    String password = edtSubmitPassword.getText().toString();
                    if (Utils.isValidPasswordSimple(password)) {
                        tvAlertPasswordError.setVisibility(View.INVISIBLE);
                        ((LoginActivity) mContext).setPassword(password);
                        RegisterNameFragment fragmentName = RegisterNameFragment.newInstance();
                        mFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out, R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out)
                                .replace(R.id.frame_login_container, fragmentName)
                                .addToBackStack(fragmentName.getClass().getName())
                                .commit();
                    } else {
                        tvAlertPasswordError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        edtSubmitPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnNext.setImageResource(R.drawable.ic_next_available);
                    btnNext.setClickable(true);
                } else {
                    btnNext.setImageResource(R.drawable.ic_next_unavailable);
                    btnNext.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSeePassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        edtSubmitPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        edtSubmitPassword.setSelection(edtSubmitPassword.length());
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        edtSubmitPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edtSubmitPassword.setSelection(edtSubmitPassword.length());
                        break;
                    }
                    case MotionEvent.ACTION_OUTSIDE: {
                        edtSubmitPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        edtSubmitPassword.setSelection(edtSubmitPassword.length());
                        break;
                    }
                }
                return true;
            }
        });
    }
}
