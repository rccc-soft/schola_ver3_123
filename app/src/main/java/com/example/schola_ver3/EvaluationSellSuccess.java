package com.example.schola_ver3;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.  ;

//EvaluationSellから
public class EvaluationSellSuccess extends AppCompatActivity implements View.OnClickListener{
    private Button completebtn;

    private String temporaryProductId;
    //editとかDBに必要なものを追加

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_complete);

        completebtn = findViewById(R.id.completebtn);
        completebtn.setOnClickListener(this);

        // Intent から productId を受け取る
        Intent intent = getIntent();
        temporaryProductId = intent.getStringExtra("product_id");

        // 商品IDが正しい場合、商品を削除
        if (temporaryProductId != null && !temporaryProductId.isEmpty()) {
            deleteProduct(temporaryProductId);
        }

    }

    private void deleteProduct(String temporaryProductId) {
        try {
            // 商品削除処理を呼び出す
            ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
            dbHelper.deleteProductById(temporaryProductId); // 商品IDで削除
            Log.d("EvaluationSellSuccess", "Product deleted: " + temporaryProductId);
        } catch (Exception e) {
            Log.e("EvaluationSellSuccess", "Error deleting product", e);
        }
    }

    @Override
    public void onClick(View v) {
        //ホーム画面へ
        if (v.getId() == R.id.completebtn) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
    }
}
