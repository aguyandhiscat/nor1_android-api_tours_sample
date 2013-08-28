package com.nor1.example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

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

        Tour t = new Tour();
        t.description = "This is a test description";
        t.title = "Test tour";
        t.thumbnailUrl = "http://media.tripapi.com/FLX-75FB-61A9-0BF/thumb_b0ae6f84bb8199ac9bdd9cf7d6df6cc0.jpg";
        list.add(t);
        // get data from the table by the ListAdapter
        ListAdapter adapter = new TripListAdapter(this, list);

        mTripList.setAdapter(adapter);
    }
}
