package com.readme.app.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.readme.app.R;
import com.readme.app.model.Book;
import com.readme.app.model.User;
import com.readme.app.model.adapter.BookAdapter;
import com.readme.app.model.adapter.UserAdapter;
import com.readme.app.model.dao.BookDAO;
import com.readme.app.model.dao.UserDAO;

import java.util.List;

public class BooksListActivity extends AppCompatActivity {

    private ListView listView;
    private BookAdapter adapter;
    private BookDAO bookDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookDAO = new BookDAO(this);
        List<Book> booksList = bookDAO.list();
        adapter = new BookAdapter(this, booksList);

        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
