package com.example.schola_ver3;

import static android.app.DownloadManager.COLUMN_ID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.korekore.R;

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

    private int userAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        getage();
        SharedPreferences prefs = getSharedPreferences("ProductPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("productId", COLUMN_ID);
        editor.apply();

        initializeViews();
        setupClickListeners();
        displayProductDetails();
    }

    private void getage() {
//// データベースヘルパーのインスタンスを作成
//        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//// ユーザIDが一致するレコードを検索
//        String[] projection = {"old"};
//        String selection = "userId = ?";
//        String[] selectionArgs = {currentUserId};
//
//        Cursor cursor = db.query(
//                "ユーザテーブル",
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//
//        userAge = "";
//        if (cursor.moveToFirst()) {
//            userAge = cursor.getString(cursor.getColumnIndexOrThrow("old"));
//        }
//
//        cursor.close();
//        db.close();

    }

    private void initializeViews() {
        evaluationbtn = findViewById(R.id.evaluationbtn);
        profilebtn = findViewById(R.id.profilebtn);
        buybtn = findViewById(R.id.buybtn);
        chatbtn = findViewById(R.id.chatbtn);

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        categoryTextView = findViewById(R.id.categoryTextView); // 修正
        priceTextView = findViewById(R.id.priceTextView); // 修正
        deliveryMethodTextView = findViewById(R.id.deliveryMethodTextView); // 修正
        regionTextView = findViewById(R.id.regionTextView); // 修正
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
        categoryTextView.setText("カテゴリ: " + intent.getStringExtra("カテゴリ"));

        String price = intent.getStringExtra("金額");
        if (price != null && !price.isEmpty()) {
            priceTextView.setText("価格: " + price + "円");
        } else {
            priceTextView.setText("価格未設定");
        }

        deliveryMethodTextView.setText("配送方法: " + intent.getStringExtra("配送方法"));

        String regionValue = intent.getStringExtra("地域");
        if (regionValue != null && !regionValue.isEmpty()) {
            try {
                int regionIndex = Integer.parseInt(regionValue) - 1;
                String[] regions = getResources().getStringArray(R.array.region_array);
                if (regionIndex >= 0 && regionIndex < regions.length) {
                    regionTextView.setText("地域: " + regions[regionIndex]);
                } else {
                    regionTextView.setText("地域: 不明");
                }
            } catch (NumberFormatException e) {
                regionTextView.setText("地域: " + regionValue);
            }
        } else {
            regionTextView.setText("地域: 未設定");
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.evaluationbtn) {
            // 出品者評価ボタンの処理
        } else if (v.getId() == R.id.profilebtn) {
            // 出品者プロフィール画面へ
        } else if (v.getId() == R.id.buybtn) {
            // 購入画面へ
//            if (userAge <= 15) {
//                Intent intent = new Intent(this, ParentCertification.class);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(this, Buy.class);
//                startActivity(intent);
//            }
        } else if (v.getId() == R.id.chatbtn) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
    }
}