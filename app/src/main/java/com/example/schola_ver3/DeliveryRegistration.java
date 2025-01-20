package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.korekore.R;

public class DeliveryRegistration extends AppCompatActivity {

    private EditText kaiinnameEditText, emailEditText, phoneEditText, passwordEditText;
    private Button uploadButton, registerButton;
    private String selectedImagePath = ""; // アップロードされた画像のパス

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registration); // activity_delivery_registration.xml をレイアウトとして設定

        // UIの初期化
        kaiinnameEditText = findViewById(R.id.kaiinname);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        uploadButton = findViewById(R.id.uploadButton);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);

        // 登録ボタンを最初は無効にする
        registerButton.setEnabled(false);

        // 入力フィールドの変更をリアルタイムで監視
        setupTextWatchers();

        // 画像アップロードボタンのクリックリスナー
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // 登録ボタンのクリックリスナー
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputFields()) {
                    saveDeliveryInfoToDatabase();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LoginH.java に画面遷移
                Intent intent = new Intent(DeliveryRegistration.this, LoginH.class);
                startActivity(intent);
                finish(); // 現在の画面を終了
            }
        });
    }

    // 入力フィールドの変更をリアルタイムで監視
    private void setupTextWatchers() {
        // 配送業者名のTextWatcher
        kaiinnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateKaiinname(s.toString().trim());
                checkInputFields(); // すべてのフィールドをチェック
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // メールアドレスのTextWatcher
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString().trim());
                checkInputFields(); // すべてのフィールドをチェック
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 電話番号のTextWatcher
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhone(s.toString().trim());
                checkInputFields(); // すべてのフィールドをチェック
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // パスワードのTextWatcher
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(s.toString().trim());
                checkInputFields(); // すべてのフィールドをチェック
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // 配送業者名のリアルタイム検証
    private void validateKaiinname(String kaiinname) {
        if (TextUtils.isEmpty(kaiinname)) {
            kaiinnameEditText.setError("配送業者名を入力してください");
        } else {
            kaiinnameEditText.setError(null);
        }
    }

    // メールアドレスのリアルタイム検証
    private void validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("メールアドレスを入力してください");
        } else if (!isValidEmail(email)) {
            emailEditText.setError("有効なメールアドレスを入力してください");
        } else {
            emailEditText.setError(null);
        }
    }

    // 電話番号のリアルタイム検証
    private void validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("電話番号を入力してください");
        } else if (!isValidPhone(phone)) {
            phoneEditText.setError("電話番号は11桁または12桁の数字で入力してください");
        } else {
            phoneEditText.setError(null);
        }
    }

    // パスワードのリアルタイム検証
    private void validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("パスワードを入力してください");
        } else if (!isValidPassword(password)) {
            passwordEditText.setError("半角英数記号8～16文字で入力してください");
        } else {
            passwordEditText.setError(null);
        }
    }

    // すべてのフィールドが有効かどうかをチェック
    // すべてのフィールドが有効かどうかをチェック
    private boolean checkInputFields() {
        String kaiinname = kaiinnameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean isValid = !TextUtils.isEmpty(kaiinname) &&
                !TextUtils.isEmpty(email) && isValidEmail(email) &&
                !TextUtils.isEmpty(phone) && isValidPhone(phone) &&
                !TextUtils.isEmpty(password) && isValidPassword(password);

        // 登録ボタンの有効/無効を切り替え
        registerButton.setEnabled(isValid);

        return isValid; // 検証結果を返す
    }

    // メールアドレスのバリデーション
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 電話番号のバリデーション
    private boolean isValidPhone(String phone) {
        return phone.matches("^[0-9]{11,12}$"); // 10桁または11桁の数字
    }

    // パスワードのバリデーション
    private boolean isValidPassword(String password) {
        String passwordPattern = "^[a-z0-9!-/:-@\\[-`{-~]{8,16}$";
        return password.matches(passwordPattern);
    }

    // 画像選択用のインテントを開始
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // 画像選択の結果を取得
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // 画像のURIを取得
            selectedImagePath = data.getData().toString();
            Log.d("DeliveryRegistration", "Selected Image Path: " + selectedImagePath);
            Toast.makeText(this, "画像が選択されました", Toast.LENGTH_SHORT).show();
        }
    }

    // 配送業者情報をデータベースに保存
    private void saveDeliveryInfoToDatabase() {
        String kaiinname = kaiinnameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 配送業者IDを自動生成
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String deliveryId = dbHelper.generateDeliveryId();

        // データベースに保存
        boolean result = dbHelper.insertDeliveryInfo(deliveryId, kaiinname, email, phone, password, selectedImagePath);

        if (result) {
            Toast.makeText(this, "配送業者情報が保存されました", Toast.LENGTH_SHORT).show();

            // メール送信
            sendRegistrationEmail(email, deliveryId);

            // ログイン画面に遷移
            Intent intent = new Intent(this, DeliveryComp.class);
            startActivity(intent);
            finish(); // 現在の画面を終了
        } else {
            Toast.makeText(this, "配送業者情報の保存に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    // メール送信
    private void sendRegistrationEmail(String toEmail, String deliveryId) {
        String subject = "配送業者登録完了のお知らせ";
        String body = "配送業者登録が完了しました。\n\n"
                + "配送業者ID: " + deliveryId + "\n\n"
                + "このIDをログイン時に使用してください。";

        // メール送信を別スレッドで実行
        new Thread(() -> {
            try {
                EmailSender.sendEmail(toEmail, subject, body);
                runOnUiThread(() -> Toast.makeText(DeliveryRegistration.this, "登録情報をメールで送信しました。", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(DeliveryRegistration.this, "メール送信に失敗しました。", Toast.LENGTH_SHORT).show());
                Log.e("DeliveryRegistration", "Failed to send email", e);
            }
        }).start();
    }
}