package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


// MyPage から
public class AccountInfomation extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    //private ActivityResultLauncher<Intent> resultLauncher;
    private ImageButton backButton; // 戻るボタン
    private ImageButton noticeButton; //　通知ボタン
    private Button amountInputButton; // 金額入力ボタン
    private EditText editTextBankName;
    private EditText editTextBranchName;
    private EditText editTextAccountNumber;
    private EditText editTextAccountName;
    private EditText editTextPhone;
//    private SalesDatabaseHelper dbHelper;
//    private static SQLiteDatabase db;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_infomation);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

//        noticeButton = (ImageButton) findViewById(R.id.noticeButton);
//        noticeButton.setOnClickListener(this);

        amountInputButton = findViewById(R.id.amountInputButton);
        amountInputButton.setOnClickListener(this);

        editTextBankName = findViewById(R.id.editTextBankName);
        amountInputButton.setOnClickListener(this);
        editTextBranchName = findViewById(R.id.editTextBranchName);
        amountInputButton.setOnClickListener(this);
        editTextAccountNumber = findViewById(R.id.editTextAccountNumber);
        amountInputButton.setOnClickListener(this);
        editTextAccountName = findViewById(R.id.editTextAccountName);
        amountInputButton.setOnClickListener(this);
        editTextPhone = findViewById(R.id.editTextPhone);
        amountInputButton.setOnClickListener(this);

        // SalesDatabaseHelper を初期化
//        dbHelper = new SalesDatabaseHelper(this);
        // データベースを初期化し、テーブルを作成
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        if (!isTableExists(db, SalesDatabaseHelper.TABLE_NAME)) {
//            Log.e("DatabaseError", "sales テーブルが存在しません");
//        }

        // 特定の memberId の sales_amount を取得して表示
//        String memberId = "1"; // 会員IDを取得するロジックをここに追加
//        int salesAmount = getSalesAmount(memberId, db);

        // EditText に取得した sales_amount を表示
//        editTextTransferAmount.setText(String.valueOf(salesAmount));

        // 振込確認画面において、 yesButton または noButton を選択した後の処理
        // 別のアクティビティからの結果を受け取る処理
//        resultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK) {
//                        // 振込完了画面へ
//                        Intent intent = new Intent(getApplication(), SalesTransferSuccessActivity.class);
//                        startActivity(intent);
//
//                    } else if (result.getResultCode() == RESULT_CANCELED) {
//                        // noButton の場合、元の売上金額に戻す
//                        revertSalesData();
//
//                        // 振込画面に戻る
//                        Intent intent = new Intent(getApplication(), AccountInformation.class);
//                        startActivity(intent);
//
//                    }
//                }
//        );
    }

    // ActivityのonCreateや、振込処理を行う前に元の金額を取得しておく
//    private void getPreviousSalesAmount() {
//        String memberId = "1"; // 会員IDを取得するロジックをここに追加
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        previousSalesAmount = getSalesAmount(memberId, db);  // 更新前の売上金額を取得
//        db.close();
//    }

//    private boolean isTableExists(SQLiteDatabase db, String tableName) {
//        Cursor cursor = db.rawQuery(
//                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
//                new String[]{tableName}
//        );
//        boolean exists = (cursor.getCount() > 0);
//        cursor.close();
//        return exists;
//    }

    // noButton の場合、売上金額を元に戻す処理
//    private void revertSalesData() {
//        SQLiteDatabase db = null;
//        try {
//            db = dbHelper.getWritableDatabase();  // データベースを開く
//
//            String memberId = "1"; // 会員IDを取得するロジックを追加
//
//            // 元の売上金額を使ってDBを更新
//            String sql = "UPDATE " + SalesDatabaseHelper.TABLE_NAME +
//                    " SET " + SalesDatabaseHelper.SALES_AMOUNT + " = ?" +
//                    " WHERE " + SalesDatabaseHelper.MEMBER_ID + " = ?";
//            db.execSQL(sql, new Object[]{previousSalesAmount, memberId});
//        } finally {
//            if (db != null && db.isOpen()) {
//                db.close();  // データベースを閉じる
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            Intent intent = new Intent(getApplication(), MyPage.class);
            startActivity(intent);
        } else if (v.getId() == R.id.amountInputButton) {

            // EditText に入力された文字列を取得
            String transferAmountStr = editTextBankName.getText().toString();
            String branchNameStr = editTextBranchName.getText().toString();
            String accountNumberStr = editTextAccountNumber.getText().toString();
            String accountNameStr = editTextAccountName.getText().toString();
            String textPhoneStr = editTextPhone.getText().toString();

            // 記入漏れがないかの処理
            if (transferAmountStr.isEmpty()) {
                // 入力されていない場合のエラー処理
                editTextBankName.setError("振込金額を入力してください");
                return;
            } else if (branchNameStr.isEmpty()){
                // 入力されていない場合のエラー処理
                editTextBranchName.setError("支店名を入力してください");
                return;
            } else if(accountNumberStr.isEmpty()){
                // 入力されていない場合のエラー処理
                editTextAccountNumber.setError("口座番号を入力してください");
                return;
            } else if (accountNameStr.isEmpty()){
                // 入力されていない場合のエラー処理
                editTextAccountName.setError("口座名義を入力してください");
                return;
            } else if(textPhoneStr.isEmpty()){
                // 入力されていない場合のエラー処理
                editTextPhone.setError("電話番号を入力してください");
                return;
            }

//            int transferAmount = Integer.parseInt(transferAmountStr);
//            String memberId = "1"; // 会員IDを取得するロジックを追加
//
//            // 振込前の売上金額を取得して保持する
//            getPreviousSalesAmount();  // ここで呼び出して元の金額を保存
//
//            // 振込金額と現在の売上金額を確認
//            if (!canProceedWithTransfer(memberId, transferAmount)) {
//                // 金額が不足している場合は処理を中断
//                return;
//            }
//
//            // データベースに新しい売上金額を挿入または更新
//            insertSalesData(memberId, transferAmount);

            // 記入漏れがなければ振込確認画面へ
            Intent intent = new Intent(this, SalesTransfer.class);
            startActivity(intent);
        }
    }
