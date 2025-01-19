package com.example.schola_ver3;

public class Review {
    private int reviewId;
    private String buyerMemberId;
    private String sellerMemberId;
    private int reviewScore;
    private String reviewComment;

    // コンストラクタ
    public Review(int reviewId, String buyerMemberId, String sellerMemberId, int reviewScore, String reviewComment) {
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

    public String getBuyerMemberId() {
        return buyerMemberId;
    }

    public String getSellerMemberId() {
        return sellerMemberId;
    }

    public int getReviewScore() {
        return reviewScore;
    }

    public String getReviewComment() {
        return reviewComment;
    }
}
