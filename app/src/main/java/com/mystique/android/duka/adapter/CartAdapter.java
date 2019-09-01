package com.mystique.android.duka.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mystique.android.duka.R;
import com.mystique.android.duka.model.Product;

import java.util.ArrayList;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<Product> cartItems;
    private List<Integer> checkedPosition = new ArrayList<>();
    private List<Product> checkedPositionProducts = new ArrayList<>();
    private TextView tvTotal;
    private float total;

    public CartAdapter(Context context, List<Product> cartItems, TextView tvTotal) {
        this.context = context;
        this.cartItems = cartItems;
        this.tvTotal = tvTotal;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_cart, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        final int quantity = Integer.parseInt(holder.tvProductQty.getText().toString());
        final int[] total = new int[cartItems.size()];

        holder.setIsRecyclable(false);
        Glide.with(context)
                .load(cartItems.get(position).getLogo())
                .into(holder.imgProduct);

        holder.tvPrice.setText(String.valueOf("Ksh " + cartItems.get(position).getValue()));
        holder.tvProduct.setText(cartItems.get(position).getName());

        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.tvProductQty.setText(String.valueOf(Integer.parseInt(holder.tvProductQty.getText().toString()) + 1));
            }
        });

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(holder.tvProductQty.getText().toString()) == 0) {
                    holder.imgRemove.setEnabled(false);
                } else {
                    holder.tvProductQty.setText(String.valueOf(Integer.parseInt(holder.tvProductQty.getText().toString()) - 1));
                }
            }
        });

        holder.chckbxCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    total[position] = quantity * cartItems.get(position).getValue();
                } else {
                    total[position] = 0 * cartItems.get(position).getValue();
                }

                int totals = 0;
                for( int i : total) {
                    totals += i;
                }
                tvTotal.setText("Ksh ".concat(String.valueOf(totals)));

            }
        });

    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox chckbxCart;
        private ImageView imgProduct;
        private TextView tvPrice, tvProduct, tvProductQty;
        private ImageView imgAdd, imgRemove;


        public CartViewHolder(View itemView) {
            super(itemView);
            chckbxCart = itemView.findViewById(R.id.chckbxCart);
            imgProduct = itemView.findViewById(R.id.imgCart);
            tvPrice = itemView.findViewById(R.id.tvCost);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            imgRemove = itemView.findViewById(R.id.imgRemove);
            tvProductQty = itemView.findViewById(R.id.tvProductQty);
        }
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

}
