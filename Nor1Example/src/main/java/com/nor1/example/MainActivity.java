package com.nor1.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
    private final String TAG = "Nor1Example:MainActivity";

    private final int INTENT_SETTINGS_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String hostPref = sharedPref.getString(SettingsFragment.KEY_PREF_HOST, "");

        Log.d(TAG, "Host: " + hostPref);
    }

    protected void onResume() {
        super.onResume();

        // Do stuff
    }

    protected void onPause() {
        // Do stuff

        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch(item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case INTENT_SETTINGS_ACTIVITY:
                // RESULT_OK, RESULT_CANCELED
                Log.d(TAG, "Returned from Settings activity");
        }
    }

    protected void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, INTENT_SETTINGS_ACTIVITY);
    }
    
}
