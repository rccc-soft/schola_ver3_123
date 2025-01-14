package com.example.schola_ver3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    private ProductDatabaseHelper dbHelper;

    private EditText editTextText;
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

        editTextText = findViewById(R.id.editTextText);
        editTextText.setOnClickListener(this);

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

        dbHelper = new ProductDatabaseHelper(this);

        displayRandomProducts();
    }

    private void displayRandomProducts() {
        LinearLayout layout1 = findViewById(R.id.productRow1); // 1行目のレイアウト
        LinearLayout layout2 = findViewById(R.id.productRow2); // 2行目のレイアウト
        LinearLayout layout3 = findViewById(R.id.productRow3); // 3行目のレイアウト
        LinearLayout layout4 = findViewById(R.id.productRow4); // 4行目のレイアウト
        LinearLayout layout5 = findViewById(R.id.productRow5); // 5行目のレイアウト

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"商品ID", "商品名", "商品画像", "金額"};

        // データベースからすべての商品を取得
        Cursor cursor = db.query("商品テーブル", projection, null, null, null, null, null);

        ArrayList<HashMap<String, Object>> products = new ArrayList<>();

        while (cursor.moveToNext()) {
            HashMap<String, Object> product = new HashMap<>();
            product.put("商品ID", cursor.getString(cursor.getColumnIndex("商品ID")));
            product.put("商品名", cursor.getString(cursor.getColumnIndex("商品名")));
            product.put("金額", cursor.getString(cursor.getColumnIndex("金額")));
            product.put("商品画像", cursor.getBlob(cursor.getColumnIndex("商品画像")));
            products.add(product);
        }

        cursor.close();

        // ランダムに10個までの商品を選択
        Random random = new Random();
        int productCount = Math.min(products.size(), 10); // 10個以上あれば10個、なければその数だけ
        ArrayList<HashMap<String, Object>> selectedProducts = new ArrayList<>();

        while (selectedProducts.size() < productCount) {
            int randomIndex = random.nextInt(products.size());
            HashMap<String, Object> randomProduct = products.get(randomIndex);

            // 重複を避けるためにチェック
            if (!selectedProducts.contains(randomProduct)) {
                selectedProducts.add(randomProduct);
            }
        }

        for (int i = 0; i < selectedProducts.size(); i++) {
            HashMap<String, Object> product = selectedProducts.get(i);
            View productView = getLayoutInflater().inflate(R.layout.product_item, null);

            ImageView imageView = productView.findViewById(R.id.imageView1);
            TextView nameTextView = productView.findViewById(R.id.textView1);
            TextView priceTextView = productView.findViewById(R.id.textView5);

            nameTextView.setText((String) product.get("商品名"));
            priceTextView.setText((String) product.get("金額"));

            byte[] imageData = (byte[]) product.get("商品画像");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);

            // 商品がクリックされたときの処理
            String productId = (String) product.get("商品ID");
            productView.setOnClickListener(v -> navigateToProductDetail(product));

            // 行ごとに異なるレイアウトに追加（2列）
            if (i == 0 || i == 1) {
                layout1.addView(productView); //(1行目）
            } else if (i == 2 || i == 3){
                layout2.addView(productView); //（2行目）
            } else if (i == 4 || i == 5){
                layout3.addView(productView); // （3行目）
            } else if (i == 6 || i == 7){
                layout4.addView(productView); //（4行目）
            } else if (i == 8 || i == 9){
                layout5.addView(productView); // （5行目）
            }
        }
    }

    private void navigateToProductDetail(HashMap<String, Object> product) {
        Intent intent = new Intent(this, ProductDetail.class);
        String productId = (String) product.get("商品ID");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "商品ID", "商品名", "商品説明", "商品画像", "カテゴリ", "金額", "配送方法", "出品日時", "地域", "出品者ID"
        };
        String selection = "商品ID = ?";
        String[] selectionArgs = {productId};

        Cursor cursor = db.query(
                "商品テーブル",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            for (String column : projection) {
                if (column.equals("商品画像")) {
                    intent.putExtra(column, cursor.getBlob(cursor.getColumnIndex(column)));
                } else {
                    intent.putExtra(column, cursor.getString(cursor.getColumnIndex(column)));
                }
            }
        }
        cursor.close();

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null; // intentを初期化
        if (v.getId() == R.id.editTextText) {
            intent = new Intent(getApplication(), ProductSearch.class);
        } else if (v.getId() == R.id.home_homebtn) {
            intent = new Intent(getApplication(), HomePage.class);
        } else if (v.getId() == R.id.home_searchbtn) {
            intent = new Intent(getApplication(), ProductSearch.class);
        } else if (v.getId() == R.id.home_exhibitbtn) {
            intent = new Intent(getApplication(), Exhibit.class);
        } else if (v.getId() == R.id.home_favobtn) {
            // intentは未設定なので、必要なクラスを設定してください
//          intent = new Intent(getApplication(), Favorite.class); // 例：お気に入り画面への遷移
        } else if (v.getId() == R.id.home_mypagebtn) {
            intent = new Intent(getApplication(), MyPage.class);
        }

        // intentがnullでない場合にstartActivityを呼び出す
        if (intent != null) {
            startActivity(intent);
        }
    }
}