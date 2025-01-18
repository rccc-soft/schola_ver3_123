package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BuyCheck extends AppCompatActivity {

    private Button buyNoButton;
    private Button buyYesButton;
    private String paymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_confirmation);

        buyNoButton = findViewById(R.id.buyNoButton);
        buyYesButton = findViewById(R.id.buyYesButton);

        // Buy.javaから支払い方法を取得
        paymentMethod = getIntent().getStringExtra("paymentMethod");

        buyNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        buyYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("paymentMethod", paymentMethod);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}