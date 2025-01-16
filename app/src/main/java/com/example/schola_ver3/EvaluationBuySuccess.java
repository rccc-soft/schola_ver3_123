package com.example.schola_ver3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.korekore.R;

//EvaluationSellから
public class EvaluationBuySuccess extends AppCompatActivity implements View.OnClickListener{
    private Button completebtn;
    //editとかDBに必要なものを追加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_evaluation_confirmation0);
        Intent intent = new Intent(getApplication(),EvaluationSell.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        //ホーム画面へ
    }
}
