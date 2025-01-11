package com.example.schola_ver3;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private ProductDatabaseHelper dbHelper;
    private TextView statusTextView;
    private Button testExhibitButton, testSearchButton, testUpdateButton, clearDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initializeViews();
        setupDatabase();
        generateTestData();
        setupClickListeners();
    }

    private void initializeViews() {
        statusTextView = findViewById(R.id.statusTextView);
        testExhibitButton = findViewById(R.id.testExhibitButton);
        testSearchButton = findViewById(R.id.testSearchButton);
        testUpdateButton = findViewById(R.id.testUpdateButton);
        clearDataButton = findViewById(R.id.clearDataButton);
    }

    private void setupDatabase() {
        dbHelper = new ProductDatabaseHelper(this);
    }

    private void generateTestData() {
        TestDataGenerator testDataGenerator = new TestDataGenerator(dbHelper);
        testDataGenerator.generateTestData();
        updateStatus("テストデータが生成されました");
    }

    private void setupClickListeners() {
        testExhibitButton.setOnClickListener(this);
        testSearchButton.setOnClickListener(this);
        testUpdateButton.setOnClickListener(this);
        clearDataButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.testExhibitButton) {
            testExhibit();
        } else if (v.getId() == R.id.testSearchButton) {
            testSearch();
        } else if (v.getId() == R.id.testUpdateButton) {
            testUpdate();
        } else if (v.getId() == R.id.clearDataButton) {
            clearTestData();
        }
    }

    private void testExhibit() {
        Intent intent = new Intent(this, Exhibit.class);
        startActivity(intent);
        updateStatus("出品テストを開始しました");
    }

    private void testSearch() {
        Intent intent = new Intent(this, ProductSearch.class);
        startActivity(intent);
        updateStatus("検索テストを開始しました");
    }

    private void testUpdate() {
        Intent intent = new Intent(this, ExhibitList.class);
        startActivity(intent);
        updateStatus("更新テストを開始しました");
    }

    private void clearTestData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("商品テーブル", null, null);
        updateStatus("テストデータがクリアされました");
    }

    private void updateStatus(String message) {
        statusTextView.setText(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}