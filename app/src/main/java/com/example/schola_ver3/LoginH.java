package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.korekore.R;

public class LoginH extends AppCompatActivity {

    private EditText editTextUserName, editTextPassword;
    private Button loginButton, signUpButton, hereButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginh); // activity_login.xml をレイアウトとして設定

        // UIの初期化
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        hereButton = findViewById(R.id.hereButton);

        // ログインボタンのクリックリスナー
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = editTextUserName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (userId.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginH.this, "配送業者IDとパスワードを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ログイン処理を実行
                performLogin(userId, password);
            }
        });

        // 新規登録ボタンのクリックリスナー
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DeliveryRegistration.java に画面遷移
                Intent intent = new Intent(LoginH.this, DeliveryRegistration.class);
                startActivity(intent);
            }
        });

        hereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginH.this, Login.class);
                startActivity(intent);
            }
        });
    }

    // ログイン処理
    private void performLogin(String userId, String password) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // 配送業者IDとパスワードを照合
        boolean isValid = dbHelper.validateDeliveryCredentials(userId, password);

        if (isValid) {
            // ログイン成功
            Toast.makeText(this, "ログイン成功", Toast.LENGTH_SHORT).show();

            // 配送業者IDを SharedPreferences に保存
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("delivery_id", userId); // 配送業者IDを保存
            editor.apply();

            // メイン画面に遷移（例: DeliveryHomePage）
            Intent intent = new Intent(LoginH.this, DeliveryHomePage.class);
            startActivity(intent);
            finish(); // 現在の画面を終了
        } else {
            // ログイン失敗
            Toast.makeText(this, "配送業者IDまたはパスワードが間違っています", Toast.LENGTH_SHORT).show();
        }
    }
}