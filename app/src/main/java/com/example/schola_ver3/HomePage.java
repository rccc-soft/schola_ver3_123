package com.example.schola_ver3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    private ProductDatabaseHelper dbHelper;

    private EditText editTextText;
    private ImageView home_homebtn;
    private ImageView home_searchbtn;
    private ImageView home_exhibitbtn;
    private ImageView home_favobtn;
    private ImageView home_mypagebtn;
    private ImageButton noticeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        editTextText = findViewById(R.id.editTextText);
        editTextText.setOnClickListener(this);

        home_homebtn = findViewById(R.id.home_homebtn);
        home_homebtn.setOnClickListener(this);

        home_searchbtn = findViewById(R.id.home_searchbtn);
        home_searchbtn.setOnClickListener(this);

        home_exhibitbtn = findViewById(R.id.home_exhibitbtn);
        home_exhibitbtn.setOnClickListener(this);

        home_favobtn = findViewById(R.id.home_favobtn);
        home_favobtn.setOnClickListener(this);

        home_mypagebtn = findViewById(R.id.home_mypagebtn);
        home_mypagebtn.setOnClickListener(this);

        noticeButton = findViewById(R.id.noticeButton);
        noticeButton.setOnClickListener(this);

        dbHelper = new ProductDatabaseHelper(this);

        displayRandomProducts();
    }

    private void displayRandomProducts() {
        LinearLayout layout1 = findViewById(R.id.productRow1);
        LinearLayout layout2 = findViewById(R.id.productRow2);
        LinearLayout layout3 = findViewById(R.id.productRow3);
        LinearLayout layout4 = findViewById(R.id.productRow4);
        LinearLayout layout5 = findViewById(R.id.productRow5);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"商品ID", "商品名", "商品画像", "金額"};

        Cursor cursor = db.query("商品テーブル", projection, null, null, null, null, null);

        ArrayList<HashMap<String, Object>> products = new ArrayList<>();

        while (cursor.moveToNext()) {
            HashMap<String, Object> product = new HashMap<>();
            product.put("商品ID", cursor.getString(cursor.getColumnIndex("商品ID")));
            product.put("商品名", cursor.getString(cursor.getColumnIndex("商品名")));
            product.put("金額", cursor.getString(cursor.getColumnIndex("金額")));
            product.put("商品画像", cursor.getBlob(cursor.getColumnIndex("商品画像")));
            products.add(product);
        }

        cursor.close();

        Random random = new Random();
        int productCount = Math.min(products.size(), 10);
        ArrayList<HashMap<String, Object>> selectedProducts = new ArrayList<>();

        while (selectedProducts.size() < productCount) {
            int randomIndex = random.nextInt(products.size());
            HashMap<String, Object> randomProduct = products.get(randomIndex);

            if (!selectedProducts.contains(randomProduct)) {
                selectedProducts.add(randomProduct);
            }
        }

        for (int i = 0; i < selectedProducts.size(); i++) {
            HashMap<String, Object> product = selectedProducts.get(i);
            View productView = getLayoutInflater().inflate(R.layout.product_item, null);

            ImageView imageView = productView.findViewById(R.id.imageView1);
            TextView nameTextView = productView.findViewById(R.id.textView1);
            TextView priceTextView = productView.findViewById(R.id.textView5);
            TextView starTextView = productView.findViewById(R.id.textView9);

            nameTextView.setText((String) product.get("商品名"));
            priceTextView.setText("￥" + (String) product.get("金額"));

            byte[] imageData = (byte[]) product.get("商品画像");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);

            String productId = (String) product.get("商品ID");
            productView.setOnClickListener(v -> navigateToProductDetail(product));

            starTextView.setOnClickListener(v -> {
                // お気に入りボタンが押されたときの処理
                Toast.makeText(HomePage.this, "お気に入りに追加しました", Toast.LENGTH_SHORT).show();
                // ここにお気に入り追加のロジックを実装
            });

            if (i == 0 || i == 1) {
                layout1.addView(productView);
            } else if (i == 2 || i == 3){
                layout2.addView(productView);
            } else if (i == 4 || i == 5){
                layout3.addView(productView);
            } else if (i == 6 || i == 7){
                layout4.addView(productView);
            } else if (i == 8 || i == 9){
                layout5.addView(productView);
            }
        }
    }

    private void navigateToProductDetail(HashMap<String, Object> product) {
        Intent intent = new Intent(this, ProductDetail.class);
        String productId = (String) product.get("商品ID");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "商品ID", "商品名", "商品説明", "商品画像", "カテゴリ", "金額", "配送方法", "出品日時", "地域", "出品者ID"
        };
        String selection = "商品ID = ?";
        String[] selectionArgs = {productId};

        Cursor cursor = db.query(
                "商品テーブル",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            for (String column : projection) {
                if (column.equals("商品画像")) {
                    intent.putExtra(column, cursor.getBlob(cursor.getColumnIndex(column)));
                } else {
                    intent.putExtra(column, cursor.getString(cursor.getColumnIndex(column)));
                }
            }
        }
        cursor.close();

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.editTextText) {
            intent = new Intent(getApplication(), ProductSearch.class);
        } else if (v.getId() == R.id.home_homebtn) {
            intent = new Intent(getApplication(), HomePage.class);
        } else if (v.getId() == R.id.home_searchbtn) {
            intent = new Intent(getApplication(), ProductSearch.class);
        } else if (v.getId() == R.id.home_exhibitbtn) {
            intent = new Intent(getApplication(), Exhibit.class);
        } else if (v.getId() == R.id.home_favobtn) {
            // お気に入り画面への遷移（未実装）
        } else if (v.getId() == R.id.home_mypagebtn) {
            intent = new Intent(getApplication(), MyPage.class);
        } else if (v.getId() == R.id.noticeButton) {
            // お知らせボタンがクリックされたときの処理をここに追加
            Toast.makeText(HomePage.this, "お知らせボタンがクリックされました", Toast.LENGTH_SHORT).show();
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}