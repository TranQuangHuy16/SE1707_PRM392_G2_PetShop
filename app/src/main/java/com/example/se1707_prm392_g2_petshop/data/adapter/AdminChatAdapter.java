package com.example.se1707_prm392_g2_petshop.data.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.Message;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.Locale;

public class AdminChatAdapter extends RecyclerView.Adapter<AdminChatAdapter.ChatViewHolder> {

    private final ArrayList<Chat> chatList = new ArrayList<>();
    private final ArrayList<User> userList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Chat chat, User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setChatList(ArrayList<Chat> chats, ArrayList<User> users) {
        chatList.clear();
        userList.clear();
        if (chats != null) chatList.addAll(chats);
        if (users != null) userList.addAll(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        User user = (userList.size() > position) ? userList.get(position) : null;

        if (user != null) {
            holder.tvChatName.setText(user.getFullName() != null ? user.getFullName() : "Unknown");
            String avatarUrl = user.getImgAvatarl();
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_profile)
                        .circleCrop()
                        .into(holder.ivAvatar);
            } else {
                holder.ivAvatar.setImageResource(R.drawable.ic_profile);
            }
        } else {
            holder.tvChatName.setText("Unknown");
            holder.ivAvatar.setImageResource(R.drawable.ic_profile);
        }

        // Tin nhắn cuối
        if (chat != null && chat.getMessages() != null && !chat.getMessages().isEmpty()) {
            Message lastMessage = chat.getMessages().get(chat.getMessages().size() - 1);
            holder.tvLastMessage.setText(lastMessage.getMessageText() != null ? lastMessage.getMessageText() : "Chưa có tin nhắn");

            String relativeTime = getRelativeTime(lastMessage.getSendAt());
            holder.tvTime.setText(relativeTime);
        } else {
            holder.tvLastMessage.setText("Chưa có tin nhắn");
            holder.tvTime.setText("");
        }

        // Hiển thị chấm xanh nếu có tin nhắn chưa đọc
        holder.viewUnreadDot.setVisibility(chat.isHasUnread() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(chat, user);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvChatName, tvLastMessage, tvTime;
        View viewUnreadDot;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvChatName = itemView.findViewById(R.id.tvChatName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            viewUnreadDot = itemView.findViewById(R.id.viewUnreadDot);
        }
    }

    /**
     * Chuyển đổi thời gian ISO sang dạng "x phút trước", "x giờ trước", "Hôm qua", "x ngày trước".
     */
    private String getRelativeTime(String sendAt) {
        if (sendAt == null || sendAt.isEmpty()) return "";

        try {
            // Parse ISO 8601 time (ví dụ: "2025-11-04T13:45:12.123")
            LocalDateTime messageTime = LocalDateTime.parse(sendAt, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(messageTime, now);

            long seconds = duration.getSeconds();
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds < 60) {
                return "Vừa xong";
            } else if (minutes < 60) {
                return minutes + " phút trước";
            } else if (hours < 24) {
                return hours + " giờ trước";
            } else if (days == 1) {
                return "Hôm qua";
            } else if (days < 7) {
                return days + " ngày trước";
            } else {
                // Hiển thị ngày/tháng khi quá 1 tuần
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                return messageTime.format(formatter);
            }

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
