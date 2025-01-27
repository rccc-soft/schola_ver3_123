package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeliveryView extends AppCompatActivity {

    private ImageButton backButton;
    private EditText deliveryIdEditText, deliveryNameEditText, emailEditText, phoneEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_reading); // activity_delivery_reading.xml をレイアウトとして設定

        // UIの初期化
        backButton = findViewById(R.id.backButton);
        deliveryIdEditText = findViewById(R.id.deliveryid);
        deliveryNameEditText = findViewById(R.id.deliveryname);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);

        // 配送業者情報をデータベースから取得して表示
        loadDeliveryInfo();

        // backButtonのクリックリスナー
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DeliveryHomePage.java に画面遷移
                Intent intent = new Intent(DeliveryView.this, DeliveryHomePage.class);
                startActivity(intent);
                finish(); // 現在の画面を終了
            }
        });
    }

    // 配送業者情報をデータベースから取得して表示するメソッド
    private void loadDeliveryInfo() {
        // 現在ログイン中の配送業者IDを取得
        String deliveryId = getCurrentDeliveryId();

        if (deliveryId == null || deliveryId.isEmpty()) {
            Toast.makeText(this, "ログイン情報が見つかりません", Toast.LENGTH_SHORT).show();
            return;
        }

        // DatabaseHelper のインスタンスを作成
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // 配送業者情報をデータベースから取得
        Cursor cursor = dbHelper.getDeliveryInfo(deliveryId);

        if (cursor != null && cursor.moveToFirst()) {
            // 各カラムのインデックスを取得
            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_ID);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_NAME);
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_EMAIL);
            int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_PHONE);
            int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_PASSWORD);

            // カラムが存在するか確認してから値を取得
            if (idIndex != -1) {
                deliveryIdEditText.setText(cursor.getString(idIndex));
            }
            if (nameIndex != -1) {
                deliveryNameEditText.setText(cursor.getString(nameIndex));
            }
            if (emailIndex != -1) {
                emailEditText.setText(cursor.getString(emailIndex));
            }
            if (phoneIndex != -1) {
                phoneEditText.setText(cursor.getString(phoneIndex));
            }
            if (passwordIndex != -1) {
                passwordEditText.setText(cursor.getString(passwordIndex));
            }

            // カーソルを閉じる
            cursor.close();
        } else {
            // データが見つからない場合の処理
            Toast.makeText(this, "配送業者情報が見つかりません", Toast.LENGTH_SHORT).show();
        }

        // 入力不可にする
        deliveryIdEditText.setEnabled(false);
        deliveryNameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        phoneEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
    }

    // 現在ログイン中の配送業者IDを取得するメソッド
    private String getCurrentDeliveryId() {
        // SharedPreferences から配送業者IDを取得
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("delivery_id", null); // キー名は適宜変更
    }
}