package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

// FAQRequestから
public class ExhibitShiped extends AppCompatActivity implements View.OnClickListener {
    private Button homeButton;

    private PurchaseDatabaseHelper dbHelper; // 購入テーブル（仮）
    private int purchaseId; // 購入ID（仮）

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exhibit_shiped);

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);

        // DBヘルパーの初期化
        dbHelper = new PurchaseDatabaseHelper(this); // 購入テーブル（仮）

        // Intent から購入IDを受け取る
        Intent intent = getIntent();
        purchaseId = intent.getIntExtra("purchase_id", -1);

        // dbHelper が null でないことを確認
        if (dbHelper != null) {
            dbHelper.updateItemShippedStatus(purchaseId); // purchase_id の配送状況を更新
        } else {
            Log.e("ExhibitShiped", "PurchaseDatabaseHelper is null");
        }
    }

    @Override
    public void onClick(View v) {
        // ホーム画面へ
        if (v.getId() == R.id.homeButton) {
            Intent intent = new Intent(getApplication(), HomePage.class);
            startActivity(intent);
        }
    }
}
