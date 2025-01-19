package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class review_seller_DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reviews.db";
    private static final int DATABASE_VERSION = 7;
    public static final String DATABASE_TABLENAME = "REVIEWdb";
    public static final String COLUMN_REVIEW_ID = "review_id";
    public static final String COLUMN_BUYER_MEMBER_ID = "buyer_member_id";
    public static final String COLUMN_SELLER_MEMBER_ID = "seller_member_id";
    public static final String COLUMN_REVIEW_SCORE = "review_score";
    public static final String COLUMN_REVIEW_COMMENT = "review_comment";

    public review_seller_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REVIEW_TABLE = "CREATE TABLE " + DATABASE_TABLENAME + "("
                + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_BUYER_MEMBER_ID + " TEXT NOT NULL,"
                + COLUMN_SELLER_MEMBER_ID + " TEXT NOT NULL,"
                + COLUMN_REVIEW_SCORE + " INTEGER NOT NULL,"
                + COLUMN_REVIEW_COMMENT + " TEXT"
                + ")";
        db.execSQL(CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 古いテーブルを削除
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLENAME);
        // 新しいテーブルを作成
        onCreate(db);
    }

    /**
     * レビューを追加するメソッド
     */
    public void addReview(String buyerMemberId, String sellerMemberId, int reviewScore, String reviewComment) throws Exception {
        if (sellerMemberId == null || sellerMemberId.isEmpty()) {
            throw new IllegalArgumentException("seller_member_id cannot be null or empty");
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUYER_MEMBER_ID, buyerMemberId);
        values.put(COLUMN_SELLER_MEMBER_ID, sellerMemberId);
        values.put(COLUMN_REVIEW_SCORE, reviewScore);
        values.put(COLUMN_REVIEW_COMMENT, reviewComment);

        try {
            db.insertOrThrow(DATABASE_TABLENAME, null, values);
        } catch (Exception e) {
            Log.e("review_seller_DB", "Error while adding review", e);
            throw e;
        } finally {
            db.close();
        }
    }

    /**
     * 全てのレビューを取得するメソッド
     */
    public List<Review> getAllReviews() throws Exception {
        List<Review> reviewList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DATABASE_TABLENAME + " ORDER BY " + COLUMN_REVIEW_ID + " DESC";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_ID));
                    String buyerId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUYER_MEMBER_ID));
                    String sellerId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SELLER_MEMBER_ID));
                    int reviewScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_SCORE));
                    String reviewComment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_COMMENT));

                    Review review = new Review(reviewId, buyerId, sellerId, reviewScore, reviewComment);
                    reviewList.add(review);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("review_seller_DB", "Error fetching all reviews", e);
            throw e;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return reviewList;
    }

    /**
     * 平均レビュースコアを取得するメソッド
     */
    public double getAverageReviewScore(String sellerMemberId) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        double average = 0.0;
        Cursor cursor = null;
        try {
            String query = "SELECT AVG(" + COLUMN_REVIEW_SCORE + ") as avg_score " +
                    "FROM " + DATABASE_TABLENAME + " WHERE " + COLUMN_SELLER_MEMBER_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(sellerMemberId)});
            if (cursor.moveToFirst()) {
                average = cursor.getDouble(cursor.getColumnIndexOrThrow("avg_score"));
            }
        } catch (Exception e) {
            Log.e("review_seller_DB", "Error while calculating average review score", e);
            throw e;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return average;
    }

    /**
     * レビューのスコア一覧を取得するメソッド
     */
    public List<Integer> getReviewScores(String sellerMemberId) throws Exception {
        List<Integer> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + COLUMN_REVIEW_SCORE + " FROM " + DATABASE_TABLENAME +
                    " WHERE " + COLUMN_SELLER_MEMBER_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(sellerMemberId)});
            if (cursor.moveToFirst()) {
                do {
                    int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_SCORE));
                    scores.add(score);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("review_seller_DB", "Error fetching review scores", e);
            throw e;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return scores;
    }
}
