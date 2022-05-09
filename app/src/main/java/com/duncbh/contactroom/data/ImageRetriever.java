package com.duncbh.contactroom.data;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.duncbh.contactroom.controller.AppController;

import org.json.JSONException;

import java.io.IOException;

public class ImageRetriever {
    String apiUrl = "https://dog.ceo/api/breeds/image/random";
    String imageUrl;

    public String getImage(ImageAsyncResponse callback, ImageView imageView, Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        imageUrl = response.getString("message");

                        Glide.with(context)
                                .load(imageUrl)
                                .into(imageView);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (null != callback)
                        callback.processFinished(imageUrl);
                },
                error -> {
                }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return imageUrl;
    }
}
