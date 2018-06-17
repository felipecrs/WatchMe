package com.readme.app.control;

import com.readme.app.R;
import com.readme.app.model.Book;
import com.readme.app.model.adapter.BookAdapter;
import com.readme.app.model.dao.BookDAO;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int UPDATE_USER_DETAILS_REQUEST = 1;
    public static final int UPDATE_BOOK_LIST_REQUEST = 2;

    TextView txtName;
    TextView txtEmail;
    ListView listViewBooks;
    BookDAO bookDAO;
    BookAdapter bookAdapter;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_add_book);
        fab.setOnClickListener(view -> {
            startBookEditActivity(null);
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
        listViewBooks = findViewById(R.id.main_listViewBooks);

        sessionManager = SessionManager.getInstance(this);

        bookDAO = new BookDAO(this);
        bookAdapter = new BookAdapter(this, bookDAO.listByUser(sessionManager.getUserId()));
        listViewBooks.setAdapter(bookAdapter);
        listViewBooks.setOnItemClickListener((parent, view, position, id) -> {
            Book book = (Book) parent.getItemAtPosition(position);
            startBookEditActivity(book.getId());
        });
        updateBookList();
        updateUserDetails();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UPDATE_USER_DETAILS_REQUEST:
                switch (resultCode) {
                    case RESULT_OK:
                        updateUserDetails();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
            case UPDATE_BOOK_LIST_REQUEST:
                switch (resultCode) {
                    case RESULT_OK:
                        updateBookList();
                        break;
                    case RESULT_CANCELED:
                        break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, UPDATE_USER_DETAILS_REQUEST);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUserDetails() {
        txtName.setText(sessionManager.getUserName());
        txtEmail.setText(sessionManager.getUserEmail());
    }

    private void updateBookList() {
        List<Book> books = bookDAO.listByUser(sessionManager.getUserId());
        bookAdapter.refreshBooks(books);
        bookDAO.close();
    }

    private void updateBookList(List<Book> books) {
        bookAdapter.refreshBooks(books);
        bookDAO.close();
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
        List<Book> books = bookDAO.getBookMatches(sessionManager.getUserId(), query);
        updateBookList(books);
    }

    private void startBookEditActivity(Integer id) {
        Intent intent = new Intent(MainActivity.this, BookEditActivity.class);
        intent.putExtra("user_id", sessionManager.getUserId());
        if (id != null) {
            // Start BookEditActivity for editing
            intent.putExtra("book_id", id);
        }
        startActivityForResult(intent, UPDATE_BOOK_LIST_REQUEST);
    }

}
