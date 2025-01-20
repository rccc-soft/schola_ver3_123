package com.example.schola_ver3;
import android.content.Intent;
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

        setContentView(R.layout.logout);

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

            //ログイン画面へ
            Intent intent = new Intent(getApplication(), Login.class);
            startActivity(intent);
        }
    }
}
