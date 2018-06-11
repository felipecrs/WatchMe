package com.readme.app.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.readme.app.R;
import com.readme.app.model.Book;
import com.readme.app.model.dao.UserDAO;
import com.readme.app.model.User;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validation;

public class UserEditActivity extends AppCompatActivity {

    private EditText edtName, edtEmail;
    private TextView txtId;

    private SessionManager sessionManager;
    private UserDAO userDAO;
    private User userToEdit;

    private Integer userIdToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtId = findViewById(R.id.user_edit_txtId);
        edtName = findViewById(R.id.user_edit_edtName);
        edtEmail = findViewById(R.id.user_edit_edtEmail);

        userDAO = new UserDAO(this);
        sessionManager = new SessionManager(this);

        // Receiving userToEdit ID from parent activity
        userIdToEdit = getIntent().getIntExtra("user_id", -1);

        // Editing
        if (userIdToEdit != -1) {
            userToEdit = userDAO.searchByID(sessionManager.getUserId());
            loadFieldsWith(userToEdit);
        }
        // Adding
        else {
            userToEdit = new User();
            getSupportActionBar().setTitle(R.string.title_activity_user_add);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        // Editing
        if (userIdToEdit != -1) {
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
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (email.isEmpty()){
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            focusView = edtEmail;
            cancel = true;
        }

        if (name.isEmpty()){
            edtName.setError(getString(R.string.error_field_required));
            focusView = edtName;
            cancel = true;
        } else if (!Validation.isNameValid(name)) {
            edtName.setError(getString(R.string.error_invalid_name));
            focusView = edtName;
            cancel = true;
        }

        User emailFound = userDAO.searchByEmail(email);

        if (emailFound != null && !emailFound.get_id().equals(userToEdit.get_id())) {
            edtEmail.setError(getString(R.string.error_email_already_exist));
            focusView = edtEmail;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            userToEdit.setName(name);
            userToEdit.setEmail(email);

            if (userDAO.save(userToEdit) != -1) {
                // Editing
                if (userIdToEdit != 1) {
                    sessionManager.updateLoginSession(userToEdit.get_id(), userToEdit.getName(), userToEdit.getEmail());
                    Message.show(this, getString(R.string.message_user_updated));
                }
                // Adding
                else {
                    Message.show(this, getString(R.string.message_book_added));
                }
                setResult(RESULT_OK);
                finish();
            } else {
                Message.show(this, getString(R.string.message_error_database));
            }
        }
    }

    private void delete () {
        userDAO.delete(userToEdit.get_id());
        sessionManager.logout();
    }

    private void loadFieldsWith(User user) {
        txtId.setText(user.get_id().toString());
        edtName.setText(user.getName());
        edtEmail.setText(user.getEmail());
    }
}
