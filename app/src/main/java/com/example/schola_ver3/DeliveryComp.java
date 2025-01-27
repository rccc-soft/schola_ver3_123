package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class DeliveryComp extends AppCompatActivity implements View.OnClickListener {
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_comp);

        // loginButtonの初期化とクリックリスナーの設定
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this); // 修正：正しい匿名クラスの実装

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DeliveryComp.this, LoginH.class); // 修正：正しいコンテキスト参照
        startActivity(intent);
    }
}
