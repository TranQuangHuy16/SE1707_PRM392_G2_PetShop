package com.example.se1707_prm392_g2_petshop.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.ChatAdapter;
import com.example.se1707_prm392_g2_petshop.data.api.ChatApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.SendMessageRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.Message;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.ChatRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.ChatUtils;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.ui.user.main.UserMainActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {
    public static boolean isVisible = false;
    private ChatPresenter presenter;
    private ChatAdapter adapter;
    private RecyclerView recyclerChat;
    private EditText edtMessage;
    private ImageButton btnSend;
    private ImageView btnBack, imgAvatar;
    private TextView tvName;

    private int currentUserId;
    private int targetUserId;
    private boolean isAdmin = false;
    private Chat currentChat;

    private final android.os.Handler handler = new android.os.Handler();
    private final int REFRESH_INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        int chatRoomId = getIntent().getIntExtra("CHAT_ROOM_ID", 0);
        int lastMessageId = getIntent().getIntExtra("LAST_MSG_ID", 0);

        if (chatRoomId != 0 && lastMessageId != 0) {
            ChatUtils.markAsRead(this, chatRoomId, lastMessageId);
        }

        setupPresenter();
        setupUI();

        String id = JwtUtil.getSubFromToken(this);
        if (id != null) currentUserId = Integer.parseInt(id);

        targetUserId = getIntent().getIntExtra("customerId", -1);

        int meId = currentUserId;
        adapter = new ChatAdapter(this, meId);
        recyclerChat.setAdapter(adapter);

        if (targetUserId != -1) {
            isAdmin = true;
            presenter.getRoomByCustomerId(targetUserId);
        } else {
            presenter.getRoomByCustomerId(currentUserId);
        }
    }

    private void setupUI() {
        recyclerChat = findViewById(R.id.recyclerChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvName);

        adapter = new ChatAdapter(this, currentUserId);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        btnBack.setOnClickListener(view -> {
            if (isAdmin) {
                finish();
            } else {
                startActivity(new Intent(ChatActivity.this, UserMainActivity.class));
                finish();
            }
        });

        btnSend.setOnClickListener(view -> sendMessage());
    }

    private void setupPresenter() {
        ChatApi chatApi = RetrofitClient.getChatApi(this);
        ChatRepository repository = new ChatRepository(chatApi);
        presenter = new ChatPresenter(this, repository);
    }

    private void sendMessage() {
        String text = edtMessage.getText().toString().trim();
        if (text.isEmpty() || currentChat == null) return;

        SendMessageRequest request = new SendMessageRequest(
                currentChat.getChatRoomId(),
                currentUserId,
                text
        );

        presenter.sendMessage(request);

        // tạo message local tạm
        Message newMessage = new Message();
        newMessage.setChatRoomId(currentChat.getChatRoomId());
        newMessage.setSenderId(currentUserId);
        newMessage.setMessageText(text);
        newMessage.setMessageId((int) System.currentTimeMillis()); // tránh trùng id null

        adapter.addMessage(newMessage);
        recyclerChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        edtMessage.setText("");

        markChatAsRead(); // gửi thì đánh dấu đã đọc luôn
    }

    @Override
    public void onGetRoomByCustomerIdSuccess(Chat chat) {
        currentChat = chat;

        List<Message> messages = chat.getMessages() != null ? chat.getMessages() : new ArrayList<>();
        adapter.setMessages(messages);
        recyclerChat.scrollToPosition(messages.size() - 1);

        markChatAsRead();

        int otherId = isAdmin ? chat.getCustomerId() : chat.getAdminId();
        presenter.getUserById(otherId);
    }

    @Override
    public void onGetRoomByCustomerIdError(String message) {
        Log.d("ChatActivity", "Room error: " + message);
    }

    @Override
    public void onSendMessageSuccess(String message) {}
    @Override
    public void onSendMessageError(String message) {}
    @Override
    public void onGetUserByIdError(String message) {
        Log.d("ChatActivity", "User error: " + message);
    }
    @Override
    public void onFailure(String message) {
        Log.d("ChatActivity", "Failure: " + message);
    }

    @Override
    public void onGetUserByIdSuccess(User user) {
        tvName.setText(user.getFullName());
        Glide.with(this)
                .load(user.getImgAvatarl())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(imgAvatar);
    }

    private void startMessageRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentChat != null) {
                    presenter.getRoomByCustomerId(isAdmin ? targetUserId : currentUserId);
                }
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        startMessageRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
        saveLastMessageId();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isVisible = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void markChatAsRead() {
        if (currentChat != null && currentChat.getMessages() != null && !currentChat.getMessages().isEmpty()) {
            Message lastMessage = currentChat.getMessages().get(currentChat.getMessages().size() - 1);
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("chat_prefs", Context.MODE_PRIVATE);
            Log.d("ChatActivity", "Saving messageId = " + lastMessage.getMessageId());
            preferences.edit()
                    .putString(String.valueOf(currentChat.getChatRoomId()), String.valueOf(lastMessage.getMessageId()))
                    .commit(); // commit đồng bộ
        }
    }

    private void saveLastMessageId() {
        if (currentChat != null && currentChat.getMessages() != null && !currentChat.getMessages().isEmpty()) {
            Message lastMessage = currentChat.getMessages().get(currentChat.getMessages().size() - 1);
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("chat_prefs", Context.MODE_PRIVATE);
            Log.d("ChatActivity", "Save onPause messageId = " + lastMessage.getMessageId());
            preferences.edit()
                    .putString(String.valueOf(currentChat.getChatRoomId()), String.valueOf(lastMessage.getMessageId()))
                    .commit();
        }
    }
}
