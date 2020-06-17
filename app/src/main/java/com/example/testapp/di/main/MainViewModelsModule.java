package com.example.testapp.di.main;

import androidx.lifecycle.ViewModel;


import com.example.testapp.di.ViewModelKey;
import com.example.testapp.ui.main.photos.PhotosViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(PhotosViewModel.class)
    public abstract ViewModel bindPhotosViewModel(PhotosViewModel viewModel);
}




