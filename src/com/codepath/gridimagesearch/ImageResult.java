package com.codepath.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
    private String fullUrl;
    private String thumbUrl;
    
    public ImageResult(JSONObject jsonObject) {
        try {
            this.fullUrl = jsonObject.getString("url");
            this.thumbUrl = jsonObject.getString("tbUrl");
        } catch (JSONException e) {
            this.fullUrl = null;
            this.thumbUrl = null;
        }
    }
    
    public String getFullUrl() {
        return fullUrl;
    }
    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }
    public String getThumbUrl() {
        return thumbUrl;
    }
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    
    public String toString() {
        return this.thumbUrl;
    }

    public static ArrayList<ImageResult> fromJSONArray(
            JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
