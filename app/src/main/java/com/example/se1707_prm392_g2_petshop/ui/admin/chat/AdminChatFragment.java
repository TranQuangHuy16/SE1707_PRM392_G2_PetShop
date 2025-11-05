package com.example.se1707_prm392_g2_petshop.ui.admin.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;

import com.example.se1707_prm392_g2_petshop.data.adapter.AdminChatAdapter;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.ChatRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.ui.chat.ChatActivity;

import java.util.ArrayList;
public class AdminChatFragment extends Fragment implements AdminChatContract.View {

    private AdminChatContract.Presenter mPresenter;
    private RecyclerView rvChat;
    private AdminChatAdapter adapter;
    private int currentUserId;
    private SharedPreferences preferences;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPresenter != null && currentUserId != 0) {
                mPresenter.start(currentUserId);
            }
            handler.postDelayed(this, 5000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = requireActivity().getApplicationContext().getSharedPreferences("chat_prefs", Context.MODE_PRIVATE);

        rvChat = view.findViewById(R.id.rvAdminChat);
        adapter = new AdminChatAdapter();
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setAdapter(adapter);

        adapter.setOnItemClickListener((chat, user) -> {
            if (chat != null) {
                int customerId = chat.getCustomerId();

                // 🔹 Khi click vào → đánh dấu đã đọc
                SharedPreferences.Editor editor = preferences.edit();
                String chatId = String.valueOf(chat.getChatRoomId());
                String lastMessageId = chat.getMessages() != null && !chat.getMessages().isEmpty()
                        ? String.valueOf(chat.getMessages().get(chat.getMessages().size() - 1).getMessageId())
                        : "";
                editor.putString(chatId, lastMessageId);
                editor.apply();

                chat.setHasUnread(false);
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(requireContext(), ChatActivity.class);
                intent.putExtra("customerId", customerId);
                startActivity(intent);
            }
        });

        ChatRepository repository = new ChatRepository(RetrofitClient.getChatApi(requireContext()));
        mPresenter = new AdminChatPresenter(this, repository);

        String id = JwtUtil.getSubFromToken(requireContext());
        if (id != null) {
            currentUserId = Integer.parseInt(id);
            mPresenter.start(currentUserId);
            handler.postDelayed(refreshRunnable, 5000);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(refreshRunnable);
    }

    @Override
    public void setPresenter(AdminChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showChatList(ArrayList<Chat> chats, ArrayList<User> users) {
        if (chats == null || users == null) return;

        ArrayList<android.util.Pair<Chat, User>> combinedList = new ArrayList<>();
        for (int i = 0; i < chats.size(); i++) {
            User user = (i < users.size()) ? users.get(i) : null;
            combinedList.add(new android.util.Pair<>(chats.get(i), user));
        }

        // 🔹 Đánh dấu tin chưa đọc
        for (android.util.Pair<Chat, User> pair : combinedList) {
            Chat chat = pair.first;
            if (chat.getMessages() != null && !chat.getMessages().isEmpty()) {
                String lastMessageId = String.valueOf(chat.getMessages()
                        .get(chat.getMessages().size() - 1).getMessageId());
                String savedId = preferences.getString(String.valueOf(chat.getChatRoomId()), "");

                chat.setHasUnread(!lastMessageId.equals(savedId));
            }
        }

        // 🔹 Sắp xếp theo thời gian tin nhắn cuối (mới nhất lên đầu)
        combinedList.sort((p1, p2) -> {
            Chat c1 = p1.first;
            Chat c2 = p2.first;

            if (c1.getMessages() == null || c1.getMessages().isEmpty()) return 1;
            if (c2.getMessages() == null || c2.getMessages().isEmpty()) return -1;

            // Lấy timestamp tin cuối cùng
            String t1 = c1.getMessages().get(c1.getMessages().size() - 1).getSendAt();
            String t2 = c2.getMessages().get(c2.getMessages().size() - 1).getSendAt();

            // So sánh theo thời gian ISO
            return t2.compareTo(t1); // giảm dần (mới nhất trước)
        });

        // 🔹 Chuẩn bị danh sách đã sắp xếp
        ArrayList<Chat> sortedChats = new ArrayList<>();
        ArrayList<User> sortedUsers = new ArrayList<>();
        for (android.util.Pair<Chat, User> pair : combinedList) {
            sortedChats.add(pair.first);
            sortedUsers.add(pair.second);
        }

        // 🔹 Gửi sang adapter
        adapter.setChatList(sortedChats, sortedUsers);
    }

}
