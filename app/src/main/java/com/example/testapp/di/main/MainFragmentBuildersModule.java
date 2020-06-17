package com.example.testapp.di.main;



import com.example.testapp.ui.main.photos.PhotosFragment;

import dagger.Component;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract PhotosFragment contributePhotosFragment();
}
