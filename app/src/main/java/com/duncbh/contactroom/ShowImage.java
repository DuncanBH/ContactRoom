package com.duncbh.contactroom;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.duncbh.contactroom.data.ImageRetriever;

public class ShowImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ImageView showImageView = findViewById(R.id.show_imageview);

        //Drawable image =
        new ImageRetriever().getImage(imageBitmap -> {
            showImageView.setImageDrawable(imageBitmap);
        }, showImageView, this.getApplicationContext());
    }

}