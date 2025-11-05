package com.example.se1707_prm392_g2_petshop.ui.user.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.CategoryAdapter;
import com.example.se1707_prm392_g2_petshop.data.adapter.ProductAdapter;
import com.example.se1707_prm392_g2_petshop.data.api.ChatApi;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.Message;
import com.example.se1707_prm392_g2_petshop.data.repositories.ChatRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.ChatUtils;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentHomeBinding;
import com.example.se1707_prm392_g2_petshop.ui.chat.ChatActivity;
import com.example.se1707_prm392_g2_petshop.ui.map.MapActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter featuredProductAdapter;

    private View viewChatDot;
    private ChatRepository chatRepository;
    private int currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        FloatingActionButton fabChat = root.findViewById(R.id.fab_chat);
        viewChatDot = root.findViewById(R.id.view_chat_dot);
        FloatingActionButton fabMap = root.findViewById(R.id.fab_map);

        fabChat.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        });

        fabMap.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapActivity.class);
            startActivity(intent);
        });

        setupCategoryRV();
        setupFeaturedProductRV();
        observeViewModel();

        // Navigate tới tất cả sản phẩm nổi bật
        binding.tvViewAllFeatured.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_home_to_product_list_all);
        });

        // Lấy userId từ token
        String id = JwtUtil.getSubFromToken(requireContext());
        if (id != null) currentUserId = Integer.parseInt(id);

        ChatApi chatApi = RetrofitClient.getChatApi(requireContext());
        chatRepository = new ChatRepository(chatApi);

        checkUnreadMessages();

        return root;
    }

    private void setupCategoryRV() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(category -> {
            NavController navController = Navigation.findNavController(requireView());
            HomeFragmentDirections.ActionHomeToProductList action =
                    HomeFragmentDirections.actionHomeToProductList(
                            category.getCategoryId(),
                            category.getCategoryName()
                    );
            navController.navigate(action);
        });
        binding.rvCategories.setAdapter(categoryAdapter);
    }

    private void setupFeaturedProductRV() {
        binding.rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        featuredProductAdapter = new ProductAdapter(product -> {
            NavController navController = Navigation.findNavController(requireView());
            HomeFragmentDirections.ActionHomeToProductDetail action =
                    HomeFragmentDirections.actionHomeToProductDetail(product.getProductId());
            navController.navigate(action);
        });
        binding.rvFeaturedProducts.setAdapter(featuredProductAdapter);
    }

    private void observeViewModel() {
        homeViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) categoryAdapter.submitList(categories);
        });

        homeViewModel.getFeaturedProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) featuredProductAdapter.submitList(products);
        });
    }

    private void checkUnreadMessages() {
        new Thread(() -> {
            try {
                Chat chat = chatRepository.getRoomByCustomerIdSync(currentUserId);
                if (chat == null) {
                    Log.d("ChatDebug", "Chat null");
                    return;
                }
                if (chat.getMessages() == null || chat.getMessages().isEmpty()) {
                    Log.d("ChatDebug", "No messages");
                    requireActivity().runOnUiThread(() -> viewChatDot.setVisibility(View.GONE));
                    return;
                }

                Message lastMessage = chat.getMessages().get(chat.getMessages().size() - 1);
                boolean unread = ChatUtils.hasUnreadMessages(requireContext(),
                        chat.getChatRoomId(),
                        lastMessage.getMessageId());

                Log.d("ChatDebug", "Unread=" + unread + " | LastMsgID=" + lastMessage.getMessageId());

                requireActivity().runOnUiThread(() -> viewChatDot.setVisibility(unread ? View.VISIBLE : View.GONE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void onResume() {
        super.onResume();
        checkUnreadMessages();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
