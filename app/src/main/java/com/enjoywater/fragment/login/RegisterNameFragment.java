package com.enjoywater.fragment.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterNameFragment extends Fragment {
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.iv_submit_name)
    ImageView ivSubmitName;
    @BindView(R.id.edt_submit_name)
    EditText edtSubmitName;
    @BindView(R.id.layout_submit_name)
    RelativeLayout layoutSubmitName;
    @BindView(R.id.btn_next)
    CircleImageView btnNext;
    @BindView(R.id.layout_fill_name)
    LinearLayout layoutFillName;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public static RegisterNameFragment newInstance() {
        RegisterNameFragment fragment = new RegisterNameFragment();
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
        View view = inflater.inflate(R.layout.fragment_register_name, container, false);
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
                    String name = edtSubmitName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        ((LoginActivity) mContext).setName(name);
                        RegisterPhoneFragment fragmentPhone = RegisterPhoneFragment.newInstance();
                        mFragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out, R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out)
                                .replace(R.id.frame_login_container, fragmentPhone)
                                .addToBackStack(fragmentPhone.getClass().getName())
                                .commit();
                    } else {
                        Toast.makeText(mContext, "Nhập tên của bạn để tiếp tục", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        edtSubmitName.addTextChangedListener(new TextWatcher() {
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
    }
}
