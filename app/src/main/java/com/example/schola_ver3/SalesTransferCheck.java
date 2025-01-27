package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//SalesTransferから
public class SalesTransferCheck extends AppCompatActivity implements View.OnClickListener{
    private Button yesButton;
    private Button noButton;
    private int updatedAmount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proceeds_transfer_confirmation);

        updatedAmount = getIntent().getIntExtra("updatedAmount", 0);

        yesButton = findViewById(R.id.yesButton);
        yesButton.setOnClickListener(this);

        noButton = findViewById(R.id.noButton);
        noButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent resultIntent = new Intent();
        if (v.getId() == R.id.yesButton) {
            resultIntent.putExtra("updatedAmount", updatedAmount);
            setResult(RESULT_OK, resultIntent);
        } else if (v.getId() == R.id.noButton) {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

}
