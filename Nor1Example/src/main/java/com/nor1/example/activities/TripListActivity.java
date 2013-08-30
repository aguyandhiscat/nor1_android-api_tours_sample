package com.nor1.example.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nor1.example.R;
import com.nor1.example._api.Nor1Api;
import com.nor1.example.adapter.TripListAdapter;
import com.nor1.example.containers.Storage;
import com.nor1.example.containers.Tour;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by alexwilczewski on 8/27/13.
 */
public class TripListActivity extends Activity {
    private final String TAG = "Nor1Example:TripListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        List<Tour> list = Nor1Api.getInstance().getToursFromSearchResults(
                (JSONObject) Storage.getInstance().get(MainActivity.STORAGE_SEARCH_RESULTS)
        );

        // Set up ListView
        ListView mTripList = (ListView) findViewById(R.id.trip_list);
        ListAdapter adapter = new TripListAdapter(this, list);
        mTripList.setAdapter(adapter);

        mTripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Goto Details view
                ListAdapter adapter = (ListAdapter) adapterView.getAdapter();
                Tour t = (Tour) adapter.getItem(i);
                openTripDetailsActivity(t);
            }
        });
    }

    protected void openTripDetailsActivity(Tour t) {
        Intent intent = new Intent(this, TripDetailsActivity.class);
        intent.putExtra("trip_reference", t.reference);
        startActivity(intent);
    }
}
