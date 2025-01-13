package com.example.schola_ver3;

import android.content.Context;

import java.util.List;

public class ReviewManager {
    private review_seller_DB dbHelper;

    public ReviewManager(Context context) {
        dbHelper = new review_seller_DB(context);
    }

    public double getAverageReviewScore(int sellerMemberId) throws Exception {
        return dbHelper.getAverageReviewScore(sellerMemberId);
    }

    public List<Integer> getReviewScores(int sellerMemberId) throws Exception {
        return dbHelper.getReviewScores(sellerMemberId);
    }

    public void addReview(int buyerMemberId, int sellerMemberId, int reviewScore, String reviewComment) throws Exception {
        dbHelper.addReview(buyerMemberId, sellerMemberId, reviewScore, reviewComment);
    }

    // 全てのレビューを取得

    public List<Review> getAllReviews() throws Exception {
        return dbHelper.getAllReviews();
    }

    public void close() {
        dbHelper.close();
    }
}
