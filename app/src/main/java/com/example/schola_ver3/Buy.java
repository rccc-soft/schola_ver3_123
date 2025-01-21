package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;

public class Buy extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Buy";
    private static final int REQUEST_BUY_CHECK = 1;

    private RadioButton deliverySelectButton;
    private RadioButton handDeliverySelectButton;
    private Spinner paymentMethodSpinner;
    private TextView addressTextView;
    private TextView priceTextView;
    private Button buyButton;
    private ProductDatabaseHelper dbHelper;
    private DatabaseHelper deliveryDbHelper;
    private String productId;
    private ImageButton imageButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(this);

        initializeViews();
        dbHelper = new ProductDatabaseHelper(this);
        deliveryDbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        productId = intent.getStringExtra("商品ID");
        if (productId == null) {
            Log.e(TAG, "商品IDがIntentに含まれていません。");
            finish();
            return;
        }

        setDeliveryMethod();
        setAddress();
        setPrice();
        setupPaymentMethodSpinner();
        setupBuyButtonListener();
    }

    private void initializeViews() {
        deliverySelectButton = findViewById(R.id.deliverySelectButton);
        handDeliverySelectButton = findViewById(R.id.handDeliverySelectButton);
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        addressTextView = findViewById(R.id.textView13);
        priceTextView = findViewById(R.id.textView12);
        buyButton = findViewById(R.id.buyButton);
    }

    private void setDeliveryMethod() {
        Cursor cursor = dbHelper.getProductInfo(productId);
        if (cursor != null && cursor.moveToFirst()) {
            String deliveryMethod = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_DELIVERY));
            if ("配送".equals(deliveryMethod)) {
                deliverySelectButton.setChecked(true);
                deliverySelectButton.setBackgroundResource(R.drawable.radio_button_selected);
                handDeliverySelectButton.setBackgroundResource(R.drawable.radio_button_normal);
            } else if ("手渡し".equals(deliveryMethod)) {
                handDeliverySelectButton.setChecked(true);
                handDeliverySelectButton.setBackgroundResource(R.drawable.radio_button_selected);
                deliverySelectButton.setBackgroundResource(R.drawable.radio_button_normal);
            }
            cursor.close();
        } else {
            Log.e(TAG, "商品情報の取得に失敗しました。");
        }
    }

    private void setAddress() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");

        Log.d(TAG, "User ID: " + userId);

        Cursor cursor = deliveryDbHelper.getDeliveryAddressByMemberId(userId);
        if (cursor != null && cursor.moveToFirst()) {
            String postalCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSTAL_CODE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS));
            if (address != null && !address.isEmpty()) {
                String fullAddress = postalCode + "\n" + address;
                addressTextView.setText(fullAddress);
            } else {
                addressTextView.setText("住所が見つかりません");
            }
            cursor.close();
        } else {
            addressTextView.setText("住所が見つかりません");
        }
    }

    private void setPrice() {
        Cursor cursor = dbHelper.getProductInfo(productId);
        if (cursor != null && cursor.moveToFirst()) {
            int price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRICE));
            priceTextView.setText(String.valueOf(price));
            cursor.close();
        } else {
            Log.e(TAG, "商品価格の取得に失敗しました。");
        }
    }

    private void setupPaymentMethodSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_method_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);
    }

    private void setupBuyButtonListener() {
        buyButton.setOnClickListener(v -> {
            String selectedPaymentMethod = paymentMethodSpinner.getSelectedItem().toString();
            Intent intent = new Intent(Buy.this, BuyCheck.class);
            intent.putExtra("paymentMethod", selectedPaymentMethod);
            intent.putExtra("product_id", productId);
            startActivityForResult(intent, REQUEST_BUY_CHECK);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BUY_CHECK) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(Buy.this, BuySuccess.class);
                startActivity(intent);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                // ユーザーが購入をキャンセルした場合の処理
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton2) {
            finish();
        }
    }
}