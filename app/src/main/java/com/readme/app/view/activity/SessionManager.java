package com.readme.app.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public final class SessionManager {

    private static SessionManager instance = null;

    private SharedPreferences preferences;
    private Context applicationContext;

    private static final int PRIVATE_MODE = 0;
    private static final String PREFERENCES_NAME = "login_settings";
    private static final String KEY_USER_ID = "user_id";

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    private SessionManager(Context context) {
        applicationContext = context.getApplicationContext();
        preferences = applicationContext.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
    }

    public void login(Integer userId, Activity fromActivity) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(KEY_USER_ID, userId);

        editor.apply();

        fromActivity.finish();

        Intent intent = new Intent(fromActivity, MainActivity.class);
        fromActivity.startActivity(intent);
    }

    public Integer getUserId(){
        return preferences.getInt(KEY_USER_ID, -1);
    }

    public void logout(Activity fromActivity){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(fromActivity, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fromActivity.finishAffinity();

        fromActivity.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return preferences.getInt(KEY_USER_ID, -1) != -1;
    }

}
