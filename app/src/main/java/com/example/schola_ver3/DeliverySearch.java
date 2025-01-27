package com.example.schola_ver3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeliverySearch extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editTextSearch;
    private ImageButton backButton;
    private ListView listViewResults;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverysearch);

        dbHelper = new DatabaseHelper(this);
        editTextSearch = findViewById(R.id.editTextText);
        backButton = findViewById(R.id.backButton);
        listViewResults = findViewById(R.id.resultsListView);
        results = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        listViewResults.setAdapter(adapter);

        // テストデータを挿入
        dbHelper.insertTestDeliveryAddresses();

        // 初期表示で全ての配送先を表示
        displayAllDeliveryAddresses();

        // 戻るボタンのクリックイベント
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DeliveryHomePageに遷移
                Intent intent = new Intent(DeliverySearch.this, DeliveryHomePage.class);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        });

        // EditTextのテキスト変更リスナー
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // テキスト変更前の処理（今回は不要）
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // テキスト変更時の処理
                String searchText = s.toString().trim();
                if (!searchText.isEmpty()) {
                    searchDeliveryAddressById(searchText);
                } else {
                    displayAllDeliveryAddresses(); // 入力が空の場合は全ての配送先を表示
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // テキスト変更後の処理（今回は不要）
            }
        });
    }

    // 全ての配送先を表示するメソッド
    private void displayAllDeliveryAddresses() {
        results.clear();
        Cursor cursor = dbHelper.getAllDeliveryAddresses();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String deliveryAddressInfo = formatDeliveryAddressInfo(cursor);
                results.add(deliveryAddressInfo);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            results.add("配送先データがありません");
        }
        adapter.notifyDataSetChanged();
    }

    // 配送先IDで検索するメソッド
    private void searchDeliveryAddressById(String deliveryAddressId) {
        results.clear();
        Cursor cursor = dbHelper.getDeliveryAddressById(deliveryAddressId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String deliveryAddressInfo = formatDeliveryAddressInfo(cursor);
                results.add(deliveryAddressInfo);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            results.add("該当する配送先が見つかりません");
        }
        adapter.notifyDataSetChanged();
    }

    // 配送先情報をフォーマットするメソッド
    private String formatDeliveryAddressInfo(Cursor cursor) {
        int deliveryAddressIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_ADDRESS_ID);
        int memberNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MEMBER_NAME);
        int postalCodeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_POSTAL_CODE);
        int addressIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS);
        int phoneNumberIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER);

        // カラムが見つからない場合はエラーメッセージを返す
        if (deliveryAddressIdIndex == -1 || memberNameIndex == -1 || postalCodeIndex == -1 || addressIndex == -1 || phoneNumberIndex == -1) {
            Log.e("DeliverySearch", "カラムが見つかりません");
            return "カラムが見つかりません";
        }

        String deliveryAddressId = cursor.getString(deliveryAddressIdIndex);
        String memberName = cursor.getString(memberNameIndex);
        String postalCode = cursor.getString(postalCodeIndex);
        String address = cursor.getString(addressIndex);
        String phoneNumber = cursor.getString(phoneNumberIndex);

        return "配送先ID: " + deliveryAddressId + "\n会員者名: " + memberName + "\n郵便番号: " + postalCode + "\n住所: " + address + "\n電話番号: " + phoneNumber;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}