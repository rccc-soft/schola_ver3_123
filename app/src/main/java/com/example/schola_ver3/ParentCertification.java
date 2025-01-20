package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ParentCertification extends AppCompatActivity {

    private EditText passwordEditText;
    private Button decideButton;
    private ImageButton backButton;
    private String memberId; // 保持する会員者ID
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_certification); // レイアウトファイルを設定

        Intent intent = getIntent();
        productId = intent.getStringExtra("商品ID");

        // SharedPreferences から会員者IDを取得
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");
        Log.d("ParentCertification", "Retrieved user_id from SharedPreferences: " + userId);

        // UIの初期化
        passwordEditText = findViewById(R.id.decideeditText);
        decideButton = findViewById(R.id.decideButton);
        backButton = findViewById(R.id.backButton);

        // 承認ボタンを最初は無効にする
        decideButton.setEnabled(false);

        // パスワード入力フィールドにTextWatcherを追加
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 入力内容が変更されたときに呼ばれる
                String password = s.toString().trim();
                if (password.isEmpty()) {
                    // パスワードが空の場合、ボタンを無効にする
                    decideButton.setEnabled(false);
                } else {
                    // パスワードが入力されている場合、ボタンを有効にする
                    decideButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 承認ボタンのクリックリスナー
        decideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString().trim(); // 入力されたパスワード

                if (password.isEmpty()) {
                    Toast.makeText(ParentCertification.this, "パスワードを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 保護者テーブルから会員者IDとパスワードを照合
                DatabaseHelper dbHelper = new DatabaseHelper(ParentCertification.this);

                // user_id と member_id を照合
                if (dbHelper.validateParentCredentials(userId, password)) {
                    // 条件が満たされた場合、Login.javaに遷移
                    Log.d("ParentCertification", "Validation successful. Proceeding to Login activity.");
                    Intent intent = new Intent(ParentCertification.this, Buy.class);
                    intent.putExtra("商品ID", productId);
                    startActivity(intent);
                    finish(); // 現在の画面を閉じる
                } else {
                    Log.d("ParentCertification", "Validation failed. Password or user_id is incorrect.");
                    Toast.makeText(ParentCertification.this, "パスワードが一致しません", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 戻るボタンのクリックリスナー
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 前の画面に戻る
            }
        });
    }
}