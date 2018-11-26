package com.readme.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.readme.app.R;
import com.readme.app.model.entity.User;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.UserDao;
import com.readme.app.model.util.Validator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {
    private UserDao userRepository;

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
        passwordEdit = findViewById(R.id.edit_text_password);

        Button signInButton = findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(view -> signIn());
        Button signUpButton = findViewById(R.id.button_sign_up);
        signUpButton.setOnClickListener(view -> signUp());

        userRepository = AppDatabase.getInstance(this).getUserDao();
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
        } else if (!Validator.isPasswordValid(password)) {
            passwordEdit.setError(getString(R.string.error_invalid_password));
            focusView = passwordEdit;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError(getString(R.string.error_field_required));
            focusView = emailEdit;
            cancel = true;
        } else if (!Validator.isEmailValid(email)) {
            emailEdit.setError(getString(R.string.error_email_invalid));
            focusView = emailEdit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            User user = userRepository.getByEmail(email);
            if (user != null) {
                // User found
                if (user.passwordMatch(password)) {
                    // Password match
                    sessionManager.login(user.getId(), this);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
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

