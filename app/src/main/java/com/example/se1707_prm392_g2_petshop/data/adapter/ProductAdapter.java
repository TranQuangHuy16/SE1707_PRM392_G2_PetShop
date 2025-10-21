package com.example.se1707_prm392_g2_petshop.data.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.databinding.ItemProductBinding;

public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private OnProductClickListener clickListener;

    public ProductAdapter(OnProductClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getProductId() == newItem.getProductId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getProductName().equals(newItem.getProductName())
                    && oldItem.getPrice() == newItem.getPrice();
        }
    };

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = getItem(position);
        holder.bind(currentProduct, clickListener);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product, OnProductClickListener listener) {
            binding.tvProductName.setText(product.getProductName());
            binding.tvProductPrice.setText(String.format("$ %.2f", product.getPrice()));

            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .into(binding.imgProduct);

            itemView.setOnClickListener(v -> listener.onProductClick(product));
        }
    }
}
