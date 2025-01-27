package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MyPage extends AppCompatActivity implements View.OnClickListener{

    private ConstraintLayout constraintLayout;

    // ImageButtonとして宣言するビュー
    private ImageButton backbtn; // バックボタン
    private ImageButton homebtn;
    private ImageButton searchbtn;
    private ImageButton exhibitbtn;
    private ImageButton favoritebtn;
    private ImageButton profilebtn;

    // Buttonとして宣言するビュー
    private Button exhibitlistbtn;
    private Button purchasedbtn;
    private Button favoritebtn2;
    private Button salestransfer;
    private Button faqbtn;
    private Button logoutbtn;
    private Button settingbtn;

    // 星を表すTextView
    private TextView star1;
    private TextView star2;
    private TextView star3;
    private TextView star4;
    private TextView star5;

    // 評価平均を表示するTextView
    private TextView averageReviewTextView;
    private TextView reviewScoresTextView;

    // EvaluationViewのインスタンス
    private EvaluationView evaluationView;
    private int sellerId;

    private TextView usernameTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mypage);
        //  sellerId = getIntent().getIntExtra("seller_member_id", -1);

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(this);

        // ImageButtonの初期化とリスナー設定
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);

        homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(this);

        searchbtn = findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(this);

        purchasedbtn = findViewById(R.id.purchasedbtn);
        purchasedbtn.setOnClickListener(this);

        favoritebtn = findViewById(R.id.favoritebtn);
        favoritebtn.setOnClickListener(this);

        profilebtn = findViewById(R.id.profilebtn);
        profilebtn.setOnClickListener(this);

        // Buttonの初期化とリスナー設定
        exhibitbtn = findViewById(R.id.exhibitbtn);
        exhibitbtn.setOnClickListener(this);

        settingbtn = findViewById(R.id.settingbtn);
        settingbtn.setOnClickListener(this);

        logoutbtn = findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(this);

        faqbtn = findViewById(R.id.faqbtn);
        faqbtn.setOnClickListener(this);

        exhibitlistbtn = findViewById(R.id.exhibitlistbtn);
        exhibitlistbtn.setOnClickListener(this);

        favoritebtn2 = findViewById(R.id.favoritebtn2);
        favoritebtn2.setOnClickListener(this);

        salestransfer = findViewById(R.id.salestransfer);
        salestransfer.setOnClickListener(this);

        // 星を表すTextViewの初期化
        star1 = findViewById(R.id.textView111);
        star2 = findViewById(R.id.textView112);
        star3 = findViewById(R.id.textView113);
        star4 = findViewById(R.id.textView114);
        star5 = findViewById(R.id.textView115);

        // 評価平均を表示するTextViewの初期化
        averageReviewTextView = findViewById(R.id.textView6); // レイアウト内の適切なIDに変更
        reviewScoresTextView = findViewById(R.id.textView7); // 必要に応じて設定

        // EvaluationViewのインスタンス作成
        evaluationView = new EvaluationView(this);

        usernameTextView = findViewById(R.id.textView); // XMLのusernameを表示するTextView
        dbHelper = new DatabaseHelper(this);

        // ユーザーのmemberIDを取得（Intentから取得）
