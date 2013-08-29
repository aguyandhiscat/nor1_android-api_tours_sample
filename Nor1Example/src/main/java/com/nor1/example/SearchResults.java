package com.nor1.example;

import org.json.JSONObject;

/**
 * Created by alexwilczewski on 8/28/13.
 */
public class SearchResults {
    private static SearchResults instance;

    private JSONObject results;

    private SearchResults() {

    }

    public static SearchResults getInstance() {
        if(instance == null) {
            instance = new SearchResults();
        }
        return instance;
    }

    public void setResults(JSONObject json) {
        this.results = json;
    }

    public JSONObject getResults() {
        return this.results;
    }
}
