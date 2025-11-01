package com.example.se1707_prm392_g2_petshop.ui.admin.products.manage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.CategoryRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductManageFragment extends Fragment implements ProductManageContract.View {

    private ProductManageContract.Presenter mPresenter;

    private ProgressBar progressBar;
    private Product currentProduct;

    private ImageView ivProductImage;
    private TextInputEditText txtProductName, txtProductDescription, txtProductPrice, txtQuantityInStock;
    private AutoCompleteTextView autoCompleteCategory;
    private MaterialButton btnChangeImage, btnEditProduct, btnSaveProduct, btnDeleteProduct;
    private SwitchMaterial switchProductStatus;

    private List<Category> categories = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private int selectedCategoryId = -1;
    private String selectedCategoryName = "";

    private boolean isEditMode = false;

    private boolean categoriesLoaded = false;
    private boolean productLoaded = false;

    // --- ActivityResultLaunchers for Image Selection ---
    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;
    private Uri tempImageUri;
    private Uri selectedImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerActivityResults();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);
        setupPresenter();
        setupListeners();
        loadCategories();
        setProductId();
    }

    private void registerActivityResults() {
        // Launcher for picking image from gallery
        pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Glide.with(requireContext()).load(uri).into(ivProductImage);
            } else {
                showErrorMessage("No image selected.");
            }
        });

        // Launcher for taking a picture
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
            if (success) {
                selectedImageUri = tempImageUri;
                Glide.with(requireContext()).load(tempImageUri).into(ivProductImage);
            } else {
                showErrorMessage("Failed to capture image.");
            }
        });

        // Launcher for requesting camera permission
        requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                launchCamera();
            } else {
                showErrorMessage("Camera permission is required to take photos.");
            }
        });
    }

    private void setupViews(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        ivProductImage = view.findViewById(R.id.product_image_view);
        txtProductName = view.findViewById(R.id.edit_text_product_name);
        txtProductDescription = view.findViewById(R.id.edit_text_product_description);
        txtProductPrice = view.findViewById(R.id.edit_text_product_price);
        txtQuantityInStock = view.findViewById(R.id.edit_text_quantity_in_stock);
        autoCompleteCategory = view.findViewById(R.id.autocomplete_text_view_category);
        btnChangeImage = view.findViewById(R.id.button_change_image);
        btnEditProduct = view.findViewById(R.id.button_edit_product);
        btnSaveProduct = view.findViewById(R.id.button_save_product);
        btnDeleteProduct = view.findViewById(R.id.button_delete_product);
        switchProductStatus = view.findViewById(R.id.switch_product_status);
    }

    private void setupPresenter() {
        ProductRepository productRepository = ProductRepository.getInstance(requireContext());
        CategoryRepository categoryRepository = CategoryRepository.getInstance(requireContext());
        mPresenter = new ProductManagePresenter(this, categoryRepository, productRepository);
    }

    private void setProductId() {
        if (getArguments() != null) {
            int productId = getArguments().getInt("productId", -1);
            if (productId != -1) {
                progressBar.setVisibility(View.VISIBLE);
                mPresenter.loadProductDetail(productId);
            }
        }
    }

    private void setupListeners() {
        // Category dropdown selection
        autoCompleteCategory.setOnItemClickListener((parent, view1, position, id) -> {
            Category selected = categories.get(position);
            selectedCategoryName = selected.getCategoryName();
            selectedCategoryId = selected.getCategoryId();
        });

        // Edit button: bật chế độ chỉnh sửa
        btnEditProduct.setOnClickListener(v -> toggleEditMode(true));

        // Change Image button
        btnChangeImage.setOnClickListener(v -> showImageSourceDialog());

        // Save product
        btnSaveProduct.setOnClickListener(v -> {
            try {
                // Check if all fields are filled
                String imageUrl = (currentProduct != null) ? currentProduct.getImageUrl() : null;

                Product product = new Product(
                        currentProduct == null ? 0 : currentProduct.getProductId(),
                        selectedCategoryId,
                        selectedCategoryName,
                        txtProductName.getText().toString(),
                        txtProductDescription.getText().toString(),
                        Double.parseDouble(txtProductPrice.getText().toString()),
                        Integer.parseInt(txtQuantityInStock.getText().toString()), // Corrected this line
                        imageUrl,
                        switchProductStatus.isChecked()
                );
                mPresenter.saveProduct(product);
                toggleEditMode(false);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Please enter valid numeric values.", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete product
        btnDeleteProduct.setOnClickListener(v -> {
            if (currentProduct != null) {
                mPresenter.deleteProduct(currentProduct.getProductId());
            }
        });
    }

    private void showImageSourceDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) { // Camera
                        checkCameraPermissionAndLaunch();
                    } else { // Gallery
                        launchGallery();
                    }
                })
                .show();
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void launchGallery() {
        pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void launchCamera() {
        tempImageUri = createImageUri();
        if (tempImageUri != null) {
            takePictureLauncher.launch(tempImageUri);
        } else {
            showErrorMessage("Could not create image file.");
        }
    }

    private Uri createImageUri() {
        File imageFile = new File(requireContext().getFilesDir(), "camera_photo.jpg");
        return FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".provider",
                imageFile
        );
    }


    private void toggleEditMode(boolean enable) {
        isEditMode = enable;

        txtProductName.setEnabled(enable);
        txtProductDescription.setEnabled(enable);
        txtProductPrice.setEnabled(enable);
        txtQuantityInStock.setEnabled(enable);
        autoCompleteCategory.setEnabled(enable);
        switchProductStatus.setEnabled(enable);
        switchProductStatus.setVisibility(View.VISIBLE);


        btnChangeImage.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnSaveProduct.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnDeleteProduct.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnEditProduct.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    // ----- View interface methods -----

    @Override
    public void showProductDetail(Product product) {
        progressBar.setVisibility(View.GONE);
        currentProduct = product;

        if (product == null) {
            showErrorMessage("Failed to load product detail.");
            return;
        }

        productLoaded = true;

        // Load product info
        txtProductName.setText(product.getProductName());
        txtProductDescription.setText(product.getDescription());
        txtProductPrice.setText(String.valueOf(product.getPrice()));
        txtQuantityInStock.setText(String.valueOf(product.getStock()));
        autoCompleteCategory.setText(product.getCategoryName(), false);
        switchProductStatus.setChecked(product.isActive());

        Glide.with(requireContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.product_img)
                .into(ivProductImage);

        updateProductCategoryView();
    }

    @Override
    public void showErrorMessage(String msg) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void loadCategories() {
        mPresenter.loadCategories().observe(getViewLifecycleOwner(), categoryList -> {
            if (categoryList == null || categoryList.isEmpty()) {
                showErrorMessage("Failed to load categories.");
                return;
            }

            categories = categoryList;
            categoriesLoaded = true;

            List<String> categoryNames = new ArrayList<>();
            for (Category c : categories) categoryNames.add(c.getCategoryName());

            categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoryNames);
            autoCompleteCategory.setAdapter(categoryAdapter);

            updateProductCategoryView();
        });
    }

    private void updateProductCategoryView() {
        if (!productLoaded || !categoriesLoaded) {
            return; // Wait until both product and categories are loaded
        }

        if (currentProduct != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getCategoryId() == currentProduct.getCategoryId()) {
                    autoCompleteCategory.setText(categories.get(i).getCategoryName(), false);
                    selectedCategoryId = currentProduct.getCategoryId();
                    selectedCategoryName = currentProduct.getCategoryName();
                    break;
                }
            }
        }
    }
}
