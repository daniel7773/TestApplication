package com.example.testapp.ui.main.photos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testapp.SessionManager;
import com.example.testapp.models.Photo;
import com.example.testapp.network.main.MainApi;
import com.example.testapp.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class PhotosViewModel extends ViewModel {

    private static final String TAG = "PhotosViewModel";

    int id = 1;
    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<Photo>>> photos = new MediatorLiveData();

    boolean isQueryExhausted = false;
    boolean isPerformingQuery = false;
    int albumId = 1;
    String QUERY_EXHAUSTED = "No more results";
    private boolean cancelRequest = false;
    private long requestStartTime = 0;


    @Inject
    public PhotosViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "PostsViewModel: viewmodel is working...");
    }

    public void searchPhotos(int _albumId) {
        albumId = _albumId;
        if (!isPerformingQuery) {
            if (_albumId == 0) {
                albumId = 1;
            }
            isQueryExhausted = false;
            executeSearch();
        }

    }

    public void searchNextAlbum() {
        if (!isQueryExhausted && !isPerformingQuery) {
            albumId += 1;
            albumId = correctAlbumId(albumId);
            executeSearch();
        }
    }

    private int correctAlbumId(int albumId) {
        int fixedAlbumId = 1;
        try {
            int userId = sessionManager.getAuthUser().getValue().data.getId();
            fixedAlbumId = albumId;
            if (userId > 1) {
                fixedAlbumId = fixedAlbumId + 10;
            }
            if (fixedAlbumId >= userId * 10) {
                isQueryExhausted = true;
            }
        } catch (Exception e) {
            Log.d(TAG, "error while loading photos related to user");
            sessionManager.logOut();
        }
        return fixedAlbumId;
    }

    public void executeSearch() {
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        final LiveData<Resource<List<Photo>>> source = LiveDataReactiveStreams.fromPublisher(

                mainApi.getPhotosFromUser(albumId)

                        .onErrorReturn(throwable -> {
                            Log.d(TAG, "albumId: " + albumId);
                            Log.e(TAG, "exception when getting photos: ", throwable);
                            Photo photo = new Photo();
                            photo.setId(-1);
                            ArrayList<Photo> photos = new ArrayList<>();
                            photos.add(photo);
                            return photos;
                        })

                        .map(photos -> {
                            Log.d(TAG, "mapping photos");
                            for (Photo photo : photos) {
                                Log.d(TAG, photo.getId() + " - ID");
                            }
                            Log.d(TAG, "Success");
                            return Resource.success(photos);
                        })

                        .subscribeOn(Schedulers.io())
        );
        if (photos == null) {
            photos = new MediatorLiveData<>();
        }
        setPhotosSource(source);
    }

    private void setPhotosSource(LiveData<Resource<List<Photo>>> source) {
        photos.addSource(source, listResource -> {
            if (!cancelRequest) {
                if (listResource != null) {
                    photos.setValue(listResource);
                    Log.d(TAG, "request time: " + ((System.currentTimeMillis() - requestStartTime) / 1000));
                    if (listResource.status == Resource.Status.SUCCESS) {
                        isPerformingQuery = false;
                        if (listResource.data != null) {
                            if (listResource.data.isEmpty()) {
                                Log.d(TAG, "On changed: query is exhausted...");
                                photos.setValue(new Resource<>(Resource.Status.ERROR, listResource.data, QUERY_EXHAUSTED));
                                isPerformingQuery = true;
                            }
                        }
                        photos.removeSource(source);
                    } else if (listResource.status == Resource.Status.ERROR) {
                        isPerformingQuery = false;
                        photos.removeSource(source);
                    }

                } else {
                    photos.removeSource(source);
                }
            } else {
                photos.removeSource(source);
            }
        });
    }

    public LiveData<Resource<List<Photo>>> getPhotos() {
        return photos;
    }

    public void cancelSearchRequest() {
        if (isPerformingQuery) {
            Log.d(TAG, "cancel search request: canceling ");
            cancelRequest = true;
            isPerformingQuery = false;
            albumId = 1;
        }
    }
}




















