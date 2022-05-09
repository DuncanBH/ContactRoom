package com.duncbh.contactroom.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.duncbh.contactroom.controller.AppController;

import org.json.JSONException;

public class AgeGenderRetriever {
    String ageBaseUrl = "https://api.agify.io?name=";
    String genderBaseUrl = "https://api.genderize.io?name=";
    int ageResult;
    String genderResult;

    public int getAge(AgeAsyncResponse callback, String name) {
        String apiUrl = ageBaseUrl + name;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        int age = response.getInt("age");
                        Log.d("TESTING", "getAge: " + age);
                        ageResult = age;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (null != callback)
                        callback.processFinished(ageResult);
                },
                error -> {
                }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        Log.d("TESTING", "getAge2: " + ageResult);
        return ageResult;
    }
    public String getGender(GenderAsyncResponse callback, String name) {
        String apiUrl = genderBaseUrl + name;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        genderResult = response.getString("gender");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (null != callback)
                        callback.processFinished(genderResult);
                },
                error -> {
                }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return genderResult;
    }


}
