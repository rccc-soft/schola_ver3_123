package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.korekore.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private ProgressBar progressBar;
    private Button buttonBackToMain;

    private ReviewManager reviewManager;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

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
                // 既に MainActivity がスタックに存在する場合は再起動を防ぐ
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                // 現在のアクティビティを終了
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // アクティビティがフォアグラウンドに戻った際にレビューを再取得
        fetchReviews();
    }

    private void fetchReviews() {
        // ProgressBarを表示
        progressBar.setVisibility(View.VISIBLE);

        executorService.execute(() -> {
            try {
                // 全レビューを取得
                List<Review> reviews = reviewManager.getAllReviews();

                // UIスレッドでRecyclerViewを更新
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (reviews != null && !reviews.isEmpty()) {
                        reviewAdapter.setReviews(reviews);
                    } else {
                        Toast.makeText(ReviewListActivity.this, "レビューが存在しません。", Toast.LENGTH_SHORT).show();
                        reviewAdapter.setReviews(null); // 空リストに設定
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
