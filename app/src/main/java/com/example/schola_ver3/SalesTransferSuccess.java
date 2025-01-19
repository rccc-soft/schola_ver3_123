package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

// FAQRequestから
public class SalesTransferSuccess extends AppCompatActivity implements View.OnClickListener {
    private Button homeButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proceeds_transfer_completion);

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // マイページ画面へ
        if (v.getId() == R.id.homeButton) {
            Intent intent = new Intent(getApplication(), MyPage.class);
            startActivity(intent);
        }
    }
}
