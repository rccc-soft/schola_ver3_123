package com.example.schola_ver3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    private ImageView home_homebtn;
    private ImageView home_searchbtn;
    private ImageView home_exhibitbtn;
    private ImageView home_favobtn;
    private ImageView home_mypagebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        home_homebtn = findViewById(R.id.home_homebtn);
        home_homebtn.setOnClickListener(this);
        home_searchbtn = findViewById(R.id.home_searchbtn);
        home_searchbtn.setOnClickListener(this);
        home_exhibitbtn = findViewById(R.id.home_exhibitbtn);
        home_exhibitbtn.setOnClickListener(this);
        home_favobtn = findViewById(R.id.home_favobtn);
        home_favobtn.setOnClickListener(this);
        home_mypagebtn = findViewById(R.id.home_mypagebtn);
        home_mypagebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.home_homebtn) {
            intent = new Intent(getApplication(), HomePage.class);
        } else if (v.getId() == R.id.home_searchbtn) {
            intent = new Intent(getApplication(), ProductSearch.class);
        } else if (v.getId() == R.id.home_exhibitbtn) {
            intent = new Intent(getApplication(), Exhibit.class);
        } else if (v.getId() == R.id.home_favobtn) {
            //intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.home_mypagebtn) {
            intent = new Intent(getApplication(), MyPage.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}