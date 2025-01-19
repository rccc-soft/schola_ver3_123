package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


// MyPage から
public class CreditSetting extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    //    private ActivityResultLauncher<Intent> resultLauncher;
    private ImageButton backButton; // 戻るボタン
    //    private ImageButton noticeButton; //　通知ボタン
    private Button updateButton; // 更新ボタン
    private EditText editTextCardNumber; //　カード番号入力
    private EditText editTextExpirationDate; // 有効期限入力
    private EditText editTextCVCCVV; // CVC/CVV入力
    private EditText editTextName; // 名前入力
    private CreditDatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private String userId;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_credit_setting);

        // UIの初期化
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

//        noticeButton = (ImageButton) findViewById(R.id.noticeButton);
//        noticeButton.setOnClickListener(this);

        updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);

        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpirationDate = findViewById(R.id.editTextExpirationDate);
        editTextCVCCVV = findViewById(R.id.editTextCVCCVV);
        editTextName = findViewById(R.id.editTextName);

        // InputFilter を設定
//        editTextCardNumber.setFilters(new InputFilter[]{
//                new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                        // 入力がリーディングゼロ（先頭が 0）の場合も許可
//                        if (dest.toString().isEmpty() && source.toString().matches("^0+")) {
//                            return source;
//                        }
//                        // それ以外はそのまま許可
//                        return source;
//                    }
//                }
//        });

//        editTextExpirationDate.setFilters(new InputFilter[]{
//                new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                        // 入力がリーディングゼロ（先頭が 0）の場合も許可
//                        if (dest.toString().isEmpty() && source.toString().matches("^0+")) {
//                            return source;
//                        }
//                        // それ以外はそのまま許可
//                        return source;
//                    }
//                }
//        });

//        editTextCVCCVV.setFilters(new InputFilter[]{
//                new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                        // 入力がリーディングゼロ（先頭が 0）の場合も許可
//                        if (dest.toString().isEmpty() && source.toString().matches("^0+")) {
//                            return source;
//                        }
//                        // それ以外はそのまま許可
//                        return source;
//                    }
//                }
//        });

        // 共通の InputFilter を作成
        InputFilter allowLeadingZeroFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 入力内容をそのまま許可
                return null; // null を返すと入力がそのまま適用されます
            }
        };


        // 会員IDを取得するロジックをここに追加
//        String memberId = "1";
        // 特定の userId の sales_amount を取得して表示
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null); // "user_id" に保存された値を取得

        if (userId != null) {
            // 会員者IDを取得できた場合
//            Toast.makeText(this, "ログイン中の会員者ID: " + userId, Toast.LENGTH_SHORT).show();
            Log.d("CreditSetting", "ログイン中の会員者ID: " + userId);
        } else {
            // 会員者IDが取得できない場合（例: ログアウト状態など）
//            Toast.makeText(this, "ログインしているユーザーはいません。", Toast.LENGTH_SHORT).show();
            Log.d("CreditSetting", "ログインしているユーザーはいません");
        }

        // SalesDatabaseHelper のインスタンスを作成
//        CreditDatabaseHelper dbHelper = new CreditDatabaseHelper(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();  // 書き込み可能なデータベースを取得

        dbHelper = new CreditDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();  // 書き込み可能なデータベースを取得

        // MEMBER_IDを引数として渡してデフォルトデータを確認して挿入
        dbHelper.checkAndInsertDefaultData(db, userId);

        // 特定の userId のクレカ情報を取得
        String cardNumber = getCardNumber(userId, db);
        String expirationDate = getExpirationDate(userId, db);
        String cardCVCCVV = getCardCVCCVV(userId, db);
        String cardName = getCardName(userId, db);

        // EditText に取得した情報を表示
        editTextCardNumber.setText(cardNumber);
        editTextExpirationDate.setText(expirationDate);
        editTextCVCCVV.setText(cardCVCCVV);
        editTextName.setText(cardName);

        // InputFilter をすべての EditText に適用
        editTextCardNumber.setFilters(new InputFilter[]{allowLeadingZeroFilter});
        editTextExpirationDate.setFilters(new InputFilter[]{allowLeadingZeroFilter});
        editTextCVCCVV.setFilters(new InputFilter[]{allowLeadingZeroFilter});

        // データベースを閉じる
        db.close();

