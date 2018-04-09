package com.enjoywater.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.enjoywater.R;
import com.enjoywater.activity.NewsDetailActivity;
import com.enjoywater.entity.News;
import com.enjoywater.entity.UserLoginInfo;
import com.enjoywater.service.ApiConstant;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSb;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER_INFO = 0;
    private static final int VIEW_TYPE_NEWS = 2;
    private Context mContext;
    private ArrayList<Object> mListHome;

    public HomeAdapter(Context context, ArrayList<Object> listHome) {
        this.mContext = context;
        this.mListHome = listHome;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = mListHome.get(position);
        if (o instanceof UserLoginInfo) {
            return VIEW_TYPE_USER_INFO;
        } else {
            return VIEW_TYPE_NEWS;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case VIEW_TYPE_USER_INFO: {
                View view = layoutInflater.inflate(R.layout.item_home_user, parent, false);
                return new UserInfoViewHolder(view);
            }
            default: {
                View view = layoutInflater.inflate(R.layout.item_home_news, parent, false);
                return new NewsViewHolder(view);
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserInfoViewHolder) {
            ((UserInfoViewHolder) holder).setData(position);
        } else if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return this.mListHome.size();
    }

    public class UserInfoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_fullname)
        TvSegoeuiSb tvFullname;
        @BindView(R.id.tv_diemtichluy)
        TvSegoeuiRegular tvDiemtichluy;
        @BindView(R.id.tv_point)
        TvSegoeuiSb tvPoint;
        @BindView(R.id.layout_info)
        RelativeLayout layoutInfo;
        @BindView(R.id.iv_view_detail_user)
        ImageView ivViewDetailUser;

        public UserInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(int position) {
            UserLoginInfo userLoginInfo = (UserLoginInfo) mListHome.get(position);
            if (userLoginInfo != null) {
                //avatar

                //name
                String name = userLoginInfo.getFulname();
                if (name != null && !name.isEmpty()) {
                    tvFullname.setText(name);
                } else {
                    tvFullname.setText(R.string.default_user_fullname);
                }
                // point

                // edit
                ivViewDetailUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_banner)
        ImageView ivBanner;
        @BindView(R.id.tv_title)
        TvSegoeuiSb tvTitle;
        @BindView(R.id.tv_description)
        TvSegoeuiRegular tvDescription;
        @BindView(R.id.btn_view_detail)
        Button btnViewDetail;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(int position) {
            final News news = (News) mListHome.get(position);
            if (news != null) {
                Glide.with(mContext).load(news.getImage()).into(ivBanner);
                String title = news.getName();
                if (title != null && !title.isEmpty()) {
                    tvTitle.setText(news.getName());
                    tvTitle.setOnLayoutListener(new TvSegoeuiSb.OnLayoutListener() {
                        @Override
                        public void onLayouted(AppCompatTextView view) {
                            if (view.getLineCount() > 2) {
                                int lineEndIndex = tvTitle.getLayout().getLineEnd(1);
                                String text = tvTitle.getText().subSequence(0, lineEndIndex - 3) + "... ";
                                tvTitle.setText(text);
                            }
                        }
                    });
                    tvTitle.setVisibility(View.VISIBLE);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
                String description = news.getSummary();
                if (description != null && !description.isEmpty()) {
                    tvDescription.setText(news.getSummary());
                    tvDescription.setOnLayoutListener(new TvSegoeuiSb.OnLayoutListener() {
                        @Override
                        public void onLayouted(AppCompatTextView view) {
                            if (view.getLineCount() > 3) {
                                int lineEndIndex = tvDescription.getLayout().getLineEnd(2);
                                String text = tvDescription.getText().subSequence(0, lineEndIndex - 3) + "... ";
                                tvDescription.setText(text);
                            }
                        }
                    });
                    tvDescription.setVisibility(View.VISIBLE);
                } else {
                    tvDescription.setVisibility(View.GONE);
                }
                btnViewDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewsDetailActivity.class);
                        intent.putExtra(ApiConstant.Param.ID, news.getId());
                        mContext.startActivity(intent);
                        ((Activity)mContext).overridePendingTransition(R.anim.slide_right_to_left_in, R.anim.slide_right_to_left_out);
                    }
                });
            }
        }


    }
}
