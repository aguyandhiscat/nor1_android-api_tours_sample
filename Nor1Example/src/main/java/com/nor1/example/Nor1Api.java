package com.nor1.example;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by alexwilczewski on 8/27/13.
 */
public class Nor1Api {
    private final String TAG = "Nor1Sample:Nor1Api";

    private final String uri = "http://trip.nor1solutions.com/api/v1/";
    private String APIKEY;

    public Nor1Api(String ApiKey) {
        this.APIKEY = ApiKey;
    }

    public void search(String address, String fromDate, String toDate, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("address", address);
        params.put("from_date", fromDate);
        params.put("to_date", toDate);

        String url = uri + "search/";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, handler);
    }
}