//        // CreditSettingDatabaseHelper を初期化
//        dbHelper = new CreditDatabaseHelper(this);
//
//        SQLiteDatabase db = null;
//        try {
//            db = dbHelper.getWritableDatabase();
//            if (!isTableExists(db, CreditDatabaseHelper.TABLE_NAME)) {
//                Log.e("DatabaseError", "sales テーブルが存在しません");
//            }
//
//            // 会員IDを取得するロジックをここに追加
//            String memberId = "1";
//
//            // レコードが存在しない場合のみ挿入
//            dbHelper.insertIfNotExists(memberId);
//
//            // 特定の memberId のクレカ情報を取得
//            int cardNumber = getCardNumber(memberId, db);
//            int expirationDate = getExpirationDate(memberId, db);
//            int cardCVCCVV = getCardCVCCVV(memberId, db);
//            String cardName = getCardName(memberId, db);
//
//            // EditText に取得した情報を表示
//            editTextCardNumber.setText(String.valueOf(cardNumber));
//            editTextExpirationDate.setText(String.valueOf(expirationDate));
//            editTextCVCCVV.setText(String.valueOf(cardCVCCVV));
//            editTextName.setText(cardName);
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }

    }

    private String getCardNumber(String userId, SQLiteDatabase db) {
        String cardNumber = null; // 見つからない場合の初期値

        // クエリを準備
        String query = "SELECT " + CreditDatabaseHelper.CARD_NUMBER +
                " FROM " + CreditDatabaseHelper.TABLE_NAME +
                " WHERE " + CreditDatabaseHelper.MEMBER_ID + " = ?";

        // クエリを実行
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        try {
            if (cursor.moveToFirst()) {
                // カラムのインデックスを取得して、カード番号を取得
                cardNumber = cursor.getString(cursor.getColumnIndexOrThrow(CreditDatabaseHelper.CARD_NUMBER));
                Log.d("DatabaseDebug", "Retrieved Value: " + cardNumber); // デバッグ用
            }
        } finally {
            // 必ずカーソルを閉じる
            cursor.close();
        }
        return cardNumber; // カード番号を返す（見つからない場合は null）
    }

    private String getExpirationDate(String userId, SQLiteDatabase db) {
        String expirationDate = null; // 見つからない場合の初期値

        // クエリを準備
        String query = "SELECT " + CreditDatabaseHelper.EXPIRATION_DATE +
                " FROM " + CreditDatabaseHelper.TABLE_NAME +
                " WHERE " + CreditDatabaseHelper.MEMBER_ID + " = ?";

        // クエリを実行
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        try {
            if (cursor.moveToFirst()) {
                // カラムのインデックスを取得して、カード番号を取得
                expirationDate = cursor.getString(cursor.getColumnIndexOrThrow(CreditDatabaseHelper.EXPIRATION_DATE));
            }
        } finally {
            // 必ずカーソルを閉じる
            cursor.close();
        }
        return expirationDate; // 有効期限を返す（見つからない場合は null）
    }

    private String getCardCVCCVV(String userId, SQLiteDatabase db) {
        String cardCVCCVV = null; // 見つからない場合の初期値

        // クエリを準備
        String query = "SELECT " + CreditDatabaseHelper.CARD_CVCCVV +
                " FROM " + CreditDatabaseHelper.TABLE_NAME +
                " WHERE " + CreditDatabaseHelper.MEMBER_ID + " = ?";

        // クエリを実行
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        try {
            if (cursor.moveToFirst()) {
                // カラムのインデックスを取得して、カード番号を取得
                cardCVCCVV = cursor.getString(cursor.getColumnIndexOrThrow(CreditDatabaseHelper.CARD_CVCCVV));
            }
        } finally {
            // 必ずカーソルを閉じる
            cursor.close();
        }
        return cardCVCCVV; // CVC/CVVを返す（見つからない場合は null）
    }

    private String getCardName(String userId, SQLiteDatabase db) {
        String cardName = null; // 見つからない場合の初期値

        // クエリを準備
        String query = "SELECT " + CreditDatabaseHelper.CARD_NAME +
                " FROM " + CreditDatabaseHelper.TABLE_NAME +
                " WHERE " + CreditDatabaseHelper.MEMBER_ID + " = ?";

        // クエリを実行
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        try {
            if (cursor.moveToFirst()) {
                // カラムのインデックスを取得して、カード番号を取得
                cardName = cursor.getString(cursor.getColumnIndexOrThrow(CreditDatabaseHelper.CARD_NAME));
            }
        } finally {
            // 必ずカーソルを閉じる
            cursor.close();
        }
        return cardName; // 名前を返す（見つからない場合は NULL）
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName}
        );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            Intent intent = new Intent(getApplication(), Setting.class);
            startActivity(intent);
        } else if (v.getId() == R.id.updateButton) {

            // EditText に入力された文字列を取得
            String cardNumber = editTextCardNumber.getText().toString().trim();
            String expirationDate = editTextExpirationDate.getText().toString().trim();
            String cardCVCCVV = editTextCVCCVV.getText().toString().trim();
            String name = editTextName.getText().toString().trim();

            Log.d("DatabaseDebug", "Input Value: " + cardNumber);

            // 記入漏れがないかの処理
//            if (cardNumber.isEmpty()) {
//                // 入力されていない場合のエラー処理
//                editTextCardNumber.setError("カード番号を入力してください");
//                return;
//            } else if (expirationDate.isEmpty()) {
//                // 入力されていない場合のエラー処理
//                editTextExpirationDate.setError("有効期限を入力してください");
//                return;
//            } else if (cardCVCCVV.isEmpty()) {
//                // 入力されていない場合のエラー処理
//                editTextCVCCVV.setError("CVC/CVVを入力してください");
//                return;
//            } else if (name.isEmpty()) {
//                // 入力されていない場合のエラー処理
//                editTextName.setError("名前を入力してください");
//                return;
//            }

            // 入力内容のバリデーション
            if (!validateCardNumber(cardNumber)) {
                editTextCardNumber.setError("カード番号は16桁で入力してください");
                return;
            }

            if (!validateExpirationDate1(expirationDate)) {
                editTextExpirationDate.setError("有効期限は4桁で入力してください");
                return;
            }

            if (!validateExpirationDate2(expirationDate)) {
                editTextExpirationDate.setError("有効期限が不正です（例: MMYY 形式、有効な月、未来の日付）");
                return;
            }

            if (!validateCVCCVV(cardCVCCVV)) {
                editTextCVCCVV.setError("CVC/CVVは3桁または4桁で入力してください");
                return;
            }

            if (name.isEmpty()) {
                editTextName.setError("名前を入力してください");
                return;
            }

//            String memberId = "1"; // 会員IDを取得するロジックを追加

//            CreditDatabaseHelper dbHelper = new CreditDatabaseHelper(this);

            // dbHelper が null でないことを確認
            if (dbHelper != null) {
                dbHelper.updateCreditData(userId, cardNumber, expirationDate, cardCVCCVV, name);
            } else {
                Log.e("CreditSetting", "CreditDatabaseHelper is null");
            }

            // 記入漏れがなければ決済情報画面へ
            Intent intent = new Intent(getApplication(), CreditSetting.class);
            startActivity(intent);
        }
    }

    // カード番号のバリデーション（16桁）
    private boolean validateCardNumber(String cardNumber) {
        return cardNumber.matches("^\\d{16}$");
    }

    // 有効期限のバリデーション
    private boolean validateExpirationDate1(String expirationDate) {
        return expirationDate.matches("^\\d{4}$");
    }

    private boolean validateExpirationDate2(String expirationDate) {

        // 月（MM）と年（YY）を抽出
        int month = Integer.parseInt(expirationDate.substring(0, 2));
        int year = Integer.parseInt(expirationDate.substring(2, 4)) + 2000; // YYを20xx形式に変換

        // 月が1～12の範囲内か確認
        if (month < 1 || month > 12) {
            return false;
        }

        // 現在の年月を取得
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int currentYear = calendar.get(java.util.Calendar.YEAR);
        int currentMonth = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTHは0から始まる

        // 有効期限が現在以降であることを確認
        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return false; // 過去の日付の場合
        }

        return true; // 妥当な有効期限
    }

    // CVC/CVVのバリデーション（3桁または4桁）
    private boolean validateCVCCVV(String cardCVCCVV) {
        return cardCVCCVV.matches("^\\d{3,4}$");
    }
}