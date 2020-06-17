package com.example.testapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.testapp.ui.auth.AuthActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Inject
    public SessionManager sessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeObservers();
    }

    private void subscribeObservers() {
        sessionManager.getAuthUser().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {
                    case LOADING: {
                        Log.d(TAG, "onChanged: BaseActivity: LOADING...");
                        break;
                    }

                    case AUTHENTICATED: {
                        Log.d(TAG, "onChanged: BaseActivity: AUTHENTICATED... " +
                                "Authenticated as: " + userAuthResource.data.getUsernameSafe());
                        break;
                    }

                    case ERROR: {
                        Log.d(TAG, "onChanged: BaseActivity: ERROR...");
                        break;
                    }

                    case NOT_AUTHENTICATED: {
                        Log.d(TAG, "onChanged: BaseActivity: NOT AUTHENTICATED. Navigating to Login screen.");
                        navLoginScreen();
                        break;
                    }
                }
            }
        });
    }

    private void navLoginScreen() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}















