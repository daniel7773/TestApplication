package com.example.testapp.di;


import com.example.testapp.di.auth.AuthModule;
import com.example.testapp.di.auth.AuthScope;
import com.example.testapp.di.auth.AuthViewModelsModule;
import com.example.testapp.di.main.MainFragmentBuildersModule;
import com.example.testapp.di.main.MainModule;
import com.example.testapp.di.main.MainScope;
import com.example.testapp.di.main.MainViewModelsModule;
import com.example.testapp.ui.auth.AuthActivity;
import com.example.testapp.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthViewModelsModule.class, AuthModule.class})
    abstract AuthActivity contributeAuthActivity();


    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelsModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();

}
