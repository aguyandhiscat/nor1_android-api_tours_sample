package com.nor1.example._api;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nor1.example.containers.DetailedTour;
import com.nor1.example.containers.Tour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alexwilczewski on 8/27/13.
 */
public class Nor1Api {
    private final String TAG = "Nor1Sample:Nor1Api";

    private static Nor1Api instance;
    private final String uri = "http://trip.nor1solutions.com/api/v1/";
    private String APIKEY;
    private boolean hasSetApiKey;

    private Nor1Api() {
        this.APIKEY = null;
        this.hasSetApiKey = false;
    }

    public static Nor1Api getInstance() {
        if(instance == null) {
            instance = new Nor1Api();
        }
        return instance;
    }

    public void setApiKey(String key) {
        this.APIKEY = key;
        this.hasSetApiKey = true;
    }

    /** Webservices **/

    /**
     * Performs a search for tours on the parameters passed.
     *
     * Returns AsyncHttpClient for additional modifications to the Request Call.
     *
     * @param address
     * @param fromDate
     * @param toDate
     * @param handler
     * @return AsyncHttpClient
     */
    public AsyncHttpClient search(String address, String fromDate, String toDate, JsonHttpResponseHandler handler) {
        if(!this.hasSetApiKey) {
            return null;
        }

        RequestParams params = new RequestParams();
        params.put("address", address);
        params.put("from_date", fromDate);
        params.put("to_date", toDate);

        String url = uri + "search/";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, handler);

        return client;
    }

    /**
     * Performs a lookup on an individual tour.
     *
     * Returns AsyncHttpClient for additional modifications to the Request Call.
     *
     * @param trip_reference
     * @param handler
     * @return AsyncHttpClient
     */
    public AsyncHttpClient find(String trip_reference, JsonHttpResponseHandler handler) {
        if(!this.hasSetApiKey) {
            return null;
        }

        String url = uri + String.format("tour/%s", trip_reference);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, handler);

        return client;
    }

    /** End Webservices **/

    /** Result Handling **/

    public List<Tour> getToursFromSearchResults(JSONObject timeline) {
        List<Tour> list = new ArrayList<Tour>();

        try {
            JSONArray jsonArrTours = timeline.getJSONArray("filtered_detailed_tours");
            int len = jsonArrTours.length();

            for(int i=0; i<len; i++) {
                JSONObject jsonObjTour = jsonArrTours.getJSONObject(i);

                Tour tour = new Tour();
                tour.reference = jsonObjTour.getString("flextrip_reference");
                tour.thumbnailUrl = jsonObjTour.getString("primary_image");
                tour.title = jsonObjTour.getString("name");
                tour.description = jsonObjTour.getString("description_short");

                list.add(tour);
            }
        } catch(JSONException e) {
            Log.e(TAG, "JSON Exception", e);
        }

        return list;
    }

    public DetailedTour getDetailedTourFromFind(JSONObject timeline) {
        DetailedTour detailedTour = new DetailedTour();

        try {
            // Parse through top-level details
            detailedTour.name = timeline.getString("name");
            detailedTour.descriptionLong = timeline.getString("description_long");

            // Parse through media / images
            JSONArray jsonArrMedia = timeline.getJSONArray("media");
            int len = jsonArrMedia.length();
            List<DetailedTour.ImageItem> images = new ArrayList<DetailedTour.ImageItem>();

            for(int i=0; i<len; i++) {
                JSONObject jsonObjImageData = jsonArrMedia.getJSONObject(i);
                String description = jsonObjImageData.getString("description");
                JSONArray jsonArrImageVersions = jsonObjImageData.getJSONArray("versions");
                if(jsonArrImageVersions.length() > 0) {
                    String imageUrl = jsonArrImageVersions.getJSONObject(0).getString("url");

                    DetailedTour.ImageItem imageItem = detailedTour.new ImageItem();
                    imageItem.imageTitle = description;
                    imageItem.imageUrl = imageUrl;
                    images.add(imageItem);
                }
            }
            detailedTour.images = images;


            // Parse through schedules / prices
            JSONObject jsonObjSchedules = timeline.getJSONObject("schedules");

            Iterator<String> itr = jsonObjSchedules.keys();
            List<DetailedTour.ScheduleItem> schedules = new ArrayList<DetailedTour.ScheduleItem>();

            while(itr.hasNext()) {
                String key = itr.next();

                JSONObject jsonObjSchedule = jsonObjSchedules.getJSONObject(key);

                DetailedTour.ScheduleItem scheduleItem = detailedTour.new ScheduleItem();
                scheduleItem.id = key;
                scheduleItem.title = jsonObjSchedule.getString("title");
                scheduleItem.type = jsonObjSchedule.getString("type");
                scheduleItem.description = jsonObjSchedule.getString("description");
                scheduleItem.commentary = jsonObjSchedule.getString("commentary");
                scheduleItem.from = jsonObjSchedule.getString("from");
                scheduleItem.to = jsonObjSchedule.getString("to");
                scheduleItem.day_hash = jsonObjSchedule.getString("day_hash");
                scheduleItem.first_service = jsonObjSchedule.getString("first_service");
                scheduleItem.last_service = jsonObjSchedule.getString("last_service");
                scheduleItem.interval = jsonObjSchedule.getString("interval");
                scheduleItem.departure_point = jsonObjSchedule.getString("departure_point");
                scheduleItem.time = jsonObjSchedule.getString("time");
                scheduleItem.duration = jsonObjSchedule.getString("duration");
                scheduleItem.duration_minutes = jsonObjSchedule.getString("duration_minutes");
                scheduleItem.frequency = jsonObjSchedule.getString("frequency");
                scheduleItem.pickup_required = jsonObjSchedule.getString("pickup_required");
                scheduleItem.dropoff_required = jsonObjSchedule.getString("dropoff_required");
                schedules.add(scheduleItem);
            }
            detailedTour.schedules = schedules;
        } catch(JSONException e) {
            Log.e(TAG, "JSON Exception", e);
        }

        return detailedTour;
    }

    /** End Result Handling **/
}
