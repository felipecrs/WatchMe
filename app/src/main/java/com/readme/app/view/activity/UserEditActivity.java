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
import com.readme.app.model.entity.Email;
import com.readme.app.model.entity.User;
import com.readme.app.model.database.AppDatabase;
import com.readme.app.model.database.dao.UserDao;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validator;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class UserEditActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText passwordEditText;
    private Spinner emailSpinner;

    private SessionManager sessionManager;
    private UserDao userDao;

    private User userToEdit;
    private MutableLiveData<List<Email>> emailsToEdit;
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
        passwordEditText = findViewById(R.id.user_edit_password);
        emailSpinner = findViewById(R.id.user_spinner_email);
        ImageButton addEmailImageButton = findViewById(R.id.imageview_add_email);
        ImageButton removeEmailImageButton = findViewById(R.id.imageview_remove_email);

        // Receiving data from parent activity
        Integer userIdToEdit = getIntent().getIntExtra(getString(R.string.intent_extra_user_id), -1);
        String userEmailToEdit = getIntent().getStringExtra("user_email");
        String userPasswordToEdit = getIntent().getStringExtra("user_password");

        editing = userIdToEdit != -1;

        ArrayAdapter<Email> emailAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item);
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        emailsToEdit = new MediatorLiveData<>();
        emailsToEdit.setValue(new ArrayList<>());

        emailsToEdit.observe( this, emails -> {
            emailAdapter.clear();
            if(emails.size() <= 1) {
                removeEmailImageButton.setEnabled(false);
            } else {
                removeEmailImageButton.setEnabled(true);
            }
            emailAdapter.addAll(emails);
            //emailAdapter.notifyDataSetChanged();
        });
        emailSpinner.setAdapter(emailAdapter);

        addEmailImageButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New email");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            builder.setView(input);

            builder.setPositiveButton("Add", (dialog, which) -> {

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                String emailAddress = input.getText().toString();
                if (Validator.isEmailValid(emailAddress)) {
                    Email newEmail = new Email(emailAddress, userToEdit.getId());
                    Email emailFound = userDao.getEmail(emailAddress);
                    if ((emailFound == null || emailFound.getUserId().equals(userToEdit.getId())) && !emailsToEdit.getValue().contains(newEmail)) {
                        addToEmailsToEdit(newEmail);
                        dialog.dismiss();
                    } else {
                        input.setError(getString(R.string.error_email_already_exist));
                    }
                } else {
                    input.setError(getString(R.string.error_email_invalid));
                    input.requestFocus();
                }
            });
        });
        removeEmailImageButton.setOnClickListener(v -> {
            removeFromEmailsToEdit((Email)emailSpinner.getSelectedItem());
        });

        // Editing
        if (editing) {
            userToEdit = userDao.getById(userIdToEdit);
            List<Email> emailsToEditList = new ArrayList<>(userDao.getEmailsByUserId(userToEdit.getId()));
            for (int i = 0; i < emailsToEditList.size(); i++) {
                Email email = emailsToEditList.get(i);
                if(email.getAddress().equals(userToEdit.getEmailAddress())) {
                    emailsToEditList.remove(email);
                    emailsToEditList.add(0,email);
                }
            }
            emailsToEdit.setValue(emailsToEditList);
        }
        // Adding
        else {
            userToEdit = new User();
            userToEdit.setEmailAddress(userEmailToEdit);
            userToEdit.setPassword(userPasswordToEdit);
            if(Validator.isEmailValid(userEmailToEdit)) addToEmailsToEdit(new Email(userEmailToEdit, userIdToEdit));
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

    private void save(){
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailSpinner.getSelectedItem().toString();

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
            Message.show(this, getString(R.string.error_email_already_exist));
            cancel = true;
        } else if (TextUtils.isEmpty(email)){
            Message.show(this, getString(R.string.error_field_required));
            cancel = true;
        }

        if (!name.isEmpty() && !Validator.isNameValid(name)) {
            nameEditText.setError(getString(R.string.error_invalid_name));
            focusView = nameEditText;
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

    private void save (String name, String email, String password) {
        userToEdit.setName(name);
        userToEdit.setEmailAddress(email);
        userToEdit.setPassword(password);

        userDao.saveUserWithEmails(userToEdit, emailsToEdit.getValue());
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

    private void loadFields () {
        nameEditText.setText(userToEdit.getName());
        passwordEditText.setText(userToEdit.getPassword());
    }

    private void addToEmailsToEdit(Email email) {
        emailsToEdit.getValue().add(email);
        emailsToEdit.setValue(emailsToEdit.getValue());
    }

    private void removeFromEmailsToEdit(Email email) {
        emailsToEdit.getValue().remove(email);
        emailsToEdit.setValue(emailsToEdit.getValue());
    }

}
