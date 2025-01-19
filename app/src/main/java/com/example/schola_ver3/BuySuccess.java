package com.example.schola_ver3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//1/15_egu
public class BuySuccess extends AppCompatActivity {

    private SalesProcessor salesProcessor;
    private ProductDatabaseHelper productDbHelper;
    private SalesDatabaseHelper salesDbHelper;

    private TextView tvSalesInfo;
    private TextView tvProductInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // レイアウトファイルを指定
        setContentView(R.layout.activity_main);

        // ビューの参照を取得
        tvSalesInfo = findViewById(R.id.tvSalesInfo);
        tvProductInfo = findViewById(R.id.tvProductInfo);

        salesProcessor = new SalesProcessor(this);
        productDbHelper = new ProductDatabaseHelper(this);
        salesDbHelper = new SalesDatabaseHelper(this);

        // Intentから出品者IDと商品IDを取得
        Intent intent = getIntent();
        String sellerId = intent.getStringExtra("seller_id");
        String productId = intent.getStringExtra("product_id");

        if (sellerId == null || productId == null) {
            Toast.makeText(this, "必要なデータがIntentに含まれていません。", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "seller_idまたはproduct_idがIntentに含まれていません。");
            return;
        }

        // 該当商品を検索
        Product product = getProductById(sellerId, productId);
        if (product == null) {
            Toast.makeText(this, "指定された商品が見つかりません。", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "商品が見つかりません。sellerId: " + sellerId + ", productId: " + productId);
            return;
        }

        // 商品が既に購入済みか確認
        if (product.isSold()) {
            Toast.makeText(this, "指定された商品は既に購入済みです。", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "商品は既に購入済みです。productId: " + productId);
        } else {
            // 売上処理を実行
            // paymentMethodを固定値（例: "クレジットカード"）として設定
            String paymentMethod = "クレジットカード";

            boolean result = salesProcessor.processSale(sellerId, product.getPrice(), paymentMethod, productId);
            if (result) {
                Toast.makeText(this, "売上が正常に処理されました！", Toast.LENGTH_LONG).show();
                Log.d("MainActivity", "売上処理が正常に完了しました。");
            } else {
                Toast.makeText(this, "売上の処理に失敗しました。", Toast.LENGTH_LONG).show();
                Log.e("MainActivity", "売上処理に失敗しました。");
            }
        }

        // データの読み込みと表示
        loadSalesData();
        loadProductData();
    }

    /**
     * 商品IDと出品者IDを基に商品を検索します。
     *
     * @param sellerId  出品者ID
     * @param productId 商品ID
     * @return 見つかった商品オブジェクト、存在しない場合はnull
     */
    private Product getProductById(String sellerId, String productId) {
        SQLiteDatabase db = productDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "商品テーブル",
                null, // 全カラムを取得
                "出品者ID = ? AND 商品ID = ?",
                new String[]{sellerId, productId},
                null,
                null,
                null
        );

        Product product = null;
        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("商品ID"));
            String seller = cursor.getString(cursor.getColumnIndexOrThrow("出品者ID"));
            String url = cursor.getString(cursor.getColumnIndexOrThrow("商品URL"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("商品画像名"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("金額"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("商品名"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("カテゴリ"));
            int region = cursor.getInt(cursor.getColumnIndexOrThrow("地域"));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow("出品日時"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("商品説明"));
            int sold = cursor.getInt(cursor.getColumnIndexOrThrow("購入済み"));
            String delivery = cursor.getString(cursor.getColumnIndexOrThrow("配送方法"));

            product = new Product(id, seller, url, image, price, name, category, region, date, description, sold, delivery);
        }

        cursor.close();
        db.close();
        return product;
    }

    /**
     * 売上テーブルのデータを読み込み、TextView に表示します。
     */
    private void loadSalesData() {
        SQLiteDatabase db = salesDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "売上テーブル",
                null, // 全カラムを取得
                null,
                null,
                null,
                null,
                "ROWID DESC" // 最新のデータを先頭に
        );

        StringBuilder salesInfo = new StringBuilder();
        while (cursor.moveToNext()) {
            String memberId = cursor.getString(cursor.getColumnIndexOrThrow("会員ID"));
            int salesAmount = cursor.getInt(cursor.getColumnIndexOrThrow("売上金額"));
            String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("振り込み方法"));

            salesInfo.append("会員ID: ").append(memberId)
                    .append(", 売上金額: ").append(salesAmount)
                    .append("円, 振り込み方法: ").append(paymentMethod)
                    .append("\n");
        }

        if (salesInfo.length() == 0) {
            salesInfo.append("売上データはありません。");
        }

        tvSalesInfo.setText(salesInfo.toString());

        cursor.close();
        db.close();
    }

    /**
     * 商品テーブルのデータを読み込み、TextView に表示します。
     */
    private void loadProductData() {
        SQLiteDatabase db = productDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "商品テーブル",
                null, // 全カラムを取得
                null,
                null,
                null,
                null,
                "ROWID DESC" // 最新のデータを先頭に
        );

        StringBuilder productInfo = new StringBuilder();
        while (cursor.moveToNext()) {
            String productId = cursor.getString(cursor.getColumnIndexOrThrow("商品ID"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("商品名"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("金額"));
            int sold = cursor.getInt(cursor.getColumnIndexOrThrow("購入済み"));

            productInfo.append("商品ID: ").append(productId)
                    .append(", 商品名: ").append(name)
                    .append(", 金額: ").append(price)
                    .append("円, 購入済み: ").append(sold == 1 ? "はい" : "いいえ")
                    .append("\n");
        }

        if (productInfo.length() == 0) {
            productInfo.append("商品データはありません。");
        }

        tvProductInfo.setText(productInfo.toString());

        cursor.close();
        db.close();
    }
}
