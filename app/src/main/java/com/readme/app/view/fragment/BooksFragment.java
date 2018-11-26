package com.readme.app.view.fragment;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.readme.app.R;
import com.readme.app.view.activity.BookEditActivity;
import com.readme.app.view.activity.MainActivity;
import com.readme.app.view.adapter.BookListAdapter;
import com.readme.app.viewmodel.BooksViewModel;

public class BooksFragment extends Fragment {

    private BooksViewModel viewModel;
    private BookListAdapter adapter;

    public static BooksFragment newInstance() {
        return new BooksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.books_fragment, container, false);
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
        adapter.setBooks(viewModel.search(newText));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(BooksViewModel.class);

        adapter = new BookListAdapter(getActivity());
        adapter.setOnBookClickListener(book -> {
            Intent intent = new Intent(getActivity(), BookEditActivity.class);
            intent.putExtra(BookEditActivity.ID_KEY,book.getId());
            startActivity(intent);
        });
        setupItems();

        RecyclerView mRecyclerView = getView().findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupItems() {
        viewModel.getItems().observe(this, books -> {
            adapter.setBooks(books);
        });
    }
}
