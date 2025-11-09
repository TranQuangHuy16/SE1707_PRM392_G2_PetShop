package com.example.se1707_prm392_g2_petshop.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.ProductRating;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductRatingAdapter extends RecyclerView.Adapter<ProductRatingAdapter.RatingViewHolder> {

    private List<ProductRating> ratings;
    private Map<Integer, String> userMap; // userId -> userName

    public ProductRatingAdapter() {
        this.ratings = new ArrayList<>();
    }

    public ProductRatingAdapter(List<ProductRating> ratings) {
        this.ratings = ratings != null ? ratings : new ArrayList<>();
    }

    public void setRatings(List<ProductRating> ratings) {
        this.ratings = ratings != null ? ratings : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Set map userId -> userName
    public void setUserMap(Map<Integer, String> userMap) {
        this.userMap = userMap;
        notifyDataSetChanged(); // cập nhật lại tên user
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        ProductRating rating = ratings.get(position);

        // Nếu có map userName thì hiển thị, nếu không thì hiển thị userId
        String userName = (userMap != null && userMap.containsKey(rating.getUserId()))
                ? userMap.get(rating.getUserId())
                : "User ID: " + rating.getUserId();
        holder.tvUserName.setText(userName);

        holder.tvStars.setText("⭐ " + rating.getStars() + " sao");
        holder.tvComment.setText(rating.getComment());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvCreatedAt.setText(sdf.format(rating.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return ratings != null ? ratings.size() : 0;
    }

    static class RatingViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvStars, tvComment, tvCreatedAt;

        RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvStars = itemView.findViewById(R.id.tvStars);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
