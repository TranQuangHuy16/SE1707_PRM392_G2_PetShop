package com.example.se1707_prm392_g2_petshop.data.utils;

/**
 * ============================================
 * HƯỚNG DẪN SỬ DỤNG WINDOW INSETS
 * ============================================
 * 
 * Để fix vấn đề bị che bởi notch (rabbit's ears) và navigation bar,
 * thêm code sau vào Activity hoặc Fragment:
 * 
 * ============================================
 * 1. TRONG ACTIVITY (onCreate)
 * ============================================
 * 
 * @Override
 * protected void onCreate(Bundle savedInstanceState) {
 *     super.onCreate(savedInstanceState);
 *     setContentView(R.layout.activity_main);
 *     
 *     // Setup edge-to-edge
 *     WindowInsetsUtil.setupEdgeToEdge(this);
 *     
 *     // Áp dụng insets cho root view
 *     View rootView = findViewById(R.id.root_layout); // ID của root layout
 *     WindowInsetsUtil.applySystemBarInsets(rootView);
 * }
 * 
 * ============================================
 * 2. TRONG FRAGMENT (onViewCreated)
 * ============================================
 * 
 * @Override
 * public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
 *     super.onViewCreated(view, savedInstanceState);
 *     
 *     // Áp dụng insets cho root view
 *     WindowInsetsUtil.applySystemBarInsets(view);
 * }
 * 
 * ============================================
 * 3. CÁC PHƯƠNG THỨC KHẢ DỤNG
 * ============================================
 * 
 * // Áp dụng padding cho TẤT CẢ các cạnh (top, bottom, left, right)
 * WindowInsetsUtil.applySystemBarInsets(view);
 * 
 * // Áp dụng padding CHỈ cho top và bottom
 * WindowInsetsUtil.applyVerticalInsets(view);
 * 
 * // Áp dụng padding CHỈ cho top (thích hợp cho toolbar)
 * WindowInsetsUtil.applyTopInsets(view);
 * 
 * // Áp dụng padding CHỈ cho bottom (thích hợp cho button ở dưới cùng)
 * WindowInsetsUtil.applyBottomInsets(view);
 * 
 * // Áp dụng MARGIN thay vì padding
 * WindowInsetsUtil.applySystemBarInsetsAsMargin(view);
 * 
 * ============================================
 * 4. TRONG XML LAYOUT
 * ============================================
 * 
 * Thêm android:fitsSystemWindows="true" vào root layout:
 * 
 * <androidx.constraintlayout.widget.ConstraintLayout
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     android:id="@+id/root_layout"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:fitsSystemWindows="true">
 *     
 *     <!-- Content here -->
 *     
 * </androidx.constraintlayout.widget.ConstraintLayout>
 * 
 * ============================================
 * 5. VÍ DỤ THỰC TẾ
 * ============================================
 * 
 * // Màn hình Login - Áp dụng cho toàn bộ
 * View rootView = findViewById(R.id.login_root);
 * WindowInsetsUtil.applySystemBarInsets(rootView);
 * 
 * // Màn hình có Toolbar - Chỉ áp dụng cho top
 * View toolbar = findViewById(R.id.toolbar);
 * WindowInsetsUtil.applyTopInsets(toolbar);
 * 
 * // Màn hình có Button dưới cùng - Chỉ áp dụng cho bottom
 * View buttonContainer = findViewById(R.id.button_container);
 * WindowInsetsUtil.applyBottomInsets(buttonContainer);
 * 
 * ============================================
 * 6. TROUBLESHOOTING
 * ============================================
 * 
 * Nếu vẫn bị che:
 * 1. Kiểm tra theme đã enable edge-to-edge chưa
 * 2. Kiểm tra root layout có android:fitsSystemWindows="true" chưa
 * 3. Thử dùng applySystemBarInsetsAsMargin thay vì padding
 * 4. Kiểm tra có view nào override insets không
 * 
 * ============================================
 */
public class WindowInsetsGuide {
    // This is just a guide class, no implementation needed
}
