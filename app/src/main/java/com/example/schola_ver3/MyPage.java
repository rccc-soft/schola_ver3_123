package com.example.schola_ver3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MyPage extends AppCompatActivity implements View.OnClickListener {
    private ImageView mybackbtn;
    private Button myexlistlistbtn;
    private Button mybuylistlistbtn;
    private Button myfavolistbtn;
    private Button mysaleslistbtn;
    private Button mycuslistbtn;
    private Button myoutlistbtn;
    private Button mysettinglistbtn;
    private ImageView mypage_homebtn;
    private ImageView mypage_searchbtn;
    private ImageView mypage_exhibitbtn;
    private ImageView mypage_favobtn;
    private ImageView mypage_mypagebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mypage);

        mybackbtn = findViewById(R.id.mybackbtn);
        mybackbtn.setOnClickListener(this);
        myexlistlistbtn = findViewById(R.id.myexlistlistbtn);
        myexlistlistbtn.setOnClickListener(this);
        mybuylistlistbtn = findViewById(R.id.mybuylistlistbtn);
        mybuylistlistbtn.setOnClickListener(this);
        myfavolistbtn = findViewById(R.id.myfavolistbtn);
        myfavolistbtn.setOnClickListener(this);
        mysaleslistbtn = findViewById(R.id.mysaleslistbtn);
        mysaleslistbtn.setOnClickListener(this);
        mycuslistbtn = findViewById(R.id.mycuslistbtn);
        mycuslistbtn.setOnClickListener(this);
        myoutlistbtn = findViewById(R.id.myoutlistbtn);
        myoutlistbtn.setOnClickListener(this);
        mysettinglistbtn = findViewById(R.id.mysettinglistbtn);
        mysettinglistbtn.setOnClickListener(this);
        mypage_homebtn = findViewById(R.id.mypage_homebtn);
        mypage_homebtn.setOnClickListener(this);
        mypage_searchbtn = findViewById(R.id.mypage_searchbtn);
        mypage_searchbtn.setOnClickListener(this);
        mypage_exhibitbtn = findViewById(R.id.mypage_exhibitbtn);
        mypage_exhibitbtn.setOnClickListener(this);
        mypage_favobtn = findViewById(R.id.mypage_favobtn);
        mypage_favobtn.setOnClickListener(this);
        mypage_mypagebtn = findViewById(R.id.mypage_mypagebtn);
        mypage_mypagebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.mybackbtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.myexlistlistbtn) {
            intent = new Intent(getApplication(), ExhibitList.class);
        } else if (v.getId() == R.id.mybuylistlistbtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.myfavolistbtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.mysaleslistbtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.mycuslistbtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.myoutlistbtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.mysettinglistbtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.mypage_homebtn) {
            intent = new Intent(getApplication(), HomePage.class);
        } else if (v.getId() == R.id.mypage_searchbtn) {
            intent = new Intent(getApplication(), ProductSearch.class);
        } else if (v.getId() == R.id.mypage_exhibitbtn) {
            intent = new Intent(getApplication(), Exhibit.class);
        } else if (v.getId() == R.id.mypage_favobtn) {
//            intent = new Intent(getApplication(), .class);
        } else if (v.getId() == R.id.mypage_mypagebtn) {
            intent = new Intent(getApplication(), MyPage.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}