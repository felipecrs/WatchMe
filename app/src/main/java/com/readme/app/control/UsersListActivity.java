package com.readme.app.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.readme.app.R;
import com.readme.app.model.adapter.UserAdapter;
import com.readme.app.model.dao.UserDAO;
import com.readme.app.model.User;

import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private ListView listView;
    private List<User> list;
    private UserAdapter adapter;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDAO = new UserDAO(this);
        list = userDAO.list();
        adapter = new UserAdapter(this, list);

        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
