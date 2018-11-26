package com.readme.app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.os.Bundle;

import com.readme.app.AndroidDatabaseManager;
import com.readme.app.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .commitNow();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SessionManager sessionManager;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            sessionManager = SessionManager.getInstance(getActivity());
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);

            Preference editProfileButton = findPreference(getString(R.string.pref_edit_profile_button_key));
            editProfileButton.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), UserEditActivity.class);
                intent.putExtra(getString(R.string.intent_extra_user_id), sessionManager.getUserId());
                startActivity(intent);
                return true;
            });

            Preference logoutButton = findPreference(getString(R.string.pref_logout_button_key));
            logoutButton.setOnPreferenceClickListener(preference -> {
                sessionManager.logout(getActivity());
                return true;
            });

            Preference databaseManagerPreference = findPreference(getString(R.string.preference_key_database_manager));
            databaseManagerPreference.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), AndroidDatabaseManager.class);
                startActivity(intent);
                return true;
            });
        }
    }
}
