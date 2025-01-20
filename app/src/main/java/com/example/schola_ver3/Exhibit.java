package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Exhibit extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> resultLauncher;
    private ActivityResultLauncher<String> imagePickerLauncher;

    private Button decisionbtn;
    private LinearLayout addpicture;
    private EditText productNameEditText, productDescriptionEditText, productPriceEditText;
    private ImageView productImageView;
    private Spinner categorySpinner, deliveryMethodSpinner, regionSpinner;
    private ProductDatabaseHelper dbHelper;
    private byte[] imageByteArray; // 画像データを保持
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exhibit);

        initializeViews();
        setupSpinners();
        setupResultLauncher();
        setupImagePickerLauncher();
        dbHelper = new ProductDatabaseHelper(this);
    }

    private void initializeViews() {
        decisionbtn = findViewById(R.id.decisionbtn);
        decisionbtn.setOnClickListener(this);
        addpicture = findViewById(R.id.addpicture);
        addpicture.setOnClickListener(this);
        productNameEditText = findViewById(R.id.productNameEditText);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productImageView = findViewById(R.id.productImageView);
        categorySpinner = findViewById(R.id.categorySpinner);
        deliveryMethodSpinner = findViewById(R.id.deliveryMethodSpinner);
        regionSpinner = findViewById(R.id.regionSpinner);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> deliveryAdapter = ArrayAdapter.createFromResource(this,
                R.array.delivery_method_array, android.R.layout.simple_spinner_item);
        deliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryMethodSpinner.setAdapter(deliveryAdapter);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this,
                R.array.region_array, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);
    }

    private void setupResultLauncher() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        saveProductToDatabase();
                        navigateToExhibitSuccess();
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        navigateToHomePage();
                    }
                }
        );
    }

    private void setupImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            productImageView.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imageByteArray = stream.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.decisionbtn) {
            if (validateInput()) {
                navigateToExhibitSave();
            }
        } else if (v.getId() == R.id.addpicture) {
            imagePickerLauncher.launch("image/*");
        } else if (v.getId() == R.id.backButton) {
//            Intent intent = new Intent(this, HomePage.class);
//            startActivity(intent);
            finish();
        }
    }

    private boolean validateInput() {
        boolean isValid = true;

        if (productNameEditText.getText().toString().trim().isEmpty()) {
            productNameEditText.setError("商品名を入力してください");
            isValid = false;
        } else if (productNameEditText.getText().toString().length() > 30) {
            productNameEditText.setError("商品名は30文字以内で入力してください");
            isValid = false;
        }

        if (productDescriptionEditText.getText().toString().length() > 1000) {
            productDescriptionEditText.setError("商品説明は1000文字以内で入力してください");
            isValid = false;
        }

        try {
            int price = Integer.parseInt(productPriceEditText.getText().toString());
            if (price < 10 || price > 99999) {
                productPriceEditText.setError("価格は10円から99999円の間で入力してください");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            productPriceEditText.setError("有効な価格を入力してください");
            isValid = false;
        }

        if (categorySpinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            ((TextView)categorySpinner.getSelectedView()).setError("カテゴリを選択してください");
            isValid = false;
        }

        if (imageByteArray == null) {
            // 画像が選択されていない場合のエラー処理
            return false;
        }

        return isValid;
    }

    private void navigateToExhibitSave() {
        Intent intent = new Intent(this, ExhibitSave.class);

        // 商品情報を Intent に渡す
        intent.putExtra("productName", productNameEditText.getText().toString());
        intent.putExtra("productDescription", productDescriptionEditText.getText().toString());
        intent.putExtra("productPrice", productPriceEditText.getText().toString());

        // カテゴリ、配送方法、地域を渡す
        intent.putExtra("category", categorySpinner.getSelectedItem().toString());
        intent.putExtra("deliveryMethod", deliveryMethodSpinner.getSelectedItem().toString());
        intent.putExtra("region", regionSpinner.getSelectedItem().toString());

        // 画像の URI を追加
        if (imageByteArray != null) {
            Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length), null, null));
            intent.putExtra("productImageUri", imageUri.toString());
        }

        resultLauncher.launch(intent);
    }

    private void saveProductToDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String productId = generateUniqueProductId();
        values.put("商品ID", productId);
        values.put("商品名", productNameEditText.getText().toString());
        values.put("商品説明", productDescriptionEditText.getText().toString());
        values.put("商品画像", imageByteArray); // 画像データを保存する場合
        values.put("商品URL", ""); // 仮の実装。実際のURLの生成ロジックが必要
        values.put("カテゴリ", categorySpinner.getSelectedItem().toString());
        values.put("金額", Integer.parseInt(productPriceEditText.getText().toString()));
        values.put("配送方法", deliveryMethodSpinner.getSelectedItem().toString());
        values.put("出品日時", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date()));
        values.put("地域", regionSpinner.getSelectedItemPosition() + 1); // 地域を保存する場合
        values.put("出品者ID", getCurrentUserId());
        values.put("購入済み", false);

        db.insert("商品テーブル", null, values);
    }

    private String generateUniqueProductId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void navigateToExhibitSuccess() {
        Intent intent = new Intent(getApplication(), ExhibitSuccess.class);
        startActivity(intent);
    }

    private void navigateToHomePage() {
        Intent intent = new Intent(getApplication(), HomePage.class);
        startActivity(intent);
    }

    private String getCurrentUserId() {
        // 現在のユーザーIDを取得するロジックを実装
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");
        return userId;
    }
}