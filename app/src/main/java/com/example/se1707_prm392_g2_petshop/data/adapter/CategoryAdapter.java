package com.example.se1707_prm392_g2_petshop.data.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.databinding.ItemCategoryBinding;

public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.CategoryViewHolder> {


    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    private final OnCategoryClickListener clickListener;
    public CategoryAdapter(OnCategoryClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    private static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getCategoryId() == newItem.getCategoryId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getCategoryName().equals(newItem.getCategoryName());
        }
    };

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentCategory = getItem(position);
        holder.bind(currentCategory, clickListener);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category category, OnCategoryClickListener listener) {
            binding.tvCategoryName.setText(category.getCategoryName());

            Glide.with(itemView.getContext())
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.ic_dog_bowl)
                    .error(R.drawable.ic_dog_bowl)
                    .into(binding.imgCategoryIcon);

            itemView.setOnClickListener(v -> listener.onCategoryClick(category));
        }

    }


}