package com.example.se1707_prm392_g2_petshop.ui.auth.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginFacebookRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.databinding.ActivityLoginBinding;
import com.example.se1707_prm392_g2_petshop.ui.admin.AdminActivity;
import com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword.ForgotPasswordActivity;
import com.example.se1707_prm392_g2_petshop.ui.auth.signup.SignUpActivity;
import com.example.se1707_prm392_g2_petshop.ui.user.main.UserMainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private LoginPresenter presenter;

    private GoogleSignInClient googleSignInClient;

    private CallbackManager callbackManager;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Log.d("LoginActivity", "Result code: " + result.getResultCode());
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleGoogleSignInResult(task);
                } else {
                    Log.w("LoginActivity", "Cancelled or no data");
                }
            });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.setupEdgeToEdge(this);
        View rootView = findViewById(android.R.id.content);
        WindowInsetsUtil.applySystemBarInsets(rootView);

        setupPresenter();
        setupGoogleSignIn();
        setupFacebookLogin();
        setupListeners();
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(this);
        AuthRepository authRepository = new AuthRepository(authApi);
        presenter = new LoginPresenter(this, authRepository);
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                presenter.loginWithFacebook(new LoginFacebookRequest(accessToken));
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "Facebook Login Cancelled.");
                showToast("Login cancelled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "Facebook Login Error", error);
                showToast("Facebook login failed: " + error.getMessage());
            }
        });
    }

    private void setupListeners() {
        binding.tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.edtUsername.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Please fill all fields");
            } else {
                presenter.login(new LoginRequest(username, password));
            }
        });

        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());

        binding.btnFacebook.setOnClickListener(v -> signInWithFacebook());

        binding.tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
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
                presenter.loginWithGoogle(new LoginGooleRequest(idToken));
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google SignIn failed: " + e.getStatusCode(), e);
            showToast("Google sign-in failed!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    
    private void navigateToNextScreen(AuthResponse response) {
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        JwtUtil.SaveJwtTokenToSharedPreferences(response.getAccessToken(), prefs);

        String role = JwtUtil.getRoleFromToken(response.getAccessToken());

        if ("Admin".equalsIgnoreCase(role)) {
            startActivity(new Intent(this, AdminActivity.class));
        } else {
            startActivity(new Intent(this, UserMainActivity.class));
        }
        finish();
    }

    // ✅ Implement interface LoginContract.View
    @Override
    public void onLoginSuccess(AuthResponse response) {
        // Lưu JWT token
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        JwtUtil.SaveJwtTokenToSharedPreferences(response.getAccessToken(), prefs);

        // Lấy userId từ JWT
        String idString = JwtUtil.getSubFromToken(this);
        int userId = idString != null ? Integer.parseInt(idString) : -1;
        // Lấy token FCM và gửi lên server
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String fcmToken = task.getResult();

                        // Gọi API cập nhật token
                        UserRepository repo = new UserRepository(this);
                        repo.updateFcmToken(userId, fcmToken);
                    } else {
                        Log.e("FCM", "Failed to get FCM token", task.getException());
                    }
                });

        // Chuyển màn hình
        navigateToNextScreen(response);
    }

    @Override
    public void onLoginGoogleSuccess(AuthResponse response) {
        navigateToNextScreen(response);
    }

    @Override
    public void onLoginFacebookSuccess(AuthResponse response) {
        navigateToNextScreen(response);
    }

    @Override
    public void onLoginFailure(String message) {
        showToast(message);
    }

    @Override
    public void onLoginError(String message) {
        Log.e(TAG, "onLoginError: " + message);
        showToast("Error: " + message);
    }
}
