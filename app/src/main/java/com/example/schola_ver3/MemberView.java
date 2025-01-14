package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MemberView extends AppCompatActivity {

    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reading);

        // SharedPreferences から会員者IDを取得
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");

        if (!userId.isEmpty()) {
            // DatabaseHelperインスタンス作成
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Cursor cursor = dbHelper.getMemberInfo(userId);

            if (cursor != null && cursor.moveToFirst()) {
                // 各カラムのインデックスを取得
                int memberIdIndex = cursor.getColumnIndex("id");
                int passwordIndex = cursor.getColumnIndex("password");

                // 他のカラムも含む
                int usernameIndex = cursor.getColumnIndex("user_name");
                int kaiinnameIndex = cursor.getColumnIndex("kaiinname");
                int huriganaIndex = cursor.getColumnIndex("hurigana");
                int birthdayIndex = cursor.getColumnIndex("birthday");
                int schoolIndex = cursor.getColumnIndex("school");
                int emailIndex = cursor.getColumnIndex("email");
                int phoneIndex = cursor.getColumnIndex("phone_number");
                int profileMessageIndex = cursor.getColumnIndex("profile_message");

                // データを取得して EditText にセット
                if (memberIdIndex != -1) {
                    String memberId = cursor.getString(memberIdIndex);
                    EditText memberIdEditText = findViewById(R.id.userid);
                    memberIdEditText.setText(memberId);
                }

                if (passwordIndex != -1) {
                    String password = cursor.getString(passwordIndex);
                    EditText passwordEditText = findViewById(R.id.password);
                    passwordEditText.setText(password);
                }

                // 他のフィールドも同様に設定
                if (usernameIndex != -1) {
                    String username = cursor.getString(usernameIndex);
                    EditText usernameEditText = findViewById(R.id.username);
                    usernameEditText.setText(username);
                }

                if (kaiinnameIndex != -1) {
                    String kaiinname = cursor.getString(kaiinnameIndex);
                    EditText kaiinnameEditText = findViewById(R.id.kaiinname);
                    kaiinnameEditText.setText(kaiinname);
                }

                if (huriganaIndex != -1) {
                    String hurigana = cursor.getString(huriganaIndex);
                    EditText huriganaEditText = findViewById(R.id.hurigana);
                    huriganaEditText.setText(hurigana);
                }

                if (birthdayIndex != -1) {
                    String birthday = cursor.getString(birthdayIndex);
                    EditText birthdayEditText = findViewById(R.id.birthday);
                    birthdayEditText.setText(birthday);
                }

                if (schoolIndex != -1) {
                    String school = cursor.getString(schoolIndex);
                    EditText schoolEditText = findViewById(R.id.school);
                    schoolEditText.setText(school);
                }

                if (emailIndex != -1) {
                    String email = cursor.getString(emailIndex);
                    EditText emailEditText = findViewById(R.id.email);
                    emailEditText.setText(email);
                }

                if (phoneIndex != -1) {
                    String phone = cursor.getString(phoneIndex);
                    EditText phoneEditText = findViewById(R.id.phone);
                    phoneEditText.setText(phone);
                }

                if (profileMessageIndex != -1) {
                    String profileMessage = cursor.getString(profileMessageIndex);
                    EditText profileMessageEditText = findViewById(R.id.profileMessage);
                    profileMessageEditText.setText(profileMessage);
                }

                cursor.close(); // Cursorを閉じる
            }
        }

        // backButton のクリックリスナーを設定
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HomePage.java に遷移
                Intent intent = new Intent(MemberView.this, HomePage.class);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        });

        // editButton のクリックリスナーを設定
        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MemberUpdate.java に遷移
                Intent intent = new Intent(MemberView.this, MemberUpdate.class);

                // 現在表示されている会員情報を Intent に追加
                intent.putExtra("user_id", ((EditText) findViewById(R.id.userid)).getText().toString());
                intent.putExtra("password", ((EditText) findViewById(R.id.password)).getText().toString());
                intent.putExtra("username", ((EditText) findViewById(R.id.username)).getText().toString());
                intent.putExtra("kaiinname", ((EditText) findViewById(R.id.kaiinname)).getText().toString());
                intent.putExtra("hurigana", ((EditText) findViewById(R.id.hurigana)).getText().toString());
                intent.putExtra("birthday", ((EditText) findViewById(R.id.birthday)).getText().toString());
                intent.putExtra("school", ((EditText) findViewById(R.id.school)).getText().toString());
                intent.putExtra("email", ((EditText) findViewById(R.id.email)).getText().toString());
                intent.putExtra("phone", ((EditText) findViewById(R.id.phone)).getText().toString());
                intent.putExtra("profileMessage", ((EditText) findViewById(R.id.profileMessage)).getText().toString());

                startActivity(intent);
            }
        });
    }
}