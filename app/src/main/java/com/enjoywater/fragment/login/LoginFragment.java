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
import android.widget.TextView;
import android.widget.Toast;

import com.enjoywater.R;
import com.enjoywater.activity.LoginActivity;
import com.enjoywater.activity.MyApplication;
import com.enjoywater.service.ApiConstant;
import com.enjoywater.service.BaseResponse;
import com.enjoywater.service.MainService;
import com.enjoywater.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_email)
    ImageView ivEmail;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.layout_login_by_email)
    RelativeLayout layoutLoginByEmail;
    @BindView(R.id.iv_fb)
    ImageView ivFb;
    @BindView(R.id.tv_fb)
    TextView tvFb;
    @BindView(R.id.layout_login_by_fb)
    RelativeLayout layoutLoginByFb;
    @BindView(R.id.layout_select_login_type)
    LinearLayout layoutSelectLoginType;
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.iv_submit_email)
    ImageView ivSubmitEmail;
    @BindView(R.id.edt_submit_email)
    EditText edtSubmitEmail;
    @BindView(R.id.layout_submit_email)
    RelativeLayout layoutSubmitEmail;
    @BindView(R.id.btn_next)
    CircleImageView btnNext;
    @BindView(R.id.layout_fill_email)
    LinearLayout layoutFillEmail;
    @BindView(R.id.layout_login)
    RelativeLayout layoutLogin;
    private Context mContext;
    private MainService mainService;
    FragmentManager mFragmentManager;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mainService = MyApplication.getInstance().getMainService();
        mFragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    private void initUI() {
        ivBanner.setVisibility(View.VISIBLE);
        layoutSelectLoginType.setVisibility(View.VISIBLE);
        layoutFillEmail.setVisibility(View.GONE);
        //
        layoutLoginByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBanner.setVisibility(View.GONE);
                layoutSelectLoginType.setVisibility(View.GONE);
                layoutFillEmail.setVisibility(View.VISIBLE);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutFillEmail.setVisibility(View.GONE);
                ivBanner.setVisibility(View.VISIBLE);
                layoutSelectLoginType.setVisibility(View.VISIBLE);
            }
        });
        btnNext.setClickable(false);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnNext.isClickable()) {
                    String email = edtSubmitEmail.getText().toString();
                    if (Utils.isValidEmail(email)) {
                        checkEmail(email);
                    } else {
                        Toast.makeText(mContext, "Nhập địa chỉ email hợp lệ để tiếp tục.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        edtSubmitEmail.addTextChangedListener(new TextWatcher() {
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

    private void checkEmail(final String email) {
        if (Utils.isInternetOn(mContext)) {
            Call<BaseResponse> checkEmail = mainService.checkEmail(email);
            checkEmail.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        BaseResponse baseResponse = response.body();
                        if (baseResponse.getStatusCode() == ApiConstant.Value.STATUS_CODE_SUCCESS) {
                            ((LoginActivity)mContext).setEmail(email);
                            RegisterPasswordFragment fragmentPassword = RegisterPasswordFragment.newInstance();
                            mFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out, R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out)
                                    .replace(R.id.frame_login_container, fragmentPassword)
                                    .addToBackStack(fragmentPassword.getClass().getName())
                                    .commit();
                        } else if (baseResponse.getStatusCode() == ApiConstant.Value.STATUS_CODE_ERROR) {
                            ((LoginActivity)mContext).setEmail(email);
                            LoginPasswordFragment passwordFragment = LoginPasswordFragment.newInstance();
                            mFragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out, R.anim.slide_left_to_right_in, R.anim.slide_left_to_right_out)
                                    .replace(R.id.frame_login_container, passwordFragment)
                                    .addToBackStack(passwordFragment.getClass().getName())
                                    .commit();
                        }
                    }

                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(mContext, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, R.string.alert_no_internet_signal, Toast.LENGTH_SHORT).show();
        }
    }
}
