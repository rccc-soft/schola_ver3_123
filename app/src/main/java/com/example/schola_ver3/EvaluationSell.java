package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.korekore.R;

public class EvaluationSell extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private Button veryGoodButton, goodButton, normalButton, badButton, veryBadButton, sendButton;
    private EditText messageInput;

    private ReviewManager reviewManager; // ReviewManager を使用
    private int sellerId;
    private int buyerId;
    private int reviewScore = 0; // 現在選択されている評価スコア

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_evaluation_0);

        // Database initialization via ReviewManager
        reviewManager = new ReviewManager(this);

        // Receive IDs via Intent
        sellerId = getIntent().getIntExtra("seller_member_id", -1);
        buyerId = getIntent().getIntExtra("buyer_member_id", -1);

        // UI elements init
        backButton = findViewById(R.id.imageButton2);
        veryGoodButton = findViewById(R.id.veryGoodButton);
        goodButton = findViewById(R.id.goodButton);
        normalButton = findViewById(R.id.normalButton);
        badButton = findViewById(R.id.badButton);
        veryBadButton = findViewById(R.id.veryBadButton);
        sendButton = findViewById(R.id.editButton);
        messageInput = findViewById(R.id.messageInput);

        // Set click listeners
        backButton.setOnClickListener(this);
        veryGoodButton.setOnClickListener(this);
        goodButton.setOnClickListener(this);
        normalButton.setOnClickListener(this);
        badButton.setOnClickListener(this);
        veryBadButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imageButton2) {
            // Go back
            finish();
        } else if (id == R.id.veryGoodButton) {
            reviewScore = 5;
            showToast("評価: 感動");
        } else if (id == R.id.goodButton) {
            reviewScore = 4;
            showToast("評価: 良い");
        } else if (id == R.id.normalButton) {
            reviewScore = 3;
            showToast("評価: 普通");
        } else if (id == R.id.badButton) {
            reviewScore = 2;
            showToast("評価: 悪い");
        } else if (id == R.id.veryBadButton) {
            reviewScore = 1;
            showToast("評価: 最悪");
        } else if (id == R.id.editButton) {
            submitReview();
            // Intentの起動は必要に応じて変更
            Intent intent = new Intent(getApplication(), ReviewListActivity.class);
            startActivity(intent);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void submitReview() {
        String message = messageInput.getText().toString();

        if (reviewScore == 0) {
            showToast("評価を選択してください");
            return;
        }

        if (message.isEmpty()) {
            showToast("メッセージを入力してください");
            return;
        }

        // Validate IDs
        if (sellerId == -1 || buyerId == -1) {
            showToast("ユーザー情報が見つかりません");
            return;
        }

        Log.d("EvaluationSell", "Submitting review: buyerId=" + buyerId + ", sellerId=" + sellerId + ", score=" + reviewScore + ", message=" + message);

        try {
            reviewManager.addReview(buyerId, sellerId, reviewScore, message);
            showToast("レビューが送信されました");
            // Clear the input
            messageInput.setText("");
            // 遷移またはアクティビティを終了
            finish();
        } catch (Exception e) {
            showToast("レビューの送信に失敗しました");
            Log.e("EvaluationSell", "Error adding review", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reviewManager != null) {
            reviewManager.close();
        }
    }
}
