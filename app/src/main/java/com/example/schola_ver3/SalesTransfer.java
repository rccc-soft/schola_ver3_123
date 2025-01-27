package com.example.schola_ver3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SalesTransfer extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SalesTransfer";
    private static final int REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> resultLauncher;
    private ImageButton backButton;
    private Button transferButton;
    private EditText editTextTransferAmount;
    private DatabaseHelper dbHelper;
    private int previousSalesAmount = 0;
    private String userId;
    private int updatedAmount;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proceeds_transfer);

        initializeViews();
        setupListeners();
        setupDatabase();
        setupResultLauncher();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        transferButton = findViewById(R.id.transferButton);
        editTextTransferAmount = findViewById(R.id.editTextTransferAmount);
    }

    private void setupListeners() {
        backButton.setOnClickListener(this);
        transferButton.setOnClickListener(this);
    }

    private void setupDatabase() {
        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);

        if (userId != null) {
            int salesAmount = dbHelper.getSalesAmount(userId);
            editTextTransferAmount.setText(String.valueOf(salesAmount));
        }
    }

    private void setupResultLauncher() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            updatedAmount = data.getIntExtra("updatedAmount", 0);
                            editTextTransferAmount.setText(String.valueOf(updatedAmount));
                            Log.d(TAG, "updatedAmount: " + updatedAmount);

                            // ここで売上金額をデータベースに更新
                            if (userId != null) {
                                dbHelper.updateSalesAmount(userId, updatedAmount);
                                Log.d(TAG, "SalesAmount updated in DB for userId: " + userId);
                            }
                        }
                        navigateToSalesTransferSuccess();
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        revertSalesData();
                        recreate();
                    }
                }
        );
    }

    private void getPreviousSalesAmount() {
        previousSalesAmount = dbHelper.getSalesAmount(userId);
    }

    private void revertSalesData() {
        dbHelper.updateSalesAmount(userId, previousSalesAmount);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            navigateToAccountInformation();
        } else if (v.getId() == R.id.transferButton) {
            handleTransferButtonClick();
        }
    }

    private void handleTransferButtonClick() {
        String transferAmountStr = editTextTransferAmount.getText().toString();
        if (transferAmountStr.isEmpty()) {
            editTextTransferAmount.setError("振込金額を入力してください");
            return;
        }

        int transferAmount = Integer.parseInt(transferAmountStr);
        getPreviousSalesAmount();

        if (canProceedWithTransfer(transferAmount)) {
            updateSalesData(transferAmount);
            navigateToSalesTransferCheck();
        }
    }

    private boolean canProceedWithTransfer(int transferAmount) {
        int currentAmount = dbHelper.getSalesAmount(userId);
        updatedAmount = currentAmount - transferAmount;

        Log.d(TAG, "currentAmount: " + currentAmount + ", updatedAmount: " + updatedAmount);

        if (updatedAmount < 0) {
            editTextTransferAmount.setError("残高が不足しています");
            return false;
        }

        return true;
    }

    private void updateSalesData(int transferAmount) {
        int currentAmount = dbHelper.getSalesAmount(userId);
        int updatedAmount = currentAmount - transferAmount;
        dbHelper.updateSalesAmount(userId, updatedAmount);
    }

    private void navigateToAccountInformation() {
        Intent intent = new Intent(getApplication(), AccountInfomation.class);
        startActivity(intent);
    }

    private void navigateToSalesTransferCheck() {
        Intent intent = new Intent(this, SalesTransferCheck.class);
        intent.putExtra("updatedAmount", updatedAmount);
        resultLauncher.launch(intent);
    }

    private void navigateToSalesTransferSuccess() {
        Intent intent = new Intent(getApplication(), SalesTransferSuccess.class);
        startActivity(intent);
    }
}