//        int userMemberId = getIntent().getIntExtra("seller_member_id", 1); // 初期値を1に設定
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userMemberId = prefs.getString("user_id", "");

        TextView salesAmountTextView = findViewById(R.id.textView6); // 売上金額表示用

        if (!userMemberId.isEmpty()) {
            // 売上金額を取得
            int salesAmount = dbHelper.getSalesAmount(userMemberId);
            // 売上金額を TextView にセット
            salesAmountTextView.setText(String.valueOf(salesAmount));
            Log.d("MyPage", "Sales Amount retrieved: " + salesAmount);
        } else {
            salesAmountTextView.setText("0"); // デフォルト値を設定
            Log.e("MyPage", "User ID not found in SharedPreferences");
        }


        if (!userMemberId.isEmpty()) {
            displayUsername(userMemberId);
        } else {
            usernameTextView.setText("ユーザー情報が見つかりません");
        }

        if (userMemberId != null) { // 初期値を1に設定しているため、この条件は基本的に常にtrue
            // 評価データを非同期で取得
            evaluationView.fetchAndDisplayReviewScore(userMemberId, new EvaluationView.ReviewScoreCallback() {
                @Override
                public void onScoreFetched(double averageScore, int roundedScore, List<Integer> reviewScores) {
                    runOnUiThread(() -> {
                        setStarColors(roundedScore);
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MyPage.this, "評価の取得に失敗しました。", Toast.LENGTH_LONG).show();
                        setStarColors(0); // 星を全て灰色に
                        averageReviewTextView.setText("評価平均: エラー");
                        reviewScoresTextView.setText("");
                    });
                    Log.e("MyPage", "Error fetching review score", e);
                }
            });
        } else {
            // UIバグ引き起こしポイント
            averageReviewTextView.setText("ユーザーのmemberIDが取得できませんでした。");
            reviewScoresTextView.setText("");
        }

        // EdgeToEdgeの設定
        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 必要に応じてデータベースにサンプルデータを追加（テスト用）
        // evaluationView.addSampleData(); // EvaluationViewクラスで実装していない場合はコメントアウト
    }

    private void displayUsername(String userMemberId) {
        Cursor cursor = dbHelper.getMemberInfo(userMemberId);
        if (cursor != null && cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            if (nameColumnIndex != -1) {
                String username = cursor.getString(nameColumnIndex);
                usernameTextView.setText(username);
            } else {
                usernameTextView.setText("ユーザー名が見つかりません");
            }
            cursor.close();
        } else {
            usernameTextView.setText("ユーザー情報が見つかりません");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.backbtn){
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        } else if (id == R.id.homebtn) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        } else if (id == R.id.searchbtn) {
            Intent intent = new Intent(this, ProductSearch.class);
            startActivity(intent);
        } else if (id == R.id.exhibitbtn) {
            Intent intent = new Intent(this, Exhibit.class);
            startActivity(intent);
        } else if (id == R.id.favoritebtn || id == R.id.favoritebtn2) {
            Intent intent = new Intent(getApplication(), FavoriteList.class);
            startActivity(intent);
        } else if (id == R.id.profilebtn) {
//            Intent intent = new Intent(this, ProfileView.class);
//            startActivity(intent);
        } else if (id == R.id.exhibitlistbtn) {
            Intent intent = new Intent(this, ExhibitList.class);
            startActivity(intent);
        } else if (id == R.id.purchasedbtn) {
            Intent intent = new Intent(this, PurchasedList.class);
            startActivity(intent);
        } else if (id == R.id.salestransfer) {
            Intent intent = new Intent(this, AccountInfomation.class);
            startActivity(intent);
        } else if (id == R.id.faqbtn) {
//            Intent intent = new Intent(this, .class);
//            startActivity(intent);
        } else if (id == R.id.logoutbtn) {
            Intent intent = new Intent(this, Logoutsub.class);
            startActivity(intent);
        } else if (id == R.id.settingbtn) {
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
        } else if (id == R.id.constraintLayout) {
            Intent intent = new Intent(this, ReviewListActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 評価スコアに基づいて星の色を設定するメソッド
     *
     * @param score 評価スコア（1〜5）
     */
    private void setStarColors(int score) {
        // 色の取得
        int yellow = getResources().getColor(R.color.yellow);
        int gray = getResources().getColor(R.color.gray);

        // 全ての星を灰色にリセット
        star1.setTextColor(gray);
        star2.setTextColor(gray);
        star3.setTextColor(gray);
        star4.setTextColor(gray);
        star5.setTextColor(gray);

        // 評価スコアに応じて星を黄色に変更
        if (score >= 1) {
            star1.setTextColor(yellow);
        }
        if (score >= 2) {
            star2.setTextColor(yellow);
        }
        if (score >= 3) {
            star3.setTextColor(yellow);
        }
        if (score >= 4) {
            star4.setTextColor(yellow);
        }
        if (score >= 5) {
            star5.setTextColor(yellow);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EvaluationViewのExecutorServiceをシャットダウン
        if (evaluationView != null) {
            evaluationView.shutdown();
        }
    }
}
