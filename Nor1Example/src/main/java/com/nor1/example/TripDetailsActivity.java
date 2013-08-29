package com.nor1.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexwilczewski on 8/29/13.
 */
public class TripDetailsActivity extends Activity {
    private final String TAG = "Nor1sample:TripDetailsActivity";

    public static final String INTENT_EXTRA_TRIP_REFERENCE = "trip_reference";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String trip_reference = intent.getStringExtra(INTENT_EXTRA_TRIP_REFERENCE);
        if(trip_reference == null) {
            // Close if no trip reference
            finish();
        }

        setContentView(R.layout.loading);

        Nor1Api mApi = new Nor1Api(this.getString(R.string.api_key));

        mApi.find(trip_reference, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject timeline) {
                Log.d(TAG, "OnSuccess, JsonObject");
                showDetails(timeline);
            }

            @Override
            public void onFailure(Throwable e, String response) {
                Log.d(TAG, "OnFailure, String");
                displayError(e);
                finish();
            }
        });
    }

    protected void showDetails(JSONObject timeline) {
        setContentView(R.layout.activity_trip_details);

        TextView textView;
        try {
            textView = (TextView) findViewById(R.id.detail_title);
            textView.setText(timeline.getString("name"));

            textView = (TextView) findViewById(R.id.detail_description);
            textView.setText(timeline.getString("description_long"));

            ListView mediaListView = (ListView) findViewById(R.id.detail_media_list);
            setUpMediaList(mediaListView, timeline.getJSONArray("media"));

            ListView schedulesListView = (ListView) findViewById(R.id.detail_schedules_list);
            setUpSchedulesList(schedulesListView, timeline.getJSONObject("detail_schedules_list"));
        } catch(JSONException e) {
            Log.e(TAG, "Json exception", e);
        }
    }

    public void displayError(Throwable e) {
        Toast.makeText(this, e.getMessage(), 2000).show();
    }

    protected void setUpMediaList(ListView mediaListView, JSONArray media) {

    }

    protected void setUpSchedulesList(ListView schedulesListView, JSONObject schedules) {

    }
}
