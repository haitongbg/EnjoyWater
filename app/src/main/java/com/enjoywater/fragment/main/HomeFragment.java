package com.enjoywater.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoywater.R;
import com.enjoywater.activity.MyApplication;
import com.enjoywater.adapter.HomeAdapter;
import com.enjoywater.entity.News;
import com.enjoywater.entity.UserInfo;
import com.enjoywater.service.BaseResponse;
import com.enjoywater.service.MainService;
import com.enjoywater.utils.Constant;
import com.enjoywater.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static HomeFragment instance;
    @BindView(R.id.rvHome)
    RecyclerView rvHome;
    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Object> mListHome;
    private UserInfo mUserInfo;
    private MainService mainService;
    private Gson gson = new Gson();
    private HomeAdapter mHomeAdapter;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mListHome = new ArrayList<>();
        mUserInfo = gson.fromJson(Utils.getStringNotNull(mContext, Constant.USER_LOGIN_INFO), UserInfo.class);
        mainService = MyApplication.getInstance().getMainService();
        if (mUserInfo != null) {
            mListHome.add(mUserInfo);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initUI();
        getNews();
        return view;
    }

    private void initUI() {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvHome.setLayoutManager(mLayoutManager);
    }

    private void getNews() {
        Call<BaseResponse> getNews = mainService.getAllNews(mUserInfo.getToken());
        getNews.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null && response.body().isSuccess() && response.body().getData() != null) {
                    try {
                        JSONArray arrayNews = new JSONArray(gson.toJson(response.body().getData()));
                        for (int i = 0, z = arrayNews.length(); i<z; i++) {
                            Object o = arrayNews.get(i);
                            if (o instanceof JSONObject) {
                                News news = gson.fromJson(o.toString(), News.class);
                                if (news != null) {
                                    mListHome.add(news);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setDataHome();
                } else {

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setDataHome() {
        mHomeAdapter = new HomeAdapter(mContext, mListHome);
        rvHome.setAdapter(mHomeAdapter);
    }
}
