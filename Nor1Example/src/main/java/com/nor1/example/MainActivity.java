package com.nor1.example;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private final String TAG = "Nor1Example:MainActivity";

    private EditText mEditAddress;
    private EditText mEditFromDate;
    private EditText mEditToDate;

    private Nor1Api mApi;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditAddress    = (EditText) findViewById(R.id.address);
        mEditFromDate   = (EditText) findViewById(R.id.from_date);
        mEditToDate     = (EditText) findViewById(R.id.to_date);

        Button searchBtn = (Button) findViewById(R.id.search_submit);
        searchBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                search();
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
            public void onSuccess(JSONObject timeline) {
                Log.d(TAG, "OnSuccess, JsonObject");
                // Go to list Activity
                // Pass timeline data through Intent
                SearchResults.getInstance().setResults(timeline);
                openTripList();
            }

            @Override
            public void onFailure(Throwable e, String response) {
                Log.d(TAG, "OnFailure, String");
                displayError(e);
            }
        });
    }

    public void displayError(Throwable e) {
        Toast.makeText(this, e.getMessage(), 2000).show();
    }

    protected void openTripList() {
        Intent intent = new Intent(this, TripListActivity.class);
        startActivity(intent);
    }
}
