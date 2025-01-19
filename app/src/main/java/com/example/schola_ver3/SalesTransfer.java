package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


// MyPage から
public class SalesTransfer extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> resultLauncher;
    private ImageButton backButton; // 戻るボタン
    private ImageButton noticeButton; //　通知ボタン
    private Button transferButton; // 振込ボタン
    private EditText editTextTransferAmount;
    private SalesDatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private int previousSalesAmount = 0;  // 元の売上金額を保存するための変数
    private String userId;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proceeds_transfer);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        noticeButton = (ImageButton) findViewById(R.id.noticeButton);
        noticeButton.setOnClickListener(this);

        transferButton = findViewById(R.id.transferButton);
        transferButton.setOnClickListener(this);

        editTextTransferAmount = findViewById(R.id.editTextTransferAmount);

        // SalesDatabaseHelper を初期化
        dbHelper = new SalesDatabaseHelper(this);

        // データベースを初期化し、テーブルを作成
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (!isTableExists(db, SalesDatabaseHelper.TABLE_NAME)) {
            Log.e("DatabaseError", "sales テーブルが存在しません");
        }

        // 特定の userId の sales_amount を取得して表示
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null); // "user_id" に保存された値を取得

        if (userId != null) {
            // 会員者IDを取得できた場合
//            Toast.makeText(this, "ログイン中の会員者ID: " + userId, Toast.LENGTH_SHORT).show();
            Log.d("SalesTransferActivity", "ログイン中の会員者ID: " + userId);
        } else {
            // 会員者IDが取得できない場合（例: ログアウト状態など）
//            Toast.makeText(this, "ログインしているユーザーはいません。", Toast.LENGTH_SHORT).show();
            Log.d("SalesTransferActivity", "ログインしているユーザーはいません");
        }

//        String userId = "2"; // 会員IDを取得するロジックをここに追加
//        int salesAmount = getSalesAmount(userId, db);

        // EditText に取得した sales_amount を表示
//        editTextTransferAmount.setText(String.valueOf(salesAmount));

        // 振込確認画面において、 yesButton または noButton を選択した後の処理
        // 別のアクティビティからの結果を受け取る処理
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // 振込完了画面へ
                        Intent intent = new Intent(getApplication(), SalesTransferSuccess.class);
                        startActivity(intent);

                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        // noButton の場合、元の売上金額に戻す
                        revertSalesData();

                        // 振込画面に戻る
                        Intent intent = new Intent(getApplication(), SalesTransfer.class);
                        startActivity(intent);

                    }
                }
        );
    }

    // ActivityのonCreateや、振込処理を行う前に元の金額を取得しておく
    private void getPreviousSalesAmount() {
//        String userId = "2"; // 会員IDを取得するロジックをここに追加
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        previousSalesAmount = getSalesAmount(userId, db);  // 更新前の売上金額を取得
        db.close();
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName}
        );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // noButton の場合、売上金額を元に戻す処理
    private void revertSalesData() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();  // データベースを開く

//            String userId = "2"; // 会員IDを取得するロジックを追加

            // 元の売上金額を使ってDBを更新
            String sql = "UPDATE " + SalesDatabaseHelper.TABLE_NAME +
                    " SET " + SalesDatabaseHelper.SALES_AMOUNT + " = ?" +
                    " WHERE " + SalesDatabaseHelper.MEMBER_ID + " = ?";
            db.execSQL(sql, new Object[]{previousSalesAmount, userId});
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // データベースを閉じる
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            Intent intent = new Intent(getApplication(), AccountInformation.class);
            startActivity(intent);
        } else if (v.getId() == R.id.transferButton) {
            // 記入漏れがないかの処理
            String transferAmountStr = editTextTransferAmount.getText().toString();
            if (transferAmountStr.isEmpty()) {
                // 入力されていない場合のエラー処理
                editTextTransferAmount.setError("振込金額を入力してください");
                return;
            }

            int transferAmount = Integer.parseInt(transferAmountStr);
//            String userId = "2"; // 会員IDを取得するロジックを追加

            // 振込前の売上金額を取得して保持する
            getPreviousSalesAmount();  // ここで呼び出して元の金額を保存

            // 振込金額と現在の売上金額を確認
            if (!canProceedWithTransfer(userId, transferAmount)) {
                // 金額が不足している場合は処理を中断
                return;
            }

            // データベースに新しい売上金額を挿入または更新
            insertSalesData(userId, transferAmount);

            // 記入漏れがなければ振込確認画面へ
            Intent intent = new Intent(this, SalesTransferCheck.class);
            resultLauncher.launch(intent);
        }
    }

    private void insertSalesData(String userId, int salesAmount) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();  // データベースを開く

            // 現在の金額 sales_amount を取得
            int currentAmount = getSalesAmount(userId, db);

            // 振込後の金額を計算
            // データベースから取得した金額ー入力金額
            int updatedAmount = currentAmount - salesAmount;

            // sales_amount を更新
            String sql = "INSERT OR REPLACE INTO " + SalesDatabaseHelper.TABLE_NAME + " (" +
                    SalesDatabaseHelper.MEMBER_ID + ", " +
                    SalesDatabaseHelper.SALES_AMOUNT + ") VALUES (?, ?)";

            db.execSQL(sql, new Object[]{userId, updatedAmount});
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // データベースを閉じる
            }
        }
    }

    // 残高確認処理
    private boolean canProceedWithTransfer(String userId, int transferAmount) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            // 現在の金額を取得
            int currentAmount = getSalesAmount(userId, db);

            // 振込後の金額を計算
            int updatedAmount = currentAmount - transferAmount;

            if (updatedAmount < 0) {
                // 残高が不足している場合のエラー処理
                editTextTransferAmount.setError("残高が不足しています");
                return false;
            }

            // 問題ない場合は true を返す
            return true;

        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // データベースを閉じる
            }
        }
    }


    private void updateSalesData() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();  // データベースを開く

//            String userId = "123"; // 会員IDを取得するロジックを追加
            int additionalAmount = Integer.parseInt(editTextTransferAmount.getText().toString());

            // 現在の sales_amount を取得
            int currentAmount = getSalesAmount(userId, db);

            // 新しい金額を加算する処理
            int updatedAmount = currentAmount + additionalAmount;

            // sales_amount を更新
            String sql = "UPDATE " + SalesDatabaseHelper.TABLE_NAME +
                    " SET " + SalesDatabaseHelper.SALES_AMOUNT + " = ?" +
                    " WHERE " + SalesDatabaseHelper.MEMBER_ID + " = ?";
            db.execSQL(sql, new Object[]{updatedAmount, userId});
        } finally {
            if (db != null && db.isOpen()) {
                db.close();  // データベースを閉じる
            }
        }
    }

    private int getSalesAmount(String userId, SQLiteDatabase db) {
        int salesAmount = 0;

        String query = "SELECT " + SalesDatabaseHelper.SALES_AMOUNT +
                " FROM " + SalesDatabaseHelper.TABLE_NAME +
                " WHERE " + SalesDatabaseHelper.MEMBER_ID + " = ?";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{userId});

            if (cursor.moveToFirst()) {
                salesAmount = cursor.getInt(0); // 取得した sales_amount の値をセット
            }
        } finally {
            if (cursor != null) {
                cursor.close();  // カーソルを閉じる
            }
        }
        return salesAmount;
    }


}
