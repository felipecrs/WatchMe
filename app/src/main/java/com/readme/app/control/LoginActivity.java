package com.readme.app.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.readme.app.R;
import com.readme.app.model.dao.UserDAO;
import com.readme.app.model.User;
import com.readme.app.model.util.Validation;

public class LoginActivity extends AppCompatActivity {
    private UserDAO userDAO;

    private SessionManager sessionManager;

    private EditText emailEdit;
    private EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setting up action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.title_activity_login));

        emailEdit = findViewById(R.id.edit_email);
        passwordEdit = findViewById(R.id.edit_password);

        Button signInButton = findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(view -> signIn());
        Button signUpButton = findViewById(R.id.button_sign_up);
        signUpButton.setOnClickListener(view -> signUp());

        userDAO = new UserDAO(this);
        sessionManager = SessionManager.getInstance(this);
        if(sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void signUp() {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        Intent intent = new Intent(this, UserEditActivity.class);
        intent.putExtra("user_email", email);
        intent.putExtra("user_password", password);
        startActivity(intent);
    }

    private void signIn() {

        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError(getString(R.string.error_field_required));
            focusView = passwordEdit;
            cancel = true;
        } else if (!Validation.isPasswordValid(password)) {
            passwordEdit.setError(getString(R.string.error_invalid_password));
            focusView = passwordEdit;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError(getString(R.string.error_field_required));
            focusView = emailEdit;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            emailEdit.setError(getString(R.string.error_email_invalid));
            focusView = emailEdit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            User user = userDAO.findByEmail(email);
            if (user != null) {
                // User found
                if (user.passwordMatch(password)) {
                    // Password match
                    sessionManager.updateLoginSession(user.getId(), user.getName(), user.getEmail());
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    // Password do not match
                    passwordEdit.setError(getString(R.string.error_wrong_password));
                    passwordEdit.requestFocus();
                }
            } else {
                // User not found
                emailEdit.setError(getString(R.string.error_email_user_not_found));
                emailEdit.requestFocus();
            }
        }
    }
}

