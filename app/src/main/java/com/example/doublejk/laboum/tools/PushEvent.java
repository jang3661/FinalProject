package com.example.doublejk.laboum.tools;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by doublejk on 2017-08-27.
 */

public class PushEvent {
    private Map<String, String> pushData;
    public PushEvent(Map<String, String> pushData) {
        this.pushData = pushData;
    }

    public Map<String, String> getPushData() {
        return pushData;
    }
}
