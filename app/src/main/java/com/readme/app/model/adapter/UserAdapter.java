package com.readme.app.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.readme.app.R;
import com.readme.app.model.dao.UserDAO;
import com.readme.app.model.User;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private UserDAO userDAO;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = users.get(position);

        if( convertView == null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_user, parent, false);
        }

        TextView txtName = convertView.findViewById(R.id.list_users_txtName);
        txtName.setText(user.getEmail());

        return convertView;
    }
}
