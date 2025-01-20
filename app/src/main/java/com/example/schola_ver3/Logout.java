package com.example.schola_ver3;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//Settingから
public class Logout extends AppCompatActivity implements View.OnClickListener{
    private Button logoutNoButton;
    private Button logoutYesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_logout);

        logoutYesButton = findViewById(R.id.logoutYesButton);
        logoutYesButton.setOnClickListener(this);
        logoutNoButton = findViewById(R.id.logoutNoButton);
        logoutNoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logoutNoButton) {
            //マイページ画面へ
            Intent intent = new Intent(getApplication(), MyPage.class);
            startActivity(intent);
        } else if (v.getId() == R.id.logoutYesButton) {
            //ここにログアウト処理を追加する
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");
            editor.apply();
            //ログイン画面へ
            Intent intent = new Intent(getApplication(), Login.class);
            startActivity(intent);
        }
    }
}
