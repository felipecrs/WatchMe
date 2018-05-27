package com.readme.app.view;

import com.readme.app.Control.SessionManager;
import com.readme.app.R;
import com.readme.app.adapter.BookAdapter;
import com.readme.app.dao.BookDAO;
import com.readme.app.model.Book;
import com.readme.app.util.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_add_book);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookEditActivity.class);
                startActivityForResult(intent, UPDATE_BOOK_LIST_REQUEST);
            }
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

        if (sessionManager.isLoggedIn()) {
            bookDAO = new BookDAO(this);
            bookAdapter = new BookAdapter(this, bookDAO.listByUser(sessionManager.getUserId()));

            listViewBooks.setAdapter(bookAdapter);

            updateUserDetails();
        }
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
        bookAdapter.refreshBooks(bookDAO.listByUser(sessionManager.getUserId()));
    }
}
