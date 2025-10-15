package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
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

import retrofit2.Retrofit;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordContract.View {

    private EditText edtEmail;
    private Button btnSend;
    private ForgotPasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtEmail);
        btnSend = findViewById(R.id.btnSend);

        presenter = new ForgotPasswordPresenter(
                this,
                new AuthRepository(RetrofitClient.getAuthApi(this))
        );

        btnSend.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            presenter.sendForgotPassword(email);
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToVerify(String email) {
        Intent intent = new Intent(this, VerifyForgotPasswordActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}