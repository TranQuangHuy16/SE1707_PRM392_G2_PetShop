package com.example.se1707_prm392_g2_petshop.data.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final Context context;
    private final int currentUserId;
    private List<Message> messages = new ArrayList<>();

    public ChatAdapter(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get(position);

        Log.d("DEBUG_CHAT", "Sender: " + message.getSenderId() + ", CurrentUser: " + currentUserId);

        holder.layoutMe.setVisibility(View.GONE);
        holder.layoutOther.setVisibility(View.GONE);

        if (message.getSenderId() == currentUserId) {
            holder.layoutMe.setVisibility(View.VISIBLE);
            holder.tvMyMessage.setText(message.getMessageText());
        } else {
            holder.layoutOther.setVisibility(View.VISIBLE);
            holder.tvOtherMessage.setText(message.getMessageText());
        }
    }


    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMe, layoutOther;
        TextView tvMyMessage, tvOtherMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutMe = itemView.findViewById(R.id.layoutMe);
            layoutOther = itemView.findViewById(R.id.layoutOther);
            tvMyMessage = itemView.findViewById(R.id.tvMyMessage);
            tvOtherMessage = itemView.findViewById(R.id.tvOtherMessage);
        }
    }
}
