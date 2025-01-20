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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton; // ログインボタン
    private Button deliveryButton; // 配送業者ボタン
    private Button signUpButton; // 新規会員登録ボタン
    private EditText editTextUserName; // 会員者ID入力フィールド
    private EditText editTextPassword; // パスワード入力フィールド

    private DatabaseHelper dbHelper; // DatabaseHelperのインスタンス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // レイアウトファイル名を指定

        // DatabaseHelperのインスタンスを初期化
        dbHelper = new DatabaseHelper(this);

        // ボタンの初期化
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        deliveryButton = findViewById(R.id.deliveryButton);
        deliveryButton.setOnClickListener(this);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        // EditTextの初期化
        editTextUserName = findViewById(R.id.editTextUserName); // 会員者ID入力フィールド
        editTextPassword = findViewById(R.id.editTextPassword); // パスワード入力フィールド
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            // EditTextから会員者ID（userId）とパスワードを取得
            String userId = editTextUserName.getText().toString();
            String password = editTextPassword.getText().toString();

            if (!userId.isEmpty() && !password.isEmpty()) {
                // ログインをデータベースで検証
                boolean isValid = dbHelper.validateLoginByUserId(userId, password);

                if (isValid) {
                    // ログイン成功の場合、ホーム画面に遷移
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id", userId);  // 会員者IDを保存
                    editor.apply();
                    Intent intent = new Intent(this, ParentCertification.class);
                    startActivity(intent);
                } else {
                    // ログイン失敗の場合、エラーメッセージを表示
                    Toast.makeText(this, "パスワードが一致しません", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 入力が不足している場合、エラーメッセージを表示
                Toast.makeText(this, "Please enter both user ID and password", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.signUpButton) {
            // 新規会員登録画面へ遷移
            Intent intent = new Intent(this, Rule.class);
            startActivity(intent);
        } else if (v.getId() == R.id.deliveryButton) {
            // 配送業者ボタンが押された時の処理
            Intent intent = new Intent(this, LoginH.class);
            startActivity(intent);
        }
    }
}
