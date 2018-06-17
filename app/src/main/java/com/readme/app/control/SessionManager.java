package com.readme.app.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public final class SessionManager {

    private static SessionManager instance = null;

    private SharedPreferences preferences;
    private Context context;

    private static final int PRIVATE_MODE = 0;
    private static final String PREFERENCES_NAME = "login_settings";
    private static final String KEY_LOGGED_IN = "loggedIn";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    private SessionManager(Context context) {
        this.context = context.getApplicationContext();
        preferences = this.context.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
    }

    public void updateLoginSession(Integer _id, String name, String email) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, _id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL,email);

        editor.apply();
    }

    public Integer getUserId(){
        return preferences.getInt(KEY_USER_ID, -1);
    }

    public String getUserName(){
        return preferences.getString(KEY_USER_NAME, null);
    }

    public String getUserEmail(){
        return preferences.getString(KEY_USER_EMAIL, null);
    }

    public void logout(Activity activity){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        startLoginActivity(activity);
    }

    private void startLoginActivity(Activity activity){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.finishAffinity();

        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

}
