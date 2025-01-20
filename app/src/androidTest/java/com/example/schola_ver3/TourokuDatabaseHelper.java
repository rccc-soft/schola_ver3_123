package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TourokuDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "haisou_info.db";

    // テーブルとカラムの定義
    public static final String TABLE_DELIVERYADRESSES = "delivery_adresses";
    public static final String COLUMN_USER_ID = "userid"; // 会員ID
    public static final String COLUMN_NAME = "username"; // ユーザー名
    public static final String COLUMN_UBIN_NUMBER = "ubinnumber"; // 郵便番号
    public static final String COLUMN_ADRESS = "adress"; // 住所
    public static final String COLUMN_TEL_NUMBER = "telnumber"; // 電話番号
    public static final String COLUMN_DELIVERY_ID = "delivery_id";


    // 会員テーブル作成SQL
    // テーブル作成SQLに配送先IDを追加
    private static final String CREATE_TABLE_DELIVERYADRESSES =
            "CREATE TABLE " + TABLE_DELIVERYADRESSES + " (" +
                    COLUMN_DELIVERY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // 自動増加の配送先ID
                    COLUMN_USER_ID + " TEXT NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL CHECK(LENGTH(" + COLUMN_NAME + ") BETWEEN 2 AND 10), " +
                    COLUMN_UBIN_NUMBER + " TEXT NOT NULL CHECK(LENGTH(" + COLUMN_UBIN_NUMBER + ") = 7), " +
                    COLUMN_ADRESS + " TEXT NOT NULL, " +
                    COLUMN_TEL_NUMBER + " TEXT NOT NULL CHECK(LENGTH(" + COLUMN_TEL_NUMBER + ") = 11) " +
                    ");";
    //"FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES Members(id)" +

    public TourokuDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DELIVERYADRESSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // データベースバージョンが 1 からアップデートされた場合
        if (oldVersion < 2) {
            // 例: テーブルに新しいカラムを追加
            String alterTableQuery = "ALTER TABLE " + TABLE_DELIVERYADRESSES + " ADD COLUMN new_column TEXT";
            db.execSQL(alterTableQuery);
        }
    }


    public Cursor getMemberInfo(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // user_id ではなく COLUMN_MEMBER_ID を使用
        String query = "SELECT * FROM " + TABLE_DELIVERYADRESSES + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{userId});
    }


    public boolean addDeliveryAdress(String userId, String username, String ubinnumber, String adress, String telnumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 既に同じ userId が存在するか確認
        String checkQuery = "SELECT 1 FROM " + TABLE_DELIVERYADRESSES + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(checkQuery, new String[]{userId});

        boolean isExisting = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_NAME, username);
        values.put(COLUMN_UBIN_NUMBER, ubinnumber);
        values.put(COLUMN_ADRESS, adress);
        values.put(COLUMN_TEL_NUMBER, telnumber);

        if (isExisting) {
            // 存在する場合は更新
            int rowsUpdated = db.update(TABLE_DELIVERYADRESSES, values, COLUMN_USER_ID + " = ?", new String[]{userId});
            return rowsUpdated > 0;
        } else {
            // 存在しない場合は挿入
            long result = db.insert(TABLE_DELIVERYADRESSES, null, values);
            return result != -1;
        }
    }


    public Cursor getDeliveryAdresses(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // user_id ではなく COLUMN_USER_ID を使用
        String query = "SELECT * FROM " + TABLE_DELIVERYADRESSES + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{userId});
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "Members", // テーブル名
                new String[]{"email"}, // 取得するカラム
                "email=?", // WHERE句
                new String[]{email}, // WHERE句のパラメータ
                null, null, null
        );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public boolean validateLoginByUserId(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean isValid = false;

        try {
            // SQLクエリを実行してuserIdに一致するレコードを取得
            String query = "SELECT * FROM " + TABLE_DELIVERYADRESSES + " WHERE " + COLUMN_USER_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{userId});

            // userIdが存在する場合
            if (cursor != null && cursor.moveToFirst()) {
                isValid = true;  // userIdが存在すればログイン成功
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cursorを閉じる
            if (cursor != null) {
                cursor.close();
            }
        }

        return isValid;
    }


    // 会員情報を更新するメソッド
    public boolean updateDeliveryAdress(String userId, String name, String ubinnumber, String adress, String telnumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_UBIN_NUMBER, ubinnumber);
        values.put(COLUMN_ADRESS, adress);
        values.put(COLUMN_TEL_NUMBER, telnumber);

        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {userId};

        int rowsAffected = db.update(TABLE_DELIVERYADRESSES, values, whereClause, whereArgs);
        db.close();
        return rowsAffected > 0;  // 更新が成功した場合 true を返す
    }
}
