package com.readme.app.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.readme.app.control.SessionManager;
import com.readme.app.R;
import com.readme.app.model.dao.BookDAO;
import com.readme.app.model.Book;
import com.readme.app.model.util.Message;
import com.readme.app.model.util.Validation;

public class BookEditActivity extends AppCompatActivity {

    EditText edtTitle;
    EditText edtAuthor;
    EditText edtTotalPages;
    EditText edtActualPage;

    Book book;
    BookDAO bookDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtTitle = findViewById(R.id.book_edit_edt_title);
        edtAuthor = findViewById(R.id.book_edit_edt_author);
        edtTotalPages = findViewById(R.id.book_edit_edt_total_pages);
        edtActualPage = findViewById(R.id.book_edit_edtActualPage);

        // Receiving book ID from parent activity
        Integer id = null;
        try{
            id = Integer.parseInt(getIntent().getExtras().getString(getString(R.string.book_id_extra_key)));
        } catch (Exception e) {
        }

        bookDAO = new BookDAO(this);

        // Editing
        if (id != null) {
            book = bookDAO.searchByID(id);
            loadFieldsWith(book);
        }
        // Adding
        else {
            getSupportActionBar().setTitle(R.string.title_activity_book_add);
            // TODO hide delete action

            book = new Book();
            book.setUser_id(new SessionManager(this).getUserId());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
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
    protected void onDestroy() {
        bookDAO.close();
        super.onDestroy();
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

        if (actualPage != null && !Validation.isTotalPagesValid(totalPages)) {
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
            book.setTitle(title);
            book.setAuthor(author);
            book.setTotalPages(totalPages);
            book.setActualPage(actualPage);

            if (bookDAO.save(book) != -1) {
                Message.show(this, getString(R.string.message_book_added));
                setResult(RESULT_OK);
                finish();
            } else {
                Message.show(this, getString(R.string.message_error_database));
            }
        }
    }

    private void delete () {
        bookDAO.delete(book.get_id());
    }

    private void loadFieldsWith(Book book) {
        edtTitle.setText(book.getTitle());
        edtAuthor.setText(book.getAuthor());
        edtTotalPages.setText(book.getTotalPages().toString());
        edtActualPage.setText(book.getActualPage().toString());
    }
}
