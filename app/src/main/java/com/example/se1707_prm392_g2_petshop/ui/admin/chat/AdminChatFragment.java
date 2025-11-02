package com.example.se1707_prm392_g2_petshop.ui.admin.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    int currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChat = view.findViewById(R.id.rvAdminChat);
        adapter = new AdminChatAdapter();
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setAdapter(adapter);

        adapter.setOnItemClickListener((chat, user) -> {
            if (chat != null) {
                int customerId = chat.getCustomerId();
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
        }
    }


    @Override
    public void setPresenter(AdminChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showChatList(ArrayList<Chat> chats, ArrayList<User> users) {
        adapter.setChatList(chats, users);
    }
}
