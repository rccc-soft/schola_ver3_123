package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//SalesTransferから
public class SalesTransferCheckActivity extends AppCompatActivity implements View.OnClickListener{
    private Button yesButton;
    private Button noButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proceeds_transfer_confirmation);

        yesButton = findViewById(R.id.yesButton);
        yesButton.setOnClickListener(this);

        noButton = findViewById(R.id.noButton);
        noButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.yesButton) {
            // 結果をセットして終了
            setResult(RESULT_OK); //振込完了画面へ
            finish();
        } else if (v.getId() == R.id.noButton) {
            // 結果をセットして終了
            setResult(RESULT_CANCELED); //振込画面へ
            finish();
        }
    }
}
