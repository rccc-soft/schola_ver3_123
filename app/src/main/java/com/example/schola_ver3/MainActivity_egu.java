package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.korekore.R;

/**
 * メインアクティビティ
 * ユーザーが他の主要な機能にアクセスするための入口
 */
public class MainActivity_egu extends AppCompatActivity {

    private Button buttonMyPage;
    private Button buttonEvaluationSell;
    private Button buttonReviewSellList;
    private Button buttonReviewBuyList;


        // サンプルのユーザー情報（実際はサーバーやDBから取得する）
        int buyer_member_id = 2001;
        int seller_member_id = 3001;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_egu); // 作成したレイアウトを設定

            // UI要素の初期化
            buttonMyPage = findViewById(R.id.buttonMyPage);
            buttonEvaluationSell = findViewById(R.id.buttonEvaluationSell);
            buttonReviewSellList = findViewById(R.id.buttonReviewSellList);
            buttonReviewBuyList = findViewById(R.id.buttonReviewBuyList);

            // ボタンリスナー設定
            buttonEvaluationSell.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity_egu.this, EvaluationSell.class);
                intent.putExtra("buyer_member_id", buyer_member_id);
                intent.putExtra("seller_member_id", seller_member_id);
                startActivity(intent);
            });

            buttonMyPage.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity_egu.this, MyPage.class);
                intent.putExtra("seller_member_id", seller_member_id);
                startActivity(intent);
            });

            buttonReviewSellList.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity_egu.this, ReviewListActivity.class);
                intent.putExtra("seller_member_id", seller_member_id);
                startActivity(intent);
            });

            buttonReviewBuyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity_egu.this, EvaluationBuy.class);
                    intent.putExtra("buyer_member_id", buyer_member_id);
                    intent.putExtra("seller_member_id", seller_member_id);
                    startActivity(intent);
                }
            });
        }
    }
