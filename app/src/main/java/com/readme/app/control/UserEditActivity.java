package com.readme.app.control;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.readme.app.R;
import com.readme.app.model.dao.UserDAO;
import com.readme.app.model.User;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validation;

public class UserEditActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;

    private SessionManager sessionManager;
    private UserDAO userDAO;

    private User userToEdit;
    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameEditText = findViewById(R.id.user_edit_name);
        emailEditText = findViewById(R.id.user_edit_email);
        passwordEditText = findViewById(R.id.user_edit_password);

        userDAO = new UserDAO(this);
        sessionManager = SessionManager.getInstance(this);

        // Receiving user ID from parent activity
        Integer userIdToEdit = getIntent().getIntExtra("user_id", -1);
        String userEmailToEdit = getIntent().getStringExtra("user_email");
        String userPasswordToEdit = getIntent().getStringExtra("user_password");
        editing = userIdToEdit != -1;

        // Editing
        if (editing) {
            userToEdit = userDAO.findById(userIdToEdit);
        }
        // Adding
        else {
            userToEdit = new User();
            userToEdit.setEmail(userEmailToEdit);
            userToEdit.setPassword(userPasswordToEdit);
            actionBar.setTitle(R.string.title_activity_user_add);
        }
        loadFields();
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        userDAO.close();
        super.onDestroy();
    }

    private void save(){
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        } else if (!Validation.isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        User emailFound = userDAO.findByEmail(email);
        if (emailFound != null && !userToEdit.getId().equals(emailFound.getId())) {
            emailEditText.setError(getString(R.string.error_email_already_exist));
            focusView = emailEditText;
            cancel = true;
        } else if (email.isEmpty()){
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_email_invalid));
            focusView = emailEditText;
            cancel = true;
        }

        if (!name.isEmpty() && !Validation.isNameValid(name)) {
            nameEditText.setError(getString(R.string.error_invalid_name));
            focusView = nameEditText;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            EditText confirmPasswordEditText = new EditText(this);
            confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

            if(userToEdit.passwordMatch(password)) {
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

    private void save (String name, String email, String password) {
        userToEdit.setName(name);
        userToEdit.setEmail(email);
        userToEdit.setPassword(password);
        if (userDAO.save(userToEdit) != -1) {
            // Editing
            if (editing) {
                sessionManager.updateLoginSession(userToEdit.getId(), userToEdit.getName(), userToEdit.getEmail());
                Message.show(this, getString(R.string.message_user_updated));
            }
            // Adding
            else {
                Message.show(this, getString(R.string.message_user_registered));
            }
            setResult(RESULT_OK);
            finish();
        } else {
            Message.show(this, getString(R.string.message_error_database));
        }
    }

    private void delete () {
        Message.showConfirmation(this,
                getString(R.string.confirm_remove_user_title),
                getString(R.string.confirm_remove_user_description),
                (dialogInterface, i) -> {
                    userDAO.delete(userToEdit.getId());
                    Message.show(this, getString(R.string.message_user_deleted));
                    sessionManager.logout(this);
                }, null).show();
    }

    private void loadFields () {
        nameEditText.setText(userToEdit.getName());
        emailEditText.setText(userToEdit.getEmail());
        passwordEditText.setText(userToEdit.getPassword());
    }
}
