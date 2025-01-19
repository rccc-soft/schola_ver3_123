package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Qr extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private ImageButton noticeButton;
    private Button haisou;
//    private ImageView qrImageView; // QRコード表示用
//    private Button generateQRButton; // QRコード生成ボタン

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.qr);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        noticeButton = (ImageButton) findViewById(R.id.noticeButton);
        noticeButton.setOnClickListener(this);
        haisou = findViewById(R.id.haisou);
        haisou.setOnClickListener(this);

//        qrImageView = findViewById(R.id.qrImageView); // QRコードを表示するImageView
//        generateQRButton = findViewById(R.id.generateQRButton); // QR生成ボタン
//        generateQRButton.setOnClickListener(v -> generateQRCode());
    }

    // 配送先データを生成
//    private String createDeliveryData() {
//        int 配送先ID = 1; // PK
//        int 会員者ID = 12345; // FK
//        String 氏名 = "山田 太郎";
//        String 電話番号 = "09012345678";
//        String 郵便番号 = "1234567";
//        String 住所 = "東京都新宿区1-1-1";
//
//        // 配送先データを文字列としてまとめる
//        return "配送先ID: " + 配送先ID + "\n"
//                + "会員者ID: " + 会員者ID + "\n"
//                + "氏名: " + 氏名 + "\n"
//                + "電話番号: " + 電話番号 + "\n"
//                + "郵便番号: " + 郵便番号 + "\n"
//                + "住所: " + 住所;
//    }

    // QRコードを生成して表示
//    private void generateQRCode() {
//        String deliveryData = createDeliveryData();
//
//        try {
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap = barcodeEncoder.encodeBitmap(deliveryData, BarcodeFormat.QR_CODE, 400, 400);
//            qrImageView.setImageBitmap(bitmap); // ImageViewにQRコードを表示
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            Intent intent = new Intent(getApplication(), ExhibitList.class);
            startActivity(intent);
        } else if (v.getId() == R.id.noticeButton) {
            Intent intent = new Intent(getApplication(), Logout.class);
            startActivity(intent);
        } else if (v.getId() == R.id.haisou) {
            Intent intent = new Intent(getApplication(), Haisou.class);
            startActivity(intent);
        }
    }
}
