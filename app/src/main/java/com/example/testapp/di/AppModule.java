package com.example.testapp.di;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.testapp.R;
import com.example.testapp.imageloader.core.DisplayImageOptions;
import com.example.testapp.models.User;
import com.example.testapp.util.Constants;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static DisplayImageOptions provideDisplayImageOptions() {
        Map<String, String> headers = new HashMap();
        headers.put("User-Agent", "default-user-agent");

        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(headers)
                .build();
    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.drawable.logo);
    }

    @Singleton
    @Provides
    @Named("app_user")
    static User someUser() {
        return new User();
    }

}
















