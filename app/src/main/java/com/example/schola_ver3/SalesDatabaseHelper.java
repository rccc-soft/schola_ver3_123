package com.example.schola_ver3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SalesDatabaseHelper extends SQLiteOpenHelper {

    // データベース名とバージョン
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "sales.db";

    // テーブル名とカラム名
    public static final String TABLE_NAME = "sales"; // 売上テーブル
    public static final String MEMBER_ID = "member_id"; // 会員者ID
    public static final String SALES_AMOUNT = "sales_amount"; // 売上金額

    // テーブル作成クエリ
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    MEMBER_ID + " TEXT PRIMARY KEY," +
//                    SALES_AMOUNT + " INTEGER)";
                    SALES_AMOUNT + " INTEGER " +
                    ");";
//                    "FOREIGN KEY (" + MEMBER_ID + ") REFERENCES Members(id))"; // 外部キー

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SalesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // データベースが初めて作成されるときに呼び出される
    // メソッド内でテーブルを作成する
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
//        setValue(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); // 外部キー制約を有効にする
    }

//    private void setValue(SQLiteDatabase db) {
//        ContentValues values = new ContentValues();
//        values.put(MEMBER_ID, "1");
//        values.put(SALES_AMOUNT, "10000");
//        values.put(MEMBER_ID, "2");
//        values.put(SALES_AMOUNT, "20000");
//        long result = db.insert(TABLE_NAME, null, values);
//        if (result == -1) {
//            throw new RuntimeException("データの挿入に失敗しました");
//        } else {
//            System.out.println("データ挿入成功: member_id=1, sales_amount=1000");
//        }
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // データベースを古いバージョンに戻すときに呼び出される
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}