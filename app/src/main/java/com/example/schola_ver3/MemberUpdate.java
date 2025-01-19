package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.korekore.R;

public class MemberUpdate extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText usernameEditText, emailEditText, phoneEditText, passwordEditText, profileMessageEditText;
    private Button saveButton;
    private ImageButton backButton;
    private String memberId; // ログイン中のユーザーID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_renewal); // レイアウトファイルを指定

        // データベースヘルパーの初期化
        dbHelper = new DatabaseHelper(this);

        // UIコンポーネントの初期化
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        profileMessageEditText = findViewById(R.id.profileMessage);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton); // backButton を取得

        // SharedPreferences からユーザーIDを取得
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        memberId = sharedPreferences.getString("user_id", "");

        if (memberId.isEmpty()) {
            Toast.makeText(this, "ログイン情報がありません", Toast.LENGTH_SHORT).show();
            finish(); // アクティビティを終了
            return;
        }

        // 会員情報をデータベースから取得してUIに表示
        loadMemberInfo();

        // 「保存」ボタンのクリックリスナー
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemberInfo();
            }
        });

        // backButton のクリックリスナーを設定
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomePage.java に遷移
                Intent intent = new Intent(MemberUpdate.this, HomePage.class);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        });
    }

    // 会員情報をデータベースから取得してUIに表示するメソッド
    private void loadMemberInfo() {
        Cursor cursor = dbHelper.getMemberInfo(memberId);
        if (cursor != null && cursor.moveToFirst()) {
            // カラム名が正しいか確認
            int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
            int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER);
            int passwordIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD);
            int profileMessageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROFILE_MESSAGE);

            // カラムが存在するかチェック
            if (usernameIndex == -1 || emailIndex == -1 || phoneIndex == -1 || passwordIndex == -1 || profileMessageIndex == -1) {
                Toast.makeText(this, "データベースのカラムが正しくありません", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            // データベースから取得した値をUIに設定
            usernameEditText.setText(cursor.getString(usernameIndex));
            emailEditText.setText(cursor.getString(emailIndex));
            phoneEditText.setText(cursor.getString(phoneIndex));
            passwordEditText.setText(cursor.getString(passwordIndex));
            profileMessageEditText.setText(cursor.getString(profileMessageIndex));
            cursor.close();
        } else {
            Toast.makeText(this, "会員情報の取得に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    // 会員情報を更新してデータベースに保存するメソッド
    private void saveMemberInfo() {
        // UIから入力値を取得
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String profileMessage = profileMessageEditText.getText().toString();

        // 入力値のバリデーション
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "すべてのフィールドを入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        // データベースを更新
        boolean isUpdated = updateMemberInfo(memberId, username, email, phone, password, profileMessage);

        if (isUpdated) {
            Toast.makeText(this, "会員情報を更新しました", Toast.LENGTH_SHORT).show();
            // 保存成功後、Homepage.java に遷移
            Intent intent = new Intent(MemberUpdate.this, HomePage.class);
            startActivity(intent);
            finish(); // 現在のアクティビティを終了
        } else {
            Toast.makeText(this, "会員情報の更新に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    // データベースの会員情報を更新するメソッド
    private boolean updateMemberInfo(String memberId, String username, String email, String phone, String password, String profileMessage) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 更新する値を設定
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, username);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, phone);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_PROFILE_MESSAGE, profileMessage);

        // 更新を実行
        int rowsAffected = db.update(
                DatabaseHelper.TABLE_MEMBERS,
                values,
                DatabaseHelper.COLUMN_MEMBER_ID + " = ?",
                new String[]{memberId}
        );

        return rowsAffected > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // データベースヘルパーを閉じる
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}