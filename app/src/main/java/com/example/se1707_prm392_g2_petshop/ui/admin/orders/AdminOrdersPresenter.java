package com.example.se1707_prm392_g2_petshop.ui.admin.orders;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.se1707_prm392_g2_petshop.data.models.Order;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;

import java.util.List;

public class AdminOrdersPresenter implements AdminOrdersContract.Presenter {

    private final AdminOrdersContract.View mView;
    private final OrderRepository mOrderRepository;
    private final LifecycleOwner mLifecycleOwner;

    // Lưu trạng thái lọc đơn hàng (ví dụ: "pending", "completed", "canceled")
    private String currentStatusFilter = null;

    public AdminOrdersPresenter(AdminOrdersContract.View view, OrderRepository orderRepository) {
        this.mView = view;
        this.mOrderRepository = orderRepository;
        this.mLifecycleOwner = (LifecycleOwner) view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadAllOrders(); // tải tất cả đơn hàng ban đầu
    }

    @Override
    public void loadAllOrders() {
        if (mView == null) return;

        mView.showLoading();

        LiveData<List<Order>> liveData = mOrderRepository.getAllOrders(currentStatusFilter);

        liveData.observe(mLifecycleOwner, orders -> {
            mView.hideLoading();

            if (orders == null) {
                mView.showError("Không thể tải danh sách đơn hàng.");
                return;
            }

            if (orders.isEmpty()) {
                mView.showError("Không có đơn hàng nào để hiển thị.");
                return;
            }

            mView.showOrders(orders);
        });
    }

    @Override
    public void onFilterSelected(String status) {
        // null hoặc "Tất cả" => tải toàn bộ đơn hàng
        currentStatusFilter = (status == null || status.equalsIgnoreCase("Tất cả")) ? null : status;
        loadAllOrders();
    }

    @Override
    public void onCancelOrderClicked(int orderId) {
        if (mView == null) return;

        mView.showLoading();

        mOrderRepository.cancelOrder(orderId).observe(mLifecycleOwner, success -> {
            mView.hideLoading();
            if (success != null && success) {
                mView.showSuccessMessage("Đã hủy đơn hàng thành công.");
                loadAllOrders();
            } else {
                mView.showError("Hủy đơn hàng thất bại.");
            }
        });
    }

    @Override
    public void onOrderClicked(Order order) {
        // Không cần mở chi tiết, chỉ hiển thị thông báo hoặc làm gì đó khác
        mView.showSuccessMessage("Bạn đã chọn đơn hàng #" + order.getOrderId());
    }

    @Override
    public void onUpdateStatusClicked(Order order, String newStatus) {
        if (mView == null) return;

        mView.showLoading();

        mOrderRepository.updateOrderStatus(order.getOrderId(), newStatus)
                .observe(mLifecycleOwner, success -> {
                    mView.hideLoading();
                    if (success != null && success) {
                        mView.showSuccessMessage("Cập nhật trạng thái thành công.");
                        loadAllOrders(); // reload danh sách
                    } else {
                        mView.showError("Cập nhật trạng thái thất bại.");
                    }
                });
    }

}
