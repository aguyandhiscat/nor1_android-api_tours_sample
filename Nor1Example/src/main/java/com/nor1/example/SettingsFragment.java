package com.nor1.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by alexwilczewski on 8/26/13.
 */
public class SettingsFragment extends PreferenceFragment {
    public static final String KEY_PREF_HOST = "pref_host";

    private EditTextPreference mHostPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((SettingsActivity) getActivity()).init();
    }

    public static String getPreferenceHost(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(SettingsFragment.KEY_PREF_HOST, "");
    }
}
