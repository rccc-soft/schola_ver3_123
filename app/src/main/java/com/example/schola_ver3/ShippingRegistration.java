package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShippingRegistration extends AppCompatActivity implements View.OnClickListener {

    private EditText userid, username, ubinnumber, adress, telnumber;
    private Button registerButton;
    private ImageButton backButton;
//    private TourokuDatabaseHelper dbHelper;
    private DatabaseHelper dbHelper;

    private String deliveryAddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_registration);

        // UI部品の初期化
        userid = findViewById(R.id.userid);
        username = findViewById(R.id.username);
        ubinnumber = findViewById(R.id.ubinnumber);
        adress = findViewById(R.id.adress);
        telnumber = findViewById(R.id.telnumber);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);

        // SharedPreferences から会員者IDを取得
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");

        if (!userId.isEmpty()) {
            // DatabaseHelperインスタンス作成
//            dbHelper = new TourokuDatabaseHelper(this);
            dbHelper = new DatabaseHelper(this);
            Cursor cursor = dbHelper.getMemberInfo(userId);

            if (cursor != null && cursor.moveToFirst()) {
                // 各カラムのインデックスを取得
//                int useridIndex = cursor.getColumnIndex(TourokuDatabaseHelper.COLUMN_USER_ID);
//                int usernameIndex = cursor.getColumnIndex(TourokuDatabaseHelper.COLUMN_NAME);
//                int ubinnumberIndex = cursor.getColumnIndex(TourokuDatabaseHelper.COLUMN_UBIN_NUMBER);
//                int adressIndex = cursor.getColumnIndex(TourokuDatabaseHelper.COLUMN_ADRESS);
//                int telnumberIndex = cursor.getColumnIndex(TourokuDatabaseHelper.COLUMN_TEL_NUMBER);
                int useridIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMBER_ID);
                int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMBER_NAME);
                int ubinnumberIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_POSTAL_CODE);
                int adressIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS);
                int telnumberIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER);

                // データを取得して EditText にセット
                if (useridIndex != -1) {
                    String memberId = cursor.getString(useridIndex);
                    userid.setText(memberId);
                    userid.setEnabled(false); // 編集不可にする
                }

                if (ubinnumberIndex != -1) {
                    String ubinnumberValue = cursor.getString(ubinnumberIndex);
                    ubinnumber.setText(ubinnumberValue);
                }

                if (usernameIndex != -1) {
                    String usernameValue = cursor.getString(usernameIndex);
                    username.setText(usernameValue);
                }

                if (adressIndex != -1) {
                    String adressValue = cursor.getString(adressIndex);
                    adress.setText(adressValue);
                }

                if (telnumberIndex != -1) {
                    String phoneValue = cursor.getString(telnumberIndex);
                    telnumber.setText(phoneValue);
                }

                cursor.close(); // Cursorを閉じる
            }
        }

        // ボタンにクリックリスナーを設定
        registerButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        // 入力項目が変更されるたびにチェックを行う
        userid.addTextChangedListener(textWatcher);
        username.addTextChangedListener(textWatcher);
        ubinnumber.addTextChangedListener(textWatcher);
        adress.addTextChangedListener(textWatcher);
        telnumber.addTextChangedListener(textWatcher);
    }

    // 入力内容が変更された際に、すべてのフィールドが入力されているかをチェック
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs(); // 入力内容が変更されるたびに確認
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    // 入力項目がすべて入力されているかを確認
    private void checkInputs() {
        boolean isValid = true;

        String useridText = userid.getText().toString().trim();
        String usernameText = username.getText().toString().trim();
        String ubinnumberText = ubinnumber.getText().toString().trim();
        String adressText = adress.getText().toString().trim();
        String telnumberText = telnumber.getText().toString().trim();

        if (usernameText.isEmpty()) {
            username.setError("ユーザー名を入力してください");
            isValid = false;
        } else {
            username.setError(null);
        }

        // 郵便番号のチェック
        if (ubinnumberText.isEmpty() || !isValidUbinnumber(ubinnumberText)) {
            ubinnumber.setError("有効な郵便番号を入力してください");
            isValid = false;
        } else {
            ubinnumber.setError(null);
        }

        //住所のチェック
        if (adressText.isEmpty()) {
            adress.setError("住所を入力してください");
            isValid = false;
        } else {
            adress.setError(null);
        }

        // 電話番号のチェック
        if (telnumberText.isEmpty() || !isValidTelnumber(telnumberText)) {
            telnumber.setError("有効な電話番号を入力してください");
            isValid = false;
        } else {
            telnumber.setError(null);
        }
        // すべてのフィールドが有効な場合に登録ボタンを有効化
        registerButton.setEnabled(isValid);
    }

    // 郵便番号が正しいかを確認（7桁）
    private boolean isValidUbinnumber(String ubinnumber) {
        return ubinnumber.matches("^\\d{7}$"); // 郵便番号が7桁の数字であるか
    }

    // 電話番号が正しいかを確認（11桁の日本国内形式を仮定）
    private boolean isValidTelnumber(String telnumber) {
        return telnumber.matches("^\\d{11}$"); // 電話番号が11桁の数字であるか
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerButton) {
            try {
                // 配送先IDを生成
                deliveryAddressId = dbHelper.generateDeliveryAddressId();
                Log.d("ShippingRegistration", "Generated Delivery Address ID: " + deliveryAddressId);

                // 入力された内容を取得
                String useridText = userid.getText().toString().trim();
                String usernameText = username.getText().toString().trim();
                String ubinnumberText = ubinnumber.getText().toString().trim();
                String adressText = adress.getText().toString().trim();
                String telnumberText = telnumber.getText().toString().trim();

                Log.d("ShippingRegistration", "Attempting to insert/update delivery address:");
                Log.d("ShippingRegistration", "User ID: " + useridText);
                Log.d("ShippingRegistration", "Username: " + usernameText);
                Log.d("ShippingRegistration", "Postal Code: " + ubinnumberText);
                Log.d("ShippingRegistration", "Address: " + adressText);
                Log.d("ShippingRegistration", "Phone Number: " + telnumberText);
                // DatabaseHelperインスタンスを作成
//                dbHelper = new TourokuDatabaseHelper(this);
                dbHelper = new DatabaseHelper(this);

                // 新しい会員情報をデータベースに登録
//                boolean isRegistered = dbHelper.addDeliveryAdress(useridText, usernameText, ubinnumberText, adressText,
//                        telnumberText);
                boolean isRegistered = dbHelper.insertDeliveryAddress(deliveryAddressId, useridText, usernameText, ubinnumberText, adressText,
                        telnumberText);

                if (isRegistered) {
                    // 登録成功時、成功画面に遷移
                    Intent intent = new Intent(this, ShippingRegistration.class);
                    startActivity(intent);
                } else {
                    // 登録失敗時
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // エラー処理
                Log.e("MemberRegistration", "Error during registration", e);
                Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.backButton) {
            // ログイン画面に戻る
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
        }
    }
}

