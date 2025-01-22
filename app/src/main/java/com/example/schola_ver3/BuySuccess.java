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

import java.util.Random;

public class BuySuccess extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_completion); // レイアウトを設定

        // DatabaseHelper のインスタンスを取得
        dbHelper = new DatabaseHelper(this);

        // テストデータを挿入
        dbHelper.insertTestProductData();

        // テスト用の配送先データを挿入
        dbHelper.insertTestDeliveryAddresses("uHTHD8");



        // 商品IDを設定
        String productId = "product001"; // 検索する商品ID

        // 商品テーブルから出品者IDと売上金額を取得
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PRODUCT_NAME, // 商品テーブル
                new String[]{DatabaseHelper.COLUMN_SELLER_ID, DatabaseHelper.COLUMN_PRICE}, // 取得するカラム
                DatabaseHelper.COLUMN_ID + "=?", // WHERE句
                new String[]{productId}, // WHERE句のパラメータ
                null, null, null
        );

        String sellerId = "";
        int saleAmount = 0;

        if (cursor != null && cursor.moveToFirst()) {
            int sellerIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SELLER_ID);
            int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);

            if (sellerIdIndex != -1 && priceIndex != -1) {
                sellerId = cursor.getString(sellerIdIndex);
                saleAmount = cursor.getInt(priceIndex);

                // ログに出品者IDと売上金額を出力
                Log.d("BuySuccess", "出品者ID: " + sellerId + ", 売上金額: " + saleAmount);
            }
            cursor.close();
        }

        // 売り上げテーブルにデータを挿入
        if (!sellerId.isEmpty() && saleAmount > 0) {
            insertSaleRecord(sellerId, saleAmount);
        }

        // 購入情報を保存
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String buyerId = sharedPreferences.getString("user_id", ""); // ログイン中の会員者IDを取得
        buyerId = "uHTHD8";
        String itemId = productId; // 購入した商品ID
        int cardId = new Random().nextInt(1000000000); // 仮のクレカIDを生成
        String destinationId = dbHelper.getDestinationIdByMemberId(buyerId); // 会員者IDに対応する配送先IDを取得


        if (!buyerId.isEmpty() && !itemId.isEmpty() && !destinationId.isEmpty()) {
            insertPurchaseRecord(buyerId, itemId, cardId, destinationId);
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

    // 売り上げテーブルにデータを挿入するメソッド
    // 売り上げテーブルにデータを挿入するメソッド
    // 売り上げテーブルにデータを挿入するメソッド
    private void insertSaleRecord(String sellerId, int price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SALE_SELLER_ID, sellerId); // 出品者ID
        values.put(DatabaseHelper.COLUMN_SALE_PRICE, price); // 売上金額

        // データを挿入
        long result = db.insert(DatabaseHelper.TABLE_SALES, null, values);
        db.close();

        // ログに出力
        if (result != -1) {
            Log.d("BuySuccess", "売り上げデータが挿入されました: sellerId=" + sellerId + ", amount=" + price);
        } else {
            Log.e("BuySuccess", "売り上げデータの挿入に失敗しました");
        }
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