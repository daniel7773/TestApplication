package com.example.testapp.network.main;


import com.example.testapp.models.Album;
import com.example.testapp.models.Photo;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainApi {

    @GET("photos")
    Flowable<List<Photo>> getPhotosFromUser(
            @Query("albumId") int id
    );

    @GET("albums")
    Flowable<List<Album>> getUserAlbums(
            @Query("userId") int id
    );

}