package com.mystique.android.duka.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mystique.android.duka.R;
import com.mystique.android.duka.activity.ProductDetailActivity;
import com.mystique.android.duka.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private Activity activity;
    private List<Product> productList;

    public ProductsAdapter(Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_products, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(activity)
                .load(productList.get(i).getLogo())
                .apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop())
                .transition(new DrawableTransitionOptions().crossFade())
                .thumbnail(0.1f)
                .into(viewHolder.imgProduct);

        viewHolder.tvTitle.setText(productList.get(i).getName());
        viewHolder.tvNumRatings.setText(String.valueOf(productList.get(i).getRating()));
        viewHolder.tvCurrentPrice.setText(String.valueOf(productList.get(i).getValue()));
        viewHolder.rbProduct.setRating(productList.get(i).getRating());

        final Product product = productList.get(i);

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailActivity.selectedProduct = product;
                activity.startActivity(new Intent(activity, ProductDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private RatingBar rbProduct;
        private TextView tvTitle, tvNumRatings, tvCurrentPrice, tvPreviousPrice;
        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvNumRatings = itemView.findViewById(R.id.tvNumRatings);
            tvCurrentPrice = itemView.findViewById(R.id.tvCurrentPrice);
            tvPreviousPrice = itemView.findViewById(R.id.tvPreviousPrice);
            rbProduct = itemView.findViewById(R.id.rbProduct);
        }
    }
}
