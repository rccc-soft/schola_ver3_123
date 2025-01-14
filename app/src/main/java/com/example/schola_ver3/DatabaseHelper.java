package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "korekore.db";

    // テーブルとカラムの定義
    public static final String TABLE_MEMBERS = "Members";
    public static final String COLUMN_MEMBER_ID = "id"; // 会員ID
    public static final String COLUMN_NAME = "user_name"; // ユーザー名
    public static final String COLUMN_EMAIL = "email"; // メールアドレス
    public static final String COLUMN_PHONE_NUMBER = "phone_number"; // 電話番号
    public static final String COLUMN_PASSWORD = "password"; // パスワード
    public static final String COLUMN_KAIINNAME = "kaiinname"; // 会員名
    public static final String COLUMN_HURIGANA = "hurigana"; // フリガナ
    public static final String COLUMN_BIRTHDAY = "birthday"; // 誕生日
    public static final String COLUMN_SCHOOL = "school"; // 学校
    public static final String COLUMN_PROFILE_MESSAGE = "profile_message"; // プロフィールメッセージ
    public static final String COLUMN_PROFILE_IMAGE_PATH = "profile_image_path"; // プロフィール画像名
    public static final String COLUMN_TERMS_ID = "terms_id"; // 利用規約ID

    // 会員テーブル作成SQL
    private static final String CREATE_TABLE_MEMBERS =
            "CREATE TABLE " + TABLE_MEMBERS + " (" +
                    COLUMN_MEMBER_ID + " TEXT PRIMARY KEY, " +  // user_idとして使用
                    COLUMN_NAME + " TEXT NOT NULL CHECK(LENGTH(" + COLUMN_NAME + ") BETWEEN 2 AND 10), " +
                    COLUMN_PASSWORD + " TEXT NOT NULL CHECK(LENGTH(" + COLUMN_PASSWORD + ") BETWEEN 8 AND 16), " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                    COLUMN_KAIINNAME + " TEXT NOT NULL, " +
                    COLUMN_HURIGANA + " TEXT NOT NULL, " +
                    COLUMN_BIRTHDAY + " TEXT NOT NULL, " +
                    COLUMN_SCHOOL + " TEXT NOT NULL, " +
                    COLUMN_PROFILE_MESSAGE + " TEXT, " +
                    COLUMN_PROFILE_IMAGE_PATH + " TEXT, " +
                    COLUMN_TERMS_ID + " TEXT NOT NULL" +
                    ");";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // profile_image_path カラムを追加
            String alterTableQuery = "ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN " + COLUMN_PROFILE_IMAGE_PATH + " TEXT";
            db.execSQL(alterTableQuery);

            // 利用規約IDカラムを追加
            String alterTermsQuery = "ALTER TABLE " + TABLE_MEMBERS + " ADD COLUMN " + COLUMN_TERMS_ID + " TEXT";
            db.execSQL(alterTermsQuery);
        }
    }



    // データを挿入や更新するためのメソッドを追加（例: insertMemberメソッド）
    public boolean validateLoginByUserId(String userId, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MEMBERS + " WHERE " + COLUMN_MEMBER_ID + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId, password});

        // ログイン情報が一致する場合
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;  // ログイン成功
        } else {
            return false; // ログイン失敗
        }
    }

    public boolean addNewMember(String userId, String username, String email, String phone, String password,
                                String profileImage, String profileMessage, String hurigana, String kaiinname,
                                String birthday, String school) {
        SQLiteDatabase db = this.getWritableDatabase();

        // INSERT文を作成
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEMBER_ID, userId);  // 会員ID
        values.put(COLUMN_NAME, username);  // ユーザー名
        values.put(COLUMN_KAIINNAME, kaiinname);  // 会員名
        values.put(COLUMN_HURIGANA, hurigana);  // ふりがな
        values.put(COLUMN_EMAIL, email);  // メールアドレス
        values.put(COLUMN_PHONE_NUMBER, phone);  // 電話番号
        values.put(COLUMN_PASSWORD, password);  // パスワード
        values.put(COLUMN_PROFILE_IMAGE_PATH, profileImage);  // プロフィール画像パス
        values.put(COLUMN_PROFILE_MESSAGE, profileMessage);  // プロフィールメッセージ
        values.put(COLUMN_BIRTHDAY, birthday);  // 生年月日
        values.put(COLUMN_SCHOOL, school);  // 学校
        values.put(COLUMN_TERMS_ID, "1");

        // データベースに挿入
        long result = db.insert(TABLE_MEMBERS, null, values);

        // 挿入成功した場合はtrueを返す
        return result != -1;
    }

    public Cursor getMemberInfo(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // user_id ではなく COLUMN_MEMBER_ID を使用
        String query = "SELECT * FROM " + TABLE_MEMBERS + " WHERE " + COLUMN_MEMBER_ID + " = ?";
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











}
