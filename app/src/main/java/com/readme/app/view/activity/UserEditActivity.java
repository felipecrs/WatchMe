package com.readme.app.view.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.readme.app.R;
import com.readme.app.model.entity.User;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.UserDao;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserEditActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private SessionManager sessionManager;
    private UserDao userDao;

    private User userToEdit;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        userDao = AppDatabase.getInstance(this).getUserDao();
        sessionManager = SessionManager.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameEditText = findViewById(R.id.user_edit_name);
        emailEditText = findViewById(R.id.user_edit_email);
        passwordEditText = findViewById(R.id.user_edit_password);

        // Receiving data from parent activity
        Integer userIdToEdit = getIntent().getIntExtra(getString(R.string.intent_extra_user_id), -1);
        String emailAdressFromIntent = getIntent().getStringExtra("user_email");
        String passwordFromIntent = getIntent().getStringExtra("user_password");

        editing = userIdToEdit != -1;

        // Editing
        if (editing) {
            userToEdit = userDao.getById(userIdToEdit);
        }
        // Adding
        else {
            userToEdit = new User();
            userToEdit.setEmail(emailAdressFromIntent);
            userToEdit.setPassword(passwordFromIntent);

            actionBar.setTitle(R.string.title_activity_user_add);
        }

        nameEditText.setText(userToEdit.getName());
        passwordEditText.setText(userToEdit.getPassword());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_common, menu);
        // Registering
        if (!editing) {
            MenuItem actionDelete = menu.findItem(R.id.action_delete);
            actionDelete.setEnabled(false);
            actionDelete.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            case R.id.action_delete:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save(){
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        } else if (!Validator.isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        User emailFound = userDao.getByEmail(email);
        if (emailFound != null && !userToEdit.getId().equals(emailFound.getId())) {
            emailEditText.setError(getString(R.string.error_email_already_exist));
            focusView = emailEditText;
            cancel = true;
        } else if (email.isEmpty()){
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!Validator.isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_email_invalid));
            focusView = emailEditText;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            EditText confirmPasswordEditText = new EditText(this);
            confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

            if(userToEdit.passwordMatch(password) || !editing) {
                // User did not changed password, must not confirm
                save(name,email,password);
            } else {
                // User changed password, must confirm
                Message.showConfirmation(this, getString(R.string.dialog_confirm_password_title), getString(R.string.dialog_confirm_password_description),
                        (dialogInterface, i) -> {
                            if(password.equals(confirmPasswordEditText.getText().toString())){
                                save(name,email,password);
                            } else {
                                Message.show(this, "Confirmation password don't match");
                            }
                        }, confirmPasswordEditText).show();
            }
        }
    }

    private void save (String name, String emailAdress, String password) {
        userToEdit.setName(name);
        userToEdit.setEmail(emailAdress);
        userToEdit.setPassword(password);

        userDao.save(userToEdit);
        // Editing
        if (editing) {
            Message.show(this, getString(R.string.message_user_updated));
        }
        // Adding
        else {
            Message.show(this, getString(R.string.message_user_registered));
        }
        finish();
    }

    private void delete () {
        Message.showConfirmation(this,
                getString(R.string.confirm_remove_user_title),
                getString(R.string.confirm_remove_user_description),
                (dialogInterface, i) -> {
                    userDao.delete(userToEdit);
                    Message.show(this, getString(R.string.message_user_deleted));
                    sessionManager.logout(this);
                }, null).show();
    }

}
