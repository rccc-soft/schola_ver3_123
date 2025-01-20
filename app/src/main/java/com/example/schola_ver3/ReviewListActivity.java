package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private ProgressBar progressBar;
    private Button buttonBackToMain;

    private ReviewManager reviewManager;
    private ExecutorService executorService;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        // 現在のユーザーIDを取得
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");

        // UI要素の初期化
        recyclerView = findViewById(R.id.recyclerViewReviews);
        progressBar = findViewById(R.id.progressBar);
        buttonBackToMain = findViewById(R.id.buttonBackToMain);

        // RecyclerViewの設定
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter();
        recyclerView.setAdapter(reviewAdapter);

        // ReviewManagerとExecutorServiceの初期化
        reviewManager = new ReviewManager(this);
        executorService = Executors.newSingleThreadExecutor();

        // レビュー一覧の取得
        fetchReviews();

        // 戻るボタンのクリックリスナーを設定
        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewListActivity.this, MainActivity_egu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchReviews();
    }

    private void fetchReviews() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d("ReviewListActivity", "Fetching reviews for user ID: " + userId);

        executorService.execute(() -> {
            try {
                List<Review> allReviews = reviewManager.getAllReviews();
                Log.d("ReviewListActivity", "Total reviews fetched: " + allReviews.size());
                List<Review> filteredReviews = new ArrayList<>();
                for (Review review : allReviews) {
                    Log.d("ReviewListActivity", "Review seller ID: " + review.getSellerMemberId());
                    if (String.valueOf(review.getSellerMemberId()).equals(userId)) {
                        filteredReviews.add(review);
                    }
                }
                Log.d("ReviewListActivity", "Filtered reviews: " + filteredReviews.size());

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (!filteredReviews.isEmpty()) {
                        reviewAdapter.setReviews(filteredReviews);
                    } else {
                        Toast.makeText(ReviewListActivity.this, "表示するレビューがありません", Toast.LENGTH_SHORT).show();
                        reviewAdapter.setReviews(null);
                    }
                });
            } catch (Exception e) {
                Log.e("ReviewListActivity", "Error fetching reviews", e);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ReviewListActivity.this, "レビューの取得に失敗しました。", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reviewManager != null) {
            reviewManager.close();
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
//
//public class ReviewListActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ReviewAdapter reviewAdapter;
//    private ProgressBar progressBar;
//    private Button buttonBackToMain;
//
//    private ReviewManager reviewManager;
//    private ExecutorService executorService;
//    private String userId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_review_list);
//
//        // ユーザーIDの取得
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        userId = sharedPreferences.getString("user_id", "");
//
//        // UI要素の初期化
//        recyclerView = findViewById(R.id.recyclerViewReviews);
//        progressBar = findViewById(R.id.progressBar);
//        buttonBackToMain = findViewById(R.id.buttonBackToMain);
//
//        // RecyclerViewの設定
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        reviewAdapter = new ReviewAdapter();
//        recyclerView.setAdapter(reviewAdapter);
//
//        // ReviewManagerとExecutorServiceの初期化
//        reviewManager = new ReviewManager(this);
//        executorService = Executors.newSingleThreadExecutor();
//
//        // レビュー一覧の取得
//        fetchReviews();
//
//        // 戻るボタンのクリックリスナーを設定
//        buttonBackToMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ReviewListActivity.this, MainActivity_egu.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        fetchReviews();
//    }
//
//    private void fetchReviews() {
//        progressBar.setVisibility(View.VISIBLE);
//
//        executorService.execute(() -> {
//            try {
//                List<Review> allReviews = reviewManager.getAllReviews();
//                List<Review> filteredReviews = new ArrayList<>();
//
//                // 現在のユーザーIDと一致する出品者IDのレビューのみをフィルタリング
//                for (Review review : allReviews) {
//                    if (String.valueOf(review.getSellerMemberId()).equals(userId)) {
//                        filteredReviews.add(review);
//                    }
//                }
//
//                runOnUiThread(() -> {
//                    progressBar.setVisibility(View.GONE);
//                    if (!filteredReviews.isEmpty()) {
//                        reviewAdapter.setReviews(filteredReviews);
//                    } else {
//                        Toast.makeText(ReviewListActivity.this, "表示できるレビューがありません。", Toast.LENGTH_SHORT).show();
//                        reviewAdapter.setReviews(null);
//                    }
//                });
//            } catch (Exception e) {
//                Log.e("ReviewListActivity", "Error fetching reviews", e);
//                runOnUiThread(() -> {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(ReviewListActivity.this, "レビューの取得に失敗しました。", Toast.LENGTH_SHORT).show();
//                });
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (reviewManager != null) {
//            reviewManager.close();
//        }
//        if (executorService != null && !executorService.isShutdown()) {
//            executorService.shutdown();
//        }
//    }
//}