//    private void insertSalesData(String memberId, int salesAmount) {
//        SQLiteDatabase db = null;
//        try {
//            db = dbHelper.getWritableDatabase();  // データベースを開く
//
//            // 現在の金額 sales_amount を取得
//            int currentAmount = getSalesAmount(memberId, db);
//
//            // 振込後の金額を計算
//            // データベースから取得した金額ー入力金額
//            int updatedAmount = currentAmount - salesAmount;
//
//            // sales_amount を更新
//            String sql = "INSERT OR REPLACE INTO " + SalesDatabaseHelper.TABLE_NAME + " (" +
//                    SalesDatabaseHelper.MEMBER_ID + ", " +
//                    SalesDatabaseHelper.SALES_AMOUNT + ") VALUES (?, ?)";
//
//            db.execSQL(sql, new Object[]{memberId, updatedAmount});
//        } finally {
//            if (db != null && db.isOpen()) {
//                db.close();  // データベースを閉じる
//            }
//        }
//    }

    // 残高確認処理
//    private boolean canProceedWithTransfer(String memberId, int transferAmount) {
//        SQLiteDatabase db = null;
//        try {
//            db = dbHelper.getWritableDatabase();
//
//            // 現在の金額を取得
//            int currentAmount = getSalesAmount(memberId, db);
//
//            // 振込後の金額を計算
//            int updatedAmount = currentAmount - transferAmount;
//
//            if (updatedAmount < 0) {
//                // 残高が不足している場合のエラー処理
//                editTextTransferAmount.setError("残高が不足しています");
//                return false;
//            }
//
//            // 問題ない場合は true を返す
//            return true;
//
//        } finally {
//            if (db != null && db.isOpen()) {
//                db.close();  // データベースを閉じる
//            }
//        }
//    }


//    private void updateSalesData() {
//        SQLiteDatabase db = null;
//        try {
//            db = dbHelper.getWritableDatabase();  // データベースを開く
//
//            String memberId = "123"; // 会員IDを取得するロジックを追加
//            int additionalAmount = Integer.parseInt(editTextTransferAmount.getText().toString());
//
//            // 現在の sales_amount を取得
//            int currentAmount = getSalesAmount(memberId, db);
//
//            // 新しい金額を加算する処理
//            int updatedAmount = currentAmount + additionalAmount;
//
//            // sales_amount を更新
//            String sql = "UPDATE " + SalesDatabaseHelper.TABLE_NAME +
//                    " SET " + SalesDatabaseHelper.SALES_AMOUNT + " = ?" +
//                    " WHERE " + SalesDatabaseHelper.MEMBER_ID + " = ?";
//            db.execSQL(sql, new Object[]{updatedAmount, memberId});
//        } finally {
//            if (db != null && db.isOpen()) {
//                db.close();  // データベースを閉じる
//            }
//        }
//    }

//    private int getSalesAmount(String memberId, SQLiteDatabase db) {
//        int salesAmount = 0;
//
//        String query = "SELECT " + SalesDatabaseHelper.SALES_AMOUNT +
//                " FROM " + SalesDatabaseHelper.TABLE_NAME +
//                " WHERE " + SalesDatabaseHelper.MEMBER_ID + " = ?";
//        Cursor cursor = null;
//        try {
//            cursor = db.rawQuery(query, new String[]{memberId});
//
//            if (cursor.moveToFirst()) {
//                salesAmount = cursor.getInt(0); // 取得した sales_amount の値をセット
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();  // カーソルを閉じる
//            }
//        }
//        return salesAmount;
//    }
}
