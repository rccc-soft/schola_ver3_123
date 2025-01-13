package com.example.schola_ver3;

public class Review {
    private int reviewId;
    private int buyerMemberId;
    private int sellerMemberId;
    private int reviewScore;
    private String reviewComment;

    // コンストラクタ
    public Review(int reviewId, int buyerMemberId, int sellerMemberId, int reviewScore, String reviewComment) {
        this.reviewId = reviewId;
        this.buyerMemberId = buyerMemberId;
        this.sellerMemberId = sellerMemberId;
        this.reviewScore = reviewScore;
        this.reviewComment = reviewComment;
    }

    // ゲッター
    public int getReviewId() {
        return reviewId;
    }

    public int getBuyerMemberId() {
        return buyerMemberId;
    }

    public int getSellerMemberId() {
        return sellerMemberId;
    }

    public int getReviewScore() {
        return reviewScore;
    }

    public String getReviewComment() {
        return reviewComment;
    }
}
