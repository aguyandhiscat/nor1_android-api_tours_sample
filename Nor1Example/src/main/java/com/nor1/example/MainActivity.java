package com.nor1.example;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private final String TAG = "Nor1Example:MainActivity";

    private final int INTENT_SETTINGS_ACTIVITY = 1;
    private final int INTENT_TRIP_LIST_ACTIVITY = 2;

    private EditText mEditAddress;
    private EditText mEditLatitude;
    private EditText mEditLongitude;
    private EditText mEditFromDate;
    private EditText mEditToDate;
    private EditText mEditCategories;

    private Nor1Api mApi;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditAddress    = (EditText) findViewById(R.id.address);
        mEditFromDate   = (EditText) findViewById(R.id.from_date);
        mEditToDate     = (EditText) findViewById(R.id.to_date);

//        mEditLatitude   = (EditText) findViewById(R.id.latitude);
//        mEditLongitude  = (EditText) findViewById(R.id.longitude);
//        mEditCategories = (EditText) findViewById(R.id.categories);

        Button searchBtn = (Button) findViewById(R.id.search_submit);
        searchBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                openTripList();
            }
        });

        mApi = new Nor1Api(this.getString(R.string.api_key));
    }

    public void search() {
        String mAddress = mEditAddress.getText().toString();
        String mFromDate = mEditFromDate.getText().toString();
        String mToDate = mEditToDate.getText().toString();

        mApi.search(mAddress, mFromDate, mToDate, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                // Set up a spinner or loading
            }

            @Override
            public void onFinish() {
                // Kill spinner
            }

            @Override
            public void onSuccess(JSONObject timeline) {
                Log.d(TAG, "OnSuccess, JsonObject");
                // Go to list Activity
                // Pass timeline data through Intent

            }

            @Override
            public void onFailure(Throwable e, String response) {
                Log.d(TAG, "OnFailure, String");
                displayError(e);
            }
        });
    }

    protected void onResume() {
        super.onResume();

        // Do stuff
    }

    protected void onPause() {
        // Do stuff

        super.onPause();
    }

    public void displayError(Throwable e) {
        Toast.makeText(this, e.getMessage(), 2000).show();
    }

    // Settings
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

    protected void openTripList() {
        Intent intent = new Intent(this, TripListActivity.class);
        startActivityForResult(intent, INTENT_TRIP_LIST_ACTIVITY);
    }
}
