package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5; // バージョンを更新
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

    // 保護者テーブルとカラムの定義
    public static final String TABLE_PARENTS = "Parents";
    public static final String COLUMN_PARENT_ID = "parent_id"; // 保護者ID (主キー)
    public static final String COLUMN_PARENT_MEMBER_ID = "member_id"; // 会員ID
    public static final String COLUMN_PARENT_NAME = "parent_name"; // 保護者名
    public static final String COLUMN_PARENT_FURIGANA = "parent_furigana"; // ふりがな
    public static final String COLUMN_PARENT_EMAIL = "parent_email"; // メールアドレス
    public static final String COLUMN_PARENT_PHONE = "parent_phone"; // 電話番号
    public static final String COLUMN_PARENT_PASSWORD = "parent_password"; // パスワード
    public static final String COLUMN_PARENT_DOCUMENTS = "parent_documents"; // 必要書類


    // 配送業者テーブルとカラムの定義
    public static final String TABLE_DELIVERY = "Delivery";
    public static final String COLUMN_DELIVERY_ID = "delivery_id"; // 配送業者ID (主キー)
    public static final String COLUMN_DELIVERY_NAME = "delivery_name"; // 配送業者名
    public static final String COLUMN_DELIVERY_EMAIL = "delivery_email"; // メールアドレス
    public static final String COLUMN_DELIVERY_PHONE = "delivery_phone"; // 電話番号
    public static final String COLUMN_DELIVERY_PASSWORD = "delivery_password"; // パスワード
    public static final String COLUMN_DELIVERY_DOCUMENTS = "delivery_documents"; // 必要書類のパス

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


    // 保護者テーブル作成SQL
    private static final String CREATE_TABLE_PARENTS =
            "CREATE TABLE " + TABLE_PARENTS + " (" +
                    COLUMN_PARENT_MEMBER_ID + " TEXT PRIMARY KEY, " + // 会員IDを主キーにする
                    COLUMN_PARENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_PARENT_FURIGANA + " TEXT NOT NULL, " +
                    COLUMN_PARENT_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PARENT_PHONE + " TEXT NOT NULL, " +
                    COLUMN_PARENT_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_PARENT_DOCUMENTS + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_PARENT_MEMBER_ID + ") REFERENCES " + TABLE_MEMBERS + "(" + COLUMN_MEMBER_ID + ")" +
                    ");";


    // 配送業者テーブル作成SQL
    private static final String CREATE_TABLE_DELIVERY =
            "CREATE TABLE " + TABLE_DELIVERY + " (" +
                    COLUMN_DELIVERY_ID + " TEXT PRIMARY KEY, " + // 配送業者ID
                    COLUMN_DELIVERY_NAME + " TEXT NOT NULL, " + // 配送業者名
                    COLUMN_DELIVERY_EMAIL + " TEXT NOT NULL UNIQUE, " + // メールアドレス
                    COLUMN_DELIVERY_PHONE + " TEXT NOT NULL, " + // 電話番号
                    COLUMN_DELIVERY_PASSWORD + " TEXT NOT NULL, " + // パスワード
                    COLUMN_DELIVERY_DOCUMENTS + " TEXT" + // 必要書類のパス
                    ");";

    public static final String TABLE_DELIVERY_ADDRESS = "DeliveryAddress";
    public static final String COLUMN_DELIVERY_ADDRESS_ID = "delivery_address_id"; // 配送先ID (主キー)
    public static final String COLUMN_MEMBER_NAME = "member_name"; // 会員者名
    public static final String COLUMN_POSTAL_CODE = "postal_code"; // 郵便番号
    public static final String COLUMN_ADDRESS = "address"; // 住所

    // 配送先テーブル作成SQL
    private static final String CREATE_TABLE_DELIVERY_ADDRESS =
            "CREATE TABLE " + TABLE_DELIVERY_ADDRESS + " (" +
                    COLUMN_DELIVERY_ADDRESS_ID + " TEXT PRIMARY KEY, " + // 配送先ID
                    COLUMN_MEMBER_ID + " TEXT NOT NULL, " + // 会員者ID
                    COLUMN_MEMBER_NAME + " TEXT NOT NULL, " + // 会員者名
                    COLUMN_POSTAL_CODE + " TEXT NOT NULL, " + // 郵便番号
                    COLUMN_ADDRESS + " TEXT NOT NULL, " + // 住所
                    COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " + // 電話番号
                    "FOREIGN KEY (" + COLUMN_MEMBER_ID + ") REFERENCES " + TABLE_MEMBERS + "(" + COLUMN_MEMBER_ID + ")" + // 外部キー
                    ");";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMBERS);
        db.execSQL(CREATE_TABLE_PARENTS);
        db.execSQL(CREATE_TABLE_DELIVERY);
        db.execSQL(CREATE_TABLE_DELIVERY_ADDRESS); // 配送先テーブルを作成
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) { // バージョン6で配送先テーブルを追加
            db.execSQL(CREATE_TABLE_DELIVERY_ADDRESS);
        }
    }

    // 配送先情報を挿入するメソッド
    public boolean insertDeliveryAddress(String deliveryAddressId, String memberId, String memberName, String postalCode, String address, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DELIVERY_ADDRESS_ID, deliveryAddressId);
        values.put(COLUMN_MEMBER_ID, memberId);
        values.put(COLUMN_MEMBER_NAME, memberName);
        values.put(COLUMN_POSTAL_CODE, postalCode);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);

        long result = db.insert(TABLE_DELIVERY_ADDRESS, null, values);
        return result != -1;
    }

    // 全ての配送先を取得するメソッド
    public Cursor getAllDeliveryAddresses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DELIVERY_ADDRESS, null, null, null, null, null, null);
    }

    // 配送先IDで配送先を取得するメソッド
    public Cursor getDeliveryAddressById(String deliveryAddressId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DELIVERY_ADDRESS + " WHERE " + COLUMN_DELIVERY_ADDRESS_ID + " LIKE ?";
        return db.rawQuery(query, new String[]{"%" + deliveryAddressId + "%"});
    }

    // 配送先IDを自動生成するメソッド
    public String generateDeliveryAddressId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder deliveryAddressId = new StringBuilder("DA"); // プレフィックスとして "DA" を追加
        for (int i = 0; i < 7; i++) { // 7文字のランダムな文字列を生成
            int randomIndex = random.nextInt(chars.length());
            deliveryAddressId.append(chars.charAt(randomIndex));
        }
        return deliveryAddressId.toString();
    }


    public boolean deleteMember(String memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_MEMBERS, COLUMN_MEMBER_ID + " = ?", new String[]{memberId});
        return rowsDeleted > 0;
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
                TABLE_MEMBERS, // テーブル名
                new String[]{COLUMN_EMAIL}, // 取得するカラム
                COLUMN_EMAIL + "=?", // WHERE句
                new String[]{email}, // WHERE句のパラメータ
                null, null, null
        );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // 保護者用パスワードを取得するメソッド
    public String getParentPassword(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PARENT_PASSWORD + " FROM " + TABLE_PARENTS + " WHERE " + COLUMN_PARENT_MEMBER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        String parentPassword = null;
        if (cursor != null && cursor.moveToFirst()) {
            int parentPasswordIndex = cursor.getColumnIndex(COLUMN_PARENT_PASSWORD);
            if (parentPasswordIndex != -1) {
                parentPassword = cursor.getString(parentPasswordIndex);
            }
            cursor.close();
        }
        db.close();
        return parentPassword;
    }

    // 保護者情報を挿入するメソッド
    public boolean insertParentInfo(String userId, String parentName, String parentFurigana, String parentEmail, String parentPhone, String parentPassword, String parentDocuments) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 保護者テーブルが存在しない場合は作成
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_PARENTS + "'", null);
        boolean tableExists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }

        if (!tableExists) {
            db.execSQL(CREATE_TABLE_PARENTS);
        }

        // INSERT文を作成
        ContentValues values = new ContentValues();
        values.put(COLUMN_PARENT_MEMBER_ID, userId); // 会員者IDを保存
        values.put(COLUMN_PARENT_NAME, parentName);
        values.put(COLUMN_PARENT_FURIGANA, parentFurigana);
        values.put(COLUMN_PARENT_EMAIL, parentEmail);
        values.put(COLUMN_PARENT_PHONE, parentPhone);
        values.put(COLUMN_PARENT_PASSWORD, parentPassword);
        values.put(COLUMN_PARENT_DOCUMENTS, parentDocuments);

        long result = db.insert(TABLE_PARENTS, null, values);
        return result != -1;
    }



    /**
     * 保護者テーブルから会員者IDとパスワードを照合するメソッド
     *
     * @param userId 会員者ID
     * @param password 保護者パスワード
     * @return 照合結果（true: 一致, false: 不一致）
     */
    public boolean validateParentCredentials(String userId, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PARENTS + " WHERE " + COLUMN_PARENT_MEMBER_ID + " = ? AND " + COLUMN_PARENT_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId, password});

        Log.d("DatabaseHelper", "Query: " + query);
        Log.d("DatabaseHelper", "Member ID: " + userId);
        Log.d("DatabaseHelper", "Password: " + password);

        if (cursor != null && cursor.moveToFirst()) {
            // 一致するレコードがある場合
            Log.d("DatabaseHelper", "Validation successful");
            cursor.close();
            return true;
        } else {
            // 一致するレコードがない場合
            Log.d("DatabaseHelper", "Validation failed");
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
    }

    /**
     * 配送業者情報をデータベースに保存するメソッド
     *
     * @param deliveryId     配送業者ID
     * @param deliveryName   配送業者名
     * @param deliveryEmail  メールアドレス
     * @param deliveryPhone  電話番号
     * @param deliveryPassword パスワード
     * @param deliveryDocuments 必要書類のパス
     * @return 保存が成功した場合は true、失敗した場合は false
     */
    public boolean insertDeliveryInfo(String deliveryId, String deliveryName, String deliveryEmail, String deliveryPhone, String deliveryPassword, String deliveryDocuments) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DELIVERY_ID, deliveryId);
            values.put(COLUMN_DELIVERY_NAME, deliveryName);
            values.put(COLUMN_DELIVERY_EMAIL, deliveryEmail);
            values.put(COLUMN_DELIVERY_PHONE, deliveryPhone);
            values.put(COLUMN_DELIVERY_PASSWORD, deliveryPassword);
            values.put(COLUMN_DELIVERY_DOCUMENTS, deliveryDocuments);

            long result = db.insert(TABLE_DELIVERY, null, values);
            Log.d("DatabaseHelper", "Insert result: " + result);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting delivery info", e);
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * 配送業者のログイン情報を検証するメソッド
     *
     * @param deliveryId 配送業者ID
     * @param password   パスワード
     * @return 検証が成功した場合は true、失敗した場合は false
     */
    public boolean validateDeliveryCredentials(String deliveryId, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DELIVERY + " WHERE " + COLUMN_DELIVERY_ID + " = ? AND " + COLUMN_DELIVERY_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{deliveryId, password});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true; // 検証成功
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return false; // 検証失敗
        }
    }

    /**
     * 配送業者IDを自動生成するメソッド
     *
     * @return 生成された配送業者ID
     */
    public String generateDeliveryId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder deliveryId = new StringBuilder("D"); // プレフィックスとして "D" を追加
        for (int i = 0; i < 7; i++) { // 7文字のランダムな文字列を生成
            int randomIndex = random.nextInt(chars.length());
            deliveryId.append(chars.charAt(randomIndex));
        }
        return deliveryId.toString();
    }

    public Cursor getDeliveryInfo(String deliveryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DELIVERY + " WHERE " + COLUMN_DELIVERY_ID + " = ?";
        return db.rawQuery(query, new String[]{deliveryId});
    }

    // テスト用の配送先データを挿入するメソッド
    public void insertTestDeliveryAddresses() {
        SQLiteDatabase db = this.getWritableDatabase();

        // テストデータ1
        insertDeliveryAddress("DA1234567", "member001", "山田太郎", "100-0001", "東京都千代田区", "03-1234-5678");
        // テストデータ2
        insertDeliveryAddress("DA7654321", "member002", "鈴木花子", "150-0002", "東京都渋谷区", "03-9876-5432");
        // テストデータ3
        insertDeliveryAddress("DA1111111", "member003", "佐藤健太", "160-0003", "東京都新宿区", "03-1111-2222");
        // テストデータ4
        insertDeliveryAddress("DA2222222", "member004", "高橋美咲", "170-0004", "東京都豊島区", "03-3333-4444");
        // テストデータ5
        insertDeliveryAddress("DA3333333", "member005", "伊藤直人", "180-0005", "東京都品川区", "03-5555-6666");

        Log.d("DatabaseHelper", "テストデータを配送先テーブルに挿入しました。");
    }

}