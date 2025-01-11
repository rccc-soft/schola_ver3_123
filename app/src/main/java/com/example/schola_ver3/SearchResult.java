package com.example.schola_ver3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResult extends AppCompatActivity implements View.OnClickListener {
    private Button productbtn; // 商品詳細を見るボタン
    private ListView resultsListView;
    private ProductDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);

        dbHelper = new ProductDatabaseHelper(this);
        resultsListView = findViewById(R.id.resultsListView);

        // Intentから検索キーワードを取得
        Intent intent = getIntent();
        String query = intent.getStringExtra("SEARCH_QUERY");

        // 検索結果を表示する
        displaySearchResults(query);

        resultsListView.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, String> selectedItem = (HashMap<String, String>) resultsListView.getItemAtPosition(position);
            Intent detailIntent = new Intent(SearchResult.this, ProductDetail.class);
            detailIntent.putExtra("productName", selectedItem.get("商品名"));
            detailIntent.putExtra("productDescription", selectedItem.get("商品説明"));
            detailIntent.putExtra("category", selectedItem.get("カテゴリ"));
            detailIntent.putExtra("productPrice", selectedItem.get("金額"));
            detailIntent.putExtra("deliveryMethod", selectedItem.get("配送方法"));
            detailIntent.putExtra("date", selectedItem.get("出品日時"));
            detailIntent.putExtra("region", selectedItem.get("地域"));
            detailIntent.putExtra("sellerId", selectedItem.get("出品者ID"));
            startActivity(detailIntent);
        });
    }

    private void displaySearchResults(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 商品テーブル WHERE 商品名 LIKE ?", new String[]{"%" + query + "%"});

        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();
            item.put("商品名", cursor.getString(cursor.getColumnIndex("商品名")));
            item.put("金額", cursor.getString(cursor.getColumnIndex("金額")));
            item.put("商品説明", cursor.getString(cursor.getColumnIndex("商品説明")));
            item.put("カテゴリ", cursor.getString(cursor.getColumnIndex("カテゴリ")));
            item.put("配送方法", cursor.getString(cursor.getColumnIndex("配送方法")));
            item.put("出品日時", cursor.getString(cursor.getColumnIndex("出品日時")));
            item.put("地域", cursor.getString(cursor.getColumnIndex("地域")));
            item.put("出品者ID", cursor.getString(cursor.getColumnIndex("出品者ID")));
            results.add(item);
        }
        cursor.close();

        if (results.isEmpty()) {
            Toast.makeText(this, "該当する商品が見つかりませんでした", Toast.LENGTH_SHORT).show();
        } else {
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    results,
                    android.R.layout.simple_list_item_2,
                    new String[]{"商品名", "金額"},
                    new int[]{android.R.id.text1, android.R.id.text2}
            );
            resultsListView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        // 他のボタンの処理が必要な場合はここに追加
    }
}