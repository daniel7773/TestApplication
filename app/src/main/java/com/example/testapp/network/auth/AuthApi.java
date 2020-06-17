package com.example.testapp.network.auth;

import com.example.testapp.models.User;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuthApi {

    @GET("users/{id}")
    Flowable<User> getUser(
            @Path("id") int id
    );

    @GET("users")
    Flowable<List<User>> getUsers();
}
