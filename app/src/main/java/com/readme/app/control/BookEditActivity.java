package com.readme.app.control;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.readme.app.R;
import com.readme.app.model.dao.BookDAO;
import com.readme.app.model.Book;
import com.readme.app.model.util.Converter;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validation;

public class BookEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 0;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST = 1;
    private EditText edtTitle, edtAuthor, edtTotalPages, edtActualPage;
    private ImageView coverImage;

    private BookDAO bookDAO;
    private Book bookToEdit;

    private boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        coverImage = findViewById(R.id.cover_image);
        coverImage.setOnClickListener(view -> {
            checkPermissionAndStartImagePicker();
        });

        edtTitle = findViewById(R.id.book_edit_edt_title);
        edtAuthor = findViewById(R.id.book_edit_edt_author);
        edtTotalPages = findViewById(R.id.book_edit_edt_total_pages);
        edtActualPage = findViewById(R.id.book_edit_edtActualPage);

        bookDAO = new BookDAO(this);

        // Receiving bookToEdit ID from parent activity
        Integer bookIdToEdit = getIntent().getIntExtra("book_id", -1);
        Integer bookUserIdToEdit = getIntent().getIntExtra("user_id", -1);
        editing = bookIdToEdit != -1;

        // Editing
        if (editing) {
            bookToEdit = bookDAO.findById(bookIdToEdit);
        }
        // Adding
        else {
            bookToEdit = new Book(bookUserIdToEdit);
            actionBar.setTitle(R.string.title_activity_book_add);
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
                break;
            case R.id.action_delete:
                delete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        bookDAO.close();
        super.onDestroy();
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

                    bookToEdit.setImage(selectedImage);
                    loadFields();
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

    private void save(){
        String title = edtTitle.getText().toString();
        String author = edtAuthor.getText().toString();
        Integer totalPages = null;
        Integer actualPage = null;
        try {
            totalPages = Integer.parseInt(edtTotalPages.getText().toString());
            actualPage = Integer.parseInt(edtActualPage.getText().toString());
        } catch (NumberFormatException e) {
        }

        boolean cancel = false;
        View focusView = null;

        if (actualPage != null && !Validation.isActualPageValid(actualPage, totalPages)) {
            edtActualPage.setError(getString(R.string.error_invalid_pages_quantity));
            focusView = edtActualPage;
            cancel = true;
        }

        if (totalPages == null){
            edtTotalPages.setError(getString(R.string.error_field_required));
            focusView = edtTotalPages;
            cancel = true;
        } else if (!Validation.isTotalPagesValid(totalPages)) {
            edtTotalPages.setError(getString(R.string.error_invalid_pages_quantity));
            focusView = edtTotalPages;
            cancel = true;
        }

        if (author.isEmpty()){
            edtAuthor.setError(getString(R.string.error_field_required));
            focusView = edtAuthor;
            cancel = true;
        } else if (!Validation.isNameValid(author)) {
            edtAuthor.setError(getString(R.string.error_author_invalid));
            focusView = edtAuthor;
            cancel = true;
        }

        if (title.isEmpty()){
            edtTitle.setError(getString(R.string.error_field_required));
            focusView = edtTitle;
            cancel = true;
        } else if (!Validation.isTitleValid(title)) {
            edtTitle.setError(getString(R.string.error_title_invalid));
            focusView = edtTitle;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            bookToEdit.setTitle(title);
            bookToEdit.setAuthor(author);
            bookToEdit.setTotalPages(totalPages);
            bookToEdit.setActualPage(actualPage);

            if (bookDAO.save(bookToEdit) != -1) {
                // Editing
                if (editing) {
                    Message.show(this, getString(R.string.message_book_edited));
                }
                // Adding
                else {
                    Message.show(this, getString(R.string.message_book_added));
                }
                setResult(RESULT_OK);
                finish();
                System.out.println("FECHOU BOOKEDIT");
            } else {
                Message.show(this, getString(R.string.message_error_database));
            }
        }
    }

    private void delete () {
        bookDAO.delete(bookToEdit.getId());
        setResult(RESULT_OK);
        finish();
    }

    private void loadFields() {
        edtTitle.setText(bookToEdit.getTitle());
        edtAuthor.setText(bookToEdit.getAuthor());
        Integer totalPages = bookToEdit.getTotalPages();
        if(totalPages != null) {
            edtTotalPages.setText(totalPages.toString());
        }
        Integer actualPage = bookToEdit.getActualPage();
        if(actualPage != null) {
            edtActualPage.setText(actualPage.toString());
        }
        if(bookToEdit.getImage() != null) {
            coverImage.setImageBitmap(bookToEdit.getImage());
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
}
