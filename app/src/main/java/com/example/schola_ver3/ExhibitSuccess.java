package com.example.schola_ver3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ExhibitSuccess extends AppCompatActivity implements View.OnClickListener {
    private Button decisionbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exhibit_success);

        initializeViews();
    }

    private void initializeViews() {
        decisionbtn = findViewById(R.id.decisionbtn);
        decisionbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.decisionbtn) {
            navigateToHomePage();
        }
    }

    private void navigateToHomePage() {
        //HomePage
        Intent intent = new Intent(getApplication(), TestActivity.class);
        startActivity(intent);
        finish();
    }
}