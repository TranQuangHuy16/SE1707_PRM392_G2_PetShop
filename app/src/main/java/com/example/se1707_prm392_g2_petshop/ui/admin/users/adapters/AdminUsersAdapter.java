package com.example.se1707_prm392_g2_petshop.ui.admin.users.adapters;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.utils.DateTimeUtils;
import com.example.se1707_prm392_g2_petshop.ui.admin.users.list.AdminUsersContract;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class AdminUsersAdapter extends ListAdapter<UserDetailResponse, AdminUsersAdapter.UserViewHolder> {

    private final AdminUsersContract.Presenter mPresenter;

    public AdminUsersAdapter(AdminUsersContract.Presenter presenter) {
        super(DIFF_CALLBACK);
        this.mPresenter = presenter;
    }

    private static final DiffUtil.ItemCallback<UserDetailResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<UserDetailResponse>() {
                @Override
                public boolean areItemsTheSame(@NonNull UserDetailResponse oldItem, @NonNull UserDetailResponse newItem) {
                    return oldItem.getUserId() == newItem.getUserId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull UserDetailResponse oldItem, @NonNull UserDetailResponse newItem) {
                    return oldItem.isActive() == newItem.isActive()
                            && Objects.equals(oldItem.getFullName(), newItem.getFullName())
                            && Objects.equals(oldItem.getEmail(), newItem.getEmail())
                            && Objects.equals(oldItem.getRole(), newItem.getRole())
                            && Objects.equals(oldItem.getImgUrl(), newItem.getImgUrl())
                            && Objects.equals(oldItem.getCreatedAt(), newItem.getCreatedAt());
                }
            };

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_user, parent, false);
        return new UserViewHolder(itemView, mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // ================= ViewHolder =================
    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final AdminUsersContract.Presenter mPresenter;
        private final ImageView imgAvatar;
        private final TextView tvFullName, tvEmail, tvRole, tvCreatedAt;
        private final SwitchMaterial switchActive;

        public UserViewHolder(@NonNull View itemView, AdminUsersContract.Presenter presenter) {
            super(itemView);
            this.mPresenter = presenter;
            imgAvatar = itemView.findViewById(R.id.img_user_avatar);
            tvFullName = itemView.findViewById(R.id.tv_user_fullname);
            tvEmail = itemView.findViewById(R.id.tv_user_email);
            tvRole = itemView.findViewById(R.id.tv_user_role);
            tvCreatedAt = itemView.findViewById(R.id.tv_user_created_at);
            switchActive = itemView.findViewById(R.id.switch_user_active);
        }

        void bind(final UserDetailResponse user) {
            // Thông tin cơ bản
            tvFullName.setText(user.getFullName());
            tvEmail.setText(user.getEmail());
            tvRole.setText(user.getRole());
            tvCreatedAt.setText("Created: " + DateTimeUtils.formatDisplayDateTime(user.getCreatedAt()));

            // Avatar
            if (user.getImgUrl() != null && !user.getImgUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(user.getImgUrl())
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_profile);
            }

            // Trạng thái Active
            switchActive.setOnCheckedChangeListener(null); // Xóa listener cũ
            switchActive.setChecked(user.isActive());
            switchActive.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (mPresenter != null) {
                    // Disable switch trong lúc update để tránh click nhiều lần
                    switchActive.setEnabled(false);
                    
                    mPresenter.handleActiveToggled(user, isChecked);
                    
                    // Re-enable sau 1 giây (có thể điều chỉnh)
                    switchActive.postDelayed(() -> switchActive.setEnabled(true), 1000);
                }
            });

            // Click Role → mở menu chọn role
            tvRole.setOnClickListener(v -> showRolePopup(v, user));

            // Click toàn item → mở chi tiết user
            itemView.setOnClickListener(v -> {
                if (mPresenter != null) mPresenter.handleUserClicked(user.getUserId());
            });
        }

        private void showRolePopup(View view, UserDetailResponse user) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenu().add(Menu.NONE, 1, 1, "Admin");
            popupMenu.getMenu().add(Menu.NONE, 2, 2, "Customer");

            popupMenu.setOnMenuItemClickListener(item -> {
                String newRole = item.getTitle().toString();
                if (mPresenter != null) mPresenter.handleRoleChanged(user, newRole);
                tvRole.setText(newRole);
                return true;
            });

            popupMenu.show();
        }
    }
}
