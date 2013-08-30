package com.nor1.example.containers;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexwilczewski on 8/30/13.
 */
public class Storage {
    private static Storage instance;

    private Map<Integer, Object> store;

    private Storage() {
        this.store = new HashMap<Integer, Object>();
    }

    public static Storage getInstance() {
        if(instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void store(int key, Object obj) {
        this.store.put(key, obj);
    }

    public Object get(int key) {
        return this.store.get(key);
    }
}
