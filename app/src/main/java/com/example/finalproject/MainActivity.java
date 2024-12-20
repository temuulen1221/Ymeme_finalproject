package com.example.finalproject;

import androidx.annotation.Nullable```java
// Add a color picker for text color
private void applyTextStyle(Paint paint) {
    int textColor = getColor();
    paint.setColor(textColor);
    paint.setTextSize(getTextSize());
    paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
}```java
// Add a color picker for text color
private void applyTextStyle(Paint paint) {
    int textColor = getColor();
    paint.setColor(textColor);
    paint.setTextSize(getTextSize());
    paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
}

// Add a method to get the text color from user input
private int getColor() {
    // Implement a color picker or get the color from a predefined list
    // For simplicity, let's use a predefined list
    String[] colors = {"White", "Black", "Red", "Green", "Blue"};
    String selectedColor = "White"; // Get the selected color from user input
    switch (selectedColor) {
        case "White":
            return Color.WHITE;
        case "Black":
            return Color.BLACK;
        case "Red":
            return Color.RED;
        case "Green":
            return Color.GREEN;
        case "Blue":
            return Color.BLUE;
        default:
            return Color.WHITE; // default color
    }
}

// Add a method to get the text color from user input
private int getColor() {
    // Implement a color picker or get the color from a predefined list
    // For simplicity, let's use a predefined list
    String[] colors = {"White", "Black", "Red", "Green", "Blue"};
    String selectedColor = "White"; // Get the selected color from user input
    switch (selectedColor) {
        case "White":
            return Color.WHITE;
        case "Black":
            return Color.BLACK;
        case "Red":
            return Color.RED;
        case "Green":
            return Color.GREEN;
        case "Blue":
            return Color.BLUE;
        default:
            return Color.WHITE; // aasd
    }
}

// Add a method to get the text size from user input
private float getTextSize() {
    // Implement a size picker or get the size from a predefined list
    // For simplicity, let's use a predefined list
    String[] sizes = {"Small", "Medium", "Large"};
    String selectedSize = "Medium"; // Get the selected size from user input
    switch (selectedSize) {
        case "Small":
            return 50;
        case "Medium":
            return 100;
        case "Large":
            return 150;
        default:
            return 100;
    }
}
```;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressDialog;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ImageView imgMeme;
    Button btnNext, btnShare, btnSelectImage, btnSaveMeme;
    EditText topText, bottomText;
    String url;
    private static final int PICK_IMAGE = 1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgMeme = findViewById(R.id.imgMeme);
        btnNext = findViewById(R.id.btnNext);
        btnShare = findViewById(R.id.btnShare);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveMeme = findViewById(R.id.btnSaveMeme);
        topText = findViewById(R.id.topText);
        bottomText = findViewById(R.id.bottomText);

        apiCall();

        btnNext.setOnClickListener(v -> apiCall());
        btnShare.setOnClickListener(v -> shareMeme());
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        btnSaveMeme.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Bitmap bitmap = ((BitmapDrawable) imgMeme.getDrawable()).getBitmap();
                Bitmap memeBitmap = createMeme(bitmap);
                saveMeme(memeBitmap);
            }
        });

        if (savedInstanceState != null) {
            topText.setText(savedInstanceState.getString("topText"));
            bottomText.setText(savedInstanceState.getString("bottomText"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgMeme.setImageURI(selectedImage);
        }
    }

    public void apiCall() {
        showLoading();
        url = "https://meme-api.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        try {
                            url = response.getString("url");
                            Glide.with(MainActivity.this).load(url).into(imgMeme);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Failed to load meme.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        Toast.makeText(MainActivity.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private Bitmap createMeme(Bitmap bitmap) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        applyTextStyle(paint);

        canvas.drawText(topText.getText().toString(), 50, 100, paint);
        canvas.drawText(bottomText.getText().toString(), 50, mutableBitmap.getHeight() - 50, paint);
        return mutableBitmap;
    }

    private void applyTextStyle(Paint paint) {
        paint.setColor(Color.WHITE); // Change color based on user selection
        paint.setTextSize(100); // Change size based on user input
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
    }

    private void saveMeme(Bitmap bitmap) {
        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Meme", null);
        Uri uri = Uri.parse(bitmapPath);
        if (uri != null) {
            Toast.makeText(this, "Meme saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving meme!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareMeme() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgMeme.getDrawable();
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Meme", null);
            Uri uri = Uri.parse(bitmapPath);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/png");
                startActivity(Intent.createChooser(intent, "Share Meme"));
            } else {
                Toast.makeText(this, "Error sharing meme!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No meme to share!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("topText", topText.getText().toString());
        outState.putString("bottomText", bottomText.getText().toString());
    }
}
