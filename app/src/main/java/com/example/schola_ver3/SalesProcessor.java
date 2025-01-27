package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//1/15_egu
public class SalesProcessor {
    private static final String TAG = "SalesProcessor";

    private DatabaseHelper dbHelper;
    private ProductDatabaseHelper productDbHelper;

    public SalesProcessor(Context context) {
        dbHelper = new DatabaseHelper(context);
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
            salesDb = dbHelper.getWritableDatabase();
            salesDb.beginTransaction();

            int currentAmount = dbHelper.getSalesAmount(memberId);
            int updatedAmount = currentAmount + salesAmount;
            dbHelper.updateSalesAmount(memberId, updatedAmount);

            salesDb.setTransactionSuccessful();
            Log.d(TAG, "売上を更新しました。会員ID: " + memberId + ", 更新後の金額: " + updatedAmount);

            productDb = productDbHelper.getWritableDatabase();
            productDb.beginTransaction();

            ContentValues productValues = new ContentValues();
            productValues.put("購入済み", 1);

            int rowsAffected = productDb.update(
                    "商品テーブル",
                    productValues,
                    "商品ID = ?",
                    new String[]{productId}
            );

            if (rowsAffected != 1) {
                throw new Exception("商品テーブルの更新に失敗しました。商品ID: " + productId);
            }

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
