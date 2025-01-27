package com.example.schola_ver3;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.  ;

public class EvaluationSell extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CONFIRMATION = 1;
    private ActivityResultLauncher<Intent> confirmationLauncher;
    private String tempMessage;

    private ImageButton backButton;
    private Button veryGoodButton, goodButton, normalButton, badButton, veryBadButton, sendButton;
    private EditText messageInput;

    private ReviewManager reviewManager; // ReviewManager を使用
//    private String sellerId;
//    private String buyerId;
    private int reviewScore = 0; // 現在選択されている評価スコア

    private String productId;
    private String sellerId;
    private String buyerId;
    private String temporaryProductId;
    private String temporarySellerId;
    private String temporaryBuyerId;

    private ProductDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_evaluation_0);

        // Database initialization via ReviewManager
        reviewManager = new ReviewManager(this);

        // Receive IDs via Intent
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        buyerId = sharedPreferences.getString("user_id", "");
//        SharedPreferences prefs = getSharedPreferences("ProductPrefs", MODE_PRIVATE);
//        productId = prefs.getString("product_id", "");
        // Intent から productId, sellerId, buyerId を受け取る
        Intent intent = getIntent();
        temporaryProductId = intent.getStringExtra("product_id");
        temporarySellerId = intent.getStringExtra("seller_member_id");
        temporaryBuyerId = intent.getStringExtra("buyer_member_id");

        dbHelper = new ProductDatabaseHelper(this);
        setDeliveryMethod();

        if (sellerId != null) {
            // 出品者IDが見つかった場合の処理
//            System.out.println("出品者ID: " + sellerId);
            System.out.println("出品者ID: " + temporarySellerId);
        } else {
            // 出品者IDが見つからなかった場合の処理
            System.out.println("該当する商品が見つかりません");
        }
//        sellerId = getIntent().getIntExtra("seller_member_id", -1);
//        buyerId = getIntent().getIntExtra("buyer_member_id", -1);

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

        setupConfirmationLauncher();
    }


    private void setupConfirmationLauncher() {
        confirmationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // ユーザーが確認した場合
//                        submitReview();
//                        Intent intent = new Intent(getApplication(), SalesTransferSuccess.class);
//                        startActivity(intent);
                        saveReviewAndNavigateToSuccess();
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        // ユーザーがキャンセルした場合
                        // 何もしない（現在の画面にとどまる）
                    }
                }
        );
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
//            // Intentの起動は必要に応じて変更
//            Intent intent = new Intent(getApplication(), EvaluationSellSend.class);
//            startActivity(intent);
            prepareReviewSubmission();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void prepareReviewSubmission() {
        tempMessage = messageInput.getText().toString();

        if (reviewScore == 0) {
            showToast("評価を選択してください");
            return;
        }

        if (tempMessage.isEmpty()) {
            showToast("メッセージを入力してください");
            return;
        }

        // 確認画面を表示
        Intent confirmIntent = new Intent(this, EvaluationSellSend.class);
        confirmationLauncher.launch(confirmIntent);
    }

    private void saveReviewAndNavigateToSuccess() {
        try {
//            reviewManager.addReview(buyerId, sellerId, reviewScore, tempMessage);
            reviewManager.addReview(temporaryBuyerId, temporarySellerId, reviewScore, tempMessage);
            showToast("レビューが送信されました");

            // ホーム画面に遷移
            Intent intent = new Intent(this, EvaluationSellSuccess.class);
            intent.putExtra("product_id", temporaryProductId); // productIdを渡す
            startActivity(intent);
            finish();
        } catch (Exception e) {
            showToast("レビューの送信に失敗しました: " + e.getMessage());
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

    private void setDeliveryMethod() {
        String productId = getIntent().getStringExtra("商品ID");
        if (productId == null) {
            Log.e("EvaluationSell", "setDeliveryMethod: productId is null");
            return;
        }
        Cursor cursor = dbHelper.getProductInfo(productId);
        if (cursor != null && cursor.moveToFirst()) {
            // 既存のコード
        } else {
            Log.e("EvaluationSell", "setDeliveryMethod: No product found for ID " + productId);
        }
    }
}
