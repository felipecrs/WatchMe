package com.readme.app.control;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.readme.app.R;
import com.readme.app.model.dao.UserDAO;
import com.readme.app.model.User;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validation;

public class UserEditActivity extends AppCompatActivity {

    private EditText nameEdit, emailEdit, passwordEdit;
    private TextView idText;

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

        idText = findViewById(R.id.user_edit_id);
        nameEdit = findViewById(R.id.user_edit_name);
        emailEdit = findViewById(R.id.user_edit_email);
        passwordEdit = findViewById(R.id.user_edit_password);

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
        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError(getString(R.string.error_field_required));
            focusView = passwordEdit;
            cancel = true;
        } else if (!Validation.isPasswordValid(password)) {
            passwordEdit.setError(getString(R.string.error_invalid_password));
            focusView = passwordEdit;
            cancel = true;
        }

        User emailFound = userDAO.findByEmail(email);
        if (emailFound != null && !userToEdit.getId().equals(emailFound.getId())) {
            emailEdit.setError(getString(R.string.error_email_already_exist));
            focusView = emailEdit;
            cancel = true;
        } else if (email.isEmpty()){
            emailEdit.setError(getString(R.string.error_field_required));
            focusView = emailEdit;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            emailEdit.setError(getString(R.string.error_email_invalid));
            focusView = emailEdit;
            cancel = true;
        }

        if (!name.isEmpty() && !Validation.isNameValid(name)) {
            nameEdit.setError(getString(R.string.error_invalid_name));
            focusView = nameEdit;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
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
    }

    private void delete () {
        userDAO.delete(userToEdit.getId());
        Message.show(this, getString(R.string.message_user_deleted));
        sessionManager.logout(this);
    }

    private void loadFields() {
        idText.setText(userToEdit.getId().toString());
        nameEdit.setText(userToEdit.getName());
        emailEdit.setText(userToEdit.getEmail());
        passwordEdit.setText(userToEdit.getPassword());
    }
}
