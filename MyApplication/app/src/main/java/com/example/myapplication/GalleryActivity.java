package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private String selectedFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        selectedFolder = getIntent().getStringExtra("folder_uri");

        if (selectedFolder == null) {
            Toast.makeText(this, "Folder not selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Uri folderUri = Uri.parse(selectedFolder);
        List<Uri> imageUris = fetchImages(folderUri);

        if (imageUris.size() == 0) {
            Toast.makeText(this, "No images available", Toast.LENGTH_LONG).show();
        }

        setupRecyclerView(imageUris);
    }

    private void setupRecyclerView(List<Uri> imageUris) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewGallery);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        ImageAdapter adapter = new ImageAdapter(imageUris, uri -> openImageDetail(uri));
        recyclerView.setAdapter(adapter);
    }

    private void openImageDetail(Uri uri) {
        Intent i = new Intent(this, ImageDetailActivity.class);
        i.putExtra("image_uri", uri.toString());
        i.putExtra("folder_uri", selectedFolder);
        startActivity(i);
    }

    private List<Uri> fetchImages(Uri folderUri) {
        List<Uri> list = new ArrayList<>();

        try {
            DocumentFile directory = DocumentFile.fromTreeUri(this, folderUri);

            if (directory == null) return list;

            for (DocumentFile file : directory.listFiles()) {
                String type = file.getType();

                if (type != null && type.startsWith("image/")) {
                    list.add(file.getUri());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}