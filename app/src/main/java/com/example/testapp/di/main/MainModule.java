package com.example.testapp.di.main;


import com.example.testapp.imageloader.core.DisplayImageOptions;
import com.example.testapp.imageloader.core.ImageLoader;
import com.example.testapp.network.main.MainApi;
import com.example.testapp.ui.main.photos.PhotosRecyclerAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScope
    @Provides
    static PhotosRecyclerAdapter provideAdapter(ImageLoader imageLoader, DisplayImageOptions displayImageOptions) {
        return new PhotosRecyclerAdapter(imageLoader, displayImageOptions);
    }

    @MainScope
    @Provides
    static MainApi provideMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }
}
