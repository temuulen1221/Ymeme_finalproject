package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;
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
            Bitmap bitmap = ((BitmapDrawable) imgMeme.getDrawable()).getBitmap();
            Bitmap memeBitmap = createMeme(bitmap);
            saveMeme(memeBitmap);
        });
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
        url = "https://meme-api.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                        Toast.makeText(MainActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private Bitmap createMeme(Bitmap bitmap) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);

        // Draw top text
        canvas.drawText(topText.getText().toString(), 50, 100, paint);
        // Draw bottom text
        canvas.drawText(bottomText.getText().toString(), 50, mutableBitmap.getHeight() - 50, paint);
        return mutableBitmap;
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
}
