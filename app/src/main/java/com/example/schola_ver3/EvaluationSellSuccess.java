package com.example.schola_ver3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.  ;

//EvaluationSellから
public class EvaluationSellSuccess extends AppCompatActivity implements View.OnClickListener{
    private Button completebtn;
    //editとかDBに必要なものを追加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_complete);

        completebtn = findViewById(R.id.completebtn);
        completebtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //ホーム画面へ
        if (v.getId() == R.id.completebtn) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
    }
}
