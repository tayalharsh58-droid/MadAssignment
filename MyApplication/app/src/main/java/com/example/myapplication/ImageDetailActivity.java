package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageDetailActivity extends AppCompatActivity {

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_activity);

        String uriString = getIntent().getStringExtra("image_uri");
        if (uriString == null) return;

        selectedImageUri = Uri.parse(uriString);

        ImageView imageView = findViewById(R.id.ivDetail);
        TextView infoText   = findViewById(R.id.tvDetails);
        Button deleteBtn    = findViewById(R.id.btnDelete);

        displayImage(imageView);
        showImageInfo(infoText, uriString);

        deleteBtn.setOnClickListener(v -> confirmDelete());
    }

    private void displayImage(ImageView imgView) {
        Glide.with(this)
                .load(selectedImageUri)
                .into(imgView);
    }

    private void showImageInfo(TextView textView, String uriString) {
        DocumentFile file = DocumentFile.fromSingleUri(this, selectedImageUri);

        if (file != null) {
            String fileName = file.getName() != null ? file.getName() : "No Name";
            long fileSize   = file.length();
            long modified   = file.lastModified();

            String sizeFormatted = convertSize(fileSize);
            String dateFormatted = new SimpleDateFormat(
                    "dd MMM yyyy, HH:mm",
                    Locale.getDefault()
            ).format(new Date(modified));

            textView.setText(
                    "File Name : " + fileName + "\n" +
                            "Location  : " + uriString + "\n" +
                            "Size      : " + sizeFormatted + "\n" +
                            "Modified  : " + dateFormatted
            );
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Remove Image")
                .setMessage("Do you really want to delete this image?")
                .setPositiveButton("Yes", (dialog, which) -> deleteImage())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteImage() {
        DocumentFile file = DocumentFile.fromSingleUri(this, selectedImageUri);
        boolean isDeleted = file != null && file.delete();

        if (isDeleted) {
            Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }
}