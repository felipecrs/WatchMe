package com.readme.app.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.readme.app.R;
import com.readme.app.model.Book;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private Context context;
    private List<Book> books;

    public BookAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.list_book,parent,false);

        Book book = books.get(position);

        TextView txtTitle = view.findViewById(R.id.list_book_txtTitle);
        txtTitle.setText(book.getTitle());

        TextView txtAuthor = view.findViewById(R.id.list);
        txtAuthor.setText(book.getAuthor());

        TextView txtReadPages = view.findViewById(R.id.list_book_txtReadPages);
        if(book.getActualPage() > 0) {
            txtReadPages.setText(context.getString(R.string.list_book_txtReadPages)+" "+book.getActualPage()+"/"+book.getTotalPages());
        } else {
            txtReadPages.setText("");
        }

        return view;
    }

    public void refreshBooks(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
        notifyDataSetChanged();
    }
}
