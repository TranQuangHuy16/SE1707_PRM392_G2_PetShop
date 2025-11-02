package com.example.se1707_prm392_g2_petshop.data.adapter;

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

import java.util.ArrayList;

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

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        User user = (userList.size() > position) ? userList.get(position) : null;

        // User info
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

        // Last message
        if (chat != null && chat.getMessages() != null && !chat.getMessages().isEmpty()) {
            Message lastMessage = chat.getMessages().get(chat.getMessages().size() - 1);
            holder.tvLastMessage.setText(lastMessage.getMessageText() != null ? lastMessage.getMessageText() : "Chưa có tin nhắn");
            holder.tvTime.setText(lastMessage.getSendAt() != null ? lastMessage.getSendAt() : "");
        } else {
            holder.tvLastMessage.setText("Chưa có tin nhắn");
            holder.tvTime.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(chat, user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvChatName, tvLastMessage, tvTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvChatName = itemView.findViewById(R.id.tvChatName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
