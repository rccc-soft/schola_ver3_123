package com.example.schola_ver3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResult extends AppCompatActivity implements View.OnClickListener{
    private ListView resultsListView;
    private ProductDatabaseHelper dbHelper;

    private EditText editTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);

        editTextText = findViewById(R.id.editTextText);
        editTextText.setOnClickListener(this);

        dbHelper = new ProductDatabaseHelper(this);
        resultsListView = findViewById(R.id.resultsListView);

        Intent intent = getIntent();
        String query = intent.getStringExtra("SEARCH_QUERY");

        displaySearchResults(query);

        resultsListView.setOnItemClickListener((parent, view, position, id) -> {
            HashMap<String, Object> selectedItem = (HashMap<String, Object>) resultsListView.getItemAtPosition(position);
            navigateToProductDetail(selectedItem);
        });
    }

    private void displaySearchResults(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "商品ID", "商品名", "商品説明", "商品画像", "カテゴリ", "金額", "配送方法", "出品日時", "地域", "出品者ID"
        };
        String selection = "商品名 LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};

        Cursor cursor = db.query(
                "商品テーブル",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        ArrayList<HashMap<String, Object>> results = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> item = new HashMap<>();
            for (String column : projection) {
                if (column.equals("商品画像")) {
                    item.put(column, cursor.getBlob(cursor.getColumnIndex(column)));
                } else {
                    item.put(column, cursor.getString(cursor.getColumnIndex(column)));
                }
            }
            results.add(item);
        }
        cursor.close();

        if (results.isEmpty()) {
            Toast.makeText(this, "該当する商品が見つかりませんでした", Toast.LENGTH_SHORT).show();
        } else {
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    results,
                    R.layout.search_result_item,
                    new String[]{"商品名", "金額", "商品画像"},
                    new int[]{R.id.productNameTextView, R.id.productPriceTextView, R.id.productImageView}
            );

            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof byte[]) {
                        ImageView imageView = (ImageView) view;
                        byte[] imageData = (byte[]) data;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        imageView.setImageBitmap(bitmap);
                        return true;
                    }
                    return false;
                }
            });

            resultsListView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null; // intentを初期化
        if (v.getId() == R.id.editTextText) {
            intent = new Intent(getApplication(), ProductSearch.class);
        }
        // intentがnullでない場合にstartActivityを呼び出す
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void navigateToProductDetail(HashMap<String, Object> item) {
        Intent detailIntent = new Intent(SearchResult.this, ProductDetail.class);
        for (String key : item.keySet()) {
            if (item.get(key) instanceof String) {
                detailIntent.putExtra(key, (String) item.get(key));
            } else if (item.get(key) instanceof byte[]) {
                detailIntent.putExtra(key, (byte[]) item.get(key));
            }
        }
        startActivity(detailIntent);
    }
}