package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

;

public class Setting extends AppCompatActivity implements View.OnClickListener {

    private ImageButton memberInfoButton;
    private ImageButton creditInfoButton;
    private ImageButton shippingInfoButton;
    private ImageButton memberDeleteButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        memberInfoButton = findViewById(R.id.memberInfoButton);
        memberInfoButton.setOnClickListener(this);

        creditInfoButton = findViewById(R.id.creditInfoButton);
        creditInfoButton.setOnClickListener(this);

        shippingInfoButton = findViewById(R.id.shippingInfoButton);
        shippingInfoButton.setOnClickListener(this);

        memberDeleteButton = findViewById(R.id.memberDeleteButton);
        memberDeleteButton.setOnClickListener(this);
    }

    //ここら辺にDB読み込みのchecklistとかいる

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.memberInfoButton) {
            //会員者情報閲覧画面へ
            Intent intent = new Intent(getApplication(), MemberView.class);
            startActivity(intent);
        } else if (v.getId() == R.id.creditInfoButton) {
            //決済情報設定画面へ
            Intent intent = new Intent(getApplication(), CreditSetting.class);
            startActivity(intent);
//        } else if (v.getId() == R.id.shippingInfoButton) {
//            //配送先設定画面へ
//            Intent intent = new Intent(getApplication(), ShippingDBCheck.class);
//            startActivity(intent);
          } else if (v.getId() == R.id.memberDeleteButton) {
          //会員者削除画面へ
              Intent intent = new Intent(getApplication(), MemberDelete.class);
              startActivity(intent);
        }
    }
}
