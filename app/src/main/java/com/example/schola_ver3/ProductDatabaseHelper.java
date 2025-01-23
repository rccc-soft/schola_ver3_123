package com.example.schola_ver3;

import static com.example.schola_ver3.DatabaseHelper.COLUMN_SALES_AMOUNT;
import static com.example.schola_ver3.DatabaseHelper.COLUMN_SALES_MEMBER_ID;
import static com.example.schola_ver3.DatabaseHelper.TABLE_MEMBERS;
import static com.example.schola_ver3.DatabaseHelper.TABLE_SALES;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "product.db";

    public static final String TABLE_NAME = "商品テーブル";
    public static final String COLUMN_ID = "商品ID";
    public static final String COLUMN_SELLER_ID = "出品者ID";
    public static final String COLUMN_URL = "商品URL";
    public static final String COLUMN_IMAGE = "商品画像";
    public static final String COLUMN_PRICE = "金額";
    public static final String COLUMN_NAME = "商品名";
    public static final String COLUMN_CATEGORY = "カテゴリ";
    public static final String COLUMN_REGION = "地域";
    public static final String COLUMN_DATE = "出品日時";
    public static final String COLUMN_DESCRIPTION = "商品説明";
    public static final String COLUMN_SOLD = "購入済み";
    public static final String COLUMN_DELIVERY = "配送方法";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_SELLER_ID + " TEXT," +
                    COLUMN_URL + " TEXT," +
                    COLUMN_IMAGE + " BLOB," +
                    COLUMN_PRICE + " INTEGER," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_REGION + " INTEGER," +
                    COLUMN_DATE + " INTEGER," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_SOLD + " INTEGER," +
                    COLUMN_DELIVERY + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getProductInfo(String productId) {
        if (productId == null) {
            Log.e("ProductDatabaseHelper", "getProductInfo: productId is null");
            return null;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        return db.rawQuery(query, new String[]{productId});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // 商品を削除するメソッド
    public void deleteProductById(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = { productId };
            db.delete(TABLE_NAME, whereClause, whereArgs);  // product_table は商品を格納するテーブル名
        } catch (Exception e) {
            Log.e("ProductDatabaseHelper", "Error deleting product with ID: " + productId, e);
        } finally {
            db.close();
        }
    }

    // 指定された商品IDの出品者IDを取得する
    public String getSellerIdByProductId(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SELLER_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productId});
        String sellerId = null;
        if (cursor.moveToFirst()) {
            sellerId = cursor.getString(0); // 出品者IDを取得
        }
        cursor.close();
        return sellerId;
    }

    public int getSalesAmount(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int salesAmount = 0;

        String query = "SELECT " + COLUMN_SALES_AMOUNT +
                " FROM " + TABLE_SALES +
                " WHERE " + COLUMN_SALES_MEMBER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor.moveToFirst()) {
            salesAmount = cursor.getInt(0);
        }
        cursor.close();
        return salesAmount;
    }

    public int getProductPrice(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PRICE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productId});
        int price = 0;
        if (cursor.moveToFirst()) {
            price = cursor.getInt(0); // 価格を取得
        }
        cursor.close();
        return price;
    }

    public void updateSalesAmount(String userId, int newAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SALES_AMOUNT, newAmount);

        db.insertWithOnConflict(TABLE_SALES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // 商品が購入済みかどうかを判定するメソッド
    public boolean isProductSold(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SOLD + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productId});
        boolean isSold = false;
        if (cursor.moveToFirst()) {
            isSold = cursor.getInt(0) == 1; // 1 は購入済み
        }
        cursor.close();
        return isSold;
    }


}