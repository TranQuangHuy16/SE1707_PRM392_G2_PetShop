package com.example.se1707_prm392_g2_petshop.ui.admin.products.list;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.google.android.material.chip.Chip;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class AdminProductAdapter extends ListAdapter<Product, AdminProductAdapter.ProductViewHolder> {
    private final AdminProductsContract.Presenter mPresenter;

    // 3. Constructor mới
    public AdminProductAdapter(AdminProductsContract.Presenter presenter) {
        // 4. Truyền vào DiffUtil.ItemCallback
        super(DIFF_CALLBACK);
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_admin_product, parent, false);
        return new ProductViewHolder(view, mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = getItem(position);
        holder.bind(product);
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            // So sánh ID để biết có cùng là một item không
            return oldItem.getProductId() == newItem.getProductId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            // So sánh theo giá trị logic, tránh cập nhật dư thừa
            return Objects.equals(oldItem.getProductName(), newItem.getProductName())
                    && oldItem.getPrice() == newItem.getPrice()
                    && oldItem.getStock() == newItem.getStock()
                    && oldItem.isActive() == newItem.isActive()
                    && Objects.equals(oldItem.getImageUrl(), newItem.getImageUrl())
                    && Objects.equals(oldItem.getCategoryName(), newItem.getCategoryName());
        }
    };
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final AdminProductsContract.Presenter mPresenter;
        private ImageView ivProduct;
        private TextView tvProductName, tvProductPrice, tvProductStock;
        private SwitchMaterial switchIsActive;
        private Chip chipCategory;
        private ImageButton btnOptions;

        public ProductViewHolder(@NonNull View itemView, AdminProductsContract.Presenter presenter) {
            super(itemView);
            this.mPresenter = presenter;
            ivProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductStock = itemView.findViewById(R.id.tv_product_stock);
            switchIsActive = itemView.findViewById(R.id.switch_is_active);
            chipCategory = itemView.findViewById(R.id.chip_product_category);
            btnOptions = itemView.findViewById(R.id.btn_product_options);
        }

        void bind(final Product product) {
            tvProductName.setText(product.getProductName());
            tvProductPrice.setText(String.format("%,.0fđ", product.getPrice()));
            tvProductStock.setText("Stock: " + product.getStock());
            chipCategory.setText(product.getCategoryName());

            if (product.getImageUrl()!= null && !product.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_pet_collar) // Ảnh mặc định
                        .error(R.drawable.product_img) // Ảnh khi có lỗi
                        .into(ivProduct);
            } else {
                ivProduct.setImageResource(R.drawable.product_img);
            }

            // Set up click listeners
            setListener(product);
        }

        private void setListener(Product product){
            if (mPresenter == null) return;
            itemView.setOnClickListener(v -> mPresenter.onProductClicked(product));
            btnOptions.setOnClickListener(v -> showPopupMenu(v, product));

            // Xóa listener cũ đi để tránh gọi nhiều lần khi bind lại
            switchIsActive.setOnCheckedChangeListener(null);
            // Cập nhật trạng thái switch mà không kích hoạt listener
            switchIsActive.setChecked(product.isActive());
            switchIsActive.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Ngay lập tức vô hiệu hóa switch để tránh người dùng nhấn liên tục
                if (product.isActive() != isChecked) {
                    buttonView.setEnabled(false); // tránh spam
                    mPresenter.onSwitchStateChanged(product.getProductId(), isChecked, buttonView);
                }
            });
        }

        private void showPopupMenu(View view, final Product product) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenu().add(Menu.NONE, 1, 1, "Edit");
            popupMenu.getMenu().add(Menu.NONE, 2, 2, "Delete");

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == 1) { // Edit
                    mPresenter.onProductClicked(product);
                    return true;
                } else if (id == 2) { // Delete
                    mPresenter.onDeleteProductClicked(product);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }
    }
}
