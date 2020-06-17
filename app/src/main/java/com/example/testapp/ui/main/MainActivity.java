package com.example.testapp.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.testapp.BaseActivity;
import com.example.testapp.R;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private MaterialTextView backButton;
    private MaterialTextView header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backButton = findViewById(R.id.back_button);
        header = findViewById(R.id.header);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(v -> sessionManager.logOut());
        header.setText(getString(R.string.photos));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                sessionManager.logOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

























