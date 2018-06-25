package com.readme.app.ui;

import com.readme.app.viewmodel.BookListViewModel;
import com.readme.app.R;
import com.readme.app.ui.adapter.BookListAdapter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int UPDATE_USER_DETAILS_REQUEST = 1;
    public static final int UPDATE_BOOK_LIST_REQUEST = 2;

    private TextView txtName;
    private TextView txtEmail;

    private RecyclerView mRecyclerView;
    private BookListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private BookListViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_add_book);
        fab.setOnClickListener(view -> {
            startBookEditActivity(-1);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView =  navigationView.getHeaderView(0);

        txtName = headerView.findViewById(R.id.nav_header_main_txtName);
        txtEmail = headerView.findViewById(R.id.nav_header_main_txtEmail);

        mAdapter = new BookListAdapter(this);
        mAdapter.setOnBookClickListener(book -> startBookEditActivity(book.getId()));

        mRecyclerView = findViewById(R.id.main_listViewBooks);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        model = ViewModelProviders.of(this).get(BookListViewModel.class);
        model.init();
        model.getAllBooks().observe(this, books -> {
            mAdapter.setBooks(books);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showResults(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                /*Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, UPDATE_USER_DETAILS_REQUEST);*/
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }

    private void showResults (String query) {

    }

    private void startBookEditActivity(final int id) {
        Intent intent = new Intent(MainActivity.this, BookEditActivity.class);
        intent.putExtra(BookEditActivity.ID_KEY, id);
        startActivity(intent);
    }

}
