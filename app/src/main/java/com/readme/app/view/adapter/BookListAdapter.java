package com.readme.app.view.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.readme.app.R;
import com.readme.app.model.entity.Book;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtAuthor;
        private final ImageView coverImageView;
        private final TextView txtReadPages;

        private ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.textview_movie_title);
            txtAuthor = itemView.findViewById(R.id.list);
            coverImageView = itemView.findViewById(R.id.imageview_series_cover);
            txtReadPages = itemView.findViewById(R.id.list_book_txtReadPages);
        }
    }

    private OnBookClickListener onBookClickListener;

    private final LayoutInflater mInflater;
    private List<Book> mBooks;

    public BookListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.book_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(view -> onBookClickListener.OnBookClicked(getBook(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mBooks != null) {
            Book current = mBooks.get(position);
            holder.txtTitle.setText(current.getTitle());
            holder.txtAuthor.setText(current.getAuthor());
            if(current.getCurrentPage() == null || current.getCurrentPage() == 0) {
                holder.txtReadPages.setText(mInflater.getContext().getString(R.string.list_book_txtReadPagesNotStarted));
            } else {
                holder.txtReadPages.setText(mInflater.getContext().getString(R.string.list_book_txtReadPages, current.getCurrentPage(), current.getPages()));
            }
            if(current.getCover() != null) {
                holder.coverImageView.setImageBitmap(current.getCover());
            }
        } else {
            // Covers the case of data not being ready yet.
        }
    }

    public void setBooks(List<Book> books){
        mBooks = books;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mBooks != null)
            return mBooks.size();
        else return 0;
    }

    public Book getBook(final int position) {
        return mBooks.get(position);
    }

    public interface OnBookClickListener {
        void OnBookClicked(Book book);
    }

    public void setOnBookClickListener(OnBookClickListener onBookClickListener) {
        this.onBookClickListener = onBookClickListener;
    }
}
