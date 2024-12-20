package com.example.ymeme; // Update with your actual package name

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide; // For image loading

public class EditMemeActivity extends AppCompatActivity {

    private ImageView memeImageView;
    private Button applyFilterButton;
    private Button addStickerButton;
    private Button saveButton;
    private Bitmap memeBitmap; // Store the meme bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meme); // Ensure this layout exists

        memeImageView = findViewById(R.id.meme_image_view);
        applyFilterButton = findViewById(R.id.apply_filter_button);
        addStickerButton = findViewById(R.id.add_sticker_button);
        saveButton = findViewById(R.id.save_button);

        // Get the meme URI from the intent
        Uri memeUri = getIntent().getParcelableExtra("memeUri");
        if (memeUri != null) {
            loadMeme(memeUri);
        } else {
            Toast.makeText(this, "Error loading meme.", Toast.LENGTH_SHORT).show();
            finish(); // Exit if the meme URI is invalid
        }

        // Set up button listeners
        applyFilterButton.setOnClickListener(v -> applyFilter());
        addStickerButton.setOnClickListener(v -> addSticker());
        saveButton.setOnClickListener(v -> saveMeme());
    }

    private void loadMeme(Uri memeUri) {
        // Load the meme bitmap from the URI using Glide for better performance
        Glide.with(this)
            .load(memeUri)
            .into(memeImageView);
    }

    private void applyFilter() {
        // Placeholder for filter application logic
        Toast.makeText(this, "Filter applied!", Toast.LENGTH_SHORT).show();
        // Implement actual filter logic here
    }

    private void addSticker() {
        // Placeholder for sticker adding logic
        Toast.makeText(this, "Sticker added!", Toast.LENGTH_SHORT).show();
        // Implement actual sticker logic here
    }

    private void saveMeme() {
        // Implement saving logic
        Toast.makeText(this, "Meme saved!", Toast.LENGTH_SHORT).show();
        // Add actual saving functionality here
    }
}
