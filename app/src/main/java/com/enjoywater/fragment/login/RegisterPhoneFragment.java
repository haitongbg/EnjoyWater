package com.enjoywater.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.enjoywater.activity.MainActivity;
import com.enjoywater.activity.MyApplication;
import com.enjoywater.entity.UserInfo;
import com.enjoywater.service.ApiConstant;
import com.enjoywater.service.BaseResponse;
import com.enjoywater.service.MainService;
import com.enjoywater.utils.Constant;
import com.enjoywater.view.ProgressWheel;
import com.enjoywater.utils.Utils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPhoneFragment extends Fragment {
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.iv_submit_phone)
    ImageView ivSubmitPhone;
    @BindView(R.id.edt_submit_phone)
    EditText edtSubmitPhone;
    @BindView(R.id.layout_submit_phone)
    RelativeLayout layoutSubmitPhone;
    @BindView(R.id.btn_next)
    CircleImageView btnNext;
    @BindView(R.id.layout_fill_phone)
    LinearLayout layoutFillPhone;
    @BindView(R.id.progress_loading)
    ProgressWheel progressLoading;
    @BindView(R.id.layout_loading)
    RelativeLayout layoutLoading;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private MainService mainService;
    private boolean isLoading = false;
    private LoginActivity mActivity;
    private Gson gson = new Gson();

    public static RegisterPhoneFragment newInstance() {
        RegisterPhoneFragment fragment = new RegisterPhoneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mFragmentManager = getFragmentManager();
        mainService = MyApplication.getInstance().getMainService();
        mActivity = (LoginActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_phone, container, false);
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
                    String phone = edtSubmitPhone.getText().toString().trim();
                    if (!phone.isEmpty() && Utils.isValidPhone(phone)) {
                        checkPhone(phone);
                    } else {
                        Toast.makeText(mContext, "Nhập số điện thoại hợp lệ để tiếp tục", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        edtSubmitPhone.addTextChangedListener(new TextWatcher() {
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
        //
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
    }

    private void checkPhone(final String phone) {
        if (Utils.isInternetOn(mContext)) {
            if (!isLoading) {
                isLoading = true;
                layoutLoading.setVisibility(View.VISIBLE);
                Call<BaseResponse> checkPhone = mainService.checkPhone(phone);
                checkPhone.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null) {
                            BaseResponse baseResponse = response.body();
                            if (baseResponse.getStatusCode() == ApiConstant.Value.STATUS_CODE_SUCCESS) {
                                mActivity.setPhone(phone);
                                registerUser();
                            } else if (baseResponse.getStatusCode() == ApiConstant.Value.STATUS_CODE_ERROR) {
                                String message = baseResponse.getMessage();
                                if (message == null || message.isEmpty()) message = mContext.getResources().getString(R.string.alert_data_error);
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                isLoading = false;
                                layoutLoading.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(mContext, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                            isLoading = false;
                            layoutLoading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(mContext, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        layoutLoading.setVisibility(View.GONE);
                    }
                });
            }
        } else {
            Toast.makeText(mContext, R.string.alert_no_internet_signal, Toast.LENGTH_SHORT).show();
        }
    }

    public void registerUser() {
        if (Utils.isInternetOn(mContext)) {
            String email = mActivity.getEmail();
            String phone = mActivity.getPhone();
            String name = mActivity.getName();
            String password = mActivity.getPassword();
            Call<BaseResponse> registerUser = mainService.registerUser(email, phone, name, password);
            registerUser.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        BaseResponse baseResponse = response.body();
                        if (baseResponse.getStatusCode() == ApiConstant.Value.STATUS_CODE_SUCCESS) {
                            UserInfo userInfo = gson.fromJson(gson.toJson(baseResponse.getData()), UserInfo.class);
                            if (userInfo != null) {
                                Log.e("User registed", gson.toJson(userInfo));
                                Utils.saveString(mContext, Constant.USER_LOGIN_INFO, (new Gson()).toJson(userInfo));
                                mActivity.startActivity(new Intent(mActivity, MainActivity.class));
                                mActivity.overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
                            }
                        } else if (baseResponse.getStatusCode() == ApiConstant.Value.STATUS_CODE_ERROR) {
                            String message = baseResponse.getMessage();
                            if (message == null || message.isEmpty()) message = mContext.getResources().getString(R.string.alert_data_error);
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                        isLoading = false;
                        layoutLoading.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(mContext, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        layoutLoading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(mContext, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                    isLoading = false;
                    layoutLoading.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(mContext, R.string.alert_no_internet_signal, Toast.LENGTH_SHORT).show();
            isLoading = false;
            layoutLoading.setVisibility(View.GONE);
        }
    }
}
