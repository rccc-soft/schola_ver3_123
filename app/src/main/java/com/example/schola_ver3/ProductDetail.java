package com.example.schola_ver3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {

    private Button evaluationbtn;
    private Button profilebtn;
    private Button buybtn;
    private Button chatbtn;

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView categoryTextView;
    private TextView priceTextView;
    private TextView deliveryMethodTextView;
    private TextView regionTextView;
    private TextView sellerIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        initializeViews();
        setupClickListeners();
        displayProductDetails();
    }

    private void initializeViews() {
        evaluationbtn = findViewById(R.id.evaluationbtn);
        profilebtn = findViewById(R.id.profilebtn);
        buybtn = findViewById(R.id.buybtn);
        chatbtn = findViewById(R.id.chatbtn);

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        categoryTextView = findViewById(R.id.detailcategoryTextView);
        priceTextView = findViewById(R.id.detailpriceTextView);
        deliveryMethodTextView = findViewById(R.id.detaildeliveryMethodTextView);
        regionTextView = findViewById(R.id.detailregionTextView);
        sellerIdTextView = findViewById(R.id.detailsellerIdTextView);
    }

    private void setupClickListeners() {
        evaluationbtn.setOnClickListener(this);
        profilebtn.setOnClickListener(this);
        buybtn.setOnClickListener(this);
        chatbtn.setOnClickListener(this);
    }

    private void displayProductDetails() {
        Intent intent = getIntent();

        byte[] imageData = intent.getByteArrayExtra("商品画像");
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            productImageView.setImageBitmap(bitmap);
        }

        productNameTextView.setText(intent.getStringExtra("商品名"));
        productDescriptionTextView.setText(intent.getStringExtra("商品説明"));
        categoryTextView.setText(intent.getStringExtra("カテゴリ"));

        String price = intent.getStringExtra("金額");
        if (price != null && !price.isEmpty()) {
            priceTextView.setText(price + "円");
        } else {
            priceTextView.setText("価格未設定");
        }

        deliveryMethodTextView.setText(intent.getStringExtra("配送方法"));
        regionTextView.setText(intent.getStringExtra("地域"));
        sellerIdTextView.setText(intent.getStringExtra("出品者ID"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.evaluationbtn) {
            // 出品者評価ボタンの処理
        } else if (v.getId() == R.id.profilebtn) {
            // 出品者プロフィール画面へ
        } else if (v.getId() == R.id.buybtn) {
            // 購入画面へ
        } else if (v.getId() == R.id.chatbtn) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
    }
}