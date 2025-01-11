package com.example.schola_ver3;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.UUID;

public class TestDataGenerator {

    private ProductDatabaseHelper dbHelper;

    public TestDataGenerator(ProductDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void generateTestData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        insertTestProduct(db, "test_user_1", "https://example.com/product1", "test_image_1.jpg", 10000, "テスト商品1", "電化製品", 13, "配送");
        insertTestProduct(db, "test_user_2", "https://example.com/product2", "test_image_2.jpg", 5000, "テスト商品2", "衣類", 27, "手渡し");
        insertTestProduct(db, "test_user_3", "https://example.com/product3", "test_image_3.jpg", 1500, "テスト商品3", "書籍", 40, "配送");
    }

    private void insertTestProduct(SQLiteDatabase db, String sellerId, String productUrl, String imageName, int price, String productName, String category, int region, String deliveryMethod) {
        ContentValues values = new ContentValues();

        String productId = UUID.randomUUID().toString();

        values.put("商品ID", productId);
        values.put("出品者ID", sellerId);
        values.put("商品URL", productUrl);
        values.put("商品画像名", imageName);
        values.put("金額", price);
        values.put("商品名", productName);
        values.put("カテゴリ", category);
        values.put("地域", region);
        values.put("出品日時", System.currentTimeMillis());
        values.put("購入済み", false);
        values.put("配送方法", deliveryMethod);

        try {
            db.insertOrThrow("商品テーブル", null, values);
            Log.d("TestDataGenerator", "Inserted product: " + productName);
        } catch (Exception e) {
            Log.e("TestDataGenerator", "Error inserting product: " + e.getMessage());
        }
    }
}