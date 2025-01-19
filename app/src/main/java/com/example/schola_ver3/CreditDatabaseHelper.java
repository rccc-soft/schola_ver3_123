package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreditDatabaseHelper extends SQLiteOpenHelper {

    // データベース名とバージョン
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "credit.db";

    // テーブル名とカラム名
    public static final String TABLE_NAME = "credit"; // クレカテーブル
    private static final String CREDIT_ID = "credit_id"; // クレカID
    public static final String MEMBER_ID = "member_id"; // 会員者ID
    public static final String CARD_NUMBER = "card_number"; // クレカ番号
    public static final String EXPIRATION_DATE = "expiration_date"; // 有効期限
    public static final String CARD_CVCCVV = "card_cvccvv"; // セキュリティ番号
    public static final String CARD_NAME = "card_name"; // 名義人


    // テーブル作成クエリ
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    CREDIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MEMBER_ID + " TEXT NOT NULL," +
                    CARD_NUMBER + " TEXT NOT NULL," +
                    EXPIRATION_DATE + " TEXT NOT NULL," +
                    CARD_CVCCVV + " TEXT NOT NULL," +
                    CARD_NAME + " TEXT NOT NULL," +
                    "FOREIGN KEY (" + MEMBER_ID + ") REFERENCES Members(id))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public CreditDatabaseHelper(Context context) {
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
//        long result = db.insert(TABLE_NAME, null, values);
//        if (result == -1) {
//            throw new RuntimeException("データの挿入に失敗しました");
//        } else {
//            System.out.println("データ挿入成功: member_id=1, sales_amount=1000");
//        }
//    }

    public void checkAndInsertDefaultData(SQLiteDatabase db, String memberId) {
        // 該当する MEMBER_ID のレコードが存在するかを確認
        Cursor cursor = null;
        try {
            // レコードの存在を確認
            String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + MEMBER_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{memberId});

            if (!cursor.moveToFirst()) {
                // レコードが存在しない場合は挿入
                ContentValues values = new ContentValues();
                values.put(MEMBER_ID, memberId);
                values.put(CARD_NUMBER, "0000000000000000"); // デフォルト値
                values.put(EXPIRATION_DATE, "MMYY"); // デフォルト値
                values.put(CARD_CVCCVV, "000"); // デフォルト値
                values.put(CARD_NAME, "例）TAROYAMADA"); // デフォルト値

                long rowId = db.insert(TABLE_NAME, null, values);

                if (rowId != -1) {
                    System.out.println("新しいレコードを挿入しました");
                } else {
                    System.out.println("データ挿入中にエラーが発生しました");
                }
            } else {
                System.out.println("既に該当する会員IDのレコードが存在しています");
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

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

    public void updateCreditData(String memberId, String cardNumber, String expirationDate, String cardCVCCVV, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 更新する値を設定
        values.put(CARD_NUMBER, cardNumber);
        values.put(EXPIRATION_DATE, expirationDate);
        values.put(CARD_CVCCVV, cardCVCCVV);
        values.put(CARD_NAME, name);

        // 更新クエリを実行
        int rowsAffected = db.update(
                TABLE_NAME,                       // テーブル名
                values,                             // 更新する値
                "member_id = ?",                   // 条件（WHERE句）
                new String[]{memberId} // 条件のパラメータ
        );

        // 更新の結果を確認
        if (rowsAffected > 0) {
            System.out.println("データを正常に更新しました");
        } else {
            System.out.println("更新対象のレコードが見つかりません");
        }
        db.close(); // データベースを閉じる
    }
}