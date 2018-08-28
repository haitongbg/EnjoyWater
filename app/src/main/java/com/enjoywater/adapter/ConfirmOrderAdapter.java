package com.enjoywater.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enjoywater.R;
import com.enjoywater.entity.Product;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSb;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderAdapter.ProductViewHolder> {
    private Context mContext;
    private ArrayList<Product> mListProduct;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    public ConfirmOrderAdapter(Context context, ArrayList<Product> listProduct) {
        this.mContext = context;
        this.mListProduct = listProduct;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_confirm_order, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return this.mListProduct.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TvSegoeuiRegular tvName;
        @BindView(R.id.tv_count)
        TvSegoeuiSb tvCount;
        @BindView(R.id.tv_total_price)
        TvSegoeuiSb tvTotalPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final int position) {
            final Product product = mListProduct.get(position);
            if (product != null) {
                String name = product.getName();
                if (name != null && !name.isEmpty()) {
                    tvName.setText(name);
                } else {
                    tvName.setText(R.string.product_name_default);
                }
                int count = product.getCount();
                tvCount.setText("x" + count);
                int price = product.getPrice();
                tvTotalPrice.setText(formatter.format(count * price) + " đ");
            }
        }
    }
}
