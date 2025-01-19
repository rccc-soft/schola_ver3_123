package com.example.schola_ver3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SalesDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sales.db";

    private static final String TABLE_NAME = "売上テーブル";
    private static final String COLUMN_MEMBER_ID = "会員ID";
    private static final String COLUMN_SALES_AMOUNT = "売上金額";
    private static final String COLUMN_PAYMENT_METHOD = "振り込み方法";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_MEMBER_ID + " TEXT NOT NULL, " +
                    COLUMN_SALES_AMOUNT + " INTEGER NOT NULL CHECK (" + COLUMN_SALES_AMOUNT + " >= 0), " +
                    COLUMN_PAYMENT_METHOD + " TEXT NOT NULL CHECK (" + COLUMN_PAYMENT_METHOD +
                    " IN ('銀行振込', 'クレジットカード', 'その他')), " +
                    "FOREIGN KEY(" + COLUMN_MEMBER_ID + ") REFERENCES ユーザーテーブル(会員ID))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SalesDatabaseHelper(Context context) {
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
