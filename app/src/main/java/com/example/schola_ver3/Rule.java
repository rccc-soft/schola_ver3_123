package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

;

//Loginから
public class Rule extends AppCompatActivity implements View.OnClickListener{
    private Button acceptbtn;
    private Button cancelbtn;
    private TextView ruleText;
    private RuleDatabaseHelper dbHelper;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rule);

        acceptbtn = findViewById(R.id.yesButton);
        acceptbtn.setOnClickListener(this);
        cancelbtn = findViewById(R.id.noButton);
        cancelbtn.setOnClickListener(this);

        //UI要素の取得
        ruleText = findViewById(R.id.ruleText);
        //データベースヘルパーの初期化
        dbHelper = new RuleDatabaseHelper(this);

        // データベースを開く
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 規約内容を取得
        String ruleContent = dbHelper.getRuleContent(db, 1);

        // 規約内容が取得できた場合、TextViewに設定
        if (ruleContent != null) {
            ruleText.setText(ruleContent);
        } else {
            ruleText.setText("規約内容が読み込めませんでした。");
        }
        db.close();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.noButton) {
            //ログイン画面へ
            Intent intent = new Intent(getApplication(), Login.class);
            startActivity(intent);
        } else if (v.getId() == R.id.yesButton) {
            //会員登録画面へ
            Intent intent = new Intent(getApplication(), MemberRegistration.class);
            startActivity(intent);
        }
    }
}
