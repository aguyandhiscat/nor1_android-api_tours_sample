package com.nor1.example;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nor1.example.containers.ImageItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

            ViewGroup mediaView = (LinearLayout) findViewById(R.id.detail_media_list);
            setUpMediaList(mediaView, timeline.getJSONArray("media"));

            ViewGroup schedulesView = (LinearLayout) findViewById(R.id.detail_schedules_list);
            setUpSchedulesList(schedulesView, timeline.getJSONObject("detail_schedules_list"));
        } catch(JSONException e) {
            Log.e(TAG, "Json exception", e);
        }
    }

    public void displayError(Throwable e) {
        Toast.makeText(this, e.getMessage(), 2000).show();
    }

    protected void setUpMediaList(ViewGroup mediaView, JSONArray media) {
        int len = media.length();
        List<ImageItem> list = new ArrayList<ImageItem>();

        for(int i=0; i<len; i++) {
            try {
                JSONObject imageData = media.getJSONObject(i);
                String description = imageData.getString("description");
                JSONArray imageVersions = imageData.getJSONArray("versions");
                if(imageVersions.length() > 0) {
                    String imageUrl = imageVersions.getJSONObject(0).getString("url");

                    View viewElem = View.inflate(this, R.layout.list_item_media, null);
                    TextView title = (TextView) viewElem.findViewById(R.id.item_media_title);
                    title.setText(description);
                    ImageView thumbnail = (ImageView) viewElem.findViewById(R.id.item_media_image);
                    thumbnail.setTag(imageUrl);
                    new DownloadImageTask().execute(thumbnail);

                    mediaView.addView(viewElem);

                    ImageItem ii = new ImageItem();
                    ii.imageTitle = description;
                    ii.imageUrl = imageUrl;

                    list.add(ii);
                }
            } catch(JSONException e) {
                Log.e(TAG, "JSON Exception", e);
            }
        }
    }

    protected void setUpSchedulesList(ViewGroup schedulesView, JSONObject schedules) {
        int len = schedules.length();
        List<ImageItem> list = new ArrayList<ImageItem>();

//        for(int i=0; i<len; i++) {
//            try {
//                JSONObject imageData = media.getJSONObject(i);
//                String description = imageData.getString("description");
//                JSONArray imageVersions = imageData.getJSONArray("versions");
//                if(imageVersions.length() > 0) {
//                    String imageUrl = imageVersions.getJSONObject(0).getString("url");
//
//                    View viewElem = View.inflate(this, R.layout.list_item_media, null);
//                    TextView title = (TextView) viewElem.findViewById(R.id.item_media_title);
//                    title.setText(description);
//                    ImageView thumbnail = (ImageView) viewElem.findViewById(R.id.item_media_image);
//                    thumbnail.setTag(imageUrl);
//                    new DownloadImageTask().execute(thumbnail);
//
//                    mediaView.addView(viewElem);
//
//                    ImageItem ii = new ImageItem();
//                    ii.imageTitle = description;
//                    ii.imageUrl = imageUrl;
//
//                    list.add(ii);
//                }
//            } catch(JSONException e) {
//                Log.e(TAG, "JSON Exception", e);
//            }
//        }
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
            } catch(MalformedURLException e) {

            } catch(IOException e) {

            }
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
        }
    }
}
