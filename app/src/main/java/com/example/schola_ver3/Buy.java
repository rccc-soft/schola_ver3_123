package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Buy extends AppCompatActivity {
    private RadioButton deliverySelectButton;
    private RadioButton handDeliverySelectButton;
    private RadioButton creditCardSelectButton;
    private RadioButton electronicMoneySelectButton;
    private TextView addressTextView;
    private TextView priceTextView;
    private Button buyButton;
    private ProductDatabaseHelper dbHelper;
    private String selectedPaymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        initializeViews();
        dbHelper = new ProductDatabaseHelper(this);

        setDeliveryMethod();
        setAddress();
        setPrice();
        setupPaymentMethodListeners();
        setupBuyButtonListener();
    }

    private void initializeViews() {
        deliverySelectButton = findViewById(R.id.deliverySelectButton);
        handDeliverySelectButton = findViewById(R.id.handDeliverySelectButton);
        creditCardSelectButton = findViewById(R.id.creditCardSelectButton);
        electronicMoneySelectButton = findViewById(R.id.electronicMoneySelectButton);
        addressTextView = findViewById(R.id.textView13);
        priceTextView = findViewById(R.id.textView12);
        buyButton = findViewById(R.id.buyButton);
    }

    private void setDeliveryMethod() {
        SharedPreferences prefs = getSharedPreferences("ProductPrefs", MODE_PRIVATE);
        String productId = prefs.getString("productId", "");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {ProductDatabaseHelper.COLUMN_DELIVERY};
        String selection = ProductDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {productId};

        Cursor cursor = db.query(
                ProductDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            String deliveryMethod = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_DELIVERY));
            if ("配送".equals(deliveryMethod)) {
                deliverySelectButton.setChecked(true);
            } else if ("手渡し".equals(deliveryMethod)) {
                handDeliverySelectButton.setChecked(true);
            }
        }
        cursor.close();
    }

    private void setAddress() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        // ここで配送先データベースからユーザーの住所を取得し、addressTextViewにセットする
        // 例: addressTextView.setText(getAddressFromDatabase(userId));
    }

    private void setPrice() {
        SharedPreferences prefs = getSharedPreferences("ProductPrefs", MODE_PRIVATE);
        String productId = prefs.getString("productId", "");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {ProductDatabaseHelper.COLUMN_PRICE};
        String selection = ProductDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {productId};

        Cursor cursor = db.query(
                ProductDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRICE));
            priceTextView.setText(String.valueOf(price));
        }
        cursor.close();
    }

    private void setupPaymentMethodListeners() {
        creditCardSelectButton.setOnClickListener(v -> {
            creditCardSelectButton.setChecked(true);
            electronicMoneySelectButton.setChecked(false);
            selectedPaymentMethod = "クレジットカード";
        });

        electronicMoneySelectButton.setOnClickListener(v -> {
            electronicMoneySelectButton.setChecked(true);
            creditCardSelectButton.setChecked(false);
            selectedPaymentMethod = "電子マネー";
        });
    }

    private void setupBuyButtonListener() {
        buyButton.setOnClickListener(v -> {
            Intent intent = new Intent(Buy.this, BuyCheck.class);
            intent.putExtra("paymentMethod", selectedPaymentMethod);
            startActivity(intent);
        });
    }
}