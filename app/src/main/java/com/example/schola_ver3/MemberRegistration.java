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

import com.example.korekore.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberRegistration extends AppCompatActivity implements View.OnClickListener {

    private EditText username, password, email, phone, hurigana, kaiinname, birthday, school, profileMessage;
    private Button uploadButton, registerButton;
    private ImageButton backButton;

    private String selectedImagePath = ""; // 画像パス用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_regisitration);

        // UIの初期化
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        hurigana = findViewById(R.id.hurigana);  // ふりがな
        kaiinname = findViewById(R.id.kaiinname);  // 会員名
        birthday = findViewById(R.id.birthday);  // 生年月日
        school = findViewById(R.id.school);  // 学校
        profileMessage = findViewById(R.id.profileMessage);  // プロフィールメッセージ

        uploadButton = findViewById(R.id.uploadButton);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);

        uploadButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        // registerButtonを最初は無効にしておく
        registerButton.setEnabled(false);

        // 入力フィールドのリスナーを設定
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        phone.addTextChangedListener(textWatcher);
        hurigana.addTextChangedListener(textWatcher);
        kaiinname.addTextChangedListener(textWatcher);
        birthday.addTextChangedListener(textWatcher);
        school.addTextChangedListener(textWatcher);
        profileMessage.addTextChangedListener(textWatcher);
    }

    // 入力内容が変更された際に、すべてのフィールドが入力されているかをチェック
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs(); // 入力内容が変更されるたびに確認
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // 入力項目がすべて入力されているかを確認
    private void checkInputs() {
        boolean isValid = true;

        String usernameText = username.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String huriganaText = hurigana.getText().toString().trim();
        String kaiinnameText = kaiinname.getText().toString().trim();
        String birthdayText = birthday.getText().toString().trim();
        String schoolText = school.getText().toString().trim();

        // ユーザー名のチェック
        if (usernameText.isEmpty()) {
            username.setError("ユーザー名を入力してください");
            isValid = false;
        } else {
            username.setError(null);
        }

        // メールアドレスのチェック
        if (emailText.isEmpty() || !isValidEmail(emailText)) {
            email.setError("有効なメールアドレスを入力してください");
            isValid = false;
        } else {
            email.setError(null);
        }

        // 電話番号のチェック
        if (phoneText.isEmpty() || !isValidPhone(phoneText)) {
            phone.setError("有効な電話番号を入力してください");
            isValid = false;
        } else {
            phone.setError(null);
        }

        // パスワードのチェック
        if (!isValidPassword(passwordText)) {
            password.setError("半角英数記号で8～16文字で入力してください");
            isValid = false;
        } else {
            password.setError(null);
        }

        // ふりがなのチェック
        if (huriganaText.isEmpty()) {
            hurigana.setError("ひらがなを入力してください");
            isValid = false;
        }  else {
            hurigana.setError(null);
        }

        // 会員名のチェック
        if (kaiinnameText.isEmpty()) {
            kaiinname.setError("会員名を入力してください");
            isValid = false;
        } else {
            kaiinname.setError(null);
        }

        // 生年月日のチェック
        if (birthdayText.isEmpty()|| !isValidDate(birthdayText)) {
            birthday.setError("適切な生年月日を入力してください");
            isValid = false;
        } else {
            birthday.setError(null);
        }

        // 学校のチェック
        if (schoolText.isEmpty()) {
            school.setError("現在通っている学校名を入力してください");
            isValid = false;
        } else {
            school.setError(null);
        }

        // すべてのフィールドが有効な場合に登録ボタンを有効化
        registerButton.setEnabled(isValid);
    }

    // メールアドレスのフォーマットが正しいかを確認
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // 電話番号が正しいかを確認（11桁の日本国内形式を仮定）
    private boolean isValidPhone(String phone) {
        return phone.matches("^\\d{11}$"); // 電話番号が11桁の数字であるか
    }

    // 会員者IDを生成（"u" + ランダムな英数字7文字）
    private String generateUserId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder userId = new StringBuilder("u");
        for (int i = 0; i < 7; i++) {
            int randomIndex = random.nextInt(chars.length());
            userId.append(chars.charAt(randomIndex));
        }
        return userId.toString();
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setLenient(false); // 厳密な日付チェックを行う
        try {
            sdf.parse(date); // 日付をパース
            return true; // 有効な日付の場合
        } catch (ParseException e) {
            return false; // 無効な日付の場合
        }
    }

    private boolean isValidPassword(String password) {
        // 半角英数記号で8～16文字の正規表現
        String passwordPattern = "^[a-z0-9!-/:-@\\[-`{-~]{8,16}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerButton) {
            try {
                // 入力された内容を取得
                String usernameText = username.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String phoneText = phone.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String huriganaText = hurigana.getText().toString().trim();
                String kaiinnameText = kaiinname.getText().toString().trim();
                String birthdayText = birthday.getText().toString().trim();
                String schoolText = school.getText().toString().trim();
                String profileMessageText = profileMessage.getText().toString().trim();

                // プロフィールメッセージが空の場合、空文字を設定
                if (profileMessageText.isEmpty()) {
                    profileMessageText = "";
                }

                // 画像パスが設定されていない場合、空文字を設定
                String profileImage = selectedImagePath.isEmpty() ? "" : selectedImagePath;

                // 会員者IDを生成
                String userId = generateUserId();

                // 会員者IDを SharedPreferences に保存
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id", userId); // 会員者IDを保存
                editor.apply();

                // DatabaseHelperインスタンスを作成
                DatabaseHelper dbHelper = new DatabaseHelper(this);

                // 新しい会員情報をデータベースに登録
                boolean isRegistered = dbHelper.addNewMember(userId, usernameText, emailText, phoneText,
                        passwordText, profileImage, profileMessageText,
                        huriganaText, kaiinnameText, birthdayText, schoolText);

                if (isRegistered) {
                    // 誕生日が現在の日時より18年前以降かどうかを判定
                    if (isOver18(birthdayText)) {
                        // 18歳以上の場合
                        sendEmailConfirmation(userId, emailText);
                        Intent intent = new Intent(this, MemberComp.class);
                        startActivity(intent);
                    } else {
                        // 18歳未満の場合
                        sendEmailConfirmation(userId, emailText);
                        Intent intent = new Intent(this, ParentRegistration.class);
                        startActivity(intent);
                    }
                } else {
                    // 登録失敗時
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // エラー処理
                Log.e("MemberRegistration", "Error during registration", e);
                Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.uploadButton) {
            // 画像選択のためのインテントを起動
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.backButton) {
            // ログイン画面に戻る
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    // 誕生日が現在の日時より18年前以降かどうかを判定
    private boolean isOver18(String birthdayText) {
        try {
            // 誕生日をDate型に変換（yyyyMMdd形式）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date birthDate = sdf.parse(birthdayText);

            // 現在の日付を取得
            Calendar today = Calendar.getInstance();
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            // 18年前の日付を計算
            Calendar eighteenYearsAgo = Calendar.getInstance();
            eighteenYearsAgo.add(Calendar.YEAR, -18);

            // 誕生日が18年前以降かどうかを判定
            return birthCalendar.before(eighteenYearsAgo) || birthCalendar.equals(eighteenYearsAgo);
        } catch (ParseException e) {
            Log.e("MemberRegistration", "誕生日が不正です", e);
            return false; // エラーが発生した場合はデフォルトでfalseを返す
        }
    }

    // メール送信をバックグラウンドスレッドで実行
    // メール送信を別スレッドで実行
    private void sendEmailConfirmation(String userId, String toEmail) {
        new Thread(() -> {
            try {
                EmailSender.sendEmail(toEmail, "会員登録完了のお知らせ",
                        "会員者登録が完了しました. 会員者ID : " + userId);
                runOnUiThread(() -> Toast.makeText(MemberRegistration.this, "メールを送信しました", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Log.e("SendEmailTask", "Error sending email", e);
                runOnUiThread(() -> Toast.makeText(MemberRegistration.this, "メール送信に失敗しました: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    // 画像選択の結果を取得
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // 画像のURIを取得
            android.net.Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // URIからパスを取得
                selectedImagePath = getPathFromURI(selectedImageUri);
                // パスを表示
                Log.d("MemberRegistration", "Selected Image Path: " + selectedImagePath);
                Toast.makeText(this, "Image selected: " + selectedImagePath, Toast.LENGTH_LONG).show();
            }
        }
    }

    // URIから実際のファイルパスを取得するメソッド
    private String getPathFromURI(android.net.Uri contentUri) {
        String[] proj = {android.provider.MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }
}