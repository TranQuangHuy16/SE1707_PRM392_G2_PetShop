package com.example.se1707_prm392_g2_petshop.ui.userdetail;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateUserDetailsRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.utils.CloudinaryStorage; // <-- Import util của bạn
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserDetailActivity extends AppCompatActivity implements UserDetailContract.View, CloudinaryStorage.CloudinaryCallback {

    private UserDetailContract.Presenter presenter;
    private UserRepository userRepository;

    // View components
    private Toolbar toolbar;
    private ImageView ivUserAvatar;
    private Button btnChooseImage;
    private TextInputEditText etUsername, etFullName, etEmail, etPhone;
    private Button btnSave;
    private ProgressBar progressBar;

    private int userId;

    // Biến lưu trữ Uri ảnh MỚI (nếu user chọn)
    private Uri newLocalImageUri = null;
    // Biến lưu URL ảnh CŨ (lấy từ server)
    private String existingImageUrl = null;

    private String currentCameraPhotoPath; // Chỉ dùng cho camera

    // ActivityResultLaunchers
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);


        String userIdString = JwtUtil.getSubFromToken(this);
        if (userIdString == null || userIdString.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy User ID trong token", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            this.userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "User ID không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRepository = UserRepository.getInstance(this);
        presenter = new UserDetailPresenter(this, userRepository, this);

        userRepository = UserRepository.getInstance(this);
        presenter = new UserDetailPresenter(this, userRepository, this);

        bindViews();
        registerActivityResults(); // <-- Quan trọng
        setupListeners();

        presenter.loadUserDetails(userId);
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        etUsername = findViewById(R.id.etUsername);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());

        // Mở dialog chọn ảnh
        btnChooseImage.setOnClickListener(v -> showImagePickerDialog());

        // Nút Save
        btnSave.setOnClickListener(v -> handleSaveChanges());
    }

    // ================================================================
    // LOGIC XỬ LÝ ẢNH (Permissions, Launchers)
    // ================================================================

    private void registerActivityResults() {
        // 1. Launcher xin quyền
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            boolean granted = true;
            for (Boolean isGranted : permissions.values()) {
                if (!isGranted) {
                    granted = false;
                    break;
                }
            }
            if (granted) {
                showImagePickerDialog(); // Quyền đã được cấp, mở lại dialog
            } else {
                Toast.makeText(this, "Permissions are required to change photo.", Toast.LENGTH_SHORT).show();
            }
        });

        // 2. Launcher lấy ảnh từ Thư viện
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                newLocalImageUri = result.getData().getData();
                if (newLocalImageUri != null) {
                    // Hiển thị ảnh mới chọn bằng Glide
                    Glide.with(this).load(newLocalImageUri).into(ivUserAvatar);
                }
            }
        });

        // 3. Launcher chụp ảnh từ Camera
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success) {
                // Uri đã được gán trong hàm 'dispatchTakePictureIntent'
                // newLocalImageUri đã được set
                Glide.with(this).load(newLocalImageUri).into(ivUserAvatar);
            } else {
                // Xóa file tạm nếu chụp không thành công
                if (currentCameraPhotoPath != null) {
                    new File(currentCameraPhotoPath).delete();
                }
            }
        });
    }

    private void showImagePickerDialog() {
        // Kiểm tra quyền trước
        String[] permissions = getRequiredPermissions();
        if (checkPermissions(permissions)) {
            // Đã có quyền, hiển thị dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Image Source");
            builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
                if (which == 0) {
                    dispatchTakePictureIntent(); // Chụp ảnh
                } else {
                    dispatchPickImageIntent(); // Chọn từ thư viện
                }
            });
            builder.show();
        } else {
            // Chưa có quyền, yêu cầu quyền
            requestPermissionLauncher.launch(permissions);
        }
    }

    // Lấy ảnh từ thư viện
    private void dispatchPickImageIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(galleryIntent);
    }

    // Chụp ảnh mới
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Camera app not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e("UserDetailActivity", "Error creating image file: " + ex.getMessage());
            Toast.makeText(this, "Error creating image file.", Toast.LENGTH_SHORT).show();
        }

        if (photoFile != null) {
            // Gán Uri cho biến global để launcher có thể dùng
            newLocalImageUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".fileprovider",
                    photoFile);
            takePictureLauncher.launch(newLocalImageUri);
        }
    }

    // Tạo file ảnh tạm
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentCameraPhotoPath = image.getAbsolutePath(); // Lưu đường dẫn
        return image;
    }

    // Tiện ích kiểm tra quyền
    private String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            return new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES};
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10-12
            return new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        } else { // Android 9 trở xuống
            return new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    private boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    // ================================================================
    // LOGIC LƯU THAY ĐỔI (VỚI CLOUDINARY)
    // ================================================================

    private void handleSaveChanges() {
        // Lấy dữ liệu từ form
        String username = etUsername.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // (Nên thêm validation ở đây)

        if (newLocalImageUri != null) {
            // ✅ TRƯỜNG HỢP 1: User đã chọn ảnh mới -> Cần UPLOAD LÊN CLOUDINARY
            Log.d("UserDetailActivity", "New image selected. Uploading to Cloudinary...");
            showLoading(true);
            // Gọi hàm Util của bạn
            CloudinaryStorage.uploadImage(getContentResolver(), newLocalImageUri, this);

        } else {
            // ✅ TRƯỜNG HỢP 2: User không đổi ảnh -> Dùng URL cũ (existingImageUrl)
            Log.d("UserDetailActivity", "No new image. Updating with existing URL: " + existingImageUrl);
            UpdateUserDetailsRequest request = new UpdateUserDetailsRequest(
                    username, fullName, email, phone, existingImageUrl // Gửi URL cũ
            );
            presenter.updateUserDetails(userId, request);
        }
    }

    // --- Callback từ CloudinaryStorage ---

    @Override
    public void onSuccess(String imageUrl) {
        // Cloudinary trả về URL thành công (đang ở background thread)
        Log.d("UserDetailActivity", "Cloudinary Upload Success: " + imageUrl);

        // Lấy dữ liệu text (phải lấy ở đây để đảm bảo request hoàn chỉnh)
        String username = etUsername.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Tạo request với URL MỚI từ Cloudinary
        UpdateUserDetailsRequest request = new UpdateUserDetailsRequest(
                username, fullName, email, phone, imageUrl // Gửi URL mới
        );

        // Chuyển về UI thread để gọi Presenter
        runOnUiThread(() -> {
            // Không cần showLoading(false) vì presenter sẽ xử lý
            presenter.updateUserDetails(userId, request);
        });
    }

    @Override
    public void onError(Exception e) {
        // Cloudinary upload lỗi (đang ở background thread)
        Log.e("UserDetailActivity", "Cloudinary Upload Error: " + e.getMessage());
        runOnUiThread(() -> {
            showLoading(false);
            showError("Image upload failed. Please try again.");
        });
    }


    // ================================================================
    // IMPLEMENT UserDetailContract.View (từ Presenter)
    // ================================================================

    @Override
    public void showUserDetails(UserDetailResponse user) {
        etUsername.setText(user.getUsername());
        etFullName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());

        // Lưu URL cũ
        this.existingImageUrl = user.getImgUrl();

        // Dùng Glide để tải ảnh đại diện hiện có
        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(existingImageUrl)
                    .placeholder(R.drawable.ic_default_avatar) // Ảnh placeholder
                    .error(R.drawable.ic_default_avatar)      // Ảnh khi lỗi
                    .into(ivUserAvatar);
        } else {
            ivUserAvatar.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!isLoading);
        btnChooseImage.setEnabled(!isLoading);
    }

    @Override
    public void showUpdateSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK); // Gửi kết quả về Activity trước
        finish(); // Đóng Activity
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}