package com.example.schola_ver3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "product.db";

    private static final String TABLE_NAME = "商品テーブル";
    private static final String COLUMN_ID = "商品ID";
    private static final String COLUMN_SELLER_ID = "出品者ID";
    private static final String COLUMN_URL = "商品URL";
    private static final String COLUMN_IMAGE = "商品画像";
    private static final String COLUMN_PRICE = "金額";
    private static final String COLUMN_NAME = "商品名";
    private static final String COLUMN_CATEGORY = "カテゴリ";
    private static final String COLUMN_REGION = "地域";
    private static final String COLUMN_DATE = "出品日時";
    private static final String COLUMN_DESCRIPTION = "商品説明";
    private static final String COLUMN_SOLD = "購入済み";
    private static final String COLUMN_DELIVERY = "配送方法";

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
}