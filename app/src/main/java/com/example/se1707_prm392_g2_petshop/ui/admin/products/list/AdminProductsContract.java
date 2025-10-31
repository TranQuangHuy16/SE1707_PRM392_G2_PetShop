package com.example.se1707_prm392_g2_petshop.ui.admin.products.list;

import android.widget.CompoundButton;

import androidx.lifecycle.LiveData;

import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import java.util.List;

public interface AdminProductsContract {
    interface View {
        /**
         * Hiển thị danh sách sản phẩm đã được Presenter phân loại.
         * @param activeProducts Danh sách sản phẩm đang hoạt động.
         * @param inactiveProducts Danh sách sản phẩm không hoạt động.
         */
        void displayProducts(List<Product> activeProducts, List<Product> inactiveProducts);

        /**
         * Hiển thị danh sách các danh mục lên Spinner.
         * @param categories Danh sách các danh mục.
         */
        void displayCategories(List<Category> categories);

        /**
         * Hiển thị chỉ báo đang tải dữ liệu (ví dụ: ProgressBar).
         */
        void showLoading();

        /**
         * Ẩn chỉ báo đang tải.
         */
        void hideLoading();

        /**
         * Hiển thị một thông báo lỗi chung.
         * @param message Nội dung lỗi.
         */
        void showErrorMessage(String message);

        /**
         * Hiển thị thông báo khi có hành động thành công (ví dụ: Xóa, Cập nhật).
         * @param message Nội dung thông báo.
         */
        void showSuccessMessage(String message);

//        /**
//         * Tải lại toàn bộ danh sách sản phẩm, thường được gọi sau khi có thay đổi dữ liệu.
//         */
//        void refreshProductList();

        /**
         * Điều hướng đến màn hình tạo mới hoặc chỉnh sửa sản phẩm.
         * @param productId ID của sản phẩm để chỉnh sửa, hoặc một giá trị đặc biệt (ví dụ: -1) cho việc tạo mới.
         */
        void navigateToManageScreen(int productId);
    }

    interface Presenter{
        /**
         * Yêu cầu Presenter bắt đầu tải tất cả dữ liệu cần thiết cho màn hình (sản phẩm và danh mục).
         */
        void start();

        /**
         * Được gọi khi người dùng chọn một danh mục từ Spinner.
         * @param categoryId ID của danh mục được chọn, có thể là null hoặc -1 cho "Tất cả".
         */
        void onCategorySelected(Integer categoryId);

        /**
         * Được gọi khi người dùng nhấn nút "Thêm sản phẩm mới".
         */
        void onAddProductClicked();

        /**
         * Được gọi khi người dùng nhấn vào một item sản phẩm (không phải các nút hành động).
         * @param product Sản phẩm được nhấn.
         */
        void onProductClicked(Product product);

        /**
         * Được gọi khi người dùng thay đổi trạng thái của công tắc active/inactive.
         * @param productId ID của sản phẩm.
         * @param isActive Trạng thái mới của công tắc.
         * @param buttonView View của công tắc để có thể tương tác lại (ví dụ: bật/tắt lại nếu API lỗi).
         */
        void onSwitchStateChanged(int productId, boolean isActive, CompoundButton buttonView);

        /**
         * Được gọi khi người dùng nhấn nút "Xóa" trên menu của một sản phẩm.
         * @param product Sản phẩm cần xóa.
         */
        void onDeleteProductClicked(Product product);
    }
}
