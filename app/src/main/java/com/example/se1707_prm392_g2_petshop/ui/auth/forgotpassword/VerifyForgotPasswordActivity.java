package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class VerifyForgotPasswordActivity extends AppCompatActivity implements VerifyForgotPasswordContract.View {

    private TextView tvEmail;
    private Button btnVerify;
    private EditText etCode1, etCode2, etCode3, etCode4, etCode5;
    private VerifyForgotPasswordPresenter presenter;
    private String email;

    private TextView tvResend;
    private CountDownTimer countDownTimer;
    private static final long RESEND_INTERVAL = 60000; // 60s


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_forgot_password);

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.setupEdgeToEdge(this);
        View rootView = findViewById(android.R.id.content);
        WindowInsetsUtil.applySystemBarInsets(rootView);

        email = getIntent().getStringExtra("email");

        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText(email);

        etCode1 = findViewById(R.id.etCode1);
        etCode2 = findViewById(R.id.etCode2);
        etCode3 = findViewById(R.id.etCode3);
        etCode4 = findViewById(R.id.etCode4);
        etCode5 = findViewById(R.id.etCode5);
        btnVerify = findViewById(R.id.btnVerify);
        tvResend = findViewById(R.id.tvResend);

        presenter = new VerifyForgotPasswordPresenter(
                this,
                new AuthRepository(RetrofitClient.getAuthApi(this))
        );

        setupOtpInputs();

        // ✅ Bắt đầu đếm ngược ngay khi mở Activity
        startResendCountdown();

        btnVerify.setOnClickListener(v -> {
            String otp = etCode1.getText().toString()
                    + etCode2.getText().toString()
                    + etCode3.getText().toString()
                    + etCode4.getText().toString()
                    + etCode5.getText().toString();

            presenter.verifyOtp(email, otp);
        });

        // ✅ Sự kiện bấm "Resend"
        tvResend.setOnClickListener(v -> {
            if (tvResend.isEnabled()) {
                presenter.requestOtp(email); // gọi API resend OTP
                showMessage("Đã gửi lại mã OTP!");
                startResendCountdown(); // reset lại timer
            }
        });
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToReset(String email) {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    private void setupOtpInputs() {
        EditText[] otpFields = {etCode1, etCode2, etCode3, etCode4, etCode5};

        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        otpFields[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void startResendCountdown() {
        tvResend.setEnabled(false);
        tvResend.setTextColor(Color.GRAY);

        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(RESEND_INTERVAL, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvResend.setText("Resend (" + seconds + "s)");
            }

            @Override
            public void onFinish() {
                tvResend.setEnabled(true);
                tvResend.setText("Resend");
                tvResend.setTextColor(Color.parseColor("#FF9800"));
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) countDownTimer.cancel();
        super.onDestroy();
    }



}