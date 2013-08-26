package com.nor1.example;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by alexwilczewski on 8/26/13.
 */
public class SettingsActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPref;
    private EditTextPreference mHostPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment sf = new SettingsFragment();
        // Display fragment as main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, sf)
                .commit();

        mHostPreference = (EditTextPreference) sf.findPreference(SettingsFragment.KEY_PREF_HOST);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mHostPreference.setSummary(sharedPref.getString(SettingsFragment.KEY_PREF_HOST, ""));
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    public void onResume() {
        super.onResume();

        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    public void onPause() {
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(SettingsFragment.KEY_PREF_HOST)) {
            mHostPreference.setSummary(sharedPreferences.getString(SettingsFragment.KEY_PREF_HOST, ""));
        }
    }
}
