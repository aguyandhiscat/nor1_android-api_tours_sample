package com.nor1.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nor1.example._api.Nor1Api;
import com.nor1.example.R;
import com.nor1.example.containers.Storage;

import org.json.JSONObject;

public class MainActivity extends Activity {
    private final String TAG = "Nor1Example:MainActivity";

    public static final int STORAGE_SEARCH_RESULTS = 1;

    private EditText mEditAddress;
    private EditText mEditFromDate;
    private EditText mEditToDate;
    private boolean responseCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Nor1Api.getInstance().setApiKey(this.getString(R.string.api_key));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Called after onCreate, sets up layout here.
        layoutToSearch();
    }

    public void layoutToSearch() {
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
    }

    public void layoutToLoading() {
        mEditAddress = mEditFromDate = mEditToDate = null;
        setContentView(R.layout.loading);
    }

    public void search() {
        String mAddress = mEditAddress.getText().toString();
        String mFromDate = mEditFromDate.getText().toString();
        String mToDate = mEditToDate.getText().toString();

        layoutToLoading();

        // Using a flag because the HttpResponseHandler library does not handle success/failure predictably
        responseCompleted = false;

        AsyncHttpClient client = Nor1Api.getInstance().search(mAddress, mFromDate, mToDate, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject timeline) {
                responseCompleted = true;
                Storage.getInstance().store(MainActivity.STORAGE_SEARCH_RESULTS, timeline);
                startTripListActivity();
            }

            @Override
            public void onFailure(Throwable e, String response) {
                responseCompleted = true;
                layoutToSearch();
                displayError(e.getMessage());
            }

            public void onFinish() {
                if(!responseCompleted) {
                    layoutToSearch();
                    displayError("Message did not go through.");
                }
            }
        });

        if(client == null) {
            displayError("ApiKey Not Set");
            layoutToSearch();
        }
    }

    public void displayError(String msg) {
        Toast.makeText(this, msg, 2000).show();
    }

    protected void startTripListActivity() {
        Intent intent = new Intent(this, TripListActivity.class);
        startActivity(intent);
    }
}
