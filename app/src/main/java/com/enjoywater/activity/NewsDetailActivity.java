package com.enjoywater.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.enjoywater.R;
import com.enjoywater.entity.News;
import com.enjoywater.entity.UserInfo;
import com.enjoywater.service.ApiConstant;
import com.enjoywater.service.BaseResponse;
import com.enjoywater.service.MainService;
import com.enjoywater.utils.Constant;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.TvSegoeuiSb;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity {
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_title)
    TvSegoeuiSb tvTitle;
    @BindView(R.id.web_detail)
    WebView webDetail;
    private News mNews;
    private MainService mainService;
    private UserInfo mUserInfo;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initUI();
        int newsId = getIntent().getIntExtra(ApiConstant.Param.ID, -1);
        if (newsId != -1) {
            mainService = MyApplication.getInstance().getMainService();
            mUserInfo = gson.fromJson(Utils.getStringNotNull(this, Constant.USER_LOGIN_INFO), UserInfo.class);
            getNewsDetail(newsId);
        } else {
            finish();
        }
    }

    private void initUI() {
        webDetail.getSettings().setJavaScriptEnabled(true);
        webDetail.getSettings().setLoadWithOverviewMode(true);
    }

    private void getNewsDetail(int newsId) {
        if (Utils.isInternetOn(this)) {
            Call<BaseResponse> getNewsDetail = mainService.getNewsDetail(mUserInfo.getToken(), newsId);
            getNewsDetail.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        if (response.body().isSuccess() && response.body().getData() != null) {
                            try {
                                JSONArray arrayNews = new JSONArray(gson.toJson(response.body().getData()));
                                Object o = arrayNews.get(0);
                                if (o instanceof JSONObject) {
                                    mNews = gson.fromJson(o.toString(), News.class);
                                    if (mNews != null) {
                                        setData();
                                    } else {
                                        Toast.makeText(NewsDetailActivity.this, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(NewsDetailActivity.this, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String message = response.body().getMessage();
                            if (message == null || message.isEmpty())
                                message = getResources().getString(R.string.alert_data_error);
                            Toast.makeText(NewsDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(NewsDetailActivity.this, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(NewsDetailActivity.this, R.string.alert_data_error, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(NewsDetailActivity.this, R.string.alert_no_internet_signal, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setData() {
        Glide.with(NewsDetailActivity.this).load(mNews.getImage()).into(ivBanner);
        String title = mNews.getName();
        if (title != null && !title.isEmpty()) {
            tvTitle.setText(mNews.getName());
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        webDetail.loadDataWithBaseURL("", "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + "<body style='margin:0;padding:0;'>" + mNews.getDetail() +"</body>", "text/html", "UTF-8", "");
    }
}
