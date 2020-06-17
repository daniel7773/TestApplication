package com.example.testapp.ui.auth;

public class AuthPresenter {

    AuthViewModel authViewModel;

    AuthPresenter(AuthViewModel authViewModel) {
        this.authViewModel = authViewModel;
    }

    public void logIn(int id) {
        authViewModel.authenticateWithId(id);
    }
}
