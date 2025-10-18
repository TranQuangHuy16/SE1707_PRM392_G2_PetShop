package com.example.se1707_prm392_g2_petshop.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.ChatAdapter;
import com.example.se1707_prm392_g2_petshop.data.api.ChatApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.SendMessageRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.Message;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.ChatRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.ui.user.main.UserMainActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {

    private ChatPresenter presenter;
    private ChatAdapter adapter;
    private RecyclerView recyclerChat;
    private EditText edtMessage;
    private ImageButton btnSend;
    private int currentUserId;
    private Chat currentChat;
    private ImageView btnBack, imgAvatar;
    private TextView tvName;
    private final android.os.Handler handler = new android.os.Handler();
    private final int REFRESH_INTERVAL = 5000; // 5 giây


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        setupPresenter();
        String id = JwtUtil.getSubFromToken(this);
        if (id != null) {
            currentUserId = Integer.parseInt(id);
            presenter.getRommByCustomerId(currentUserId);
        }

        setupUI();



    }

    private void setupUI() {
        recyclerChat = findViewById(R.id.recyclerChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvName);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(this, currentUserId);
        recyclerChat.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, UserMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void setupPresenter() {
        ChatApi chatApi = RetrofitClient.getChatApi(this);
        ChatRepository repository = new ChatRepository(chatApi);
        presenter = new ChatPresenter(this, repository);
    }

    private void sendMessage() {
        String text = edtMessage.getText().toString().trim();
        if (text.isEmpty() || currentChat == null) return;

        SendMessageRequest request = new SendMessageRequest(currentChat.getChatRoomId(), currentUserId, text);

        presenter.sendMessage(request);

        Message newMessage = new Message();
        newMessage.setChatRoomId(currentChat.getChatRoomId());
        newMessage.setSenderId(currentUserId);
        newMessage.setMessageText(text);

        adapter.addMessage(newMessage);
        recyclerChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        edtMessage.setText("");

        // TODO: gửi lên server qua presenter
        // presenter.sendMessage(newMessage);
    }

    @Override
    public void onGetRoomByCustomerIdSuccess(Chat chat) {
        currentChat = chat;
        List<Message> list = chat.getMessages() != null ? chat.getMessages() : new ArrayList<>();
        adapter.setMessages(list);
        recyclerChat.scrollToPosition(list.size() - 1);

        if (currentUserId == currentChat.getCustomerId()) {
            presenter.getUserById(currentChat.getAdminId());
        } else {
            presenter.getUserById(currentChat.getCustomerId());
        }

        startMessageRefresh();
    }

    @Override
    public void onGetRoomByCustomerIdError(String message) {
        // TODO: Hiển thị thông báo lỗi
    }

    @Override
    public void onSendMessageSuccess(String message) {

    }

    @Override
    public void onSendMessageError(String message) {

    }

    @Override
    public void onGetUserByIdSuccess(User user) {
        tvName.setText(user.getFullName());
        Glide.with(this)
                .load(user.getImgAvatarl())   // URL hoặc đường dẫn ảnh
                .placeholder(R.drawable.ic_profile) // ảnh tạm trong khi tải
                .error(R.drawable.ic_profile)       // ảnh hiển thị khi lỗi
                .circleCrop()                       // bo tròn ảnh
                .into(imgAvatar);                   // gắn vào ImageView

    }

    @Override
    public void onGetUserByIdError(String message) {

    }

    @Override
    public void onFailure(String message) {
        // TODO: Hiển thị lỗi mạng
    }

    private void startMessageRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentChat != null) {
                    presenter.getRommByCustomerId(currentUserId); // gọi lại API để cập nhật tin nhắn mới
                }
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMessageRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

}