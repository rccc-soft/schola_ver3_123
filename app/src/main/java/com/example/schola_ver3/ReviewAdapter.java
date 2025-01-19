package com.example.schola_ver3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerViewアダプター
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews = new ArrayList<>();

    /**
     * レビューリストを設定
     */
    public void setReviews(List<Review> reviews) {
        if (reviews == null) {
            this.reviews = new ArrayList<>();
        } else {
            this.reviews = reviews;
        }
        notifyDataSetChanged();
    }

    /**
     * ViewHolderクラス
     */
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewBuyerId;
        public TextView textViewReviewScore;
        public TextView textViewReviewComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBuyerId = itemView.findViewById(R.id.textViewBuyerId);
            textViewReviewScore = itemView.findViewById(R.id.textViewReviewScore);
            textViewReviewComment = itemView.findViewById(R.id.textViewReviewComment);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // レビューアイテムのレイアウトをインフレート
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // レビュー情報を設定
        Review review = reviews.get(position);
        holder.textViewBuyerId.setText("購入者ID: " + review.getBuyerMemberId());
        holder.textViewReviewScore.setText("評価: " + getStarRating(review.getReviewScore()));
        holder.textViewReviewComment.setText("レビューコメント: " + review.getReviewComment());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    /**
     * 評価スコアを星の形に変換
     */
    private String getStarRating(int score) {
        StringBuilder stars = new StringBuilder();
        for(int i = 0; i < score; i++) {
            stars.append("★");
        }
        for(int i = score; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
}
