package com.readme.app.Control;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.readme.app.view.LoginActivity;

public class SessionManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    Context context;
    Activity activity;

    private static final int PRIVATE_MODE = 0;

    private static final String PREFERENCES_NAME = "settings";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public static final String KEY_USER_ID = "_id";

    public static final String KEY_USER_NAME = "name";

    public static final String KEY_USER_EMAIL = "email";

    public SessionManager(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        preferences = context.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void updateLoginSession(Integer _id, String name, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, _id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL,email);
        editor.commit();
    }

    public Integer getUserId(){
        Integer result = preferences.getInt(KEY_USER_ID, -1);
        if (result != -1) {
            return result;
        }
        return null;
    }

    public String getUserName(){
        return preferences.getString(KEY_USER_NAME, null);
    }

    public String getUserEmail(){
        return preferences.getString(KEY_USER_EMAIL, null);
    }

    public void logout(){
        editor.clear();
        editor.commit();
        startLoginActivity();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void checkLogin() {
        if (!isLoggedIn()) {
            startLoginActivity();
        }
    }

    private void startLoginActivity(){
        Intent intent = new Intent(context, LoginActivity.class);

        activity.finishAffinity();

        context.startActivity(intent);
    }
}
