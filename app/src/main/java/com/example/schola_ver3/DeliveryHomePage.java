package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schola_ver3.DeliverySearch;

public class DeliveryHomePage extends AppCompatActivity {

    private Button searchButton, categoryButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_homepage); // activity_delivery_home_page.xml をレイアウトとして設定

        // ボタンの初期化
        searchButton = findViewById(R.id.searchbtn);
        categoryButton = findViewById(R.id.categorybtn);
        logoutButton = findViewById(R.id.category);

        // 検索ボタンのクリックリスナー
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LoginH.java に画面遷移
                Intent intent = new Intent(DeliveryHomePage.this, DeliverySearch.class);
                startActivity(intent);
                finish(); // 現在の画面を終了
            }
        });

        // 会員情報ボタンのクリックリスナー
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LoginH.java に画面遷移
                Intent intent = new Intent(DeliveryHomePage.this, DeliveryView.class);
                startActivity(intent);
                finish(); // 現在の画面を終了
            }
        });

        // ログアウトボタンのクリックリスナー
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LoginH.java に画面遷移
                Intent intent = new Intent(DeliveryHomePage.this, LoginH.class);
                startActivity(intent);
                finish(); // 現在の画面を終了
            }
        });
    }
}