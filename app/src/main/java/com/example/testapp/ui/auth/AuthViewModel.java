package com.example.testapp.ui.auth;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.example.testapp.SessionManager;
import com.example.testapp.models.User;
import com.example.testapp.network.auth.AuthApi;
import com.example.testapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";
    private AuthPresenter authPresenter = new AuthPresenter(this);

    // inject
    private final SessionManager sessionManager; // @Singleton scoped dependency
    private final AuthApi authApi; // @AuthScope scoped dependency

    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.authApi = authApi;
        Log.d(TAG, "AuthViewModel: viewmodel is working...");
    }

    public LiveData<AuthResource<List<User>>> observeUsersState() {
        return getUsers();
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getAuthUser();
    }

    public void authenticateWithId(int userId) {
        Log.d(TAG, "attemptLogin: attempting to login.");
        sessionManager.authenticateWithId(queryUserId(userId));
    }

    private LiveData<AuthResource<User>> queryUserId(int userId) {

        return LiveDataReactiveStreams.fromPublisher(authApi.getUser(userId)
                .onErrorReturn(throwable -> {
                    User errorUser = new User();
                    errorUser.setId(-1);
                    return errorUser;
                })
                .map((Function<User, AuthResource<User>>) user -> {
                    if (user.getId() == -1) {
                        return AuthResource.error("Could not authenticate", null);
                    }
                    return AuthResource.authenticated(user);
                })
                .subscribeOn(Schedulers.io()));

    }

    LiveData<AuthResource<List<User>>> getUsers() {
        return LiveDataReactiveStreams.fromPublisher(authApi.getUsers()
                .onErrorReturn(throwable -> {
                    List<User> errorUser = new ArrayList<>();
                    User user = new User();
                    user.setId(Constants.userErrorId);
                    return errorUser;
                })
                .map((Function<List<User>, AuthResource<List<User>>>) users -> {
                    if (users.size() > 0 && users.get(0).getId() == Constants.userErrorId) {
                        return AuthResource.error("Could not load info about users", null);
                    }
                    return AuthResource.success(users);
                })
                .subscribeOn(Schedulers.io()));
    }

    public AuthPresenter getAuthPresenter() {
        return authPresenter;
    }
}









