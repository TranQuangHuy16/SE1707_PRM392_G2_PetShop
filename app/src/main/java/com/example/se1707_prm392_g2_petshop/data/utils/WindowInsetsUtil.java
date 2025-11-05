package com.example.se1707_prm392_g2_petshop.data.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Utility class để xử lý Window Insets
 * - Fix vấn đề bị che bởi notch (rabbit's ears)
 * - Fix vấn đề bị che bởi navigation bar
 * - Fix vấn đề bị che bởi status bar
 */
public class WindowInsetsUtil {

    /**
     * Áp dụng padding cho view để tránh bị che bởi system bars
     * @param view View cần áp dụng padding
     */
    public static void applySystemBarInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            // Áp dụng padding
            v.setPadding(
                insets.left,
                insets.top,
                insets.right,
                insets.bottom
            );
            
            return WindowInsetsCompat.CONSUMED;
        });
    }

    /**
     * Áp dụng padding CHỈ cho top và bottom (status bar và navigation bar)
     * @param view View cần áp dụng padding
     */
    public static void applyVerticalInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            // Giữ nguyên padding left/right, chỉ thêm top/bottom
            int paddingLeft = v.getPaddingLeft();
            int paddingRight = v.getPaddingRight();
            
            v.setPadding(
                paddingLeft,
                insets.top,
                paddingRight,
                insets.bottom
            );
            
            return WindowInsetsCompat.CONSUMED;
        });
    }

    /**
     * Áp dụng padding CHỈ cho top (status bar và notch)
     * @param view View cần áp dụng padding
     */
    public static void applyTopInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            int paddingLeft = v.getPaddingLeft();
            int paddingRight = v.getPaddingRight();
            int paddingBottom = v.getPaddingBottom();
            
            v.setPadding(
                paddingLeft,
                insets.top,
                paddingRight,
                paddingBottom
            );
            
            return WindowInsetsCompat.CONSUMED;
        });
    }

    /**
     * Áp dụng padding CHỈ cho bottom (navigation bar)
     * @param view View cần áp dụng padding
     */
    public static void applyBottomInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            int paddingLeft = v.getPaddingLeft();
            int paddingTop = v.getPaddingTop();
            int paddingRight = v.getPaddingRight();
            
            v.setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                insets.bottom
            );
            
            return WindowInsetsCompat.CONSUMED;
        });
    }

    /**
     * Áp dụng margin thay vì padding
     * @param view View cần áp dụng margin
     */
    public static void applySystemBarInsetsAsMargin(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = insets.top;
            params.bottomMargin = insets.bottom;
            params.leftMargin = insets.left;
            params.rightMargin = insets.right;
            v.setLayoutParams(params);
            
            return WindowInsetsCompat.CONSUMED;
        });
    }

    /**
     * Setup edge-to-edge cho Activity
     * @param activity Activity cần setup
     */
    public static void setupEdgeToEdge(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            activity.getWindow().setDecorFitsSystemWindows(false);
        } else {
            // Android 10 và thấp hơn
            View decorView = activity.getWindow().getDecorView();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(flags);
        }
    }

    /**
     * Áp dụng padding với animation mượt mà
     * @param view View cần áp dụng
     */
    public static void applySystemBarInsetsAnimated(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            v.animate()
                .translationY(0)
                .setDuration(250)
                .withStartAction(() -> {
                    v.setPadding(
                        insets.left,
                        insets.top,
                        insets.right,
                        insets.bottom
                    );
                })
                .start();
            
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
