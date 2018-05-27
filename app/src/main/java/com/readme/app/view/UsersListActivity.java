package com.readme.app.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.readme.app.R;
import com.readme.app.adapter.UserAdapter;
import com.readme.app.dao.UserDAO;
import com.readme.app.model.User;

import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private ListView lvUsers;
    private List<User> userList;
    private UserAdapter userAdapter;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDAO = new UserDAO(this);
        userList = userDAO.list();
        userAdapter = new UserAdapter(this,userList);

        lvUsers = findViewById(R.id.user_list_lvUsers);
        lvUsers.setAdapter(userAdapter);
    }
}
