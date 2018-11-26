package com.readme.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.readme.app.R;
import com.readme.app.view.activity.BookEditActivity;
import com.readme.app.view.activity.MovieEditActivity;
import com.readme.app.view.activity.SeriesEditActivity;
import com.readme.app.view.adapter.SeriesAdapter;
import com.readme.app.viewmodel.SeriesViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SeriesFragment extends Fragment {

    private SeriesViewModel viewModel;
    private SeriesAdapter adapter;

    public static SeriesFragment newInstance() {
        return new SeriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.series_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SeriesViewModel.class);

        adapter = new SeriesAdapter(getActivity(), item -> {
            Intent intent = new Intent(getActivity(), SeriesEditActivity.class);
            intent.putExtra(SeriesEditActivity.ID_KEY, item.getId());
            startActivity(intent);
        });
        setupItems();

        RecyclerView mRecyclerView = getView().findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                showResults("");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                setupItems();
                return true;
            }
        });
        SearchView searchView = (SearchView)searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showResults(newText);
                return true;
            }
        });
    }

    private void showResults(String newText) {
        adapter.setItems(viewModel.getSearchResults(newText));
    }

    private void setupItems() {
        viewModel.getItems().observe(this, items -> {
            adapter.setItems(items);
        });
    }
}
