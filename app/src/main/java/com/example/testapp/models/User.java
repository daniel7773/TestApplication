package com.example.testapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("username")
    @Expose
    private String username;


    public User(int id, String username, String email, String website) {
        this.id = id;
        this.username = username;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsernameSafe() {
        return username == null ? "Unknown user" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}






















