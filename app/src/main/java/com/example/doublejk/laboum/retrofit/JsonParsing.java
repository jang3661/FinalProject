package com.example.doublejk.laboum.retrofit;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by doublejk on 2017-08-09.
 */

public class JsonParsing {
    JsonObject jsonObject;
    ArrayList<SearchItem> searchItems;

    public JsonParsing(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        searchItems = new ArrayList<>();
    }

    public ArrayList<SearchItem> parsing() {
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < jsonArray.size(); i++) {
            SearchItem searchItem = new SearchItem();
            if (jsonArray.get(i).getAsJsonObject().get("id")
                    .getAsJsonObject().get("kind").toString().equals("\"youtube#video\"")) {
                searchItem.setVideoId(subString(jsonArray.get(i).getAsJsonObject().get("id")
                        .getAsJsonObject().get("videoId").toString()));
                searchItem.setTitle(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                        .getAsJsonObject().get("title").toString()));
                searchItem.setImgUrl(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                        .getAsJsonObject().get("thumbnails").getAsJsonObject().get("medium")
                        .getAsJsonObject().get("url").toString()));
                searchItems.add(searchItem);
            }
        }
        return searchItems;
    }

    public ArrayList<SearchItem> popularSearchParsing() {
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");
        for (int i = 0; i < jsonArray.size(); i++) {
            SearchItem searchItem = new SearchItem();
            searchItem.setVideoId(subString(jsonArray.get(i).getAsJsonObject().get("id").toString()));
            searchItem.setTitle(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                    .getAsJsonObject().get("title").toString()));
            searchItem.setImgUrl(subString(jsonArray.get(i).getAsJsonObject().get("snippet")
                    .getAsJsonObject().get("thumbnails").getAsJsonObject().get("medium")
                    .getAsJsonObject().get("url").toString()));
            searchItems.add(searchItem);
        }
        return searchItems;
    }

    public String subString(String s) {
        return s.substring(1, s.length() - 1);
    }
}
