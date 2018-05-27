package com.readme.app.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.readme.app.Control.SessionManager;
import com.readme.app.R;
import com.readme.app.dao.UserDAO;
import com.readme.app.model.User;
import com.readme.app.util.Message;
import com.readme.app.util.Validation;

public class LoginActivity extends AppCompatActivity {
    private UserDAO userDAO;

    private SessionManager sessionManager;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        sessionManager = new SessionManager(this);
        userDAO = new UserDAO(this);
    }

    private void attemptLogin() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!Validation.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (userDAO.searchByEmail(email) != null) {
                if (userDAO.isValidCredentials(email, password)) {
                    User user = userDAO.searchByEmail(email);
                    sessionManager.updateLoginSession(user.get_id(), user.getName(), user.getEmail());
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    mPasswordView.setError(getString(R.string.error_wrong_password));
                    mPasswordView.requestFocus();
                }
            } else {
                if (userDAO.save(new User(email, password)) != -1) {
                    User user = userDAO.searchByEmail(email);
                    sessionManager.updateLoginSession(user.get_id(), user.getName(), user.getEmail());
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    Message.show(LoginActivity.this, getString(R.string.message_user_registered));
                } else {
                    Message.show(LoginActivity.this, getString(R.string.message_error_database));
                }
            }
        }
    }
}

