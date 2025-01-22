package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QuickResponse extends AppCompatActivity {

    private ImageButton backButton; // 戻るボタン
    private Button shippingCompletedButton; // 発送完了ボタン
//    private QRDatabaseHelper dbHelper; // DatabaseHelperのインスタンス
    private DatabaseHelper dbHelper;  // 購入テーブル（仮）
    private int purchaseId; // 購入ID

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // UIの初期化
        backButton = (ImageButton) findViewById(R.id.backButton);
//        backButton.setOnClickListener((View.OnClickListener) this);

        shippingCompletedButton = findViewById(R.id.shippingCompletedButton);
//        shippingCompletedButton.setOnClickListener((View.OnClickListener) this);

        // データベースヘルパーを初期化
        dbHelper = new DatabaseHelper(this); // 購入テーブル（仮）

        // QRコードを表示するImageView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);

        // Intent から購入IDを受け取る
        Intent intent = getIntent();
        purchaseId = intent.getIntExtra("purchase_id", -1);

        // 配送先IDの取得
        String destination = dbHelper.getDestinationByPurchaseId(purchaseId); // 購入テーブルに配送先IDを取得するメソッドを追加
//        String destination = "U3455";

        // 配送先の取得
//        String randomCode = generateRandomString(10);
//        String randomContent2 = destination;

        // 配送先IDに基づいてQRコードを生成してImageViewに表示
        generateQRCode(destination, qrCodeImageView);

        // 戻るボタンがクリックされたときの処理
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ExhibitList を起動するインテントを作成
                Intent intent = new Intent(QuickResponse.this, ExhibitDetails.class);
                startActivity(intent);  // 新しいアクティビティを開始
            }
        });

        // 発送完了ボタンがクリックされたときの処理
        shippingCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 生成したQRコードをデータベースに保存
//                dbHelper.insertQRCode(randomCode, destination);

                // randomContent2 を確認 (例: ログに表示)
                Log.i("ShippingCompleted", "destination: " + destination);

                // ExhibitShiped を起動するインテントを作成
                Intent intent = new Intent(QuickResponse.this, ExhibitShiped.class);
                intent.putExtra("purchase_id", purchaseId); // 購入ID
                startActivity(intent);  // 新しいアクティビティを開始
            }
        });
    }

    // ランダムな文字列を生成するメソッド
//    private String generateRandomString(int length) {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        StringBuilder randomString = new StringBuilder();
//        Random random = new Random();
//
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            randomString.append(characters.charAt(index));
//        }
//
//        return randomString.toString();
//    }

    // QRコードを生成してImageViewにセットするメソッド
    private void generateQRCode(String content, ImageView imageView) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            android.graphics.Bitmap bitmap = barcodeEncoder.encodeBitmap(content, com.google.zxing.BarcodeFormat.QR_CODE, 300, 300);
            imageView.setImageBitmap(bitmap);
        } catch (com.google.zxing.WriterException e) {
            e.printStackTrace();
        }
    }
}
