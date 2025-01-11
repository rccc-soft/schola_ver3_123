package com.example.schola_ver3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ExhibitList extends AppCompatActivity {
    private static final String TAG = "ExhibitList";
    private ListView exhibitListView;
    private ProductDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exhibit_list);

        dbHelper = new ProductDatabaseHelper(this);
        exhibitListView = findViewById(R.id.exhibitListView);

        displayUserProducts();

        exhibitListView.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> selectedItem = (HashMap<String, String>) exhibitListView.getItemAtPosition(position);
            navigateToExhibitEdit(selectedItem);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayUserProducts();
    }

    private void displayUserProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String userId = getCurrentUserId();
        Cursor cursor = db.rawQuery("SELECT * FROM 商品テーブル WHERE 出品者ID = ?", new String[]{userId});

        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();
            item.put("商品ID", cursor.getString(cursor.getColumnIndex("商品ID")));
            item.put("商品名", cursor.getString(cursor.getColumnIndex("商品名")));
            item.put("金額", cursor.getString(cursor.getColumnIndex("金額")));
            item.put("商品説明", cursor.getString(cursor.getColumnIndex("商品説明")));
            item.put("カテゴリ", cursor.getString(cursor.getColumnIndex("カテゴリ")));
            item.put("配送方法", cursor.getString(cursor.getColumnIndex("配送方法")));
            item.put("地域", cursor.getString(cursor.getColumnIndex("地域")));
            results.add(item);
            Log.d(TAG, "Product loaded: " + item);
        }
        cursor.close();

        if (results.isEmpty()) {
            Toast.makeText(this, "表示する商品がありません", Toast.LENGTH_SHORT).show();
        } else {
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    results,
                    android.R.layout.simple_list_item_2,
                    new String[]{"商品名", "金額"},
                    new int[]{android.R.id.text1, android.R.id.text2}
            );
            exhibitListView.setAdapter(adapter);
        }
    }

    private void navigateToExhibitEdit(HashMap<String, String> item) {
        Intent editIntent = new Intent(ExhibitList.this, ExhibitEdit.class);
        editIntent.putExtra("商品ID", item.get("商品ID"));
        editIntent.putExtra("商品名", item.get("商品名"));
        editIntent.putExtra("商品説明", item.get("商品説明"));
        editIntent.putExtra("金額", item.get("金額"));
        editIntent.putExtra("カテゴリ", item.get("カテゴリ"));
        editIntent.putExtra("配送方法", item.get("配送方法"));
        editIntent.putExtra("地域", item.get("地域"));
        Log.d(TAG, "Navigating to ExhibitEdit with Product ID: " + item.get("商品ID"));
        startActivity(editIntent);
    }

    private String getCurrentUserId() {
        // 現在のユーザーIDを取得するロジックを実装
        return "dummy_user_id";
    }
}