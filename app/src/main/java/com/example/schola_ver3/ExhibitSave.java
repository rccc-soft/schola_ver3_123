package com.example.schola_ver3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ExhibitSave extends AppCompatActivity implements View.OnClickListener {
    private Button yesbtn;
    private Button nobtn;
    private TextView productInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exhibit_save);

        initializeViews();
        displayProductInfo();
    }

    private void initializeViews() {
        yesbtn = findViewById(R.id.yesbtn);
        yesbtn.setOnClickListener(this);
        nobtn = findViewById(R.id.nobtn);
        nobtn.setOnClickListener(this);
        productInfoTextView = findViewById(R.id.productInfoTextView);
    }

    private void displayProductInfo() {
        Intent intent = getIntent();
        String productInfo = "商品名: " + intent.getStringExtra("productName") + "\n" +
                "説明: " + intent.getStringExtra("productDescription") + "\n" +
                "価格: " + intent.getStringExtra("productPrice") + "円\n" +
                "カテゴリ: " + intent.getStringExtra("category") + "\n" +
                "配送方法: " + intent.getStringExtra("deliveryMethod") + "\n" +
                "地域: " + intent.getStringExtra("region");
        productInfoTextView.setText(productInfo);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.yesbtn) {
            setResultAndFinish(RESULT_OK);
        } else if (v.getId() == R.id.nobtn) {
            setResultAndFinish(RESULT_CANCELED);
        }
    }

    private void setResultAndFinish(int resultCode) {
        setResult(resultCode);
        finish();
    }
}