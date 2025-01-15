package com.example.schola_ver3;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schola_ver3.Login;
import com.example.schola_ver3.MyPage;

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
