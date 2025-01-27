package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example. ;

public class ProductSearch extends AppCompatActivity implements View.OnClickListener {
    private Button searchbtn;
    private ImageButton backButton;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        searchEditText = findViewById(R.id.searchEditText);
        searchbtn = findViewById(R.id.searchbtn);
    }

    private void setupClickListeners() {
        if (searchbtn != null) searchbtn.setOnClickListener(this);
        if (backButton != null) backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchbtn) {
            performSearch();
        } else if (v.getId() == R.id.backButton) {
            finish();
        }
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "検索キーワードを入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        if (query.length() > 30) {
            Toast.makeText(this, "検索キーワードは30文字以内で入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        if (containsSpecialChars(query)) {
            Toast.makeText(this, "検索キーワードに特殊文字は使用できません", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, SearchResult.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }

    private boolean containsSpecialChars(String input) {
        String specialChars = "\"'\\;:[]{}";
        for (char c : specialChars.toCharArray()) {
            if (input.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
}