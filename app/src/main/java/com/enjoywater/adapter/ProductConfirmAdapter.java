package com.enjoywater.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enjoywater.R;
import com.enjoywater.entity.Product;
import com.enjoywater.view.DialogConfirmProduct;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSb;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductConfirmAdapter extends RecyclerView.Adapter<ProductConfirmAdapter.ProductViewHolder> {
    private Context mContext;
    private ArrayList<Product> mListProduct;
    private DialogConfirmProduct.ItemClickListener mItemClickListener;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    public ProductConfirmAdapter(Context context, ArrayList<Product> listProduct, DialogConfirmProduct.ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mListProduct = listProduct;
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_product_confirm, parent, false);
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
        @BindView(R.id.tv_price)
        TvSegoeuiSb tvPrice;
        @BindView(R.id.btn_subtract)
        ImageView btnSubtract;
        @BindView(R.id.tv_count)
        TvSegoeuiSb tvCount;
        @BindView(R.id.btn_add)
        ImageView btnAdd;

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
                int price = product.getPrice();
                tvPrice.setText(formatter.format(price) + " đ");
                int count = product.getCount();
                tvCount.setText(count + "");
                if (count <= 1) btnSubtract.setImageResource(R.drawable.ic_subtract);
                else btnSubtract.setImageResource(R.drawable.ic_subtract_active);
                if (count >= 999) btnAdd.setImageResource(R.drawable.ic_add);
                else btnAdd.setImageResource(R.drawable.ic_add_active);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = product.getCount();
                        if (count < 999) {
                            count++;
                            product.setCount(count);
                            tvCount.setText(count + "");
                            mListProduct.get(position).setCount(count);
                            mItemClickListener.changeProductCount(position, count);
                            if (count == 2) btnSubtract.setImageResource(R.drawable.ic_subtract_active);
                            if (count == 999) btnAdd.setImageResource(R.drawable.ic_add);
                        }
                    }
                });
                btnSubtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = product.getCount();
                        if (count > 1) {
                            count--;
                            product.setCount(count);
                            tvCount.setText(count + "");
                            mListProduct.get(position).setCount(count);
                            mItemClickListener.changeProductCount(position, count);
                            if (count == 1) btnSubtract.setImageResource(R.drawable.ic_subtract);
                            if (count == 998) btnAdd.setImageResource(R.drawable.ic_add_active);
                        }
                    }
                });
            }
        }
    }
}
