package com.nor1.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nor1.example.adapter.TripListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexwilczewski on 8/27/13.
 */
public class TripListActivity extends Activity {
    private final String TAG = "Nor1Example:MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        ListView mTripList = (ListView) findViewById(R.id.trip_list);

        List<Tour> list = new ArrayList<Tour>();

        try {
            JSONArray tours = SearchResults.getInstance().getResults().getJSONArray("filtered_detailed_tours");
            int len = tours.length();

            for(int i=0; i<len; i++) {
                JSONObject tour = tours.getJSONObject(i);

                Tour t = new Tour();
                t.reference = tour.getString("flextrip_reference");
                t.thumbnailUrl = tour.getString("primary_image");
                t.title = tour.getString("name");
                t.description = tour.getString("description_short");

                list.add(t);
            }
        } catch(JSONException e) {
            Log.e(TAG, "Json Issue", e);
        }

        // get data from the table by the ListAdapter
        ListAdapter adapter = new TripListAdapter(this, list);
        mTripList.setAdapter(adapter);

        mTripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Goto Details view
                ListAdapter adapter = (ListAdapter) adapterView.getAdapter();
                Tour t = (Tour) adapter.getItem(i);
                openTripDetails(t);
            }
        });
    }

    protected void openTripDetails(Tour t) {
        Intent intent = new Intent(this, TripDetailsActivity.class);
        intent.putExtra("trip_reference", t.reference);
        startActivity(intent);
    }
}
