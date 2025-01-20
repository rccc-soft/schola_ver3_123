package com.example.schola_ver3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MemberDelete extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_delete);

        databaseHelper = new DatabaseHelper(this);

        // Intentで渡された会員IDを取得
        Intent intent = getIntent();
        memberId = intent.getStringExtra("member_id");

        TextView confirmMessage = findViewById(R.id.confirm_message);
        confirmMessage.setText("この会員情報を削除しますか？");

        Button confirmButton = findViewById(R.id.button_confirm_delete);
        Button cancelButton = findViewById(R.id.button_cancel_delete);

        confirmButton.setOnClickListener(v -> {
            // データベースから削除処理
            if (databaseHelper.deleteMember(memberId)) {
                // ログイン画面に遷移
                Intent loginIntent = new Intent(MemberDelete.this, Login.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
            } else {
                confirmMessage.setText("削除に失敗しました。");
            }
        });

        cancelButton.setOnClickListener(v -> {
            // 戻る
            finish();
        });
    }
}
