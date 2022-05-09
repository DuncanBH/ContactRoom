package com.duncbh.contactroom;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.duncbh.contactroom.data.ImageRetriever;

public class ShowImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ImageView showImageView = findViewById(R.id.show_imageview);
        TextView textView = findViewById(R.id.show_url_textview);

        new ImageRetriever().getImage(imageUrl -> {
            textView.setText(imageUrl);
        }, showImageView, this.getApplicationContext());

    }

}
