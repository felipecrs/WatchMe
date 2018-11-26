package com.readme.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readme.app.R;
import com.readme.app.model.entity.Series;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        private ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textview_series_title);
        }
    }

    private OnItemClickListener onItemClickListener;
    private final LayoutInflater mInflater;
    private List<Series> items;

    public SeriesAdapter(Context context, OnItemClickListener onItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.series_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(view -> onItemClickListener.OnItemClicked(getItem(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (items != null) {
            Series current = items.get(position);
            holder.titleTextView.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
        }
    }

    public void setItems(List<Series> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else return 0;
    }

    public Series getItem(final int position) {
        if (items != null)
            return items.get(position);
        else return null;
    }

    public interface OnItemClickListener {
        void OnItemClicked(Series item);
    }
}
