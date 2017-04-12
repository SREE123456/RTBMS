package com.example.hp.roughpartial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ParseJSON {

    //private String myJSONString;

    private static final String JSON_ARRAY = "result";
    private static final String ID = "id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    String latitude,longitude,id;

    private JSONArray users = null;

    private int TRACK = 0;

    public void extractJSON(String myJSONString) {
        try {
            JSONObject jsonObject = new JSONObject(myJSONString);
            users = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            moveNext();
        }
    }

    private void moveNext() {
        while (TRACK < users.length()) {
            TRACK++;
            showData();
        }
    }

    private void showData() {
        try {
            JSONObject jsonObject = users.getJSONObject(TRACK);
            if(jsonObject.getString(LATITUDE)!=null && jsonObject.getString(LONGITUDE)!=null) {
                id = jsonObject.getString(ID);
                latitude = jsonObject.getString(LATITUDE);
                longitude = jsonObject.getString(LONGITUDE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String giveLatitude(){
        return latitude;
    }
    public String giveLongitude(){
        return longitude;
    }
}