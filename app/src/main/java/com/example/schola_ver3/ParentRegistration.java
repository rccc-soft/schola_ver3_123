package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.korekore.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParentRegistration extends AppCompatActivity {

    private EditText parentNameEditText, parentFuriganaEditText, parentEmailEditText, parentPhoneEditText, parentPasswordEditText;
    private Button registerButton;
    private Button uploadButton; // 画像アップロード用ボタン
    private String selectedImagePath = ""; // 選択された画像のパス


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_registration);

        // UIの初期化
        parentNameEditText = findViewById(R.id.parentname);
        parentFuriganaEditText = findViewById(R.id.parenthurigana);
        parentEmailEditText = findViewById(R.id.parentemail);
        parentPhoneEditText = findViewById(R.id.parentphone);
        parentPasswordEditText = findViewById(R.id.parentpassword);
        registerButton = findViewById(R.id.parentregisterButton);
        uploadButton = findViewById(R.id.parentuploadButton); // 画像アップロード用ボタン

        // 登録ボタンの初期状態を無効にする
        registerButton.setEnabled(false);

        // 画像アップロードボタンのクリックリスナーを設定
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // 各入力フィールドにTextWatcherを設定
        setupTextWatchers(); // この行を追加

        // 登録ボタンのクリックリスナーを設定
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputFields()) {
                    saveParentInfoToDatabase();
                }
            }
        });
    }

    // 各入力フィールドにTextWatcherを設定
    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputFields(); // テキストが変更されるたびにチェック
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        parentNameEditText.addTextChangedListener(textWatcher);
        parentFuriganaEditText.addTextChangedListener(textWatcher);
        parentEmailEditText.addTextChangedListener(textWatcher);
        parentPhoneEditText.addTextChangedListener(textWatcher);
        parentPasswordEditText.addTextChangedListener(textWatcher);
    }

    // 画像選択用のインテントを開始
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // 入力フィールドのチェック
    // 入力フィールドのチェック
    private boolean checkInputFields() {
        boolean isValid = true;
        String parentName = parentNameEditText.getText().toString().trim();
        String parentFurigana = parentFuriganaEditText.getText().toString().trim();
        String parentEmail = parentEmailEditText.getText().toString().trim();
        String parentPhone = parentPhoneEditText.getText().toString().trim();
        String parentPassword = parentPasswordEditText.getText().toString().trim();

        // 保護者名のチェック
        if (TextUtils.isEmpty(parentName)) {
            parentNameEditText.setError("保護者名を入力してください");
            Log.d("ParentRegistration", "保護者名が空です");
            isValid = false;
        } else {
            parentNameEditText.setError(null);
        }

        // ふりがなのチェック
        if (TextUtils.isEmpty(parentFurigana)) {
            parentFuriganaEditText.setError("ひらがなを入力してください");
            Log.d("ParentRegistration", "ふりがなが空です");
            isValid = false;
        } else if (!isValidHiragana(parentFurigana)) {
            parentFuriganaEditText.setError("ひらがなのみ入力してください");
            Log.d("ParentRegistration", "ふりがながひらがなではありません");
            isValid = false;
        } else {
            parentFuriganaEditText.setError(null);
        }

        // メールアドレスのチェック
        if (TextUtils.isEmpty(parentEmail) || !isValidEmail(parentEmail)) {
            parentEmailEditText.setError("有効なメールアドレスを入力してください");
            Log.d("ParentRegistration", "メールアドレスが無効です");
            isValid = false;
        } else {
            parentEmailEditText.setError(null);
        }

        // 電話番号のチェック
        if (TextUtils.isEmpty(parentPhone) || !isValidPhone(parentPhone)) {
            parentPhoneEditText.setError("有効な電話番号を入力してください");
            Log.d("ParentRegistration", "電話番号が無効です");
            isValid = false;
        } else {
            parentPhoneEditText.setError(null);
        }

        // パスワードのチェック
        if (TextUtils.isEmpty(parentPassword) || !isValidPassword(parentPassword)) {
            parentPasswordEditText.setError("半角英数記号で8～16文字で入力してください");
            Log.d("ParentRegistration", "パスワードが無効です");
            isValid = false;
        } else {
            parentPasswordEditText.setError(null);
        }

        // すべての必須項目が正しく入力されているか確認
        if (isValid) {
            registerButton.setEnabled(true); // 登録ボタンを有効にする
            Log.d("ParentRegistration", "すべての入力が有効です");
        } else {
            registerButton.setEnabled(false); // 登録ボタンを無効にする
            Log.d("ParentRegistration", "入力に問題があります");
        }

        return isValid;
    }

    // メールアドレスのバリデーション
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // 電話番号のバリデーション
    private boolean isValidPhone(String phone) {
        return phone.matches("^[0-9]{10,11}$"); // 10桁または11桁の数字
    }

    // パスワードのバリデーション
    private boolean isValidPassword(String password) {
        String passwordPattern = "^[a-z0-9!-/:-@\\[-`{-~]{8,16}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // ひらがなのバリデーション
    private boolean isValidHiragana(String input) {
        return input.matches("^[ぁ-んー]*$"); // ひらがなのみ許可
    }

    // 保護者情報をデータベースに保存
    // 保護者情報をデータベースに保存
    // 保護者情報をデータベースに保存
    private void saveParentInfoToDatabase() {
        // ログイン情報から会員者IDを取得
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "ログイン情報がありません。再度ログインしてください。", Toast.LENGTH_SHORT).show();
            return;
        }

        // 入力内容を取得
        String parentName = parentNameEditText.getText().toString().trim();
        String parentFurigana = parentFuriganaEditText.getText().toString().trim();
        String parentEmail = parentEmailEditText.getText().toString().trim();
        String parentPhone = parentPhoneEditText.getText().toString().trim();
        String parentPassword = parentPasswordEditText.getText().toString().trim();
        String parentDocuments = selectedImagePath; // 選択された画像のパスを保存

        // データベースに保存
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        boolean result = dbHelper.insertParentInfo(userId, parentName, parentFurigana, parentEmail, parentPhone, parentPassword, parentDocuments);

        if (result) {
            Toast.makeText(this, "保護者情報が保存されました。", Toast.LENGTH_SHORT).show();

            // メール送信
            sendRegistrationEmail(parentEmail, parentPassword);

            // 次の画面に遷移（例: 保護者用画面）
            Intent intent = new Intent(ParentRegistration.this, MemberComp.class);
            startActivity(intent);
            finish(); // 現在のアクティビティを終了
        } else {
            Toast.makeText(this, "保護者情報の保存に失敗しました。", Toast.LENGTH_SHORT).show();
        }
    }

    // メール送信
    private void sendRegistrationEmail(String toEmail, String password) {
        String subject = "登録完了のお知らせ";
        String body = "保護者登録が完了しました。\n\n"
                + "メールアドレス: " + toEmail + "\n"
                + "パスワード: " + password + "\n\n"
                + "この情報を大切に保管してください。";

        // メール送信を別スレッドで実行
        new Thread(() -> {
            try {
                EmailSender.sendEmail(toEmail, subject, body);
                runOnUiThread(() -> Toast.makeText(ParentRegistration.this, "登録情報をメールで送信しました。", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(ParentRegistration.this, "メール送信に失敗しました。", Toast.LENGTH_SHORT).show());
                Log.e("ParentRegistration", "Failed to send email", e);
            }
        }).start();
    }



    // onCreateメソッド内で、各EditTextにTextWatcherを設定


}