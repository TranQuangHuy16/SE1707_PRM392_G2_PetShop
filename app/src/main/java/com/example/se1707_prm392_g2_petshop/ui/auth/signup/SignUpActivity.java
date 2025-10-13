package com.example.se1707_prm392_g2_petshop.ui.auth.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RegisterRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.databinding.ActivitySignUpBinding;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.ui.user.main.UserMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private static final String TAG = "SignUpActivity";
    private ActivitySignUpBinding binding;
    private SignUpPresenter presenter;
    private GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleGoogleSignInResult(task);
                } else {
                    Log.w(TAG, "Google Sign-In cancelled or no data");
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupPresenter();
        setupGoogleSignIn();
        setupListeners();
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(this);
        AuthRepository authRepository = new AuthRepository(authApi);
        presenter = new SignUpPresenter(this, authRepository);
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupListeners() {
        // Điều hướng sang Login
        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // register
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.etUsername.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                String phone = binding.etPhone.getText().toString().trim();
                String fullName = binding.etFullName.getText().toString().trim();

                RegisterRequest user = new RegisterRequest(username, password, fullName, email, phone);

                presenter.reigster(user);
            }
        });

        // 👇 Đăng nhập bằng Google
        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String idToken = account.getIdToken();
                if (idToken == null) {
                    showToast("Google ID Token is null!");
                    return;
                }
                Log.d(TAG, "Google SignIn ID Token: " + idToken);
                presenter.loginWithGoogle(new LoginGooleRequest(idToken));
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google SignIn failed: " + e.getStatusCode(), e);
            showToast("Google sign-in failed!");
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // ✅ Implement SignUpContract.View
    @Override
    public void onLoginGoogleSuccess(AuthResponse response) {
        Log.d(TAG, "onLoginGoogleSuccess: " + response.getAccessToken());
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        JwtUtil.SaveJwtTokenToSharedPreferences(response.getAccessToken(), prefs);
        startActivity(new Intent(this, UserMainActivity.class));
        finish();
    }

    @Override
    public void onRegisterSuccess() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onFailure(String message) {
        showToast(message);
    }

    @Override
    public void onError(String message) {
        Log.e(TAG, "onLoginError: " + message);
        showToast("Error: " + message);
    }
}
