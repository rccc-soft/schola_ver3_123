package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log; // Logクラスをインポート
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Random;

public class BuySuccess extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ProductDatabaseHelper productDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_completion);

        dbHelper = new DatabaseHelper(this);
        productDbHelper = new ProductDatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("ProductPrefs", MODE_PRIVATE);
        String productId = prefs.getString("product_id", "");

        // ProductDatabaseHelperを使用してsellerIdとsaleAmountを取得
        String sellerId = productDbHelper.getSellerIdByProductId(productId);
        int saleAmount = productDbHelper.getProductPrice(productId);

        // ログに出品者IDと売上金額を出力
        Log.d("BuySuccess", "出品者ID: " + sellerId + ", 売上金額: " + saleAmount);

        // 売り上げテーブルにデータを挿入
        if (sellerId != null && !sellerId.isEmpty() && saleAmount > 0) {
            Log.d("BuySuccess", "売上データ挿入前: sellerId=" + sellerId + ", saleAmount=" + saleAmount);
            insertSaleRecord(sellerId, saleAmount);
        } else {
            Log.e("BuySuccess", "売上データ挿入条件を満たしていません: sellerId=" + sellerId + ", saleAmount=" + saleAmount);
        }

        // 購入情報を保存
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String buyerId = sharedPreferences.getString("user_id", ""); // ログイン中の会員者IDを取得

        String itemId = productId; // 購入した商品ID
        int cardId = new Random().nextInt(1000000000); // 仮のクレカIDを生成
        Log.d("BuySuccess", "取得したユーザーID: " + buyerId);
        String destinationId = dbHelper.getDestinationIdByMemberId(buyerId); // 会員者IDに対応する配送先IDを取得
        Log.d("BuySuccess", "Buyer ID: " + buyerId + ", Destination ID: " + destinationId);

        if (!buyerId.isEmpty() && !itemId.isEmpty() && !destinationId.isEmpty()) {
            insertPurchaseRecord(buyerId, itemId, cardId, destinationId);
            updateProductSoldStatus(itemId);
        }

        // ホームボタンの参照を取得
        Button homeButton = findViewById(R.id.homeButton);

        // ホームボタンのクリックリスナーを設定
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomePage.java に画面遷移
                Intent intent = new Intent(BuySuccess.this, HomePage.class);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        });
    }

    // 商品の購入済み状態を更新するメソッド
    private void updateProductSoldStatus(String productId) {
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProductDatabaseHelper.COLUMN_SOLD, 1); // 1 は true を表す

        int updatedRows = db.update(
                ProductDatabaseHelper.TABLE_NAME,
                values,
                ProductDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{productId}
        );

        if (updatedRows > 0) {
            Log.d("BuySuccess", "商品ID " + productId + " の購入済み状態を更新しました");
        } else {
            Log.e("BuySuccess", "商品ID " + productId + " の購入済み状態の更新に失敗しました");
        }

        db.close();
    }

    // 売り上げテーブルにデータを挿入するメソッド
    private void insertSaleRecord(String sellerId, int price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 現在の売上金額を取得
        int currentSales = getCurrentSales(sellerId);

        // 新しい売上金額を計算（現在の売上 + 新しい商品の価格）
        int newSalesAmount = currentSales + price;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SALES_AMOUNT, newSalesAmount);

        int updatedRows = db.update(DatabaseHelper.TABLE_SALES,
                values,
                DatabaseHelper.COLUMN_SALES_MEMBER_ID + "=?",
                new String[]{sellerId});

        if (updatedRows == 0) {
            // 新規レコードの挿入
            values.put(DatabaseHelper.COLUMN_SALES_MEMBER_ID, sellerId);
            long result = db.insert(DatabaseHelper.TABLE_SALES, null, values);
            if (result != -1) {
                Log.d("BuySuccess", "新しい売り上げデータが挿入されました: sellerId=" + sellerId + ", amount=" + newSalesAmount);
            } else {
                Log.e("BuySuccess", "新しい売り上げデータの挿入に失敗しました");
            }
        } else {
            Log.d("BuySuccess", "既存の売り上げデータが更新されました: sellerId=" + sellerId + ", amount=" + newSalesAmount);
        }

        db.close();
    }

    // 現在の売上金額を取得するメソッド
    private int getCurrentSales(String sellerId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int currentSales = 0;

        Cursor cursor = db.query(DatabaseHelper.TABLE_SALES,
                new String[]{DatabaseHelper.COLUMN_SALES_AMOUNT},
                DatabaseHelper.COLUMN_SALES_MEMBER_ID + "=?",
                new String[]{sellerId},
                null, null, null);

        if (cursor.moveToFirst()) {
            currentSales = cursor.getInt(0);
        }
        cursor.close();

        return currentSales;
    }

    // 購入テーブルにデータを挿入するメソッド
    private void insertPurchaseRecord(String buyerId, String itemId, int cardId, String destinationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BUYER_ID, buyerId); // 会員者ID
        values.put(DatabaseHelper.COLUMN_ITEM_ID, itemId); // 商品ID
        values.put(DatabaseHelper.COLUMN_CARD_ID, cardId); // クレカID
        values.put(DatabaseHelper.COLUMN_DESTINATION_ID, destinationId); // 配送先ID
        values.put(DatabaseHelper.COLUMN_IS_SHIPPED, 0); // 配送状況 (false)
        values.put(DatabaseHelper.COLUMN_IS_SELLER_RATED, 0); // 出品者評価済み (false)

        // データを挿入
        long result = db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();

        // ログに出力
        if (result != -1) {
            Log.d("BuySuccess", "購入データが挿入されました: buyerId=" + buyerId + ", itemId=" + itemId + ", cardId=" + cardId + ", destinationId=" + destinationId);
        } else {
            Log.e("BuySuccess", "購入データの挿入に失敗しました");
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }


}