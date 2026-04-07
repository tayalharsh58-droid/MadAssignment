package com.example.myapplication;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    // Click listener interface
    public interface ImageClickListener {
        void onClick(Uri uri);
    }

    private List<Uri> imageList;
    private ImageClickListener clickListener;

    public ImageAdapter(List<Uri> imageList, ImageClickListener clickListener) {
        this.imageList = imageList;
        this.clickListener = clickListener;
    }

    // ViewHolder class
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ImageViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.ivGridImage);
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageList.get(position);

        Glide.with(holder.img.getContext())
                .load(imageUri)
                .centerCrop()
                .into(holder.img);

        holder.img.setOnClickListener(view -> clickListener.onClick(imageUri));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
