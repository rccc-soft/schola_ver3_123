package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Boot extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // onCreate内でSharedPreferencesを取得
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");

        // ログイン状態に応じて遷移先を決定
        if (userId != null && !userId.isEmpty()) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        // 現在のアクティビティを終了
        finish();
    }
}