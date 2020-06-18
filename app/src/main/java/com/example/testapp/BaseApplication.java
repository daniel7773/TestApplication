package com.example.testapp;


import com.example.testapp.di.DaggerAppComponent;
import com.example.testapp.imageloader.core.ImageLoader;
import com.example.testapp.imageloader.core.ImageLoaderConfiguration;
import com.example.testapp.imageloader.core.download.BaseImageDownloader;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this)
                .imageDownloader(new BaseImageDownloader(this, 20 * 1000, 30 * 1000))
                .build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}






