package com.example.testapp.ui.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.User;
import com.example.testapp.ui.main.MainActivity;
import com.example.testapp.viewmodels.ViewModelProviderFactory;
import com.google.android.material.textview.MaterialTextView;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity {

    private static final String TAG = "AuthActivity";

    private AuthViewModel viewModel;
    private AuthAdapter authAdapter;

    private ProgressBar progressBar;
    private RecyclerView usersList;
    private MaterialTextView header;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Drawable logo;

    @Inject
    @Named("app_user")
    User userNumber1;

    @Inject
    @Named("auth_user")
    User userNumber2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        setTitle("Users");
        progressBar = findViewById(R.id.progress_bar);
        usersList = findViewById(R.id.recycler_view);
        header = findViewById(R.id.header);
        header.setText(R.string.users);

        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);

        authAdapter = new AuthAdapter(this, viewModel.getAuthPresenter());
        usersList.setAdapter(authAdapter);
        usersList.setLayoutManager(new LinearLayoutManager(this));

        subscribeObservers();

        viewModel.observeUsersState();
        Log.d(TAG, "onCreate: " + userNumber1);
        Log.d(TAG, "onCreate: " + userNumber2);
    }

    private void subscribeObservers() {
        viewModel.getUsers().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {

                    case LOADING: {
                        showProgressBar(true);
                        Log.d(TAG, "loading");
                        break;
                    }

                    case SUCCESS: {
                        showProgressBar(false);
                        Log.d(TAG, "success");
                        authAdapter.setItems(userAuthResource.data);
                        break;
                    }

                    case ERROR: {
                        showProgressBar(false);
                        Log.d(TAG, "error");
                        Toast.makeText(AuthActivity.this, userAuthResource.message
                                + "\n Some error occured", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            } else {
                Log.d(TAG, "userAuthResource == null");
            }
        });

        viewModel.observeAuthState().observe(this, userAuthResource -> {
            if (userAuthResource != null) {
                switch (userAuthResource.status) {

                    case LOADING: {
                        showProgressBar(true);
                        break;
                    }

                    case AUTHENTICATED: {
                        showProgressBar(false);
                        Log.d(TAG, "onChanged: LOGIN SUCCESS: " + userAuthResource.data.getUsernameSafe());
                        onLoginSuccess();
                        break;
                    }

                    case ERROR: {
                        showProgressBar(false);
                        Toast.makeText(AuthActivity.this, userAuthResource.message
                                + "\nCan't get user data", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case NOT_AUTHENTICATED: {
                        showProgressBar(false);
                        break;
                    }
                }
            }
        });
    }

    private void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess: login successful!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
