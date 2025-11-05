package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordContract.View {

    private EditText edtPassword, edtConfirmPassword;
    private Button btnSend;
    private ResetPasswordPresenter presenter;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.setupEdgeToEdge(this);
        View rootView = findViewById(android.R.id.content);
        WindowInsetsUtil.applySystemBarInsets(rootView);

        email = getIntent().getStringExtra("email");

        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSend = findViewById(R.id.btnSend);

        presenter = new ResetPasswordPresenter(
                this,
                new AuthRepository(RetrofitClient.getAuthApi(this))
        );

        btnSend.setOnClickListener(v -> {
            String password = edtPassword.getText().toString().trim();
            String confirm = edtConfirmPassword.getText().toString().trim();
            presenter.resetPassword(email, password, confirm);
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResetSuccess() {
        Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}