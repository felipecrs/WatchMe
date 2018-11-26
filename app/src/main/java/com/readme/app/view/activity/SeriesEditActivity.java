package com.readme.app.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.readme.app.R;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validator;
import com.readme.app.viewmodel.MovieEditViewModel;
import com.readme.app.viewmodel.SeriesEditViewModel;
import com.readme.app.viewmodel.SeriesViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class SeriesEditActivity extends AppCompatActivity {

    public static final String ID_KEY = "id";

    private EditText titleEditText;

    private SeriesEditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.series_edit_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        titleEditText = findViewById(R.id.edittext_series_title);

        viewModel = ViewModelProviders.of(this).get(SeriesEditViewModel.class);
        viewModel.initialize(getIntent().getIntExtra(ID_KEY, -1));

        if(viewModel.isEditing()) {
            // Editing
            titleEditText.setText(viewModel.getOldItem().getTitle());
        } else {
            // Registering
            actionBar.setTitle(R.string.title_activity_series_add);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_common, menu);
        // Registering
        if (!viewModel.isEditing()) {
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
                break;
            case R.id.action_delete:
                delete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        String title = titleEditText.getText().toString();

        boolean cancel = false;

        View focusView = null;

        if (title.isEmpty()){
            titleEditText.setError(getString(R.string.error_field_required));
            focusView = titleEditText;
            cancel = true;
        } else if (!Validator.isTitleValid(title)) {
            titleEditText.setError(getString(R.string.error_title_invalid));
            focusView = titleEditText;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            viewModel.getNewItem().setTitle(title);

            if (viewModel.isEditing() && viewModel.getOldItem().equals(viewModel.getNewItem())) {
                Message.show(this, getString(R.string.message_no_changes));
            } else {
                viewModel.save();

                // Editing
                if(viewModel.isEditing()) {
                    Message.show(this, getString(R.string.message_movie_edited));
                }
                // Registering
                else {
                    Message.show(this, getString(R.string.message_movie_added));
                }
                finish();
            }
        }
    }

    private void delete() {
        viewModel.delete();
        Message.show(this, getString(R.string.message_series_deleted));
        finish();
    }
}
