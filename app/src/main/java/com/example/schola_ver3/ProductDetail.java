package com.example.schola_ver3;

import static com.example.schola_ver3.ProductDatabaseHelper.COLUMN_SOLD;
import static com.example.schola_ver3.ProductDatabaseHelper.TABLE_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageButton favoriteButton;
    private DatabaseHelper dbHelper;
    private ProductDatabaseHelper productdbhelper;
    private String userId;

    private Button evaluationbtn;
    private Button profilebtn;
    private Button buybtn;
    private Button chatbtn;
    private ImageButton backbtn;

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView categoryTextView;
    private TextView priceTextView;
    private TextView deliveryMethodTextView;
    private TextView regionTextView;
    private TextView exhibituserTextView;

    private String productId;
    private String exhibituserId;

    private int age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        // Intentから商品IDを取得
        Intent intent = getIntent();
        productId = intent.getStringExtra("商品ID");

        // 商品IDをSharedPreferencesに保存
        if (productId != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("ProductPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("product_id", productId);
            editor.apply();
        }

        exhibituserId = intent.getStringExtra("出品者ID");

        favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(this);

        dbHelper = new DatabaseHelper(this);
        productdbhelper = new ProductDatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getString("user_id", null);

        updateFavoriteButtonState();

        initializeViews();
        setupClickListeners();
        displayProductDetails();
    }

    private void initializeViews() {
        evaluationbtn = findViewById(R.id.evaluationbtn);
        profilebtn = findViewById(R.id.profilebtn);
        buybtn = findViewById(R.id.buybtn);
        chatbtn = findViewById(R.id.chatbtn);
        backbtn = findViewById(R.id.backbtn);

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        priceTextView = findViewById(R.id.priceTextView);
        deliveryMethodTextView = findViewById(R.id.deliveryMethodTextView);
        regionTextView = findViewById(R.id.regionTextView);
        exhibituserTextView = findViewById(R.id.exhibituserTextView);
    }

    private void setupClickListeners() {
        evaluationbtn.setOnClickListener(this);
        profilebtn.setOnClickListener(this);
        buybtn.setOnClickListener(this);
        chatbtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    private void updateFavoriteButtonState() {
        if (userId != null && productId != null) {
            boolean isFavorite = dbHelper.isFavorite(productId, userId);
            favoriteButton.setImageResource(isFavorite ?
                    R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        }
    }

    private void displayProductDetails() {
        Intent intent = getIntent();

        byte[] imageData = intent.getByteArrayExtra("商品画像");
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            productImageView.setImageBitmap(bitmap);
        }

        productNameTextView.setText(intent.getStringExtra("商品名"));
        productDescriptionTextView.setText(intent.getStringExtra("商品説明"));
        categoryTextView.setText("カテゴリ: " + intent.getStringExtra("カテゴリ"));

        String price = intent.getStringExtra("金額");
        if (price != null && !price.isEmpty()) {
            priceTextView.setText("価格: " + price + "円");
        } else {
            priceTextView.setText("価格未設定");
        }

        deliveryMethodTextView.setText("配送方法: " + intent.getStringExtra("配送方法"));

        String regionValue = intent.getStringExtra("地域");
        if (regionValue != null && !regionValue.isEmpty()) {
            try {
                int regionIndex = Integer.parseInt(regionValue) - 1;
                String[] regions = getResources().getStringArray(R.array.region_array);
                if (regionIndex >= 0 && regionIndex < regions.length) {
                    regionTextView.setText("地域: " + regions[regionIndex]);
                } else {
                    regionTextView.setText("地域: 不明");
                }
            } catch (NumberFormatException e) {
                regionTextView.setText("地域: " + regionValue);
            }
        } else {
            regionTextView.setText("地域: 未設定");
        }

        exhibituserTextView.setText("出品者ID: " + intent.getStringExtra("出品者ID"));

        // ユーザーの生年月日を取得して高校生以上か未満かを判断
        checkUserAgeAndDisplayMessage();
    }

    private void checkUserAgeAndDisplayMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        if (userId != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = dbHelper.getMemberInfo(userId);

            if (cursor != null && cursor.moveToFirst()) {
                String birthday = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BIRTHDAY));

                // 生年月日がYYYYMMDD形式で保存されていると仮定
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    Date birthDate = sdf.parse(birthday);

                    // 現在の日付を取得
                    Calendar today = Calendar.getInstance();
                    Calendar birthCalendar = Calendar.getInstance();
                    birthCalendar.setTime(birthDate);

                    // 年齢計算
                    age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

                    // 誕生日が今年まだ来ていない場合は1歳引く
                    if (today.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                            (today.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                                    today.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
                        age--;
                    }

                    // 4月1日を基準とした高校生判定
                    Calendar graduationDate = Calendar.getInstance();
                    graduationDate.set(today.get(Calendar.YEAR), Calendar.APRIL, 1);

                    // ログを追加
                    Log.d("AgeCalculation", "Birthday: " + birthday);
                    Log.d("AgeCalculation", "Age: " + age);
                    Log.d("AgeCalculation", "Today's Year: " + today.get(Calendar.YEAR));
                    Log.d("AgeCalculation", "Birth Year: " + birthCalendar.get(Calendar.YEAR));
                    Log.d("AgeCalculation", "Graduation Year: " + graduationDate.get(Calendar.YEAR));

//                    if (age >= 16) { // 高校生以上（16歳以上）
//                        Toast.makeText(this, "高校生以上です。", Toast.LENGTH_SHORT).show();
//                    } else { // 高校生未満
//                        Toast.makeText(this, "高校生未満です。", Toast.LENGTH_SHORT).show();
//                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "誕生日のフォーマットが無効です。", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "ユーザー情報が見つかりません。", Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) cursor.close();
            db.close();
        } else {
            Toast.makeText(this, "ユーザーIDが見つかりません。", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backbtn) {
            finish();
        } else if(userId != exhibituserId) {
            boolean isSold = new ProductDatabaseHelper(this).isProductSold(productId);
            if(isSold) {
                // 購入済みの場合、購入者IDを確認
                String buyerId = dbHelper.getBuyerIdByItemId(productId);
                if (buyerId != null && buyerId.equals(userId)) {
                    // 自身が購入者の場合、一部の機能を有効にする
                    if (v.getId() == R.id.evaluationbtn) {
                        // 出品者評価ボタンの処理
                        Intent intent = new Intent(this, EvaluationSell.class);
                        startActivity(intent);
                    } else if (v.getId() == R.id.profilebtn) {
                        // 出品者プロフィール画面へ
                    } else if (v.getId() == R.id.chatbtn) {
                        Intent intent = new Intent(this, HomePage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "この商品は既に購入済みです", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "この商品は既に購入済みです", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 購入済みでない場合、全ての機能を有効にする
                if (v.getId() == R.id.profilebtn) {
                    // 出品者プロフィール画面へ
                } else if (v.getId() == R.id.buybtn) {
                    if (age >= 16) {
                        Intent intent = new Intent(this, Buy.class);
                        intent.putExtra("商品ID", productId);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, ParentCertification.class);
                        intent.putExtra("商品ID", productId);
                        startActivity(intent);
                    }
                } else if (v.getId() == R.id.chatbtn) {
                    Intent intent = new Intent(this, HomePage.class);
                    startActivity(intent);
                } else if (v.getId() == R.id.favoriteButton) {
                    toggleFavorite();
                }
            }
        } else {
            Toast.makeText(this, "出品者のため選択できません", Toast.LENGTH_SHORT).show();
        }
    }


    private void toggleFavorite() {
        if (userId == null || productId == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isFavorite = dbHelper.isFavorite(productId, userId);
        if (isFavorite) {
            dbHelper.removeFavorite(productId, userId);
//            Toast.makeText(this, "お気に入りから削除しました", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addFavorite(productId, userId);
//            Toast.makeText(this, "お気に入りに追加しました", Toast.LENGTH_SHORT).show();
        }
        updateFavoriteButtonState();
    }
}