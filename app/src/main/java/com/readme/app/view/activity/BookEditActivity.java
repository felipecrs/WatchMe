package com.readme.app.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.readme.app.model.entity.Book;
import com.readme.app.viewmodel.BookEditViewModel;
import com.readme.app.R;
import com.readme.app.model.util.Converter;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validator;

public class BookEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 0;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST = 1;

    public static final String ID_KEY = "id";

    private EditText titleEditText, authorEditText, totalPagesEditText, actualPageEditText;
    private ImageView coverImageView;

    private BookEditViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        coverImageView = findViewById(R.id.cover_image);
        coverImageView.setOnClickListener(view -> checkPermissionAndStartImagePicker());

        titleEditText = findViewById(R.id.edittext_movie_title);
        authorEditText = findViewById(R.id.book_edit_edt_author);
        totalPagesEditText = findViewById(R.id.book_edit_edt_total_pages);
        actualPageEditText = findViewById(R.id.book_edit_edtActualPage);

        viewModel = ViewModelProviders.of(this).get(BookEditViewModel.class);
        viewModel.init(getIntent().getIntExtra(ID_KEY, -1));

        if(viewModel.isEditing()) {
            // Editing a book
            Book book = viewModel.getOldItem();
            titleEditText.setText(book.getTitle());
            authorEditText.setText(book.getAuthor());
            totalPagesEditText.setText(book.getPages().toString());
            if(book.getCurrentPage() != null) {
                actualPageEditText.setText(book.getCurrentPage().toString());
            }
            if(book.getCover() != null) {
                coverImageView.setImageBitmap(book.getCover());
            }
        } else {
            // Registering a book
            actionBar.setTitle(R.string.title_activity_book_add);
        }

        sessionManager = SessionManager.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_common, menu);
        // Registering a book
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap selectedImage = Converter.decodeSampledBitmapFromFile(filePath, Converter.DEFAULT_BOOK_IMAGE_WIDTH, Converter.DEFAULT_BOOK_IMAGE_HEIGHT);

                    coverImageView.setImageBitmap(selectedImage);
                    viewModel.getNewItem().setCover(selectedImage);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    startImagePicker();
                } else {
                    Message.show(this, getString(R.string.message_permission_denied));
                }
            }
        }
    }

    private void checkPermissionAndStartImagePicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Don't have permission to read files, requesting
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE_REQUEST);
        } else {
            startImagePicker();
        }
    }

    private void startImagePicker(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void save() {
        String title = titleEditText.getText().toString();
        String author = authorEditText.getText().toString();
        Integer totalPages = null;
        Integer actualPage = null;
        try {
            totalPages = Integer.parseInt(totalPagesEditText.getText().toString());
            actualPage = Integer.parseInt(actualPageEditText.getText().toString());
        } catch (NumberFormatException ignored) {
        }

        boolean cancel = false;
        View focusView = null;

        if (actualPage != null && !Validator.isActualPageValid(actualPage, totalPages)) {
            actualPageEditText.setError(getString(R.string.error_invalid_pages_quantity));
            focusView = actualPageEditText;
            cancel = true;
        }

        if (totalPages == null){
            totalPagesEditText.setError(getString(R.string.error_field_required));
            focusView = totalPagesEditText;
            cancel = true;
        } else if (!Validator.isTotalPagesValid(totalPages)) {
            totalPagesEditText.setError(getString(R.string.error_invalid_pages_quantity));
            focusView = totalPagesEditText;
            cancel = true;
        }

        if (author.isEmpty()){
            authorEditText.setError(getString(R.string.error_field_required));
            focusView = authorEditText;
            cancel = true;
        } else if (!Validator.isNameValid(author)) {
            authorEditText.setError(getString(R.string.error_author_invalid));
            focusView = authorEditText;
            cancel = true;
        }

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
            if(viewModel.isEditing()) {
                viewModel.getNewItem().setId(viewModel.getOldItem().getId());
                viewModel.getNewItem().setUserId(viewModel.getOldItem().getUserId());
            } else {
                viewModel.getNewItem().setUserId(sessionManager.getUserId());
            }
            viewModel.getNewItem().setTitle(title);
            viewModel.getNewItem().setAuthor(author);
            viewModel.getNewItem().setPages(totalPages);
            viewModel.getNewItem().setCurrentPage(actualPage);
            // viewModel.getNewItem().setCover() is called when image was picked to preserve original Bitmap
            //viewModel.getNewItem().setCover(Converter.drawableToBitmap(coverImageView.getDrawable()));

            viewModel.save();

            // Editing
            if(viewModel.isEditing()) {
                Message.show(this, getString(R.string.message_book_edited));
            }
            // Registering
            else {
                Message.show(this, getString(R.string.message_book_added));
            }
            finish();
        }
    }

    private void delete() {
        viewModel.delete();
        finish();
    }
}
