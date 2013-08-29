package com.nor1.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

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
                t.thumbnailUrl = tour.getString("primary_image");
                t.title = tour.getString("name");
                t.description = tour.getString("description_short");

                list.add(t);
            }
        } catch(JSONException e) { }

        // get data from the table by the ListAdapter
        ListAdapter adapter = new TripListAdapter(this, list);

        mTripList.setAdapter(adapter);
    }
}
