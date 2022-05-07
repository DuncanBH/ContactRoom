package com.duncbh.contactroom.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.duncbh.contactroom.controller.AppController;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageRetriever {
    Drawable image;
    String apiUrl = "https://dog.ceo/api/breeds/image/random";

    public void getImage(final ImageBmpAsyncResponse callblack, ImageView imageView, Context context) {
    //public Drawable getImage(final ImageBmpAsyncResponse callblack, ImageView imageView, Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        String imageUrl = response.getString("message");

                        //Get image as bmp
                        try {
                            URL url;
                            url = new URL(imageUrl);

                            Glide.with(context)
                                    .load(imageUrl)
                                    .into(imageView);

//                            InputStream input = (InputStream) url.getContent();
//                            Drawable d = Drawable.createFromStream(input, "apiImage");
//                            image = d;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    if (null != callblack)
//                        callblack.processFinished(image);
                },
                error -> {
                }
        );
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }
}
