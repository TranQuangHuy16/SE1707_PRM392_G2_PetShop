package com.example.se1707_prm392_g2_petshop.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.CartItem;

public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartViewHolder> {

    public interface OnCartItemClickListener {
        void onIncreaseClick(CartItem cartItem);
        void onDecreaseClick(CartItem cartItem);
        void onRemoveClick(CartItem cartItem);
    }

    private OnCartItemClickListener clickListener;
    private boolean isReadOnly = false;

    public CartAdapter(OnCartItemClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    public CartAdapter(OnCartItemClickListener clickListener, boolean isReadOnly) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.isReadOnly = isReadOnly;
    }

    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<CartItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getCartItemId() == newItem.getCartItemId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getQuantity() == newItem.getQuantity() &&
                   oldItem.getTotalPrice() == newItem.getTotalPrice();
        }
    };

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isReadOnly ? R.layout.item_cart_readonly : R.layout.item_cart;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new CartViewHolder(view, isReadOnly);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = getItem(position);
        holder.bind(cartItem, clickListener);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvQuantity;
        private TextView tvTotal;
        private ImageButton btnIncrease;
        private ImageButton btnDecrease;
        private ImageButton btnRemove;
        private boolean isReadOnly;

        public CartViewHolder(@NonNull View itemView, boolean isReadOnly) {
            super(itemView);
            this.isReadOnly = isReadOnly;
            imgProduct = itemView.findViewById(R.id.img_cart_product);
            tvProductName = itemView.findViewById(R.id.tv_cart_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_cart_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvTotal = itemView.findViewById(R.id.tv_cart_item_total);
            
            // Only find buttons if not readonly
            if (!isReadOnly) {
                btnIncrease = itemView.findViewById(R.id.btn_increase);
                btnDecrease = itemView.findViewById(R.id.btn_decrease);
                btnRemove = itemView.findViewById(R.id.btn_remove);
            }
        }

        public void bind(CartItem cartItem, OnCartItemClickListener listener) {
            tvProductName.setText(cartItem.getProductName());
            tvProductPrice.setText(String.format("$ %.2f", cartItem.getProductPrice()));
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
            tvTotal.setText(String.format("$ %.2f", cartItem.getTotalPrice()));

            Glide.with(itemView.getContext())
                    .load(cartItem.getProductImageUrl())
                    .placeholder(R.drawable.product_img)
                    .into(imgProduct);

            // Only set click listeners if not readonly
            if (!isReadOnly && listener != null) {
                btnIncrease.setOnClickListener(v -> listener.onIncreaseClick(cartItem));
                btnDecrease.setOnClickListener(v -> listener.onDecreaseClick(cartItem));
                btnRemove.setOnClickListener(v -> listener.onRemoveClick(cartItem));
            }
        }
    }
}
