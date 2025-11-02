package com.example.se1707_prm392_g2_petshop.ui.auth.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.ui.auth.signup.SignUpActivity;
import com.example.se1707_prm392_g2_petshop.ui.user.main.UserMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class WelcomeActivity extends AppCompatActivity implements WelcomeContract.View {

    private static final String TAG = "WelcomeActivity";
    private Button btnStart;
    private LinearLayout btnGoogle;
    private TextView tvSignUp;

    private WelcomePresenter presenter;
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
        setContentView(R.layout.activity_welcome);

        btnStart = findViewById(R.id.btnStart);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnGoogle = findViewById(R.id.btnGoogle); // 👈 nhớ thêm nút này vào XML

        setupPresenter();
        setupGoogleSignIn();
        setupListeners();
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(this);
        AuthRepository repository = new AuthRepository(authApi);
        presenter = new WelcomePresenter(this, repository);
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupListeners() {
        btnStart.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        btnGoogle.setOnClickListener(v -> signInWithGoogle());
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

    // ✅ Implement WelcomeContract.View
    @Override
    public void onLoginGoogleSuccess(AuthResponse response) {
        Log.d(TAG, "onLoginGoogleSuccess: " + response.getAccessToken());
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        JwtUtil.SaveJwtTokenToSharedPreferences(response.getAccessToken(), prefs);
        startActivity(new Intent(this, UserMainActivity.class));
        finish();
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
