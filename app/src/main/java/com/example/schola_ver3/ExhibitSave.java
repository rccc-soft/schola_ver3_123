package com.example.schola_ver3;

import android.content.Intent;
import android.graphics.BitmapFactory; // BitmapFactory をインポート
import android.net.Uri; // Uri をインポート
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView; // ImageView をインポート
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ExhibitSave extends AppCompatActivity implements View.OnClickListener {
    private Button yesbtn;
    private Button nobtn;
    private ImageView productImageView; // ImageView を追加
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView, categoryTextView, deliveryMethodTextView, regionTextView;

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

        productImageView = findViewById(R.id.productImageView); // ImageView の初期化
        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        deliveryMethodTextView = findViewById(R.id.deliveryMethodTextView);
        regionTextView = findViewById(R.id.regionTextView);
    }

    private void displayProductInfo() {
        Intent intent = getIntent();

        // 画像の取得
        String uriString = intent.getStringExtra("productImageUri");
        if (uriString != null) {
            Uri imageUri = Uri.parse(uriString);
            productImageView.setImageURI(imageUri);
        }

        productNameTextView.setText("商品名: " + intent.getStringExtra("productName"));
        productDescriptionTextView.setText("説明: " + intent.getStringExtra("productDescription"));
        productPriceTextView.setText("価格: " + intent.getStringExtra("productPrice") + "円");
        categoryTextView.setText("カテゴリ: " + intent.getStringExtra("category"));
        deliveryMethodTextView.setText("配送方法: " + intent.getStringExtra("deliveryMethod"));
        regionTextView.setText("地域: " + intent.getStringExtra("region"));
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