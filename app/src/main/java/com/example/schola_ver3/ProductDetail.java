package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {

    private Button evaluationbtn;
    private Button profilebtn;
    private Button buybtn;
    private Button chatbtn;

    private TextView productnametextview;
    private TextView productDescriptiontextview;
    private TextView categorytextview;
    private TextView pricetextview;
    private TextView deliverymethodtextview;
    private TextView datetextview;
    private TextView regiontextview;
    private TextView selleridtextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        // ボタンの初期化
        evaluationbtn = findViewById(R.id.evaluationbtn);
        evaluationbtn.setOnClickListener(this);
        profilebtn = findViewById(R.id.profilebtn);
        profilebtn.setOnClickListener(this);
        buybtn = findViewById(R.id.buybtn);
        buybtn.setOnClickListener(this);
        chatbtn = findViewById(R.id.chatbtn);
        chatbtn.setOnClickListener(this);

        // 商品情報の初期化
        productnametextview = findViewById(R.id.productNameTextView);
        productDescriptiontextview = findViewById(R.id.productDescriptionTextView);
        categorytextview = findViewById(R.id.categoryTextview);
        pricetextview = findViewById(R.id.priceTexview);
        deliverymethodtextview = findViewById(R.id.deliveryMethodTexview);
        datetextview = findViewById(R.id.dateTexview);
        regiontextview = findViewById(R.id.regionTexview);
        selleridtextview = findViewById(R.id.sellerIDTexview);

        // Intentからデータを取得して表示
        Intent intent = getIntent();
        productnametextview.setText(intent.getStringExtra("productName"));
        productDescriptiontextview.setText(intent.getStringExtra("productDescription"));
        categorytextview.setText(intent.getStringExtra("category"));
        pricetextview.setText(intent.getStringExtra("productPrice") + "円");
        deliverymethodtextview.setText(intent.getStringExtra("deliveryMethod"));
        datetextview.setText(intent.getStringExtra("date"));
        regiontextview.setText(intent.getStringExtra("region"));
        selleridtextview.setText(intent.getStringExtra("sellerId"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.evaluationbtn) {
            // 出品者評価ボタンの処理
        } else if (v.getId() == R.id.profilebtn) {
            // 出品者プロフィール画面へ
//            Intent intent = new Intent(this, SellerProfile.class); // 遷移先のクラス名を指定
//            startActivity(intent);
        } else if (v.getId() == R.id.buybtn) {
            // 購入画面へ
//            Intent intent = new Intent(this, Purchase.class); // 遷移先のクラス名を指定
//            startActivity(intent);
        } else if (v.getId() == R.id.chatbtn) {
            // チャット画面へ
//            Intent intent = new Intent(this, Chat.class); // 遷移先のクラス名を指定
//            startActivity(intent);
            Intent intent = new Intent(this, TestActivity.class); // 遷移先のクラス名を指定
            startActivity(intent);
        }
    }
}