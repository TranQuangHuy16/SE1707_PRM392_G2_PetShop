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

public class ProductRatingAdapter extends RecyclerView.Adapter<ProductRatingAdapter.RatingViewHolder> {

    private List<ProductRating> ratings;

    //  Constructor mặc định (thêm mới)
    public ProductRatingAdapter() {
        this.ratings = new ArrayList<>();
    }

    //  Constructor có danh sách (vẫn giữ nguyên)
    public ProductRatingAdapter(List<ProductRating> ratings) {
        this.ratings = ratings != null ? ratings : new ArrayList<>();
    }

    public void setRatings(List<ProductRating> ratings) {
        this.ratings = ratings != null ? ratings : new ArrayList<>();
        notifyDataSetChanged();
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

        holder.tvUserName.setText("User ID: " + rating.getUserId());
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
