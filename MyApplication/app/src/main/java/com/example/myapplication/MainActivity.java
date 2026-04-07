package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String photoPath = "";

    // Camera launcher
    private final ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
                if (result) {
                    Toast.makeText(this, "Image captured successfully!\n" + photoPath, Toast.LENGTH_LONG).show();
                }
            });

    // Folder picker launcher
    private final ActivityResultLauncher<Uri> pickFolderLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
                if (uri != null) {
                    Intent i = new Intent(MainActivity.this, GalleryActivity.class);
                    i.putExtra("folder_uri", uri.toString());
                    startActivity(i);
                }
            });

    // Permission launcher
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cameraBtn = findViewById(R.id.btnCamera);
        Button browseBtn = findViewById(R.id.btnBrowse);

        cameraBtn.setOnClickListener(view -> handleCameraClick());
        browseBtn.setOnClickListener(view -> pickFolderLauncher.launch(null));
    }

    private void handleCameraClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        try {
            File imageFile = createImageFile();

            Uri uri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    imageFile
            );

            cameraLauncher.launch(uri);

        } catch (IOException e) {
            Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file = File.createTempFile("PHOTO_" + time + "_", ".jpg", folder);
        photoPath = file.getAbsolutePath();

        return file;
    }
}