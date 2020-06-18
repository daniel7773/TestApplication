package com.example.testapp.ui.main.photos;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.imageloader.core.DisplayImageOptions;
import com.example.testapp.imageloader.core.ImageLoader;
import com.example.testapp.imageloader.core.ImageLoaderConfiguration;
import com.example.testapp.imageloader.core.assist.FailReason;
import com.example.testapp.imageloader.core.listener.SimpleImageLoadingListener;
import com.example.testapp.models.Photo;
import com.example.testapp.views.SquareImageView;

import java.util.ArrayList;
import java.util.List;


public class PhotosRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    DisplayImageOptions displayImageOptions;
    ImageLoaderConfiguration imageLoaderConfiguration;
    ImageLoader imageLoader;

    public PhotosRecyclerAdapter(ImageLoaderConfiguration imageLoaderConfiguration, DisplayImageOptions displayImageOptions, ImageLoader imageLoader) {
        this.displayImageOptions = displayImageOptions;
        this.imageLoaderConfiguration = imageLoaderConfiguration;
        this.imageLoader = imageLoader;
    }

    private List<Photo> photos = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_list_item, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PhotosViewHolder) holder).bind(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        if (this.photos == null) {
            this.photos = photos;
        } else {
            this.photos.addAll(photos);
        }
        notifyDataSetChanged();
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        SquareImageView image;
        ProgressBar progressBar;

        public PhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progress);
        }

        public void bind(Photo photo) {
            title.setText(photo.getTitle());

            imageLoader
                    .displayImage(photo.getThumbnailUrl(), image, displayImageOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, (imageUri, view, current, total) -> progressBar.setProgress(Math.round(100.0f * current / total)));
        }
    }
}







