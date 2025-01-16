package com.example.schola_ver3;

import android.content.Intent;
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

//import com.example.korekore.R;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        String usernameText = username.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String huriganaText = hurigana.getText().toString().trim();
        String kaiinnameText = kaiinname.getText().toString().trim();
        String birthdayText = birthday.getText().toString().trim();
        String schoolText = school.getText().toString().trim();

        Log.d("MemberRegistration", "Username: " + usernameText);
        Log.d("MemberRegistration", "Email: " + emailText);
        Log.d("MemberRegistration", "Phone: " + phoneText);
        Log.d("MemberRegistration", "Password: " + passwordText);
        Log.d("MemberRegistration", "Hurigana: " + huriganaText);
        Log.d("MemberRegistration", "Kaiinname: " + kaiinnameText);
        Log.d("MemberRegistration", "Birthday: " + birthdayText);
        Log.d("MemberRegistration", "School: " + schoolText);

        // プロフィールメッセージが空でも問題ないのでそのチェックは省略
        // 画像も空でも問題ないのでそのチェックも省略

        // 必要項目がすべて入力されているか確認（プロフィールメッセージと画像は空でもOK）
        boolean allFieldsFilled = !usernameText.isEmpty() && isValidEmail(emailText) &&
                isValidPhone(phoneText) && !passwordText.isEmpty() &&
                !huriganaText.isEmpty() && !kaiinnameText.isEmpty() &&
                !birthdayText.isEmpty() && !schoolText.isEmpty();

        // 登録ボタンの有効化
        registerButton.setEnabled(allFieldsFilled);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerButton) {
            try {
                // 入力された内容を取得
                String usernameText = username.getText().toString().trim();
                String emailText = email.getText().toString().trim(); // 入力されたメールアドレス
                String phoneText = phone.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String huriganaText = hurigana.getText().toString().trim();
                String kaiinnameText = kaiinname.getText().toString().trim();
                String birthdayText = birthday.getText().toString().trim();
                String schoolText = school.getText().toString().trim();
                String profileMessageText = profileMessage.getText().toString().trim();

                // プロフィールメッセージが空の場合、空文字を設定
                if (profileMessageText.isEmpty()) {
                    profileMessageText = ""; // 空文字を設定
                }

                // 画像パスが設定されていない場合、空文字を設定
                String profileImage = selectedImagePath.isEmpty() ? "" : selectedImagePath;

                // 会員者IDを生成
                String userId = generateUserId();

                // 会員者IDをログに出力
                Log.d("MemberRegistration", "Generated User ID: " + userId);

                // DatabaseHelperインスタンスを作成
                DatabaseHelper dbHelper = new DatabaseHelper(this);

                // 新しい会員情報をデータベースに登録
                boolean isRegistered = dbHelper.addNewMember(userId, usernameText, emailText, phoneText,
                        passwordText, profileImage, profileMessageText,
                        huriganaText, kaiinnameText, birthdayText, schoolText);

                if (isRegistered) {
                    // 登録成功時、成功画面に遷移
                    sendEmailConfirmation(userId, emailText); // 入力されたメールアドレスに送信
                    Intent intent = new Intent(this, MemberComp.class);
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
        } else if (v.getId() == R.id.uploadButton) {
            // 画像選択のためのインテントを起動
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1); // 1は画像選択用のリクエストコード
        } else if (v.getId() == R.id.backButton) {
            // ログイン画面に戻る
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    // メール送信をバックグラウンドスレッドで実行
    private void sendEmailConfirmation(String userId, String toEmail) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                EmailSender.sendEmail(toEmail, "Member Registration Confirmation",
                        "Your registration is complete. Your Member ID is: " + userId);
                runOnUiThread(() -> Toast.makeText(MemberRegistration.this, "Email sent successfully", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Log.e("SendEmailTask", "Error sending email", e);
                runOnUiThread(() -> Toast.makeText(MemberRegistration.this, "Failed to send email: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
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


//package com.example.korekore;
//
//import android.content.ContentValues;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.Random;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class MemberRegistration extends AppCompatActivity implements View.OnClickListener {
//
//    private EditText username, password, email, phone, hurigana, kaiinname, birthday, school, profileMessage;
//    private Button uploadButton, registerButton;
//    private ImageButton backButton;
//
//    private String selectedImagePath = ""; // 画像パス用
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_regisitration);
//
//        // UIの初期化
//        username = findViewById(R.id.username);
//        password = findViewById(R.id.password);
//        email = findViewById(R.id.email);
//        phone = findViewById(R.id.phone);
//        hurigana = findViewById(R.id.hurigana);  // ふりがな
//        kaiinname = findViewById(R.id.kaiinname);  // 会員名
//        birthday = findViewById(R.id.birthday);  // 生年月日
//        school = findViewById(R.id.school);  // 学校
//        profileMessage = findViewById(R.id.profileMessage);  // プロフィールメッセージ
//
//        uploadButton = findViewById(R.id.uploadButton);
//        registerButton = findViewById(R.id.registerButton);
//        backButton = findViewById(R.id.backButton);
//
//        uploadButton.setOnClickListener(this);
//        registerButton.setOnClickListener(this);
//        backButton.setOnClickListener(this);
//
//        // registerButtonを最初は無効にしておく
//        registerButton.setEnabled(false);
//
//        // 入力フィールドのリスナーを設定
//        username.addTextChangedListener(textWatcher);
//        password.addTextChangedListener(textWatcher);
//        email.addTextChangedListener(textWatcher);
//        phone.addTextChangedListener(textWatcher);
//        hurigana.addTextChangedListener(textWatcher);
//        kaiinname.addTextChangedListener(textWatcher);
//        birthday.addTextChangedListener(textWatcher);
//        school.addTextChangedListener(textWatcher);
//        profileMessage.addTextChangedListener(textWatcher);
//    }
//
//    // 入力内容が変更された際に、すべてのフィールドが入力されているかをチェック
//    private final TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            checkInputs(); // 入力内容が変更されるたびに確認
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {}
//    };
//
//    // 入力項目がすべて入力されているかを確認
//    private void checkInputs() {
//        String usernameText = username.getText().toString().trim();
//        String emailText = email.getText().toString().trim();
//        String phoneText = phone.getText().toString().trim();
//        String passwordText = password.getText().toString().trim();
//        String huriganaText = hurigana.getText().toString().trim();
//        String kaiinnameText = kaiinname.getText().toString().trim();
//        String birthdayText = birthday.getText().toString().trim();
//        String schoolText = school.getText().toString().trim();
//
//        Log.d("MemberRegistration", "Username: " + usernameText);
//        Log.d("MemberRegistration", "Email: " + emailText);
//        Log.d("MemberRegistration", "Phone: " + phoneText);
//        Log.d("MemberRegistration", "Password: " + passwordText);
//        Log.d("MemberRegistration", "Hurigana: " + huriganaText);
//        Log.d("MemberRegistration", "Kaiinname: " + kaiinnameText);
//        Log.d("MemberRegistration", "Birthday: " + birthdayText);
//        Log.d("MemberRegistration", "School: " + schoolText);
//
//        // プロフィールメッセージが空でも問題ないのでそのチェックは省略
//        // 画像も空でも問題ないのでそのチェックも省略
//
//        // 必要項目がすべて入力されているか確認（プロフィールメッセージと画像は空でもOK）
//        boolean allFieldsFilled = !usernameText.isEmpty() && isValidEmail(emailText) &&
//                isValidPhone(phoneText) && !passwordText.isEmpty() &&
//                !huriganaText.isEmpty() && !kaiinnameText.isEmpty() &&
//                !birthdayText.isEmpty() && !schoolText.isEmpty();
//
//        // 登録ボタンの有効化
//        registerButton.setEnabled(allFieldsFilled);
//    }
//
//
//    // メールアドレスのフォーマットが正しいかを確認
//    private boolean isValidEmail(String email) {
//        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//        Pattern pattern = Pattern.compile(emailPattern);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }
//
//    // 電話番号が正しいかを確認（11桁の日本国内形式を仮定）
//    private boolean isValidPhone(String phone) {
//        return phone.matches("^\\d{11}$"); // 電話番号が11桁の数字であるか
//    }
//
//    // 会員者IDを生成（"u" + ランダムな英数字7文字）
//    private String generateUserId() {
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        Random random = new Random();
//        StringBuilder userId = new StringBuilder("u");
//        for (int i = 0; i < 7; i++) {
//            int randomIndex = random.nextInt(chars.length());
//            userId.append(chars.charAt(randomIndex));
//        }
//        return userId.toString();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.registerButton) {
//            try {
//                // 入力された内容を取得
//                String usernameText = username.getText().toString().trim();
//                String emailText = email.getText().toString().trim();
//                String phoneText = phone.getText().toString().trim();
//                String passwordText = password.getText().toString().trim();
//                String huriganaText = hurigana.getText().toString().trim();
//                String kaiinnameText = kaiinname.getText().toString().trim();
//                String birthdayText = birthday.getText().toString().trim();
//                String schoolText = school.getText().toString().trim();
//                String profileMessageText = profileMessage.getText().toString().trim();
//
//                // プロフィールメッセージが空の場合、空文字を設定
//                if (profileMessageText.isEmpty()) {
//                    profileMessageText = ""; // 空文字を設定
//                }
//
//                // 画像パスが設定されていない場合、空文字を設定
//                String profileImage = selectedImagePath.isEmpty() ? "" : selectedImagePath;
//
//                // 会員者IDを生成
//                String userId = generateUserId();
//
//                // 会員者IDをログに出力
//                Log.d("MemberRegistration", "Generated User ID: " + userId);
//
//                // DatabaseHelperインスタンスを作成
//                DatabaseHelper dbHelper = new DatabaseHelper(this);
//
//                // 新しい会員情報をデータベースに登録
//                boolean isRegistered = dbHelper.addNewMember(userId, usernameText, emailText, phoneText,
//                        passwordText, profileImage, profileMessageText,
//                        huriganaText, kaiinnameText, birthdayText, schoolText);
//
//                if (isRegistered) {
//                    // 登録成功時、成功画面に遷移
//                    sendEmailConfirmation(userId, emailText);
//                    Intent intent = new Intent(this, MemberComp.class);
//                    startActivity(intent);
//                } else {
//                    // 登録失敗時
//                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                // エラー処理
//                Log.e("MemberRegistration", "Error during registration", e);
//                Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        } else if (v.getId() == R.id.uploadButton) {
//            // 画像選択のためのインテントを起動
//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, 1); // 1は画像選択用のリクエストコード
//        } else if (v.getId() == R.id.backButton) {
//            // ログイン画面に戻る
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
//        }
//    }
//
//
//    // メールで会員者IDを送信
//    private void sendEmailConfirmation(String userId, String email) {
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setType("message/rfc822");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Member Registration Confirmation");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your registration is complete. Your Member ID is: " + userId);
//
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(this, "No email client installed.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    // 画像選択の結果を取得
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
//            // 画像のURIを取得
//            android.net.Uri selectedImageUri = data.getData();
//            if (selectedImageUri != null) {
//                // URIからパスを取得
//                selectedImagePath = getPathFromURI(selectedImageUri);
//                // パスを表示
//                Log.d("MemberRegistration", "Selected Image Path: " + selectedImagePath);
//                Toast.makeText(this, "Image selected: " + selectedImagePath, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    // URIから実際のファイルパスを取得するメソッド
//    private String getPathFromURI(android.net.Uri contentUri) {
//        String[] proj = {android.provider.MediaStore.Images.Media.DATA};
//        android.database.Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
//            String path = cursor.getString(columnIndex);
//            cursor.close();
//            return path;
//        }
//        return null;
//    }
//
//}
