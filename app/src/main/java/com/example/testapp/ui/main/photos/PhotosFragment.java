package com.example.testapp.ui.main.photos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.util.VerticalSpacingItemDecoration;
import com.example.testapp.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PhotosFragment extends DaggerFragment {

    private static final String TAG = "PostsFragments";

    private RecyclerView recyclerView;

    @Inject
    PhotosRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    PhotosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);

        initRecyclerView();
        subscribeObervers();
        viewModel.searchPhotos(1);
    }

    private void subscribeObervers() {

        viewModel.getPhotos().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                switch (listResource.status) {

                    case LOADING: {
                        Log.d(TAG, "onChanged: LOADING...");
                        break;
                    }

                    case SUCCESS: {
                        Log.d(TAG, "onChanged: got photos...");
                        adapter.setPhotos(listResource.data);
                        break;
                    }

                    case ERROR: {
                        Log.e(TAG, "onChanged: ERROR..." + listResource.message);
                        adapter.setPhotos(listResource.data);
                        break;
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.searchNextPage();
                }
            }
        });
    }
}
























