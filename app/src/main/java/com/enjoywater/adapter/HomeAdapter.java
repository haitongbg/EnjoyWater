package com.enjoywater.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.enjoywater.R;
import com.enjoywater.entity.News;
import com.enjoywater.entity.Product;
import com.enjoywater.entity.UserLoginInfo;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSb;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER_INFO = 0;
    private static final int VIEW_TYPE_TOP_SALES = 1;
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
        } else if (o instanceof ArrayList) {
            return VIEW_TYPE_TOP_SALES;
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
            case VIEW_TYPE_TOP_SALES: {
                View view = layoutInflater.inflate(R.layout.item_home_top_sales, parent, false);
                return new TopSalesViewHolder(view);
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
        } else if (holder instanceof TopSalesViewHolder) {
            ((TopSalesViewHolder) holder).setData(position);
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

    public class TopSalesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_list_hot_sale)
        RecyclerView rvListHotSale;

        public TopSalesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(int position) {

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
            News news = (News) mListHome.get(position);
            if (news != null) {
                Glide.with(mContext).load(news.getImage()).into(ivBanner);
                tvTitle.setText(news.getName());
                tvDescription.setText(news.getSummary());
            }
        }
    }
}
