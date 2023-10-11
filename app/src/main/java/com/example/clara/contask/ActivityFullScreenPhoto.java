package com.example.clara.contask;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public  class ActivityFullScreenPhoto extends AppCompatActivity {


    private ImageView imageFullScreen;

    private CircleImageView btnImageSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_photo);

        imageFullScreen = findViewById(R.id.imageFullScreen);
        btnImageSize= findViewById(R.id.btn_image_size);

        String photoUrl = getIntent().getExtras().getString("photoUrl", null);
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(imageFullScreen);
        }

        btnImageSize.setOnClickListener(v -> {
            if(imageFullScreen.getScaleType()==ImageView.ScaleType.FIT_CENTER){
                imageFullScreen.setScaleType(ImageView.ScaleType.FIT_XY);

            }
            else {
                imageFullScreen.setScaleType(ImageView.ScaleType.FIT_CENTER);

            }
        });
    }


}