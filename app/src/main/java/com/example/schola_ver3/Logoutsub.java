package com.example.schola_ver3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Logoutsub extends AppCompatActivity implements View.OnClickListener{
    private Button logoutNoButton;
    private Button logoutYesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logout);

        logoutNoButton = findViewById(R.id.logoutNoButton);
        logoutNoButton.setOnClickListener(this);
        logoutYesButton = findViewById(R.id.logoutYesButton);
        logoutYesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.logoutNoButton) {
            Intent intent = new Intent(this, MyPage.class);
            startActivity(intent);
        } else if (id == R.id.logoutYesButton) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");
            editor.apply();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }
}
