package com.enjoywater.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.enjoywater.R;
import com.enjoywater.entity.Product;
import com.enjoywater.fragment.main.ProductFragment;
import com.enjoywater.view.TvSegoeuiRegular;
import com.enjoywater.view.TvSegoeuiSb;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private ArrayList<Product> mListProduct;
    private ProductFragment.ItemClickListener mItemClickListener;
    private DecimalFormat formatter = new DecimalFormat("###,###,###");

    public ProductAdapter(Context context, ArrayList<Product> listProduct, ProductFragment.ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mListProduct = listProduct;
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
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
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.iv_selected)
        ImageView ivSelected;

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
                //Glide.with(mContext).load(product.getImage()).into(ivImage);
                if (product.isSelected()) {
                    ivSelected.setImageResource(R.drawable.ic_radio_button_active);
                } else {
                    ivSelected.setImageResource(R.drawable.ic_radio_button);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (product.isSelected()) {
                            product.setSelected(false);
                            mListProduct.get(position).setSelected(false);
                            mItemClickListener.productSelect(position, false);
                            ivSelected.setImageResource(R.drawable.ic_radio_button);
                        } else {
                            product.setSelected(true);
                            mListProduct.get(position).setSelected(true);
                            mItemClickListener.productSelect(position, true);
                            ivSelected.setImageResource(R.drawable.ic_radio_button_active);
                        }
                    }
                });
            }
        }
    }
}
