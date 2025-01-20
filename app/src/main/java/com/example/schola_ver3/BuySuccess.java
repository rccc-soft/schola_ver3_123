package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.korekore.R;

public class BuySuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_completion); // activity_buy_complete.xml をレイアウトとして設定

        // ホームボタンの参照を取得
        Button homeButton = findViewById(R.id.homeButton);

        // ホームボタンのクリックリスナーを設定
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomePage.java に画面遷移
                Intent intent = new Intent(BuySuccess.this, HomePage.class);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        });
    }
}