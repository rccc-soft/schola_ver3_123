package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schola_ver3.MyPage;

public class Haisou extends AppCompatActivity implements View.OnClickListener {

    private Button finish;
    //editとかDBに必要なものを追加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.haisou);

        finish = findViewById(R.id.finish);
        finish.setOnClickListener(this);
        //editとかDBに必要なものを追加


        //ここに入力処理いりそう
    }

    //ここら辺にDB読み込みのchecklistとかいる

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.finish) {
            //規約画面へ
            Intent intent = new Intent(getApplication(), MyPage.class);
            startActivity(intent);
        }
    }
}
