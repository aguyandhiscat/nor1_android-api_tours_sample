package com.nor1.example.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nor1.example._api.Nor1Api;
import com.nor1.example.R;
import com.nor1.example.containers.DetailedTour;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

        AsyncHttpClient client = Nor1Api.getInstance().find(trip_reference, new JsonHttpResponseHandler() {
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

        DetailedTour detailedTour = Nor1Api.getInstance().getDetailedTourFromFind(timeline);

        ((TextView) findViewById(R.id.detail_title)).setText(detailedTour.name);
        ((TextView) findViewById(R.id.detail_description)).setText(detailedTour.descriptionLong);

        ViewGroup mediaView = (LinearLayout) findViewById(R.id.detail_media_list);
        setUpMediaList(mediaView, detailedTour.images);

        ViewGroup schedulesView = (LinearLayout) findViewById(R.id.detail_schedules_list);
        setUpSchedulesList(schedulesView, detailedTour.schedules);
    }

    public void displayError(Throwable e) {
        Toast.makeText(this, e.getMessage(), 2000).show();
    }

    protected void setUpMediaList(ViewGroup mediaView, List<DetailedTour.ImageItem> images) {
        Iterator<DetailedTour.ImageItem> itr = images.iterator();
        while(itr.hasNext()) {
            DetailedTour.ImageItem imageItem = itr.next();
            View viewElem = View.inflate(this, R.layout.list_item_media, null);

            ((TextView) viewElem.findViewById(R.id.item_media_title)).setText(imageItem.imageTitle);
            ImageView thumbnail = (ImageView) viewElem.findViewById(R.id.item_media_image);
            thumbnail.setTag(imageItem.imageUrl);
            new DownloadImageTask().execute(thumbnail);

            mediaView.addView(viewElem);
        }
    }

    protected void setUpSchedulesList(ViewGroup schedulesView, List<DetailedTour.ScheduleItem> schedules) {
        Iterator<DetailedTour.ScheduleItem> itr = schedules.iterator();
        while(itr.hasNext()) {
            DetailedTour.ScheduleItem scheduleItem = itr.next();
            View viewElem = View.inflate(this, R.layout.list_item_schedule, null);

            ((TextView) viewElem.findViewById(R.id.item_schedule_key)).setText(scheduleItem.id);
            ((TextView) viewElem.findViewById(R.id.item_schedule_title)).setText("Title: "+scheduleItem.title);
            ((TextView) viewElem.findViewById(R.id.item_schedule_type)).setText("Type: "+scheduleItem.type);
            ((TextView) viewElem.findViewById(R.id.item_schedule_description)).setText("Description: "+scheduleItem.description);
            ((TextView) viewElem.findViewById(R.id.item_schedule_commentary)).setText("Commentary: "+scheduleItem.commentary);
            ((TextView) viewElem.findViewById(R.id.item_schedule_from_date)).setText("From: "+scheduleItem.from);
            ((TextView) viewElem.findViewById(R.id.item_schedule_to_date)).setText("To: "+scheduleItem.to);
            ((TextView) viewElem.findViewById(R.id.item_schedule_day_hash)).setText("Day Hash: "+scheduleItem.day_hash);
            ((TextView) viewElem.findViewById(R.id.item_schedule_first_service)).setText("First Service: "+scheduleItem.first_service);
            ((TextView) viewElem.findViewById(R.id.item_schedule_last_service)).setText("Last Service: "+scheduleItem.last_service);
            ((TextView) viewElem.findViewById(R.id.item_schedule_interval)).setText("Interval: "+scheduleItem.interval);
            ((TextView) viewElem.findViewById(R.id.item_schedule_departure_point)).setText("Departure Point: "+scheduleItem.departure_point);
            ((TextView) viewElem.findViewById(R.id.item_schedule_time)).setText("Time: "+scheduleItem.time);
            ((TextView) viewElem.findViewById(R.id.item_schedule_duration)).setText("Duration: "+scheduleItem.duration);
            ((TextView) viewElem.findViewById(R.id.item_schedule_duration_minutes)).setText("Duration Minutes: "+scheduleItem.duration_minutes);
            ((TextView) viewElem.findViewById(R.id.item_schedule_frequency)).setText("Frequency: "+scheduleItem.frequency);
            ((TextView) viewElem.findViewById(R.id.item_schedule_pickup_required)).setText("Pickup Required: "+scheduleItem.pickup_required);
            ((TextView) viewElem.findViewById(R.id.item_schedule_dropoff_required)).setText("Dropoff Required: "+scheduleItem.dropoff_required);

            schedulesView.addView(viewElem);
        }
    }

    public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {
        ImageView imageView = null;

        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return downloadImage((String)imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap downloadImage(String url) {
            try {
                URL newurl = new URL(url);
                return BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            }
            catch(MalformedURLException e) { }
            catch(IOException e) { }

            // In case something happened with image loading, return a 1x1 transparent image
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
        }
    }
}
