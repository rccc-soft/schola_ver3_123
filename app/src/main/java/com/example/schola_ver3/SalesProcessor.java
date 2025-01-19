package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//1/15_egu
public class SalesProcessor {
    private static final String TAG = "SalesProcessor";

    private SalesDatabaseHelper salesDbHelper;
    private ProductDatabaseHelper productDbHelper;

    public SalesProcessor(Context context) {
        salesDbHelper = new SalesDatabaseHelper(context);
        productDbHelper = new ProductDatabaseHelper(context);
    }

    /**
     * 売上を処理し、売上テーブルに挿入するとともに商品を購入済みに更新します。
     *
     * @param memberId       売上に関連する会員ID
     * @param salesAmount    売上金額
     * @param paymentMethod  使用された振り込み方法
     * @param productId      購入された商品の商品ID
     * @return 両方の操作が成功した場合はtrue、そうでない場合はfalse
     */
    public boolean processSale(String memberId, int salesAmount, String paymentMethod, String productId) {
        SQLiteDatabase salesDb = null;
        SQLiteDatabase productDb = null;
        boolean success = false;

        try {
            // 売上データベースのトランザクション開始
            salesDb = salesDbHelper.getWritableDatabase();
            salesDb.beginTransaction();

            // 売上テーブルに挿入
            ContentValues salesValues = new ContentValues();
            salesValues.put("会員ID", memberId);
            salesValues.put("売上金額", salesAmount);
            salesValues.put("振り込み方法", paymentMethod);

            long salesRowId = salesDb.insert("売上テーブル", null, salesValues);
            if (salesRowId == -1) {
                throw new Exception("売上テーブルへの挿入に失敗しました");
            }

            // トランザクションを成功としてマーク
            salesDb.setTransactionSuccessful();
            Log.d(TAG, "売上を挿入しました。行ID: " + salesRowId);

            // 商品データベースのトランザクション開始
            productDb = productDbHelper.getWritableDatabase();
            productDb.beginTransaction();

            // 商品を購入済みに更新
            ContentValues productValues = new ContentValues();
            productValues.put("購入済み", 1); // 1は購入済みを意味する

            int rowsAffected = productDb.update(
                    "商品テーブル",
                    productValues,
                    "商品ID = ?",
                    new String[]{productId}
            );

            if (rowsAffected != 1) {
                throw new Exception("商品テーブルの更新に失敗しました。商品ID: " + productId);
            }

            // トランザクションを成功としてマーク
            productDb.setTransactionSuccessful();
            Log.d(TAG, "商品テーブルを更新しました。商品ID: " + productId);

            success = true;
        } catch (Exception e) {
            Log.e(TAG, "売上処理中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (salesDb != null) {
                salesDb.endTransaction();
                salesDb.close();
            }
            if (productDb != null) {
                productDb.endTransaction();
                productDb.close();
            }
        }

        return success;
    }
}
